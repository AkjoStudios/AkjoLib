package io.github.akjo03.lib.functional.async;

import io.github.akjo03.lib.functional.Cause;
import io.github.akjo03.lib.functional.Functions;
import io.github.akjo03.lib.functional.Result;
import io.github.akjo03.lib.functional.Tuple;
import io.github.akjo03.lib.io.OperationException;
import io.github.akjo03.lib.io.Timeout;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.List;
import java.util.function.Consumer;

import static io.github.akjo03.lib.functional.util.ActionableThreshold.threshold;
import static io.github.akjo03.lib.functional.util.ResultCollector.resultCollector;
import static io.github.akjo03.lib.functional.Functions.*;
import static io.github.akjo03.lib.functional.Tuple.*;
import static io.github.akjo03.lib.functional.async.PromiseMappers.*;

/*
 *  Copyright (c) 2023 Sergiy Yevtushenko.
 *  Copyright (c) 2024 Lukas Küffer.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  -------------------
 *
 *  This code was ported and modified from the project Pragmatica.
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/Promise.java
 */
@SuppressWarnings({"unused", "java:S125", "UnusedReturnValue"})
public interface Promise<T> {
    // ----- Promise Implementation -----

    @SuppressWarnings({"java:S1117", "java:S3077"})
    final class PromiseImpl<T> implements Promise<T> {
        @RequiredArgsConstructor
        private static class CompletionAction<T> {
            private volatile CompletionAction<T> next;
            private final Consumer<Result<T>> action;
            private final PromiseImpl<?> dependency;

            @Override
            @SuppressWarnings("java:S3358")
            public @NotNull String toString() {
                return this == NOP
                        ? "NOP"
                        : "Action(" + (dependency == null ? "free" : dependency.toString()) + ")";
            }
        }

        @SuppressWarnings("rawtypes")
        private static final CompletionAction NOP = new CompletionAction<>(Functions::unit, null);

        @SuppressWarnings({"unchecked", "java:S1170"})
        private final CompletionAction<T> head = NOP;
        private volatile CompletionAction<T> processed;
        private final Result<T> value;

        private static final VarHandle headHandle;
        private static final VarHandle valueHandle;

        static {
            try {
                final var lookup = MethodHandles.lookup();
                headHandle = lookup.findVarHandle(PromiseImpl.class, "head", CompletionAction.class);
                valueHandle = lookup.findVarHandle(PromiseImpl.class, "value", Result.class);
            } catch (final ReflectiveOperationException e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        @Contract(pure = true)
        private PromiseImpl(Result<T> value) {
            this.value = value;
            this.processed = value == null ? null : this.head;
        }

        // ----- General Methods -----

        @SuppressWarnings("unchecked")
        private void complete(Result<T> result) {
            CompletionAction<T> processed = NOP;
            CompletionAction<T> head;

            while ((head = swapHead()) != null) {
                while (head != null) {
                    head.action.accept(result);
                    var current = head;
                    head = head.next;

                    if (current.dependency != null) {
                        current.next = processed;
                        processed = current;
                    }
                }
            }
            this.processed = processed;
        }

        private void push(@NotNull CompletionAction<T> newHead) {
            CompletionAction<T> oldHead;
            do {
                oldHead = head;
                newHead.next = oldHead;
            } while (!headHandle.compareAndSet(this, oldHead, newHead));
        }

        private CompletionAction<T> swapHead() {
            CompletionAction<T> head;

            do {
                head = this.head;
            } while (!headHandle.compareAndSet(this, head, NOP));

            CompletionAction<T> current = head;
            CompletionAction<T> previous = null;
            CompletionAction<T> next;

            while (current != NOP) {
                next = current.next;
                current.next = previous;
                previous = current;
                current = next;
            }

            return previous;
        }

        private Result<T> join(long delayNanos) {
            var start = System.nanoTime();

            CompletionAction<T> action;

            while ((action = processed) == null) {
                Thread.onSpinWait();

                if (System.nanoTime() - start > delayNanos) {
                    return OperationException.TIMEOUT.result();
                }
            }

            while (action != NOP) {
                var currentNanoTime = System.nanoTime();

                if (currentNanoTime - start > delayNanos) {
                    return OperationException.TIMEOUT.result();
                }

                action.dependency.join(currentNanoTime - start);
                action = action.next;
            }

            return value;
        }

        // ----- Consuming Methods (Blocking) -----

        @Override
        public Result<T> join() {
            CompletionAction<T> action;

            while ((action = processed) == null) {
                Thread.onSpinWait();
                Thread.yield();
            }

            while (action != NOP) {
                action.dependency.join();
                action = action.next;
            }

            return value;
        }

        @Override
        public Result<T> join(@NotNull Timeout timeout) {
            return join(timeout.nanoseconds());
        }

        // ----- Consuming Methods (Non-Blocking) -----

        @Contract("_ -> this")
        @Override
        public Promise<T> async(Consumer<Promise<T>> action) {
            runAsync(() -> action.accept(this));
            return this;
        }

        @Contract("_, _ -> this")
        @Override
        @SuppressWarnings({"java:S2142", "java:S108"})
        public Promise<T> async(Timeout timeout, Consumer<Promise<T>> action) {
            runAsync(() -> {
                try {
                    Thread.sleep(timeout.duration());
                } catch (InterruptedException ignored) {}
                action.accept(this);
            });
            return this;
        }

        @Contract("_ -> this")
        @Override
        public Promise<T> onResult(Consumer<Result<T>> action) {
            if (value != null) {
                action.accept(value);
            } else {
                push(new CompletionAction<>(action, null));
            }

            return this;
        }

        // ----- State Changing Methods -----

        @Contract("_ -> this")
        @Override
        public Promise<T> resolve(Result<T> result) {
            if (valueHandle.compareAndSet(this, null, value)) {
                runAsync(() -> complete(result));
            }

            return this;
        }

        // ----- Transformational Methods -----

        @Override
        public <U> @NotNull Promise<U> map(Function1<U, ? super T> mapper) {
            if (value != null) { return new PromiseImpl<>(value.map(mapper)); }

            var promise = new PromiseImpl<U>(null);

            push(new CompletionAction<>(result -> promise.resolve(result.map(mapper)), promise));

            return promise;
        }

        @Override
        public <U> @NotNull Promise<U> map(Cause cause, Function1<U, ? super T> mapper) {
            if (value != null) { return new PromiseImpl<>(value.map(cause, mapper)); }

            var promise = new PromiseImpl<U>(null);

            push(new CompletionAction<>(result -> promise.resolve(result.map(cause, mapper)), promise));

            return promise;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> Promise<U> flatMap(Function1<Promise<U>, ? super T> mapper) {
            if (value != null) { return value.fold(__ -> new PromiseImpl<>((Result<U>) value), mapper); }

            var result = new PromiseImpl<U>(null);
            push(new CompletionAction<>(
                    value -> value.fold(__ -> result
                            .resolve((Result<U>) value), mapper)
                            .onResult(result::resolve),
                    result
            ));
            return result;
        }

        @Override
        public @NotNull Promise<T> mapFailure(Function1<Cause, Cause> mapper) {
            if (value != null) { return new PromiseImpl<>(value.mapFailure(mapper)); }

            var promise = new PromiseImpl<T>(null);

            push(new CompletionAction<>(result -> promise.resolve(result.mapFailure(mapper)), promise));

            return promise;
        }

        // ----- Conditional Methods -----

        @Override
        public boolean isResolved() {
            return value != null;
        }

        // ----- Other Methods -----

        @Override
        public @NotNull String toString() {
            return "Promise(" + (value == null ? "<>" : value.toString()) + ')';
        }
    }

    // ----- Simple Creational Methods -----

    @Contract(" -> new")
    static <R> @NotNull Promise<R> promise() {
        return new PromiseImpl<>(null);
    }

    static <R> @NotNull Promise<R> promise(Consumer<Promise<R>> consumer) {
        var promise = Promise.<R>promise();
        runAsync(() -> consumer.accept(promise));
        return promise;
    }

    @Contract("_ -> new")
    static <R> @NotNull Promise<R> resolved(Result<R> result) {
        return new PromiseImpl<>(result);
    }

    @Contract("_ -> new")
    static <R> @NotNull Promise<R> successful(R value) {
        return resolved(Result.success(value));
    }

    @Contract("_ -> new")
    static <R> @NotNull Promise<R> failed(@NotNull Cause cause) {
        return resolved(cause.result());
    }

    // ----- Complex Creational Methods -----

    @SafeVarargs
    static <R> @NotNull Promise<R> any(Promise<R>... promises) {
        return Promise.promise(result -> List.of(
                promises
        ).forEach(promise -> promise
                .onResult(result::resolve)
                .onResultDo(() -> cancelAll(promises))
        ));
    }

    @SafeVarargs
    static <T> @NotNull Promise<T> anySuccess(Result<T> failureResult, Promise<T>... promises) {
        return Promise.promise(anySuccess ->
                threshold(promises.length, () -> anySuccess.resolve(failureResult))
                        .apply(at -> List.of(promises).forEach(promise -> promise
                                .onResult(result -> result
                                        .onSuccess(anySuccess::success)
                                        .onSuccessDo(() -> cancelAll(promises))
                                ).onResultDo(at::registerEvent)
                        ))
        );
    }

    static <T> @NotNull Promise<T> anySuccess(Result<T> failureResult, List<Promise<T>> promises) {
        return Promise.promise(anySuccess ->
                threshold(promises.size(), () -> anySuccess.resolve(failureResult))
                        .apply(at -> promises.forEach(promise -> promise
                                .onResult(result -> result
                                        .onSuccess(anySuccess::success)
                                        .onSuccessDo(() -> cancelAll(promises))
                                ).onResultDo(at::registerEvent)
                        ))
        );
    }

    @SafeVarargs
    static <T> @NotNull Promise<T> anySuccess(Promise<T>... promises) {
        return anySuccess(OperationException.CANCELLED.result(), promises);
    }

    static <T> @NotNull Promise<T> anySuccess(List<Promise<T>> promises) {
        return anySuccess(OperationException.CANCELLED.result(), promises);
    }

    // ----- General Methods -----

    static void runAsync(Runnable runnable) {
        AsyncExecutor.INSTANCE.runAsync(runnable);
    }

    private static <R> @NotNull Promise<R> setup(VariableFunction<R> transformer, Promise<?>@NotNull... promises) {
        var promise = Promise.<R>promise();

        resultCollector(promises.length, values -> promise.success(transformer.apply(values)))
                .accept(promise::failure, resultCollector -> {
                    int count = 0;
                    for (var currentPromise : promises) {
                        final var index = count++;
                        currentPromise.onResult(result -> result.accept(
                                promise::failure,
                                value -> resultCollector.registerEvent(index, value)
                        ));
                    }
                });

        return promise;
    }

    // ----- Consuming Methods (Blocking) -----

    Result<T> join();

    Result<T> join(Timeout timeout);

    // ----- Consuming Methods (Non-Blocking) -----

    Promise<T> async(Consumer<Promise<T>> action);

    Promise<T> async(Timeout timeout, Consumer<Promise<T>> action);

    Promise<T> onResult(Consumer<Result<T>> action);

    default Promise<T> onResultDo(Runnable action) {
        return onResult(__ -> action.run());
    }

    default Promise<T> onSuccess(Consumer<T> action) {
        return onResult(result -> result.onSuccess(action));
    }

    default Promise<T> onSuccessDo(Runnable action) {
        return onResult(result -> result.onSuccessDo(action));
    }

    default Promise<T> onFailure(Consumer<? super Cause> action) {
        return onResult(result -> result.onFailure(action));
    }

    default Promise<T> onFailureDo(Runnable action) {
        return onResult(result -> result.onFailureDo(action));
    }

    // ----- State Changing Methods -----

    Promise<T> resolve(Result<T> result);

    default Promise<T> cancel() {
        return resolve(OperationException.CANCELLED.result());
    }

    default Promise<T> success(T value) {
        return resolve(Result.success(value));
    }

    default Promise<T> failure(@NotNull Cause cause) {
        return resolve(cause.result());
    }

    // ----- Static State Changing Methods -----

    static <T> void cancelAll(@NotNull List<Promise<T>> promises) {
        promises.forEach(Promise::cancel);
    }

    @SafeVarargs
    static <T> void cancelAll(@NotNull Promise<T>@NotNull... promises) {
        cancelAll(List.of(promises));
    }

    // ----- Transformational Methods -----

    <U> Promise<U> map(Function1<U, ? super T> mapper);

    <U> Promise<U> map(Cause cause, Function1<U, ? super T> mapper);

    default <U> Promise<U> map(Function0<U> mapper) {
        return map(__ -> mapper.apply());
    }

    <U> Promise<U> flatMap(Function1<Promise<U>, ? super T> mapper);

    default <U> Promise<U> flatMap(Function0<Promise<U>> mapper) {
        return flatMap(__ -> mapper.apply());
    }

    Promise<T> mapFailure(Function1<Cause, Cause> mapper);

    // ----- Conditional Methods -----

    boolean isResolved();

    // ----- Mapper Methods -----

    @Contract(pure = true)
    static <T1> @NotNull Mapper1<T1> all(
            Promise<T1> value1
    ) { return () -> value1.map(Tuple::tuple); }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    static <T1, T2> @NotNull Mapper2<T1, T2> all(
            Promise<T1> value1,
            Promise<T2> value2
    ) { return () -> setup(values -> tuple(
            (T1) values[0],
            (T2) values[1]
    ), value1, value2); }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    static <T1, T2, T3> @NotNull Mapper3<T1, T2, T3> all(
            Promise<T1> value1,
            Promise<T2> value2,
            Promise<T3> value3
    ) { return () -> setup(values -> tuple(
            (T1) values[0],
            (T2) values[1],
            (T3) values[2]
    ), value1, value2, value3); }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4> @NotNull Mapper4<T1, T2, T3, T4> all(
            Promise<T1> value1,
            Promise<T2> value2,
            Promise<T3> value3,
            Promise<T4> value4
    ) { return () -> setup(values -> tuple(
            (T1) values[0],
            (T2) values[1],
            (T3) values[2],
            (T4) values[3]
    ), value1, value2, value3, value4); }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4, T5> @NotNull Mapper5<T1, T2, T3, T4, T5> all(
            Promise<T1> value1,
            Promise<T2> value2,
            Promise<T3> value3,
            Promise<T4> value4,
            Promise<T5> value5
    ) { return () -> setup(values -> tuple(
            (T1) values[0],
            (T2) values[1],
            (T3) values[2],
            (T4) values[3],
            (T5) values[4]
    ), value1, value2, value3, value4, value5); }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4, T5, T6> @NotNull Mapper6<T1, T2, T3, T4, T5, T6> all(
            Promise<T1> value1,
            Promise<T2> value2,
            Promise<T3> value3,
            Promise<T4> value4,
            Promise<T5> value5,
            Promise<T6> value6
    ) { return () -> setup(values -> tuple(
            (T1) values[0],
            (T2) values[1],
            (T3) values[2],
            (T4) values[3],
            (T5) values[4],
            (T6) values[5]
    ), value1, value2, value3, value4, value5, value6); }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4, T5, T6, T7> @NotNull Mapper7<T1, T2, T3, T4, T5, T6, T7> all(
            Promise<T1> value1,
            Promise<T2> value2,
            Promise<T3> value3,
            Promise<T4> value4,
            Promise<T5> value5,
            Promise<T6> value6,
            Promise<T7> value7
    ) { return () -> setup(values -> tuple(
            (T1) values[0],
            (T2) values[1],
            (T3) values[2],
            (T4) values[3],
            (T5) values[4],
            (T6) values[5],
            (T7) values[6]
    ), value1, value2, value3, value4, value5, value6, value7); }

    @Contract(pure = true)
    @SuppressWarnings({"unchecked", "java:S107"})
    static <T1, T2, T3, T4, T5, T6, T7, T8> @NotNull Mapper8<T1, T2, T3, T4, T5, T6, T7, T8> all(
            Promise<T1> value1,
            Promise<T2> value2,
            Promise<T3> value3,
            Promise<T4> value4,
            Promise<T5> value5,
            Promise<T6> value6,
            Promise<T7> value7,
            Promise<T8> value8
    ) { return () -> setup(values -> tuple(
            (T1) values[0],
            (T2) values[1],
            (T3) values[2],
            (T4) values[3],
            (T5) values[4],
            (T6) values[5],
            (T7) values[6],
            (T8) values[7]
    ), value1, value2, value3, value4, value5, value6, value7, value8); }

    @Contract(pure = true)
    @SuppressWarnings({"unchecked", "java:S107"})
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> @NotNull Mapper9<T1, T2, T3, T4, T5, T6, T7, T8, T9> all(
            Promise<T1> value1,
            Promise<T2> value2,
            Promise<T3> value3,
            Promise<T4> value4,
            Promise<T5> value5,
            Promise<T6> value6,
            Promise<T7> value7,
            Promise<T8> value8,
            Promise<T9> value9
    ) { return () -> setup(values -> tuple(
            (T1) values[0],
            (T2) values[1],
            (T3) values[2],
            (T4) values[3],
            (T5) values[4],
            (T6) values[5],
            (T7) values[6],
            (T8) values[7],
            (T9) values[8]
    ), value1, value2, value3, value4, value5, value6, value7, value8, value9); }
}
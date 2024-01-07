package io.github.akjo03.lib.functional;

import io.github.akjo03.lib.functional.Result.Failure;
import io.github.akjo03.lib.functional.Result.Success;
import io.github.akjo03.lib.functional.async.Promise;
import io.github.akjo03.lib.functional.util.AggregatedCause;
import io.github.akjo03.lib.functional.util.Causes;
import io.github.akjo03.lib.functional.util.SimpleCause;
import io.github.akjo03.lib.validation.Validator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.github.akjo03.lib.functional.Functions.*;
import static io.github.akjo03.lib.functional.Tuple.*;
import static io.github.akjo03.lib.functional.ResultMappers.*;

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
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/Result.java
 */
@SuppressWarnings({"unused", "java:S125", "UnusedReturnValue"})
public sealed interface Result<T> permits Success, Failure {
    // ----- Result Implementations (Success and Failure) -----

    record Success<T>(T value) implements Result<T> {
        @Override
        public <R> R fold(
                Function1<? extends R, ? super Cause> failureMapper,
                @NotNull Function1<? extends R, ? super T> successMapper
        ) { return successMapper.apply(value); }

        @Override
        public @NotNull String toString() {
            return "Success(" + value.toString() + ")";
        }
    }

    record Failure<T>(Cause cause) implements Result<T> {
        @Override
        public <R> R fold(
                @NotNull Function1<? extends R, ? super Cause> failureMapper,
                Function1<? extends R, ? super T> successMapper
        ) { return failureMapper.apply(cause); }

        @Override
        public @NotNull String toString() {
            return "Failure(" + cause.toString() + ")";
        }
    }

    // ----- Simple Creational Methods -----

    @Contract("_ -> new")
    static <R> @NotNull Result<R> success(R value) {
        return new Success<>(value);
    }

    @Contract("_ -> new")
    static <R> @NotNull Result<R> failure(Cause cause) {
        return new Failure<>(cause);
    }

    // ----- Complex Creational Methods -----

    static <R> Result<R> lift(
            Function1<? extends Cause, ? super Throwable> exceptionMapper,
            Function0<R> supplier
    ) {
        try { return success(supplier.apply()); }
        catch (Exception exception) { return failure(exceptionMapper.apply(exception)); }
    }

    static <R> Result<R> lift(
            Cause cause,
            Function0<R> supplier
    ) { return lift(
            (Function1<? extends Cause, ? super Throwable>) e -> new SimpleCause(e.getMessage(), Option.present(cause)),
            supplier
    ); }

    static <R> Result<R> lift(
            Function0<R> supplier
    ) { return lift(Causes.IRRELEVANT, supplier); }

    static <R> Result<R> lift(
            Function1<? extends Cause, ? super Throwable> exceptionMapper,
            Validator<R> validator,
            Function0<R> supplier
    ) {
        try { return validator.validate(supplier); }
        catch (Exception exception) { return failure(exceptionMapper.apply(exception)); }
    }

    static <R> Result<R> lift(
            Cause cause,
            Validator<R> validator,
            Function0<R> supplier
    ) { return lift(__ -> cause, validator, supplier); }

    static <R> Result<R> lift(
            Validator<R> validator,
            Function0<R> supplier
    ) { return lift(Causes.IRRELEVANT, validator, supplier); }

    static <R> Result<R> liftNullable(
            Cause nullCause,
            Function1<? extends Cause, ? super Throwable> exceptionMapper,
            Function0<R> supplier
    ) {
        try {
            R value = supplier.apply();
            return value == null ? failure(nullCause) : success(value);
        } catch (Exception exception) { return failure(exceptionMapper.apply(exception)); }
    }

    static <R> Result<R> liftNullable(
            Cause nullCause,
            Cause exceptionCause,
            Function0<R> supplier
    ) { return liftNullable(nullCause, (Function1<? extends Cause, ? super Throwable>) __ -> exceptionCause, supplier); }

    static <R> Result<R> liftNullable(
            Cause cause,
            Function0<R> supplier
    ) { return liftNullable(cause, (Function1<? extends Cause, ? super Throwable>) __ -> cause, supplier); }

    static <R> Result<R> liftNullable(
            Function0<R> supplier
    ) { return liftNullable(Causes.IRRELEVANT, supplier); }

    static <R> Result<R> liftNullable(
            Cause nullCause,
            Function1<? extends Cause, ? super Throwable> exceptionMapper,
            Validator<R> validator,
            Function0<R> supplier
    ) {
        try {
            R value = supplier.apply();
            return value == null ? failure(nullCause) : validator.validate(supplier);
        } catch (Exception exception) { return failure(exceptionMapper.apply(exception)); }
    }

    static <R> Result<R> liftNullable(
            Cause nullCause,
            Cause exceptionCause,
            Validator<R> validator,
            Function0<R> supplier
    ) { return liftNullable(nullCause, __ -> exceptionCause, validator, supplier); }

    static <R> Result<R> liftNullable(
            Cause cause,
            Validator<R> validator,
            Function0<R> supplier
    ) { return liftNullable(cause, __ -> cause, validator, supplier); }

    static <R> Result<R> liftNullable(
            Validator<R> validator,
            Function0<R> supplier
    ) { return liftNullable(Causes.IRRELEVANT, validator, supplier); }

    @SafeVarargs
    static <T> @NotNull Result<T> any(@NotNull Result<T> first, Result<T>... results) {
        if (first.isSuccess()) { return first; }

        for (var result : results) {
            if (result.isSuccess()) { return result; }
        }

        return first;
    }

    @SafeVarargs
    static <T> @NotNull Result<T> any(@NotNull Result<T> first, Function0<Result<T>>... suppliers) {
        if (first.isSuccess()) { return first; }

        for (var supplier : suppliers) {
            var result = supplier.apply();
            if (result.isSuccess()) { return result; }
        }

        return first;
    }

    static <T> Result<List<T>> allOf(@NotNull List<Result<T>> results) {
        var failure = new Cause[1];
        var values = new ArrayList<T>();

        results.forEach(value -> value.fold(cause -> failure[0] = cause, values::add));

        return failure[0] != null ? failure(failure[0]) : success(values);
    }

    static <T> Result<List<T>> aggregate(@NotNull List<Result<T>> results) {
        List<Cause> failures = new ArrayList<>();
        List<T> values = new ArrayList<>();

        results.forEach(result -> result.apply(failures::add, values::add));

        return failures.isEmpty()
                ? success(values)
                : failure(new AggregatedCause(failures));
    }

    static <T, M> Result<M> aggregate(
            @NotNull List<Result<T>> results,
            Function1<M, List<T>> mapper
    ) {
        List<Cause> failures = new ArrayList<>();
        List<T> values = new ArrayList<>();

        results.forEach(result -> result.apply(failures::add, values::add));

        return failures.isEmpty()
                ? success(mapper.apply(values))
                : failure(new AggregatedCause(failures));
    }

    @SafeVarargs
    static Result<Unit> allOf(Result<Unit>@NotNull... values) {
        for (var value : values) {
            if (value.isFailure()) { return value; }
        }

        return Unit.unitResult();
    }

    // ----- Consuming Methods -----

    <R> R fold(Function1<? extends R, ? super Cause> failureMapper, Function1<? extends R, ? super T> successMapper);

    /**
     * This method is marked as deprecated to prevent accidental usage.
     *
     * @deprecated This method should not be used in production code. It is only for testing purposes.
     */
    @Deprecated
    @SuppressWarnings({"java:S6355", "java:S1133", "DeprecatedIsStillUsed"})
    default T unwrap() {
        return fold(cause -> {
            throw new IllegalStateException("Cannot unwrap a failure result! Cause: " + cause.message());
        }, Functions::identity);
    }

    default Result<T> apply(Consumer<? super Cause> onFailure, Consumer<? super T> onSuccess) {
        return fold(cause -> {
            onFailure.accept(cause);
            return this;
        }, value -> {
            onSuccess.accept(value);
            return this;
        });
    }

    default Result<T> accept(Consumer<Cause> onFailure, Consumer<T> onSuccess) {
        return fold(cause -> {
            onFailure.accept(cause);
            return this;
        }, value -> {
            onSuccess.accept(value);
            return this;
        });
    }

    default Result<T> onSuccess(Consumer<T> onSuccess) {
        fold(__ -> Functions.toNull(), value -> {
            onSuccess.accept(value);
            return null;
        });
        return this;
    }

    default Result<T> onSuccessDo(Runnable onSuccess) {
        fold(__ -> Functions.toNull(), __ -> {
            onSuccess.run();
            return null;
        });
        return this;
    }

    default Result<T> onFailure(Consumer<? super Cause> onFailure) {
        fold(cause -> {
            onFailure.accept(cause);
            return null;
        }, __ -> Functions.toNull());
        return this;
    }

    default Result<T> onFailureDo(Runnable onFailure) {
        fold(cause -> {
            onFailure.run();
            return null;
        }, __ -> Functions.toNull());
        return this;
    }

    default Result<T> onResult(@NotNull Consumer<Result<T>> onResult) {
        onResult.accept(this);
        return this;
    }

    default Result<T> onResultDo(@NotNull Runnable onResult) {
        onResult.run();
        return this;
    }

    default T or(T other) {
        return fold(__ -> other, Functions::identity);
    }

    default T or(Function0<T> other) {
        return fold(__ -> other.apply(), Functions::identity);
    }

    default Result<T> orElse(Result<T> other) {
        return fold(__ -> other, __ -> this);
    }

    default Result<T> orElse(Function0<Result<T>> other) {
        return fold(__ -> other.apply(), __ -> this);
    }

    // ----- Static Consuming Methods -----

    static <T> void process(
            @NotNull List<Result<T>> results,
            Consumer<List<T>> onSuccess,
            Consumer<List<Cause>> onFailure
    ) {
        List<Cause> failures = new ArrayList<>();
        List<T> values = new ArrayList<>();

        results.forEach(result -> result.apply(failures::add, values::add));

        if (failures.isEmpty()) {
            onSuccess.accept(values);
        } else {
            onFailure.accept(failures);
        }
    }

    // ----- Throwing Methods -----

    @SuppressWarnings("java:S1874")
    default T orThrow() throws IllegalArgumentException {
        if (isSuccess()) { return unwrap(); }
        else { throw new IllegalArgumentException(fold(Cause::message, Functions::toNull)); }
    }

    @SuppressWarnings("java:S1874")
    default <E extends Throwable> T orThrow(E exception) throws E {
        if (isSuccess()) { return unwrap(); }
        else { throw exception; }
    }

    @SuppressWarnings("java:S1874")
    default <E extends Throwable> T orThrow(Function1<? extends E, Cause> exceptionMapper) throws E {
        if (isSuccess()) { return unwrap(); }
        else { throw exceptionMapper.apply(fold(Functions::identity, Functions::toNull)); }
    }

    // ----- Transformational Methods -----

    @SuppressWarnings("unchecked")
    default <R> Result<R> map(Function1<R, ? super T> mapper) {
        return fold(__ -> (Result<R>) this, value -> lift(() -> mapper.apply(value)));
    }

    @SuppressWarnings("unchecked")
    default <R> Result<R> map(Cause cause, Function1<R, ? super T> mapper) {
        return fold(__ -> (Result<R>) this, value -> lift(cause, () -> mapper.apply(value)));
    }
    default Result<T> mapFailure(Function1<Cause, ? super Cause> mapper) {
        return fold(cause -> mapper.apply(cause).result(), __ -> this);
    }

    @SuppressWarnings("unchecked")
    default <R> Result<R> flatMap(Function1<Result<R>, ? super T> mapper) {
        return fold(__ -> (Result<R>) this, mapper);
    }

    @SuppressWarnings("unchecked")
    default <R> Result<R> replace(Function0<R> mapper) {
        return fold(__ -> (Result<R>) this, __ -> success(mapper.apply()));
    }

    @SuppressWarnings("unchecked")
    default <R> Result<R> flatReplace(Function0<Result<R>> mapper) {
        return fold(__ -> (Result<R>) this, __ -> mapper.apply());
    }

    default Option<T> toOption() {
        return fold(__ -> Option.empty(), Option::option);
    }

    default Optional<T> toOptional() {
        return fold(__ -> Optional.empty(), Optional::of);
    }

    default Promise<T> toPromise() {
        return fold(Promise::failed, Promise::successful);
    }

    // ----- Conditional Methods -----

    default boolean isSuccess() {
        return fold(Functions::toFalse, Functions::toTrue);
    }

    default boolean isFailure() {
        return fold(Functions::toTrue, Functions::toFalse);
    }

    // ----- Stream Methods -----

    default Stream<T> stream() {
        return fold(__ -> Stream.empty(), Stream::of);
    }

    default Result<T> filter(Cause cause, Predicate<T> predicate) {
        return fold(__ -> this, value -> predicate.test(value) ? this : failure(cause));
    }

    default Result<T> filter(Function1<Cause, T> causeMapper, Predicate<T> predicate) {
        return fold(__ -> this, value -> predicate.test(value) ? this : failure(causeMapper.apply(value)));
    }

    @SafeVarargs
    static <C, R> Result<R> combine(Function1<R, List<C>> combiner, Result<C>@NotNull... results) {
        List<Cause> failures = new ArrayList<>();
        List<C> values = new ArrayList<>();

        Arrays.stream(results).forEach(result -> result.apply(failures::add, values::add));

        return failures.isEmpty()
                ? success(combiner.apply(values))
                : failure(new AggregatedCause(failures));
    }

    @SafeVarargs
    static <R> Result<R> combine(Operator2<R> combiner, Result<R>@NotNull... results) {
        List<Cause> failures = new ArrayList<>();
        List<R> values = new ArrayList<>();

        Arrays.stream(results).forEach(result -> result.apply(failures::add, values::add));

        return failures.isEmpty()
                ? lift(() -> values.stream().reduce(combiner.operator()).orElseThrow())
                : failure(new AggregatedCause(failures));
    }

    static <T> Result<List<T>> sequence(@NotNull List<Result<T>> results) {
        return results.stream()
                .reduce(Result.success(new ArrayList<>()),
                        (acc, result) -> acc.flatMap(list -> result.map(value -> {
                            list.add(value);
                            return list;
                        })), (res1, res2) -> {
                    throw new UnsupportedOperationException("Parallel Stream not supported!");
                }
        );
    }

    // ----- Mapper Methods -----

    @Contract(pure = true)
    static <T1> @NotNull Mapper1<T1> all(
            Result<T1> value1
    ) { return () -> value1.flatMap(
            _value1 -> success(tuple(
                    _value1
            ))
    ); }

    @Contract(pure = true)
    static <T1, T2> @NotNull Mapper2<T1, T2> all(
            Result<T1> value1,
            Result<T2> value2
    ) { return () -> value1.flatMap(
            _value1 -> value2.flatMap(
                    _value2 -> success(tuple(
                            _value1,
                            _value2
                    ))
            )
    ); }

    @Contract(pure = true)
    static <T1, T2, T3> @NotNull Mapper3<T1, T2, T3> all(
            Result<T1> value1,
            Result<T2> value2,
            Result<T3> value3
    ) { return () -> value1.flatMap(
            _value1 -> value2.flatMap(
                    _value2 -> value3.flatMap(
                            _value3 -> success(tuple(
                                    _value1,
                                    _value2,
                                    _value3
                            ))
                    )
            )
    ); }

    @Contract(pure = true)
    static <T1, T2, T3, T4> @NotNull Mapper4<T1, T2, T3, T4> all(
            Result<T1> value1,
            Result<T2> value2,
            Result<T3> value3,
            Result<T4> value4
    ) { return () -> value1.flatMap(
            _value1 -> value2.flatMap(
                    _value2 -> value3.flatMap(
                            _value3 -> value4.flatMap(
                                    _value4 -> success(tuple(
                                            _value1,
                                            _value2,
                                            _value3,
                                            _value4
                                    ))
                            )
                    )
            )
    ); }

    @Contract(pure = true)
    static <T1, T2, T3, T4, T5> @NotNull Mapper5<T1, T2, T3, T4, T5> all(
            Result<T1> value1,
            Result<T2> value2,
            Result<T3> value3,
            Result<T4> value4,
            Result<T5> value5
    ) { return () -> value1.flatMap(
            _value1 -> value2.flatMap(
                    _value2 -> value3.flatMap(
                            _value3 -> value4.flatMap(
                                    _value4 -> value5.flatMap(
                                            _value5 -> success(tuple(
                                                    _value1,
                                                    _value2,
                                                    _value3,
                                                    _value4,
                                                    _value5
                                            ))
                                    )
                            )
                    )
            )
    ); }

    @Contract(pure = true)
    static <T1, T2, T3, T4, T5, T6> @NotNull Mapper6<T1, T2, T3, T4, T5, T6> all(
            Result<T1> value1,
            Result<T2> value2,
            Result<T3> value3,
            Result<T4> value4,
            Result<T5> value5,
            Result<T6> value6
    ) { return () -> value1.flatMap(
            _value1 -> value2.flatMap(
                    _value2 -> value3.flatMap(
                            _value3 -> value4.flatMap(
                                    _value4 -> value5.flatMap(
                                            _value5 -> value6.flatMap(
                                                    _value6 -> success(tuple(
                                                            _value1,
                                                            _value2,
                                                            _value3,
                                                            _value4,
                                                            _value5,
                                                            _value6
                                                    ))
                                            )
                                    )
                            )
                    )
            )
    ); }

    @Contract(pure = true)
    static <T1, T2, T3, T4, T5, T6, T7> @NotNull Mapper7<T1, T2, T3, T4, T5, T6, T7> all(
            Result<T1> value1,
            Result<T2> value2,
            Result<T3> value3,
            Result<T4> value4,
            Result<T5> value5,
            Result<T6> value6,
            Result<T7> value7
    ) { return () -> value1.flatMap(
            _value1 -> value2.flatMap(
                    _value2 -> value3.flatMap(
                            _value3 -> value4.flatMap(
                                    _value4 -> value5.flatMap(
                                            _value5 -> value6.flatMap(
                                                    _value6 -> value7.flatMap(
                                                            _value7 -> success(tuple(
                                                                    _value1,
                                                                    _value2,
                                                                    _value3,
                                                                    _value4,
                                                                    _value5,
                                                                    _value6,
                                                                    _value7
                                                            ))
                                                    )
                                            )
                                    )
                            )
                    )
            )
    ); }

    @Contract(pure = true)
    @SuppressWarnings("java:S107")
    static <T1, T2, T3, T4, T5, T6, T7, T8> @NotNull Mapper8<T1, T2, T3, T4, T5, T6, T7, T8> all(
            Result<T1> value1,
            Result<T2> value2,
            Result<T3> value3,
            Result<T4> value4,
            Result<T5> value5,
            Result<T6> value6,
            Result<T7> value7,
            Result<T8> value8
    ) { return () -> value1.flatMap(
            _value1 -> value2.flatMap(
                    _value2 -> value3.flatMap(
                            _value3 -> value4.flatMap(
                                    _value4 -> value5.flatMap(
                                            _value5 -> value6.flatMap(
                                                    _value6 -> value7.flatMap(
                                                            _value7 -> value8.flatMap(
                                                                    _value8 -> success(tuple(
                                                                            _value1,
                                                                            _value2,
                                                                            _value3,
                                                                            _value4,
                                                                            _value5,
                                                                            _value6,
                                                                            _value7,
                                                                            _value8
                                                                    ))
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            )
    ); }

    @Contract(pure = true)
    @SuppressWarnings("java:S107")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> @NotNull Mapper9<T1, T2, T3, T4, T5, T6, T7, T8, T9> all(
            Result<T1> value1,
            Result<T2> value2,
            Result<T3> value3,
            Result<T4> value4,
            Result<T5> value5,
            Result<T6> value6,
            Result<T7> value7,
            Result<T8> value8,
            Result<T9> value9
    ) { return () -> value1.flatMap(
            _value1 -> value2.flatMap(
                    _value2 -> value3.flatMap(
                            _value3 -> value4.flatMap(
                                    _value4 -> value5.flatMap(
                                            _value5 -> value6.flatMap(
                                                    _value6 -> value7.flatMap(
                                                            _value7 -> value8.flatMap(
                                                                    _value8 -> value9.flatMap(
                                                                            _value9 -> success(tuple(
                                                                                    _value1,
                                                                                    _value2,
                                                                                    _value3,
                                                                                    _value4,
                                                                                    _value5,
                                                                                    _value6,
                                                                                    _value7,
                                                                                    _value8,
                                                                                    _value9
                                                                            ))
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            )
    ); }
}
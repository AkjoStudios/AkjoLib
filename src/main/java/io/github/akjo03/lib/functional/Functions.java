package io.github.akjo03.lib.functional;

import io.github.akjo03.lib.functional.util.Causes;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

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
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/Functions.java
 */
@SuppressWarnings({"unused", "java:S125"})
public interface Functions {
    // ----- Function Variants (0-9) -----

    @FunctionalInterface
    interface Function0<R> {
        R apply();

        @Contract(pure = true)
        static <T> @NotNull Function0<Result<T>> of(@NotNull ThrowingSupplier<T> supplier) {
            return () -> {
                try { return Result.success(supplier.get()); }
                catch (Throwable throwable) { return Result.failure(Causes.fromThrowable(throwable)); }
            };
        }

        @Contract(pure = true)
        static <T> @NotNull Function0<T> of(
                T value
        ) { return () -> value; }

        @Contract(pure = true)
        static <T> @NotNull Function0<T> of(
                @NotNull Supplier<T> supplier
        ) { return supplier::get; }

        @Contract(pure = true)
        static <T> @NotNull Function0<Result<T>> of(
                @NotNull Cause cause,
                @NotNull ThrowingSupplier<T> supplier
        ) {
            return () -> {
                try { return Result.success(supplier.get()); }
                catch (Throwable throwable) { return Result.failure(cause); }
            };
        }

        @Contract(pure = true)
        static <T> @NotNull Function0<Result<T>> of(
                @NotNull Function1<? extends Cause, ? super Throwable> exceptionMapper,
                @NotNull ThrowingSupplier<T> supplier
        ) {
            return () -> {
                try { return Result.success(supplier.get()); }
                catch (Throwable throwable) { return Result.failure(exceptionMapper.apply(throwable)); }
            };
        }
    }

    @Contract(pure = true)
    static <R> @NotNull Function0<R> supplier(
            R value
    ) { return () -> value; }

    @Contract(pure = true)
    static <R> @NotNull Function0<R> supplier(
            Supplier<R> supplier
    ) {
        return supplier == null
            ? Functions::toNull
            : supplier::get;
    }

    @FunctionalInterface
    interface Function1<R, T1> {
        R apply(T1 param1);

        default <N> Function1<N, T1> then(Function1<N, R> function) {
            return value -> function.apply(apply(
                    value
            ));
        }

        default <N> Function1<R, N> before(Function1<T1, N> function) {
            return value -> apply(function.apply(
                    value
            ));
        }

        @Contract(pure = true)
        static <T> @NotNull Function1<T, T> identity() { return Functions::identity; }
    }

    @FunctionalInterface
    interface Function2<R, T1, T2> {
        R apply(T1 param1, T2 param2);

        default Function1<R, T2> bind(T1 param1) {
            return param2 -> apply(
                    param1,
                    param2
            );
        }

        default <N> Function2<N, T1, T2> then(Function1<N, R> function) {
            return (t1, t2) -> function.apply(apply(
                    t1,
                    t2
            ));
        }
    }

    @FunctionalInterface
    interface Function3<R, T1, T2, T3> {
        R apply(T1 param1, T2 param2, T3 param3);

        default Function2<R, T2, T3> bind(T1 param1) {
            return (param2, param3) -> apply(
                    param1,
                    param2,
                    param3
            );
        }

        default <N> Function3<N, T1, T2, T3> then(Function1<N, R> function) {
            return (param1, param2, param3) -> function.apply(apply(
                    param1,
                    param2,
                    param3
            ));
        }
    }

    @FunctionalInterface
    interface Function4<R, T1, T2, T3, T4> {
        R apply(T1 param1, T2 param2, T3 param3, T4 param4);

        default Function3<R, T2, T3, T4> bind(T1 param1) {
            return (param2, param3, param4) -> apply(
                    param1,
                    param2,
                    param3,
                    param4
            );
        }

        default <N> Function4<N, T1, T2, T3, T4> then(Function1<N, R> function) {
            return (param1, param2, param3, param4) -> function.apply(apply(
                    param1,
                    param2,
                    param3,
                    param4
            ));
        }
    }

    @FunctionalInterface
    interface Function5<R, T1, T2, T3, T4, T5> {
        R apply(T1 param1, T2 param2, T3 param3, T4 param4, T5 param5);

        default Function4<R, T2, T3, T4, T5> bind(T1 param1) {
            return (param2, param3, param4, param5) -> apply(
                    param1,
                    param2,
                    param3,
                    param4,
                    param5
            );
        }

        default <N> Function5<N, T1, T2, T3, T4, T5> then(Function1<N, R> function) {
            return (param1, param2, param3, param4, param5) -> function.apply(apply(
                    param1,
                    param2,
                    param3,
                    param4,
                    param5
            ));
        }
    }

    @FunctionalInterface
    interface Function6<R, T1, T2, T3, T4, T5, T6> {
        R apply(T1 param1, T2 param2, T3 param3, T4 param4, T5 param5, T6 param6);

        default Function5<R, T2, T3, T4, T5, T6> bind(T1 param1) {
            return (param2, param3, param4, param5, param6) -> apply(
                    param1,
                    param2,
                    param3,
                    param4,
                    param5,
                    param6
            );
        }

        default <N> Function6<N, T1, T2, T3, T4, T5, T6> then(Function1<N, R> function) {
            return (param1, param2, param3, param4, param5, param6) -> function.apply(apply(
                    param1,
                    param2,
                    param3,
                    param4,
                    param5,
                    param6
            ));
        }
    }

    @FunctionalInterface
    interface Function7<R, T1, T2, T3, T4, T5, T6, T7> {
        R apply(T1 param1, T2 param2, T3 param3, T4 param4, T5 param5, T6 param6, T7 param7);

        default Function6<R, T2, T3, T4, T5, T6, T7> bind(T1 param1) {
            return (param2, param3, param4, param5, param6, param7) -> apply(
                    param1,
                    param2,
                    param3,
                    param4,
                    param5,
                    param6,
                    param7
            );
        }

        default <N> Function7<N, T1, T2, T3, T4, T5, T6, T7> then(Function1<N, R> function) {
            return (param1, param2, param3, param4, param5, param6, param7) -> function.apply(apply(
                    param1,
                    param2,
                    param3,
                    param4,
                    param5,
                    param6,
                    param7
            ));
        }
    }

    @FunctionalInterface
    interface Function8<R, T1, T2, T3, T4, T5, T6, T7, T8> {
        @SuppressWarnings("java:S107")
        R apply(T1 param1, T2 param2, T3 param3, T4 param4, T5 param5, T6 param6, T7 param7, T8 param8);

        default Function7<R, T2, T3, T4, T5, T6, T7, T8> bind(T1 param1) {
            return (param2, param3, param4, param5, param6, param7, param8) -> apply(
                    param1,
                    param2,
                    param3,
                    param4,
                    param5,
                    param6,
                    param7,
                    param8
            );
        }

        default <N> Function8<N, T1, T2, T3, T4, T5, T6, T7, T8> then(Function1<N, R> function) {
            return (param1, param2, param3, param4, param5, param6, param7, param8) -> function.apply(apply(
                    param1,
                    param2,
                    param3,
                    param4,
                    param5,
                    param6,
                    param7,
                    param8
            ));
        }
    }

    @FunctionalInterface
    interface Function9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        @SuppressWarnings("java:S107")
        R apply(T1 param1, T2 param2, T3 param3, T4 param4, T5 param5, T6 param6, T7 param7, T8 param8, T9 param9);

        default Function8<R, T2, T3, T4, T5, T6, T7, T8, T9> bind(T1 param1) {
            return (param2, param3, param4, param5, param6, param7, param8, param9) -> apply(
                    param1,
                    param2,
                    param3,
                    param4,
                    param5,
                    param6,
                    param7,
                    param8,
                    param9
            );
        }

        default <N> Function9<N, T1, T2, T3, T4, T5, T6, T7, T8, T9> then(Function1<N, R> function) {
            return (param1, param2, param3, param4, param5, param6, param7, param8, param9) -> function.apply(apply(
                    param1,
                    param2,
                    param3,
                    param4,
                    param5,
                    param6,
                    param7,
                    param8,
                    param9
            ));
        }
    }

    // ----- Identity and Unit Methods -----

    @Contract(value = "_ -> param1", pure = true)
    static <T> T identity(T t) { return t; }

    @Contract(pure = true)
    static <T1> void unit() {}

    @Contract(pure = true)
    static <T1> void unit(
            T1 param1
    ) {}

    @Contract(pure = true)
    static <T1, T2> void unit(
            T1 param1,
            T2 param2
    ) {}

    @Contract(pure = true)
    static <T1, T2, T3> void unit(
            T1 param1,
            T2 param2,
            T3 param3
    ) {}

    @Contract(pure = true)
    static <T1, T2, T3, T4> void unit(
            T1 param1,
            T2 param2,
            T3 param3,
            T4 param4
    ) {}

    @Contract(pure = true)
    static <T1, T2, T3, T4, T5> void unit(
            T1 param1,
            T2 param2,
            T3 param3,
            T4 param4,
            T5 param5
    ) {}

    @Contract(pure = true)
    static <T1, T2, T3, T4, T5, T6> void unit(
            T1 param1,
            T2 param2,
            T3 param3,
            T4 param4,
            T5 param5,
            T6 param6
    ) {}

    @Contract(pure = true)
    static <T1, T2, T3, T4, T5, T6, T7> void unit(
            T1 param1,
            T2 param2,
            T3 param3,
            T4 param4,
            T5 param5,
            T6 param6,
            T7 param7
    ) {}

    @Contract
    @SuppressWarnings("java:S107")
    static <T1, T2, T3, T4, T5, T6, T7, T8> void unit(
            T1 param1,
            T2 param2,
            T3 param3,
            T4 param4,
            T5 param5,
            T6 param6,
            T7 param7,
            T8 param8
    ) {}

    @Contract
    @SuppressWarnings("java:S107")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> void unit(
            T1 param1,
            T2 param2,
            T3 param3,
            T4 param4,
            T5 param5,
            T6 param6,
            T7 param7,
            T8 param8,
            T9 param9
    ) {}

    // ----- Throwing Supplier, Throwing Runnable and TriConsumer -----

    @FunctionalInterface
    interface ThrowingSupplier<T> {
        @SuppressWarnings({"RedundantThrows", "java:S112"})
        T get() throws Throwable;
    }

    @FunctionalInterface
    interface ThrowingRunnable {
        @SuppressWarnings({"RedundantThrows", "java:S112"})
        void run() throws Throwable;
    }

    @FunctionalInterface
    interface TriConsumer<T1, T2, T3> {
        void accept(T1 param1, T2 param2, T3 param3);
    }

    // ----- Variable Function Variant -----

    @FunctionalInterface
    interface VariableFunction<R> {
        R apply(Object... params);
    }

    // ----- To Functions -----

    @Contract(pure = true)
    @SuppressWarnings("SameReturnValue")
    static <R, T1> @Nullable R toNull() { return null; }
    @Contract(pure = true)
    @SuppressWarnings("SameReturnValue")
    static <R, T1> @Nullable R toNull(T1 param1) { return null; }

    @Contract(pure = true)
    @SuppressWarnings("SameReturnValue")
    static <T1> boolean toTrue(T1 param1) { return true; }

    @Contract(pure = true)
    @SuppressWarnings("SameReturnValue")
    static <T1> boolean toFalse(T1 param1) { return false; }
}
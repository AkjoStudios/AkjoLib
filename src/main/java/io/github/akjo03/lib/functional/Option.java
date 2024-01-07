package io.github.akjo03.lib.functional;

import io.github.akjo03.lib.functional.Option.None;
import io.github.akjo03.lib.functional.Option.Some;
import io.github.akjo03.lib.functional.async.Promise;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.github.akjo03.lib.functional.OptionMappers.*;

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
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/Option.java
 */
@SuppressWarnings({"unused", "java:S125", "UnusedReturnValue", "SameReturnValue"})
public sealed interface Option<T> permits Some, None {
    // ----- None Constant -----

    @SuppressWarnings({"rawtypes"})
    None NONE = new None();

    // ----- Option Implementations (Some and None) -----

    record Some<T>(T value) implements Option<T> {
        @Override
        public <R> R fold(
                Functions.Function0<? extends R> emptySupplier,
                @NotNull Functions.Function1<? extends R, ? super T> presentMapper
        ) { return presentMapper.apply(value); }

        @Override
        public @NotNull String toString() {
            return "Some(" + value.toString() + ")";
        }
    }

    record None<T>() implements Option<T> {
        @Override
        public <R> R fold(
                @NotNull Functions.Function0<? extends R> emptySupplier,
                @NotNull Functions.Function1<? extends R, ? super T> presentMapper
        ) { return emptySupplier.apply(); }

        @Contract(pure = true)
        @Override
        public @NotNull String toString() {
            return "None()";
        }
    }

    // ----- Simple Creational Methods -----

    @Contract("_ -> new")
    static <R> @NotNull Option<R> present(R value) {
        return new Some<>(value);
    }

    @Contract(" -> new")
    @SuppressWarnings("unchecked")
    static <R> @NotNull Option<R> empty() {
        return NONE;
    }

    @Contract("_ -> !null")
    static <T> Option<T> option(T value) {
        return value == null ? empty() : present(value);
    }

    @Contract("_ -> !null")
    static <T> Option<T> option(@NotNull Functions.Function0<T> supplier) {
        return option(supplier.apply());
    }

    @Contract("_ -> !null")
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <T> Option<T> from(@NotNull Optional<T> optional) {
        return option(optional.orElse(null));
    }

    // ----- Complex Creational Methods -----

    @SafeVarargs
    static <T> @NotNull Option<T> any(@NotNull Option<T> first, Option<T>... options) {
        if (first.isPresent()) { return first; }

        for (var option : options) {
            if (option.isPresent()) { return option; }
        }

        return first;
    }

    @SafeVarargs
    static <T> @NotNull Option<T> any(@NotNull Option<T> first, Functions.Function0<Option<T>>... suppliers) {
        if (first.isPresent()) { return first; }

        for (var supplier : suppliers) {
            var option = supplier.apply();
            if (option.isPresent()) { return option; }
        }

        return first;
    }

    @SafeVarargs
    static <T> Option<List<T>> allOf(Option<T>... values) {
        return allOf(List.of(values));
    }

    static <T> Option<List<T>> allOf(@NotNull List<Option<T>> values) {
        var result = new ArrayList<T>();
        for (var value : values) {
            if (value.isEmpty()) { return empty(); }
            value.onValue(result::add);
        }
        return present(result);
    }

    // ----- Consuming Methods -----

    <R> R fold(Functions.Function0<? extends R> emptySupplier, Functions.Function1<? extends R, ? super T> presentMapper);

    /**
     * This method is marked as deprecated to prevent accidental usage.
     *
     * @deprecated This method should not be used in production code. It is only for testing purposes.
     */
    @Deprecated
    @SuppressWarnings({"java:S6355", "java:S1133"})
    default T unwrap() {
        return fold(() -> {
            throw new UnwrapException("Cannot unwrap an empty Option!");
        }, Functions::identity);
    }

    default Option<T> apply(Runnable onEmpty, Consumer<? super T> onPresent) {
        fold(() -> {
            onEmpty.run();
            return null;
        }, value -> {
            onPresent.accept(value);
            return null;
        });
        return this;
    }

    default Option<T> accept(Runnable onEmpty, Consumer<T> onSuccess) {
        return fold(() -> {
            onEmpty.run();
            return this;
        }, value -> {
            onSuccess.accept(value);
            return this;
        });
    }

    default Option<T> onValue(Consumer<? super T> onPresent) {
        return apply(Functions::unit, onPresent);
    }

    default Option<T> onValueDo(Runnable onPresent) {
        return apply(Functions::unit, __ -> onPresent.run());
    }

    default Option<T> onEmpty(Runnable onEmpty) {
        return apply(onEmpty, Functions::unit);
    }

    default Option<T> onOptionDo(@NotNull Runnable onOption) {
        onOption.run();
        return this;
    }

    default T or(T other) {
        return fold(() -> other, Functions::identity);
    }

    default T or(Functions.Function0<T> other) {
        return fold(other, Functions::identity);
    }

    default Option<T> orElse(Option<T> other) {
        return fold(() -> other, __ -> this);
    }

    default Option<T> orElse(Functions.Function0<Option<T>> other) {
        return fold(other, __ -> this);
    }

    // ----- Transformational Methods -----

    default <U> Option<U> map(Functions.Function1<U, ? super T> mapper) {
        return fold(Option::empty, value -> Result.lift(() -> mapper.apply(value)).toOption());
    }

    default <U> Option<U> map(Cause cause, Functions.Function1<U, ? super T> mapper) {
        return fold(Option::empty, value -> Result.lift(cause, () -> mapper.apply(value)).toOption());
    }

    default <U> Option<U> flatMap(Functions.Function1<Option<U>, ? super T> mapper) {
        return fold(Option::empty, mapper);
    }

    default <U> Option<U> replace(Functions.Function0<U> mapper) {
        return fold(Option::empty, __ -> present(mapper.apply()));
    }

    default <U> Option<U> flatReplace(Functions.Function0<Option<U>> mapper) {
        return fold(Option::empty, __ -> mapper.apply());
    }

    default Result<T> toResult(@NotNull Cause cause) {
        return fold(cause::result, Result::success);
    }

    default Optional<T> toOptional() {
        return fold(Optional::empty, Optional::of);
    }

    default Promise<T> toPromise(@NotNull Cause cause) {
        return fold(cause::promise, Promise::successful);
    }

    // ----- Conditional Methods -----

    default boolean isPresent() {
        return fold(() -> false, __ -> true);
    }

    default boolean isEmpty() {
        return fold(() -> true, __ -> false);
    }

    // ----- Stream Methods -----

    default Stream<T> stream() {
        return fold(Stream::empty, Stream::of);
    }

    default Option<T> filter(Predicate<? super T> predicate) {
        return fold(Option::empty, value -> predicate.test(value) ? this : empty());
    }

    // ----- Mapper Methods -----

    @Contract(pure = true)
    static <T1> @NotNull Mapper1<T1> all(
            Option<T1> option1
    ) { return () -> option1.flatMap(
            value1 -> present(Tuple.tuple(
                    value1
            ))
    ); }

    @Contract(pure = true)
    static <T1, T2> @NotNull Mapper2<T1, T2> all(
            Option<T1> option1,
            Option<T2> option2
    ) { return () -> option1.flatMap(
            value1 -> option2.flatMap(
                    value2 -> present(Tuple.tuple(
                            value1,
                            value2
                    ))
            )
    ); }

    @Contract(pure = true)
    static <T1, T2, T3> @NotNull Mapper3<T1, T2, T3> all(
            Option<T1> option1,
            Option<T2> option2,
            Option<T3> option3
    ) { return () -> option1.flatMap(
            value1 -> option2.flatMap(
                    value2 -> option3.flatMap(
                            value3 -> present(Tuple.tuple(
                                    value1,
                                    value2,
                                    value3
                            ))
                    )
            )
    ); }

    @Contract(pure = true)
    static <T1, T2, T3, T4> @NotNull Mapper4<T1, T2, T3, T4> all(
            Option<T1> option1,
            Option<T2> option2,
            Option<T3> option3,
            Option<T4> option4
    ) { return () -> option1.flatMap(
            value1 -> option2.flatMap(
                    value2 -> option3.flatMap(
                            value3 -> option4.flatMap(
                                    value4 -> present(Tuple.tuple(
                                            value1,
                                            value2,
                                            value3,
                                            value4
                                    ))
                            )
                    )
            )
    ); }

    @Contract(pure = true)
    static <T1, T2, T3, T4, T5> @NotNull Mapper5<T1, T2, T3, T4, T5> all(
            Option<T1> option1,
            Option<T2> option2,
            Option<T3> option3,
            Option<T4> option4,
            Option<T5> option5
    ) { return () -> option1.flatMap(
            value1 -> option2.flatMap(
                    value2 -> option3.flatMap(
                            value3 -> option4.flatMap(
                                    value4 -> option5.flatMap(
                                            value5 -> present(Tuple.tuple(
                                                    value1,
                                                    value2,
                                                    value3,
                                                    value4,
                                                    value5
                                            ))
                                    )
                            )
                    )
            )
    ); }

    @Contract(pure = true)
    static <T1, T2, T3, T4, T5, T6> @NotNull Mapper6<T1, T2, T3, T4, T5, T6> all(
            Option<T1> option1,
            Option<T2> option2,
            Option<T3> option3,
            Option<T4> option4,
            Option<T5> option5,
            Option<T6> option6
    ) { return () -> option1.flatMap(
            value1 -> option2.flatMap(
                    value2 -> option3.flatMap(
                            value3 -> option4.flatMap(
                                    value4 -> option5.flatMap(
                                            value5 -> option6.flatMap(
                                                    value6 -> present(Tuple.tuple(
                                                            value1,
                                                            value2,
                                                            value3,
                                                            value4,
                                                            value5,
                                                            value6
                                                    ))
                                            )
                                    )
                            )
                    )
            )
    ); }

    @Contract(pure = true)
    static <T1, T2, T3, T4, T5, T6, T7> @NotNull Mapper7<T1, T2, T3, T4, T5, T6, T7> all(
            Option<T1> option1,
            Option<T2> option2,
            Option<T3> option3,
            Option<T4> option4,
            Option<T5> option5,
            Option<T6> option6,
            Option<T7> option7
    ) { return () -> option1.flatMap(
            value1 -> option2.flatMap(
                    value2 -> option3.flatMap(
                            value3 -> option4.flatMap(
                                    value4 -> option5.flatMap(
                                            value5 -> option6.flatMap(
                                                    value6 -> option7.flatMap(
                                                            value7 -> present(Tuple.tuple(
                                                                    value1,
                                                                    value2,
                                                                    value3,
                                                                    value4,
                                                                    value5,
                                                                    value6,
                                                                    value7
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
            Option<T1> option1,
            Option<T2> option2,
            Option<T3> option3,
            Option<T4> option4,
            Option<T5> option5,
            Option<T6> option6,
            Option<T7> option7,
            Option<T8> option8
    ) { return () -> option1.flatMap(
            value1 -> option2.flatMap(
                    value2 -> option3.flatMap(
                            value3 -> option4.flatMap(
                                    value4 -> option5.flatMap(
                                            value5 -> option6.flatMap(
                                                    value6 -> option7.flatMap(
                                                            value7 -> option8.flatMap(
                                                                    value8 -> present(Tuple.tuple(
                                                                            value1,
                                                                            value2,
                                                                            value3,
                                                                            value4,
                                                                            value5,
                                                                            value6,
                                                                            value7,
                                                                            value8
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
            Option<T1> option1,
            Option<T2> option2,
            Option<T3> option3,
            Option<T4> option4,
            Option<T5> option5,
            Option<T6> option6,
            Option<T7> option7,
            Option<T8> option8,
            Option<T9> option9
    ) { return () -> option1.flatMap(
            value1 -> option2.flatMap(
                    value2 -> option3.flatMap(
                            value3 -> option4.flatMap(
                                    value4 -> option5.flatMap(
                                            value5 -> option6.flatMap(
                                                    value6 -> option7.flatMap(
                                                            value7 -> option8.flatMap(
                                                                    value8 -> option9.flatMap(
                                                                            value9 -> present(Tuple.tuple(
                                                                                    value1,
                                                                                    value2,
                                                                                    value3,
                                                                                    value4,
                                                                                    value5,
                                                                                    value6,
                                                                                    value7,
                                                                                    value8,
                                                                                    value9
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
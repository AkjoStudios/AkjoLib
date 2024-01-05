package io.github.akjo03.lib.functional;

import io.github.akjo03.lib.functional.Tuple.Tuple0;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/Unit.java
 */
@SuppressWarnings({"unused", "java:S125", "java:S6548"})
public final class Unit implements Tuple0 {
    @Contract(pure = true)
    private Unit() {}

    private static final Unit UNIT = new Unit();
    private static final Result<Unit> UNIT_RESULT = Result.success(UNIT);

    @Contract(pure = true)
    public static Unit unit() { return UNIT; }

    @Contract(pure = true)
    public static <T1> Unit unit(
            T1 value1
    ) { return UNIT; }

    @Contract(pure = true)
    public static <T1, T2> Unit unit(
            T1 value1,
            T2 value2
    ) { return UNIT; }

    @Contract(pure = true)
    public static <T1, T2, T3> Unit unit(
            T1 value1,
            T2 value2,
            T3 value3
    ) { return UNIT; }

    @Contract(pure = true)
    public static <T1, T2, T3, T4> Unit unit(
            T1 value1,
            T2 value2,
            T3 value3,
            T4 value4
    ) { return UNIT; }

    @Contract(pure = true)
    public static <T1, T2, T3, T4, T5> Unit unit(
            T1 value1,
            T2 value2,
            T3 value3,
            T4 value4,
            T5 value5
    ) { return UNIT; }

    @Contract(pure = true)
    public static <T1, T2, T3, T4, T5, T6> Unit unit(
            T1 value1,
            T2 value2,
            T3 value3,
            T4 value4,
            T5 value5,
            T6 value6
    ) { return UNIT; }

    @Contract(pure = true)
    public static <T1, T2, T3, T4, T5, T6, T7> Unit unit(
            T1 value1,
            T2 value2,
            T3 value3,
            T4 value4,
            T5 value5,
            T6 value6,
            T7 value7
    ) { return UNIT; }

    @Contract(pure = true)
    @SuppressWarnings("java:S107")
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Unit unit(
            T1 value1,
            T2 value2,
            T3 value3,
            T4 value4,
            T5 value5,
            T6 value6,
            T7 value7,
            T8 value8
    ) { return UNIT; }

    @Contract(pure = true)
    @SuppressWarnings("java:S107")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Unit unit(
            T1 value1,
            T2 value2,
            T3 value3,
            T4 value4,
            T5 value5,
            T6 value6,
            T7 value7,
            T8 value8,
            T9 value9
    ) { return UNIT; }

    @Contract(pure = true)
    public static Result<Unit> unitResult() { return UNIT_RESULT; }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "()";
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tuple0;
    }

    @Contract(pure = true)
    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public <T> T map(@NotNull Functions.Function0<T> mapper) {
        return mapper.apply();
    }
}
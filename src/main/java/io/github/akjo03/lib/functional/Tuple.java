package io.github.akjo03.lib.functional;

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
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/Tuple.java
 */
@SuppressWarnings({"unused", "java:S125"})
public interface Tuple {
    // ----- Size -----

    int size();

    // ----- Tuple Variants (0-9) -----

    interface Tuple0 extends Tuple {
        <T> T map(Functions.Function0<T> mapper);

        default int size() { return 0; }
    }

    interface Tuple1<T1> extends Tuple {
        <T> T map(Functions.Function1<T, T1> mapper);

        default int size() { return 1; }
    }

    interface Tuple2<T1, T2> extends Tuple {
        <T> T map(Functions.Function2<T, T1, T2> mapper);

        default int size() { return 2; }

        T1 first();
        T2 last();
    }

    interface Tuple3<T1, T2, T3> extends Tuple {
        <T> T map(Functions.Function3<T, T1, T2, T3> mapper);

        default int size() { return 3; }
    }

    interface Tuple4<T1, T2, T3, T4> extends Tuple {
        <T> T map(Functions.Function4<T, T1, T2, T3, T4> mapper);

        default int size() { return 4; }
    }

    interface Tuple5<T1, T2, T3, T4, T5> extends Tuple {
        <T> T map(Functions.Function5<T, T1, T2, T3, T4, T5> mapper);

        default int size() { return 5; }
    }

    interface Tuple6<T1, T2, T3, T4, T5, T6> extends Tuple {
        <T> T map(Functions.Function6<T, T1, T2, T3, T4, T5, T6> mapper);

        default int size() { return 6; }
    }

    interface Tuple7<T1, T2, T3, T4, T5, T6, T7> extends Tuple {
        <T> T map(Functions.Function7<T, T1, T2, T3, T4, T5, T6, T7> mapper);

        default int size() { return 7; }
    }

    interface Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> extends Tuple {
        <T> T map(Functions.Function8<T, T1, T2, T3, T4, T5, T6, T7, T8> mapper);

        default int size() { return 8; }
    }

    interface Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> extends Tuple {
        <T> T map(Functions.Function9<T, T1, T2, T3, T4, T5, T6, T7, T8, T9> mapper);

        default int size() { return 9; }
    }

    // ----- Tuple Factories (0-9) -----

    @Contract("_ -> new")
    @SuppressWarnings("java:S101")
    static <T1> @NotNull Tuple1<T1> tuple(
            T1 param1
    ) { record tuple1<T1>(
                T1 param1
    ) implements Tuple1<T1> {
            public <T> T map(
                    @NotNull Functions.Function1<T, T1> mapper
            ) { return mapper.apply(
                    param1()
            ); }
    } return new tuple1<>(
            param1
    ); }

    @Contract("_, _ -> new")
    @SuppressWarnings("java:S101")
    static <T1, T2> @NotNull Tuple2<T1, T2> tuple(
            T1 param1,
            T2 param2
    ) { record tuple2<T1, T2>(
                T1 param1,
                T2 param2
    ) implements Tuple2<T1, T2> {
            public <T> T map(
                    @NotNull Functions.Function2<T, T1, T2> mapper
            ) { return mapper.apply(
                    param1(),
                    param2()
            ); }

            @Contract(pure = true)
            public T1 first() { return param1(); }

            @Contract(pure = true)
            public T2 last() { return param2(); }
    } return new tuple2<>(
            param1,
            param2
    ); }

    @Contract("_, _, _ -> new")
    @SuppressWarnings("java:S101")
    static <T1, T2, T3> @NotNull Tuple3<T1, T2, T3> tuple(
            T1 param1,
            T2 param2,
            T3 param3
    ) { record tuple3<T1, T2, T3>(
                T1 param1,
                T2 param2,
                T3 param3
    ) implements Tuple3<T1, T2, T3> {
            public <T> T map(
                    @NotNull Functions.Function3<T, T1, T2, T3> mapper
            ) { return mapper.apply(
                    param1(),
                    param2(),
                    param3()
            ); }
    } return new tuple3<>(
            param1,
            param2,
            param3
    ); }

    @Contract("_, _, _, _ -> new")
    @SuppressWarnings("java:S101")
    static <T1, T2, T3, T4> @NotNull Tuple4<T1, T2, T3, T4> tuple(
            T1 param1,
            T2 param2,
            T3 param3,
            T4 param4
    ) { record tuple4<T1, T2, T3, T4>(
                T1 param1,
                T2 param2,
                T3 param3,
                T4 param4
    ) implements Tuple4<T1, T2, T3, T4> {
            public <T> T map(
                    @NotNull Functions.Function4<T, T1, T2, T3, T4> mapper
            ) { return mapper.apply(
                    param1(),
                    param2(),
                    param3(),
                    param4()
            ); }
    } return new tuple4<>(
            param1,
            param2,
            param3,
            param4
    ); }

    @Contract("_, _, _, _, _ -> new")
    @SuppressWarnings("java:S101")
    static <T1, T2, T3, T4, T5> @NotNull Tuple5<T1, T2, T3, T4, T5> tuple(
            T1 param1,
            T2 param2,
            T3 param3,
            T4 param4,
            T5 param5
    ) { record tuple5<T1, T2, T3, T4, T5>(
                T1 param1,
                T2 param2,
                T3 param3,
                T4 param4,
                T5 param5
    ) implements Tuple5<T1, T2, T3, T4, T5> {
            public <T> T map(
                    @NotNull Functions.Function5<T, T1, T2, T3, T4, T5> mapper
            ) { return mapper.apply(
                    param1(),
                    param2(),
                    param3(),
                    param4(),
                    param5()
            ); }
    } return new tuple5<>(
            param1,
            param2,
            param3,
            param4,
            param5
    ); }

    @Contract("_, _, _, _, _, _ -> new")
    @SuppressWarnings("java:S101")
    static <T1, T2, T3, T4, T5, T6> @NotNull Tuple6<T1, T2, T3, T4, T5, T6> tuple(
            T1 param1,
            T2 param2,
            T3 param3,
            T4 param4,
            T5 param5,
            T6 param6
    ) { record tuple6<T1, T2, T3, T4, T5, T6>(
                T1 param1,
                T2 param2,
                T3 param3,
                T4 param4,
                T5 param5,
                T6 param6
    ) implements Tuple6<T1, T2, T3, T4, T5, T6> {
            public <T> T map(
                    @NotNull Functions.Function6<T, T1, T2, T3, T4, T5, T6> mapper
            ) { return mapper.apply(
                    param1(),
                    param2(),
                    param3(),
                    param4(),
                    param5(),
                    param6()
            ); }
    } return new tuple6<>(
            param1,
            param2,
            param3,
            param4,
            param5,
            param6
    ); }

    @Contract("_, _, _, _, _, _, _ -> new")
    @SuppressWarnings("java:S101")
    static <T1, T2, T3, T4, T5, T6, T7> @NotNull Tuple7<T1, T2, T3, T4, T5, T6, T7> tuple(
            T1 param1,
            T2 param2,
            T3 param3,
            T4 param4,
            T5 param5,
            T6 param6,
            T7 param7
    ) { record tuple7<T1, T2, T3, T4, T5, T6, T7>(
                T1 param1,
                T2 param2,
                T3 param3,
                T4 param4,
                T5 param5,
                T6 param6,
                T7 param7
    ) implements Tuple7<T1, T2, T3, T4, T5, T6, T7> {
            public <T> T map(
                    @NotNull Functions.Function7<T, T1, T2, T3, T4, T5, T6, T7> mapper
            ) { return mapper.apply(
                    param1(),
                    param2(),
                    param3(),
                    param4(),
                    param5(),
                    param6(),
                    param7()
            ); }
    } return new tuple7<>(
            param1,
            param2,
            param3,
            param4,
            param5,
            param6,
            param7
    ); }

    @Contract("_, _, _, _, _, _, _, _ -> new")
    @SuppressWarnings({"java:S107", "java:S101"})
    static <T1, T2, T3, T4, T5, T6, T7, T8> @NotNull Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> tuple(
            T1 param1,
            T2 param2,
            T3 param3,
            T4 param4,
            T5 param5,
            T6 param6,
            T7 param7,
            T8 param8
    ) { record tuple8<T1, T2, T3, T4, T5, T6, T7, T8>(
                T1 param1,
                T2 param2,
                T3 param3,
                T4 param4,
                T5 param5,
                T6 param6,
                T7 param7,
                T8 param8
    ) implements Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> {
            public <T> T map(
                    @NotNull Functions.Function8<T, T1, T2, T3, T4, T5, T6, T7, T8> mapper
            ) { return mapper.apply(
                    param1(),
                    param2(),
                    param3(),
                    param4(),
                    param5(),
                    param6(),
                    param7(),
                    param8()
            ); }
    } return new tuple8<>(
            param1,
            param2,
            param3,
            param4,
            param5,
            param6,
            param7,
            param8
    ); }

    @Contract("_, _, _, _, _, _, _, _, _ -> new")
    @SuppressWarnings({"java:S107", "java:S101"})
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> @NotNull Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> tuple(
            T1 param1,
            T2 param2,
            T3 param3,
            T4 param4,
            T5 param5,
            T6 param6,
            T7 param7,
            T8 param8,
            T9 param9
    ) { record tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>(
                T1 param1,
                T2 param2,
                T3 param3,
                T4 param4,
                T5 param5,
                T6 param6,
                T7 param7,
                T8 param8,
                T9 param9
    ) implements Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> {
            public <T> T map(
                    @NotNull Functions.Function9<T, T1, T2, T3, T4, T5, T6, T7, T8, T9> mapper
            ) { return mapper.apply(
                    param1(),
                    param2(),
                    param3(),
                    param4(),
                    param5(),
                    param6(),
                    param7(),
                    param8(),
                    param9()
            ); }
    } return new tuple9<>(
            param1,
            param2,
            param3,
            param4,
            param5,
            param6,
            param7,
            param8,
            param9
    ); }
}
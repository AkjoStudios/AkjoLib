package io.github.akjo03.lib.functional;

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
 *  This code was moved out of the original file (Option) from the project Pragmatica and was modified.
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/Option.java#L588-L790
 */
@SuppressWarnings({"unused", "java:S125"})
public interface OptionMappers {
    interface Mapper1<T1> {
        Option<Tuple.Tuple1<T1>> identity();

        default <R> Option<R> map(Functions.Function1<R, T1> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Option<R> flatMap(Functions.Function1<Option<R>, T1> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper2<T1, T2> {
        Option<Tuple.Tuple2<T1, T2>> identity();

        default <R> Option<R> map(Functions.Function2<R, T1, T2> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Option<R> flatMap(Functions.Function2<Option<R>, T1, T2> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper3<T1, T2, T3> {
        Option<Tuple.Tuple3<T1, T2, T3>> identity();

        default <R> Option<R> map(Functions.Function3<R, T1, T2, T3> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Option<R> flatMap(Functions.Function3<Option<R>, T1, T2, T3> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper4<T1, T2, T3, T4> {
        Option<Tuple.Tuple4<T1, T2, T3, T4>> identity();

        default <R> Option<R> map(Functions.Function4<R, T1, T2, T3, T4> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Option<R> flatMap(Functions.Function4<Option<R>, T1, T2, T3, T4> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper5<T1, T2, T3, T4, T5> {
        Option<Tuple.Tuple5<T1, T2, T3, T4, T5>> identity();

        default <R> Option<R> map(Functions.Function5<R, T1, T2, T3, T4, T5> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Option<R> flatMap(Functions.Function5<Option<R>, T1, T2, T3, T4, T5> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper6<T1, T2, T3, T4, T5, T6> {
        Option<Tuple.Tuple6<T1, T2, T3, T4, T5, T6>> identity();

        default <R> Option<R> map(Functions.Function6<R, T1, T2, T3, T4, T5, T6> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Option<R> flatMap(Functions.Function6<Option<R>, T1, T2, T3, T4, T5, T6> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper7<T1, T2, T3, T4, T5, T6, T7> {
        Option<Tuple.Tuple7<T1, T2, T3, T4, T5, T6, T7>> identity();

        default <R> Option<R> map(Functions.Function7<R, T1, T2, T3, T4, T5, T6, T7> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Option<R> flatMap(Functions.Function7<Option<R>, T1, T2, T3, T4, T5, T6, T7> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper8<T1, T2, T3, T4, T5, T6, T7, T8> {
        Option<Tuple.Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> identity();

        default <R> Option<R> map(Functions.Function8<R, T1, T2, T3, T4, T5, T6, T7, T8> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Option<R> flatMap(Functions.Function8<Option<R>, T1, T2, T3, T4, T5, T6, T7, T8> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper9<T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        Option<Tuple.Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> identity();

        default <R> Option<R> map(Functions.Function9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Option<R> flatMap(Functions.Function9<Option<R>, T1, T2, T3, T4, T5, T6, T7, T8, T9> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }
}
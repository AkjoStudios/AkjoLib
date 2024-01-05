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
 *  This code was moved out of the original file (Result) from the project Pragmatica and was modified.
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/Result.java#L669-L890
 */
@SuppressWarnings({"unused", "java:S125"})
public interface ResultMappers {
    interface Mapper1<T1> {
        Result<Tuple.Tuple1<T1>> identity();

        default <R> Result<R> map(Functions.Function1<R, T1> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Result<R> flatMap(Functions.Function1<Result<R>, T1> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper2<T1, T2> {
        Result<Tuple.Tuple2<T1, T2>> identity();

        default <R> Result<R> map(Functions.Function2<R, T1, T2> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Result<R> flatMap(Functions.Function2<Result<R>, T1, T2> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper3<T1, T2, T3> {
        Result<Tuple.Tuple3<T1, T2, T3>> identity();

        default <R> Result<R> map(Functions.Function3<R, T1, T2, T3> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Result<R> flatMap(Functions.Function3<Result<R>, T1, T2, T3> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper4<T1, T2, T3, T4> {
        Result<Tuple.Tuple4<T1, T2, T3, T4>> identity();

        default <R> Result<R> map(Functions.Function4<R, T1, T2, T3, T4> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Result<R> flatMap(Functions.Function4<Result<R>, T1, T2, T3, T4> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper5<T1, T2, T3, T4, T5> {
        Result<Tuple.Tuple5<T1, T2, T3, T4, T5>> identity();

        default <R> Result<R> map(Functions.Function5<R, T1, T2, T3, T4, T5> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Result<R> flatMap(Functions.Function5<Result<R>, T1, T2, T3, T4, T5> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper6<T1, T2, T3, T4, T5, T6> {
        Result<Tuple.Tuple6<T1, T2, T3, T4, T5, T6>> identity();

        default <R> Result<R> map(Functions.Function6<R, T1, T2, T3, T4, T5, T6> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Result<R> flatMap(Functions.Function6<Result<R>, T1, T2, T3, T4, T5, T6> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper7<T1, T2, T3, T4, T5, T6, T7> {
        Result<Tuple.Tuple7<T1, T2, T3, T4, T5, T6, T7>> identity();

        default <R> Result<R> map(Functions.Function7<R, T1, T2, T3, T4, T5, T6, T7> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Result<R> flatMap(Functions.Function7<Result<R>, T1, T2, T3, T4, T5, T6, T7> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper8<T1, T2, T3, T4, T5, T6, T7, T8> {
        Result<Tuple.Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> identity();

        default <R> Result<R> map(Functions.Function8<R, T1, T2, T3, T4, T5, T6, T7, T8> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Result<R> flatMap(Functions.Function8<Result<R>, T1, T2, T3, T4, T5, T6, T7, T8> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }

    interface Mapper9<T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        Result<Tuple.Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> identity();

        default <R> Result<R> map(Functions.Function9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> mapper) {
            return identity().map(tuple -> tuple.map(mapper));
        }

        default <R> Result<R> flatMap(Functions.Function9<Result<R>, T1, T2, T3, T4, T5, T6, T7, T8, T9> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper));
        }
    }
}
package io.github.akjo03.lib.functional.async;

import static io.github.akjo03.lib.functional.Functions.*;
import static io.github.akjo03.lib.functional.Tuple.*;

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
 *  This code was moved out of the original file (Promise) from the project Pragmatica and was modified.
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/Promise.java#L561-L725
 */
@SuppressWarnings({"unused", "java:S125"})
public interface PromiseMappers {
    interface Mapper1<T1> {
        Promise<Tuple1<T1>> identity();

        default <R> Promise<R> map(Function1<R, T1> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise());
        }

        default <R> Promise<R> flatMap(Function1<Promise<R>, T1> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise().flatMap(Function1.identity()));
        }
    }

    interface Mapper2<T1, T2> {
        Promise<Tuple2<T1, T2>> identity();

        default <R> Promise<R> map(Function2<R, T1, T2> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise());
        }

        default <R> Promise<R> flatMap(Function2<Promise<R>, T1, T2> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise().flatMap(Function1.identity()));
        }
    }

    interface Mapper3<T1, T2, T3> {
        Promise<Tuple3<T1, T2, T3>> identity();

        default <R> Promise<R> map(Function3<R, T1, T2, T3> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise());
        }

        default <R> Promise<R> flatMap(Function3<Promise<R>, T1, T2, T3> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise().flatMap(Function1.identity()));
        }
    }

    interface Mapper4<T1, T2, T3, T4> {
        Promise<Tuple4<T1, T2, T3, T4>> identity();

        default <R> Promise<R> map(Function4<R, T1, T2, T3, T4> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise());
        }

        default <R> Promise<R> flatMap(Function4<Promise<R>, T1, T2, T3, T4> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise().flatMap(Function1.identity()));
        }
    }

    interface Mapper5<T1, T2, T3, T4, T5> {
        Promise<Tuple5<T1, T2, T3, T4, T5>> identity();

        default <R> Promise<R> map(Function5<R, T1, T2, T3, T4, T5> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise());
        }

        default <R> Promise<R> flatMap(Function5<Promise<R>, T1, T2, T3, T4, T5> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise().flatMap(Function1.identity()));
        }
    }

    interface Mapper6<T1, T2, T3, T4, T5, T6> {
        Promise<Tuple6<T1, T2, T3, T4, T5, T6>> identity();

        default <R> Promise<R> map(Function6<R, T1, T2, T3, T4, T5, T6> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise());
        }

        default <R> Promise<R> flatMap(Function6<Promise<R>, T1, T2, T3, T4, T5, T6> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise().flatMap(Function1.identity()));
        }
    }

    interface Mapper7<T1, T2, T3, T4, T5, T6, T7> {
        Promise<Tuple7<T1, T2, T3, T4, T5, T6, T7>> identity();

        default <R> Promise<R> map(Function7<R, T1, T2, T3, T4, T5, T6, T7> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise());
        }

        default <R> Promise<R> flatMap(Function7<Promise<R>, T1, T2, T3, T4, T5, T6, T7> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise().flatMap(Function1.identity()));
        }
    }

    interface Mapper8<T1, T2, T3, T4, T5, T6, T7, T8> {
        Promise<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> identity();

        default <R> Promise<R> map(Function8<R, T1, T2, T3, T4, T5, T6, T7, T8> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise());
        }

        default <R> Promise<R> flatMap(Function8<Promise<R>, T1, T2, T3, T4, T5, T6, T7, T8> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise().flatMap(Function1.identity()));
        }
    }

    interface Mapper9<T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        Promise<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> identity();

        default <R> Promise<R> map(Function9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise());
        }

        default <R> Promise<R> flatMap(Function9<Promise<R>, T1, T2, T3, T4, T5, T6, T7, T8, T9> mapper) {
            return identity().flatMap(tuple -> tuple.map(mapper).toPromise().flatMap(Function1.identity()));
        }
    }
}
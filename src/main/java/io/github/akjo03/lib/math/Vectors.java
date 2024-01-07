package io.github.akjo03.lib.math;

import io.github.akjo03.lib.functional.Result;
import org.jetbrains.annotations.NotNull;

import static io.github.akjo03.lib.functional.Tuple.*;

/*
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
 */
@SuppressWarnings({"unused", "java:S125", "java:S100"})
public interface Vectors {
    static Result<Vector2> _2(@NotNull Tuple2<Double, Double> tuple) {
        return tuple.map(Vector2::new);
    }

    static Result<Vector2> _2(@NotNull Tuple3<Double, Double, Double> tuple) {
        return tuple.map((x, y, z) -> new Vector2(x, y));
    }

    static Result<Vector3> _3(@NotNull Tuple2<Double, Double> tuple) {
        return tuple.map((x, y) -> new Vector3(x, y, 0));
    }

    static Result<Vector3> _3(@NotNull Tuple3<Double, Double, Double> tuple) {
        return tuple.map(Vector3::new);
    }
}
package io.github.akjo03.lib.functional;

import io.github.akjo03.lib.functional.async.Promise;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


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
@FunctionalInterface
@SuppressWarnings({"unused", "UnusedReturnValue", "java:S125", "java:S100"})
public interface Wrapper<T> {
    T value();

    default Result<T> _R() {
        return Result.lift(this::value);
    }

    default Result<T> _R(Cause cause) {
        return Result.lift(cause, this::value);
    }

    default Option<T> _O() {
        return Option.option(this::value);
    }

    default Promise<T> _P(Consumer<Promise<T>> consumer) {
        return Promise.promise(consumer).success(this.value());
    }

    record WrapperImpl<T>(T value) implements Wrapper<T> {}

    @Contract("_ -> new")
    static <T> @NotNull Wrapper<T> wrap(T value) {
        return new WrapperImpl<>(value);
    }
}
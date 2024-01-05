package io.github.akjo03.lib.functional.util;

import io.github.akjo03.lib.functional.Result;
import io.github.akjo03.lib.validation.Validators;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static io.github.akjo03.lib.functional.util.Causes.*;

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
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/utils/ResultCollector.java
 */
@SuppressWarnings({"unused", "java:S125"})
public record ResultCollector(Object[] results, AtomicInteger counter, Consumer<Object[]> action) {
    @Contract("_, _ -> new")
    public static @NotNull Result<ResultCollector> resultCollector(int threshold, Consumer<Object[]> action) {
        return Validators.isGreaterThanOrEqualTo(
                () -> 0,
                value -> cause("Threshold of ResultCollector must be greater than or equal to 0, but was {0}!", value)
        ).validate(() -> threshold).map(
                actualThreshold -> new ResultCollector(
                        new Object[actualThreshold],
                        new AtomicInteger(actualThreshold),
                        action
                )
        );
    }

    @Contract("_ -> this")
    public ResultCollector apply(@NotNull Consumer<ResultCollector> setup) {
        setup.accept(this);
        return this;
    }

    public void registerEvent(int index, Object obj) {
        if (counter.get() <= 0) { return; }

        results[index] = Validators.isNull(
                value -> cause("ResultCollector already has a value at index {0}!", index)
        ).validate(() -> results[index]).map(
                value -> obj
        ).or(() -> results[index]);

        if (counter.decrementAndGet() == 0) { action.accept(results); }
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultCollector that = (ResultCollector) o;
        return Objects.equals(counter.get(), that.counter.get()) && Arrays.equals(results, that.results);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(counter, action);
        result = 31 * result + Arrays.hashCode(results);
        return result;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "ResultCollector" + Arrays.toString(results) + "(" + counter + ")";
    }
}
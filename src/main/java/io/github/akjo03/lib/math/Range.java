package io.github.akjo03.lib.math;

import io.github.akjo03.lib.functional.Functions;
import io.github.akjo03.lib.functional.Result;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
@SuppressWarnings({"unused", "java:S125"})
public class Range<T extends Number & Comparable<T>> {
    private final T min;
    private final T max;

    @Contract("_, _ -> new")
    public static <T extends Number & Comparable<T>> @NotNull Range<T> of(@NotNull T min, @NotNull T max) {
        return min.compareTo(max) < 0
                ? new Range<>(min, max)
                : new Range<>(max, min);
    }

    public boolean contains(@NotNull T number) {
        boolean lowerCheck = number.compareTo(min) >= 0;
        boolean upperCheck = number.compareTo(max) <= 0;
        return lowerCheck && upperCheck;
    }

    public boolean contains(@NotNull Range<T> range) {
        boolean lowerCheck = range.min().compareTo(min) >= 0;
        boolean upperCheck = range.max().compareTo(max) <= 0;
        return lowerCheck && upperCheck;
    }

    public boolean overlaps(@NotNull Range<T> range) {
        boolean lowerCheck = this.min.compareTo(range.max) <= 0;
        boolean upperCheck = this.max.compareTo(range.min) >= 0;
        return lowerCheck && upperCheck;
    }

    public boolean isAdjacent(@NotNull Range<T> range) {
        boolean lowerCheck = range.min().compareTo(max) == 0;
        boolean upperCheck = range.max().compareTo(min) == 0;
        return lowerCheck || upperCheck;
    }

    public boolean isDisjoint(@NotNull Range<T> range) {
        boolean lowerCheck = range.min().compareTo(max) > 0;
        boolean upperCheck = range.max().compareTo(min) < 0;
        return lowerCheck || upperCheck;
    }

    public <C> Result<C> checkRange(
            @NotNull T number,
            @NotNull Functions.Function0<Result<C>> belowSupplier,
            @NotNull Functions.Function0<Result<C>> aboveSupplier,
            @NotNull Result<C> defaultValue
    ) {
        boolean lowerCheck = number.compareTo(min) < 0;
        boolean upperCheck = number.compareTo(max) > 0;

        if (lowerCheck) {
            return belowSupplier.apply();
        }
        if (upperCheck) {
            return aboveSupplier.apply();
        }

        return defaultValue;
    }
}
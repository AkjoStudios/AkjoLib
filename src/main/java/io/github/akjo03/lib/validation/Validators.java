package io.github.akjo03.lib.validation;

import io.github.akjo03.lib.functional.Cause;
import io.github.akjo03.lib.math.Range;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

import static io.github.akjo03.lib.functional.Functions.Function0;
import static io.github.akjo03.lib.functional.Functions.Function1;
import static io.github.akjo03.lib.functional.Result.failure;
import static io.github.akjo03.lib.functional.Result.success;

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
@SuppressWarnings({"unused", "java:S125"})
public interface Validators {
    @Contract(pure = true)
    static <T> @NotNull Validator<T> isNotNull(
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value != null
                    ? success(value)
                    : failure(causeSupplier.apply(null));
    }; }

    @Contract(pure = true)
    static <T, M> @NotNull Validator<T> isNotNull(
            Function1<M, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue != null
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> isNull(
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value == null
                    ? success(null)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M> @NotNull Validator<T> isNull(
            Function1<M, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue == null
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> doesEqual(
            Function0<T> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return Objects.equals(value, expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M> @NotNull Validator<T> doesEqual(
            Function1<M, T> mapper,
            Function0<M> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return Objects.equals(mappedValue, expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> doesNotEqual(
            Function0<T> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return !Objects.equals(value, expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M> @NotNull Validator<T> doesNotEqual(
            Function1<M, T> mapper,
            Function0<M> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return !Objects.equals(mappedValue, expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Boolean> isTrue(
            Function1<Cause, Boolean> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value
                    ? success(true)
                    : failure(causeSupplier.apply(false));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> isTrue(
            Function1<Boolean, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Boolean> isFalse(
            Function1<Cause, Boolean> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return !value
                    ? success(false)
                    : failure(causeSupplier.apply(true));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> isFalse(
            Function1<Boolean, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return !mappedValue
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T extends Number & Comparable<T>> @NotNull Validator<T> isGreaterThan(
            Function0<T> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.compareTo(expectedSupplier.apply()) > 0
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M extends Number & Comparable<M>> @NotNull Validator<T> isGreaterThan(
            Function1<M, T> mapper,
            Function0<M> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.compareTo(expectedSupplier.apply()) > 0
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T extends Number & Comparable<T>> @NotNull Validator<T> isGreaterThanOrEqualTo(
            Function0<T> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.compareTo(expectedSupplier.apply()) >= 0
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M extends Number & Comparable<M>> @NotNull Validator<T> isGreaterThanOrEqualTo(
            Function1<M, T> mapper,
            Function0<M> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.compareTo(expectedSupplier.apply()) >= 0
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T extends Number & Comparable<T>> @NotNull Validator<T> isLessThan(
            Function0<T> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.compareTo(expectedSupplier.apply()) < 0
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M extends Number & Comparable<M>> @NotNull Validator<T> isLessThan(
            Function1<M, T> mapper,
            Function0<M> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.compareTo(expectedSupplier.apply()) < 0
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T extends Number & Comparable<T>> @NotNull Validator<T> isLessThanOrEqualTo(
            Function0<T> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.compareTo(expectedSupplier.apply()) <= 0
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M extends Number & Comparable<M>> @NotNull Validator<T> isLessThanOrEqualTo(
            Function1<M, T> mapper,
            Function0<M> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.compareTo(expectedSupplier.apply()) <= 0
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T extends Number & Comparable<T>> @NotNull Validator<T> isInRange(
            Function0<Range<T>> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return expectedSupplier.apply().contains(value)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M extends Number & Comparable<M>> @NotNull Validator<T> isInRange(
            Function1<M, T> mapper,
            Function0<Range<M>> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return expectedSupplier.apply().contains(mappedValue)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T extends Number & Comparable<T>> @NotNull Validator<T> isOutsideRange(
            Function0<Range<T>> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return !expectedSupplier.apply().contains(value)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M extends Number & Comparable<M>> @NotNull Validator<T> isOutsideRange(
            Function1<M, T> mapper,
            Function0<Range<M>> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return !expectedSupplier.apply().contains(mappedValue)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T extends Number & Comparable<T>> @NotNull Validator<T> isPositive(
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.compareTo(zero(value)) > 0
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M extends Number & Comparable<M>> @NotNull Validator<T> isPositive(
            Function1<M, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.compareTo(zero(mappedValue)) > 0
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T extends Number & Comparable<T>> @NotNull Validator<T> isNegative(
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.compareTo(zero(value)) < 0
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M extends Number & Comparable<M>> @NotNull Validator<T> isNegative(
            Function1<M, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.compareTo(zero(mappedValue)) < 0
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T extends Enum<T>> @NotNull Validator<String> isEnumValue(
            Class<T> enumClass,
            Function1<Cause, String> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return Function0.of(causeSupplier.apply(value), () -> {
                Enum.valueOf(enumClass, value);
                return value;
            }).apply();
    }; }

    @Contract(pure = true)
    static <T, M extends Enum<M>> @NotNull Validator<T> isEnumValue(
            Function1<M, T> mapper,
            Class<M> enumClass,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return Function0.of(causeSupplier.apply(value), () -> {
                Enum.valueOf(enumClass, mappedValue.toString());
                return value;
            }).apply();
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> isInstanceOf(
            Class<?> expectedClass,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return expectedClass.isInstance(value)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M> @NotNull Validator<T> isInstanceOf(
            Function1<M, T> mapper,
            Class<M> expectedClass,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return expectedClass.isInstance(mappedValue)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<String> stringIsNotEmpty(
            Function1<Cause, String> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return !value.isEmpty()
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> stringIsNotEmpty(
            Function1<String, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return !mappedValue.isEmpty()
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<String> stringIsNotBlank(
            Function1<Cause, String> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return !value.isBlank()
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> stringIsNotBlank(
            Function1<String, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return !mappedValue.isBlank()
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<String> stringStartsWith(
            Function0<String> expectedSupplier,
            Function1<Cause, String> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.startsWith(expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> stringStartsWith(
            Function1<String, T> mapper,
            Function0<String> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.startsWith(expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<String> stringEndsWith(
            Function0<String> expectedSupplier,
            Function1<Cause, String> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.endsWith(expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> stringEndsWith(
            Function1<String, T> mapper,
            Function0<String> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.endsWith(expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<String> stringDoesMatch(
            Function0<String> regexSupplier,
            Function1<Cause, String> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.matches(regexSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> stringDoesMatch(
            Function1<String, T> mapper,
            Function0<String> regexSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.matches(regexSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<String> stringDoesNotMatch(
            Function0<String> regexSupplier,
            Function1<Cause, String> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return !value.matches(regexSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> stringDoesNotMatch(
            Function1<String, T> mapper,
            Function0<String> regexSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return !mappedValue.matches(regexSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<Collection<T>> collectionIsNotEmpty(
            Function1<Cause, Collection<T>> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return !value.isEmpty()
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M> @NotNull Validator<Collection<T>> collectionIsNotEmpty(
            Function1<Collection<M>, Collection<T>> mapper,
            Function1<Cause, Collection<T>> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return !mappedValue.isEmpty()
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<Collection<T>> collectionIsEmpty(
            Function1<Cause, Collection<T>> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.isEmpty()
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M> @NotNull Validator<Collection<T>> collectionIsEmpty(
            Function1<Collection<M>, Collection<T>> mapper,
            Function1<Cause, Collection<T>> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.isEmpty()
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<Collection<T>> collectionContainsNoneOf(
            Function0<Collection<T>> expectedSupplier,
            Function1<Cause, Collection<T>> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.stream().noneMatch(expectedSupplier.apply()::contains)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M> @NotNull Validator<Collection<T>> collectionContainsNoneOf(
            Function1<Collection<M>, Collection<T>> mapper,
            Function0<Collection<M>> expectedSupplier,
            Function1<Cause, Collection<T>> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.stream().noneMatch(expectedSupplier.apply()::contains)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<Collection<T>> collectionContainsAnyOf(
            Function0<Collection<T>> expectedSupplier,
            Function1<Cause, Collection<T>> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.stream().anyMatch(expectedSupplier.apply()::contains)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M> @NotNull Validator<Collection<T>> collectionContainsAnyOf(
            Function1<Collection<M>, Collection<T>> mapper,
            Function0<Collection<M>> expectedSupplier,
            Function1<Cause, Collection<T>> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.stream().anyMatch(expectedSupplier.apply()::contains)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<Collection<T>> collectionContainsAllOf(
            Function0<Collection<T>> expectedSupplier,
            Function1<Cause, Collection<T>> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.containsAll(expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M> @NotNull Validator<Collection<T>> collectionContainsAllOf(
            Function1<Collection<M>, Collection<T>> mapper,
            Function0<Collection<M>> expectedSupplier,
            Function1<Cause, Collection<T>> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.containsAll(expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<Collection<T>> collectionContainsExactly(
            Function0<Collection<T>> expectedSupplier,
            Function1<Cause, Collection<T>> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.containsAll(expectedSupplier.apply()) && expectedSupplier.apply().containsAll(value)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T, M> @NotNull Validator<Collection<T>> collectionContainsExactly(
            Function1<Collection<M>, Collection<T>> mapper,
            Function0<Collection<M>> expectedSupplier,
            Function1<Cause, Collection<T>> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.containsAll(expectedSupplier.apply()) && expectedSupplier.apply().containsAll(mappedValue)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Instant> instantIsBefore(
            Function0<Instant> expectedSupplier,
            Function1<Cause, Instant> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.isBefore(expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> instantIsBefore(
            Function1<Instant, T> mapper,
            Function0<Instant> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.isBefore(expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Instant> instantIsAfter(
            Function0<Instant> expectedSupplier,
            Function1<Cause, Instant> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.isAfter(expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> instantIsAfter(
            Function1<Instant, T> mapper,
            Function0<Instant> expectedSupplier,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.isAfter(expectedSupplier.apply())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Instant> instantIsInPast(
            Function1<Cause, Instant> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.isBefore(Instant.now())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> instantIsInPast(
            Function1<Instant, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.isBefore(Instant.now())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Instant> instantIsInFuture(
            Function1<Cause, Instant> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return value.isAfter(Instant.now())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> instantIsInFuture(
            Function1<Instant, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return mappedValue.isAfter(Instant.now())
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Path> pathExists(
            Function1<Cause, Path> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return Files.exists(value)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> pathExists(
            Function1<Path, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return Files.exists(mappedValue)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Path> pathDoesNotExist(
            Function1<Cause, Path> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return Files.notExists(value)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> pathDoesNotExist(
            Function1<Path, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return Files.notExists(mappedValue)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Path> pathIsDirectory(
            Function1<Cause, Path> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return Files.isDirectory(value)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> pathIsDirectory(
            Function1<Path, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return Files.isDirectory(mappedValue)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Path> pathIsRegularFile(
            Function1<Cause, Path> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return Files.isRegularFile(value)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> pathIsRegularFile(
            Function1<Path, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return Files.isRegularFile(mappedValue)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Path> pathIsSymbolicLink(
            Function1<Cause, Path> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return Files.isSymbolicLink(value)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> pathIsSymbolicLink(
            Function1<Path, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return Files.isSymbolicLink(mappedValue)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Path> pathIsReadable(
            Function1<Cause, Path> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return Files.isReadable(value)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> pathIsReadable(
            Function1<Path, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return Files.isReadable(mappedValue)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Path> pathIsWritable(
            Function1<Cause, Path> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return Files.isWritable(value)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> pathIsWritable(
            Function1<Path, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return Files.isWritable(mappedValue)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static @NotNull Validator<Path> pathIsExecutable(
            Function1<Cause, Path> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            return Files.isExecutable(value)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @Contract(pure = true)
    static <T> @NotNull Validator<T> pathIsExecutable(
            Function1<Path, T> mapper,
            Function1<Cause, T> causeSupplier
    ) { return supplier -> {
            var value = supplier.apply();
            var mappedValue = mapper.apply(value);
            return Files.isExecutable(mappedValue)
                    ? success(value)
                    : failure(causeSupplier.apply(value));
    }; }

    @SuppressWarnings("unchecked")
    private static <T extends Number & Comparable<T>> T zero(T value) {
        return switch (value) {
            case null -> null;
            case Integer __ -> (T) Integer.valueOf(0);
            case Long __ -> (T) Long.valueOf(0);
            case Float __ -> (T) Float.valueOf(0);
            case Short __ -> (T) Short.valueOf((short) 0);
            case Byte __ -> (T) Byte.valueOf((byte) 0);
            default -> (T) Double.valueOf(0);
        };
    }
}
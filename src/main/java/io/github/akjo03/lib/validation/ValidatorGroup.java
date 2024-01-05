package io.github.akjo03.lib.validation;

import io.github.akjo03.lib.functional.Functions;
import io.github.akjo03.lib.functional.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
@SuppressWarnings({"unused", "java:S125", "UnusedReturnValue"})
public record ValidatorGroup<T>(List<Validator<T>> validators) {
    public Result<T> validate(T value, Functions.Function1<T, List<T>> mapper) {
        return Result.aggregate(validators.stream()
                .map(validator -> validator.validate(Functions.supplier(value)))
                .toList()
        ).map(mapper);
    }

    public Result<T> validate(T value) {
        return validate(value, __ -> value);
    }

    public Result<T> validate(Functions.Function0<T> supplier, Functions.Function1<T, List<T>> mapper) {
        return Result.aggregate(validators.stream()
                .map(validator -> validator.validate(supplier))
                .toList()
        ).map(mapper);
    }

    public Result<T> validate(Functions.Function0<T> supplier) {
        return validate(supplier, __ -> supplier.apply());
    }

    @Contract("_ -> new")
    public static <T> @NotNull ValidatorGroup<T> of(List<Validator<T>> validators) {
        return new ValidatorGroup<>(validators);
    }

    @Contract("_ -> new")
    @SafeVarargs
    public static <T> @NotNull ValidatorGroup<T> of(Validator<T>... validators) {
        return new ValidatorGroup<>(List.of(validators));
    }
}
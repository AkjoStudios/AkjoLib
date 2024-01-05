package io.github.akjo03.lib.functional.util;

import io.github.akjo03.lib.functional.Cause;
import io.github.akjo03.lib.functional.Option;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
public record AggregatedCause(List<Cause> causes) implements Cause {
    @Override
    public String message() {
        return causes.stream()
                .map(Cause::message)
                .collect(Collectors.joining(", "));
    }

    @Override
    public @NotNull Option<Cause> source() {
        return Option.from(causes.stream()
                .map(Cause::source)
                .flatMap(Option::stream)
                .reduce((cause1, cause2) -> new AggregatedCause(Arrays.asList(cause1, cause2)))
        );
    }
}
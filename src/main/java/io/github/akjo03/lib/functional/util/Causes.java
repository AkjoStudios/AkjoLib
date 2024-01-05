package io.github.akjo03.lib.functional.util;

import io.github.akjo03.lib.functional.Cause;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

import static io.github.akjo03.lib.functional.Option.empty;
import static io.github.akjo03.lib.functional.Option.present;

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
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/utils/Causes.java
 */
@SuppressWarnings({"unused", "java:S125"})
public final class Causes {
    @Contract(pure = true)
    private Causes() {}

    public static final Cause IRRELEVANT = new SimpleCause("irrelevant", empty());

    @Contract("_ -> new")
    public static @NotNull Cause cause(String message) {
        return new SimpleCause(message, empty());
    }

    @Contract("_, _ -> new")
    public static @NotNull Cause cause(String message, Cause source) {
        return new SimpleCause(message, present(source));
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull Cause cause(String template, Object... args) {
        return cause(MessageFormat.format(template, args));
    }

    @Contract("_, _, _ -> new")
    public static <T> @NotNull Cause cause(String template, Cause source, Object... args) {
        return cause(MessageFormat.format(template, args), source);
    }

    public static @NotNull Cause fromThrowable(@NotNull Throwable throwable) {
        var sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));

        return cause(sw.toString());
    }


}
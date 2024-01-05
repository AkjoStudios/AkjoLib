package io.github.akjo03.lib.console;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@SuppressWarnings({"unused", "java:S125"})
public enum ConsoleColor {
    BLACK("\033[%c;30m"),
    RED("\033[%c;31m"),
    GREEN("\033[%c;32m"),
    YELLOW("\033[%c;33m"),
    BLUE("\033[%c;34m"),
    PURPLE("\033[%c;35m"),
    CYAN("\033[%c;36m"),
    WHITE("\033[%c;37m"),
    BLACK_BRIGHT("\033[%c;90m"),
    RED_BRIGHT("\033[%c;91m"),
    GREEN_BRIGHT("\033[%c;92m"),
    YELLOW_BRIGHT("\033[%c;93m"),
    BLUE_BRIGHT("\033[%c;94m"),
    PURPLE_BRIGHT("\033[%c;95m"),
    CYAN_BRIGHT("\033[%c;96m"),
    WHITE_BRIGHT("\033[%c;97m"),
    RESET("\033[0m");

    private final String code;

    @Contract(pure = true)
    public @NotNull String code(boolean bold) {
        return String.format(code, bold ? '1' : '0');
    }

    @Contract(pure = true)
    public @NotNull String code() {
        return code(false);
    }

    public @NotNull String colorize(@NotNull String text, boolean bold) {
        return code(bold) + text + RESET.code();
    }

    public @NotNull String colorize(@NotNull String text) {
        return colorize(text, false);
    }
}
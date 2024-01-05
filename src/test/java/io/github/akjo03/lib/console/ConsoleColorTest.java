package io.github.akjo03.lib.console;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
@SuppressWarnings("java:S125")
class ConsoleColorTest {
    @Test
    void testCode() {
        assertEquals("\033[0;30m", ConsoleColor.BLACK.code());
        assertEquals("\033[0;30m", ConsoleColor.BLACK.code(false));
        assertEquals("\033[1;30m", ConsoleColor.BLACK.code(true));
    }

    @Test
    void testColorize() {
        String text = "Hello, World!";
        assertEquals("\033[0;30m" + text + "\033[0m", ConsoleColor.BLACK.colorize(text));
        assertEquals("\033[0;30m" + text + "\033[0m", ConsoleColor.BLACK.colorize(text, false));
        assertEquals("\033[1;30m" + text + "\033[0m", ConsoleColor.BLACK.colorize(text, true));
    }
}
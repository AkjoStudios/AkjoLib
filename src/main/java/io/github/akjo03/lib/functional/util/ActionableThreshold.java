package io.github.akjo03.lib.functional.util;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

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
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/utils/ActionableThreshold.java
 */
@SuppressWarnings({"unused", "java:S125"})
public record ActionableThreshold(AtomicInteger counter, Runnable action) {
    @Contract("_, _ -> new")
    public static @NotNull ActionableThreshold threshold(int count, Runnable action) {
        return new ActionableThreshold(new AtomicInteger(count), action);
    }

    @Contract("_ -> this")
    public ActionableThreshold apply(@NotNull Consumer<ActionableThreshold> setup) {
        setup.accept(this);
        return this;
    }

    public void registerEvent() {
        if (counter.get() <= 0) { return; }
        if (counter.decrementAndGet() == 0) { action.run(); }
    }
}
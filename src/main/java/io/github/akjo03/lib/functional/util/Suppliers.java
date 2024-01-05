package io.github.akjo03.lib.functional.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

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
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/utils/Suppliers.java
 */
@SuppressWarnings({"unused", "java:S125"})
public final class Suppliers {
    @Contract(pure = true)
    private Suppliers() {}

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Supplier<T> memoize(Supplier<T> factory) {
        return new MemoizingSupplier<>(factory);
    }

    private static class MemoizingSupplier<T> implements Supplier<T> {
        private final Supplier<T> initializer = this::initialize;

        @SuppressWarnings("java:S3077")
        private volatile Supplier<T> delegate = initializer;

        private Supplier<T> factory;

        @Contract(pure = true)
        public MemoizingSupplier(Supplier<T> factory) {
            this.factory = factory;
        }

        private synchronized T initialize() {
            if (delegate == initializer) { return delegate.get(); }

            var value = factory.get();
            delegate = () -> value;
            factory = null;
            return value;
        }

        @Override
        public T get() { return delegate.get(); }
    }
}
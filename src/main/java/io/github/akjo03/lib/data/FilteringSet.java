package io.github.akjo03.lib.data;

import io.github.akjo03.lib.functional.Functions;
import io.github.akjo03.lib.functional.Result;
import io.github.akjo03.lib.functional.build.Constructor;
import io.github.akjo03.lib.validation.Validators;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.akjo03.lib.functional.util.Causes.cause;

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
@SuppressWarnings({"unused", "java:S125"})
public class FilteringSet<T> extends AbstractSet<T> {
    private final Set<T> original;
    private final Set<T> filters;
    private final int size;

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FilteringSetConstructor<T> implements Constructor<FilteringSet<T>> {
        private final Set<T> original;
        private final Set<T> filters;

        @Override
        public @NotNull FilteringSet<T> build() {
            return new FilteringSet<>(original, filters, original.size() - filters.size());
        }

        @Override
        public @NotNull Result<FilteringSet<T>> validate(@NotNull FilteringSet<T> constructable) {
            return Validators.collectionContainsAllOf(
                    Functions.supplier(constructable.filters),
                    __ -> cause("FilteringSet should contain all of the filters!")
            ).validate(Functions.supplier(constructable.original)).map(__ -> constructable);
        }
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull FilteringSetConstructor<T> constructor(Set<T> original, Set<T> filters) {
        return new FilteringSetConstructor<>(original, filters);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new Iterator<>() {
            private final Iterator<T> originalIterator = original.iterator();
            private T next = advance();

            private @Nullable T advance() {
                while (originalIterator.hasNext()) {
                    T originalNext = originalIterator.next();
                    if (isElementVisible(originalNext)) {
                        return originalNext;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public T next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                T result = next;
                next = advance();
                return result;
            }
        };
    }

    @Contract("null -> false")
    private boolean isElementVisible(T element) {
        return element != null && original.contains(element) && !filters.contains(element);
    }

    @Override
    public int size() {
        return (int) original.stream().filter(this::isElementVisible).count();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (T element : original) {
            if (isElementVisible(element)) {
                action.accept(element);
            }
        }
    }

    @Contract(pure = true)
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return Functions.Function0.of(
                (Functions.ThrowingSupplier<Boolean>) () -> isElementVisible((T) o)
        ).apply().or(false);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    @Override
    public Object@NotNull[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (T element : this) {
            array[i++] = element;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K> K@NotNull[] toArray(K@NotNull[] other) {
        if (other.length < size) {
            other = Arrays.copyOf(other, size);
        }
        int i = 0;
        for (T element : this) {
            other[i++] = (K) element;
        }
        return other;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object object) {
        if (object == this) { return true; }
        if (!(object instanceof Set<?> set)) { return false; }
        if (set.size() != size()) { return false; }

        return containsAll(set);
    }

    @Override
    public int hashCode() {
        return stream().mapToInt(k -> 31 * 17 + k.hashCode()).sum();
    }

    @Override
    public String toString() {
        return stream().map(Objects::toString).collect(Collectors.joining(", ", "{", "}"));
    }

    @Override
    public Stream<T> stream() {
        return original.stream().filter(this::isElementVisible);
    }

    @Override
    public Stream<T> parallelStream() {
        return original.parallelStream().filter(this::isElementVisible);
    }
}
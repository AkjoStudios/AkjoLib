package io.github.akjo03.lib.data;

import io.github.akjo03.lib.functional.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

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
class FilteringSetTest {
    private Set<Integer> original;
    private FilteringSet<Integer> filteringSet;

    @BeforeEach
    void setUp() {
        original = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        Set<Integer> filters = new HashSet<>(Arrays.asList(2, 4));
        filteringSet = FilteringSet.constructor(original, filters).construct().or((FilteringSet<Integer>) null);
    }

    @Test
    void testOf_withFiltersNotInOriginal_resultShouldBeFalse() {
        Set<Integer> faultyFilters = new HashSet<>(Arrays.asList(7, 8));
        Result<FilteringSet<Integer>> result = FilteringSet
                .constructor(original, faultyFilters)
                .construct();
        assertTrue(result.isFailure());
    }

    @Test
    void testSize() {
        assertEquals(3, filteringSet.size());
    }

    @Test
    @SuppressWarnings({"UseBulkOperation", "WhileLoopReplaceableByForEach"})
    void testIterator_hasNextAndNext() {
        Set<Integer> expectedElements = new HashSet<>(Arrays.asList(1, 3, 5));
        Set<Integer> actualElements = new HashSet<>();
        var iterator = filteringSet.iterator();
        while (iterator.hasNext()) {
            actualElements.add(iterator.next());
        }
        assertEquals(expectedElements, actualElements);
    }

    @Test
    void testIterator_nextWithoutHasNext_shouldThrowException() {
        var iterator = filteringSet.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @SuppressWarnings("UseBulkOperation")
    void testForEach() {
        Set<Integer> expectedElements = new HashSet<>(Arrays.asList(1, 3, 5));
        Set<Integer> actualElements = new HashSet<>();
        filteringSet.forEach(actualElements::add);
        assertEquals(expectedElements, actualElements);
    }

    @Test
    void testContains() {
        assertTrue(filteringSet.contains(1));
        assertFalse(filteringSet.contains(2));
    }

    @Test
    void testToArray() {
        Object[] expectedArray = {1, 3, 5};
        assertArrayEquals(expectedArray, filteringSet.toArray());
    }

    @Test
    void testToArray_withGivenArray() {
        Integer[] array = new Integer[5];
        Integer[] expectedArray = {1, 3, 5, null, null};
        assertArrayEquals(expectedArray, filteringSet.toArray(array));
    }

    @Test
    void testEquals() {
        Set<Integer> otherSet = new HashSet<>(Arrays.asList(1, 3, 5));
        assertEquals(filteringSet, otherSet);
    }

    @Test
    void testHashCode() {
        Set<Integer> otherSetElements = new HashSet<>(Arrays.asList(1, 3, 5));
        Set<Integer> otherSetFilters = new HashSet<>();
        FilteringSet<Integer> otherSet = FilteringSet.constructor(otherSetElements, otherSetFilters)
                .construct()
                .or((FilteringSet<Integer>) null);
        assertEquals(otherSet.hashCode(), filteringSet.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "{1, 3, 5}";
        assertEquals(expectedString, filteringSet.toString());
    }

    @Test
    @SuppressWarnings("SimplifyStreamApiCallChains")
    void testStream() {
        Set<Integer> expectedSet = new HashSet<>(Arrays.asList(1, 3, 5));
        Set<Integer> actualSet = filteringSet.stream().collect(Collectors.toSet());
        assertEquals(expectedSet, actualSet);
    }

    @Test
    void testParallelStream() {
        Set<Integer> expectedSet = new HashSet<>(Arrays.asList(1, 3, 5));
        Set<Integer> actualSet = filteringSet.parallelStream().collect(Collectors.toSet());
        assertEquals(expectedSet, actualSet);
    }
}
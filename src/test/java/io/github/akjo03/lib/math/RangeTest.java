package io.github.akjo03.lib.math;

import io.github.akjo03.lib.functional.Functions;
import io.github.akjo03.lib.functional.Result;
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
class RangeTest {
    @Test
    void testCalculateMinAndMax() {
        Range<Integer> range = Range.of(5, 3);
        assertEquals(3, range.min());
        assertEquals(5, range.max());
    }

    @Test
    void testContains() {
        Range<Integer> range = Range.of(3, 5);
        assertTrue(range.contains(4));
        assertFalse(range.contains(2));
        assertFalse(range.contains(6));
    }

    @Test
    void testContainsRange() {
        Range<Integer> range = Range.of(3, 5);
        assertTrue(range.contains(Range.of(4, 5)));
        assertFalse(range.contains(Range.of(2, 3)));
        assertFalse(range.contains(Range.of(5, 6)));
    }

    @Test
    void testCheckRange() {
        Range<Integer> range = Range.of(3, 5);
        Functions.Function0<Result<String>> below = () -> Result.success("Below range");
        Functions.Function0<Result<String>> above = () -> Result.success("Above range");
        Result<String> defaultValue = Result.success("In range");

        range.checkRange(2, below, above, defaultValue)
                .onSuccess(s -> assertEquals("Below range", s))
                .onFailure(s -> fail("Should not fail"));
        range.checkRange(6, below, above, defaultValue)
                .onSuccess(s -> assertEquals("Above range", s))
                .onFailure(s -> fail("Should not fail"));
        range.checkRange(4, below, above, defaultValue)
                .onSuccess(s -> assertEquals("In range", s))
                .onFailure(s -> fail("Should not fail"));
    }

    @Test
    void testOverlaps() {
        Range<Integer> range = Range.of(3, 5);
        assertTrue(range.overlaps(Range.of(4, 6)));
        assertTrue(range.overlaps(Range.of(2, 4)));
        assertTrue(range.overlaps(Range.of(4, 5)));
        assertTrue(range.overlaps(Range.of(3, 4)));
        assertTrue(range.overlaps(Range.of(5, 6)));
        assertFalse(range.overlaps(Range.of(1, 2)));
        assertFalse(range.overlaps(Range.of(6, 7)));
    }

    @Test
    void testIsAdjacent() {
        Range<Integer> range = Range.of(3, 5);
        assertTrue(range.isAdjacent(Range.of(5, 6)));
        assertTrue(range.isAdjacent(Range.of(1, 3)));
        assertFalse(range.isAdjacent(Range.of(1, 2)));
        assertFalse(range.isAdjacent(Range.of(6, 7)));
    }

    @Test
    void testIsDisjoint() {
        Range<Integer> range = Range.of(3, 5);
        assertTrue(range.isDisjoint(Range.of(1, 2)));
        assertTrue(range.isDisjoint(Range.of(6, 7)));
        assertFalse(range.isDisjoint(Range.of(1, 3)));
        assertFalse(range.isDisjoint(Range.of(5, 7)));
    }
}
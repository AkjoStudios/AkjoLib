package io.github.akjo03.lib.math;

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
class Vector2Test {
    @Test
    void testPredefinedVectors() {
        assertEquals(Vector2.ZERO, new Vector2(0, 0));
        assertEquals(Vector2.ONE, new Vector2(1, 1));
        assertEquals(Vector2.UP, new Vector2(0, 1));
        assertEquals(Vector2.DOWN, new Vector2(0, -1));
        assertEquals(Vector2.LEFT, new Vector2(-1, 0));
        assertEquals(Vector2.RIGHT, new Vector2(1, 0));
        assertEquals(Vector2.POSITIVE_INFINITY, new Vector2(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(Vector2.NEGATIVE_INFINITY, new Vector2(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
    }

    @Test
    void testEqualsAndHashCode() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(3, 4);
        Vector2 v3 = new Vector2(4, 3);

        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
        assertNotEquals(v1, v3);
        assertNotEquals(v1.hashCode(), v3.hashCode());
    }

    @Test
    void testToString() {
        Vector2 v = new Vector2(3.14, 4.15);
        assertEquals("Vector2(3.14, 4.15)", v.toString());
    }

    @Test
    void testAdd() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(4, 3);
        Vector2 v3 = new Vector2(7, 7);

        assertEquals(v3, v1.add(v2));
    }

    @Test
    void testSubtract() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(4, 3);
        Vector2 v3 = new Vector2(-1, 1);

        assertEquals(v3, v1.subtract(v2));
    }

    @Test
    void testMultiply() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(6, 8);
        double scalar = 2;

        assertEquals(v2, v1.multiply(scalar));
    }

    @Test
    void testDivide() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(6, 8);
        double scalar = 2;

        assertEquals(v1, v2.divide(scalar));
        assertThrows(ArithmeticException.class, () -> v1.divide(0));
    }

    @Test
    void testMultiplyVector() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(4, 3);
        Vector2 v3 = new Vector2(12, 12);

        assertEquals(v3, v1.multiply(v2));
    }

    @Test
    void testDivideVector() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(4, 3);
        Vector2 v3 = new Vector2(0.75, 1.3333333333333333);

        assertEquals(v3, v1.divide(v2));
        assertThrows(ArithmeticException.class, () -> v1.divide(Vector2.ZERO));
    }

    @Test
    void testIsApproximately() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(3.0000000001, 4.0000000001);
        Vector2 v3 = new Vector2(3.01, 4.01);
        Vector2 v4 = new Vector2(3.1, 4.1);
        assertTrue(v1.isApproximately(v2));
        assertFalse(v1.isApproximately(v3));
        assertTrue(v1.isApproximately(v3, 0.1));
        assertFalse(v1.isApproximately(v4, 0.1));
    }

    @Test
    void testMagnitude() {
        Vector2 v1 = new Vector2(3, 4);
        assertEquals(5, v1.magnitude());
    }

    @Test
    void testMagnitudeSquared() {
        Vector2 v1 = new Vector2(3, 4);
        assertEquals(25, v1.magnitudeSquared());
    }

    @Test
    void testNormalize() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(0.6, 0.8);
        assertEquals(v2, v1.normalize());
        assertEquals(Vector2.ZERO, Vector2.ZERO.normalize());
    }

    @Test
    void testToVector3() {
        Vector2 v1 = new Vector2(3, 4);
        Vector3 v2 = new Vector3(3, 4, 0);
        assertEquals(v2, v1.toVector3());
    }

    @Test
    void testAngle() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(1, 0);
        Vector2 v3 = new Vector2(0, 1);
        assertEquals(0, Vector2.angle(v1, v1));
        assertEquals(Math.PI / 2, Vector2.angle(v2, v3));
        assertTrue(Double.isNaN(Vector2.angle(v1, Vector2.ZERO)));
    }

    @Test
    void testClampMagnitude() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = Vector2.clampMagnitude(v1, 4);
        Vector2 v3 = Vector2.clampMagnitude(v1, 5);
        Vector2 v4 = new Vector2(2.4, 3.2);
        assertEquals(v4, v2);
        assertEquals(v1, v3);
    }

    @Test
    void testDistance() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(0, 0);
        assertEquals(5, Vector2.distance(v1, v2));
    }

    @Test
    void testDistanceSquared() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(0, 0);
        assertEquals(25, Vector2.distanceSquared(v1, v2));
    }

    @Test
    void testDot() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(4, 3);
        assertEquals(24, Vector2.dot(v1, v2));
    }

    @Test
    void testLerp() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(4, 3);
        Vector2 v3 = new Vector2(3.5, 3.5);
        assertEquals(v3, Vector2.lerp(v1, v2, 0.5));
        assertEquals(v1, Vector2.lerp(v1, v2, 0));
        assertEquals(v2, Vector2.lerp(v1, v2, 1));
    }

    @Test
    void testLerpUnclamped() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(4, 3);
        Vector2 v3 = new Vector2(3.5, 3.5);
        Vector2 v4 = new Vector2(2, 5);
        Vector2 v5 = new Vector2(5, 2);
        assertEquals(v3, Vector2.lerpUnclamped(v1, v2, 0.5));
        assertEquals(v1, Vector2.lerpUnclamped(v1, v2, 0));
        assertEquals(v2, Vector2.lerpUnclamped(v1, v2, 1));
        assertEquals(v4, Vector2.lerpUnclamped(v1, v2, -1));
        assertEquals(v5, Vector2.lerpUnclamped(v1, v2, 2));
    }

    @Test
    void testMax() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(4, 3);
        Vector2 v3 = new Vector2(4, 4);
        assertEquals(v3, Vector2.max(v1, v2));
    }

    @Test
    void testMin() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(4, 3);
        Vector2 v3 = new Vector2(3, 3);
        assertEquals(v3, Vector2.min(v1, v2));
    }

    @Test
    void testScale() {
        Vector2 v1 = new Vector2(3, 4);
        Vector2 v2 = new Vector2(4, 3);
        Vector2 v3 = new Vector2(12, 12);
        assertEquals(v3, Vector2.scale(v1, v2));
    }
}
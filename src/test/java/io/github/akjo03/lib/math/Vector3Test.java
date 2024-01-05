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
class Vector3Test {
    @Test
    void testPredefinedVectors() {
        assertEquals(Vector3.ZERO, new Vector3(0, 0, 0));
        assertEquals(Vector3.ONE, new Vector3(1, 1, 1));
        assertEquals(Vector3.UP, new Vector3(0, 1, 0));
        assertEquals(Vector3.DOWN, new Vector3(0, -1, 0));
        assertEquals(Vector3.LEFT, new Vector3(-1, 0, 0));
        assertEquals(Vector3.RIGHT, new Vector3(1, 0, 0));
        assertEquals(Vector3.FORWARD, new Vector3(0, 0, 1));
        assertEquals(Vector3.BACKWARD, new Vector3(0, 0, -1));
        assertEquals(Vector3.POSITIVE_INFINITY, new Vector3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(Vector3.NEGATIVE_INFINITY, new Vector3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
    }

    @Test
    void testEqualsAndHashCode() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(3, 4, 5);
        Vector3 v3 = new Vector3(4, 3, 6);

        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
        assertNotEquals(v1, v3);
        assertNotEquals(v1.hashCode(), v3.hashCode());
    }

    @Test
    void testToString() {
        Vector3 v = new Vector3(3.14, 4.15, 5.16);
        assertEquals("Vector3(3.14, 4.15, 5.16)", v.toString());
    }

    @Test
    void testAdd() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(4, 3, 6);
        Vector3 v3 = new Vector3(7, 7, 11);

        assertEquals(v3, v1.add(v2));
    }

    @Test
    void testSubtract() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(4, 3, 7);
        Vector3 v3 = new Vector3(-1, 1, -2);

        assertEquals(v3, v1.subtract(v2));
    }

    @Test
    void testMultiply() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(6, 8, 10);
        double scalar = 2;

        assertEquals(v2, v1.multiply(scalar));
    }

    @Test
    void testDivide() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(6, 8, 10);
        double scalar = 2;

        assertEquals(v1, v2.divide(scalar));
    }

    @Test
    void testMultiplyVector() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(6, 8, 10);
        Vector3 v3 = new Vector3(18, 32, 50);

        assertEquals(v3, v1.multiply(v2));
    }

    @Test
    void testDivideVector() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(6, 8, 10);
        Vector3 v3 = new Vector3(0.5, 0.5, 0.5);

        assertEquals(v3, v1.divide(v2));
    }

    @Test
    void testIsApproximately() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(3.0000000001, 4.0000000001, 5.0000000001);
        Vector3 v3 = new Vector3(3.01, 4.01, 5.01);
        Vector3 v4 = new Vector3(3.1, 4.1, 5.1);
        assertTrue(v1.isApproximately(v2));
        assertFalse(v1.isApproximately(v3));
        assertTrue(v1.isApproximately(v3, 0.1));
        assertFalse(v1.isApproximately(v4, 0.1));
    }

    @Test
    void testMagnitude() {
        Vector3 v1 = new Vector3(3, 4, 5);
        assertEquals(7.0710678118654755, v1.magnitude());
    }

    @Test
    void testMagnitudeSquared() {
        Vector3 v1 = new Vector3(3, 4, 5);
        assertEquals(50, v1.magnitudeSquared());
    }

    @Test
    void testNormalize() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(0.4242640687119285, 0.565685424949238, 0.7071067811865475);
        assertEquals(v2, v1.normalize());
        assertEquals(Vector3.ZERO, Vector3.ZERO.normalize());
    }

    @Test
    void testToVector2() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector2 v2 = new Vector2(3, 4);
        assertEquals(v2, v1.toVector2());
    }


    @Test
    void testAngle() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(1, 0, 0);
        Vector3 v3 = new Vector3(0, 1, 0);
        Vector3 v4 = new Vector3(0, 0, 1);
        assertEquals(0, Vector3.angle(v1, v1));
        assertEquals(Math.PI / 2, Vector3.angle(v2, v3));
        assertEquals(Math.PI / 2, Vector3.angle(v2, v4));
        assertTrue(Double.isNaN(Vector3.angle(v1, Vector3.ZERO)));
    }

    @Test
    void testClampMagnitude() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = Vector3.clampMagnitude(v1, 7);
        Vector3 v3 = Vector3.clampMagnitude(v1, 8);
        Vector3 v4 = new Vector3(2.96984848098, 3.95979797464, 4.94974746831);
        assertEquals(v4, v2);
        assertEquals(v1, v3);
    }

    @Test
    void testCross() {
        Vector3 v1 = new Vector3(1, 0, 0);
        Vector3 v2 = new Vector3(0, 1, 0);
        Vector3 v3 = new Vector3(0, 0, 1);
        assertEquals(v3, Vector3.cross(v1, v2));
        assertEquals(v1, Vector3.cross(v2, v3));
        assertEquals(v2, Vector3.cross(v3, v1));
        assertEquals(Vector3.ZERO, Vector3.cross(v1, v1));
        assertEquals(Vector3.ZERO, Vector3.cross(v2, v2));
        assertEquals(Vector3.ZERO, Vector3.cross(v3, v3));
        Vector3 v4 = new Vector3(2, 3, 4);
        Vector3 v5 = new Vector3(5, 6, 7);
        Vector3 expectedCross = new Vector3(-3, 6, -3);
        assertEquals(expectedCross, Vector3.cross(v4, v5));
    }

    @Test
    void testDistance() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(0, 0, 0);
        assertEquals(Math.sqrt(50), Vector3.distance(v1, v2));
    }

    @Test
    void testDistanceSquared() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(0, 0, 0);
        assertEquals(50, Vector3.distanceSquared(v1, v2));
    }

    @Test
    void testDot() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(4, 3, 2);
        assertEquals(34, Vector3.dot(v1, v2));
    }

    @Test
    void testLerp() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(4, 3, 2);
        Vector3 v3 = new Vector3(3.5, 3.5, 3.5);
        assertEquals(v3, Vector3.lerp(v1, v2, 0.5));
        assertEquals(v1, Vector3.lerp(v1, v2, 0));
        assertEquals(v2, Vector3.lerp(v1, v2, 1));
    }

    @Test
    void testLerpUnclamped() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(4, 3, 2);
        Vector3 v3 = new Vector3(3.5, 3.5, 3.5);
        Vector3 v4 = new Vector3(2, 5, 8);
        Vector3 v5 = new Vector3(5, 2, -1);
        assertEquals(v3, Vector3.lerpUnclamped(v1, v2, 0.5));
        assertEquals(v1, Vector3.lerpUnclamped(v1, v2, 0));
        assertEquals(v2, Vector3.lerpUnclamped(v1, v2, 1));
        assertEquals(v4, Vector3.lerpUnclamped(v1, v2, -1));
        assertEquals(v5, Vector3.lerpUnclamped(v1, v2, 2));
    }

    @Test
    void testMax() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(4, 3, 6);
        Vector3 v3 = new Vector3(4, 4, 6);
        assertEquals(v3, Vector3.max(v1, v2));
    }

    @Test
    void testMin() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(4, 3, 2);
        Vector3 v3 = new Vector3(3, 3, 2);
        assertEquals(v3, Vector3.min(v1, v2));
    }

    @Test
    void testScale() {
        Vector3 v1 = new Vector3(3, 4, 5);
        Vector3 v2 = new Vector3(4, 3, 2);
        Vector3 v3 = new Vector3(12, 12, 10);
        assertEquals(v3, Vector3.scale(v1, v2));
    }
}
package io.github.akjo03.lib.math;

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
@SuppressWarnings({"unused", "java:S125"})
public record Vector3(double x, double y, double z) {
    private static final double EPSILON = 0.0000001;

    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    public static final Vector3 ONE = new Vector3(1, 1, 1);
    public static final Vector3 UP = new Vector3(0, 1, 0);
    public static final Vector3 DOWN = new Vector3(0, -1, 0);
    public static final Vector3 LEFT = new Vector3(-1, 0, 0);
    public static final Vector3 RIGHT = new Vector3(1, 0, 0);
    public static final Vector3 FORWARD = new Vector3(0, 0, 1);
    public static final Vector3 BACKWARD = new Vector3(0, 0, -1);
    public static final Vector3 POSITIVE_INFINITY = new Vector3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final Vector3 NEGATIVE_INFINITY = new Vector3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3 v)) { return false; }

        boolean thisXIsInfinity = this.x == Double.POSITIVE_INFINITY || this.x == Double.NEGATIVE_INFINITY;
        boolean thisYIsInfinity = this.y == Double.POSITIVE_INFINITY || this.y == Double.NEGATIVE_INFINITY;
        boolean thisZIsInfinity = this.z == Double.POSITIVE_INFINITY || this.z == Double.NEGATIVE_INFINITY;
        boolean vXIsInfinity = v.x == Double.POSITIVE_INFINITY || v.x == Double.NEGATIVE_INFINITY;
        boolean vYIsInfinity = v.y == Double.POSITIVE_INFINITY || v.y == Double.NEGATIVE_INFINITY;
        boolean vZIsInfinity = v.z == Double.POSITIVE_INFINITY || v.z == Double.NEGATIVE_INFINITY;

        if (thisXIsInfinity && vXIsInfinity) { return true; }
        if (thisYIsInfinity && vYIsInfinity) { return true; }
        if (thisZIsInfinity && vZIsInfinity) { return true; }

        return Math.abs(x - v.x) < EPSILON && Math.abs(y - v.y) < EPSILON && Math.abs(z - v.z) < EPSILON;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(x) + Double.hashCode(y) + Double.hashCode(z);
    }

    @Override
    public @NotNull String toString() {
        String x = String.format("%.2f", this.x);
        String y = String.format("%.2f", this.y);
        String z = String.format("%.2f", this.z);
        return "Vector3(" + x + ", " + y + ", " + z + ")";
    }

    @Contract("_ -> new")
    public @NotNull Vector3 add(@NotNull Vector3 v) {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    @Contract("_ -> new")
    public @NotNull Vector3 subtract(@NotNull Vector3 v) {
        return new Vector3(x - v.x, y - v.y, z - v.z);
    }

    @Contract("_ -> new")
    public @NotNull Vector3 multiply(double scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    @Contract("_ -> new")
    public @NotNull Vector3 divide(double scalar) throws ArithmeticException {
        if (scalar == 0) { throw new ArithmeticException("Cannot divide by zero"); }
        return new Vector3(x / scalar, y / scalar, z / scalar);
    }

    @Contract("_ -> new")
    public @NotNull Vector3 multiply(@NotNull Vector3 v) {
        return new Vector3(x * v.x, y * v.y, z * v.z);
    }

    @Contract("_ -> new")
    public @NotNull Vector3 divide(@NotNull Vector3 v) throws ArithmeticException {
        if (v.x == 0 || v.y == 0 || v.z == 0) { throw new ArithmeticException("Cannot divide by zero"); }
        return new Vector3(x / v.x, y / v.y, z / v.z);
    }

    public boolean isApproximately(@NotNull Vector3 v, double epsilon) {
        return Math.abs(x - v.x) < epsilon && Math.abs(y - v.y) < epsilon && Math.abs(z - v.z) < epsilon;
    }

    public boolean isApproximately(@NotNull Vector3 v) {
        return isApproximately(v, EPSILON);
    }

    @Contract(pure = true)
    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    @Contract(pure = true)
    public double magnitudeSquared() {
        return x * x + y * y + z * z;
    }

    @Contract(pure = true)
    public @NotNull Vector3 normalize() throws ArithmeticException {
        double magnitude = magnitude();
        if (magnitude == 0) { return ZERO; }
        return new Vector3(x / magnitude, y / magnitude, z / magnitude);
    }

    @Contract(" -> new")
    public @NotNull Vector2 toVector2() {
        return new Vector2(x, y);
    }

    public static double angle(@NotNull Vector3 from, @NotNull Vector3 to) {
        double dotProduct = dot(from, to);
        double magnitudeProduct = Math.sqrt(from.magnitudeSquared() * to.magnitudeSquared());
        return Math.acos(dotProduct / magnitudeProduct);
    }

    public static @NotNull Vector3 clampMagnitude(@NotNull Vector3 vector, double maxLength) {
        double magnitude = vector.magnitude();
        if (magnitude > maxLength) {
            return vector.divide(magnitude).multiply(maxLength);
        }
        return vector;
    }

    @Contract("_, _ -> new")
    public static @NotNull Vector3 cross(@NotNull Vector3 first, @NotNull Vector3 second) {
        return new Vector3(
            first.y * second.z - first.z * second.y,
            first.z * second.x - first.x * second.z,
            first.x * second.y - first.y * second.x
        );
    }

    @Contract(pure = true)
    public static double distance(@NotNull Vector3 first, @NotNull Vector3 second) {
        double dx = second.x - first.x;
        double dy = second.y - first.y;
        double dz = second.z - first.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Contract(pure = true)
    public static double distanceSquared(@NotNull Vector3 first, @NotNull Vector3 second) {
        double dx = second.x - first.x;
        double dy = second.y - first.y;
        double dz = second.z - first.z;
        return dx * dx + dy * dy + dz * dz;
    }

    @Contract(pure = true)
    public static double dot(@NotNull Vector3 first, @NotNull Vector3 second) {
        return first.x * second.x + first.y * second.y + first.z * second.z;
    }

    public static @NotNull Vector3 lerp(@NotNull Vector3 from, @NotNull Vector3 to, double t) {
        if (t < 0) { t = 0; }
        if (t > 1) { t = 1; }
        return lerpUnclamped(from, to, t);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Vector3 lerpUnclamped(@NotNull Vector3 from, @NotNull Vector3 to, double t) {
        return from.add(to.subtract(from).multiply(t));
    }

    @Contract("_, _ -> new")
    public static @NotNull Vector3 max(@NotNull Vector3 first, @NotNull Vector3 second) {
        return new Vector3(
            Math.max(first.x, second.x),
            Math.max(first.y, second.y),
            Math.max(first.z, second.z)
        );
    }

    @Contract("_, _ -> new")
    public static @NotNull Vector3 min(@NotNull Vector3 first, @NotNull Vector3 second) {
        return new Vector3(
            Math.min(first.x, second.x),
            Math.min(first.y, second.y),
            Math.min(first.z, second.z)
        );
    }

    @Contract("_, _ -> new")
    public static @NotNull Vector3 scale(@NotNull Vector3 first, @NotNull Vector3 second) {
        return new Vector3(
            first.x * second.x,
            first.y * second.y,
            first.z * second.z
        );
    }
}
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
public record Vector2(double x, double y) {
    private static final double EPSILON = 0.0000001;

    public static final Vector2 ZERO = new Vector2(0, 0);
    public static final Vector2 ONE = new Vector2(1, 1);
    public static final Vector2 UP = new Vector2(0, 1);
    public static final Vector2 DOWN = new Vector2(0, -1);
    public static final Vector2 LEFT = new Vector2(-1, 0);
    public static final Vector2 RIGHT = new Vector2(1, 0);
    public static final Vector2 POSITIVE_INFINITY = new Vector2(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final Vector2 NEGATIVE_INFINITY = new Vector2(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2 v)) { return false; }

        boolean thisXIsInfinity = this.x == Double.POSITIVE_INFINITY || this.x == Double.NEGATIVE_INFINITY;
        boolean thisYIsInfinity = this.y == Double.POSITIVE_INFINITY || this.y == Double.NEGATIVE_INFINITY;
        boolean vXIsInfinity = v.x == Double.POSITIVE_INFINITY || v.x == Double.NEGATIVE_INFINITY;
        boolean vYIsInfinity = v.y == Double.POSITIVE_INFINITY || v.y == Double.NEGATIVE_INFINITY;

        if (thisXIsInfinity && vXIsInfinity) { return true; }
        if (thisYIsInfinity && vYIsInfinity) { return true; }

        return Math.abs(x - v.x) < EPSILON && Math.abs(y - v.y) < EPSILON;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(x) + Double.hashCode(y);
    }

    @Override
    public @NotNull String toString() {
        String x = String.format("%.2f", this.x);
        String y = String.format("%.2f", this.y);
        return "Vector2(" + x + ", " + y + ")";
    }

    @Contract("_ -> new")
    public @NotNull Vector2 add(@NotNull Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }

    @Contract("_ -> new")
    public @NotNull Vector2 subtract(@NotNull Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }

    @Contract("_ -> new")
    public @NotNull Vector2 multiply(double scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    @Contract("_ -> new")
    public @NotNull Vector2 divide(double scalar) throws ArithmeticException {
        if (scalar == 0) { throw new ArithmeticException("Cannot divide by zero"); }
        return new Vector2(x / scalar, y / scalar);
    }

    @Contract("_ -> new")
    public @NotNull Vector2 multiply(@NotNull Vector2 v) {
        return new Vector2(x * v.x, y * v.y);
    }

    @Contract("_ -> new")
    public @NotNull Vector2 divide(@NotNull Vector2 v) throws ArithmeticException {
        if (v.x == 0 || v.y == 0) { throw new ArithmeticException("Cannot divide by zero"); }
        return new Vector2(x / v.x, y / v.y);
    }

    public boolean isApproximately(@NotNull Vector2 v, double epsilon) {
        return Math.abs(x - v.x) < epsilon && Math.abs(y - v.y) < epsilon;
    }

    public boolean isApproximately(@NotNull Vector2 v) {
        return isApproximately(v, EPSILON);
    }

    @Contract(pure = true)
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    @Contract(pure = true)
    public double magnitudeSquared() {
        return x * x + y * y;
    }

    @Contract(pure = true)
    public @NotNull Vector2 normalize() throws ArithmeticException {
        double magnitude = magnitude();
        if (magnitude == 0) { return ZERO; }
        return new Vector2(x / magnitude, y / magnitude);
    }

    @Contract(" -> new")
    public @NotNull Vector3 toVector3() {
        return new Vector3(x, y, 0);
    }

    public static double angle(@NotNull Vector2 from, @NotNull Vector2 to) {
        double dotProduct = dot(from, to);
        double magnitudeProduct = Math.sqrt(from.magnitudeSquared() * to.magnitudeSquared());
        return Math.acos(dotProduct / magnitudeProduct);
    }

    public static @NotNull Vector2 clampMagnitude(@NotNull Vector2 vector, double maxLength) {
        double magnitude = vector.magnitude();
        if (magnitude > maxLength) {
            return vector.normalize().multiply(maxLength);
        }
        return vector;
    }

    public static double distance(@NotNull Vector2 first, @NotNull Vector2 second) {
        return (first.subtract(second)).magnitude();
    }

    public static double distanceSquared(@NotNull Vector2 first, @NotNull Vector2 second) {
        return (first.subtract(second)).magnitudeSquared();
    }

    @Contract(pure = true)
    public static double dot(@NotNull Vector2 first, @NotNull Vector2 second) {
        return first.x * second.x + first.y * second.y;
    }

    public static @NotNull Vector2 lerp(@NotNull Vector2 from, @NotNull Vector2 to, double t) {
        if (t < 0) { t = 0; }
        if (t > 1) { t = 1; }
        return lerpUnclamped(from, to, t);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Vector2 lerpUnclamped(@NotNull Vector2 from, @NotNull Vector2 to, double t) {
        return from.add(to.subtract(from).multiply(t));
    }

    @Contract("_, _ -> new")
    public static @NotNull Vector2 max(@NotNull Vector2 first, @NotNull Vector2 second) {
        return new Vector2(
                Math.max(first.x, second.x),
                Math.max(first.y, second.y)
        );
    }

    @Contract("_, _ -> new")
    public static @NotNull Vector2 min(@NotNull Vector2 first, @NotNull Vector2 second) {
        return new Vector2(
                Math.min(first.x, second.x),
                Math.min(first.y, second.y)
        );
    }

    @Contract("_, _ -> new")
    public static @NotNull Vector2 scale(@NotNull Vector2 first, @NotNull Vector2 second) {
        return new Vector2(
                first.x * second.x,
                first.y * second.y
        );
    }
}
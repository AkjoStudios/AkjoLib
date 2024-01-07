package io.github.akjo03.lib.io;

import io.github.akjo03.lib.functional.Tuple;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static io.github.akjo03.lib.functional.Tuple.tuple;

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
 *  This file was ported and modified from the project Pragmatica.
 *  See https://github.com/siy/pragmatica/blob/b05a6985c1b4277f15ca6d7319334d721a33f8b8/core/src/main/java/org/pragmatica/lang/io/Timeout.java
 */
@SuppressWarnings({"unused", "java:S125"})
public sealed interface Timeout extends Comparable<Timeout> {
    // ----- Nanos, Micros, Millis, and Seconds -----

    long nanoseconds();

    default long microseconds() {
        return TimeUnit.NANOSECONDS.toMicros(nanoseconds());
    }

    default long milliseconds() {
        return TimeUnit.NANOSECONDS.toMillis(nanoseconds());
    }

    default long seconds() {
        return TimeUnit.NANOSECONDS.toSeconds(nanoseconds());
    }

    // ----- Seconds and Nanos -----

    long NANOS_IN_SECOND = TimeUnit.SECONDS.toNanos(1);

    default Tuple.Tuple2<Long, Integer> secondsAndNanos() {
        return tuple(nanoseconds() / NANOS_IN_SECOND, (int) (nanoseconds() % NANOS_IN_SECOND));
    }

    // ----- Duration and Comparing -----

    default Duration duration() {
        return secondsAndNanos().map(Duration::ofSeconds).orThrow();
    }

    @Override
    default int compareTo(@NotNull Timeout other) {
        return Long.compare(nanoseconds(), other.nanoseconds());
    }

    static Timeout fromDuration(@NotNull Duration duration) {
        return Timeout.timeout(duration.toMillis()).millis();
    }

    // ----- Timeout Implementation -----

    record TimeoutImpl(long nanoseconds) implements Timeout {
        @Contract(pure = true)
        @Override
        public @NotNull String toString() { return "Timeout(" + nanoseconds + "ns)"; }
    }

    // ----- Timeout Factory -----

    @Contract(pure = true)
    static @NotNull TimeoutFactory timeout(long nanoseconds) {
        return () -> nanoseconds;
    }

    interface TimeoutFactory {
        long value();

        default Timeout nanos() {
            return new TimeoutImpl(value());
        }

        default Timeout micros() {
            return new TimeoutImpl(TimeUnit.MICROSECONDS.toNanos(value()));
        }

        default Timeout millis() {
            return new TimeoutImpl(TimeUnit.MILLISECONDS.toNanos(value()));
        }

        default Timeout seconds() {
            return new TimeoutImpl(TimeUnit.SECONDS.toNanos(value()));
        }

        default Timeout minutes() {
            return new TimeoutImpl(TimeUnit.MINUTES.toNanos(value()));
        }

        default Timeout hours() {
            return new TimeoutImpl(TimeUnit.HOURS.toNanos(value()));
        }

        default Timeout days() {
            return new TimeoutImpl(TimeUnit.DAYS.toNanos(value()));
        }
    }
}
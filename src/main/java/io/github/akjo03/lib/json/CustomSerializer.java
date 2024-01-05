package io.github.akjo03.lib.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.akjo03.lib.functional.Functions;
import io.github.akjo03.lib.functional.Cause;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

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
public abstract class CustomSerializer<T> extends StdSerializer<T> implements IJsonSerializer<T> {
    protected CustomSerializer(Class<T> type) {
        super(type);
    }

    @Override
    public void serialize(T value, @NotNull JsonGenerator generator, SerializerProvider provider) throws IOException {
        validate(value).orThrow(
                (Functions.Function1<IOException, Cause>) cause -> new IOException(cause.message())
        );

        generator.writeObject(toJson(value).orThrow(
                (Functions.Function1<IOException, Cause>) cause -> new IOException(cause.message())
        ));
    }
}
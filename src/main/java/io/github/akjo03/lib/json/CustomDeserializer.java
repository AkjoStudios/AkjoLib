package io.github.akjo03.lib.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.github.akjo03.lib.functional.Cause;
import io.github.akjo03.lib.functional.Functions.*;
import io.github.akjo03.lib.functional.Result;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public abstract class CustomDeserializer<T> extends StdDeserializer<T> implements IJsonDeserializer<T> {
    protected CustomDeserializer(Class<T> type) {
        super(type);
    }

    @Override
    public T deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        Result<T> result = fromJson(parser, context);

        T value = result.orThrow((Function1<MismatchedInputException, Cause>) cause ->
                MismatchedInputException.from(
                        parser,
                        this.getValueType(),
                        cause.message()
                )
        );

        return Result.aggregate(List.of(
                result, validate(value)
        ), __ -> value).or((T) null);
    }

    protected static <T> @NotNull List<T> deserializeList(
            @NotNull JsonNode node,
            @NotNull JsonParser parser,
            @NotNull Class<T> type
    ) throws JsonProcessingException {
        List<T> list = new ArrayList<>();
        for (JsonNode element : node) {
            list.add(parser.getCodec().treeToValue(element, type));
        }
        return list;
    }
}
package io.github.akjo03.lib.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import io.github.akjo03.lib.functional.Result;

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
public interface IJsonDeserializer<T> {
    Result<T> fromJson(JsonParser parser, DeserializationContext context);
    Result<T> validate(T t);
}
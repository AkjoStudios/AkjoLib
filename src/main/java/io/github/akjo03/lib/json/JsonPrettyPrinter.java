package io.github.akjo03.lib.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
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
public class JsonPrettyPrinter extends DefaultPrettyPrinter {
    public JsonPrettyPrinter() {
        _arrayIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
        _objectIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
    }

    public JsonPrettyPrinter(DefaultPrettyPrinter base) {
        super(base);
    }

    @Override
    public DefaultPrettyPrinter createInstance() {
        return new JsonPrettyPrinter(this);
    }

    @Override
    public void writeObjectFieldValueSeparator(@NotNull JsonGenerator generator) throws IOException {
        generator.writeRaw(": ");
    }

    @Override
    public DefaultPrettyPrinter withSeparators(@NotNull Separators separators) {
        _separators = separators;
        _objectFieldValueSeparatorWithSpaces = separators.getObjectFieldValueSeparator() + " ";

        return this;
    }

    @Override
    public void writeEndArray(JsonGenerator generator, int valueCount) throws IOException {
        if (!_arrayIndenter.isInline()) { --_nesting; }
        if (valueCount > 0) { _arrayIndenter.writeIndentation(generator, _nesting); }

        generator.writeRaw(']');
    }

    @Override
    public void writeEndObject(JsonGenerator generator, int entryCount) throws IOException {
        if (!_objectIndenter.isInline()) { --_nesting; }
        if (entryCount > 0) { _objectIndenter.writeIndentation(generator, _nesting); }

        generator.writeRaw('}');
    }
}
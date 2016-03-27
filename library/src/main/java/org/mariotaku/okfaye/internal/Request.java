package org.mariotaku.okfaye.internal;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import org.mariotaku.okfaye.Extension;

import java.io.IOException;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public abstract class Request {
    @JsonField(name = "channel")
    String channel;
    @JsonField(name = "id")
    String id;
    @JsonField(name = "ext", typeConverter = ExtensionSerializer.class)
    Extension extension;

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "channel='" + channel + '\'' +
                ", id='" + id + '\'' +
                ", extension=" + extension +
                '}';
    }

    static class ExtensionSerializer implements TypeConverter<Extension> {
        @Override
        public Extension parse(JsonParser jsonParser) throws IOException {
            // We don't support parse
            return null;
        }

        @Override
        public void serialize(Extension object, String fieldName, boolean writeFieldNameForObject,
                              JsonGenerator jsonGenerator) throws IOException {
            if (object == null) return;
            if (writeFieldNameForObject) {
                jsonGenerator.writeFieldName(fieldName);
            }
            final JsonMapper<? extends Extension> mapper = LoganSquare.mapperFor(object.getClass());
            //noinspection unchecked
            ((JsonMapper<Extension>) mapper).serialize(object, jsonGenerator, true);
        }
    }
}

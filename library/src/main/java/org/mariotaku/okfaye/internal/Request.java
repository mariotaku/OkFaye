package org.mariotaku.okfaye.internal;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import org.mariotaku.okfaye.Faye;

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
    Faye.Extension extension;

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setExtension(Faye.Extension extension) {
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

    static class ExtensionSerializer implements TypeConverter<Faye.Extension> {
        @Override
        public Faye.Extension parse(JsonParser jsonParser) throws IOException {
            // We don't support parse
            return null;
        }

        @Override
        public void serialize(Faye.Extension object, String fieldName, boolean writeFieldNameForObject,
                              JsonGenerator jsonGenerator) throws IOException {
            if (object == null) return;
            if (writeFieldNameForObject) {
                jsonGenerator.writeFieldName(fieldName);
            }
            final JsonMapper<? extends Faye.Extension> mapper = LoganSquare.mapperFor(object.getClass());
            //noinspection unchecked
            ((JsonMapper<Faye.Extension>) mapper).serialize(object, jsonGenerator, true);
        }
    }
}

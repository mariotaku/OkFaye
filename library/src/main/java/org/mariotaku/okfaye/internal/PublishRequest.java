package org.mariotaku.okfaye.internal;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

/**
 * Created by mariotaku on 16/3/27.
 */
@JsonObject
public class PublishRequest extends IdentifiedRequest {
    @JsonField(name = "data", typeConverter = ObjectSerializeConverter.class)
    Object data;

    public PublishRequest() {

    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PublishRequest{" +
                "data='" + data + '\'' +
                "} " + super.toString();
    }

    public static PublishRequest create(String channel, Object data) {
        PublishRequest request = new PublishRequest();
        request.setChannel(channel);
        request.setData(data);
        return request;
    }

    static class ObjectSerializeConverter implements TypeConverter<Object> {
        @Override
        public Object parse(JsonParser jsonParser) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void serialize(Object object, String fieldName, boolean writeFieldNameForObject,
                              JsonGenerator jsonGenerator) throws IOException {
            if (writeFieldNameForObject) {
                jsonGenerator.writeFieldName(fieldName);
            }
            //noinspection unchecked
            final JsonMapper<Object> mapper = (JsonMapper<Object>) LoganSquare.mapperFor(object.getClass());
            mapper.serialize(object, jsonGenerator, true);
        }
    }
}

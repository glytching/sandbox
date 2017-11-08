package org.glytching.sandbox.okhttp;

import com.google.common.collect.Maps;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okio.Buffer;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OkHttpTest {

    @Test
    public void canEncodePercent() {
        Map<String, String> input = Maps.newHashMap();
        input.put("username", "me");
        input.put("password", "letme%in");
        RequestBody requestBody = createEncodedFormBody(input);

        assertThat(bodyToString(requestBody), is("password=letme%in&username=me"));
    }

    private RequestBody createEncodedFormBody(final Map<String, String> content) {
        final FormBody.Builder builder = new FormBody.Builder();
        for (final Map.Entry<String, String> contentEntry : content.entrySet()) {
            builder.addEncoded(contentEntry.getKey(), contentEntry.getValue());
        }
        return builder.build();
    }

    private String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            request.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.inloop.volleytest.api;

import com.android.volley.*;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import eu.inloop.volleytest.App;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * A canned request for retrieving the response body at a given URL as a String.
 */
public class CustomCacheStringRequest extends Request<String> {
    private final Listener<String> mListener;

    public CustomCacheStringRequest(String url, Listener<String> listener, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mListener = listener;
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return evaluateResponse(parsed, response.data, response.headers);
    }

    public static <T> Response<T> evaluateResponse(T parsedData, byte[] rawData, Map<String, String> headers) {
        return Response.success(parsedData, CustomHttpHeaderParser.parseNetworkIgnoreCacheHeaders(headers, parsedData, rawData, 0, 1000*60*5));
    }

    @Override
    public CustomCache<String> getCustomCache() {
        return new CustomCache<String>() {
            @Override
            public Cache<String> getCache() {
                return  App.sMockCache;
            }

            @Override
            public Response<String> parseCustomCacheResponse(Object parsedData, Map<String, String> headers) {
                return evaluateResponse((String)parsedData, null, headers);
            }
        };
    }
}

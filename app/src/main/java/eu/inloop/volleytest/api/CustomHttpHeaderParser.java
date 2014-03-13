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

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;

import java.util.Map;

/**
 * Utility methods for parsing HTTP headers.
 */
public class CustomHttpHeaderParser {

    /**
     * Extracts a {@link Cache.Entry} from a {@link NetworkResponse}.
     * Cache-control headers are ignored. SoftTtl == 3 mins, ttl == 24 hours.
     *
     * @param response                The network response to parse headers from
     * @param cacheHitButRefreshedAge An cache entry with older than this will be delivered
     *                                and a request will be made to refresh the cache entry. The response will be
     *                                delivered twice (once from cache, the second time from network.
     * @param cacheExpiredAge         Maximum cache entry age.
     * @return a cache entry for the given response, or null if the response is not cacheable.
     */
    public static <T> Cache.Entry<T> parseNetworkIgnoreCacheHeaders(Map<String, String> headers, T response, byte[] rawData, long cacheHitButRefreshedAge, long cacheExpiredAge) {
        long now = System.currentTimeMillis();

        long serverDate = 0;
        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = com.android.volley.toolbox.HttpHeaderParser.parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

        final long cacheHitButRefreshed = cacheHitButRefreshedAge;
        final long cacheExpired = cacheExpiredAge;
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;

        Cache.Entry entry = new Cache.Entry();
        entry.data = rawData;
        entry.parsedData = response;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;
    }
}

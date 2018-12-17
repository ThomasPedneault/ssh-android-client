/*
 * Copyright (c) 2018 Ian Clement.  All rights reserved.
 */

package com.example.thomaspedneault.ssh_client.networking;

import java.util.List;
import java.util.Map;

/**
 * A data class for HTTP responses. Used by HttpRequestBuilder.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @version 1
 */
public class HttpResponse {

    private int responseCode;
    private Map<String, List<String>> headerFields;

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setHeaderFields(Map<String, List<String>> headerFields) {
        this.headerFields = headerFields;
    }
}

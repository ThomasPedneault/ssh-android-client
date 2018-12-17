/*
 * Copyright (c) 2018 Ian Clement.  All rights reserved.
 */

package com.example.thomaspedneault.ssh_client.networking;

import com.example.thomaspedneault.ssh_client.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * A builder for an HTTP request.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @version 1
 */
public class HttpRequestBuilder {

    // Use the default for web forms.
    public static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

    /**
     * Supported HTTP methods
     */
    public enum Method {
        GET, POST, PUT, DELETE
    }

    private String url;
    private Method method;
    private String contentType;
    private String requestBody;
    private boolean cookies;
    private int expectedStatusCode;

    /**
     * Create a request builder with specific path on the server.
     * - Default request method is GET.
     * @param path the path on the server
     */
    public HttpRequestBuilder(String path) {
        this.url = ServerConfig.getInstance().getUrlPrefix() + "/" + path;
        method = Method.GET;
        expectedStatusCode = -1;
        cookies = false;
    }

    /**
     * Set the request method
     * @param method
     * @return
     */
    public HttpRequestBuilder method(Method method) {
        this.method = method;
        return this;
    }

    /**
     * Set the request body and it's content type
     * @param contentType
     * @param requestBody
     * @return
     */
    public HttpRequestBuilder withRequestBody(String contentType, String requestBody) {
        this.contentType = contentType;
        this.requestBody = requestBody;
        return this;
    }

    /**
     * Set the request body with the default content-type (www-form)
     * @param requestBody
     * @return
     */
    public HttpRequestBuilder withRequestBody(String requestBody) {
        this.contentType = DEFAULT_CONTENT_TYPE;
        this.requestBody = requestBody;
        return this;
    }

    /**
     * Set the expected status code. If the expectedStatusCode is not the expected value a ServerException is thrown when the request is performed.
     * @param code
     * @return
     */
    public HttpRequestBuilder expectingStatus(int code) {
        this.expectedStatusCode = code;
        return this;
    }

    /**
     * Use the ServerConfig instance's cookies.
     * @return
     */
    public HttpRequestBuilder cookies() {
        cookies = true;
        return this;
    }

    /**
     * Perform the request
     * @return
     * @throws IOException
     * @throws ServerException
     */
    public HttpResponse perform() throws IOException, ServerException {

        // create the http connection
        HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();
        httpConnection.setRequestMethod(method.toString());

        // send content type if required
        if(contentType != null)
            httpConnection.setRequestProperty("Content-Type", contentType);

        // send the cookies if requested
        if(cookies) {
            CookieManager cookieManager = ServerConfig.getInstance().getCookieManager();
            httpConnection.setRequestProperty("Cookie", StringUtils.join(cookieManager.getCookieStore().getCookies(), ";"));
        }

        // send the request body
        if(requestBody != null) {
            httpConnection.setDoOutput(true);
            PrintWriter writer = new PrintWriter(httpConnection.getOutputStream());
            writer.write(requestBody);
            writer.close();
        }

        // check the status code if there was an expected code
        if(expectedStatusCode >= 0) {
            if(httpConnection.getResponseCode() != expectedStatusCode)
                throw new ServerException();
        }

        // save and return all response info
        HttpResponse response = new HttpResponse();
        response.setResponseCode(httpConnection.getResponseCode());
        response.setHeaderFields(httpConnection.getHeaderFields());

        // if we are using cookies, update the cookies in the manager
        if(cookies) {
            String cookiesHeader = httpConnection.getHeaderField("Set-Cookie");
            List<HttpCookie> cookies = HttpCookie.parse(cookiesHeader);
            for(HttpCookie cookie : cookies)
                ServerConfig.getInstance().getCookieManager().getCookieStore().add(null, cookie);
        }

        return response;
    }

}

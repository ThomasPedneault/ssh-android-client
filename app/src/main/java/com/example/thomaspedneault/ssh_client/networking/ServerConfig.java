/*
 * Copyright (c) 2018 Ian Clement.  All rights reserved.
 */

package com.example.thomaspedneault.ssh_client.networking;

import java.io.IOException;
import java.net.CookieManager;

/**
 * Server configuration singleton class.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @version 1
 */
public class ServerConfig {

    // Server address and port config
    private static final String HOST = "10.0.2.2";
    private static final int PORT = 9999;
    private static final String PROTOCOL = "http";

    // Construct the URL prefix for all requests.
    private static final String PREFIX = String.format("%s://%s:%s", PROTOCOL, HOST, PORT);

    // Username and password for client login.
    private static final String CLIENT_USERNAME = "user";
    private static final String CLIENT_PASSWORD = "user";

    // store the singleton instance
    private static ServerConfig instance;

    /**
     * Get singleton instance.
     * @return
     */
    public static ServerConfig getInstance() {
        // lazy instantiation.
        if(instance == null)
            instance = new ServerConfig();
        return instance;
    }

    // stores all cookies for this server
    private CookieManager cookieManager;

    // flag to indicate whether the client app is logged in.
    private boolean loggedIn;

    // Create a ServerConfig instance.
    // private to implemented the singleton pattern
    private ServerConfig() {
        cookieManager = new CookieManager();
    }

    /**
     * Get the URL prefix.
     * @return
     */
    public String getUrlPrefix() {
        return PREFIX;
    }

    /**
     * Get the cookie manager.
     * @return
     */
    public CookieManager getCookieManager() {
        return cookieManager;
    }

    /**
     * Is the client logged in.
     * @return
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Perform client login.
     * @throws IOException
     * @throws ServerException
     */
    public void clientLogin() throws IOException, ServerException {
        HttpResponse response = new HttpRequestBuilder("login")
                .method(HttpRequestBuilder.Method.POST)
                .withRequestBody(String.format("username=%s&password=%s", CLIENT_USERNAME, CLIENT_PASSWORD))
                .cookies()
                .expectingStatus(200)
                .perform();
        loggedIn = true;
    }

}

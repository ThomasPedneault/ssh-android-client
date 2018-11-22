package com.example.thomaspedneault.ssh_client.model;

public interface IOnAsyncTaskComplete {

    void onBegin();

    void onComplete(ServerConnection.States state);
}

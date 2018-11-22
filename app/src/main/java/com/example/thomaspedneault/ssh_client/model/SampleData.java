package com.example.thomaspedneault.ssh_client.model;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    public static List<ServerConnection> getServerConnections() {
        List<ServerConnection> connections = new ArrayList<>();

        Identity identity = new Identity("android", "android");
        Identity badIdentity = new Identity("baduser", "badpass");
        ServerInfo serverInfo = new ServerInfo("10.39.167.25", "Thomas' UNIX VM");

        connections.add(new ServerConnection(new ServerInfo("111", "doesnt exist"), identity));
        connections.add(new ServerConnection(serverInfo, badIdentity));
        connections.add(new ServerConnection(serverInfo, identity));
        connections.add(new ServerConnection(serverInfo, identity));
        connections.add(new ServerConnection(serverInfo, badIdentity));
        connections.add(new ServerConnection(serverInfo, badIdentity));
        connections.add(new ServerConnection(serverInfo, identity));
        connections.add(new ServerConnection(serverInfo, badIdentity));
        connections.add(new ServerConnection(serverInfo, identity));

        return connections;
    }
}

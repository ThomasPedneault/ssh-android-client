package com.example.thomaspedneault.ssh_client.model;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    public static List<ServerConnection> getServerConnections() {
        List<ServerConnection> connections = new ArrayList<>();

        connections.add(new ServerConnection(
                new ServerInfo("linux2-cs.johnabbott.qc.ca", "Linux2"),
                new Identity("1621638", "Stella1414"))
        );

        return connections;
    }
}

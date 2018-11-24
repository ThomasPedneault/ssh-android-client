package com.example.thomaspedneault.ssh_client.model;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    public static List<ServerConnection> getServerConnections() {
        List<ServerConnection> connections = new ArrayList<>();

        Identity identity = new Identity("1621638", "stella1999");
        ServerInfo serverInfo = new ServerInfo("linux2-cs.johnabbott.qc.ca", "linux2");

        connections.add(new ServerConnection(serverInfo, identity));

        return connections;
    }
}

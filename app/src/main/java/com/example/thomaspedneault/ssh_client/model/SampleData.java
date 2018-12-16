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

    public static List<String> getSavedCommands() {
        List<String> commands = new ArrayList<>();

        commands.add("ls -al");
        commands.add("echo hello world");
        commands.add("echo 123");
        commands.add("ls -al");
        commands.add("echo hello world");
        commands.add("echo 123");
        commands.add("ls -al");
        commands.add("echo hello world");
        commands.add("echo 123");
        commands.add("ls -al");
        commands.add("echo hello world");
        commands.add("echo 123");
        commands.add("ls -al");
        commands.add("echo hello world");
        commands.add("echo 123");
        commands.add("ls -al");
        commands.add("echo hello world");
        commands.add("echo 123");

        return commands;
    }
}

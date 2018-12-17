package com.example.thomaspedneault.ssh_client.model;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    public static List<String> getSavedCommands() {
        List<String> commands = new ArrayList<>();

        commands.add("clear");
        commands.add("ls");
        commands.add("echo foo");
        commands.add("echo bar");
        commands.add("echo garply");

        return commands;
    }

}

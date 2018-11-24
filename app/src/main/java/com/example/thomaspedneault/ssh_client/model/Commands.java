package com.example.thomaspedneault.ssh_client.model;

public class Commands {

    public static class Bash {
        public static String loadAverage = "cat /proc/loadavg | awk -F' ' '{ printf \"%s,%s,%s\", $1, $2, $3 }'";

    }

}

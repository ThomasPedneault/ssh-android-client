package com.example.thomaspedneault.ssh_client.model;

import com.example.thomaspedneault.ssh_client.sqlite.Identifiable;

import java.util.Date;
import java.util.Objects;

public class CommandLog implements Identifiable<Long> {

    private long id;
    private String serverIp;
    private String username;
    private String command;
    private String output;
    private Date timestamp;

    public CommandLog() {
        this.id = -1;
    }

    public CommandLog(long id, String serverIp, String username, String command, String output, Date timestamp) {
        this.id = id;
        this.serverIp = serverIp;
        this.username = username;
        this.command = command;
        this.output = output;
        this.timestamp = timestamp;
    }

    public CommandLog(String serverIp, String username, String command, String output, Date timestamp) {
        this.serverIp = serverIp;
        this.username = username;
        this.command = command;
        this.output = output;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandLog that = (CommandLog) o;
        return Objects.equals(serverIp, that.serverIp) &&
                Objects.equals(username, that.username) &&
                Objects.equals(command, that.command) &&
                Objects.equals(output, that.output) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverIp, username, command, output, timestamp);
    }

    @Override
    public String toString() {
        return "CommandLog{" +
                "serverIp='" + serverIp + '\'' +
                ", username='" + username + '\'' +
                ", command='" + command + '\'' +
                ", output='" + output + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}

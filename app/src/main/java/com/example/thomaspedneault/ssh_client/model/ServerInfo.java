package com.example.thomaspedneault.ssh_client.model;

import java.util.Objects;

public class ServerInfo {

    private String ip;
    private String name;
    private boolean state;

    public ServerInfo() {
        this.ip = "DEFAULT IP";
        this.name = "DEFAULT NAME";
        this.state = true;
    }

    public ServerInfo(String ip, String name, boolean state) {
        this.ip = ip;
        this.name = name;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerInfo that = (ServerInfo) o;
        return state == that.state &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, name, state);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

}

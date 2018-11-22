package com.example.thomaspedneault.ssh_client.model;

import java.util.Objects;

public class ServerInfo {

    private String ip;
    private String name;

    public ServerInfo() {
        this("DEFAULT", "DEFAULT");
    }

    public ServerInfo(String ip, String name) {
        this.ip = ip;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerInfo that = (ServerInfo) o;
        return Objects.equals(ip, that.ip) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, name);
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

}

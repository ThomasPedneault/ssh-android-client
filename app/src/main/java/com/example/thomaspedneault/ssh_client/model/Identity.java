package com.example.thomaspedneault.ssh_client.model;

import java.util.Objects;

public class Identity {

    private String username;
    private String password;

    public Identity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Identity{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identity identity = (Identity) o;
        return Objects.equals(username, identity.username) &&
                Objects.equals(password, identity.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

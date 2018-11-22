package com.example.thomaspedneault.ssh_client.model;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.thomaspedneault.ssh_client.R;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Objects;

public class ServerConnection {

    public enum States {
        Up(R.color.serverUp), Down(R.color.serverDown), Warn(R.color.serverWarn);

        private final int colorID;

        States(int id) {
            this.colorID = id;
        }

        public int getColor(Context ctx) {
            return ContextCompat.getColor(ctx, colorID);
        }
    }

    private static final int SSH_PORT = 22;
    private static final int TIMEOUT = 30000;

    private ServerInfo server;
    private Identity identity;

    private States state;
    private Session session;
    private Exception lastException;

    public ServerConnection(ServerInfo server, Identity identity) {
        this.server = server;
        this.identity = identity;
        this.state = States.Down;
    }

    public void asyncConnect(IOnAsyncTaskComplete asyncTaskEvent) {
        new Thread(() -> connect(asyncTaskEvent)).start();
    }

    public void asyncExecCommand(String command, IOnCommandCompleteEvent commandCompleteEvent) {
        new Thread(() -> execCommand(command, commandCompleteEvent)).start();
    }

    private void connect(IOnAsyncTaskComplete asyncTaskEvent) {
        asyncTaskEvent.onBegin();

        try {
            JSch jsch = new JSch();

            session = jsch.getSession(identity.getUsername(), server.getIp(), SSH_PORT);

            // TODO: Security risk, set host file name file.
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.setPassword(identity.getPassword());
            session.connect(TIMEOUT);

            asyncTaskEvent.onComplete(States.Up);
        }
        catch (JSchException e) {
            this.lastException = e;
            asyncTaskEvent.onComplete(States.Warn);
        }
    }

    private void execCommand(String command, IOnCommandCompleteEvent commandCompleteEvent) {
        String output = "";

        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);

            InputStream input = channel.getInputStream();
            channel.connect();

            InputStreamReader inputStreamReader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                output += line + "\n";
            }

            bufferedReader.close();
            inputStreamReader.close();
            channel.disconnect();
        } catch (JSchException | IOException e) {
            output = e.getMessage();
        } finally {

            commandCompleteEvent.onComplete(output);
        }
    }

    @Override
    public String toString() {
        return "ServerConnection{" +
                "server=" + server +
                ", identity=" + identity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerConnection that = (ServerConnection) o;
        return Objects.equals(server, that.server) &&
                Objects.equals(identity, that.identity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(server, identity);
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public ServerInfo getServer() {
        return server;
    }

    public void setServer(ServerInfo server) {
        this.server = server;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public String getExceptionMessage() {
        return lastException.getMessage();
    }

}

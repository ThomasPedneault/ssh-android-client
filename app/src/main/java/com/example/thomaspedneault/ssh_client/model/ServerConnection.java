package com.example.thomaspedneault.ssh_client.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ServerConnection implements Parcelable {

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

    private static int nextId = 1;

    private int id;
    private ServerInfo server;
    private Identity identity;
    private List<CommandPair> batchCommands;

    private States state;
    private Session session;
    private Exception lastException;

    public ServerConnection(ServerInfo server, Identity identity) {
        this.id = nextId++;
        this.server = server;
        this.identity = identity;
        this.state = States.Down;
        this.batchCommands = new ArrayList<>();
    }

    public ServerConnection(int id, ServerInfo server, Identity identity) {
        this.id = id;
        this.server = server;
        this.identity = identity;
        this.state = States.Down;
        this.batchCommands = new ArrayList<>();
    }

    protected ServerConnection(Parcel in) {
        this(in.readInt(), new ServerInfo(in.readString(), in.readString()), new Identity(in.readString(), in.readString()));
    }

    public void asyncConnect(IOnAsyncTaskComplete asyncTaskEvent) {
        new Thread(() -> connect(asyncTaskEvent)).start();
    }

    public void addBatchCommand(String command, IOnCommandCompleteEvent commandCompleteEvent) {
        batchCommands.add(new CommandPair(command, commandCompleteEvent));
    }

    public void runBatchCommands(int pollingRate) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for(CommandPair cmd : batchCommands) {
                    execCommand(cmd.command, cmd.completeEvent);
                }
            }
        }, 0, pollingRate);
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
                "id=" + id +
                ",server=" + server +
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

    public int getId() { return id; }

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

    public static final Creator<ServerConnection> CREATOR = new Creator<ServerConnection>() {
        @Override
        public ServerConnection createFromParcel(Parcel in) {
            return new ServerConnection(in);
        }

        @Override
        public ServerConnection[] newArray(int size) {
            return new ServerConnection[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(server.getIp());
        dest.writeString(server.getName());
        dest.writeString(identity.getUsername());
        dest.writeString(identity.getPassword());
    }

    private static class CommandPair {

        private String command;
        private IOnCommandCompleteEvent completeEvent;

        public CommandPair(String command, IOnCommandCompleteEvent completeEvent) {
            this.command = command;
            this.completeEvent = completeEvent;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public IOnCommandCompleteEvent getCompleteEvent() {
            return completeEvent;
        }

        public void setCompleteEvent(IOnCommandCompleteEvent completeEvent) {
            this.completeEvent = completeEvent;
        }
    }

}

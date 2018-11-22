package com.example.thomaspedneault.ssh_client.ui;

import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thomaspedneault.ssh_client.R;
import com.example.thomaspedneault.ssh_client.model.IOnAsyncTaskComplete;
import com.example.thomaspedneault.ssh_client.model.IOnCommandCompleteEvent;
import com.example.thomaspedneault.ssh_client.model.Identity;
import com.example.thomaspedneault.ssh_client.model.SampleData;
import com.example.thomaspedneault.ssh_client.model.ServerConnection;
import com.example.thomaspedneault.ssh_client.model.ServerInfo;
import com.example.thomaspedneault.ssh_client.util.CircleView;
import com.jcraft.jsch.JSch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class ServerListFragment extends Fragment {

    private List<ServerConnection> servers;

    private RecyclerView serversRecyclerView;
    private ServerAdapter serverAdapter;

    public ServerListFragment() {
        servers = new ArrayList<>();
        servers.addAll(SampleData.getServerConnections());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_server_list, container, false);

        serversRecyclerView = root.findViewById(R.id.servers_RecyclerView);
        serversRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        int margins = getResources().getDimensionPixelOffset(R.dimen.server_view_margin);
        serversRecyclerView.addItemDecoration(new ServerItemDecoration(margins));

        serverAdapter = new ServerAdapter(servers);
        serversRecyclerView.setAdapter(serverAdapter);

        return root;
    }

    public class ServerViewHolder extends RecyclerView.ViewHolder {

        // Declare UI references
        private View root;
        private TextView serverIpTextView;
        private TextView serverNameTextView;
        private TextView serverExceptionTextView;
        private final CircleView stateCircleView;

        private ServerConnection connection;

        public ServerViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;

            serverIpTextView = root.findViewById(R.id.serverIp_TextView);
            serverNameTextView = root.findViewById(R.id.serverName_TextView);
            serverExceptionTextView = root.findViewById(R.id.serverException_TextView);
            stateCircleView = root.findViewById(R.id.state_CircleView);

            root.setOnClickListener(v -> Toast.makeText(getContext(), connection.getServer().getIp(), Toast.LENGTH_SHORT).show());
        }

        /**
         * Sets the Holder's connection and attempts to start the connection to the server.
         * @param connection server connection to use.
         */
        public void setServerConnection(final ServerConnection connection) {
            this.connection = connection;
            this.serverIpTextView.setText(connection.getServer().getIp());
            this.serverNameTextView.setText(connection.getServer().getName());

            // Open the connection to the server.
            this.connection.asyncConnect(new IOnAsyncTaskComplete() {
                @Override
                public void onBegin() { }

                @Override
                public void onComplete(ServerConnection.States state) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> handleServerResponse(state));
                }
            });
        }

        /**
         * Handles the response from the server according to the returned state.
         * @param state response from the server
         */
        private void handleServerResponse(ServerConnection.States state) {
            stateCircleView.setColor(state.getColor(getContext()));
            connection.setState(state);

            // Verify which state was returned.
            switch(connection.getState()) {
                case Up:
                    connection.asyncExecCommand("top -bn1 | head -n 5", output -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        serverExceptionTextView.setVisibility(View.VISIBLE);
                        serverExceptionTextView.setText(output);
                    }));
                    break;
                case Warn:
                    serverExceptionTextView.setVisibility(View.VISIBLE);
                    serverExceptionTextView.setText(connection.getExceptionMessage());
                    break;
                default:
                    break;
            }
        }
    }

    public class ServerAdapter extends RecyclerView.Adapter<ServerViewHolder> {

        private List<ServerConnection> servers;

        public ServerAdapter(List<ServerConnection> servers) {
            this.servers = servers;
        }

        @NonNull
        @Override
        public ServerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ServerViewHolder(
                    LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.server_info_view, viewGroup, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ServerViewHolder serverViewHolder, int i) {
            serverViewHolder.setServerConnection(servers.get(i));
        }

        @Override
        public int getItemCount() {
            return servers.size();
        }
    }

    private class ServerItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public ServerItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
        }
    }
}

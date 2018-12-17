package com.example.thomaspedneault.ssh_client.ui.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.example.thomaspedneault.ssh_client.model.SampleData;
import com.example.thomaspedneault.ssh_client.model.ServerConnection;
import com.example.thomaspedneault.ssh_client.ui.terminal.TerminalActivity;
import com.example.thomaspedneault.ssh_client.ui.add.ServerAddActivity;
import com.example.thomaspedneault.ssh_client.util.CircleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class ServerListFragment extends Fragment {

    private static final int POLL_RATE = 5000;

    private List<ServerConnection> servers;

    private RecyclerView serversRecyclerView;
    private ServerAdapter serverAdapter;
    private FloatingActionButton addServerFab;

    public ServerListFragment() {
        servers = new ArrayList<>();
        servers.addAll(SampleData.getServerConnections());
    }

    public void addServerConnection(ServerConnection connection) {
        servers.add(connection);
        serverAdapter.notifyDataSetChanged();
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

        addServerFab = root.findViewById(R.id.addServer_Fab);
        addServerFab.setOnClickListener(v -> {
            Intent intent = new Intent(this.getActivity(), ServerAddActivity.class);
            startActivityForResult(intent, ServerListActivity.NEW_SERVER_REQUEST);
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getContext(), "Reached onActivityResult", Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case ServerListActivity.NEW_SERVER_REQUEST:
                if(resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getContext(), "Reached NEW_SERVER_REQUEST", Toast.LENGTH_SHORT).show();
                    ServerConnection connection = data.getParcelableExtra("connection");
                    addServerConnection(connection);
                }
                break;
            case ServerListActivity.EDIT_SERVER_REQUEST:
                if(resultCode == Activity.RESULT_OK) {
                    ServerConnection connection = data.getParcelableExtra("connection");
                    Toast.makeText(getContext(), connection.toString(), Toast.LENGTH_SHORT).show();
                    for(int i = 0; i < servers.size(); i++) {
                        if(servers.get(i).getId() == connection.getId()) {
                            servers.remove(i);
                            servers.add(i, connection);
                            serverAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
                break;
            default:
                Toast.makeText(getContext(),"Invalid Request Returned", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class ServerViewHolder extends RecyclerView.ViewHolder {

        // Declare UI references
        private View root;
        private TextView serverIpTextView;
        private TextView serverNameTextView;
        private TextView serverExceptionTextView;

        // Constraint layout containing all the UI elements for the server's output.
        private ConstraintLayout serverOutputConstraintLayout;

        // Server statistics UI elements.
        private TextView loadAvg1minTextView;
        private TextView loadAvg5minTextView;
        private TextView loadAvg15minTextView;
        private TextView filesystemTextView;
        private TextView usedRamTextView;
        private TextView countUsersTextView;
        private final CircleView stateCircleView;

        private ServerConnection connection;

        public ServerViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;

            serverIpTextView = root.findViewById(R.id.serverIp_TextView);
            serverNameTextView = root.findViewById(R.id.serverName_TextView);
            serverExceptionTextView = root.findViewById(R.id.serverException_TextView);

            serverOutputConstraintLayout = root.findViewById(R.id.serverOutput_ConstraintLayout);

            // Server statistics
            loadAvg1minTextView = root.findViewById(R.id.loadAvg1min_TextView);
            loadAvg5minTextView = root.findViewById(R.id.loadAvg5min_TextView);
            loadAvg15minTextView = root.findViewById(R.id.loadAvg15min_TextView);
            filesystemTextView = root.findViewById(R.id.filesystem_TextView);
            usedRamTextView = root.findViewById(R.id.usedRAM_TextView);
            countUsersTextView = root.findViewById(R.id.countUsers_TextView);
            stateCircleView = root.findViewById(R.id.state_CircleView);

            root.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), TerminalActivity.class);
                intent.putExtra("connection", connection);
                getActivity().startActivity(intent);
            });

            root.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select an Action")
                        .setPositiveButton("Edit", (dialog, which) -> {
                            Intent intent = new Intent(getActivity(), ServerAddActivity.class);
                            intent.putExtra("connection", connection);
                            getActivity().startActivityForResult(intent, ServerListActivity.EDIT_SERVER_REQUEST);
                        })
                        .setNegativeButton("Delete", (dialog, which) -> {
                            List<ServerConnection> newServers = new ArrayList<>();
                            for(ServerConnection c : servers) {
                                if(c.getId() != connection.getId()) {
                                    newServers.add(c);
                                }
                            }
                            servers = newServers;
                            serverAdapter.notifyDataSetChanged();
                        })
                        .setNeutralButton("Cancel", (dialog, which) -> {
                            // Do nothing
                        });
                builder.create().show();
                return true;
            });
        }

        private String getCommand(int id) {
            return getContext().getString(id);
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
                    // Make the server output constraint layout visible.
                    serverOutputConstraintLayout.setVisibility(View.VISIBLE);

                    connection.addBatchCommand(getCommand(R.string.loadAverage), output -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        String[] values = output.split(",");
                        loadAvg1minTextView.setText(values[0]);
                        loadAvg5minTextView.setText(values[1]);
                        loadAvg15minTextView.setText(values[2]);
                    }));

                    connection.addBatchCommand(getCommand(R.string.filesystem), output -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        String[] values = output.replace("\n", "").split(",");
                        int totalDiskSpace = Integer.parseInt(values[0]);
                        int usedDiskSpace = Integer.parseInt(values[1]);
                        float remaining = (usedDiskSpace * 100) / totalDiskSpace;
                        filesystemTextView.setText(String.format("%.2f", remaining) + "%");
                        if(remaining < 74.99)
                            filesystemTextView.setTextColor(ServerConnection.States.Up.getColor(getContext()));
                        else if(remaining < 89.99)
                            filesystemTextView.setTextColor(ServerConnection.States.Warn.getColor(getContext()));
                        else
                            filesystemTextView.setTextColor(ServerConnection.States.Down.getColor(getContext()));
                    }));

                    connection.addBatchCommand(getCommand(R.string.usedRam), output -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        String[] values = output.split(" ");
                        double percentage = (Double.parseDouble(values[1]) / Double.parseDouble(values[0])) * 100;
                        String value = String.format("%.2f", percentage);
                        usedRamTextView.setText(value + "%");
                    }));

                    connection.addBatchCommand(getCommand(R.string.countUsers), output -> Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        countUsersTextView.setText(output);
                    }));

                    connection.runBatchCommands(POLL_RATE);
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

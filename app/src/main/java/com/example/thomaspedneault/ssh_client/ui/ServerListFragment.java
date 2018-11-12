package com.example.thomaspedneault.ssh_client.ui;

import android.graphics.Rect;
import android.support.annotation.NonNull;
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
import com.example.thomaspedneault.ssh_client.model.ServerInfo;
import com.example.thomaspedneault.ssh_client.util.CircleView;
import com.jcraft.jsch.JSch;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ServerListFragment extends Fragment {

    private List<ServerInfo> dummy;

    private RecyclerView serversRecyclerView;
    private ServerAdapter serverAdapter;

    public ServerListFragment() {
        dummy = new ArrayList<>();

        dummy.add(new ServerInfo("10.39.167.25", "Thomas' UNIX VM", true));
        dummy.add(new ServerInfo("8.8.8.8", "Google DNS", false));
        dummy.add(new ServerInfo("linux2-cs.johnabbott.qc.ca", "Linux2", true));
        dummy.add(new ServerInfo("hello.hello.com", "Tsetin2", false));
        dummy.add(new ServerInfo("10.0.0.1", "localhost", false));
        dummy.add(new ServerInfo("10.39.167.25", "Thomas' UNIX VM", true));
        dummy.add(new ServerInfo("8.8.8.8", "Google DNS", false));
        dummy.add(new ServerInfo("linux2-cs.johnabbott.qc.ca", "Linux2", true));
        dummy.add(new ServerInfo("hello.hello.com", "Tsetin2", false));
        dummy.add(new ServerInfo("10.0.0.1", "localhost", false));
        dummy.add(new ServerInfo("10.39.167.25", "Thomas' UNIX VM", true));
        dummy.add(new ServerInfo("8.8.8.8", "Google DNS", false));
        dummy.add(new ServerInfo("linux2-cs.johnabbott.qc.ca", "Linux2", true));
        dummy.add(new ServerInfo("hello.hello.com", "Tsetin2", false));
        dummy.add(new ServerInfo("10.0.0.1", "localhost", false));
        dummy.add(new ServerInfo("10.39.167.25", "Thomas' UNIX VM", true));
        dummy.add(new ServerInfo("8.8.8.8", "Google DNS", false));
        dummy.add(new ServerInfo("linux2-cs.johnabbott.qc.ca", "Linux2", true));
        dummy.add(new ServerInfo("hello.hello.com", "Tsetin2", false));
        dummy.add(new ServerInfo("10.0.0.1", "localhost", false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_server_list, container, false);

        serversRecyclerView = root.findViewById(R.id.servers_RecyclerView);
        serversRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        int margins = getResources().getDimensionPixelOffset(R.dimen.server_view_margin);
        serversRecyclerView.addItemDecoration(new ServerItemDecoration(margins));

        serverAdapter = new ServerAdapter(dummy);
        serversRecyclerView.setAdapter(serverAdapter);

        return root;
    }

    public class ServerViewHolder extends RecyclerView.ViewHolder {

        // Declare UI references
        private View root;
        private TextView serverIpTextView;
        private TextView serverNameTextView;
        private CircleView stateCircleView;

        private ServerInfo server;

        public ServerViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;

            serverIpTextView = root.findViewById(R.id.serverIp_TextView);
            serverNameTextView = root.findViewById(R.id.serverName_TextView);
            stateCircleView = root.findViewById(R.id.state_CircleView);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), server.getIp(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setServerInfo(ServerInfo serverInfo) {
            this.server = serverInfo;
            this.serverIpTextView.setText(serverInfo.getIp());
            this.serverNameTextView.setText(serverInfo.getName());

            if(serverInfo.isState()) {
                stateCircleView.setColor(getContext().getColor(R.color.serverUp));
            } else {
                stateCircleView.setColor(getContext().getColor(R.color.serverDown));
            }
        }
    }

    public class ServerAdapter extends RecyclerView.Adapter<ServerViewHolder> {

        private List<ServerInfo> servers;

        public ServerAdapter(List<ServerInfo> servers) {
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
            serverViewHolder.setServerInfo(servers.get(i));
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

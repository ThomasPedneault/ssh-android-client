package com.example.thomaspedneault.ssh_client.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.thomaspedneault.ssh_client.R;
import com.example.thomaspedneault.ssh_client.model.Identity;
import com.example.thomaspedneault.ssh_client.model.ServerConnection;
import com.example.thomaspedneault.ssh_client.model.ServerInfo;

/**
 * A placeholder fragment containing a simple view.
 */
public class ServerAddFragment extends Fragment {

    public ServerAddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_server_add, container, false);

        // set the onclick event for the save button.
        Button saveButton = root.findViewById(R.id.save_Button);
        saveButton.setOnClickListener(v -> {
            // get the values from the form.
            EditText ipAddress = root.findViewById(R.id.serverIp_EditText);
            EditText nickname = root.findViewById(R.id.serverNickname_EditText);
            EditText username = root.findViewById(R.id.username_EditText);
            EditText password = root.findViewById(R.id.password_EditText);

            // return the serverconnection object.
            ServerInfo serverInfo = new ServerInfo(ipAddress.getText().toString(), nickname.getText().toString());
            Identity identity = new Identity(username.getText().toString(), password.getText().toString());
            ServerConnection conn = new ServerConnection(serverInfo, identity);

            ((ServerAddActivity) getActivity()).sendServerConnection(conn);
        });

        return root;
    }
}

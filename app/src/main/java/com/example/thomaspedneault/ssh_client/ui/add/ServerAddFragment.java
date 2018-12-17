package com.example.thomaspedneault.ssh_client.ui.add;

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

    // get the values from the form.
    private EditText ipAddress;
    private EditText nickname;
    private EditText username;
    private EditText password;

    public ServerAddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_server_add, container, false);

        ipAddress = root.findViewById(R.id.serverIp_EditText);
        nickname = root.findViewById(R.id.serverNickname_EditText);
        username = root.findViewById(R.id.username_EditText);
        password = root.findViewById(R.id.password_EditText);

        // Verify if there is a connection from the intent.
        ServerConnection connection = getActivity().getIntent().getParcelableExtra("connection");
        if(connection != null) {
            ipAddress.setText(connection.getServer().getIp());
            nickname.setText(connection.getServer().getName());
            username.setText(connection.getIdentity().getUsername());
            password.setText(connection.getIdentity().getPassword());
        }

        // set the onclick event for the save button.
        Button saveButton = root.findViewById(R.id.save_Button);
        saveButton.setOnClickListener(v -> {
            // return the ServerConnection object.
            ServerInfo serverInfo = new ServerInfo(ipAddress.getText().toString(), nickname.getText().toString());
            Identity identity = new Identity(username.getText().toString(), password.getText().toString());
            ServerConnection returnConnection;
            if(connection != null)
                returnConnection = new ServerConnection(connection.getId(), serverInfo, identity);
            else
                returnConnection = new ServerConnection(serverInfo, identity);

            ((ServerAddActivity) getActivity()).sendServerConnection(returnConnection);
        });

        return root;
    }
}

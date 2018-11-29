package com.example.thomaspedneault.ssh_client.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thomaspedneault.ssh_client.R;
import com.example.thomaspedneault.ssh_client.model.IOnAsyncTaskComplete;
import com.example.thomaspedneault.ssh_client.model.IOnCommandCompleteEvent;
import com.example.thomaspedneault.ssh_client.model.ServerConnection;

/**
 * A placeholder fragment containing a simple view.
 */
public class TerminalFragment extends Fragment {

    public TerminalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_terminal, container, false);

        // Get server connection and establish it.
        ServerConnection connection = getActivity().getIntent().getParcelableExtra("connection");
        connection.asyncConnect(new IOnAsyncTaskComplete() {
            @Override
            public void onBegin() { }

            @Override
            public void onComplete(ServerConnection.States state) {
                // Set the event listener on the button.
                Button executeButton = root.findViewById(R.id.execute_Button);
                executeButton.setOnClickListener(v -> {
                    EditText commandEditText = root.findViewById(R.id.command_EditText);
                    EditText outputEditText = root.findViewById(R.id.output_EditText);

                    String command = commandEditText.getText().toString();

                    connection.asyncExecCommand(command, output -> getActivity().runOnUiThread(() -> outputEditText.setText(output != "" ? output : "Null reponse")));
                });

                // Make the console visible.
                getActivity().runOnUiThread(() -> {
                    root.findViewById(R.id.terminalLoading_ConstraintLayout).setVisibility(View.GONE);
                    root.findViewById(R.id.terminal_ConstraintLayout).setVisibility(View.VISIBLE);
                });
            }
        });

        Toast.makeText(getContext(), connection.getServer().getIp(), Toast.LENGTH_LONG).show();

        return root;
    }
}

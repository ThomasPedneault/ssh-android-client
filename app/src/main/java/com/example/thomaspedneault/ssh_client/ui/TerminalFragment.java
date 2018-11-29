package com.example.thomaspedneault.ssh_client.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thomaspedneault.ssh_client.R;
import com.example.thomaspedneault.ssh_client.model.IOnAsyncTaskComplete;
import com.example.thomaspedneault.ssh_client.model.IOnCommandCompleteEvent;
import com.example.thomaspedneault.ssh_client.model.ServerConnection;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class TerminalFragment extends Fragment {

    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("[HH:mm:ss]");

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

                    // Close the keyboard.
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            getActivity().getCurrentFocus().getWindowToken(), 0);

                    connection.asyncExecCommand(command, output -> getActivity().runOnUiThread(() ->
                    {
                        String prefix = DATE_FORMAT.format(new Date()) + " " + connection.getIdentity().getUsername();
                        outputEditText.append(prefix + ": " + command + "\n");
                        String[] lines = output.split("\n");
                        for(int i = 0; i < lines.length; i++) {
                            outputEditText.append("-->" + lines[i] + "\n");
                        }
                    }));
                });

                // Make the console visible.
                getActivity().runOnUiThread(() -> {
                    root.findViewById(R.id.terminalLoading_ConstraintLayout).setVisibility(View.GONE);
                    root.findViewById(R.id.terminal_ConstraintLayout).setVisibility(View.VISIBLE);
                });
            }
        });

        return root;
    }
}

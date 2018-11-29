package com.example.thomaspedneault.ssh_client.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
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
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class TerminalFragment extends Fragment {

    private static final String OUTPUT_LINE_PREFIX = ">";

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("[HH:mm:ss]");

    public TerminalFragment() { }

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
                // Set the event listeners for the UI elements.
                Button executeButton = root.findViewById(R.id.execute_Button);
                EditText commandEditText = root.findViewById(R.id.command_EditText);

                commandEditText.setOnKeyListener((v, keyCode, event) -> {
                    if(event.getAction() == KeyEvent.ACTION_DOWN) {
                        switch(keyCode) {
                            case KeyEvent.KEYCODE_ENTER:
                                executeButton.callOnClick();
                                return true;
                            default:
                                break;
                        }
                    }

                    return false;
                });

                executeButton.setOnClickListener(v -> {
                    EditText outputEditText = root.findViewById(R.id.output_EditText);

                    String command = commandEditText.getText().toString();

                    // Close the keyboard.
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(inputManager).hideSoftInputFromWindow(
                            Objects.requireNonNull(getActivity().getCurrentFocus()).getWindowToken(), 0);

                    // Check if the command is to clear the console.
                    if(command.equals("clear")) {
                        outputEditText.setText("");
                        return;
                    }

                    connection.asyncExecCommand(command, output -> getActivity().runOnUiThread(() ->
                    {
                        String prefix = DATE_FORMAT.format(new Date()) + " " + connection.getIdentity().getUsername();
                        outputEditText.append(prefix + ": " + command + "\n");
                        String[] lines = output.split("\n");
                        for (String line : lines) {
                            outputEditText.append(OUTPUT_LINE_PREFIX + line + "\n");
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

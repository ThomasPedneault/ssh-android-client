package com.example.thomaspedneault.ssh_client.ui.terminal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thomaspedneault.ssh_client.R;
import com.example.thomaspedneault.ssh_client.model.IOnAsyncTaskComplete;
import com.example.thomaspedneault.ssh_client.model.SampleData;
import com.example.thomaspedneault.ssh_client.model.ServerConnection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class TerminalFragment extends Fragment {

    private static final String OUTPUT_LINE_PREFIX = "";

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("[HH:mm:ss]");

    private EditText outputEditText;
    private EditText commandEditText;
    private Button executeButton;
    private Button saveButton;
    private Dialog savedCommandsDialog;

    private ServerConnection connection;
    private List<String> savedCommands;

    public TerminalFragment() {
        savedCommands = SampleData.getSavedCommands();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_terminal, container, false);

        // Get server connection and establish it.
        connection = getActivity().getIntent().getParcelableExtra("connection");
        connection.asyncConnect(new IOnAsyncTaskComplete() {
            @Override
            public void onBegin() { }

            @Override
            public void onComplete(ServerConnection.States state) {
                // Set the event listeners for the UI elements.
                executeButton = root.findViewById(R.id.execute_Button);
                saveButton = root.findViewById(R.id.save_Button);
                commandEditText = root.findViewById(R.id.command_EditText);
                outputEditText = root.findViewById(R.id.output_EditText);
                outputEditText.setShowSoftInputOnFocus(false);

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

                // Execute the command in the EditText and display the output.
                executeButton.setOnClickListener(v -> {
                    String command = commandEditText.getText().toString();

                    // Close the keyboard.
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(inputManager).hideSoftInputFromWindow(
                            Objects.requireNonNull(getActivity().getCurrentFocus()).getWindowToken(), 0);

                    runCommand(command);
                });

                // Add the command in the EditText to the list of saved commands.
                saveButton.setOnClickListener(v -> {
                    // If the command is empty, do nothing.
                    if(commandEditText.getText().toString() == "")
                        return;

                    savedCommands.add(commandEditText.getText().toString());

                    // Close the keyboard.
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(inputManager).hideSoftInputFromWindow(
                            Objects.requireNonNull(getActivity().getCurrentFocus()).getWindowToken(), 0);
                });

                // Set the event listener to display the command selection.
                outputEditText.setOnLongClickListener(v -> {
                    if(savedCommandsDialog == null) {
                        savedCommandsDialog = new Dialog(getContext(), R.style.DialogSlideAnim);
                        savedCommandsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        savedCommandsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        savedCommandsDialog.setContentView(R.layout.saved_commands_view);
                        savedCommandsDialog.setCanceledOnTouchOutside(true);
                        savedCommandsDialog.setCancelable(true);
                    }

                    RecyclerView rv = savedCommandsDialog.findViewById(R.id.savedCommands_RecyclerView);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new LinearLayoutManager(getContext()));

                    SavedCommandAdapter savedCommandAdapter = new SavedCommandAdapter(savedCommands);
                    rv.setAdapter(savedCommandAdapter);

                    // Update the data in the adapter in case it was changed.
                    savedCommandAdapter.notifyDataSetChanged();
                    savedCommandsDialog.show();

                    return true;
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

    private void runCommand(String command) {
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
    }

    private class SavedCommandHolder extends RecyclerView.ViewHolder {
        private View root;
        private TextView commandNameTextView;

        private String command;
        private int index;

        public SavedCommandHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            commandNameTextView = root.findViewById(R.id.commandName_TextView);

            // Run the command if it is clicked.
            commandNameTextView.setOnClickListener(v -> {
                runCommand(command);
                savedCommandsDialog.dismiss();
            });

            // Prompt the user to delete the command.
            commandNameTextView.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Would you like to delete this command?")
                        .setTitle(commandNameTextView.getText().toString())
                        .setPositiveButton("Delete", (dialog, which) -> {
                            savedCommands.remove(this.index);

                            // Notify the adapter that the data changed.
                            RecyclerView rv = savedCommandsDialog.findViewById(R.id.savedCommands_RecyclerView);
                            rv.getAdapter().notifyDataSetChanged();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // Do nothing
                        });
                builder.create().show();
                return true;
            });
        }

        public void setCommand(int index) {
            this.index = index;
            this.command = savedCommands.get(index);
            this.commandNameTextView.setText(command);
        }
    }

    private class SavedCommandAdapter extends RecyclerView.Adapter<SavedCommandHolder> {
        private List<String> savedCommands;

        public SavedCommandAdapter(List<String> commands) { this.savedCommands = commands; }

        @NonNull
        @Override
        public SavedCommandHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new SavedCommandHolder(
                    LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.command_view, viewGroup, false)
            );
        }

        @NonNull
        @Override
        public void onBindViewHolder(@NonNull SavedCommandHolder holder, int i) {
            holder.setCommand(i);
        }

        @Override
        public int getItemCount() {
            return savedCommands.size();
        }
    }
}

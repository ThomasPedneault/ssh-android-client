package com.example.thomaspedneault.ssh_client.ui.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.thomaspedneault.ssh_client.R;
import com.example.thomaspedneault.ssh_client.model.ServerConnection;

public class ServerAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void sendServerConnection(ServerConnection connection) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("connection", connection);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}

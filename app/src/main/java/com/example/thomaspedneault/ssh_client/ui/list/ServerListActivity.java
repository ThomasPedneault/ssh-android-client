package com.example.thomaspedneault.ssh_client.ui.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.thomaspedneault.ssh_client.R;
import com.example.thomaspedneault.ssh_client.model.ServerConnection;

public class ServerListActivity extends AppCompatActivity {

    public static final int NEW_SERVER_REQUEST = 1001;
    public static final int EDIT_SERVER_REQUEST = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


}

package com.example.thomaspedneault.ssh_client.ui;

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

    private static final int NEW_SERVER_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case NEW_SERVER_REQUEST:
                if(resultCode == Activity.RESULT_OK) {
                    ServerConnection connection = data.getParcelableExtra("connection");
                    Toast.makeText(getApplicationContext(), connection.getServer().getIp(), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.newServer_MenuItem:
                Intent intent = new Intent(this, ServerAddActivity.class);
                this.startActivityForResult(intent, NEW_SERVER_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

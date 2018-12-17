package com.example.thomaspedneault.ssh_client.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thomaspedneault.ssh_client.sqlite.Table;
import com.example.thomaspedneault.ssh_client.sqlite.TableFactory;

public class CommandLogDatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_FILE_NAME = "logs.db";

    private static final int DATABASE_VERSION = 1;

    private Context context;

    private Table<CommandLog> commandLogTable;

    public CommandLogDatabaseHandler(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);

        commandLogTable = TableFactory.makeFactory(this, CommandLog.class).getTable();

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(commandLogTable.getCreateTableStatement());
        if(commandLogTable.hasInitialData()) {
            commandLogTable.initialize(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CommandLogDatabaseHandler.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL(commandLogTable.getDropTableStatement());
        onCreate(db);
    }

    public Context getContext() {
        return context;
    }

    public Table<CommandLog> getCommandLogTable() {
        return commandLogTable;
    }

}

package com.example.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements DialogFragment.DialogListener, UpdateFragment.DialogListener1 {
    private static final String TAG = "MainActivity";
    private SQLiteDatabase mDatabase;
    String name, address, phone;
    String updtName, updtAddress, updtPhone;
    FloatingActionButton fab;
    EmployeeAdapter mAdapter;
    static MainActivity activity;
    int updtID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        EmployeeDBHelper dbHelper = new EmployeeDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EmployeeAdapter(this, getAllData(), new EmployeeAdapter.AdapterInterface() {
            @Override
            public void editOnClick(View v, int position) {
                Cursor cursor = getAllData();
                cursor.moveToPosition(position);
                 updtID = cursor.getInt(cursor.getColumnIndex(EmployeeDBHelper.COLUMN_ID));

                UpdateFragment dialogFragment1 = new UpdateFragment();
                dialogFragment1.show(getSupportFragmentManager(), "Update Dialog");

            }

            @Override
            public void deleteOnClick(View v, int position) {
                Log.i(TAG, "deleteOnClick: " + position);
                Cursor cursor = getAllData();
                cursor.moveToPosition(position);
                int id = cursor.getInt(cursor.getColumnIndex(EmployeeDBHelper.COLUMN_ID));
                mDatabase.delete(EmployeeDBHelper.TABLE_NAME, EmployeeDBHelper.COLUMN_ID + "=" + id, null);
                mAdapter.swapCursor(getAllData());
            }
        });
        recyclerView.setAdapter(mAdapter);

        Log.i(TAG, "onCreate: ");

        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");
                DialogFragment dialogFragment = new DialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "Dialog");

            }
        });
    }


    public void addRow() {
        Log.i(TAG, "addRow: ");
        if (name.trim().length() == 0 || address.trim().length() == 0 || phone.trim().length() == 0) {
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(EmployeeDBHelper.COLUMN_NAME, name);
        cv.put(EmployeeDBHelper.COLUMN_ADDRESS, address);
        cv.put(EmployeeDBHelper.COLUMN_PHONE, phone);

        mDatabase.insert(EmployeeDBHelper.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllData());
    }

    public void updateRow(int id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EmployeeDBHelper.COLUMN_NAME, updtName);
        contentValues.put(EmployeeDBHelper.COLUMN_PHONE, updtPhone);
        contentValues.put(EmployeeDBHelper.COLUMN_ADDRESS, updtAddress);
        Log.i(TAG, "editOnClick: "  + updtPhone + " " +updtAddress);


        mDatabase.update(EmployeeDBHelper.TABLE_NAME, contentValues, EmployeeDBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        mAdapter.swapCursor(getAllData());
    }

    private Cursor getAllData() {
        Log.i(TAG, "getAllData: ");
        return mDatabase.query(
                EmployeeDBHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void sendData(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        Log.i(TAG, "sendData: " + name + " " + address + " " + phone);
    }

    @Override
    public void updateData(String name, String address, String phone) {
        updtName = name;
        updtAddress = address;
        updtPhone = phone;
        Log.i(TAG, "updateData: " + name + " " + address + " " + phone);
        updateRow(updtID);
    }

    public static MainActivity getInstance() {
        return activity;
    }


}

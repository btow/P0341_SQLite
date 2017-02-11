package com.example.samsung.p0341_sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName, etEmail, etId;
    Button btnAdd, btnRead, btnClear, btnUpd, btnDel;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etId = (EditText) findViewById(R.id.etId);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        btnUpd = (Button) findViewById(R.id.btnUpd);
        btnUpd.setOnClickListener(this);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {

        String id = etId.getText().toString();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        switch (v.getId()) {
            case R.id.btnAdd :
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_MAIL, email);
                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                break;

            case R.id.btnRead :
                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS,null,null,null,null,null,null);

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID),
                        nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME),
                        emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);
                    do {
                        Log.d("mLog", "ID = " + cursor.getInt(idIndex)
                                + ", name = " + cursor.getString(nameIndex)
                                + ", email = " + cursor.getString(emailIndex));
                    } while (cursor.moveToNext());
                } else {
                    Log.d("mLog", "0 rows");
                }
                cursor.close();

                break;
            case R.id.btnClear :
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                break;
            case R.id.btnUpd :

                if (id.equalsIgnoreCase("")) break;

                contentValues.put(DBHelper.KEY_MAIL, email);
                contentValues.put(DBHelper.KEY_NAME, name);
                String[] args = {id};

                int updCount = database.update(DBHelper.TABLE_CONTACTS, contentValues, DBHelper.KEY_ID + "= ?", args);

                Log.d("mLog", "updates rows count = " + updCount);

                break;
            case R.id.btnDel :

                if (id.equalsIgnoreCase("")) break;

                int delCount = database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + "=" + id, null);

                Log.d("mLog", "deleted rows count = " + delCount);
                break;
            default:
                break;
        }
        dbHelper.close();
    }
}

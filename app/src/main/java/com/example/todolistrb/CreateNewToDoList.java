package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateNewToDoList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_to_do_list);
    }

    public void CreateListFunction(View view)
    {
        EditText ListTitle = findViewById(R.id.ListTitle);
        if(ListTitle.length() != 0)
        {
            String ListValue;
            int CompletedValue = 0;
            long Status;

            ListValue = ListTitle.getText().toString();
            SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
            Cursor db_cursor = DB.rawQuery("Select * From ToDoListTable where ListName = '" + ListValue + "' order by ListName ASC", null);
            db_cursor.moveToFirst();
            if(db_cursor.getCount() == 0)
            {
                String UniqueId = new SimpleDateFormat("DDMMYYYYHHMMSS", Locale.getDefault()).format(new Date());
                String CurrentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String CurrentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                ContentValues cValues = new ContentValues();
                cValues.put("UniqueId", UniqueId);
                cValues.put("ListName", ListValue);
                cValues.put("CreationDate", CurrentDate);
                cValues.put("CreationTime", CurrentTime);
                cValues.put("Completed", CompletedValue);
                Status = DB.insert("ToDoListTable", null, cValues);

                if(Status != -1)
                {
                    ListTitle.setText(null);
                    Toast.makeText(CreateNewToDoList.this, "New list created successfully.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(CreateNewToDoList.this, "Error occurred while creating new List. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                ListTitle.setText(null);
                Toast.makeText(CreateNewToDoList.this, "List Title already exist. Please try again with a different List Title.", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(CreateNewToDoList.this, "List Title cannot be blank.", Toast.LENGTH_SHORT).show();
        }
    }

    public void ResetFunction(View view)
    {
        EditText listTitle = findViewById(R.id.ListTitle);
        if(listTitle.length() != 0)
        {
            listTitle.setText(null);
            Toast.makeText(CreateNewToDoList.this, "Reset successful.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(CreateNewToDoList.this, "List title is already blank.", Toast.LENGTH_SHORT).show();
        }
    }

    public void GoBackFunction(View view)
    {
        Intent GoBack = new Intent(CreateNewToDoList.this, MainActivity.class);
        startActivity(GoBack);
    }
}
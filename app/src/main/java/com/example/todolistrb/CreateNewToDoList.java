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
// In below if condition, we are checking List name field should not be blank.
        if(ListTitle.length() != 0)
        {
            String ListValue;
            int CompletedValue = 0;
            long Status;

            ListValue = ListTitle.getText().toString();
            SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
            Cursor db_cursor = DB.rawQuery("Select * From ToDoListTable where ListName = '" + ListValue + "' order by ListName ASC", null);
            db_cursor.moveToFirst();
// Using below if condition, we are checking if entered list name already exist in our database or not. If it doesn't exist, it will proceed to create new list.
            if(db_cursor.getCount() == 0)
            {
// UniqueId, CreationDate & CreationTime is calculated in below code using timestamp.
                String CurrentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String CurrentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
// We are putting values column by column in DB using below code.
                ContentValues cValues = new ContentValues();
                cValues.put("ListName", ListValue);
                cValues.put("CreationDate", CurrentDate);
                cValues.put("CreationTime", CurrentTime);
                cValues.put("Completed", CompletedValue);
                Status = DB.insert("ToDoListTable", null, cValues);
// We are checking if insertion is successful or not.
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
// If list title already exist in DB, it will jump here.
            else
            {
                ListTitle.setText(null);
                Toast.makeText(CreateNewToDoList.this, "List Title already exist. Please try again with a different List Title.", Toast.LENGTH_LONG).show();
            }
            DB.close();
        }
// If there is no value entered by user, below message will be displayed to user.
        else
        {
            Toast.makeText(CreateNewToDoList.this, "List Title cannot be blank.", Toast.LENGTH_SHORT).show();
        }
    }

// Below is code to reset field values.
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
            Toast.makeText(CreateNewToDoList.this, "List title text box is already blank.", Toast.LENGTH_SHORT).show();
        }
    }

// Below is code to navigate to home page.
    public void GoBackFunction(View view)
    {
        Intent GoBack = new Intent(CreateNewToDoList.this, MainActivity.class);
        startActivity(GoBack);
    }

// Below is code to delete all lists from DB.
    public void DeleteAllListsFromDB(View view)
    {
        SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
        Cursor db_cursor = DB.rawQuery("Select * From ToDoListTable", null);
        if(db_cursor.getCount() == 0)
        {
            Toast.makeText(CreateNewToDoList.this, "There are no lists to delete.", Toast.LENGTH_SHORT).show();
        }
        else{
            DB.execSQL("DELETE from ToDoListTable");
            Cursor db_cursor1 = DB.rawQuery("Select * From ToDoListTable", null);
            if (db_cursor1.getCount() == 0) {
                Toast.makeText(CreateNewToDoList.this, "All lists are deleted successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateNewToDoList.this, "Failed to delete all Lists. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
        DB.close();
    }
}
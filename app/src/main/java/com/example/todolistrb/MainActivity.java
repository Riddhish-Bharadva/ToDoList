package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Calling a constructor of an activity (Java class) to establish connection with my Database and create a table if it does not exist.
        CreateDBT CDBT = new CreateDBT(getApplicationContext());
        CDBT.getWritableDatabase();

// Displaying data from database to my ListTitleView on Home Page.
        SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
        ListView ListTitleView = (ListView) findViewById(R.id.ListTitleView);
        ArrayList<String> ArrayListTitle = new ArrayList<String>();
        ArrayAdapter<String> ArrayListTitleAdapter;
        Cursor db_cursor = DB.rawQuery("Select * From ToDoListTable order by ListName ASC", null);
        if(db_cursor.getCount() != 0)
        {
            if(db_cursor.moveToFirst())
            {
                do{
                    ArrayListTitle.add(db_cursor.getString(db_cursor.getColumnIndex("ListName")));
                    ArrayListTitleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayListTitle);
                    ListTitleView.setAdapter(ArrayListTitleAdapter);
                }while (db_cursor.moveToNext());
            }
            db_cursor.close();
        }
        else
        {
            ArrayListTitle.add("No list created yet to display here.");
            ArrayListTitleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayListTitle);
            ListTitleView.setAdapter(ArrayListTitleAdapter);
        }
        DB.close();
    }
    public void CreateNewList (View view)
    {
        Intent CreateNewList = new Intent(MainActivity.this, CreateNewToDoList.class);
        startActivity(CreateNewList);
    }
}
package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class TaskHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_home_page);

        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListName");
        TextView ListTitleHeading = findViewById(R.id.ListTitleHeading);
        ListTitleHeading.setText("Todo list Name : " + ListName);

// Displaying data from database to my ListTitleView on Home Page.
        SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
        ListView TaskTitleView = findViewById(R.id.TaskView);
        final ArrayList<String> ArrayTaskTitle = new ArrayList<String>();
        ArrayAdapter<String> ArrayTaskTitleAdapter;
// In below select statement, COLLATE NOCASE is key word to ignore case of data and sort them in ascending / descending order.
        Cursor db_cursor = DB.rawQuery("Select * From TaskTable where ListName = '" + ListName + "' order by TaskName COLLATE NOCASE ASC", null);
        if(db_cursor.getCount() != 0)
        {
            if(db_cursor.moveToFirst())
            {
                do{
                    String TaskName = db_cursor.getString(db_cursor.getColumnIndex("TaskName"));
                    ArrayTaskTitle.add(TaskName);
                    ArrayTaskTitleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayTaskTitle);
                    TaskTitleView.setAdapter(ArrayTaskTitleAdapter);
                }while (db_cursor.moveToNext());
            }
            db_cursor.close();
        }
// If there are no entries in DB, it will display below message to user.
        else
        {
            ArrayTaskTitle.add("No task created yet to display here.");
            ArrayTaskTitleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayTaskTitle);
            TaskTitleView.setAdapter(ArrayTaskTitleAdapter);
        }
        DB.close();
    }

    public void AddNewTask(View view)
    {
        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListName");
        Intent CreateNewTask = new Intent(TaskHomePage.this, CreateNewTask.class);
        CreateNewTask.putExtra("ListName", ListName);
        startActivity(CreateNewTask);
    }

// Below is code to navigate to Task home page.
    public void GoBackFunction(View view)
    {
        Intent GoBack = new Intent(TaskHomePage.this, MainActivity.class);
        startActivity(GoBack);
    }
}
package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNewTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_task);

// Below code is fetching List Name from previous page to this page. We need this to enter ListName in our database whenever a user tries to add new Task in any list.
        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListName");

// Below code is setting List Name in it's Text Box.
        TextView ListTitleTextBox = findViewById(R.id.ListTitleTextBox);
        ListTitleTextBox.setText("You are going to add a Task in " + ListName);
    }

    public void CreateTaskButton(View view)
    {
        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListName");
        EditText NewTask = findViewById(R.id.NewTaskTextBox);
// In below if condition, we are checking List name field should not be blank.
        if(NewTask.length() != 0)
        {
            String TaskValue;
            int CompletedValue = 0;
            long Status;

            TaskValue = NewTask.getText().toString();
            SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
            Cursor db_cursor = DB.rawQuery("Select * From TaskTable where ListName = '" + ListName + "' and TaskName = '" + TaskValue + "'", null);
            db_cursor.moveToFirst();
// Using below if condition, we are checking if entered list name already exist in our database or not. If it doesn't exist, it will proceed to create new list.
            if (db_cursor.getCount() == 0) {
// UniqueId, CreationDate & CreationTime is calculated in below code using timestamp.
                String DueDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
// We are putting values column by column in DB using below code.
                ContentValues cValues = new ContentValues();
                cValues.put("ListName", ListName);
                cValues.put("TaskName", TaskValue);
                cValues.put("DueDate", DueDate);
                cValues.put("TaskCompleted", CompletedValue);
                Status = DB.insert("TaskTable", null, cValues);
// We are checking if insertion is successful or not.
                if (Status != -1)
                {
                    NewTask.setText(null);
                    Toast.makeText(CreateNewTask.this, "New task created successfully.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(CreateNewTask.this, "Error occurred while creating new task. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
// If list title already exist in DB, it will jump here.
            else
            {
                NewTask.setText(null);
                Toast.makeText(CreateNewTask.this, "This task already exist. Please try again with a different task name.", Toast.LENGTH_LONG).show();
            }
            DB.close();
        }
// If Task name is blank, below message will be displayed.
        else
        {
            Toast.makeText(CreateNewTask.this, "Task name cannot be blank.", Toast.LENGTH_LONG).show();
        }
    }

// Below code is for reset function.
    public void OnClickResetButton(View view)
    {
        EditText NewTaskTextBox = findViewById(R.id.NewTaskTextBox);
        if(NewTaskTextBox.length() != 0)
        {
            NewTaskTextBox.setText(null);
            Toast.makeText(CreateNewTask.this, "Reset successful.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(CreateNewTask.this, "Task title is already blank.", Toast.LENGTH_SHORT).show();
        }
    }

// Below is code to navigate to Task home page.
    public void GoBackFunction(View view)
    {
        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListName");
        Intent GoBack = new Intent(CreateNewTask.this, TaskHomePage.class);
        GoBack.putExtra("ListName", ListName);
        startActivity(GoBack);
    }
}
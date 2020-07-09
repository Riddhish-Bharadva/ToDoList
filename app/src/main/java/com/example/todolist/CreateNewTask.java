package com.example.todolist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNewTask extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_task);

// Below code is fetching List Name from previous page to this page. We need this to enter ListName in our database whenever a user tries to add new Task in any list.
        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListName");

// Below code is setting List Name in it's Text Box.
        TextView ListTitleTextBox = findViewById(R.id.ListTitleTextBox);
        ListTitleTextBox.setText(ListName);
// Below code is used to select date on create new task page.
        final Calendar myCalendar = Calendar.getInstance();
        final EditText TaskDate = findViewById(R.id.TaskDateTextBox);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                TaskDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        TaskDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateNewTask.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
// Calendar code ends.
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
                EditText TaskDate = findViewById(R.id.TaskDateTextBox);
                String DueDate;
                if(TaskDate.getText().toString().length() != 0)
                {
                    DueDate = TaskDate.getText().toString();
                }
                else
                {
                    DueDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                }
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
                    TaskDate.setText(null);
                    Intent GoBack = new Intent(CreateNewTask.this, TaskHomePage.class);
                    GoBack.putExtra("ListName", ListName);
                    Toast.makeText(CreateNewTask.this, "Task is successfully created.", Toast.LENGTH_SHORT).show();
                    this.finish();
                    startActivity(GoBack);
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
        EditText TaskDate = findViewById(R.id.TaskDateTextBox);
        if(NewTaskTextBox.length() != 0 || TaskDate.getText().toString().length() != 0)
        {
            NewTaskTextBox.setText(null);
            TaskDate.setText(null);
            Toast.makeText(CreateNewTask.this, "Reset successful.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(CreateNewTask.this, "All fields are already blank.", Toast.LENGTH_SHORT).show();
        }
    }

// Below is code to navigate back in application.
    public void GoBackFunction(View view)
    {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListName");
        Intent GoBack = new Intent(CreateNewTask.this, TaskHomePage.class);
        GoBack.putExtra("ListName", ListName);
        startActivity(GoBack);
        finish();
    }
}
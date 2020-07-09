package com.rab.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskEditPage extends AppCompatActivity {

    String GV_ListName;
    String GV_TaskName;
    SQLiteDatabase myDB;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit_page);

        SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
        this.myDB = DB;
        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListName");
        String TaskName = bundle.getString("TaskName");
        this.GV_ListName = ListName;
        this.GV_TaskName = TaskName;
        TextView ListTitleTextBox = findViewById(R.id.EListTitleTextBox);
        EditText EditTaskTextBox = findViewById(R.id.EditTaskTextBox);
        final EditText ETaskDateTextBox = findViewById(R.id.ETaskDateTextBox);
        Cursor db_Connection = myDB.rawQuery("Select * from TaskTable where ListName = '" + GV_ListName + "' and TaskName = '" + GV_TaskName + "'", null);
        if(db_Connection.getCount() != 0) {
            db_Connection.moveToFirst();
            String DueDate = db_Connection.getString(db_Connection.getColumnIndex("DueDate"));
            ETaskDateTextBox.setText(DueDate);
        }
        ListTitleTextBox.setText(ListName);
        EditTaskTextBox.setText(TaskName);
// Calendar code starts.
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                // ToDo Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                ETaskDateTextBox.setText(sdf.format(myCalendar.getTime()));
            }
        };
        ETaskDateTextBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                new DatePickerDialog(TaskEditPage.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
// Calendar code ends.

    public void UpdateTask(View view)
    {
        EditText UpdatedTaskTitle = findViewById(R.id.EditTaskTextBox);
        EditText UpdatedTaskDueDate = findViewById(R.id.ETaskDateTextBox);
        Cursor cursor = myDB.rawQuery("Select * from TaskTable where ListName = '" + GV_ListName + "' and TaskName = '" + UpdatedTaskTitle.getText().toString() + "'", null);
        Intent updatedRefresh = new Intent(TaskEditPage.this, TaskHomePage.class);
        updatedRefresh.putExtra("ListName",GV_ListName);
        if(cursor.getCount() == 0 && UpdatedTaskTitle.getText().length() != 0 && UpdatedTaskDueDate.getText().length() != 0)
        {
            String UR = "Update TaskTable set TaskName = '" + UpdatedTaskTitle.getText().toString() + "' where ListName = '" + GV_ListName + "' and TaskName = '" + GV_TaskName + "'";
            myDB.execSQL(UR);
            String UR1 = "Update TaskTable set DueDate = '" + UpdatedTaskDueDate.getText().toString() + "' where ListName = '" + GV_ListName + "' and TaskName = '" + UpdatedTaskTitle.getText().toString() + "'";
            myDB.execSQL(UR1);
            Cursor check_cursor = myDB.rawQuery("Select * from TaskTable where ListName = '" + GV_ListName + "' and TaskName = '" + GV_TaskName + "'", null);
            if(check_cursor.getCount() == 0)
            {
                Toast.makeText(TaskEditPage.this,"Task updated successfully.",Toast.LENGTH_SHORT).show();
                this.finish();
                startActivity(updatedRefresh);
            }
            else
            {
                Toast.makeText(TaskEditPage.this, "Task cannot be edited. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(cursor.getCount() != 0 && UpdatedTaskDueDate.getText().length() != 0)
        {
            if(cursor.moveToFirst()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date Date1 = null, Date2 = null;
                try {
                    Date1 = sdf.parse(UpdatedTaskDueDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    Date2 = sdf.parse(cursor.getString(cursor.getColumnIndex("DueDate")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (Date1 != null && Date2 != null && Date1.compareTo(Date2) != 0) // Checking if both dates are not same.
                {
                    Log.i("Log.i",cursor.getString(cursor.getColumnIndex("DueDate")));
                    String UR1 = "Update TaskTable set DueDate = '" + UpdatedTaskDueDate.getText().toString() + "' where ListName = '" + GV_ListName + "' and TaskName = '" + UpdatedTaskTitle.getText().toString() + "'";
                    myDB.execSQL(UR1);
                    Toast.makeText(TaskEditPage.this, "Task updated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TaskEditPage.this, "Task with same details cannot be updated.", Toast.LENGTH_SHORT).show();
                }
            }
            this.finish();
            startActivity(updatedRefresh);
        }
        else if(cursor.getCount() == 0 && UpdatedTaskTitle.getText().length() != 0 && UpdatedTaskDueDate.getText().length() == 0)
        {
            String UR = "Update TaskTable set TaskName = '" + UpdatedTaskTitle.getText().toString() + "' where ListName = '" + GV_ListName + "' and TaskName = '" + GV_TaskName + "'";
            myDB.execSQL(UR);
            Cursor check_cursor = myDB.rawQuery("Select * from TaskTable where ListName = '" + GV_ListName + "' and TaskName = '" + GV_TaskName + "'", null);
            if(check_cursor.getCount() == 0)
            {
                Toast.makeText(TaskEditPage.this,"Task updated successfully.",Toast.LENGTH_SHORT).show();
                this.finish();
                startActivity(updatedRefresh);
            }
            else
            {
                Toast.makeText(TaskEditPage.this, "Task cannot be edited. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(TaskEditPage.this,"Task with same name already exist in this list.",Toast.LENGTH_SHORT).show();
        }
    }

// Below is code to reset field values.
    public void ResetFunction(View view)
    {
        EditText TaskTitleTextBox = findViewById(R.id.EditTaskTextBox);
        EditText ETaskDateTextBox = findViewById(R.id.ETaskDateTextBox);
        Cursor db_cursor = myDB.rawQuery("Select * From TaskTable where ListName = '" + GV_ListName + "' and TaskName = '" + GV_TaskName + "'", null);
        String DueDate = db_cursor.getString(db_cursor.getColumnIndex("DueDate"));
        if(TaskTitleTextBox.length() != 0 || ETaskDateTextBox.getText().length() != 0)
        {
            TaskTitleTextBox.setText(GV_ListName);
            ETaskDateTextBox.setText(DueDate);
            Toast.makeText(TaskEditPage.this, "Reset successful.", Toast.LENGTH_SHORT).show();
        }
        else if(TaskTitleTextBox.getText().toString() != GV_ListName || ETaskDateTextBox.getText().toString() != DueDate)
        {
            Toast.makeText(TaskEditPage.this, "Reset successful.", Toast.LENGTH_SHORT).show();
        }
    }
// Below is code to navigate to back in application.
    public void GoBackFunction(View view)
    {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent GoBack = new Intent(TaskEditPage.this, TaskHomePage.class);
        GoBack.putExtra("ListName", GV_ListName);
        this.finish();
        startActivity(GoBack);
    }
}
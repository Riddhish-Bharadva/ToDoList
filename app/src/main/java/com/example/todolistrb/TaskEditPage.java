package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskEditPage extends AppCompatActivity {

    String GV_ListName;
    String GV_TaskName;
    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit_page);

        SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListName");
        String TaskName = bundle.getString("TaskName");
        this.myDB = DB;
        this.GV_ListName = ListName;
        this.GV_TaskName = TaskName;
        Cursor db_cursor = DB.rawQuery("Select * From TaskTable where ListName = '" + ListName + "' and TaskName = '" + TaskName + "'", null);
//        String DueDate = db_cursor.getString(db_cursor.getColumnIndex("DueDate"));
//        Toast.makeText(this,""+DueDate,Toast.LENGTH_LONG).show();
        TextView ListTitleTextBox = findViewById(R.id.EListTitleTextBox);
        EditText EditTaskTextBox = findViewById(R.id.EditTaskTextBox);
        final EditText ETaskDateTextBox = findViewById(R.id.ETaskDateTextBox);
        ListTitleTextBox.setText(ListName);
        EditTaskTextBox.setText(TaskName);
//        ETaskDateTextBox.setText(new SimpleDateFormat("dd-MM-yyyy",DueDate));
// Calander code starts.
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
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
// Calander code ends.
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

// Below is code to navigate to Task home page.
    public void GoBackFunction(View view)
    {
        Intent GoBack = new Intent(TaskEditPage.this, TaskHomePage.class);
        GoBack.putExtra("ListName", GV_ListName);
        startActivity(GoBack);
    }
}
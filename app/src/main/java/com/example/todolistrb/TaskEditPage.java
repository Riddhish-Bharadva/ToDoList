package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TaskEditPage extends AppCompatActivity {

    String GV_ListName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit_page);

        SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListName");
        this.GV_ListName = ListName;
        String TaskName = bundle.getString("TaskName");
        TextView ListTitleTextBox = findViewById(R.id.EListTitleTextBox);
        ListTitleTextBox.setText(ListName);
        EditText EditTaskTextBox = findViewById(R.id.EditTaskTextBox);
        EditTaskTextBox.setText(TaskName);
    }

    // Below is code to navigate to Task home page.
    public void GoBackFunction(View view)
    {
        Intent GoBack = new Intent(TaskEditPage.this, TaskHomePage.class);
        GoBack.putExtra("ListName", GV_ListName);
        startActivity(GoBack);
    }
}
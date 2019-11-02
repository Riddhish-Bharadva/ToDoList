package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TaskHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_home_page);

        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("Pos");
        TextView ListTitleHeading = findViewById(R.id.ListTitleHeading);
        ListTitleHeading.setText(ListName);
    }
}
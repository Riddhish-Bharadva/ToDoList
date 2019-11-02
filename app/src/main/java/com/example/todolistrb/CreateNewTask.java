package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        ListTitleTextBox.setText(ListName);
    }

    public void OnClickResetButton(View view)
    {
        TextView NewTaskTextBox = findViewById(R.id.NewTaskTextBox);
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
}
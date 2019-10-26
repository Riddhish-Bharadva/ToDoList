package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNewToDoList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_to_do_list);
    }

    public void ResetFunction(View view)
    {
        EditText listTitle = findViewById(R.id.ListTitle);
        listTitle.setText(null);
        if(listTitle.length() == 0)
        {
            Toast.makeText(CreateNewToDoList.this, "Reset successful.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(CreateNewToDoList.this, "Error occurred while resetting values. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void GoBackFunction(View view)
    {
        Intent GB = new Intent(CreateNewToDoList.this, MainActivity.class);
        startActivity(GB);
    }
}
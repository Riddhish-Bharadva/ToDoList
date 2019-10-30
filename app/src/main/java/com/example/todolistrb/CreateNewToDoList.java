package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNewToDoList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_to_do_list);
    }

    public void CreateListFunction()
    {
        EditText NewListValue = findViewById(R.id.ListTitle);
        if(NewListValue.length() != 0)
        {
            String CurrentDate = new SimpleDateFormat("DD-MM-YYYY", Locale.getDefault()).format(new Date());
            String CurrentTime = new SimpleDateFormat("HH:MM:SS", Locale.getDefault()).format(new Date());
            Toast.makeText(CreateNewToDoList.this, "List name is: " + NewListValue + " Date is: " + CurrentDate + " Time is: " + CurrentTime, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(CreateNewToDoList.this, "List name cannot be blank.", Toast.LENGTH_SHORT).show();
        }
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
        Intent GoBack = new Intent(CreateNewToDoList.this, MainActivity.class);
        startActivity(GoBack);
    }
}
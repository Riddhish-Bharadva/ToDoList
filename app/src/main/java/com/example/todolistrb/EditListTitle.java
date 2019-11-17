package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditListTitle extends AppCompatActivity {

    SQLiteDatabase myDB;
    String GV_ListTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list_title);

        SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
        this.myDB = DB;
        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListTitle");
        this.GV_ListTitle = ListName;
        EditText EListTitle = findViewById(R.id.EListTitle);
        EListTitle.setText(ListName);
    }

// Below code is for update function.
    public void UpdateListFunction(View view)
    {
        EditText EListTitle = findViewById(R.id.EListTitle);
        Intent updatedRefresh = new Intent(EditListTitle.this, MainActivity.class);
        Cursor db_ct = myDB.rawQuery("Select * from ToDoListTable where ListName = '" + GV_ListTitle + "' and TaskCompleted = '1'", null);

        if(EListTitle.getText().toString() != null)
        {
            String UR = "Update ToDoListTable set ListName = '" + EListTitle.getText().toString() + "' where ListName = '" + GV_ListTitle + "'";
            myDB.execSQL(UR);
            String UR1 = "Update TaskTable set ListName = '" + EListTitle.getText().toString() + "' where ListName = '" + GV_ListTitle + "'";
            myDB.execSQL(UR1);
            startActivity(updatedRefresh);
        }
        else if(EListTitle.getText().toString() == null)
        {
            Toast.makeText(EditListTitle.this, "List title cannot be edited. Please try again.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(EditListTitle.this, "List title cannot be edited. Please try again.",Toast.LENGTH_SHORT).show();
        }
    }

// Below code is for reset function.
    public void ResetButton(View view)
    {
        EditText EListTitle = findViewById(R.id.EListTitle);
        if(EListTitle.getText().toString() != this.GV_ListTitle)
        {
            EListTitle.setText(this.GV_ListTitle);
            Toast.makeText(EditListTitle.this, "Reset successful.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(EditListTitle.this, "List title is already default.", Toast.LENGTH_SHORT).show();
        }
    }
// Below is code to navigate to List home page.
    public void GoBackFunction(View view)
    {
        Intent GoBack = new Intent(EditListTitle.this, MainActivity.class);
        startActivity(GoBack);
    }
}
package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    public void UpdateListFunction(View view) {
        Intent updatedRefresh = new Intent(EditListTitle.this, MainActivity.class);
        EditText UpdatedListTitle = findViewById(R.id.EListTitle);
        Cursor cursor = myDB.rawQuery("Select * from ToDoListTable where ListName = '" + UpdatedListTitle.getText().toString() + "'", null);
        if(cursor.getCount() == 0 && UpdatedListTitle.getText().length() != 0) {
            String UpdatedValue;
            UpdatedValue = UpdatedListTitle.getText().toString();
            SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
            String UR = "Update ToDoListTable set ListName = '" + UpdatedValue + "' where ListName = '" + GV_ListTitle + "'";
            myDB.execSQL(UR);
            String UR1 = "Update TaskTable set ListName = '" + UpdatedValue + "' where ListName = '" + GV_ListTitle + "'";
            myDB.execSQL(UR1);
            Cursor check_cursor = myDB.rawQuery("Select * from ToDoListTable where ListName = '" + UpdatedValue + "'", null);
            if(check_cursor.getCount() != 0)
            {
                startActivity(updatedRefresh);
                Toast.makeText(EditListTitle.this,"List title is updated successfully.",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(EditListTitle.this, "List title cannot be edited. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(EditListTitle.this,"List title already exist. Please try again with different title.",Toast.LENGTH_SHORT).show();
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
// Below is code to navigate back in application.
    public void GoBackFunction(View view)
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
        CreateDBT CDBT;
        CDBT.onCreate(DB);
    }
    public void CreateNewList (View view)
    {
        Intent CNL = new Intent(MainActivity.this, CreateNewToDoList.class);
        startActivity(CNL);
    }
}
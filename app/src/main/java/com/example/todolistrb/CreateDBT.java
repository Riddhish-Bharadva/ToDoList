package com.example.todolistrb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class CreateDBT extends SQLiteOpenHelper
{
    public CreateDBT(Context context)
    {
        super(context, "ToDoList", null, 1);
    }
    public void onCreate(SQLiteDatabase DB)
    {
        DB.execSQL("CREATE Table IF NOT EXISTS ToDoListTable (ListName VARCHAR, CreationDate Date, CreationTime Time, Completed int)");
        DB.execSQL("CREATE Table IF NOT EXISTS TaskTable (ListName VARCHAR, TaskName VARCHAR, DueDate Date, TaskCompleted int)");
    }
    public void onUpgrade(SQLiteDatabase DB, int i, int j)
    {
        DB.execSQL("DROP Table ToDoListTable");
        DB.execSQL("CREATE Table IF NOT EXISTS ToDoListTable (ListName VARCHAR, CreationDate Date, CreationTime Time, Completed int)");
        DB.execSQL("DROP Table TaskTable");
        DB.execSQL("CREATE Table IF NOT EXISTS TaskTable (ListName VARCHAR, TaskName VARCHAR, DueDate Date, TaskCompleted int)");
    }
}
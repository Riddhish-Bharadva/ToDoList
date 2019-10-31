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
        DB.execSQL("CREATE Table IF NOT EXISTS ToDoListTable (UniqueId VARCHAR, ListName VARCHAR, CreationDate Date, CreationTime Time, Completed int)");
    }
    public void onUpgrade(SQLiteDatabase DB, int i, int j)
    {
        DB.execSQL("DROP Table List");
        DB.execSQL("CREATE Table IF NOT EXISTS List (Details VARCHAR, CreationDate Date, Completed Boolean)");
    }
}
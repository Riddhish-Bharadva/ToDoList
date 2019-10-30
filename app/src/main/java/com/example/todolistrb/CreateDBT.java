package com.example.todolistrb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class CreateDBT extends SQLiteOpenHelper
{
    public CreateDBT(Context context)
    {
        super(context, "ToDoList", null, 1);
    }
    public void onCreate(SQLiteDatabase DB)
    {
        DB.execSQL("CREATE Table IF NOT EXISTS List (Details VARCHAR, CreationDate Date, Completed Boolean)");
        Log.i("Table:", "Table Created.");
    }
    public void onUpgrade(SQLiteDatabase DB, int i, int j)
    {
        DB.execSQL("DROP Table List");
        DB.execSQL("CREATE Table IF NOT EXISTS List (Details VARCHAR, CreationDate Date, Completed Boolean)");
    }
}
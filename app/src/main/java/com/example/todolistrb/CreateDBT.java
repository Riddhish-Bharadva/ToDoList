package com.example.todolistrb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class CreateDBT extends SQLiteOpenHelper
{
    public CreateDBT(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }
    public void onCreate(SQLiteDatabase DB)
    {
        DB.execSQL("CREATE Table IF NOT EXISTS List (Details VARCHAR, Completed Boolean)");
    }
    public void onUpgrade(SQLiteDatabase DB, int i, int j)
    {
        DB.execSQL("DROP Table List");
        DB.execSQL("CREATE Table IF NOT EXISTS List (Details VARCHAR, Completed Boolean)");
    }
}
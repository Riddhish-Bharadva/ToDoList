package com.example.todolistrb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Calling a constructor of an activity (Java class) to establish connection with my Database and create a table if it does not exist.
        CreateDBT CDBT = new CreateDBT(getApplicationContext());
        CDBT.getWritableDatabase();

// Displaying data from database to my ListTitleView on Home Page.
        SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
        this.myDB = DB;
        ListView ListTitleView = findViewById(R.id.TaskListView);
        final ArrayList<String> ArrayListTitle = new ArrayList<String>();
        ArrayList<String> CT = new ArrayList<String>();
        ArrayList<String> TT = new ArrayList<String>();
        MyAdapter MA;
        ArrayAdapter<String> ArrayListTitleAdapter;

// Checking if there are any entries in database.
        Cursor db_cursor_all_values = DB.rawQuery("Select * From ToDoListTable", null);
// In below if condition, values where there are few tasks pending for completion are checked.
        if(db_cursor_all_values.getCount() != 0)
        {
// In below select statement, COLLATE NOCASE is key word to ignore case of data and sort them in ascending / descending order.
            Cursor db_cursor = DB.rawQuery("Select * From ToDoListTable where Completed = '0' order by ListName COLLATE NOCASE ASC", null);
            Cursor db_cursor_completed = DB.rawQuery("Select * From ToDoListTable where Completed = '1' order by ListName COLLATE NOCASE ASC", null);
// In below select statement, we are selecting those values where completed status is 0.
            if(db_cursor.getCount() != 0)
            {
                if(db_cursor.moveToFirst())
                {
                    do{
// Below is code to add List names in array from db.
                        String ListName = db_cursor.getString(db_cursor.getColumnIndex("ListName"));
                        Cursor db_ct = DB.rawQuery("Select * from Tasktable where ListName = '" + ListName + "' and TaskCompleted = '1'", null);
                        Cursor db_tt = DB.rawQuery("Select * from Tasktable where ListName = '" + ListName + "'", null);
                        ArrayListTitle.add(ListName);
// Below is code for getting count of completed task for all lists from db.
                        if(db_ct.getCount() != 0)
                        {
                            String ct1 = "" + db_ct.getCount();
                            CT.add(ct1);
                        }
                        else
                        {
                            String ct1 = "0";
                            CT.add(ct1);
                        }
// Below is code for getting count of total task for all lists from db.
                        if(db_tt.getCount() != 0)
                        {
                            String tt1 = "" + db_tt.getCount();
                            TT.add(tt1);
                        }
                        else
                        {
                            String tt1 = "0";
                            TT.add(tt1);
                        }
                        if(db_tt.getCount() == db_ct.getCount() && db_ct.getCount() != 0)
                        {
                            String UR = "Update ToDoListTable set Completed = 1 where ListName = '" + ListName + "'";
                            DB.execSQL(UR);
                        }
                        else
                        {
                            String UR = "Update ToDoListTable set Completed = 0 where ListName = '" + ListName + "'";
                            DB.execSQL(UR);
                        }
                    }while (db_cursor.moveToNext());
                    MA = new MyAdapter(this, ArrayListTitle, CT, TT);
                    ListTitleView.setAdapter(MA);
// We are sending ListName to TaskHomePage using below code.
                    ListTitleView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String pos = ArrayListTitle.get(position);
                            Intent intent = new Intent(MainActivity.this, TaskHomePage.class);
                            intent.putExtra("ListName", pos);
                            startActivity(intent);
                        }
                    });
                }
                db_cursor.close();
            }
// In below if condition, values with all tasks completed are checked.
            if(db_cursor_completed.getCount() != 0)
            {
                if(db_cursor_completed.moveToFirst())
                {
                    do{
// Below is code to add List names in array from db.
                        String ListName1 = db_cursor_completed.getString(db_cursor_completed.getColumnIndex("ListName"));
                        Cursor db_tt = DB.rawQuery("Select * from Tasktable where ListName = '" + ListName1 + "'", null);
                        Cursor db_ct = DB.rawQuery("Select * from Tasktable where ListName = '" + ListName1 + "' and TaskCompleted = '1'", null);
                        ArrayListTitle.add(ListName1);
// Below is code for getting count of completed task for all lists from db.
                        if(db_ct.getCount() != 0)
                        {
                            String ct1 = "" + db_ct.getCount();
                            CT.add(ct1);
                        }
                        else
                        {
                            String ct1 = "0";
                            CT.add(ct1);
                        }
// Below is code for getting count of total task for all lists from db.
                        if(db_tt.getCount() != 0)
                        {
                            String tt1 = "" + db_tt.getCount();
                            TT.add(tt1);
                        }
                        else
                        {
                            String tt1 = "0";
                            TT.add(tt1);
                        }
                        if(db_tt.getCount() == db_ct.getCount() && db_ct.getCount() != 0)
                        {
                            String UR = "Update ToDoListTable set Completed = 1 where ListName = '" + ListName1 + "'";
                            DB.execSQL(UR);
                        }
                        else
                        {
                            String UR = "Update ToDoListTable set Completed = 0 where ListName = '" + ListName1 + "'";
                            DB.execSQL(UR);
                        }
                    }while (db_cursor_completed.moveToNext());
                    MA = new MyAdapter(this, ArrayListTitle, CT, TT);
                    ListTitleView.setAdapter(MA);
                }
                db_cursor_completed.close();
            }
        }
// Below message will be printed if there are no lists created by users.
        else
        {
            ArrayListTitle.add("No list created yet to display here.");
            ArrayListTitleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayListTitle);
            ListTitleView.setAdapter(ArrayListTitleAdapter);
        }
    }

    // Below is code for button on clicking on home to navigate to new list creation page.
    public void CreateNewList (View view)
    {
        Intent CreateNewList = new Intent(MainActivity.this, CreateNewToDoList.class);
        startActivity(CreateNewList);
    }

//Custom view adapter code.
    class MyAdapter extends BaseAdapter
    {
        Context mc;
        ArrayList<String> mtitle;
        ArrayList<String> mct;
        ArrayList<String> mtt;
        public class ViewHolder
        {
            public TextView ListTitleTextBox;
            public TextView TotalTaskTextBox;
            public TextView CompletedTaskTextBox;
            public Button EditTitleButton;
            public Button DeleteTitleButton;
            public LinearLayout HomePageContainer;
        }
        MyAdapter(Context c, ArrayList<String> title, ArrayList<String> ct, ArrayList<String> tt)
        {
            this.mc = c;
            this.mtitle = title;
            this.mct = ct;
            this.mtt = tt;
        }

        @Override
        public int getCount() {
            return mtitle.size();
        }

        @Override
        public Object getItem(int position) {
            return mtitle.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            ViewHolder Holder;
            if(convertView == null)
            {
                Holder = new ViewHolder();
                LayoutInflater Inflater = (LayoutInflater) mc.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = Inflater.inflate(R.layout.activity_home_page_custom_view, parent, false);
                Holder.ListTitleTextBox = convertView.findViewById(R.id.ListTitleTextBox);
                Holder.CompletedTaskTextBox = convertView.findViewById(R.id.CompletedTaskTextBox);
                Holder.TotalTaskTextBox = convertView.findViewById(R.id.TotalTaskTextBox);
                Holder.EditTitleButton = convertView.findViewById(R.id.ListEditButton);
                Holder.DeleteTitleButton = convertView.findViewById(R.id.ListDeleteButton);
                Holder.HomePageContainer = convertView.findViewById(R.id.HomePageContainer);
                convertView.setTag(Holder);
            }
            else
            {
                Holder = (ViewHolder) convertView.getTag();
            }
            Holder.ListTitleTextBox.setText(mtitle.get(position));
            Holder.CompletedTaskTextBox.setText("Completed Tasks : " + mct.get(position));
            Holder.TotalTaskTextBox.setText("Total Tasks : " + mtt.get(position));
            Holder.HomePageContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent EditListTitle = new Intent(MainActivity.this, TaskHomePage.class);
                    EditListTitle.putExtra("ListName",mtitle.get(position));
                    startActivity(EditListTitle);
                }
            });
            Holder.EditTitleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent EditListTitle = new Intent(MainActivity.this, EditListTitle.class);
                    EditListTitle.putExtra("ListTitle",mtitle.get(position));
                    startActivity(EditListTitle);
                }
            });
            Holder.DeleteTitleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String myQuery1 = "DELETE from ToDoListTable where ListName = '" + mtitle.get(position) + "'";
                    myDB.execSQL(myQuery1);
                    String myQuery2 = "DELETE from TaskTable where ListName = '" + mtitle.get(position) + "'";
                    myDB.execSQL(myQuery2);
                    Cursor Data = myDB.rawQuery("Select * from ToDoListTable where ListName = '" + mtitle.get(position) + "'", null);
                    if(Data.getCount() == 0) {
                        Intent RefreshPage = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(RefreshPage);
                        Toast.makeText(MainActivity.this,"List deleted successfully.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return convertView;
        }
    }
}
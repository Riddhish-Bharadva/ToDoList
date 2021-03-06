package com.rab.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TaskHomePage extends AppCompatActivity {

    String GV_ListName;
    ArrayList GV_ArrayTaskTitle = new ArrayList<String>();
    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_home_page);

        SQLiteDatabase DB = this.openOrCreateDatabase("ToDoList", MODE_PRIVATE, null);
        myDB = DB;
        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListName");
        TextView ListTitleHeading = findViewById(R.id.ListTitleHeading);
        ListTitleHeading.setText(ListName);
        this.GV_ListName = ListName;

// Displaying data from database to my ListTitleView on Home Page.
        ListView TaskTitleView = findViewById(R.id.TaskListView);
        final ArrayList<String> ArrayTaskTitle = new ArrayList<String>();
        ArrayList<String> ArrayTaskDueDate = new ArrayList<String>();
        ArrayList<Integer> ArrayTaskComplete = new ArrayList<Integer>();
        ArrayAdapter<String> ArrayTaskTitleAdapter;
        TaskMyAdapter TaskMA;
        TaskMA = new TaskMyAdapter(this, ArrayTaskTitle, ArrayTaskDueDate, ArrayTaskComplete);
// In below select statement, COLLATE NOCASE is key word to ignore case of data and sort them in ascending / descending order.
        Cursor db_cursor_all_task = DB.rawQuery("Select * From TaskTable where ListName = '" + ListName + "' order by TaskName COLLATE NOCASE ASC", null);
        String TaskName;
        String TaskExpiryDate;
        int TaskCompleted;
        if(db_cursor_all_task.getCount() != 0)
        {
            Cursor db_cursor = DB.rawQuery("Select * From TaskTable where ListName = '" + ListName + "' and TaskCompleted = '0' order by TaskName COLLATE NOCASE ASC", null);
            Cursor db_cursor_completed = DB.rawQuery("Select * From TaskTable where ListName = '" + ListName + "' and TaskCompleted = '1' order by TaskName COLLATE NOCASE ASC", null);
            if(db_cursor.getCount() != 0) {
                if(db_cursor.moveToFirst())
                {
                    do {
                        TaskName = db_cursor.getString(db_cursor.getColumnIndex("TaskName"));
                        TaskExpiryDate = db_cursor.getString(db_cursor.getColumnIndex("DueDate"));
                        TaskCompleted = db_cursor.getInt(db_cursor.getColumnIndex("TaskCompleted"));
                        ArrayTaskTitle.add(TaskName);
                        ArrayTaskDueDate.add(TaskExpiryDate);
                        ArrayTaskComplete.add(TaskCompleted);
                    } while (db_cursor.moveToNext());
                }
            }
            if(db_cursor_completed.getCount() != 0)
            {
                if (db_cursor_completed.moveToFirst())
                {
                    do {
                        TaskName = db_cursor_completed.getString(db_cursor_completed.getColumnIndex("TaskName"));
                        TaskExpiryDate = db_cursor_completed.getString(db_cursor_completed.getColumnIndex("DueDate"));
                        TaskCompleted = db_cursor_completed.getInt(db_cursor_completed.getColumnIndex("TaskCompleted"));
                        ArrayTaskTitle.add(TaskName);
                        ArrayTaskDueDate.add(TaskExpiryDate);
                        ArrayTaskComplete.add(TaskCompleted);
                    } while (db_cursor_completed.moveToNext());
                }
            }
            db_cursor.close();
            db_cursor_completed.close();
            this.GV_ArrayTaskTitle = ArrayTaskTitle;
// We are assigning all our array data in global arrays so that we can access them from any other methods.
            ArrayTaskTitleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayTaskTitle);
            TaskTitleView.setAdapter(ArrayTaskTitleAdapter);
            TaskTitleView.setAdapter(TaskMA);
        }
// If there are no entries in DB, it will display below message to user.
        else
        {
            ArrayTaskTitle.add("Create new task to display here");
            ArrayTaskTitleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayTaskTitle);
            TaskTitleView.setAdapter(ArrayTaskTitleAdapter);
        }
        db_cursor_all_task.close();
    }

    public void CreateNewTask(View view) {
        Bundle bundle = getIntent().getExtras();
        String ListName = bundle.getString("ListName");
        Intent CreateNewTask = new Intent(TaskHomePage.this, com.rab.todolist.CreateNewTask.class);
        CreateNewTask.putExtra("ListName", ListName);
        startActivity(CreateNewTask);
    }
//Custom view adapter code.
    class TaskMyAdapter extends BaseAdapter
    {
        Context mc;
        ArrayList<String> mtasktitle;
        ArrayList<String> mtaskduedate;
        ArrayList<Integer> mtaskcompleted;
        public class ViewHolder
        {
            public TextView TaskTitleTextBox;
            public TextView TaskDueDateTextBox;
            public CheckBox TaskCompletedCheckBox;
            public Button EditButton;
            public Button DeleteButton;
            public CheckBox CompletedCheckBox;
        }
        TaskMyAdapter(Context c, ArrayList<String> tasktitle, ArrayList<String> duedate, ArrayList<Integer> taskcompleted)
        {
            this.mc = c;
            this.mtasktitle = tasktitle;
            this.mtaskduedate = duedate;
            this.mtaskcompleted = taskcompleted;
        }
        @Override
        public int getCount() {
            return mtasktitle.size();
        }

        @Override
        public Object getItem(int position) {
            return mtasktitle.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            TaskHomePage.TaskMyAdapter.ViewHolder Holder;
            if(convertView == null)
            {
                Holder = new TaskHomePage.TaskMyAdapter.ViewHolder();
                LayoutInflater Inflater = (LayoutInflater) mc.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = Inflater.inflate(R.layout.activity_task_home_page_custom_view, parent, false);
                Holder.TaskTitleTextBox = convertView.findViewById(R.id.ListTaskTextBox);
                Holder.TaskDueDateTextBox = convertView.findViewById(R.id.TaskDueDate);
                Holder.TaskCompletedCheckBox = convertView.findViewById(R.id.CompletedCheckBox);
                Holder.EditButton = convertView.findViewById(R.id.EditButton);
                Holder.DeleteButton = convertView.findViewById(R.id.DeleteButton);
                Holder.CompletedCheckBox = convertView.findViewById(R.id.CompletedCheckBox);
                convertView.setTag(Holder);
            }
            else
            {
                Holder = (TaskHomePage.TaskMyAdapter.ViewHolder) convertView.getTag();
            }
            Holder.TaskTitleTextBox.setText(mtasktitle.get(position));
            Holder.TaskTitleTextBox.setTextColor(Color.parseColor("#000000"));
            Holder.TaskDueDateTextBox.setText("Due on : " + mtaskduedate.get(position));
            Holder.TaskDueDateTextBox.setBackgroundColor(Color.parseColor("#f2f2f2"));
            Holder.TaskDueDateTextBox.setTextColor(Color.parseColor("#000000"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date d = new Date();
            String Date1 = sdf.format(d.getTime());
            Date DateToday = null;
            try {
                DateToday = sdf.parse(Date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date DueDate = null;
            try {
                DueDate = sdf.parse(mtaskduedate.get(position));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (DueDate.before(DateToday) && mtaskcompleted.get(position) == 0) // If due date is in past and if task is incomplete.
            {
                Holder.TaskDueDateTextBox.setBackgroundColor(Color.parseColor("#800000"));
                Holder.TaskDueDateTextBox.setTextColor(Color.parseColor("#ffffff"));
            }
            else if (DueDate.compareTo(DateToday) == 0 && mtaskcompleted.get(position) == 0) // If due date is today and if task is incomplete.
            {
                Holder.TaskDueDateTextBox.setBackgroundColor(Color.parseColor("#FFD700"));
                Holder.TaskDueDateTextBox.setTextColor(Color.parseColor("#000000"));
            }
            if(mtaskcompleted.get(position) == 0)
            {
                Holder.TaskCompletedCheckBox.setChecked(false);
//                Holder.TaskCompletedCheckBox.setEnabled(true);
            }
            else if(mtaskcompleted.get(position) == 1)
            {
                Holder.TaskCompletedCheckBox.setChecked(true);
//                Holder.TaskCompletedCheckBox.setEnabled(false);
            }
            Holder.EditButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(TaskHomePage.this, TaskEditPage.class);
                    intent.putExtra("ListName", GV_ListName);
                    intent.putExtra("TaskName", mtasktitle.get(position));
                    startActivity(intent);
                }
            });
            Holder.DeleteButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String myQuery = "DELETE from TaskTable where ListName = '" + GV_ListName + "' and TaskName = '" + mtasktitle.get(position) + "'";
                    myDB.execSQL(myQuery);
                    Cursor Data = myDB.rawQuery("Select * from TaskTable where ListName = '" + GV_ListName + "' and TaskName = '" + mtasktitle.get(position) + "'",null);
                    if(Data.getCount() == 0)
                    {
                        Data.close();
                        Toast.makeText(TaskHomePage.this, "Task is successfully deleted.",Toast.LENGTH_SHORT).show();
// Below code will change status of list in main page.
                        Cursor db_ct = myDB.rawQuery("Select * from TaskTable where ListName = '" + GV_ListName + "' and TaskCompleted = '1'", null);
                        Cursor db_tt = myDB.rawQuery("Select * from Tasktable where ListName = '" + GV_ListName + "'", null);
                        if(db_tt.getCount() == db_ct.getCount() && db_tt.getCount() != 0)
                        {
                            String UpdateRecord = "Update ToDoListTable set Completed = 1 where ListName = '" + GV_ListName + "'";
                            myDB.execSQL(UpdateRecord);
                        }
                        else
                        {
                            String UpdateRecord = "Update ToDoListTable set Completed = 0 where ListName = '" + GV_ListName + "'";
                            myDB.execSQL(UpdateRecord);
                        }
// Resetting page.
                        Intent resetPage = new Intent(TaskHomePage.this, TaskHomePage.class);
                        resetPage.putExtra("ListName",GV_ListName);
                        finish();
                        startActivity(resetPage);
                    }
                    else
                    {
                        Toast.makeText(TaskHomePage.this, "Failed to delete data. Please try again after sometime.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Holder.CompletedCheckBox.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(mtaskcompleted.get(position) == 0) {
                        String UR = "Update TaskTable set TaskCompleted = 1 where ListName = '" + GV_ListName + "' and TaskName = '" + mtasktitle.get(position) + "'";
                        myDB.execSQL(UR);
                        Toast.makeText(TaskHomePage.this, "Task is successfully completed.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String UR = "Update TaskTable set TaskCompleted = 0 where ListName = '" + GV_ListName + "' and TaskName = '" + mtasktitle.get(position) + "'";
                        myDB.execSQL(UR);
                        Toast.makeText(TaskHomePage.this, "Task marked as incomplete.", Toast.LENGTH_SHORT).show();
                    }
                    Intent resetPage = new Intent(TaskHomePage.this, TaskHomePage.class);
                    resetPage.putExtra("ListName",GV_ListName);
                    Cursor c = myDB.rawQuery("Select * from TaskTable where ListName = '" + GV_ListName + "' and TaskName = '" + mtasktitle.get(position) + "' and TaskCompleted = '0'", null);
                    if(c.getCount() != 0)
                    {
                        finish();
                        startActivity(resetPage);
                    }
                    else if(c.getCount() == 0)
                    {
                        Cursor db_ct = myDB.rawQuery("Select * from TaskTable where ListName = '" + GV_ListName + "' and TaskCompleted = '1'", null);
                        Cursor db_tt = myDB.rawQuery("Select * from TaskTable where ListName = '" + GV_ListName + "'", null);
                        if(db_tt.getCount() == db_ct.getCount() && db_tt.getCount() != 0)
                        {
                            String UpdateRecord = "Update ToDoListTable set Completed = 1 where ListName = '" + GV_ListName + "'";
                            myDB.execSQL(UpdateRecord);
                        }
                        finish();
                        startActivity(resetPage);
                    }
                }
            });
            return convertView;
        }
    }

// Below code executes when user goes back in application.
    @Override
    protected void onRestart()
    {
        super.onRestart();
        this.finish();
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
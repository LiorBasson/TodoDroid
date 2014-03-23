package com.lb.todosqlite.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.lb.todosqlite.model.Tag;
import com.lb.todosqlite.model.Todo;

public class DatabaseHelper extends SQLiteOpenHelper 
{

	// Logcat tag
    private static final String LOG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "todosDB";
 
    // Table Names
    private static final String TABLE_TODO = "todos";
    private static final String TABLE_TAG = "tags";
    private static final String TABLE_TODO_TAG = "todo_tags";
    private static final String[] ALL_TABLES = new String[]{TABLE_TODO, TABLE_TAG, TABLE_TODO_TAG};
 
    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATION_DATE = "created_at"; 
 
    // TODOS Table - column names
    private static final String KEY_TODO = "todo";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DUE_DATE = "due_date";
    private static final String KEY_NOTIFICATION = "notification";
    
 
    // TAGS Table - column names
    private static final String KEY_TAG_NAME = "tag_name";
 
    // TODO_TAGS Table - column names
    private static final String KEY_TODO_ID = "todo_id";
    private static final String KEY_TAG_ID = "tag_id";
 
    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TODO
            + " TEXT," + KEY_STATUS + " INTEGER," + KEY_DUE_DATE
            + " DATETIME," + KEY_NOTIFICATION + " TEXT," + KEY_CREATION_DATE + " DATETIME" + ")";
 
    // Tag table create statement
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT,"
            + KEY_CREATION_DATE + " DATETIME" + ")";
 
    // todo_tag table create statement
    private static final String CREATE_TABLE_TODO_TAG = "CREATE TABLE "
            + TABLE_TODO_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TODO_ID + " INTEGER," + KEY_TAG_ID + " INTEGER,"
            + KEY_CREATION_DATE + " DATETIME" + ")";
 
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
        // creating required tables
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_TODO_TAG);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_TAG);
 
        // create new tables
        onCreate(db);
    }
    
    
    // Creating a todo
    public long createToDo(Todo todo, long[] tag_ids) 
    {
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_TODO, todo.getNote());
    	values.put(KEY_STATUS, todo.getStatus());
    	values.put(KEY_DUE_DATE, todo.getDueDate());
    	values.put(KEY_NOTIFICATION, todo.getNotification());    	
    	values.put(KEY_CREATION_DATE, getDateTime());
    
    	// insert row
    	long todo_id = db.insert(TABLE_TODO, null, values);
   
    	// assigning tags to todo
    	for (long tag_id : tag_ids) 
    	{
    		createTodoTag(todo_id, tag_id);
    	}
    	return todo_id;
    }
    
    // get single todo
    public Todo getTodo(long todo_id) 
    {
       SQLiteDatabase db = this.getReadableDatabase();
    
       String selectQuery = "SELECT  * FROM " + TABLE_TODO + " WHERE "
               + KEY_ID + " = " + todo_id;
    
       Log.e(LOG, selectQuery);
    
       Cursor c = db.rawQuery(selectQuery, null);
    
       if (c != null)
           c.moveToFirst();
    
       Todo td = new Todo();
       td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
       td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
       td.setDueDate(c.getString(c.getColumnIndex(KEY_DUE_DATE)));
       td.setNotification(c.getString(c.getColumnIndex(KEY_NOTIFICATION)));
       td.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));
    
       return td;
   }
    
    // getting all todos
    public List<Todo> getAllToDos() 
    {
       List<Todo> todos = new ArrayList<Todo>();
       String selectQuery = "SELECT  * FROM " + TABLE_TODO;
    
       Log.e(LOG, selectQuery);
    
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor c = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (c.moveToFirst()) {
           do {
               Todo td = new Todo();
               td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
               td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
               td.setDueDate(c.getString(c.getColumnIndex(KEY_DUE_DATE)));
               td.setStatus(c.getInt((c.getColumnIndex(KEY_STATUS))));
               td.setNotification(c.getString(c.getColumnIndex(KEY_NOTIFICATION)));               
    
               // adding to todo list
               todos.add(td);
           } while (c.moveToNext());
       }
    
       return todos;
    }
    
    // getting all todos under single tag    
    public List<Todo> getAllToDosByTag(String tag_name) 
    {
       List<Todo> todos = new ArrayList<Todo>();
    
       String selectQuery = "SELECT  * FROM " + TABLE_TODO + " td, "
               + TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
               + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
               + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
               + "tt." + KEY_TODO_ID;
    
       Log.e(LOG, selectQuery);
    
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor c = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (c.moveToFirst()) {
           do {
               Todo td = new Todo();
               td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
               td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
               td.setDueDate(c.getString(c.getColumnIndex(KEY_DUE_DATE)));
               td.setStatus(c.getInt((c.getColumnIndex(KEY_STATUS))));
               td.setNotification(c.getString(c.getColumnIndex(KEY_NOTIFICATION)));               
    
               // adding to todo list
               todos.add(td);
           } while (c.moveToNext());
       }
    
       return todos;
    }
    
    // getting todo count
    public int getToDoCount() {
       String countQuery = "SELECT  * FROM " + TABLE_TODO;
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery(countQuery, null);

       int count = cursor.getCount();
       cursor.close();

       // return count
       return count;
   }
    
    // getting tags count
    public int getTagsCount() {
       String selectQuery = "SELECT  * FROM " + TABLE_TAG;
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);

       int count = cursor.getCount();
       cursor.close();

       // return count
       return count;
   }
    
    // getting TodoTags count
    public int getToDoTagsCount() {
       String countQuery = "SELECT  * FROM " + TABLE_TODO_TAG;
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery(countQuery, null);

       int count = cursor.getCount();
       cursor.close();

       // return count
       return count;
   }
    
    // getting TodoTags count
    public List<Tag> getTagsByToDo(long todoID) {
    	List<Tag> tags = new ArrayList<Tag>();
    	
       String countQuery = "SELECT  * FROM " + TABLE_TODO_TAG  + " WHERE "
               + KEY_TODO_ID + " = " + todoID;
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor c = db.rawQuery(countQuery, null);       
       
       // looping through all rows and adding to list
       if (c.moveToFirst()) {
           do {
               Tag tag = new Tag();
               tag.setId(c.getInt((c.getColumnIndex(KEY_TAG_ID))));
               tags.add(tag);
           } while (c.moveToNext());
       }
       List<Tag> allTags =	getAllTags();
       
       for (Tag t : tags)
       {
    	   for (Tag tg : allTags)
    	   {
    		   if (t.getId() == tg.getId())
    			   {
    			   		t.setTagName(tg.getTagName());
    			   		break;
    			   }
    	   }
       }
       
       c.close();
       return tags;
   }
    
    
    // Updating a todo
    public int updateToDo(Todo todo) 
   {
       SQLiteDatabase db = this.getWritableDatabase();
    
       ContentValues values = new ContentValues();
       values.put(KEY_TODO, todo.getNote());
       values.put(KEY_STATUS, todo.getStatus());
       values.put(KEY_DUE_DATE, todo.getDueDate());
       values.put(KEY_NOTIFICATION, todo.getNotification());
    
       // updating row
       return db.update(TABLE_TODO, values, KEY_ID + " = ?",
               new String[] { String.valueOf(todo.getId()) });
   }
    
    // Deleting a todo
    public void deleteToDo(long todo_id) 
    {
    	SQLiteDatabase db = this.getWritableDatabase();
		while (db.inTransaction())
		{
			  try 
			  {
				  this.wait(100);
			  } 
			  catch (InterruptedException e) 
			  {	
				  Log.d("DataBaseHelper", "DeleteToDo() experienced an interrupt to a wait(100ms). breaking while (db.inTransaction()) loop. ", e);
				  break;
			  }
		}  
		db.delete(TABLE_TODO, KEY_ID + " = ?",
		      new String[] { String.valueOf(todo_id) });
		// TODO: Test and add it (delete TodoTags table relevant row) written on 06032014 
		db.delete(TABLE_TODO_TAG, KEY_TODO_ID + " = ?", new String[] { String.valueOf(todo_id) });
    }
    
    // Deleting a todo, if isDeleteAlsoAssociatedTags=true will delete them if not in use elsewhere
    public void deleteToDo(long todo_id, boolean isDeleteAlsoAssociatedTags) 
	  {
    	if (isDeleteAlsoAssociatedTags)
    	{
    		// gets all associated tags and verify it is not associated with additional todos before deleting it
    	    List<Tag> allTagsAssociated = getTagsByToDo(todo_id);
    	    for (Tag tag : allTagsAssociated)
    	    {
    	    	List<Todo> AllTodoAssociatedWithTagName = getAllToDosByTag(tag.getTagName());
    	    	if (!(AllTodoAssociatedWithTagName.size()>1))
    	    		deleteTag(tag, false);    	    		
    	    }    	    
    	}
    	
    	deleteToDo(todo_id); 
	  }
  
    // Creating tag
    public long createTag(Tag tag) 
  {
     SQLiteDatabase db = this.getWritableDatabase();
  
     ContentValues values = new ContentValues();
     values.put(KEY_TAG_NAME, tag.getTagName());
     values.put(KEY_CREATION_DATE, getDateTime());
  
     // insert row
     long tag_id = db.insert(TABLE_TAG, null, values);
  
     return tag_id;
 }
  
    // getting all tag
    public List<Tag> getAllTags() 
 {
     List<Tag> tags = new ArrayList<Tag>();
     String selectQuery = "SELECT  * FROM " + TABLE_TAG;
  
     Log.e(LOG, selectQuery);
  
     SQLiteDatabase db = this.getReadableDatabase();
     Cursor c = db.rawQuery(selectQuery, null);
  
     // looping through all rows and adding to list
     if (c.moveToFirst()) {
         do {
             Tag t = new Tag();
             t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
             t.setTagName(c.getString(c.getColumnIndex(KEY_TAG_NAME)));
  
             // adding to tags list
             tags.add(t);
         } while (c.moveToNext());
     }
     return tags;
 }
  
 	//Updating a tag
 	public int updateTag(Tag tag) 
 	{
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_TAG_NAME, tag.getTagName());
	 
	    // updating row
	    return db.update(TABLE_TAG, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(tag.getId()) });
 	}
 	
 	//Deleting a tag
 	public void deleteTag(Tag tag, boolean should_delete_all_tag_todos) {
 	    SQLiteDatabase db = this.getWritableDatabase();
 	 
 	    // before deleting tag
 	    // check if todos under this tag should also be deleted
 	    if (should_delete_all_tag_todos) {
 	        // get all todos under this tag
 	        List<Todo> allTagToDos = getAllToDosByTag(tag.getTagName());
 	 
 	        // delete all todos
 	        for (Todo todo : allTagToDos) {
 	            // delete todo
 	            deleteToDo(todo.getId());
 	        }
 	    }
 	 
 	    // now delete the tag
 	    db.delete(TABLE_TAG, KEY_ID + " = ?",
 	            new String[] { String.valueOf(tag.getId()) });
 	}
 	
 	// Creating todo_tag
 	public long createTodoTag(long todo_id, long tag_id) {
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values = new ContentValues();
       values.put(KEY_TODO_ID, todo_id);
       values.put(KEY_TAG_ID, tag_id);
       values.put(KEY_CREATION_DATE, getDateTime());

       long id = db.insert(TABLE_TODO_TAG, null, values);

       return id;
   }
 	
 	// Updating a todo tag
 	public int updateNoteTag(long todo_id, long tag_id) {
 	    SQLiteDatabase db = this.getWritableDatabase();
 	 
 	    ContentValues values = new ContentValues();
 	    values.put(KEY_TAG_ID, tag_id);
 	 
 	    // updating row
 	    return db.update(TABLE_TODO, values, KEY_ID + " = ?",
 	            new String[] { String.valueOf(todo_id) });
 	}
 	
 	// Clearing whole DB
 	public void clearDB()
 	{
 		SQLiteDatabase db = this.getReadableDatabase();
 		
 		try {
			for (String table : ALL_TABLES)
				db.delete(table, null, null);
		} catch (Exception e) {
			Log.d("DatabaseHelper","ClearDB() caught an exception when attempted executing db.delete(table, null, null). see exception: ", e);
		}
 	}
 	
 	// closing database
    public void closeDB() 
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
        Log.e(LOG, "closeDB() was called");
    }
    
    // get datetime  // TODO: change format?
    private String getDateTime() 
   {
       SimpleDateFormat dateFormat = new SimpleDateFormat(
    		   "yyyy-MM-dd HH:mm:ss", Locale.getDefault()); 
       Date date = new Date();
       return dateFormat.format(date);
   }
 	
}

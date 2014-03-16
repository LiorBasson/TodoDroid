package com.lb.todosqlite.model;

public class Todo 
{
	int id; // TODO: change to long. follow refactoring for all usages
    String note;
    int status;
    String dueDate;
 
    // constructors
    public Todo() 
    {
    }
 
    public Todo(String note, int status) 
    {
        this.note = note;
        this.status = status;
    }
 
    public Todo(int id, String note, int status) 
    {
        this.id = id;
        this.note = note;
        this.status = status;
    }
 
    // setters
    public void setId(int id) 
    {
        this.id = id;
    }
 
    public void setNote(String note) 
    {
        this.note = note;
    }
 
    public void setStatus(int status) 
    {
        this.status = status;
    }
     
    public void setDueDate(String dueDate)
    {
        this.dueDate = dueDate;
    }
 
    // getters
    public long getId() 
    {
        return this.id;
    }
 
    public String getNote() 
    {
        return this.note;
    }
 
    public int getStatus() 
    {
        return this.status;
    }
    
    public String getDueDate()
    {
    	
    	return this.dueDate;
    }
}

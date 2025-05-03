package com.disruption.sys.utils;

import java.sql.Date;

public class TableRow {
    private final double value;
    private final String pos;
    private final String id;
    private final Date date;

    public TableRow(String pos, double value, String id, Date date){
        this.value = value;
        this.pos = pos;
        this.id = id;
        this.date = date;
    }

    public double getValue(){
        return value;
    }
    public String getPos(){
        return pos;
    }
    public String getId(){
        return id;
    }
    public Date getDate(){
        return date;
    }
}

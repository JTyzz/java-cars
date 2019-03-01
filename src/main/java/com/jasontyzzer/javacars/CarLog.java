package com.jasontyzzer.javacars;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarLog implements Serializable {
    private String text;
    private String logDate;

    public CarLog(String text){
        this.text = text;

        Date date = new Date();
        String dateStr = "yyyy-MM-dd hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(dateStr);
        logDate = dateFormat.format(date);
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.apicloud.wifisyncserver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class MySimpleFormatter extends SimpleFormatter {
    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    private StringBuilder sb = new StringBuilder();

    public MySimpleFormatter() {
    }

    public synchronized String format(LogRecord record) {
        Level level = record.getLevel();
        String lstr = " I/app3c   (2)";
        if (level.toString().equals("SEVERE")) {
            lstr = " E/app3c   (1)";
        }

        if (level.toString().equals("INFO")) {
            lstr = " I/app3c   (2)";
        }

        if (level.toString().equals("WARNING")) {
            lstr = " W/app3c   (3)";
        }

        String time = this.sdf.format(new Date(record.getMillis()));
        String message = record.getMessage();
        this.sb.delete(0, this.sb.length());
        this.sb.append(" ").append(time);
        this.sb.append(lstr);
        this.sb.append(": ").append(message);
        System.out.println(this.sb.toString());
        this.sb.append("\n");
        return this.sb.toString();
    }
}

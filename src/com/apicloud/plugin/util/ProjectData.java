package com.apicloud.plugin.util;

/**
 * Created with IntelliJ IDEA.
 * Date: 21-6-1
 * Time: 下午5:21
 * Description:
 */
public class ProjectData {

    public  String type;

    public  String adbPath;

    public ProjectData(){

    }
    public ProjectData(String type){
        this.type = type;
    }
    public ProjectData(String type,String adbPath){
        this.type = type;
        this.adbPath = adbPath;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdbPath() {
        return adbPath;
    }

    public void setAdbPath(String adbPath) {
        this.adbPath = adbPath;
    }
}

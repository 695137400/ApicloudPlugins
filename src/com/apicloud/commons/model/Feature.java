//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.apicloud.commons.model;

import java.util.ArrayList;
import java.util.List;

public class Feature {
    private String name;
    private String desc;
    private boolean isAndroid;
    private boolean isIos;
    private String type;
    private List<Param> params = new ArrayList();

    public Feature() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Param> getParams() {
        return this.params;
    }

    public void addParams(Param param) {
        this.params.add(param);
    }

    public void removeParams(Param param) {
        this.params.remove(param);
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isAndroid() {
        return this.isAndroid;
    }

    public void setAndroid(boolean isAndroid) {
        this.isAndroid = isAndroid;
    }

    public boolean isIos() {
        return this.isIos;
    }

    public void setIos(boolean isIos) {
        this.isIos = isIos;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

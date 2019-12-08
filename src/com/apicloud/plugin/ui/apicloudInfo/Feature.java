package com.apicloud.plugin.ui.apicloudInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-6-18-0018<br/>
 * Time: 17:36:40<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9">扩展属性</span><br/>
 */
public class Feature {
    /**
     * 名称
     */
    String titleName = "";
    /**
     * 是否强制绑定
     */
    boolean forceBind = true;

  public   List<FeatureParam> param = new ArrayList<>();

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public boolean isForceBind() {
        return forceBind;
    }

    public void setForceBind(boolean forceBind) {
        this.forceBind = forceBind;
    }
}
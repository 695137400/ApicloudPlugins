package com.apicloud.plugin.ui.apicloudInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-6-18-0018<br/>
 * Time: 16:48:06<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9">apicloud 详细配置</span><br/>
 */
public class ApicloudInfo {
    /**
     * 应用ID
     */
    String appId = "";
    /**
     * Widget的版本号
     */
    String version = "";
    /**
     * Widget的名称
     */
    String appName = "";
    /**
     * Widget的简单描述信息
     */
    String appDescription = "";
    /**
     * Widget的作者信息
     */
   public Author author = new Author();
    /**
     * Widget运行的起始页，支持相对/绝对路径
     */
    String content = "";
    /**
     * 权限
     */
  public   Permission permission = new Permission();
    /**
     * 用于配置在哪些类型的页面里面可以访问APICloud的扩展API方法
     */
   public Access access = new Access();
    /**
     * 扩展模块功能、第三方SDK等接入规范
     */
   public List<Feature> feature = new ArrayList<>();
    /**
     * 全局设置或者属性
     */
    public Preference preference = new Preference();


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

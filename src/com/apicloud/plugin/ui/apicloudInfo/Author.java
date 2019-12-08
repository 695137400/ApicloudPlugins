package com.apicloud.plugin.ui.apicloudInfo;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-6-18-0018<br/>
 * Time: 16:55:09<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */
public class Author {
    /**
     * 邮箱
     */
    String email = "";
    /**
     * 授权链接
     */
    String href = "";
    /**
     * 作者名
     */
    String authorName = "";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}

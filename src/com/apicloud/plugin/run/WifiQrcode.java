package com.apicloud.plugin.run;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: lizhichao
 * Date: 2018/1/10 0010
 * Time: 10:49:44
 * Description:
 */
public class WifiQrcode extends AnAction implements DumbAware {

    static WifiQrcode instance = null;

    public void setIcon(Image icon) {
        ((ImageIcon)getTemplatePresentation().getIcon()).setImage(icon);
    }

    public static WifiQrcode getInstance() {
        return instance;
    }

    public WifiQrcode() {
        super(" ", "请用手机PP扫码连接", new ImageIcon(WifiQrcode.class.getResource("/com/apicloud/plugin/icons/apicloud.png")));
        instance = this;
    }


    @Override
    public void actionPerformed(AnActionEvent event) {
    }

}

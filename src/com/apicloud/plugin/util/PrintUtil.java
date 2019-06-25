package com.apicloud.plugin.util;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.ide.browsers.OpenUrlHyperlinkInfo;
import com.intellij.openapi.editor.markup.TextAttributes;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 打印简单工具类  XZZXZ
 *
 * @author ob
 */
public class PrintUtil {
    public static String msg = "";

    public static void info(String m, String name) {
        if (null != msg && !"".equals(msg)) {
            print(msg + "\n" + m, new Color(102, 207, 239), name);
            msg = "";
        } else {
            print(m, new Color(102, 207, 239), name);
        }
    }

    public static void error(String m, String name) {
        if (null != msg && !"".equals(msg)) {
            print(msg + "\n" + m, Color.red, name);
            msg = "";
        } else {
            print(m, Color.red, name);
        }
    }

    private static void print(String msg, Color color, String name) {
        ConsoleView console = RunProperties.console(name);
        console.print(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()) + "  " + msg + "\n", new ConsoleViewContentType("styleName", new TextAttributes(new Color(102, 207, 239), null, null, null, Font.PLAIN)));
    }

    public static void printUrl(String msg, String name, String url) {
        ConsoleView console = RunProperties.console(name);
        console.print(msg, new ConsoleViewContentType("styleName", new TextAttributes(new Color(102, 207, 239), null, null, null, Font.PLAIN)));
        console.printHyperlink(url + "\n", new OpenUrlHyperlinkInfo(url));
    }

    public static void printInfoNoDate(String msg, String name) {
        ConsoleView console = RunProperties.console(name);
        console.print(msg + "\n", new ConsoleViewContentType("styleName", new TextAttributes(new Color(102, 207, 239), null, null, null, Font.PLAIN)));
    }

}

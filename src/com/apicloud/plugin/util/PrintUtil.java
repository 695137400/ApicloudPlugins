package com.apicloud.plugin.util;

import com.apicloud.plugin.tail.TailContentExecutor;
import com.apicloud.plugin.tail.TailRunExecutor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 打印简单工具类  XZZXZ
 *
 * @author ob
 */
public class PrintUtil {
    public static ConsoleView console = null;
    public static String msg = "";

    public static void init(Project project, String title) {
        if (null == console) {
            showLogInConsole(project, TailRunExecutor.EXECUTOR_ID);
        }
    }

    private static void showLogInConsole(Project project,String title) {
        final TailContentExecutor executor = new TailContentExecutor(project);
        Disposer.register(project, executor);
        executor.run(title);
        ExecutorRegistry.getInstance().getRegisteredExecutors();
        if (null != msg && !"".equals(msg)) {
            info(msg);
            msg = "";
        }
    }

    public static void info(String m) {
        if (null != msg && !"".equals(msg)) {
            print(msg + "\n" + m, new Color(102, 207, 239));
            msg = "";
        } else {
            print(m, new Color(102, 207, 239));
        }
    }

    public static void error(String m) {
        if (null != msg && !"".equals(msg)) {
            print(msg + "\n" + m, Color.red);
            msg = "";
        } else {
            print(m, Color.red);
        }
    }

    private static void print(String msg, Color color) {
        console.print(new SimpleDateFormat("YYYY-MM-DD HH:mm:ss").format(new Date()) + "  " + msg + "\n", new ConsoleViewContentType("styleName", new TextAttributes(color, null, null, null, Font.PLAIN)));
    }
}

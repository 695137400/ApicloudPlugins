package com.apicloud.plugin;

import com.apicloud.plugin.run.WebStorm;
import com.apicloud.plugin.tail.TailRunExecutor;
import com.apicloud.plugin.util.PrintUtil;
import com.apicloud.plugin.util.RunProperties;
import com.intellij.ide.ui.OptionsTopHitProvider;
import com.intellij.ide.ui.search.OptionDescription;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-4-25-0025<br/>
 * Time: 10:31:31<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */
public class ApicloudOptionsTopHitProvider extends OptionsTopHitProvider {
    private int index = 0;

    @NotNull
    @Override
    public Collection<OptionDescription> getOptions(@Nullable Project project) {
        if (index > 0) {
            try {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Properties properties = System.getProperties();
                            String systemPath = properties.getProperty("idea.plugins.path");
                            File tempPath = new File(FileUtil.getTempDirectory().toString() + "/apicloud-intelliJ-plugin");
                            WebStorm webStorm = RunProperties.getWebStorm(project.getName());
                            if (tempPath.exists()) {
                                if (!webStorm.isMacOS() && !webStorm.isLinux()) {
                                    webStorm.runCmd(tempPath.getAbsolutePath() + "/tools/adb.exe  kill-server", false);
                                }
                                if (webStorm.isMacOS()) {
                                    try {
                                        webStorm.runCmd(tempPath.getAbsolutePath() + "/tools/adb-ios  kill-server", false);
                                    } catch (Exception e) {
                                        String chx = "chmod +x " + tempPath.getAbsolutePath() + "/tools/adb-ios";
                                        webStorm.runCmd(chx, false);
                                        try {
                                            webStorm.runCmd(tempPath.getAbsolutePath() + "/tools/adb-ios  kill-server", false);
                                        } catch (Exception e1) {
                                            PrintUtil.info("ADB被占用，没有权限停止", project.getName());
                                        }
                                    }
                                }
                                if (webStorm.isLinux()) {
                                    try {
                                        webStorm.runCmd(tempPath.getAbsolutePath() + "/tools/adb-linux  kill-server", false);
                                    } catch (Exception e) {
                                        String chx = "chmod +x " + tempPath.getAbsolutePath() + "/tools/adb-linux";
                                        webStorm.runCmd(chx, false);
                                        try {
                                            webStorm.runCmd(tempPath.getAbsolutePath() + "/tools/adb-linux  kill-server", false);
                                        } catch (Exception e1) {
                                            PrintUtil.info("ADB被占用，没有权限停止", project.getName());
                                        }
                                    }
                                }
                                Thread.sleep(100);
                                tempPath.delete();
                            } else {
                                tempPath = FileUtil.createTempDirectory("apicloud-intelliJ-plugin", null, true);
                            }
                            PrintUtil.info("查找Apicloud插件", project.getName());
                            PrintUtil.info("开始解压Apicloud插件", project.getName());
                            try {
                                com.apicloud.plugin.util.FileUtil.unZip(systemPath + "/ApicloudPlugins/lib/resources.jar", tempPath.getAbsolutePath() + "/", false);
                                Thread.sleep(100);
                                if (webStorm.isMacOS() || webStorm.isLinux()) {
                                    String chx = "chmod +x " + tempPath.getAbsolutePath() + "/tools/adb-ios";
                                    webStorm.runCmd(chx, false);
                                    chx = "chmod +x " + tempPath.getAbsolutePath() + "/tools/adb-linux";
                                    webStorm.runCmd(chx, false);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!new File(systemPath + "此文件标识apicloud插件是否安装完成，如果删除则重新安装.txt").exists()) {
                                PrintUtil.info("systemPath：" + systemPath, project.getName());
                                PrintUtil.info("解压完毕", project.getName());
                                PrintUtil.info("开始拷贝", project.getName());
                                com.apicloud.plugin.util.FileUtil.copyFolder(tempPath.getAbsolutePath() + "/templates/", systemPath + "/templates/");
                                new File(systemPath + "此文件标识apicloud插件是否安装完成，如果删除则重新安装.txt").createNewFile();
                                PrintUtil.info("拷贝完毕，请重启IDEA", project.getName());
                            } else {
                                PrintUtil.info("欢迎使用Apicloud插件", project.getName());
                            }
                            Thread.sleep(500);
                            System.out.println("所有初始化完毕");
                            System.out.println("项目路径：" + project.getBaseDir().getPath());
                            //ApicloudConfigEdit.configPath = project.getBaseDir().getPath();
                            //ApicloudConfigEdit.restoreData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } catch (Exception e) {
                PrintUtil.error(e.getMessage(), project.getName());
                e.printStackTrace();
            }
        }
        index++;
        System.out.println("apicloud 插件已加载");

        return new ArrayList<>();
    }

    @Override
    public String getId() {
        System.out.println("getId");
        return TailRunExecutor.TOOLWINDOWS_ID;
    }
}

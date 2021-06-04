package com.apicloud.plugin.ui.createApp;

import com.alibaba.fastjson.JSON;
import com.apicloud.plugin.run.WebStorm;
import com.apicloud.plugin.util.ProjectData;
import com.apicloud.plugin.util.RunProperties;
import com.intellij.ide.plugins.cl.PluginClassLoader;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: <br/>
 * Date: 2018-9-30-0030<br/>
 * Time: 15:24:09<br/>
 * Author:<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */
public class CreateAppFrom {


    public JPanel panel1;
    public JPanel kongPanel;
    public JPanel homePanel;
    public JPanel bottomPanel;
    public JPanel leftPanel;
    public JLabel kongLable;
    public JLabel homeLable;
    public JLabel bottomLable;
    public JLabel leftLable;
    public JLabel allLable;
    public TextFieldWithBrowseButton textField1;

    public ProjectData getProjectData() {
        return projectData;
    }

    ProjectData projectData = new ProjectData("default");

    private static CreateAppFrom instance;

    public static CreateAppFrom getInstance() {
        if (instance == null) {
            instance = new CreateAppFrom();
        }
        return instance;
    }

    private CreateAppFrom() {
        FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();
        textField1.addBrowseFolderListener(new TextBrowseFolderListener(fileChooserDescriptor) {
            protected void onFileChosen(@NotNull VirtualFile chosenFile) {
                String adbPath = chosenFile.getPath();
                if (null != adbPath && !"".equals(adbPath) && adbPath.length() > 0 && checkAdb(adbPath)) {
                    super.onFileChosen(chosenFile);
                    projectData.setAdbPath(adbPath);
                    System.out.println(JSON.toJSON(projectData));
                } else {
                    textField1.setText("");
                    JOptionPane.showMessageDialog(null, "您选择的adb文件不正确.", "提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        WebStorm storm = RunProperties.getWebStorm("create");
        String path = null;
        try {
            if (storm.isMacOS()) {
                path = RunProperties.pluginsPath + "/tools/adb-ios";
            } else if (storm.isLinux()) {
                path = RunProperties.pluginsPath + "/tools/adb-linux";
            } else {
                path = RunProperties.pluginsPath + "/tools/adb.exe";
            }
        } catch (Exception e) {

        }

        textField1.setText(path);

        kongPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                projectData.setType("default");
                System.out.println(JSON.toJSON(projectData));
                kongLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/4-1.png")));
                bottomLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/1.png")));
                homeLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/2.png")));
                leftLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/3.png")));
                allLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/4-1-1.png")));
            }
        });
        homePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                projectData.setType("home");
                System.out.println(JSON.toJSON(projectData));
                kongLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/4.png")));
                bottomLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/1.png")));
                homeLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/2-1.png")));
                leftLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/3.png")));
                allLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/2-1-1.png")));
            }
        });
        bottomPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                projectData.setType("bottom");
                System.out.println(JSON.toJSON(projectData));
                kongLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/4.png")));
                bottomLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/1-1.png")));
                homeLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/2.png")));
                leftLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/3.png")));
                allLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/1-1-1.png")));
            }
        });
        leftPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                projectData.setType("slide");
                System.out.println(JSON.toJSON(projectData));
                kongLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/4.png")));
                bottomLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/1.png")));
                homeLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/2.png")));
                leftLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/3-1.png")));
                allLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/3-1-1.png")));
            }
        });
    }

    private boolean checkAdb(String path) {
        WebStorm webStorm = new WebStorm("1");
        if (webStorm.isLinux() || webStorm.isMacOS()) {
            try {
                String out = (String) webStorm.runCmd(path + "  version", false);
                if (null != out && !"".equalsIgnoreCase(out) && out.length() > 0) {
                    if (out.toLowerCase().contains("installed as")) {
                        return true;
                    }
                }
            } catch (Exception e) {
                if (webStorm.isLinux() || webStorm.isMacOS()) {
                    String chx = "chmod +x " + path;
                    webStorm.runCmd(chx, false);
                    try {
                        String out = (String) webStorm.runCmd(path + "  version", false);
                        if (null != out && !"".equalsIgnoreCase(out) && out.length() > 0) {
                            if (out.toLowerCase().contains("installed as")) {
                                return true;
                            }
                        }
                    } catch (Exception e1) {

                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CreateAppFrom");
        frame.setContentPane(new CreateAppFrom().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setOpaque(true);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setEnabled(true);
        panel2.setVisible(true);
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setAutoscrolls(false);
        panel3.setOpaque(false);
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("选择ADB：");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textField1 = new TextFieldWithBrowseButton();
        panel4.add(textField1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(500, -1), null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        kongPanel = new JPanel();
        kongPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 10, 0, 10), -1, -1));
        panel7.add(kongPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        kongLable = new JLabel();
        kongLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/4-1.png")));
        kongLable.setText("");
        kongPanel.add(kongLable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("空白应用");
        kongPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        homePanel = new JPanel();
        homePanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 10, 0, 10), -1, -1));
        panel7.add(homePanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        homeLable = new JLabel();
        homeLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/2.png")));
        homeLable.setText("");
        homePanel.add(homeLable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("首页导航");
        homePanel.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 10, 0, 10), -1, -1));
        panel7.add(bottomPanel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        bottomLable = new JLabel();
        bottomLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/1.png")));
        bottomLable.setText("");
        bottomPanel.add(bottomLable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("底部导航");
        bottomPanel.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 10, 0, 10), -1, -1));
        panel7.add(leftPanel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        leftLable = new JLabel();
        leftLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/3.png")));
        leftLable.setText("");
        leftPanel.add(leftLable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("侧边导航");
        leftPanel.add(label5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 1, new Insets(20, 0, 20, 0), -1, -1));
        panel6.add(panel8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        allLable = new JLabel();
        allLable.setIcon(new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/4-1-1.png")));
        allLable.setText("");
        panel8.add(allLable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("选择模版：");
        panel9.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel9.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}

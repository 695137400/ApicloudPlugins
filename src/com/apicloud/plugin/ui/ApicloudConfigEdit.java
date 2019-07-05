package com.apicloud.plugin.ui;

import com.alibaba.fastjson.JSON;
import com.apicloud.plugin.ui.apicloudInfo.ApicloudInfo;
import com.apicloud.plugin.ui.apicloudInfo.Feature;
import com.apicloud.plugin.ui.apicloudInfo.FeatureParam;
import com.apicloud.plugin.util.ApicloudXML;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApicloudConfigEdit extends JPanel {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTabbedPane tabbedPane1;
    private JTextArea apicloudDescription;
    private JTextField apicloudId;
    private JTextField apicloudVersion;
    private JTextField authorName;
    private JTextField apicloudContent;
    private JTextField authoHref;
    private JTextField authoEmail;
    private JTextField apicloudName;
    private JButton selectColorAppBackground;
    private JTextField appBackgroundText;
    private JTextField windowBackgroundText;
    private JButton selectColorWindowBackground;
    private JTextField frameBackgroundColorText;
    private JButton selectColorFrameBackgroundColor;
    private JCheckBox pageBounce;
    private JCheckBox hScrollBarEnabled;
    private JCheckBox vScrollBarEnabled;
    private JCheckBox autoLaunch;
    private JCheckBox statusBarAppearance;
    private JCheckBox autoUpdate;
    private JCheckBox fullScreen;
    private JCheckBox smartUpdate;
    private JCheckBox debug;
    private JRadioButton keyResize;
    private JRadioButton keyPan;
    private JRadioButton keyAuto;
    private JCheckBox softInputBarEnabled;
    private JCheckBox dragAndDrop;
    private JCheckBox audioCheckBox;
    private JCheckBox locationCheckBox;
    private JCheckBox voipCheckBox;
    private JCheckBox newsstandContentCheckBox;
    private JCheckBox bluetoothCentralCheckBox;
    private JCheckBox fetchCheckBox;
    private JCheckBox externalAccessoryCheckBox;
    private JCheckBox bluetoothPeripheralCheckBox;
    private JCheckBox remoteNotificationCheckBox;
    private JTextArea querySchemesText;
    private JTextArea forbiddenSchemesText;
    private JTextField fontText;
    private JTextField urlSchemeText;
    private JTextField userAgentText;
    private JCheckBox allowKeyboardExtension;
    private JCheckBox fileShare;
    private JTextField customRefreshHeaderText;
    private JCheckBox launcher;
    private JCheckBox checkSslTrusted;
    private JCheckBox appCertificateVerify;
    private JCheckBox readPhoneState;
    private JCheckBox call;
    private JCheckBox sms;
    private JCheckBox camera;
    private JCheckBox record;
    private JCheckBox location;
    private JCheckBox fileSystem;
    private JCheckBox internetl;
    private JCheckBox bootCompleted;
    private JCheckBox hardware;
    private JCheckBox contact;
    private JButton addFeatureButton;
    private JTabbedPane tabbedPane2;
    private JButton deleteTab;
    private JButton selectImgAppBackground;
    private JButton selectImgFrameBackgroundColor;
    private JButton selectImgrWindowBackground;
    private JRadioButton accessNojailbreak;
    private JRadioButton accessLocal;
    private JRadioButton accessAll;
    private JTextField accessText;
    private JPanel jJBSX;
    private List<String> tabs = new ArrayList<String>();


    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    private String configPath = "";

    public ApicloudConfigEdit() {

        ButtonGroup accessGroup = new ButtonGroup();
        accessGroup.add(accessAll);
        accessGroup.add(accessLocal);
        accessGroup.add(accessNojailbreak);

        ButtonGroup keyGroup = new ButtonGroup();
        keyGroup.add(keyAuto);
        keyGroup.add(keyResize);
        keyGroup.add(keyPan);

        // setContentPane(contentPane);
        // setModal(true);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        addFeatureButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = JOptionPane.showInputDialog(null, "输入一个名称：", "输入对话框", JOptionPane.PLAIN_MESSAGE);
                if (!tabs.contains(str)) {
                    tabs.add(str);
                    tabbedPane2.addTab(str, new FeatureRow(str));
                } else {
                    JOptionPane.showMessageDialog(null, "当前名称已经存在", "错误提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        deleteTab.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int se = JOptionPane.showConfirmDialog(null, "确认删除当前TAB？", "提示", JOptionPane.YES_NO_OPTION);
                if (0 == se) {
                    String name = tabbedPane2.getSelectedComponent().getName();
                    tabs.remove(name);
                    tabbedPane2.remove(tabbedPane2.getSelectedComponent());
                }
            }
        });
        selectColorAppBackground.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "选取颜色", null);
                // 如果用户取消或关闭窗口, 则返回的 color 为 null
                if (color == null) {
                    return;
                }
                int alpha = color.getAlpha();
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                appBackgroundText.setText("rgba(" + red + "," + green + "," + blue + "," + alpha + ")");
            }
        });
        selectColorWindowBackground.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "选取颜色", null);
                // 如果用户取消或关闭窗口, 则返回的 color 为 null
                if (color == null) {
                    return;
                }
                int alpha = color.getAlpha();
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                windowBackgroundText.setText("rgba(" + red + "," + green + "," + blue + "," + alpha + ")");
            }
        });
        selectColorFrameBackgroundColor.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "选取颜色", null);
                // 如果用户取消或关闭窗口, 则返回的 color 为 null
                if (color == null) {
                    return;
                }
                int alpha = color.getAlpha();
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                frameBackgroundColorText.setText("rgba(" + red + "," + green + "," + blue + "," + alpha + ")");
            }
        });
        selectImgAppBackground.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = new JFileChooser();
                jf.showOpenDialog(null);//显示打开的文件对话框
                File f = jf.getSelectedFile();//使用文件类获取选择器选择的文件
                if (null != f) {
                    String s = f.getAbsolutePath();//返回路径名
                    appBackgroundText.setText(s);
                }
            }
        });
        selectImgrWindowBackground.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = new JFileChooser();
                jf.showOpenDialog(null);//显示打开的文件对话框
                File f = jf.getSelectedFile();//使用文件类获取选择器选择的文件
                if (null != f) {
                    String s = f.getAbsolutePath();//返回路径名
                    windowBackgroundText.setText(s);
                }
            }
        });
        selectImgFrameBackgroundColor.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = new JFileChooser();
                jf.showOpenDialog(null);//显示打开的文件对话框
                File f = jf.getSelectedFile();//使用文件类获取选择器选择的文件
                if (null != f) {
                    String s = f.getAbsolutePath();//返回路径名
                    frameBackgroundColorText.setText(s);
                }
            }
        });

    }

    public void init() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    restoreData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void onOK() {
        System.out.println("ok");
        ApicloudInfo apicloudInfo = new ApicloudInfo();
        apicloudInfo.setAppId(apicloudId.getText());
        apicloudInfo.setVersion(apicloudVersion.getText());
        apicloudInfo.setAppDescription(apicloudDescription.getText());
        apicloudInfo.setAppName(apicloudName.getText());
        apicloudInfo.setContent(apicloudContent.getText());
        apicloudInfo.author.setAuthorName(authorName.getText());
        apicloudInfo.author.setEmail(authoEmail.getText());
        apicloudInfo.author.setHref(authoHref.getText());
        if (null != accessText.getText() && !"".equals(accessText.getText())) {
            apicloudInfo.access.setAccessText(accessText.getText());
        } else if (accessAll.isSelected()) {
            apicloudInfo.access.setAccessText("*");
        } else if (accessLocal.isSelected()) {
            apicloudInfo.access.setAccessText("local");
        } else {
            apicloudInfo.access.setAccessText("nojailbreak");
        }

        if (contact.isSelected()) {
            apicloudInfo.permission.setContact(true);
        }
        if (readPhoneState.isSelected()) {
            apicloudInfo.permission.setReadPhoneState(true);
        }
        if (camera.isSelected()) {
            apicloudInfo.permission.setCamera(true);
        }
        if (fileSystem.isSelected()) {
            apicloudInfo.permission.setFileSystem(true);
        }
        if (internetl.isSelected()) {
            apicloudInfo.permission.setInternetl(true);
        }
        if (call.isSelected()) {
            apicloudInfo.permission.setCall(true);
        }
        if (record.isSelected()) {
            apicloudInfo.permission.setRecord(true);
        }
        if (hardware.isSelected()) {
            apicloudInfo.permission.setHardware(true);
        }
        if (sms.isSelected()) {
            apicloudInfo.permission.setSms(true);
        }
        if (location.isSelected()) {
            apicloudInfo.permission.setLocation(true);
        }
        if (bootCompleted.isSelected()) {
            apicloudInfo.permission.setBootCompleted(true);
        }

        if (pageBounce.isSelected()) {
            apicloudInfo.preference.setPageBounce(true);
        }
        if (hScrollBarEnabled.isSelected()) {
            apicloudInfo.preference.sethScrollBarEnabled(true);
        }
        if (vScrollBarEnabled.isSelected()) {
            apicloudInfo.preference.setvScrollBarEnabled(true);
        }
        if (autoLaunch.isSelected()) {
            apicloudInfo.preference.setAutoLaunch(true);
        }
        if (statusBarAppearance.isSelected()) {
            apicloudInfo.preference.setStatusBarAppearance(true);
        }
        if (fullScreen.isSelected()) {
            apicloudInfo.preference.setFullScreen(true);
        }
        if (autoUpdate.isSelected()) {
            apicloudInfo.preference.setAutoUpdate(true);
        }
        if (smartUpdate.isSelected()) {
            apicloudInfo.preference.setSmartUpdate(true);
        }
        if (debug.isSelected()) {
            apicloudInfo.preference.setDebug(true);
        }
        if (allowKeyboardExtension.isSelected()) {
            apicloudInfo.preference.setAllowKeyboardExtension(true);
        }
        if (softInputBarEnabled.isSelected()) {
            apicloudInfo.preference.setSoftInputBarEnabled(true);
        }
        if (launcher.isSelected()) {
            apicloudInfo.preference.setLauncher(true);
        }
        if (checkSslTrusted.isSelected()) {
            apicloudInfo.preference.setCheckSslTrusted(true);
        }
        if (dragAndDrop.isSelected()) {
            apicloudInfo.preference.setDragAndDrop(true);
        }
        if (fileShare.isSelected()) {
            apicloudInfo.preference.setFileShare(true);
        }
        if (appCertificateVerify.isSelected()) {
            apicloudInfo.preference.setAppCertificateVerify(true);
        }

        if (keyAuto.isSelected()) {
            apicloudInfo.preference.setSoftInputMode("auto");
        } else if (keyResize.isSelected()) {
            apicloudInfo.preference.setSoftInputMode("resize");
        } else if (keyPan.isSelected()) {
            apicloudInfo.preference.setSoftInputMode("pan");
        }
        if (null != fontText.getText() && !"".equals(fontText.getText())) {
            apicloudInfo.preference.setFont(fontText.getText());
        }
        if (null != urlSchemeText.getText() && !"".equals(urlSchemeText.getText())) {
            apicloudInfo.preference.setUrlScheme(urlSchemeText.getText());
        }
        if (null != userAgentText.getText() && !"".equals(userAgentText.getText())) {
            apicloudInfo.preference.setUserAgent(userAgentText.getText());
        }
        if (null != customRefreshHeaderText.getText() && !"".equals(customRefreshHeaderText.getText())) {
            apicloudInfo.preference.setCustomRefreshHeader(customRefreshHeaderText.getText());
        }
        if (null != forbiddenSchemesText.getText() && !"".equals(forbiddenSchemesText.getText())) {
            apicloudInfo.preference.setForbiddenSchemes(forbiddenSchemesText.getText());
        }
        if (null != querySchemesText.getText() && !"".equals(querySchemesText.getText())) {
            apicloudInfo.preference.setQuerySchemes(querySchemesText.getText());
        }
        String backgroundMode = "";
        if (audioCheckBox.isSelected()) {
            backgroundMode += "audio";
        }
        if (newsstandContentCheckBox.isSelected()) {
            backgroundMode += "|";
            backgroundMode += "newsstand-content";
        }
        if (externalAccessoryCheckBox.isSelected()) {
            backgroundMode += "|";
            backgroundMode += "external-accessory";
        }
        if (locationCheckBox.isSelected()) {
            backgroundMode += "|";
            backgroundMode += "location";
        }
        if (bluetoothCentralCheckBox.isSelected()) {
            backgroundMode += "|";
            backgroundMode += "bluetooth-central";
        }
        if (bluetoothPeripheralCheckBox.isSelected()) {
            backgroundMode += "|";
            backgroundMode += "bluetooth-peripheral";
        }
        if (voipCheckBox.isSelected()) {
            backgroundMode += "|";
            backgroundMode += "voip";
        }
        if (fetchCheckBox.isSelected()) {
            backgroundMode += "|";
            backgroundMode += "fetch";
        }
        if (remoteNotificationCheckBox.isSelected()) {
            backgroundMode += "|";
            backgroundMode += "remote-notification";
        }
        if (!"".equals(backgroundMode)) {
            apicloudInfo.preference.setBackgroundMode(backgroundMode);
        }
        if (null != appBackgroundText.getText() && !"".equals(appBackgroundText.getText())) {
            apicloudInfo.preference.setAppBackground(appBackgroundText.getText());
        }
        if (null != windowBackgroundText.getText() && !"".equals(windowBackgroundText.getText())) {
            apicloudInfo.preference.setWindowBackground(windowBackgroundText.getText());
        }
        if (null != frameBackgroundColorText.getText() && !"".equals(frameBackgroundColorText.getText())) {
            apicloudInfo.preference.setFrameBackgroundColor(frameBackgroundColorText.getText());
        }
        Component[] tabs = tabbedPane2.getComponents();
        for (int i = 0; i < tabs.length; i++) {
            FeatureRow fw = (FeatureRow) tabs[i];
            Feature feature = new Feature();
            feature.setTitleName(fw.getName());
            if (((JCheckBox) ((JPanel) fw.getComponent(0)).getComponent(3)).isSelected()) {
                feature.setForceBind(true);
            } else {
                feature.setForceBind(false);
            }
            JScrollPane s1 = (JScrollPane) fw.getComponent(1);
            JPanel rp = (JPanel) ((JViewport) s1.getComponent(0)).getComponent(0);
            for (int k = 0; k < rp.getComponentCount(); k++) {
                JPanel jp1 = (JPanel) rp.getComponent(k);
                String key = ((JTextField) jp1.getComponent(0)).getText();
                String value = ((JTextField) jp1.getComponent(1)).getText();
                FeatureParam featureParam = new FeatureParam();
                featureParam.setName(key);
                featureParam.setValue(value);
                feature.param.add(featureParam);
            }
            apicloudInfo.feature.add(feature);
        }
        System.out.println(JSON.toJSON(apicloudInfo));

    }

    private void restoreData() {
        try {
            ApicloudInfo apicloudInfo = ApicloudXML.readDoc(configPath);
            apicloudId.setText(apicloudInfo.getAppId());
            apicloudVersion.setText(apicloudInfo.getVersion());
            apicloudDescription.setText(apicloudInfo.getAppDescription());
            apicloudInfo.setAppName(apicloudName.getText());
            apicloudName.setText(apicloudInfo.getAppName());
            apicloudContent.setText(apicloudInfo.getContent());
            authorName.setText(apicloudInfo.author.getAuthorName());
            authoEmail.setText(apicloudInfo.author.getEmail());
            authoHref.setText(apicloudInfo.author.getHref());

            if (!StringUtils.isEmpty(apicloudInfo.access.getAccessText())) {
                if ("*".equals(apicloudInfo.access.getAccessText())) {
                    accessAll.setSelected(true);
                } else if ("local".equals(apicloudInfo.access.getAccessText())) {
                    accessLocal.setSelected(true);
                } else if ("nojailbreak".equals(apicloudInfo.access.getAccessText())) {
                    accessNojailbreak.setSelected(true);
                } else {
                    accessText.setText(apicloudInfo.access.getAccessText());
                }
            }
            contact.setSelected(apicloudInfo.permission.isContact());
            readPhoneState.setSelected(apicloudInfo.permission.isReadPhoneState());
            camera.setSelected(apicloudInfo.permission.isCamera());
            fileSystem.setSelected(apicloudInfo.permission.isFileSystem());
            internetl.setSelected(apicloudInfo.permission.isInternetl());
            call.setSelected(apicloudInfo.permission.isCall());
            record.setSelected(apicloudInfo.permission.isRecord());
            hardware.setSelected(apicloudInfo.permission.isHardware());
            sms.setSelected(apicloudInfo.permission.isSms());
            location.setSelected(apicloudInfo.permission.isLocation());
            bootCompleted.setSelected(apicloudInfo.permission.isBootCompleted());
            pageBounce.setSelected(apicloudInfo.preference.isPageBounce());
            hScrollBarEnabled.setSelected(apicloudInfo.preference.ishScrollBarEnabled());
            vScrollBarEnabled.setSelected(apicloudInfo.preference.isvScrollBarEnabled());
            autoLaunch.setSelected(apicloudInfo.preference.isAutoLaunch());
            statusBarAppearance.setSelected(apicloudInfo.preference.isStatusBarAppearance());
            fullScreen.setSelected(apicloudInfo.preference.isFullScreen());
            autoUpdate.setSelected(apicloudInfo.preference.isAutoUpdate());
            smartUpdate.setSelected(apicloudInfo.preference.isSmartUpdate());
            debug.setSelected(apicloudInfo.preference.isDebug());
            allowKeyboardExtension.setSelected(apicloudInfo.preference.isAllowKeyboardExtension());
            softInputBarEnabled.setSelected(apicloudInfo.preference.isSoftInputBarEnabled());
            launcher.setSelected(apicloudInfo.preference.isLauncher());
            checkSslTrusted.setSelected(apicloudInfo.preference.isCheckSslTrusted());
            dragAndDrop.setSelected(apicloudInfo.preference.isDragAndDrop());
            fileShare.setSelected(apicloudInfo.preference.isFileShare());
            appCertificateVerify.setSelected(apicloudInfo.preference.isAppCertificateVerify());
            if ("auto".equals(apicloudInfo.preference.getSoftInputMode())) {
                keyAuto.setSelected(true);
            } else if ("resize".equals(apicloudInfo.preference.getSoftInputMode())) {
                keyResize.setSelected(true);
            } else if ("pan".equals(apicloudInfo.preference.getSoftInputMode())) {
                keyPan.setSelected(true);
            }
            if (!StringUtils.isEmpty(apicloudInfo.preference.getFont())) {
                fontText.setText(apicloudInfo.preference.getFont());
            }
            if (!StringUtils.isEmpty(apicloudInfo.preference.getUrlScheme())) {
                urlSchemeText.setText(apicloudInfo.preference.getUrlScheme());
            }
            if (!StringUtils.isEmpty(apicloudInfo.preference.getUserAgent())) {
                userAgentText.setText(apicloudInfo.preference.getUserAgent());
            }
            if (!StringUtils.isEmpty(apicloudInfo.preference.getCustomRefreshHeader())) {
                customRefreshHeaderText.setText(apicloudInfo.preference.getCustomRefreshHeader());
            }
            if (!StringUtils.isEmpty(apicloudInfo.preference.getForbiddenSchemes())) {
                forbiddenSchemesText.setText(apicloudInfo.preference.getForbiddenSchemes());
            }
            if (!StringUtils.isEmpty(apicloudInfo.preference.getQuerySchemes())) {
                querySchemesText.setText(apicloudInfo.preference.getQuerySchemes());
            }
            String backgroundMode = apicloudInfo.preference.getBackgroundMode();
            if (!StringUtils.isEmpty(backgroundMode)) {
                String[] backgroundModeList = backgroundMode.split("|");
                if (backgroundModeList.length > 0) {
                    for (int i = 0; i < backgroundModeList.length; i++) {
                        String bm = backgroundModeList[i];
                        switch (bm) {
                            case "audio":
                                audioCheckBox.setSelected(true);
                                break;
                            case "newsstand-content":
                                newsstandContentCheckBox.setSelected(true);
                                break;
                            case "external-accessory":
                                externalAccessoryCheckBox.setSelected(true);
                                break;
                            case "location":
                                locationCheckBox.setSelected(true);
                                break;
                            case "bluetooth-central":
                                bluetoothCentralCheckBox.setSelected(true);
                                break;
                            case "bluetooth-peripheral":
                                bluetoothPeripheralCheckBox.setSelected(true);
                                break;
                            case "voip":
                                voipCheckBox.setSelected(true);
                                break;
                            case "fetch":
                                fetchCheckBox.setSelected(true);
                                break;
                            case "remote-notification":
                                remoteNotificationCheckBox.setSelected(true);
                                break;
                        }
                    }
                }
            }
            if (!StringUtils.isEmpty(apicloudInfo.preference.getAppBackground())) {
                appBackgroundText.setText(apicloudInfo.preference.getAppBackground());
            }
            if (!StringUtils.isEmpty(apicloudInfo.preference.getWindowBackground())) {
                windowBackgroundText.setText(apicloudInfo.preference.getWindowBackground());
            }
            if (!StringUtils.isEmpty(apicloudInfo.preference.getFrameBackgroundColor())) {
                frameBackgroundColorText.setText(apicloudInfo.preference.getFrameBackgroundColor());
            }
            List<Feature> feature = apicloudInfo.feature;
            if (null != feature && feature.size() > 0) {
                for (int i = 0; i < feature.size(); i++) {
                    Feature fea = feature.get(i);
                    String title = fea.getTitleName();
                    FeatureRow fw = new FeatureRow(title);
                    JScrollPane s1 = new JScrollPane();
                    JPanel rp = new JPanel();
                    List<FeatureParam> param = fea.param;
                    if (null != param && param.size() > 0) {
                        for (int t = 0; t < param.size(); t++) {
                            JPanel jp1 = new JPanel();
                            FeatureParam featureParam = param.get(t);
                            JTextField key = new JTextField();
                            key.setText(featureParam.getName());
                            JTextField value = new JTextField();
                            value.setText(featureParam.getValue());
                            jp1.add(key, 0);
                            jp1.add(value, 1);
                            rp.add(jp1, t);
                        }
                        s1.add(rp, 0);
                    }
                    JCheckBox ck = new JCheckBox();
                    ck.setSelected(fea.isForceBind());
                    fw.add(ck, 0);
                    fw.add(s1, 1);
                    tabbedPane2.addTab(title, fw);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCancel() {
        System.out.println("取消");
        //dispose();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("apicloud");
        frame.setContentPane(new ApicloudConfigEdit().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    public JPanel getContentPane() {
        return contentPane;
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setMinimumSize(new Dimension(-1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("确定");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tabbedPane1 = new JTabbedPane();
        tabbedPane1.setToolTipText("基本属性");
        panel3.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        jJBSX = new JPanel();
        jJBSX.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        jJBSX.setEnabled(true);
        jJBSX.setFocusable(false);
        jJBSX.setName("");
        jJBSX.setOpaque(true);
        jJBSX.setRequestFocusEnabled(false);
        jJBSX.setVerifyInputWhenFocusTarget(false);
        tabbedPane1.addTab("基本属性", jJBSX);
        final JLabel label1 = new JLabel();
        label1.setText("应用名称：");
        label1.setVerticalAlignment(0);
        label1.setVerticalTextPosition(0);
        jJBSX.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        apicloudName = new JTextField();
        apicloudName.setMinimumSize(new Dimension(200, 37));
        apicloudName.setText("");
        jJBSX.add(apicloudName, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("作者名：");
        jJBSX.add(label2, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("作者邮箱：");
        jJBSX.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("授权链接：");
        jJBSX.add(label4, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("起始页：");
        jJBSX.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("描述：");
        label6.setVerticalAlignment(1);
        label6.setVerticalTextPosition(1);
        jJBSX.add(label6, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        apicloudDescription = new JTextArea();
        apicloudDescription.setColumns(5);
        apicloudDescription.setRows(0);
        apicloudDescription.setText("");
        jJBSX.add(apicloudDescription, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        authorName = new JTextField();
        authorName.setMinimumSize(new Dimension(200, 37));
        authorName.setText("");
        jJBSX.add(authorName, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        authoEmail = new JTextField();
        authoEmail.setMinimumSize(new Dimension(200, 37));
        authoEmail.setText("");
        jJBSX.add(authoEmail, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        authoHref = new JTextField();
        authoHref.setMinimumSize(new Dimension(200, 37));
        authoHref.setText("");
        jJBSX.add(authoHref, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        apicloudContent = new JTextField();
        apicloudContent.setMinimumSize(new Dimension(200, 37));
        apicloudContent.setText("");
        jJBSX.add(apicloudContent, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("ID：");
        jJBSX.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        apicloudId = new JTextField();
        apicloudId.setMinimumSize(new Dimension(200, 37));
        apicloudId.setText("");
        jJBSX.add(apicloudId, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("version：");
        jJBSX.add(label8, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        apicloudVersion = new JTextField();
        apicloudVersion.setMinimumSize(new Dimension(200, 37));
        apicloudVersion.setText("");
        jJBSX.add(apicloudVersion, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(16, 8, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("偏好设置", panel4);
        final JLabel label9 = new JLabel();
        label9.setText("frameBackgroundColor：");
        label9.setVerticalAlignment(0);
        label9.setVerticalTextPosition(0);
        panel4.add(label9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        frameBackgroundColorText = new JTextField();
        frameBackgroundColorText.setMinimumSize(new Dimension(200, 37));
        frameBackgroundColorText.setText("");
        panel4.add(frameBackgroundColorText, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("pageBounce：");
        panel4.add(label10, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("hScrollBarEnabled ：");
        panel4.add(label11, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("vScrollBarEnabled：");
        panel4.add(label12, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("statusBarAppearance：");
        label13.setVerticalAlignment(1);
        label13.setVerticalTextPosition(1);
        panel4.add(label13, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("appBackground：");
        panel4.add(label14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appBackgroundText = new JTextField();
        appBackgroundText.setMinimumSize(new Dimension(200, 37));
        appBackgroundText.setText("");
        panel4.add(appBackgroundText, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("windowBackground：");
        panel4.add(label15, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        windowBackgroundText = new JTextField();
        windowBackgroundText.setMinimumSize(new Dimension(200, 37));
        windowBackgroundText.setText("");
        panel4.add(windowBackgroundText, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        selectColorAppBackground = new JButton();
        selectColorAppBackground.setText("选择颜色");
        panel4.add(selectColorAppBackground, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectColorFrameBackgroundColor = new JButton();
        selectColorFrameBackgroundColor.setText("选择颜色");
        panel4.add(selectColorFrameBackgroundColor, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pageBounce = new JCheckBox();
        pageBounce.setSelected(true);
        pageBounce.setText("页面支持滚动");
        panel4.add(pageBounce, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        hScrollBarEnabled = new JCheckBox();
        hScrollBarEnabled.setSelected(true);
        hScrollBarEnabled.setText("横向滚动条");
        panel4.add(hScrollBarEnabled, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        vScrollBarEnabled = new JCheckBox();
        vScrollBarEnabled.setSelected(true);
        vScrollBarEnabled.setText("竖直滚动条");
        panel4.add(vScrollBarEnabled, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("fullScreen：");
        panel4.add(label16, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label17 = new JLabel();
        label17.setText("autoLaunch：");
        panel4.add(label17, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        autoLaunch = new JCheckBox();
        autoLaunch.setSelected(true);
        autoLaunch.setText("启动页自动隐藏");
        panel4.add(autoLaunch, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statusBarAppearance = new JCheckBox();
        statusBarAppearance.setText("沉浸式效果");
        panel4.add(statusBarAppearance, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setText("autoUpdate：");
        panel4.add(label18, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        autoUpdate = new JCheckBox();
        autoUpdate.setSelected(true);
        autoUpdate.setText("自动检测更新");
        panel4.add(autoUpdate, new GridConstraints(4, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fullScreen = new JCheckBox();
        fullScreen.setText("全屏运行");
        panel4.add(fullScreen, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label19 = new JLabel();
        label19.setText("smartUpdate：");
        panel4.add(label19, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        smartUpdate = new JCheckBox();
        smartUpdate.setSelected(true);
        smartUpdate.setText("支持增量更新");
        panel4.add(smartUpdate, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        label20.setText("debug：");
        panel4.add(label20, new GridConstraints(5, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        debug = new JCheckBox();
        debug.setText("调试模式");
        panel4.add(debug, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label21 = new JLabel();
        label21.setText("allowKeyboardExtension：");
        panel4.add(label21, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label22 = new JLabel();
        label22.setText("softInputMode：");
        panel4.add(label22, new GridConstraints(6, 4, 2, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(6, 5, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-4473925)), null));
        keyResize = new JRadioButton();
        keyResize.setText("resize");
        panel5.add(keyResize, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        keyPan = new JRadioButton();
        keyPan.setText("pan");
        panel5.add(keyPan, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        keyAuto = new JRadioButton();
        keyAuto.setSelected(true);
        keyAuto.setText("auto");
        panel5.add(keyAuto, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label23 = new JLabel();
        label23.setText("配置键盘弹出方式");
        label23.setVerticalTextPosition(0);
        panel5.add(label23, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label24 = new JLabel();
        label24.setText("font(多个用'|'隔开)：");
        panel4.add(label24, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label25 = new JLabel();
        label25.setText("backgroundMode:");
        panel4.add(label25, new GridConstraints(9, 4, 4, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new GridConstraints(9, 5, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-4473925)), null));
        audioCheckBox = new JCheckBox();
        audioCheckBox.setText("audio");
        panel6.add(audioCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        locationCheckBox = new JCheckBox();
        locationCheckBox.setText("location");
        panel6.add(locationCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        voipCheckBox = new JCheckBox();
        voipCheckBox.setText("voip");
        panel6.add(voipCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        newsstandContentCheckBox = new JCheckBox();
        newsstandContentCheckBox.setText("newsstand-content");
        panel6.add(newsstandContentCheckBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bluetoothCentralCheckBox = new JCheckBox();
        bluetoothCentralCheckBox.setText("bluetooth-central");
        panel6.add(bluetoothCentralCheckBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fetchCheckBox = new JCheckBox();
        fetchCheckBox.setText("fetch");
        panel6.add(fetchCheckBox, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        externalAccessoryCheckBox = new JCheckBox();
        externalAccessoryCheckBox.setText("external-accessory");
        panel6.add(externalAccessoryCheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bluetoothPeripheralCheckBox = new JCheckBox();
        bluetoothPeripheralCheckBox.setText("bluetooth-peripheral");
        panel6.add(bluetoothPeripheralCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        remoteNotificationCheckBox = new JCheckBox();
        remoteNotificationCheckBox.setText("remote-notification");
        panel6.add(remoteNotificationCheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label26 = new JLabel();
        label26.setText("后台运行");
        panel6.add(label26, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label27 = new JLabel();
        label27.setText("urlScheme：");
        panel4.add(label27, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fontText = new JTextField();
        panel4.add(fontText, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label28 = new JLabel();
        label28.setText("userAgent：");
        panel4.add(label28, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label29 = new JLabel();
        label29.setText("dragAndDrop：");
        panel4.add(label29, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        urlSchemeText = new JTextField();
        panel4.add(urlSchemeText, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        userAgentText = new JTextField();
        panel4.add(userAgentText, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        softInputBarEnabled = new JCheckBox();
        softInputBarEnabled.setSelected(true);
        softInputBarEnabled.setText("显示键盘上方的工具条");
        panel4.add(softInputBarEnabled, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label30 = new JLabel();
        label30.setText("softInputBarEnabled：");
        panel4.add(label30, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        allowKeyboardExtension = new JCheckBox();
        allowKeyboardExtension.setSelected(true);
        allowKeyboardExtension.setText("允许使用第三方键盘");
        panel4.add(allowKeyboardExtension, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dragAndDrop = new JCheckBox();
        dragAndDrop.setSelected(true);
        dragAndDrop.setText("允许页面拖拽");
        panel4.add(dragAndDrop, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label31 = new JLabel();
        label31.setText("fileShare：");
        panel4.add(label31, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileShare = new JCheckBox();
        fileShare.setText("iTunes文件共享");
        panel4.add(fileShare, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label32 = new JLabel();
        label32.setText("customRefreshHeader：");
        panel4.add(label32, new GridConstraints(13, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        customRefreshHeaderText = new JTextField();
        panel4.add(customRefreshHeaderText, new GridConstraints(13, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label33 = new JLabel();
        label33.setText("appCertificateVerify：");
        panel4.add(label33, new GridConstraints(14, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appCertificateVerify = new JCheckBox();
        appCertificateVerify.setSelected(true);
        appCertificateVerify.setText("校验应用证书");
        panel4.add(appCertificateVerify, new GridConstraints(14, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label34 = new JLabel();
        label34.setText("forbiddenSchemes(逗号隔开)：");
        panel4.add(label34, new GridConstraints(15, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        forbiddenSchemesText = new JTextArea();
        panel4.add(forbiddenSchemesText, new GridConstraints(15, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label35 = new JLabel();
        label35.setText("querySchemes(逗号隔开)：");
        panel4.add(label35, new GridConstraints(15, 4, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        querySchemesText = new JTextArea();
        panel4.add(querySchemesText, new GridConstraints(15, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        selectImgAppBackground = new JButton();
        selectImgAppBackground.setText("选择图片");
        panel4.add(selectImgAppBackground, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectImgFrameBackgroundColor = new JButton();
        selectImgFrameBackgroundColor.setText("选择图片");
        panel4.add(selectImgFrameBackgroundColor, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectImgrWindowBackground = new JButton();
        selectImgrWindowBackground.setText("选择图片");
        panel4.add(selectImgrWindowBackground, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectColorWindowBackground = new JButton();
        selectColorWindowBackground.setText("选择颜色");
        panel4.add(selectColorWindowBackground, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label36 = new JLabel();
        label36.setText("launcher：");
        panel4.add(label36, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        launcher = new JCheckBox();
        launcher.setSelected(true);
        launcher.setText("桌面显示应用图标");
        panel4.add(launcher, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label37 = new JLabel();
        label37.setText("checkSslTrusted：");
        panel4.add(label37, new GridConstraints(8, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkSslTrusted = new JCheckBox();
        checkSslTrusted.setSelected(true);
        checkSslTrusted.setText("检查https证书");
        panel4.add(checkSslTrusted, new GridConstraints(8, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("扩展feature", panel7);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(panel9, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel9.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null));
        tabbedPane2 = new JTabbedPane();
        panel9.add(tabbedPane2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel9.add(panel10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        addFeatureButton = new JButton();
        addFeatureButton.setText("添加模块(Feature)");
        panel10.add(addFeatureButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteTab = new JButton();
        deleteTab.setHorizontalTextPosition(0);
        deleteTab.setText("删除TAB");
        panel10.add(deleteTab, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(4, 12, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("权限设置", panel11);
        final JLabel label38 = new JLabel();
        label38.setText("读取手机状态和身份：");
        panel11.add(label38, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel11.add(spacer2, new GridConstraints(0, 11, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel11.add(spacer3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        readPhoneState = new JCheckBox();
        readPhoneState.setText("");
        panel11.add(readPhoneState, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label39 = new JLabel();
        label39.setText("直接拨打电话：");
        panel11.add(label39, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        call = new JCheckBox();
        call.setText("");
        panel11.add(call, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label40 = new JLabel();
        label40.setText("直接发送短信：");
        panel11.add(label40, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sms = new JCheckBox();
        sms.setText("");
        panel11.add(sms, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label41 = new JLabel();
        label41.setText("                          ");
        panel11.add(label41, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label42 = new JLabel();
        label42.setText("使用拍照和视频：");
        panel11.add(label42, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        camera = new JCheckBox();
        camera.setText("");
        panel11.add(camera, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label43 = new JLabel();
        label43.setText("使用录音：");
        panel11.add(label43, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        record = new JCheckBox();
        record.setText("");
        panel11.add(record, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label44 = new JLabel();
        label44.setText("访问地理位置信息：");
        panel11.add(label44, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        location = new JCheckBox();
        location.setText("");
        panel11.add(location, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label45 = new JLabel();
        label45.setText("                          ");
        panel11.add(label45, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label46 = new JLabel();
        label46.setText("访问文件系统：");
        panel11.add(label46, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileSystem = new JCheckBox();
        fileSystem.setText("");
        panel11.add(fileSystem, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label47 = new JLabel();
        label47.setText("                          ");
        panel11.add(label47, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label48 = new JLabel();
        label48.setText("访问网络：");
        panel11.add(label48, new GridConstraints(0, 9, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        internetl = new JCheckBox();
        internetl.setText("");
        panel11.add(internetl, new GridConstraints(0, 10, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label49 = new JLabel();
        label49.setText("开机启动：");
        panel11.add(label49, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bootCompleted = new JCheckBox();
        bootCompleted.setText("");
        panel11.add(bootCompleted, new GridConstraints(2, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label50 = new JLabel();
        label50.setText("访问设备通讯录：");
        panel11.add(label50, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contact = new JCheckBox();
        contact.setText("");
        panel11.add(contact, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label51 = new JLabel();
        label51.setText("硬件设备：");
        panel11.add(label51, new GridConstraints(1, 9, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        hardware = new JCheckBox();
        hardware.setText("");
        panel11.add(hardware, new GridConstraints(1, 10, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(8, 15, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("访问设置", panel12);
        final JLabel label52 = new JLabel();
        Font label52Font = this.$$$getFont$$$(null, -1, 14, label52.getFont());
        if (label52Font != null) label52.setFont(label52Font);
        label52.setText("*：所有页面都可以访问扩展API方法，包括本地页面及远程web页面");
        panel12.add(label52, new GridConstraints(0, 0, 1, 15, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label53 = new JLabel();
        Font label53Font = this.$$$getFont$$$(null, -1, 14, label53.getFont());
        if (label53Font != null) label53.setFont(label53Font);
        label53.setText("local：只有本地页面可以访问扩展API方法");
        panel12.add(label53, new GridConstraints(1, 0, 1, 15, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label54 = new JLabel();
        Font label54Font = this.$$$getFont$$$(null, -1, 14, label54.getFont());
        if (label54Font != null) label54.setFont(label54Font);
        label54.setText("其它域名：只有在该域及其子域下面的页面可以访问扩展API方法，");
        panel12.add(label54, new GridConstraints(2, 0, 1, 15, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label55 = new JLabel();
        Font label55Font = this.$$$getFont$$$(null, -1, 14, label55.getFont());
        if (label55Font != null) label55.setFont(label55Font);
        label55.setText("注意我们这里未区分http和https，配置 http://apicloud.com 和 https://apicloud.com 效果一样");
        panel12.add(label55, new GridConstraints(3, 0, 1, 15, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label56 = new JLabel();
        Font label56Font = this.$$$getFont$$$(null, -1, 14, label56.getFont());
        if (label56Font != null) label56.setFont(label56Font);
        label56.setText("多个用换行分开");
        panel12.add(label56, new GridConstraints(5, 0, 1, 7, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label57 = new JLabel();
        Font label57Font = this.$$$getFont$$$(null, -1, 14, label57.getFont());
        if (label57Font != null) label57.setFont(label57Font);
        label57.setText("不允许越狱的设备使用本应用。若配置，将不允许越狱的设备使用本应用。");
        panel12.add(label57, new GridConstraints(4, 0, 1, 7, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        accessAll = new JRadioButton();
        accessAll.setSelected(true);
        accessAll.setText("所有(*)");
        panel12.add(accessAll, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel12.add(spacer4, new GridConstraints(7, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel12.add(spacer5, new GridConstraints(6, 5, 1, 10, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        accessLocal = new JRadioButton();
        accessLocal.setText("仅本地(local)");
        panel12.add(accessLocal, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        accessNojailbreak = new JRadioButton();
        accessNojailbreak.setText("禁止越狱(nojailbreak)");
        panel12.add(accessNojailbreak, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label58 = new JLabel();
        label58.setText("自定义：");
        panel12.add(label58, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        accessText = new JTextField();
        panel12.add(accessText, new GridConstraints(6, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        label8.setLabelFor(apicloudVersion);
        label15.setLabelFor(apicloudVersion);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}

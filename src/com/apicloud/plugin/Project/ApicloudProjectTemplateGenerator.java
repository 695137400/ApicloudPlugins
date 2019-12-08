package com.apicloud.plugin.Project;

import com.apicloud.plugin.util.PrintUtil;
import com.intellij.ide.util.projectWizard.WebProjectTemplate;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Random;


/**
 * WEB 模版
 */
public class ApicloudProjectTemplateGenerator extends WebProjectTemplate<String> {

    /**
     * 插件名称
     *
     * @return
     */
    @Override
    public String getName() {
        return "Apicloud App";
    }

    /**
     * 插件描述
     *
     * @return
     */
    @Override
    public String getDescription() {
        return "<h3>加速移动创新，帮助开发者和软件企业从传统B/S架构成功转向APP，快速进入移动、云和大数据时代<a href='https://www.apicloud.com/'>apicloud</a></h3>";
    }

    @Override
    public void generateProject(Project project, VirtualFile file, String type, Module module) {
        try {
            // PrintUtil.init(project);
            String projectPath = file.getCanonicalPath();
            PrintUtil.msg += "开始创建工程\n";
            Properties properties = System.getProperties();
            String systemPath = properties.getProperty("idea.plugins.path");
            File tempPath = new File(FileUtil.getTempDirectory() + "/apicloud-intelliJ-plugin");
            if (!tempPath.exists()) {
                PrintUtil.msg += ("临时解压文件路径：" + tempPath + "\n");
                PrintUtil.msg += ("当前工程路径：" + projectPath + "\n");
                PrintUtil.msg += ("当前系统缓存路径：" + systemPath + "\n");
                PrintUtil.msg += ("插件寻找结果：resources.jar exists:" + new File(systemPath + "/ApicloudPlugins/lib/resources.jar").exists() + "\n");
                com.apicloud.plugin.util.FileUtil.unZip(systemPath + "/ApicloudPlugins/lib/resources.jar", tempPath.getAbsolutePath() + "/", false);
            }
            PrintUtil.msg += ("当前选择创建工程类型：" + type + "\n");
            if ("default".equals(type)) {
                com.apicloud.plugin.util.FileUtil.copyFolder(tempPath.getAbsolutePath() + "/widget/default/", projectPath + "/");
            } else if ("bottom".equals(type)) {
                com.apicloud.plugin.util.FileUtil.copyFolder(tempPath.getAbsolutePath() + "/widget/bottom/", projectPath + "/");
            } else if ("home".equals(type)) {
                com.apicloud.plugin.util.FileUtil.copyFolder(tempPath.getAbsolutePath() + "/widget/home/", projectPath + "/");
            } else if ("slide".equals(type)) {
                com.apicloud.plugin.util.FileUtil.copyFolder(tempPath.getAbsolutePath() + "/widget/slide/", projectPath + "/");
            }
            PrintUtil.msg += ("config.xml封装：" + module.getName() + "\n");

            changeName(projectPath + File.separator + "config.xml", module.getName());
        } catch (Exception e) {
            e.printStackTrace();
            PrintUtil.msg += ("创建工程出错：" + e + "\n") + "\n";
        }
        PrintUtil.msg += "工程创建完毕!\n";
    }

    private static void changeName(String config, String name) {
        PrintUtil.msg += ("-------------->changeName") + "\n";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        Element wgtName = null;
        Element root = null;
        try {
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder db = factory.newDocumentBuilder();
            Document xmldoc = db.parse(new File(config));
            root = xmldoc.getDocumentElement();
            root.setAttribute("id","A"+String.valueOf(new Random().nextLong()).replaceAll("-","").substring(0,13));
            wgtName = (Element) selectSingleNode("/widget/name", root);
            wgtName.setTextContent(name);
            saveXml(config, xmldoc);
        } catch (Exception e) {
            e.printStackTrace();
            PrintUtil.msg += (e.getMessage()) + "\n";
        }
    }

    private static Node selectSingleNode(String express, Object source) {
        PrintUtil.msg += ("-------------->selectSingleNode") + "\n";
        Node result = null;
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        try {
            result = (Node) xpath.evaluate(express, source, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            PrintUtil.msg += (e.getMessage()) + "\n";
        }
        return result;
    }

    private static void saveXml(String fileName, Document doc) {
        PrintUtil.msg += ("-------------->saveXml") + "\n";
        TransformerFactory transFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty("indent", "no");
            doc.setXmlStandalone(true);
            // transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            DOMSource source = new DOMSource();
            source.setNode(doc);

            StreamResult result = new StreamResult();
            result.setOutputStream(new FileOutputStream(fileName));

            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            PrintUtil.msg += (e.getMessage()) + "\n";
        } catch (TransformerException e) {
            e.printStackTrace();
            PrintUtil.msg += (e.getMessage()) + "\n";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            PrintUtil.msg += (e.getMessage()) + "\n";
        }
    }

    @NotNull
    @Override
    public ApicloudProjectPeer createPeer() {
        return new ApicloudProjectPeer();
    }

    /**
     * 插件图标
     *
     * @return
     */
    @Override
    public Icon getIcon() {
        return new ImageIcon(getClass().getResource("/com/apicloud/plugin/icons/apicloud.png"));
    }
}

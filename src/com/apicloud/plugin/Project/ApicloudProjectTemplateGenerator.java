package com.apicloud.plugin.Project;

import com.apicloud.plugin.run.WebStorm;
import com.apicloud.plugin.util.PrintUtil;
import com.apicloud.plugin.util.ProjectData;
import com.apicloud.plugin.util.RunProperties;
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
public class ApicloudProjectTemplateGenerator extends WebProjectTemplate<ProjectData> {

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
    public void generateProject(Project project, VirtualFile file, ProjectData data, Module module) {
        try {
            String projectPath = file.getCanonicalPath();
            PrintUtil.msg += "开始创建工程\n";
            Properties properties = System.getProperties();
            String systemPath = properties.getProperty("idea.plugins.path");
            RunProperties.setAdbPath(data.getAdbPath());
            PrintUtil.msg += ("当前选择创建工程类型：" + data.getType() + "\n");
            if ("default".equals(data.getType())) {
                com.apicloud.plugin.util.FileUtil.copyFolder(systemPath + "/ApicloudPlugins/lib/widget/default/", projectPath + "/");
            } else if ("bottom".equals(data.getType())) {
                com.apicloud.plugin.util.FileUtil.copyFolder(systemPath + "/ApicloudPlugins/lib/widget/bottom/", projectPath + "/");
            } else if ("home".equals(data.getType())) {
                com.apicloud.plugin.util.FileUtil.copyFolder(systemPath + "/ApicloudPlugins/lib/widget/home/", projectPath + "/");
            } else if ("slide".equals(data.getType())) {
                com.apicloud.plugin.util.FileUtil.copyFolder(systemPath + "/ApicloudPlugins/lib/widget/slide/", projectPath + "/");
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
            root.setAttribute("id", "A" + String.valueOf(new Random().nextLong()).replaceAll("-", "").substring(0, 13));
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

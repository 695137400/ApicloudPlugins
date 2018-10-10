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
import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

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
            File tempPath = new File(FileUtil.getTempDirectory().toString() + "/apicloud-intelliJ-plugin");
            if (!tempPath.exists()) {
                PrintUtil.msg += ("临时解压文件路径：" + tempPath + "\n");
                PrintUtil.msg += ("当前工程路径：" + projectPath + "\n");
                PrintUtil.msg += ("当前系统缓存路径：" + systemPath + "\n");
                PrintUtil.msg += ("插件寻找结果：resources.jar exists:" + new File(systemPath + "/ApicloudPlugins/lib/resources.jar").exists() + "\n");
                ApicloudProjectTemplateGenerator.unZip(systemPath + "/ApicloudPlugins/lib/resources.jar", tempPath.getAbsolutePath() + "/", false);
            }
            PrintUtil.msg += ("当前选择创建工程类型：" + type + "\n");
            if ("default".equals(type)) {
                copyFolder(tempPath.getAbsolutePath() + "/widget/default/", projectPath + "/");
            } else if ("bottom".equals(type)) {
                copyFolder(tempPath.getAbsolutePath() + "/widget/bottom/", projectPath + "/");
            } else if ("home".equals(type)) {
                copyFolder(tempPath.getAbsolutePath() + "/widget/home/", projectPath + "/");
            } else if ("slide".equals(type)) {
                copyFolder(tempPath.getAbsolutePath() + "/widget/slide/", projectPath + "/");
            }
            PrintUtil.msg += ("config.xml封装：" + project.getName() + "\n");
            changeName(projectPath + File.separator + "config.xml", project.getName());
        } catch (Exception e) {
            e.printStackTrace();
            PrintUtil.msg += ("创建工程出错：" + e + "\n") + "\n";
        }
        PrintUtil.msg += "工程创建完毕!\n";
    }

    public static boolean unZip(String zipFilePath, String base, boolean deleteFile) throws Exception {
        PrintUtil.msg += ("开始解压文件---------------------") + "\n";
        try {
            File file = new File(zipFilePath);
            if (!file.exists()) {
                throw new RuntimeException("解压文件不存在!");
            }
            JarFile zipFile = new JarFile(file);
            Enumeration<JarEntry> e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) e.nextElement();
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(base + "/" + name);
                    f.mkdirs();
                    //PrintUtil.msg += ("创建文件夹：" + base + "/" + name) + "\n";
                } else {
                    File f = new File(base + zipEntry.getName());
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                    //PrintUtil.msg += ("创建文件夹：" + f.getParentFile().getAbsolutePath()) + "\n";
                    //PrintUtil.msg += ("创建文件：" + f.getAbsolutePath()) + "\n";
                    InputStream is = zipFile.getInputStream(zipEntry);
                    FileOutputStream fos = new FileOutputStream(f);
                    int length = 0;
                    byte[] b = new byte[1024];
                    while ((length = is.read(b, 0, 1024)) != -1) {
                        fos.write(b, 0, length);
                    }
                    is.close();
                    fos.close();
                }
            }
            if (zipFile != null) {
                zipFile.close();
            }
            if (deleteFile) {
                file.deleteOnExit();
            }
            return true;
        } catch (IOException ex) {
            PrintUtil.msg += (ex.getMessage()) + "\n";
            return false;
        }
    }

    public static void copyFolder(String oldPath, String newPath) {
        PrintUtil.msg += ("拷贝文件---------------------------") + "\n";
        try {
            if (!new File(newPath).exists()) {
                PrintUtil.msg += newPath + "目标路径不存在，创建\n";
                new File(newPath).mkdirs();
            }
            File a = new File(oldPath);
            String[] file = a.list();
            //PrintUtil.msg += "等待拷贝文件列表\n";
            File temp = null;
            for (int i = 0; i < file.length; ++i) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }
                //PrintUtil.msg += "当前文件" + temp.getAbsolutePath() + "\n";
                //PrintUtil.msg += "目标文件-------->：" + new File(newPath + "/" + temp.getName().toString()).exists() + "\n";
                if (temp.isFile() && !new File(newPath + "/" + temp.getName().toString()).exists()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + temp.getName().toString());
                    byte[] b = new byte[5120];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                    //PrintUtil.msg += ("copy file :" + newPath + "/" + temp.getName().toString() + "\n") + "\n";
                }
                if (temp.isDirectory() && !new File(newPath + "/" + temp.getName().toString()).exists()) {
                   //PrintUtil.msg += ("copyFolder :" + oldPath + "/" + file[i] + "\n") + "\n";
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            PrintUtil.msg += (e.getMessage());
        }
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
            root.setAttribute("id",UUID.randomUUID().toString().replaceAll("-","").substring(0,10));
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

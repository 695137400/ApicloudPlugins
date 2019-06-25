package com.apicloud.plugin.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: <br/>
 * Date: 18-10-31<br/>
 * Time: 下午3:26<br/>
 * Author:<br/>
 * Description: <span style="color:#63D3E9">文件工具</span><br/>
 */
public class FileUtil {

    public static boolean delAllFiles(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFiles(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }

    public static void copyFolder(String oldPath, String newPath) {
        try {
            new File(newPath).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; ++i) {
                if (file[i].contains(".svn")) {
                    continue;
                }
                if (oldPath.endsWith(File.separator))
                    temp = new File(oldPath + file[i]);
                else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + File.separator + temp.getName().toString());

                    byte[] b = new byte[5120];
                    int len;
                    while ((len = input.read(b)) != -1) {

                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory())
                    copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAppId(String configPath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Element widget = null;
        Element root = null;
        try {
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder db = factory.newDocumentBuilder();
            Document xmldoc = db.parse(new File(configPath));
            root = xmldoc.getDocumentElement();
            widget = (Element) selectSingleNode("/widget", root);
            String appId = widget.getAttribute("id");
            //System.out.println(appId);
            return appId;
            // theBook.setTextContent(replaceValue);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Node selectSingleNode(String express, Object source) {
        Node result = null;
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        try {
            result = (Node) xpath
                    .evaluate(express, source, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getWidgetContent(String configPath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Element content = null;
        Element root = null;
        try {
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder db = factory.newDocumentBuilder();
            Document xmldoc = db.parse(new File(configPath));
            root = xmldoc.getDocumentElement();
            content = (Element) selectSingleNode("/widget/content", root);
            String src = content.getAttribute("src");
            //System.out.println(appId);
            return src;
            // theBook.setTextContent(replaceValue);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
}

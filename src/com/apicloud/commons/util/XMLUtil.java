//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.apicloud.commons.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public final class XMLUtil {
    private XMLUtil() {
    }

    public static Document loadXmlFile(String filePath) throws DocumentException {
        return loadXmlFile(new File(filePath));
    }

    public static Document loadXmlFile(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(file);
    }

    public static Document loadXmlFile(InputStream in) throws DocumentException {
        SAXReader reader = new SAXReader();
        reader.setEncoding("UTF-8");
        return reader.read(in);
    }

    public static boolean saveXml(String filePath, Document document) throws IOException {
        return saveXml(new File(filePath), document);
    }

    public static boolean saveXml(File file, Document document) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        return saveXml(file, document, format);
    }

    public static boolean saveXml(String filePath, Document document, OutputFormat format) throws IOException {
        return saveXml(new File(filePath), document, format);
    }

    public static boolean saveXml(File file, Document document, OutputFormat format) throws IOException {
        FileOutputStream fos = null;
        XMLWriter xmlWriter = null;

        try {
            file.getParentFile().mkdirs();
            fos = new FileOutputStream(file);
            xmlWriter = new XMLWriter(fos, format);
            xmlWriter.write(document);
        } finally {
            if (xmlWriter != null) {
                xmlWriter.close();
            }

            if (fos != null) {
                fos.close();
            }

        }

        return true;
    }

    public static Document createDocument() {
        return DocumentHelper.createDocument();
    }

    public static void setAttribute(Element element, String name, String value) {
        Attribute attribute = element.attribute(name);
        if (attribute != null) {
            attribute.setValue(value);
        } else {
            element.addAttribute(name, value);
        }

    }

    public static void setText(Element element, String value) {
        element.setText(value);
    }

    public static Element setElementChild(Element element, String childName) {
        Element child = element.element(childName);
        if (child == null) {
            child = element.addElement(childName);
        }

        return child;
    }

    public static Element setElementChild(Element element, String childName, String childValue) {
        Element child = element.element(childName);
        if (child != null) {
            child.setText(childValue);
        } else {
            child = element.addElement(childName, childValue);
        }

        return child;
    }

    private static Document testCreateXMLFile() {
        Document document = DocumentHelper.createDocument();
        Element catalogElement = document.addElement("catalog");
        catalogElement.addComment("使用addComment方法方法向catalog元素添加注释");
        catalogElement.addProcessingInstruction("target", "text");
        Element journalElement = catalogElement.addElement("journal");
        journalElement.addAttribute("title", "XML Zone");
        journalElement.addAttribute("publisher", "Willpower Co");
        Element articleElement = journalElement.addElement("article");
        articleElement.addAttribute("level", "Intermediate");
        articleElement.addAttribute("date", "July-2006");
        Element titleElement = articleElement.addElement("title");
        titleElement.setText("Dom4j Create XML Schema");
        Element authorElement = articleElement.addElement("author");
        Element firstName = authorElement.addElement("fistname");
        firstName.setText("Yi");
        Element lastName = authorElement.addElement("lastname");
        lastName.setText("Qiao");
        return document;
    }

    private static Document testModifyXMLFile(String fileName) {
        SAXReader reader = new SAXReader();
        Document document = null;

        try {
            document = reader.read(new File(fileName));
            List<?> list = document.selectNodes("/catalog/journal/@title");
            Iterator itr = list.iterator();

            while(itr.hasNext()) {
                Attribute attribute = (Attribute)itr.next();
                if (attribute.getValue().equals("XML Zone")) {
                    attribute.setText("Modi XML");
                }
            }

            list = document.selectNodes("/catalog/journal");
            itr = list.iterator();
            Element articleElement;
            if (itr.hasNext()) {
                articleElement = (Element)itr.next();
                Element dateElement = articleElement.addElement("date");
                dateElement.setText("2006-07-10");
                dateElement.addAttribute("type", "Gregorian calendar");
            }

            list = document.selectNodes("/catalog/journal/article");
            itr = list.iterator();

            while(itr.hasNext()) {
                articleElement = (Element)itr.next();
                Iterator iter = articleElement.elementIterator("title");

                while(iter.hasNext()) {
                    Element titleElement = (Element)iter.next();
                    if (titleElement.getText().equals("Dom4j Create XML Schema")) {
                        articleElement.remove(titleElement);
                    }
                }
            }
        } catch (DocumentException var8) {
            var8.printStackTrace();
        }

        return document;
    }

    private static void print(List<Element> els) {
        Iterator var2 = els.iterator();

        while(var2.hasNext()) {
            Element el = (Element)var2.next();
            if (el.hasContent()) {
                print(el.elements());
            }
        }

    }

    public static String readFileByLines(File file) {
        String string = "";
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            for(String tempString = null; (tempString = reader.readLine()) != null; string = string + tempString) {
            }

            reader.close();
        } catch (IOException var12) {
            var12.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException var11) {
                }
            }

        }

        return string;
    }

    public static String formatXml(String str) throws Exception {
        Document document = null;
        document = DocumentHelper.parseText(str.trim());
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        StringWriter writer = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(writer, format);
        xmlWriter.write(document);
        xmlWriter.close();
        return writer.toString();
    }

    public static void main(String[] args) throws IOException {
        Document document = testCreateXMLFile();
        String filePath = "d:/temp/test_create.xml";
        saveXml(filePath, document);
        document = testModifyXMLFile(filePath);
        String modifyPath = "d:/temp/test_modify.xml";
        saveXml(modifyPath, document);

        try {
            Document doc = loadXmlFile(filePath);
            List<Element> listEl = doc.getRootElement().elements();
            print(listEl);
        } catch (DocumentException var6) {
            var6.printStackTrace();
        }

    }
}

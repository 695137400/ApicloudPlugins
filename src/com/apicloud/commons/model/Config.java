//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.apicloud.commons.model;

import com.apicloud.commons.util.XMLUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Config {
    private String id;
    private String version;
    private String name;
    private String desc;
    private String authorName;
    private String authorEmail;
    private String authorHref;
    private String contentSrc;
    private List<Access> accesses = new ArrayList();
    private List<Preference> preferences = new ArrayList();
    private List<Permission> permissions = new ArrayList();
    private List<Feature> features = new ArrayList();

    public Config() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return this.authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorHref() {
        return this.authorHref;
    }

    public void setAuthorHref(String authorHref) {
        this.authorHref = authorHref;
    }

    public String getContentSrc() {
        return this.contentSrc;
    }

    public void setContentSrc(String contentSrc) {
        this.contentSrc = contentSrc;
    }

    public List<Access> getAccesses() {
        return this.accesses;
    }

    public void addAccess(Access access) {
        this.accesses.add(access);
    }

    public void removeAccess(Access access) {
        this.accesses.remove(access);
    }

    public List<Feature> getFeatures() {
        return this.features;
    }

    public void addFeature(Feature feature) {
        this.features.add(feature);
    }

    public void removeFeature(Feature feature) {
        this.features.remove(feature);
    }

    public List<Preference> getPreferences() {
        return this.preferences;
    }

    public void addPreference(Preference preference) {
        this.preferences.add(preference);
    }

    public void removePreference(Preference preference) {
        this.preferences.remove(preference);
    }

    public List<Permission> getPermissions() {
        return this.permissions;
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }

    public static Config loadXml(String xml) {
        try {
            return loadXml((InputStream)(new ByteArrayInputStream(xml.getBytes("UTF-8"))));
        } catch (UnsupportedEncodingException var1) {
            return null;
        }
    }

    public static Config loadXml(InputStream input) {
        Config config = new Config();
        if (input == null) {
            return null;
        } else {
            Document document = null;

            try {
                document = XMLUtil.loadXmlFile(input);
                if (input != null) {
                    input.close();
                }
            } catch (DocumentException var14) {
                var14.printStackTrace();
                return null;
            } catch (IOException var15) {
                var15.printStackTrace();
                return null;
            }

            Element rootElement = document.getRootElement();
            String id = rootElement.attributeValue("id");
            String version = rootElement.attributeValue("version");
            config.setId(id);
            config.setVersion(version);
            parseGenral(rootElement, config);
            List<Element> preferenceElementList = rootElement.elements("preference");
            List<Preference> preferences = parsePreference(preferenceElementList);
            config.getPreferences().addAll(preferences);
            List<Element> accessElementList = rootElement.elements("access");
            List<Access> accesses = parseAccess(accessElementList);
            config.getAccesses().addAll(accesses);
            List<Element> permissionElementList = rootElement.elements("permission");
            List<Permission> permissions = parsePermission(permissionElementList);
            config.getPermissions().addAll(permissions);
            List<Element> featureElementList = rootElement.elements("feature");
            List<Feature> features = parseFeature(featureElementList);
            config.getFeatures().addAll(features);
            return config;
        }
    }

    private static void parseGenral(Element rootElement, Config config) {
        Element nameElement = rootElement.element("name");
        String name = nameElement.getText();
        config.setName(name);
        Element descriptionElement = rootElement.element("description");
        String description = descriptionElement.getText();
        config.setDesc(description);
        Element authorElement = rootElement.element("author");
        String authorName = authorElement.getText();
        String authorEmail = authorElement.attributeValue("email");
        String authorHref = authorElement.attributeValue("href");
        config.setAuthorHref(authorHref);
        config.setAuthorEmail(authorEmail);
        config.setAuthorName(authorName);
        Element contentElement = rootElement.element("content");
        String content = contentElement.attributeValue("src");
        config.setContentSrc(content);
    }

    private static List<Preference> parsePreference(List<Element> preferenceElementList) {
        List<Preference> preferences = new ArrayList();
        Iterator var3 = preferenceElementList.iterator();

        while(var3.hasNext()) {
            Element pref = (Element)var3.next();
            Preference preference = new Preference();
            String name = pref.attributeValue("name");
            String value = pref.attributeValue("value");
            preference.setName(name);
            preference.setValue(value);
            preferences.add(preference);
        }

        return preferences;
    }

    private static List<Access> parseAccess(List<Element> accessElementList) {
        List<Access> accesses = new ArrayList();
        Iterator var3 = accessElementList.iterator();

        while(var3.hasNext()) {
            Element acs = (Element)var3.next();
            Access access = new Access();
            String origin = acs.attributeValue("origin");
            access.setOrigin(origin);
            accesses.add(access);
        }

        return accesses;
    }

    private static List<Permission> parsePermission(List<Element> permissionElementList) {
        List<Permission> permissions = new ArrayList();
        Iterator var3 = permissionElementList.iterator();

        while(var3.hasNext()) {
            Element pref = (Element)var3.next();
            Permission permission = new Permission();
            String name = pref.attributeValue("name");
            permission.setName(name);
            permissions.add(permission);
        }

        return permissions;
    }

    private static List<Feature> parseFeature(List<Element> featureElementList) {
        List<Feature> features = new ArrayList();
        Iterator var3 = featureElementList.iterator();

        while(var3.hasNext()) {
            Element pref = (Element)var3.next();
            Feature feature = new Feature();
            String name = pref.attributeValue("name");
            feature.setName(name);
            features.add(feature);
            List<Element> paramElementList = pref.elements("param");
            parseParam(paramElementList, feature);
        }

        return features;
    }

    private static void parseParam(List<Element> paramElementList, Feature feature) {
        List<Param> params = new ArrayList();
        Iterator var4 = paramElementList.iterator();

        while(var4.hasNext()) {
            Element param = (Element)var4.next();
            Param pa = new Param();
            String name = param.attributeValue("name");
            String value = param.attributeValue("value");
            pa.setName(name);
            pa.setValue(value);
            params.add(pa);
        }

        feature.getParams().addAll(params);
    }

    public static Document getDocument(Config config) {
        Document document = XMLUtil.createDocument();
        Element rootElement = document.addElement("widget");
        rootElement.addAttribute("id", config.getId());
        rootElement.addAttribute("version", config.getVersion());
        createGenralElement(rootElement, config);
        createPreferenceElement(rootElement, config);
        createPermissionElement(rootElement, config);
        createAccessElement(rootElement, config);
        createFeatureElement(rootElement, config);
        return document;
    }

    public static String getDocumentContent(Config config) {
        return getDocument(config).asXML();
    }

    public static File saveXml(Config config, File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }

        Document document = XMLUtil.createDocument();
        Element rootElement = document.addElement("widget");
        rootElement.addAttribute("id", config.getId());
        rootElement.addAttribute("version", config.getVersion());
        createGenralElement(rootElement, config);
        createPreferenceElement(rootElement, config);
        createPermissionElement(rootElement, config);
        createAccessElement(rootElement, config);
        createFeatureElement(rootElement, config);

        try {
            XMLUtil.saveXml(file, document);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return file;
    }

    private static void createGenralElement(Element rootElement, Config config) {
        Element nameElement = rootElement.addElement("name");
        nameElement.setText(config.getName());
        Element descriptionElement = rootElement.addElement("description");
        descriptionElement.setText(config.getDesc());
        Element authorElement = rootElement.addElement("author");
        authorElement.addAttribute("email", config.getAuthorEmail());
        authorElement.addAttribute("href", config.getAuthorHref());
        authorElement.setText(config.getAuthorName());
        Element contentElement = rootElement.addElement("content");
        contentElement.addAttribute("src", config.getContentSrc());
    }

    private static void createPreferenceElement(Element rootElement, Config config) {
        Iterator var3 = config.getPreferences().iterator();

        while(var3.hasNext()) {
            Preference preference = (Preference)var3.next();
            Element preferenceElement = rootElement.addElement("preference");
            preferenceElement.addAttribute("name", preference.getName());
            preferenceElement.addAttribute("value", preference.getValue());
        }

    }

    private static void createAccessElement(Element rootElement, Config config) {
        Iterator var3 = config.getAccesses().iterator();

        while(var3.hasNext()) {
            Access access = (Access)var3.next();
            Element accessElement = rootElement.addElement("access");
            accessElement.addAttribute("origin", access.getOrigin());
        }

    }

    private static void createPermissionElement(Element rootElement, Config config) {
        Iterator var3 = config.getPermissions().iterator();

        while(var3.hasNext()) {
            Permission permission = (Permission)var3.next();
            Element permissionElement = rootElement.addElement("permission");
            permissionElement.addAttribute("name", permission.getName());
        }

    }

    private static void createFeatureElement(Element rootElement, Config config) {
        Iterator var3 = config.getFeatures().iterator();

        while(var3.hasNext()) {
            Feature feature = (Feature)var3.next();
            Element featureElement = rootElement.addElement("feature");
            featureElement.addAttribute("name", feature.getName());
            createParamElement(featureElement, feature);
        }

    }

    private static void createParamElement(Element featureElement, Feature feature) {
        Iterator var3 = feature.getParams().iterator();

        while(var3.hasNext()) {
            Param param = (Param)var3.next();
            Element paramElement = featureElement.addElement("param");
            paramElement.addAttribute("name", param.getName());
            paramElement.addAttribute("value", param.getValue());
        }

    }
}

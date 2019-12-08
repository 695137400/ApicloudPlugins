package com.apicloud.plugin.util;

import com.alibaba.fastjson.JSON;
import com.apicloud.plugin.ui.apicloudInfo.ApicloudInfo;
import com.apicloud.plugin.ui.apicloudInfo.Feature;
import com.apicloud.plugin.ui.apicloudInfo.FeatureParam;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-6-18-0018<br/>
 * Time: 21:59:54<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */
public class ApicloudXML {

    public static ApicloudInfo readDoc(String path) throws Exception {
        SAXReader reader = new SAXReader(); //1.创建一个xml解析器对象
        Document doc = reader.read(new File(path));//2.读取xml文档，返回Document对象
        Element elem = doc.getRootElement();
        System.out.println(elem.getName());
        List<Element> list = elem.elements(); //获取所有子标签
        ApicloudInfo apicloudInfo = new ApicloudInfo();
        if ("widget".equals(elem.getName())) {
            if (null != elem.attribute("id")) {
                apicloudInfo.setAppId(elem.attribute("id").getValue());
            }
            if (null != elem.attribute("version")) {
                apicloudInfo.setVersion(elem.attribute("version").getValue());
            }
        }
        if (list.size() > 0) {
            forElements(list, apicloudInfo);
        }
        return apicloudInfo;
    }

    public static void forElements(List<Element> elements, ApicloudInfo apicloudInfo) {
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            if ("name".equals(element.getName())) {
                if (null != element.getText()) {
                    apicloudInfo.setAppName(element.getText());
                }
            } else if ("description".equals(element.getName())) {
                if (null != element.getText()) {
                    apicloudInfo.setAppDescription(element.getText());
                }
            } else if ("content".equals(element.getName())) {
                if (null != element.attribute("src")) {
                    apicloudInfo.setContent(element.attribute("src").getValue());
                }
            } else if ("preference".equals(element.getName())) {
                if (null != element.attribute("name")) {
                    String name = element.attribute("name").getValue();
                    if ("appBackground".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setAppBackground(element.attribute("value").getValue());
                        }
                    } else if ("windowBackground".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setWindowBackground(element.attribute("value").getValue());
                        }
                    } else if ("frameBackgroundColor".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setWindowBackground(element.attribute("value").getValue());
                        }
                    } else if ("pageBounce".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setFrameBackgroundColor(element.attribute("value").getValue());
                        }
                    } else if ("hScrollBarEnabled".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setWindowBackground(element.attribute("value").getValue());
                        }
                    } else if ("vScrollBarEnabled".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.sethScrollBarEnabled("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("autoLaunch".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setAutoLaunch("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("statusBarAppearance".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setStatusBarAppearance("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("fullScreen".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setFullScreen("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("autoUpdate".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setAutoUpdate("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("smartUpdate".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setSmartUpdate("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("debug".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setDebug("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("allowKeyboardExtension".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setAllowKeyboardExtension("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("softInputMode".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setSoftInputMode(element.attribute("value").getValue());
                        }
                    } else if ("softInputBarEnabled".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setSoftInputBarEnabled("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("dragAndDrop".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setDragAndDrop("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("font".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setFont(element.attribute("value").getValue());
                        }
                    } else if ("backgroundMode".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setBackgroundMode(element.attribute("value").getValue());
                        }
                    } else if ("urlScheme".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setUrlScheme(element.attribute("value").getValue());
                        }
                    } else if ("querySchemes".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setQuerySchemes(element.attribute("value").getValue());
                        }
                    } else if ("forbiddenSchemes".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setForbiddenSchemes(element.attribute("value").getValue());
                        }
                    } else if ("userAgent".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setUserAgent(element.attribute("value").getValue());
                        }
                    } else if ("fileShare".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setFileShare("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("customRefreshHeader".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setCustomRefreshHeader(element.attribute("value").getValue());
                        }
                    } else if ("launcher".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setLauncher("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("checkSslTrusted".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setCheckSslTrusted("true".equals(element.attribute("value").getValue()));
                        }
                    } else if ("appCertificateVerify".equals(name)) {
                        if (null != element.attribute("value")) {
                            apicloudInfo.preference.setAppCertificateVerify("true".equals(element.attribute("value").getValue()));
                        }
                    }
                }
            } else if ("permission".equals(element.getName())) {
                if (null != element.attribute("name")) {
                    String value = element.attribute("name").getValue();
                    if ("location".equals(value)) {
                        apicloudInfo.permission.setLocation(true);
                    }
                    if ("readPhoneState".equals(value)) {
                        apicloudInfo.permission.setReadPhoneState(true);
                    }
                    if ("call".equals(value)) {
                        apicloudInfo.permission.setCall(true);
                    }
                    if ("sms".equals(value)) {
                        apicloudInfo.permission.setSms(true);
                    }
                    if ("camera".equals(value)) {
                        apicloudInfo.permission.setCamera(true);
                    }
                    if ("record".equals(value)) {
                        apicloudInfo.permission.setRecord(true);
                    }
                    if ("fileSystem".equals(value)) {
                        apicloudInfo.permission.setFileSystem(true);
                    }
                    if ("internetl".equals(value)) {
                        apicloudInfo.permission.setInternetl(true);
                    }
                    if ("bootCompleted".equals(value)) {
                        apicloudInfo.permission.setBootCompleted(true);
                    }
                    if ("hardware".equals(value)) {
                        apicloudInfo.permission.setHardware(true);
                    }
                    if ("contact".equals(value)) {
                        apicloudInfo.permission.setContact(true);
                    }
                }
            } else if ("feature".equals(element.getName())) {
                Feature feature = new Feature();
                if (null != element.attribute("name")) {
                    feature.setTitleName(element.attribute("name").getValue());
                    if (element.elements().size() > 0) {
                        List<Element> es = element.elements();
                        for (int h = 0; h < es.size(); h++) {
                            Element e = es.get(h);
                            if (null != e.attribute("name")) {
                                FeatureParam featureParam = new FeatureParam();
                                featureParam.setName(e.attribute("name").getValue());
                                if (null != e.attribute("value")) {
                                    featureParam.setValue(e.attribute("value").getValue());
                                }
                                feature.param.add(featureParam);
                            }
                        }
                    }
                }
                apicloudInfo.feature.add(feature);
            } else if ("author".equals(element.getName())) {
                if (null != element.attribute("email")) {
                    apicloudInfo.author.setEmail(element.attribute("email").getValue());
                }
                if (null != element.attribute("href")) {
                    apicloudInfo.author.setHref(element.attribute("href").getValue());
                }
                if (null != element.getText()) {
                    apicloudInfo.author.setAuthorName(element.getText());
                }
            } else if ("access".equals(element.getName())) {
                if (null != element.attribute("origin")) {
                    apicloudInfo.access.setAccessText(element.attribute("origin").getValue());
                }
            } else if ("description".equals(element.getName())) {
                if (null != element.getText()) {
                    apicloudInfo.setAppDescription(element.getText());
                }
            }
            if (element.elements().size() > 0 && !"feature".equals(element.getName())) {
                forElements(element.elements(), apicloudInfo);
            }
        }
    }

    public static void main(String[] args) {
        String path = "D:\\workspace\\Apicloud\\CapitalOperation\\config.xml";
        try {
            System.out.println(JSON.toJSON(readDoc(path)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

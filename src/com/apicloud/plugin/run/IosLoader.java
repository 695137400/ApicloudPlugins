package com.apicloud.plugin.run;

import com.apicloud.plugin.util.PrintUtil;
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

public class IosLoader {
	
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String wigetPath = null;
/*	public static void main(String[] args) {
		String webStormPath = "C:\\Users\\gaoang\\WebstormProjects\\workspace\\webStorm-APICloud";
		String widgetPath = "C:\\Users\\gaoang\\WebstormProjects\\workspace\\svn46";
		runMain(webStormPath, widgetPath);
	}*/
	public static void run(String webStormPath,String widgetPath) {
//		String webStormPath = "C:\\Users\\gaoang\\WebstormProjects\\workspace\\webStorm-APICloud";
//		String widgetPath = "C:\\Users\\gaoang\\WebstormProjects\\workspace\\svn46";
		PrintUtil.info("----------->runMain");
		getWidgetPath(widgetPath);
		if(wigetPath == null){
			PrintUtil.info("Not found config.xml");
			return;
		}
		
		String appId = getAppId(wigetPath+File.separator+"config.xml");
		
		if (appId == null || "".equals(appId)) {
			PrintUtil.info("Please make sure the directory is correct");
			return;
		}
		
		//apicloud-loader
		String apicloud_loaderpath = webStormPath+File.separator+"appLoader"+File.separator+"apicloud-loader-ios";
		String apicloud_loader = apicloud_loaderpath+File.separator +"load.ipa";
		String apicloud_verPath = apicloud_loaderpath+File.separator+"load.conf";
		
		
		String custom_loaderpath = webStormPath+File.separator+"appLoader"+File.separator+"custom-loader-ios";
		String custom_loader = apicloud_loaderpath+File.separator +appId+File.separator+"load.ipa";
		String custom_verPath = apicloud_loaderpath+File.separator+appId+File.separator+"load.conf";
		
		String syncappJarPath = webStormPath+File.separator+"syncapp.jar";
//		String javaPath = null;
		String loaderpath = null;
		String loader = null;
		String verPath = null;
		if(new File(custom_loader).exists() && new File(custom_verPath).exists()){
			loaderpath = custom_loaderpath;
			loader = custom_loader;
			verPath = custom_verPath;
		}else{
			if(new File(apicloud_loader).exists() && new File(apicloud_verPath).exists()){
				loaderpath = apicloud_loaderpath;
				loader = apicloud_loader;
				verPath = apicloud_verPath;
			}
		}
		
		if(loaderpath == null || loader ==null || verPath == null){
			System.out.println("Please make sure the directory is correct");
			return;
		}
		
		String cachePath = System.getProperties().getProperty("user.home")
				+ File.separator + "uztools" + File.separator + appId  ;
		copyFolder(wigetPath,cachePath);
		String runLoader = "";
		
		if(!isMacOS()){
			String sysPath = System.getProperty("java.library.path");
			sysPath = sysPath.substring(0, sysPath.length()-1);
			String jrePath = webStormPath+ File.separator +"jre";
			String set_java_path = "cmd.exe /C set JAVA_HOME="+jrePath+"&&set PATH="+sysPath+jrePath+"\\bin;";
			String runJar = "&&"+jrePath+"\\bin\\java.exe -jar "+syncappJarPath+" "+cachePath+" "+loaderpath+" "+loader+" "+verPath;
			runLoader = set_java_path+runJar;
		}else{
			runLoader = "java -jar "+syncappJarPath+" "+cachePath+" "+loaderpath+" "+loader+" "+verPath;
			
		}
//		String[]  runLoader = {batPath,javaPath, "-jar",syncappJarPath , cachePath, loaderpath, loader, verPath};
		
		try {
			runCmd(runLoader);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!isMacOS()) {
			delAllFiles(cachePath);
		}else{
//			String[] cmd = {"rm -rf",cachePath};
			try {
				runCmd("rm -rf "+cachePath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static boolean isMacOS(){
		return OS.indexOf("mac")>=0&&OS.indexOf("os")>0;
	}
	public static void runCmd(String cmd) throws Exception {
		// System.out.println("cmd = " + cmd[2]);
		Runtime rt = Runtime.getRuntime();
		BufferedReader br = null;
		InputStreamReader isr = null;
				
		Process p = rt.exec(cmd);
		isr = new InputStreamReader(p.getInputStream(), "gbk");
		br = new BufferedReader(isr);
		
		String msg = null;
		while ((msg = br.readLine()) != null) {
			 System.out.println(msg);
		}
		
		if (isr != null) {
			isr.close();
		}
		if (br != null) {
			br.close();
		}
	}
	private static String getAppId(String configPath) {
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
	private static void getWidgetPath(String filePath){
		if(filePath == null || "".equals(filePath)){
			return;
		}
		File file = new File(filePath);
		String[] fileList = file.list();
		boolean isContinue = true;
		for(String fileS : fileList){
			if("config.xml".equals(fileS)){
				String content =  getWidgetContent(filePath+File.separator+"config.xml");
				if(isContent(fileList,content)){
					wigetPath = filePath;
					isContinue = false;
					break;
				}
			}
		}
		
		if(isContinue)getWidgetPath(file.getParent());
		
		
	}
	private static String getWidgetContent(String configPath){
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
	private static boolean isContent(String[] listFile,String content){
		for(String file:listFile){
			if(file.equals(content)){
				return true;
			}
		}
		return false;
	}
	public static void copyFolder(String oldPath, String newPath) {
		try {
			new File(newPath).mkdirs();
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; ++i) {
				if(file[i].contains(".svn")){
					continue;
				}
				if (oldPath.endsWith(File.separator))
					temp = new File(oldPath + file[i]);
				else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ File.separator + temp.getName().toString());

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
	public static boolean delAllFiles(String path){
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
}

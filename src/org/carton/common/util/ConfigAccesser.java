package org.carton.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class ConfigAccesser {
	private static final String TRUE="true";
	private static final String MAP="map";
	private static final String LIST="list";
	private static final String LISTITEM="item";
	Document document;
	Element rootElement;
	File savedFile;
	public ConfigAccesser() throws FileNotFoundException, JDOMException, IOException {
		this(new File((new File("")).getAbsolutePath()+File.separator+"config.info"));
	}	
	public ConfigAccesser(File f) throws FileNotFoundException, JDOMException, IOException {
		savedFile=f;
		if(savedFile.exists()&&savedFile.canRead()&&savedFile.canWrite()) {
			SAXBuilder saxBuilder = new SAXBuilder();
			document = saxBuilder.build(new FileInputStream(f));
			rootElement=document.getRootElement();
		}else {
			rootElement=new Element("Settings");
			document=new Document(rootElement);
		}
		rootElement.setAttribute("Creater", "ConfigAccesser V1.1");
	}
	private Element getIndexElement(String settingName) {
		List<Element> children=rootElement.getChildren();
		for(Element i:children) {
			if(i.getName().equals(settingName)) {
				return i;
			}
		}
		rootElement.setAttribute(settingName+"_enabled", TRUE);
		Element setting=new Element(settingName);
		rootElement.addContent(setting);
		return setting;
	}
	public void saveMapToXml(String settingName,Map<String,String> map) {
		Element setting=getIndexElement(settingName);
		setting.setAttribute("DataType",MAP);
		String list="";
		for(String i:map.keySet()) {
			list+=i+","; 
			if(sameNodeCheck(i,setting)!=null) {
				sameNodeCheck(i,setting).setAttribute("value", map.get(i));
			}else {
				Element e=new Element(i);
				e.setAttribute("value", map.get(i));
				setting.addContent(e);
			}
		}
		list.subSequence(0, list.length()-1);
		setting.setAttribute("items", list);
		
	}
	public void saveListToXml(String settingName,List<String> listData) {
		Element setting=getIndexElement(settingName);
		setting.setAttribute("DataType",LIST);
		String length=""+listData.size();
		for(int i=0;i<listData.size();i++) {
			if(sameNodeCheck(LISTITEM+"_"+i,setting)!=null) {
				sameNodeCheck(LISTITEM+"_"+i,setting).setAttribute("value", listData.get(i));
			}else {
				Element e=new Element(LISTITEM+"_"+i);
				e.setAttribute("value", listData.get(i));
				setting.addContent(e);
			}
			
		}
		setting.setAttribute("length", length);
		
	}
	public void loadListFromElement(String settingName,List<String> listData) {
		String result=rootElement.getAttributeValue(settingName+"_enabled");
		if(result==null||!result.equals(TRUE))return;
		Element setting=getIndexElement(settingName);
		if(setting==null)return;
		result=setting.getAttributeValue("DataType");
		if(result==null||!result.equals(LIST))return;
		try {
			int length=Integer.parseInt(setting.getAttributeValue("length"));
			for(int i=0;i<length;i++) {
				Element e=setting.getChild(LISTITEM+"_"+i);
				if(e==null) {
					listData.add("");
				}else {
					String value=e.getAttributeValue("value");
					if(value!=null) {
						listData.add(value);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public void loadMapFromElement(String settingName,Map<String,String> map) {
		String result=rootElement.getAttributeValue(settingName+"_enabled");
		if(result==null||!result.equals(TRUE))return;
		Element setting=getIndexElement(settingName);
		if(setting==null)return;
		result=setting.getAttributeValue("DataType");
		if(result==null||!result.equals(MAP))return;
		String items[]=setting.getAttributeValue("items").split(",");
		for(String i:items) {
			Element e=setting.getChild(i);
			if(e==null) {
				map.put(i, "");
			}else {
				String value=e.getAttributeValue("value");
				if(value!=null) {
					map.put(i, value);
				}
			}
		}
	}
	private Element sameNodeCheck(String name,Element e) {
		for(Element c:(List<Element>)e.getChildren()) {
			if(c.getName().equals(name))
				return c;
		}
		return null;
	}
	public void saveConfiguration() throws IOException {
		savedFile.createNewFile();
		XMLOutputter xop=new XMLOutputter();
		FileOutputStream fos=new FileOutputStream(savedFile);
		xop.output(document, fos);
	}
}

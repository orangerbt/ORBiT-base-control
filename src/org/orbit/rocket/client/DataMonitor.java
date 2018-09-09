package org.orbit.rocket.client;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.carton.common.net.GeneralUDPSocket;
import org.carton.common.net.ReceiveListener;
import org.carton.common.net.ServiceDiscoverUDPSocket;
import org.carton.common.service.GeneralServiceExecutePool;
import org.carton.common.util.ConfigAccesser;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.orbit.rocket.gui.*;
import org.orbit.rocket.server.*;


public class DataMonitor extends GeneralServiceExecutePool {
	String name;
	boolean isGPSReady;
	HashMap<String,ValueProfile[]> profileMap;
	HashMap<String,DisplayUnit> displayMap;
	int dataPort;
	int discoverPort;
	ConcurrentLinkedQueue<HashMap<String,Object>> dataQueue = new ConcurrentLinkedQueue<HashMap<String,Object>>();
	GeneralUDPSocket gus;
	ServiceDiscoverUDPSocket sdus;
	File generalConfigFile;
	ArrayList<InetAddress> serverList;
	DataLogger logger;
	
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 * @throws IOException
	 */
	public DataMonitor() throws FileNotFoundException, JDOMException, IOException {
		File localPath=new File("").getAbsoluteFile();
		generalConfigFile=new File(localPath.getAbsolutePath()+File.separator+"Data_Monitor_Setting.xml");
		Element e=loadConfigurationFromFile();
		serverList=new ArrayList<InetAddress>();
		logger=new DataLogger();
		if(e!=null) {
			loadConfigurationFromElement(e);
			gus=new GeneralUDPSocket(dataPort);
			sdus=new ServiceDiscoverUDPSocket(discoverPort);
			gus.addRecevieListener(new ReceiveListener() {

				@Override
				public boolean verify(byte[] data, InetAddress ip, int port) {
					// TODO Auto-generated method stub
					return serverList.contains(ip);
				}

				@Override
				public void process(byte[] data, InetAddress ip, int port) {
					// TODO Auto-generated method stub
					try {
						ByteArrayInputStream bis=new ByteArrayInputStream(data);
						ObjectInputStream ois=new ObjectInputStream(bis);
						HashMap<String,Object> dataFrame=$(ois.readObject());
						dataQueue.add(dataFrame);
					}catch(Exception e) {
						e.printStackTrace();
					}
					
				}
				
			});
			this.lunchUnit(logger);
			this.lunchUnit(gus);
		}else{
			logger.submitWarning("Profile Compiling Error", "", "");
			throw new FileNotFoundException("Profile Compiling Error");
		}
	}
	/**
	 * 
	 * @throws UnknownHostException
	 */
	public void checkServer() throws UnknownHostException {
		sdus.discoverService(name, false,discoverPort, new ReceiveListener() {

			@Override
			public boolean verify(byte[] data, InetAddress ip, int port) {
				String[] info=new String(data).trim().split("@");
				if(info[0].equals(name))
					return true;
				return false;
			}

			@Override
			public void process(byte[] data, InetAddress ip, int port) {
				// TODO Auto-generated method stub
				serverList.add(ip);
			}
			
		});
	}
	
	
	/**
	 * 
	 * @param u
	 */
	public void addDisplayUnit(DisplayUnit u) {
		if(displayMap.containsKey(u.getDataType()))displayMap.get(u.getDataType()).addNext(u);
		else {
			displayMap.put(u.getDataType(), u);
		}
	}
	/**
	 * 
	 * @param l
	 */
	public void addAllDisplayUnit(List<DisplayUnit> l) {
		for(DisplayUnit u:l) {
			addDisplayUnit(u);
		}
	}
	/**
	 * 
	 * @param DataType
	 * @param DataName
	 */
	public void removeDisplayUnit(String DataType,String DataName) {
		if(displayMap.containsKey(DataType))displayMap.get(DataType).remove(DataType+"#"+DataName);
	}
	/**
	 * 
	 * @throws Exception
	 */
	private void processInfo()throws Exception {
		if(!dataQueue.isEmpty()) {
			HashMap<String,Object> dataItem=dataQueue.poll();
			String dataType=$(dataItem.get("data type"));
			ArrayList<String> result=$(dataItem.get("value"));
			long time=Long.parseLong($(dataItem.get("time")));
			ValueProfile vps[]=profileMap.get(dataType);
			double dataArray[]=new double[result.size()];
			for(int i=0;i<result.size();i++) {
				double data=Double.parseDouble(result.get(i));
				dataArray[i]=data;
			}
			for(ValueProfile vp:vps) {
				displayMap.get(dataType).updateInfo(vp.compileData(dataArray));
			}
		}
	}
	/**
	 * 
	 * @param e
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 * @throws IOException
	 */
	private void loadConfigurationFromElement(Element e) throws FileNotFoundException, JDOMException, IOException {
		if(e.getName()!="DataMonitor"&&
			e.getAttributeValue("Name")==null&&
			e.getAttributeValue("DataPort")==null&&
			e.getAttributeValue("DiscoverPort")==null&&
			e.getAttributeValue("ProfileSize")==null&&
			e.getAttributeValue("IsGPSReady")==null)return;
		this.name=e.getAttributeValue("Name");
		this.dataPort=Integer.parseInt(e.getAttributeValue("DataPort"));
		this.discoverPort=Integer.parseInt(e.getAttributeValue("DiscoverPort"));
		this.isGPSReady=(e.getAttributeValue("IsGPSReady").equals("TRUE"))?true:false;
		profileMap=new HashMap<String,ValueProfile[]>();
		for(Element profiles:(List<Element>)e.getChildren()) {
			String dataType=profiles.getAttributeValue("DataType");
			int profilesSize=Integer.parseInt(profiles.getAttributeValue("ProfilesSize"));
			ValueProfile vps[]=new ValueProfile[profilesSize];
			for(Element vpi:(List<Element>)profiles.getChildren()) {
				ValueProfile vp=ValueProfile.complieProfileFromNode(vpi);
				vps[vp.getOrder()]=vp;
			}
			profileMap.put(dataType, vps);
		}
	}
	private Element saveConfigConfigurationToElement() {
		Element e=new Element("DataMonitor");
		e.setAttribute("Name", name);
		e.setAttribute("DataPort", dataPort+"");
		e.setAttribute("DiscoverPort", discoverPort+"");
		e.setAttribute("IsGPSReady",(isGPSReady)?"TRUE":"FALSE");
		
		for(String i:profileMap.keySet()) {
			Element profiles=new Element("Profiles"); 
			profiles.setAttribute("DataType", i);
			profiles.setAttribute("ProfilesSize", profileMap.get(i).length+"");
			for(ValueProfile vp:profileMap.get(i)) {
				profiles.addContent(vp.compileProfileToElement());
			}
			e.addContent(profiles);
		}
		return e;
	}
	private Element loadConfigurationFromFile() throws FileNotFoundException, JDOMException, IOException {
		Element rootElement;
		if(generalConfigFile.exists()&&generalConfigFile.canRead()&&generalConfigFile.canWrite()) {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(new FileInputStream(generalConfigFile));
			rootElement=document.getRootElement();
			return rootElement;
		}else {
			return null;
		}
		
	}
	private void saveConfigurationToFile(Element e) throws IOException {
		Document document=new Document(e);
		XMLOutputter xop=new XMLOutputter();
		FileOutputStream fos=new FileOutputStream(generalConfigFile);
		xop.output(document, fos);
	}
	/**
	 * 
	 * @throws IOException
	 */
	public void saveConfiguration() throws IOException {
		Element e=saveConfigConfigurationToElement();
		saveConfigurationToFile(e);
	}
	private <T> T $(Object o) {
		return (T)o;
	}
}














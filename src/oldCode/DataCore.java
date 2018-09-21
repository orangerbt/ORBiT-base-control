package oldCode;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jdom.JDOMException;

import fixedGeneralPacket.ConfigAccesser;
import fixedGeneralPacket.GeneralServiceExecutePool;
import fixedGeneralPacket.GeneralUDPSocket;
import fixedGeneralPacket.ReceiveListener;
import fixedGeneralPacket.ServiceDiscoverUDPSocket;

public class DataCore extends GeneralServiceExecutePool{
//	ConfigAccesser dataFormatConfig;
	ConfigAccesser generalConfig;
	HashMap<String,String> generalSetting;
	ArrayList<DataCollector> collectors=new ArrayList<DataCollector>();
//	private ConcurrentLinkedQueue<HashMap<String,ArrayList<String>>> dataQueue = new ConcurrentLinkedQueue<HashMap<String,ArrayList<String>>>();
	GeneralUDPSocket gus;
//	HashSet<InetAddress> ipList=new HashSet<InetAddress>();
	HashMap<String,HashSet<InetAddress>> monitorIPList=new HashMap<String,HashSet<InetAddress>>();
	HashMap<String,ConcurrentLinkedQueue<HashMap<String,Object>>> monitorQueueList=new HashMap<String,ConcurrentLinkedQueue<HashMap<String,Object>>>();
	HashMap<String,String> dataDeliverList=new HashMap<String,String>();
	ServiceDiscoverUDPSocket sdus;
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 * @throws IOException
	 */
	public DataCore() throws FileNotFoundException, JDOMException, IOException {
		File localPath=new File("").getAbsoluteFile();
		File generalConfigFile=new File(localPath.getAbsolutePath()+File.separator+"setting.xml");
		generalConfig=new ConfigAccesser(generalConfigFile);
		loadConfig();
	}
	/**
	 * 
	 * @param dc
	 */
	public void addCollector(DataCollector dc) {
		collectors.add(dc);
	}
	/**
	 * 
	 * @throws NumberFormatException
	 * @throws SocketException
	 */
	public void loadConfig() throws NumberFormatException, SocketException {
		generalSetting=new HashMap<String,String>();
		ArrayList<String> listData=new ArrayList<String>(); 
		generalConfig.loadMapFromElement("GeneralSetting", generalSetting);
		generalConfig.loadListFromElement("Monitors", listData);
		generalConfig.loadMapFromElement("DataDeliverList", dataDeliverList);
		sdus=new ServiceDiscoverUDPSocket(Integer.parseInt(generalSetting.get("DiscoverPort")));
		for(String i:listData) {
			monitorQueueList.put(i, new ConcurrentLinkedQueue<HashMap<String,Object>>());
			monitorIPList.put(i, new HashSet<InetAddress>());
			sdus.addService(i, true,new ReceiveListener() {
				
				@Override
				public boolean verify(byte[] data, InetAddress ip, int port) {
					// TODO Auto-generated method stub
					return true;
				}
	
				@Override
				public void process(byte[] data, InetAddress ip, int port) {
					// TODO Auto-generated method stub
					String info[]=new String(data).trim().split("@");
					System.out.println(""+ip+":"+port);
//					ipList.add(ip);
					if(monitorIPList.containsKey(info[0])) {
						monitorIPList.get(info[0]).add(ip);
					}else {
						HashSet<InetAddress> ipList=new HashSet<InetAddress>();
						ipList.add(ip);
						monitorIPList.put(info[0], ipList);
					}
				}
				
			});
		}
		gus=new GeneralUDPSocket(Integer.parseInt(generalSetting.get("DataPort")));
		this.lunchUnit(gus);
	}
	// DATA FORMAT
	// <DATA TYPE>,<DATA 1>,<DATA 2>,<DATA 3>
	//int - 123
	//double - 12.12
	//Point - 21.3#23.123
	/**
	 * 
	 */
	public void compileData() {
		HashMap<String,Object> dataFrame=new HashMap<String,Object>();
		for(DataCollector dc:collectors) {
			String[] data=dc.getRocketData();
			String dataType=data[0];
			data=Arrays.copyOfRange(data, 1, data.length);
//			HashMap<String,String> dataFormat=dataItems.get(dataType);
//			boolean isPoint=dataFormat.get("ValueType").equals("Point");
			//TestCase
//			boolean isPoint=false;
			ArrayList<String> result=new ArrayList<String>();
			for(int i=0;i<data.length;i++) {
				String dataPoint=data[i];
				if(dataPoint.contains("#")) {
					result.add(dataPoint.replaceAll("#", ","));
				}else {
					result.add(dataPoint);
				}
			}
			dataFrame.put("data type", dataType);
			dataFrame.put("value", result);
			dataFrame.put("time", System.currentTimeMillis()+"");
			String delivers[]=dataDeliverList.get(dataType).split(",");
			for(String i:delivers) {
				monitorQueueList.get(i).add(dataFrame);
			}
			
		}
		//TestCase
		try {
			sendData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @throws IOException
	 */
	public void sendData() throws IOException {
		Set<String> monitors=monitorQueueList.keySet();
		for(String monitor:monitors) {
			HashMap<String,Object> dataFrame=monitorQueueList.get(monitor).remove();
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(bos);
			oos.writeObject(dataFrame);
			oos.flush();
			byte[] dataPackage=bos.toByteArray();
			oos.close();
			bos.close();
			HashSet<InetAddress> ipList=monitorIPList.get(monitor);
			for(InetAddress i:ipList) {
//				gus.send(dataPackage, Integer.parseInt(generalSetting.get("DataPort")), i);
				gus.send(dataPackage, 17852, i);
			}
		}
		
//		System.out.println(dataFrame);
//		for(InetAddress i:ipList) {
////			gus.send(dataPackage, Integer.parseInt(generalSetting.get("DataPort")), i);
//			gus.send(dataPackage, 17852, i);
//		}
		
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 * @throws IOException
	 */
	public void saveConfig() throws FileNotFoundException, JDOMException, IOException {
		ArrayList<String> listData=new ArrayList<String>();
		listData.addAll(monitorQueueList.keySet());
		File localPath=new File("").getAbsoluteFile();
		File generalConfigFile=new File(localPath.getAbsolutePath()+File.separator+"setting.xml");
		generalConfig=new ConfigAccesser(generalConfigFile);
		generalConfig.saveListToXml("Monitors", listData);
		generalConfig.saveMapToXml("DataDeliverList", dataDeliverList);
		generalConfig.saveMapToXml("GeneralSetting", generalSetting);
		generalConfig.saveConfiguration();
	}
}

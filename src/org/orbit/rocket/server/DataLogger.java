package org.orbit.rocket.server;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.carton.common.service.GeneralService;
import org.carton.common.service.ServiceListener;

public class DataLogger extends GeneralService{
	boolean status[]=new boolean[3];
	private ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<String>();
	SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	File logFile;
	boolean isAlive;
	FileOutputStream fis;
	/**
	 * 
	 * @param messege
	 * @param valueName
	 * @param value
	 */
	public void submitInfo(String messege,String valueName,String value) {
		Date now=new Date();
        String time = format0.format(now.getTime());
        String info=new String();
		if(valueName==null||valueName.equals("")) {
			info=String.format("INFO %-20s AT %-19s.%s",messege,time,now.getTime()%1000);
		}else {
			info=String.format("INFO %-20s AT %-19s.%-3s,%10s = %-10s",messege,time,now.getTime()%1000,valueName,value);
		}
		System.out.println(info);
		concurrentLinkedQueue.add(info);
		synchronized(fis) {
			fis.notifyAll();
		}
		
	}
	/**
	 * 
	 * @param messege
	 * @param valueName
	 * @param value
	 */
	public void submitWarning(String messege,String valueName,String value) {
		Date now=new Date();
        String time = format0.format(now.getTime());
        String warning=new String();
		if(valueName==null&&valueName.equals("")) {
			warning=String.format("WARNING %-20s AT %-19s.%s",messege,time,now.getTime()%1000);
		}else {
			warning=String.format("WARNING %-20s AT %-19s.%-3s,%10s = %-10s",messege,time,now.getTime()%1000,valueName,value);
		}
		System.err.println(warning);
		concurrentLinkedQueue.add(warning);
		synchronized(fis) {
			fis.notifyAll();
		}
	}
	/**
	 * 
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "DataLogger";
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		File localPath=new File("").getAbsoluteFile();
		String os = System.getProperty("os.name");
		String time="";
		if(os.toLowerCase().startsWith("win")){
			SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss");
			time = format0.format(new Date().getTime());
		}else {
			time = format0.format(new Date().getTime());
		}
		logFile=new File(localPath.getAbsolutePath()+File.separator+time+".log");
		try {
//			System.out.println(logFile);
			fis=new FileOutputStream(logFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
		}
		submitInfo("Logger Running",null,null);
		status[0]=true;
	}

	@Override
	public void execute() {
		PrintWriter pw=new PrintWriter(fis);
//		System.out.print("run");
		while(status[0]) {
			while(!concurrentLinkedQueue.isEmpty()) {
				String test=concurrentLinkedQueue.remove();
//				System.out.println(test);
				pw.println(test);
				pw.flush();
			}
			synchronized(fis) {
				try {
					fis.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
			}
		}
		status[1]=true;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub\
		status[0]=false;
		synchronized(fis) {
			fis.notifyAll();
		}
		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		status[2]=true;
	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return status[0];
	}

	@Override
	public boolean isExecuted() {
		// TODO Auto-generated method stub
		return status[1];
	}

	@Override
	public boolean isFinish() {
		// TODO Auto-generated method stub
		return status[2];
	}

	@Override
	public void addListener(ServiceListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateListener() {
		
	}

	@Override
	protected void callListener(HashMap<String, Object> map) {
		
	}
	
}

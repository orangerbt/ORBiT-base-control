package org.orbit.rocket.gui;
import java.util.List;
import java.util.Map;

public interface DisplayCore {
	String getMonitorName();
	String getServerIP();
	int getServerPort();
	int getMonitorID();
	List<Map<String,String>> getSensorSetting();//See excel file attach
	void setSensorSetting(List<Map<String,String>> setting);//See excel file attach
	void setServerIP(String ip);
	void setServerPort(int port);
	void setMonitorName(String name);
	void addDataListener(InfoListener l);
	void removeDataListener(InfoListener l);
}

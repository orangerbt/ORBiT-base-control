package org.orbit.rocket.gui;

public abstract class DisplayCore {
	public abstract String getMonitorName();
	public abstract String getServerIP();
	public abstract int getServerPort();
	public abstract String getStatus();
	public abstract int getID();
	public abstract void setMonitorName(String name);
	public abstract void setServerIP(String ip);
	public abstract void setServerPort(int port);
	
}

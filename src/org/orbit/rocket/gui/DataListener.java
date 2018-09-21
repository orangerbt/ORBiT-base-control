package org.orbit.rocket.gui;

public abstract class DataListener {
	 abstract boolean verify(String sensorKey);
	 abstract void action(String sensorKey,String data,long time);
	 abstract void action(String sensorKey,String sensorName,String sensorUnit,String data,long time);//Action method will be executed by order
}

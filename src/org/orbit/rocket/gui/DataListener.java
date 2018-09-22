package org.orbit.rocket.gui;

public abstract class DataListener {
	 abstract boolean verify(String sensorKey);
<<<<<<< HEAD
	 abstract void action(String sensorKey,String data);
=======
	 abstract void action(String sensorKey,String data,long time);
>>>>>>> faa7b65a487ca2b65d9c67edf2fce335245a4fa2
	 abstract void action(String sensorKey,String sensorName,String sensorUnit,String data,long time);//Action method will be executed by order
}

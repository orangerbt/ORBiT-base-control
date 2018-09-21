package oldCode;

public interface InfoListener {
	boolean verify(SensorProfile source);
	void action(SensorProfile source,String data);
}

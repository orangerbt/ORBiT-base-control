package test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.orbit.rocket.client.MonitorLoader;

public class TestMain {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		MonitorLoader ml=new MonitorLoader(null);
		List<Map<String,String>> r=ml.getSensorinfo();
		for(Map<String,String> i:r) {
			System.out.println(i);
		}
	}

}

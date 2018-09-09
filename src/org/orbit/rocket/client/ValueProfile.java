package org.orbit.rocket.client;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

import org.jdom.Element;

public class ValueProfile implements Serializable {
	private String type;
	private String name;
	private String display;
	private String unit;
	private double highLimited;
	private double lowLimited;
	private double passValue;
	int order;
	/**
	 * 
	 * @param type
	 * @param name
	 * @param display
	 * @param unit
	 * @param order
	 * @param high
	 * @param low
	 * @param pass
	 */
	public ValueProfile(String type,String name,String display,String unit,int order,double high,double low,double pass) {
		this.type=type;
		this.name=name;
		this.display=display;
		this.highLimited=high;
		this.lowLimited=low;
		this.passValue=pass;
		this.order=order;
		this.unit=unit;
	}
	/**
	 * 
	 * @return
	 */
	public int getOrder() {
		return order;
	}
	/**
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}
	/**
	 * 
	 * @param e
	 * @return
	 */
	public static ValueProfile complieProfileFromNode(Element e) {
		if(e.getName().equals("ValueProfile")) {
			String type=e.getAttributeValue("type");
			String name=e.getAttributeValue("name");
			String display=e.getAttributeValue("display");
			String unit=e.getAttributeValue("unit");
			String highLimited=e.getAttributeValue("highLimited");
			String lowLimited=e.getAttributeValue("lowLimited");
			String passValue=e.getAttributeValue("passValue");
			String order= e.getAttributeValue("order");
			String result=((type!=null)?type:"")+"$"
					+((name!=null)?name:"")+"$"
					+((order!=null)?order:"")+"$"
					+((unit!=null)?unit:"")+"$"
					+((display!=null)?display:"")+"$"
					+((highLimited!=null&&!highLimited.equals(""))?highLimited:"")
					+((passValue!=null&&!passValue.equals(""))?passValue:"")
					+((lowLimited!=null&&!lowLimited.equals(""))?("$"+lowLimited):"");
//			System.out.println(result);
			return compileProfileFromString(result);
		}
		return null;
	}
	/**
	 * 
	 * @param profile
	 * @return
	 */
	public static ValueProfile compileProfileFromString(String profile) {
		ValueProfile vp;
		String type="";
		String name="";
		String display="";
		String unit="";
		double highLimited=0;
		double lowLimited=0;
		double passValue=0;
		int order=0;
		if(profile==null)return null;
		profile=profile.trim();
		profile=profile.replace('$', '#');
		String values[]=profile.split("#");
//		System.out.println(Arrays.toString(values));
		if(values[0].equals(""))return null;
		type=values[0];
		if(values[1].equals(""))return null;
		name=values[1];
		if(values[2].equals(""))return null;
		try {
			order=Integer.parseInt(values[2]);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		if(values[3].equals(""))return null;
		unit=values[3];
		if(values[4].equals(""))return null;
		display=values[4];
		if(values[4].equals("VALUE"));
		if(values[4].equals("PASS")) {
			try {
				passValue=Double.parseDouble(values[5]);
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		if(values[4].equals("H/L")) {
			try {
				highLimited=Double.parseDouble(values[5]);
				lowLimited=Double.parseDouble(values[6]);
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		vp=new ValueProfile(type, name, display,unit, order, highLimited, lowLimited, passValue);
		return vp;
	}
	/**
	 * 
	 * @return
	 */
	public Element compileProfileToElement() {
		Element e=new Element("ValueProfile");
		e.setAttribute("type", type);
		e.setAttribute("name", name);
		e.setAttribute("display", display);
		e.setAttribute("unit", unit);
		e.setAttribute("order", order+"");
		if(this.display.equals("H/L")) {
			e.setAttribute("highLimited", highLimited+"");
			e.setAttribute("lowLimited", lowLimited+"");
			e.setAttribute("passValue", "");
		}else if(this.display.equals("PASS")) {
			e.setAttribute("highLimited", "");
			e.setAttribute("lowLimited", "");
			e.setAttribute("passValue", passValue+"");
		}else if(this.display.equals("VALUE")){
			e.setAttribute("highLimited", "");
			e.setAttribute("lowLimited", "");
			e.setAttribute("passValue", "");
		}
		return e;
	}
	/**
	 * 
	 * @return
	 */
	public String compileProfileToString() {
		String result="";
		if(type!=null)result+=type;result+="$";
		if(name!=null)result+=name;result+="$";
		if(display!=null)result+=display;result+="$";
		if(order>-1)result+=order;result+="$";
		result+=highLimited;result+="$";
		result+=lowLimited;result+="$";
		result+=passValue;
		return result;
	}
	/**
	 * 
	 * @param datas
	 * @return
	 */
	public HashMap<String,String> compileData(double[] datas){
		double data=datas[this.order];
		String value="";
		if(this.display.equals("H/L")) {
			value+=((data/(this.highLimited-this.lowLimited))*100)+"%";
		}else if(this.display.equals("PASS")) {
			value=(data>=this.passValue)?"PASS":"FAIL";
		}else if(this.display.equals("VALUE")){
			value=data+" "+unit;
		}
		HashMap<String,String> result=new HashMap<String,String>();
		result.put("DataTerm", this.type+"#"+this.name);
		result.put("Value", value);
		result.put("RawData", data+"");
		return result;
	}
}

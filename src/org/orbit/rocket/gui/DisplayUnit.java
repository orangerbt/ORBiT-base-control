package org.orbit.rocket.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.*;

public class DisplayUnit extends JPanel {
	JLabel dataName;
	JLabel value;
	String dataTerm;
	DisplayUnit next;
	//color: Red-Error Yellow-Warning Green-Normal
	/**
	 * 
	 * @param dataTerm
	 */
	public DisplayUnit(String dataTerm) {
		super(new GridLayout(1,2));
		dataName=new JLabel();
		value=new JLabel();
		this.add(dataName);
		this.add(value);
		this.dataTerm=dataTerm;
	}
	/**
	 * 
	 * @return
	 */
	public String getDataType() {
		return dataTerm.split("#")[0];
	}
	/**
	 * 
	 * @return
	 */
	public String getDataName() {
		return dataTerm.split("#")[1];
	}
	/**
	 * 
	 * @param info
	 */
	public void updateInfo(HashMap<String,String> info) {
		if(!dataTerm.equals(info.get("DataTerm"))) {
			if(next!=null) {
				next.updateInfo(info);
				return;
			}
			return;
		}
		String name=info.get("DataTerm").split("#")[1];
		dataName.setText(name);
		String valueInfo=info.get("Value");
		if(valueInfo.contains("%")) {
			value.setText(valueInfo);
			this.setBackground(Color.WHITE);
		}else if(valueInfo.contains("A")) {
			value.setText(info.get("RawData"));
			if(valueInfo.equals("PASS"))this.setBackground(Color.GREEN);
			if(valueInfo.equals("FAIL"))this.setBackground(Color.RED);
		}else {
			value.setText(valueInfo);
			this.setBackground(Color.WHITE);
		}
		this.repaint();
	}
	/**
	 * 
	 * @param nextU
	 */
	public void addNext(DisplayUnit nextU) {
		if(next!=null)next.addNext(nextU);
		else next=nextU;
	}
	/**
	 * 
	 * @param DataTerm
	 */
	public void remove(String DataTerm) {
		if(next!=null&&DataTerm.equals(next.getDataType()+"#"+next.getDataName())) {
			next=next.breakChain();
		}
	}
	/**
	 * 
	 * @return
	 */
	public DisplayUnit breakChain() {
		return next;
	}
}

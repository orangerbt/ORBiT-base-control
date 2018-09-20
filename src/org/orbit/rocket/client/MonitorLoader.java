package org.orbit.rocket.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook ;
import org.apache.poi.ss.usermodel.CellType;

public class MonitorLoader {
	HSSFSheet mainSheet;
	String adKey;
	public MonitorLoader(File setting) throws IOException {
		FileInputStream in = new FileInputStream("SensorSetting.xls");
		HSSFWorkbook book = new HSSFWorkbook(in);
		mainSheet = book.getSheet("Administrator");
		HSSFRow roll=mainSheet.getRow(1);
		adKey=roll.getCell(2).getStringCellValue();
	}
	public List<Map<String,String>> getSensorinfo() throws NoSuchAlgorithmException{
		HSSFRow itemRoll=mainSheet.getRow(2);
		int cellIndex=1;
		ArrayList<String> itemList=new ArrayList<String>();
		while(itemRoll.getCell(cellIndex)!=null&&!itemRoll.getCell(cellIndex).getStringCellValue().equals("")) {
			itemList.add(itemRoll.getCell(cellIndex).getStringCellValue());
			cellIndex++;
		}
		cellIndex=1;
		int rollIndex=3;
		HSSFRow roll=mainSheet.getRow(rollIndex);
		ArrayList<Map<String,String>> result=new ArrayList<Map<String,String>>();
		while(roll!=null&&roll.getCell(cellIndex)!=null&&!roll.getCell(cellIndex).getStringCellValue().equals("")) {
			Map<String,String> items=new HashMap<String,String>();
			for(int i=0;i<itemList.size();i++) {
				if(itemList.get(i).equals("Sensor Key")) {
					items.put(itemList.get(i),getMD5(items.toString()+adKey));
				}else {
					if(roll.getCell(cellIndex+i).getCellType().equals(CellType.NUMERIC))
						items.put(itemList.get(i), roll.getCell(cellIndex+i).getNumericCellValue()+"");
					if(roll.getCell(cellIndex+i).getCellType().equals(CellType.STRING))
						items.put(itemList.get(i), roll.getCell(cellIndex+i).getStringCellValue());
					if(roll.getCell(cellIndex+i).getCellType().equals(CellType.BLANK))
						items.put(itemList.get(i),"");
				}
			}
			result.add(items);
			rollIndex++;
			roll=mainSheet.getRow(rollIndex);
		} 
		return result;
	}
	private String getMD5(String in) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(in.getBytes());
	    byte[] digest = md.digest();
	    String myHash = DatatypeConverter
	      .printHexBinary(digest).toUpperCase();
		return myHash;
	}
}

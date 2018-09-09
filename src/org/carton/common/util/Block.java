package org.carton.common.util;

/**
 * This class is design as a byte array saver than processer
 * @author mike
 *
 */
public class Block  {
	byte[] info;
	int pointer=0;
	int pos;
	public Block(int length,int pos){
		info=new byte[length];
		this.pos=pos;
	}
	public Block(byte[] data,int pos){
		info=data;
		this.pos=pos;
	}
	/**
	 * write data into inner byte array
	 * @param d
	 * @return
	 */
	public boolean write(byte d){
		if(pointer<info.length){
			info[pointer]=d;
			pointer++;
			return true;
		}
		return false;
	}
	/**
	 * return the inner byte array
	 * @return
	 */
	public byte[] read(){
		return info;
	}
	/**
	 * return the length of inner byte array
	 * @return
	 */
	public int getLength() {
		return info.length;
	}
	/**
	 * return the location in the array
	 * @return String
	 */
	public String toString(){
		return ""+pos;
	}
}

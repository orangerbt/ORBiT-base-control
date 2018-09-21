package fixedGeneralPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;

public class ServiceDiscoverUDPSocket implements AutoCloseable{
	private boolean isAlive=true;
	private String uuid;
	private Thread receiver=new Thread() {
		public void run() {
			while(isAlive) {
				byte[] buffer=new byte[2048];
				DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
				try {
					ds.receive(packet);
					byte tempBuff[]=packet.getData();
					
					if(((new String(tempBuff)).trim().split("@"))[2].equals(uuid))continue;
					else {
						HashMap<String, Object> map=new HashMap<String, Object>();
						map.put("data", tempBuff);
						map.put("address", packet.getAddress());
						map.put("port", packet.getPort());
						for(ReceiveListener l:rlList) {
							l.action(map);
						}
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
	};

	private int port;
	private DatagramSocket ds;
	private Timer timer=new Timer();
	private ArrayList<ReceiveListener> rlList=new ArrayList<ReceiveListener>();
	private ArrayList<String> serviceList=new ArrayList<String>();
	/**
	 * Constructor
	 * @param ds
	 */
	public ServiceDiscoverUDPSocket(DatagramSocket ds) {
		this.ds=ds;
		receiver.start();
		uuid=UUID.randomUUID().toString();
	}
	/**
	 * Constructor
	 * @param port
	 * @throws SocketException
	 */
	public ServiceDiscoverUDPSocket(int port) throws SocketException {
		ds=new DatagramSocket(port);
		receiver.start();
		uuid=UUID.randomUUID().toString();
	}
	/**
	 * add a service to available service list
	 * @param service
	 * @param isHost
	 */
	public void addService(String service,boolean isHost) {
		if(!serviceList.contains(service+isHost)) {
			ReceiveListener rl=new ReceiveListener() {

				@Override
				public boolean verify(byte[] data, InetAddress ip, int port) {
					// TODO Auto-generated method stub
					String[] info=(new String(data)).trim().split("@");
					if(info.length<2)return false;
					if(info[0].equals(service)) {
						return true;
					}
					return false;
				}

				@Override
				public void process(byte[] data, InetAddress ip, int port) {
					// TODO Auto-generated method stub
					String[] info=(new String(data)).trim().split("@");
//					System.out.println(ds.getLocalPort()+" process");
					if(info[1].equals("host")&&!isHost) {
						try {
							final String sendingMessege=service+"@"+"peer"+"@"+uuid;
//							DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),InetAddress.getByName(info[3]), ds.getLocalPort());
							DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),ip, port);
							ds.send(packet);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(info[1].equals("peer")&&isHost) {
						try {
							final String sendingMessege=service+"@"+"host"+"@"+uuid;
//							DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),InetAddress.getByName(info[3]), ds.getLocalPort());
							DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),ip, port);
							ds.send(packet);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			};
			rlList.add(rl);
			serviceList.add(service+isHost);
		}
	}
	/**
	 * Add a service to available service list and run a listener when a service request is received
	 * @param service
	 * @param isHost
	 * @param l
	 */
	public void addService(String service,boolean isHost,ReceiveListener l) {
		ReceiveListener rl=new ReceiveListener() {

			@Override
			public boolean verify(byte[] data, InetAddress ip, int port) {
				// TODO Auto-generated method stub
				String[] info=(new String(data)).trim().split("@");
				if(info.length<2)return false;
				if(info[0].equals(service)) {
					return true;
				}
				return false;
			}

			@Override
			public void process(byte[] data, InetAddress ip, int port) {
				// TODO Auto-generated method stub
				String[] info=(new String(data)).trim().split("@");
//				System.out.println(ds.getLocalPort()+" process");
				if(info[1].equals("host")&&!isHost) {
					try {
						final String sendingMessege=service+"@"+"peer"+"@"+uuid;
//						DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),InetAddress.getByName(info[3]), ds.getLocalPort());
						DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),ip, port);
						ds.send(packet);
						l.process(data, ip, port);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(info[1].equals("peer")&&isHost) {
					try {
						final String sendingMessege=service+"@"+"host"+"@"+uuid;
//						DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),InetAddress.getByName(info[3]), ds.getLocalPort());
						DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),ip, port);
						ds.send(packet);
						l.process(data, ip, port);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		};
		if(!serviceList.contains(service+isHost)) {
			rlList.add(rl);
			serviceList.add(service+isHost);
		}
	}
	/**
	 * Discover specific server from local network
	 * @param service
	 * @param isHost
	 */
	public void discoverService(String service,boolean isHost) {
		if(!serviceList.contains(service+isHost)) {
			return;
		}
		String messege=service+"@";
		if(isHost)
			messege+="host";
		else
			messege+="peer";
		final String sendingMessege=messege+"@"+uuid;;
		CounterTask ct=new CounterTask(10) {

			@Override
			void action() {
				// TODO Auto-generated method stub
				try {
//					DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),InetAddress.getByName("255.255.255.255"), ds.getLocalPort());
					DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),InetAddress.getByAddress(new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 }), port);
//					System.out.println(packet);
					ds.send(packet);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		timer.schedule(ct, 0,100);
	}
	/**
	 *  Discover specific server from local network
	 * @param service
	 * @param isHost
	 * @param port
	 * @param l
	 * @throws UnknownHostException
	 */
	public void discoverService(String service,boolean isHost,int port,ReceiveListener l) throws UnknownHostException {
		String messege=service+"@";
		if(isHost)
			messege+="host";
		else
			messege+="peer";
		final String sendingMessege=messege+"@"+uuid;;
		CounterTask ct=new CounterTask(10) {

			@Override
			void action() {
				// TODO Auto-generated method stub
				try {
//					DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),InetAddress.getByName("255.255.255.255"), ds.getLocalPort());
					DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),InetAddress.getByAddress(new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 }), port);
//					System.out.println(packet);
					ds.send(packet);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		timer.schedule(ct, 0,100);
		if(!serviceList.contains(service+isHost)) {
			rlList.add(l);
			serviceList.add(service+isHost);
		}
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		ds.close();
		timer.cancel();
		receiver.stop();
		
	}
}




//@Override
//public boolean verify(byte[] data) {
//	// TODO Auto-generated method stub
//	String[] info=(new String(data)).trim().split("@");
//	if(info.length<3)return false;
//	if(info[0].equals(service)) {
//		return true;
//	}
//	return false;
//}
//
//@Override
//public void process(byte[] data) {
//	// TODO Auto-generated method stub
//	String[] info=(new String(data)).trim().split("@");
//	if(info[1].equals("host")&&!isHost) {
//		try {
//			final String sendingMessege=service+"@"+"peer"+"@"+InetAddress.getLocalHost();
////			DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),InetAddress.getByName(info[3]), ds.getLocalPort());
//			DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),InetAddress.getByName(info[3]), port);
//			ds.send(packet);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}else if(info[1].equals("peer")&&isHost) {
//		try {
//			final String sendingMessege=service+"@"+"host"+"@"+InetAddress.getLocalHost();
////			DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),InetAddress.getByName(info[3]), ds.getLocalPort());
//			DatagramPacket packet=new DatagramPacket(sendingMessege.getBytes(), sendingMessege.length(),InetAddress.getByName(info[3]), port);
//			ds.send(packet);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}

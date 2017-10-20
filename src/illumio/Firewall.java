package illumio;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class Firewall {
	
	private HashMap<String, HashMap<Integer, Integer>> ports = new HashMap<>();
	private HashMap<String, HashMap<Integer, Integer>> ips = new HashMap<>();
	
	public Firewall() {}
	
	// store ip-addresses and ports into private data members for future lookup
	public Firewall(String filePath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			
			String line = null;
			
			while((line = br.readLine()) != null) {
				
				String[] packets = line.split(",");				
				String directionProtocol = packets[0] + ":" + packets[1];
				
				storePorts(directionProtocol, packets[2]);
				storeIpAddresses(directionProtocol, packets[3]);				
			}
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// constant time complexity, very fast
	public boolean acceptPacket(String direction, String protocol, int port, String ip_address) {
		
		String directionProtocol = direction + ":" + protocol;
		
		if (!ports.containsKey(directionProtocol)) {
			return false;
		}
		else {
			if (!ports.get(directionProtocol).containsKey(port)) {
				return false;
			}
			else if (!ips.containsKey(directionProtocol)){
				return false;
			}
			else {
				try {
					// convert ip-address to integer
					InetAddress inet = InetAddress.getByName(ip_address);
					int ipInt = ByteBuffer.wrap(inet.getAddress()).getInt();
					
					if (!ips.get(directionProtocol).containsKey(ipInt)) {
						return false;
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	
	private void storePorts(String directionProtocol, String portNumber) {
		
		HashMap<Integer, Integer> portMap = new HashMap<>();
		if (ports.containsKey(directionProtocol)) {
			portMap = ports.get(directionProtocol);					
		}
		
		if (portNumber.contains("-")) {
			int fromPort = Integer.parseInt(portNumber.split("-")[0]);
			int toPort = Integer.parseInt(portNumber.split("-")[1]);
			
			for (int i = fromPort; i <= toPort; i++) {
				portMap.put(i, i);
			}
		}
		else {
			portMap.put(Integer.parseInt(portNumber), Integer.parseInt(portNumber));
		}	
		
		ports.put(directionProtocol, portMap);
	}
	
	private void storeIpAddresses(String directionProtocol, String ipAddresses) {
		
		HashMap<Integer, Integer> ipMap = new HashMap<>();
		if (ips.containsKey(directionProtocol)) {
			ipMap = ips.get(directionProtocol);					
		}
		
		try {
			if (ipAddresses.contains("-")) {
				String startIP = ipAddresses.split("-")[0];
				String endIP = ipAddresses.split("-")[1];
				
				// convert ip-address to integer
				InetAddress inetStart = InetAddress.getByName(startIP);
				int fromIP = ByteBuffer.wrap(inetStart.getAddress()).getInt();
				
				// convert ip-address to integer
				InetAddress inetEnd = InetAddress.getByName(endIP);
				int toIP = ByteBuffer.wrap(inetEnd.getAddress()).getInt();
				
				for (int i = fromIP; i <= toIP; i++) {
					ipMap.put(i, i);
				}
			}
			else {
				InetAddress inet = InetAddress.getByName(ipAddresses);
				int ipAddress = ByteBuffer.wrap(inet.getAddress()).getInt();
				ipMap.put(ipAddress, ipAddress);
			}	
			
			ips.put(directionProtocol, ipMap);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
}

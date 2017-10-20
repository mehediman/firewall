package illumio;

public class TestProgram {

	public static void main(String[] args) {
		
		Firewall firewall = new Firewall("fw.csv");
		
		boolean result = firewall.acceptPacket("inbound", "tcp", 80, "192.168.1.2");
		System.out.println(result);
		
		result = firewall.acceptPacket("inbound", "udp", 53, "192.168.2.1");
		System.out.println(result);
		
		result = firewall.acceptPacket("outbound", "tcp", 10234, "192.168.10.11");
		System.out.println(result);
		
		result = firewall.acceptPacket("inbound", "tcp", 81, "192.168.1.2");
		System.out.println(result);
		
		result = firewall.acceptPacket("inbound", "udp", 24, "52.12.48.92");
		System.out.println(result);
	}

}

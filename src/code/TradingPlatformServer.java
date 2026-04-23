package code;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class TradingPlatformServer {
	 

	public static void main(String[] args) {
		ArrayList<Account> accounts = new ArrayList<Account>();
		Account dummy1 = new Account("wBuffet", "19300830".toCharArray(), 2000000 );
		Account dummy2 = new Account("dTrump", "19460614".toCharArray(), 3000000 );
		Account dummy3 = new Account("jPowell", "19530204".toCharArray(), 4000000 );
		dummy1.getEquityList().add(new Stock("AAPL", 251.2, 100));
		dummy2.getEquityList().add(new Stock("NVDA", 171, 50));
		dummy3.getEquityList().add(new Stock("TSM", 328, 60));
		accounts.add(dummy1);
		accounts.add(dummy2);
		accounts.add(dummy3);
		
		try (ServerSocket socket = new ServerSocket(Protocol.PORT)) {
			System.out.println("Awaiting Connections");
			while(true) {
				Socket s = socket.accept();
				TradingService ts = new TradingService(s, accounts);
				Thread t = new Thread(ts);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

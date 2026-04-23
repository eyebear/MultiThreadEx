package code;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", Protocol.PORT);
				Scanner console = new Scanner(System.in)){
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			DataInputStream input = new DataInputStream(in);
			DataOutputStream output = new DataOutputStream(out);
			while(true) {
				String reply = input.readUTF();
				System.out.println(reply);
				if(reply.equalsIgnoreCase("Your Session is terminated")) {
					break;
				}
				String command = console.nextLine();
				output.writeUTF(command);
				output.flush();
			}
			System.out.println("Client is terminated");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

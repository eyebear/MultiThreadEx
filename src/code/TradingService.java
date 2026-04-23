package code;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class TradingService implements Runnable{
	private Socket socket;
	private ArrayList<Account> accounts;
	private DataInputStream input;
	private DataOutputStream output;
	private Account currentAccount;
	public TradingService(Socket socket, ArrayList<Account> accounts) {
		// TODO Auto-generated constructor stub
		this.socket=socket;
		this.accounts=accounts;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try(Socket s=socket) {
			this.input = new DataInputStream(s.getInputStream());
			this.output = new DataOutputStream(s.getOutputStream());
			runService();
			System.out.println("Session Thread is finished");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void runService() throws IOException {
		// TODO Auto-generated method stub
		boolean quit = false;
		String msg = "";
		while(!quit) {
			System.out.println("Enter Loop 1");
			msg += "Enter LOGIN or QUIT:";
			output.writeUTF(msg);
			output.flush();
			msg = "";
			String command = input.readUTF();
			if(command.equalsIgnoreCase(Protocol.LOGIN)) {
				msg+="Enter Username:";
				output.writeUTF(msg);
				output.flush();
				msg="";
				String username = input.readUTF();
				msg+="Enter Password: ";
				output.writeUTF(msg);
				output.flush();
				msg="";
				char[] password = input.readUTF().toCharArray();
				if(validateLogin(username, password)) {
					boolean userQuit=false;
					while(!userQuit) {
						msg+="You are logged in! What can we help you today? (Enter: DEPOSIT/WITHDRAW/BUY/SELL/LOGOUT) \n"+currentAccount.getSummary();
						output.writeUTF(msg);
						output.flush();
						msg="";
						
						command = input.readUTF();
						if(command.equalsIgnoreCase(Protocol.DEPOSIT)) {
							output.writeUTF("Enter The Amount to DEPOSIT: ");
							output.flush();
							double amount = Double.parseDouble(input.readUTF());

							if(currentAccount.deposit(amount)) {
								msg+="Deposit successful \n";
							}else {
								msg+="Deposit FAILED \n";
							}
							System.out.println("Deposit finised");
						}else if(command.equalsIgnoreCase(Protocol.WITHDRAW)) {
							output.writeUTF("Enter The Amount to WITHDRAW: ");
							output.flush();
							double amount = Double.parseDouble(input.readUTF());
							if(currentAccount.withdraw(amount)) {
								msg+="Withdraw successful \n";
							}else {
								msg+="Withdraw FAILED \n";
							}
						}else if(command.equalsIgnoreCase(Protocol.BUY)) {
							output.writeUTF("Enter symbol of the stock: ");
							output.flush();
							String symbol = input.readUTF();
							output.writeUTF("Enter quantity you wish to buy: ");
							output.flush();
							int quantity = Integer.parseInt(input.readUTF());
							output.writeUTF("Enter the unit price of the stock: ");
							output.flush();
							double price = Double.parseDouble(input.readUTF());
							if(currentAccount.buyStock(symbol, quantity, price)) {
								msg+="Purchase Successful \n";
							}else {
								msg+="Purchase FAILED \n";
							}
						}else if(command.equalsIgnoreCase(Protocol.SELL)) {
							output.writeUTF("Enter symbol of the stock: ");
							output.flush();
							String symbol = input.readUTF();
							output.writeUTF("Enter quantity you wish to sell: ");
							output.flush();
							int quantity = Integer.parseInt(input.readUTF());
							output.writeUTF("Enter the unit price of the stock: ");
							output.flush();
							double price = Double.parseDouble(input.readUTF());
							if(currentAccount.sellStock(symbol, quantity, price)) {
								msg+="Selling "+symbol+" Successfully \n";

							}else {
								msg+="Selling "+symbol+" FAILED \n";
							}
						}else if(command.equalsIgnoreCase(Protocol.LOGOUT)) {
							msg+="You have logged out\n";
							userQuit=true;
						}else {
							msg+="Invalid Input, Use Valid Commands";
						}
					}
				}else {
					System.out.println("Invalid Username&Password");
					msg+="Your username and password combination does not match out record!\n";
				}
			}else if(command.equalsIgnoreCase("QUIT")) {
				output.writeUTF("Your Session is terminated");
				output.flush();
				System.out.println("Session Terminated");
				quit=true;
			}else {
				System.out.println("Invalid Input");
				msg+="Your input is invalid\n";
			}
		}
	}

	private boolean validateLogin(String username, char[] password) {
		Iterator<Account> it = accounts.iterator();
		while(it.hasNext()) {
			this.currentAccount=it.next();
			if(this.currentAccount.getUsername().equals(username)) {
				return Arrays.equals(this.currentAccount.getPassword(), password);
			}
		}
		return false;
	}

}

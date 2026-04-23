package code;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The data class keeping a record for each user's money balance and stock list
 * @author aoaofeng
 * */
public class Account {
	private String username;
	private char[] password;
	private double balance;
	private ArrayList<Stock> equityList;
	public Account(String username, char[] password, double balance) {
		this.username=username;
		this.password=password;
		this.balance=balance;
		this.equityList=new ArrayList<Stock>();
	}
	
	/**
	 * @return	the current user account balance in dollars
	 * */
	public double getBalance() {
		return balance;
	}
	/**
	 * @return return the current user's name
	 * */
	public String getUsername() {
		return this.username;
	}
	/**
	 * @return return the current user's password as a char array
	 * */
	public char[] getPassword() {
		return this.password;
	}
	/**
	 * Purchase a stock for the user
	 * @param stockname: the name of the stock to buy
	 * @param quantity: the quantity of the stock to buy
	 * @param price: the purchase price
	 * @return the status of the purchase, true if successful, false otherwise
	 * */
	public boolean buyStock(String stockname, int quantity, double price) {
		
		double newBalance = this.balance - quantity*price;
		
		if(newBalance>=0) {
			this.balance=newBalance;
			Stock stockToBuy = retrieveStock(stockname);
			if(stockToBuy==null) {
				this.equityList.add(new Stock(stockname, price, quantity));
			}else {
				int currentQuantity = stockToBuy.getQuantity();
				double newBookPrice = (stockToBuy.getBookCostPrice()*currentQuantity+price*quantity)/(currentQuantity+quantity);
				stockToBuy.setQuantity(quantity+currentQuantity);
				stockToBuy.setBookCostPrice(newBookPrice);
			}
			return true;
		}else {
			System.out.println("Insufficient Fund");
			return false;
		}
	}
	/**
	 * Sell a stock for the user
	 * @param stockname: the name of the stock to sell
	 * @param quantity: the quantity of the stock to sell
	 * @param price: the selling price
	 * @return the status of the sell, true if successful, false otherwise
	 * */
	public boolean sellStock(String stockName, int quantity, double price) {
		if(validateSell(stockName, quantity, price)) {
			this.balance+=quantity*price;
			Stock stockToSell = retrieveStock(stockName);
			if(stockToSell.getQuantity()>quantity) {
				int newQuantity = stockToSell.getQuantity()-quantity;
				int currentQuantity = stockToSell.getQuantity();
				double newBookPrice = (stockToSell.getBookCostPrice()*currentQuantity-price*quantity)/(newQuantity);
				stockToSell.setBookCostPrice(newBookPrice);
				stockToSell.setQuantity(newQuantity);
			}else {
				equityList.remove(stockToSell);
			}
			return true;
		}else {
			System.out.println("Not Enough Stock");
			return false;
		}
	}
	/**
	 * Find and return the stock object
	 * @param stockName: the stock object to retrieve from the list
	 * @return	the stock object from the stock list
	 * */
	private Stock retrieveStock(String stockName) {
		Iterator<Stock> it = this.equityList.iterator();
		while(it.hasNext()) {
			Stock current = it.next();
			if(current.getSymbol().equalsIgnoreCase(stockName)) {
				return current;
			}
		}
		return null;
	}
	/**
	 * Check if the selling is valid, i.e. stock exists in the list, quantity is enough, and price not negative
	 * @param stockname: the name of the stock to sell
	 * @param quantity: the quantity of the stock to sell
	 * @param price: the selling price
	 * @return if this sell order is valid, true if valid, false otherwise
	 * */
	private boolean validateSell(String stockName, int quantity, double price) {
			Stock current = retrieveStock(stockName);
			return price>=0&&quantity>0&&current!=null&&current.getQuantity()>=quantity;

	}
	/**
	 * Deposit money for the user
	 * @param amount: the amount of money to add to the balance
	 * @return the status of the deposit, true if successful, false otherwise
	 * */
	public boolean deposit(double amount) {
		if(amount>=0) {
			this.balance+=amount;
			return true;
		}else {
			System.out.println("Invalid amount");
			return false;
		}
	}
	/**
	 * Withdraw money for the user
	 * @param amount: the amount of money to deduct from the balance
	 * @return the status of the withdrawal, true if successful, false otherwise
	 * */
	public boolean withdraw(double amount) {
		if(amount<0||(this.balance-amount)<0) {
			System.out.println("Invalid amount");
			return false;
		}else {
			this.balance-=amount;
			return true;
		}
	}
	/**
	 * @return return a string representation of the current user, including name, balance, stock list
	 * */
	public String getSummary() {
		String output = this.username+" balance: "+this.balance+"\n";
		Iterator<Stock> it = this.equityList.iterator();
		while(it.hasNext()) {
			Stock current=it.next();
			output+=current.getSymbol()+" quantity: "+current.getQuantity()+" book price: "+current.getBookCostPrice()+"\n";
		}
		return output;
	}
	
	/**
	 * Helper method to add dummy data to the account
	 * */
	public ArrayList<Stock> getEquityList(){
		return this.equityList;
	}

}

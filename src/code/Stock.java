package code;

public class Stock{

	private final String symbol;
	private double bookCostPrice;
	private int quantity;

	public Stock(String symbol, double currentPrice, int quantity) {
		this.symbol = symbol;
		this.bookCostPrice = currentPrice;
		this.quantity=quantity;
	}

	/**
	 * @return	return the symbol of the stock, i.e. AAPL, NVDA, etc.
	 * */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @return	return the average cost of the stock
	 * */
	public double getBookCostPrice() {
		return bookCostPrice;
	}

	/**
	 * @param	currentPrice: set the average cost of the stock as currentPrice
	 * */
	public void setBookCostPrice(double currentPrice) {
		if (currentPrice < 0.0) {
			this.bookCostPrice=0;
		}
		this.bookCostPrice = currentPrice;
	}

	/**
	 * @return	return the quantity of the stock held by the user
	 * */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param	quantity: set the current quantity of the stock held by the user
	 * */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
package training.pega.place_order.model;

public class CardDetails {
	private String cardType;
	private int securityCode;
	private long cardNumber;
	private int expiryMonth;
	private int expiryYear;

	public CardDetails(String cardType, int securityCode, long cardNumber, int expiryMonth, int expiryYear) {
		this.cardType = cardType;
		this.securityCode = securityCode;
		this.cardNumber = cardNumber;
		this.expiryMonth = expiryMonth;
		this.expiryYear = expiryYear;
	}

	public CardDetails() {
		cardType = "Visa";
		securityCode = 777;
		cardNumber = 123456789;
		expiryMonth = 6;
		expiryYear = 2018;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public int getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(int securityCode) {
		this.securityCode = securityCode;
	}

	public long getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getExpiryMonth() {
		return expiryMonth;
	}

	public void setExpiryMonth(int expiryMonth) {
		this.expiryMonth = expiryMonth;
	}

	public int getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(int expiryYear) {
		this.expiryYear = expiryYear;
	}

}

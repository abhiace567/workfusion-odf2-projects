package training.pega.place_order.model;

public class BillingDetails {

	private String firstName;
	private String lastName;
	private String streetAddress;
	private long zipCode;
	private long areaCode;
	private long phone;

	public BillingDetails(String firstName, String lastName, String streetAddress, long zipCode, long areaCode,
			long phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.streetAddress = streetAddress;
		this.zipCode = zipCode;
		this.areaCode = areaCode;
		this.phone = phone;
	}

	public BillingDetails() {
		firstName = "Mike";
		lastName = "Alex";
		streetAddress = "some address";
		zipCode = 12345l;
		areaCode = 789l;
		phone = 987654321l;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public long getZipCode() {
		return zipCode;
	}

	public void setZipCode(long zipCode) {
		this.zipCode = zipCode;
	}

	public long getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(long areaCode) {
		this.areaCode = areaCode;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

}

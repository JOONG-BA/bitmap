package org.dfpl.lecture.db.bitmap;

public class Loan {

	private String customerName;
	private String street;
	private String city;
	private String loanNumber;
	private String branchName;
	private String amount;

	public Loan(String customerName, String street, String city, String loanNumber, String branchName, String amount) {
		super();
		this.customerName = customerName;
		this.street = street;
		this.city = city;
		this.loanNumber = loanNumber;
		this.branchName = branchName;
		this.amount = amount;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLoanNumber() {
		return loanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "(" + customerName + "," + loanNumber + ")";
	}

}

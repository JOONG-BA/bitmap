package org.dfpl.lecture.db.bitmap;

public class Assignment4 {
	
	public static void main(String[] args) {
	
		LoanManager loanManager = new LoanManager();
		loanManager.addLast(new Loan("Adams", "Spring", "Pittsfield", "L-16", "Perryridge", "1300"));
		loanManager.addLast(new Loan("Curry", "North", "Rye", "L-93", "Mianus", "500"));
		loanManager.addLast(new Loan("Jones", "MAIN", "Harrison", "L-17", "Downtown", "1000"));
		loanManager.addLast(new Loan("Smith", "North", "Rye", "L-11", "Round Hill", "900"));
		loanManager.addLast(new Loan("Smith",  "North", "Rye", "L-23", "Redwood", "2000"));
		loanManager.addLast(new Loan("Williams", "Nassau", "Princeton", "L-17", "Downtown", "1000"));
		
		System.out.println("[1]");
		System.out.println(loanManager.WhereAND("North", null, "Round Hill"));
		System.out.println(loanManager.WhereOR(null, "Harrison", "Perryridge"));
		
		loanManager.popFirst();
		loanManager.popFirst();
		loanManager.popFirst();
		loanManager.addLast(new Loan("Adams", "Spring", "Pittsfield", "L-16", "Perryridge", "1300"));
		loanManager.addLast(new Loan("Curry", "North", "Rye", "L-93", "Mianus", "500"));
		loanManager.addLast(new Loan("Jones", "MAIN", "Harrison", "L-17", "Downtown", "1000"));
		
		System.out.println("[2]");
		System.out.println(loanManager.WhereAND("North", null, "Round Hill"));
		System.out.println(loanManager.WhereOR(null, "Harrison", "Perryridge"));
	}
}

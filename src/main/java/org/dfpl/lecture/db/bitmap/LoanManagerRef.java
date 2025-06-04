package org.dfpl.lecture.db.bitmap;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

public class LoanManagerRef {

	private ArrayList<Loan> data;

	// 후에 MyBitSet으로 변경하여 테스트
	// 코드 내에 MyBitSet의 구현을 이용하여 map을 초기화하세요
	private HashMap<String, BitSet> streetBitMap;
	private HashMap<String, BitSet> cityBitMap;
	private HashMap<String, BitSet> branchNameBitMap;

	public LoanManagerRef() {
		data = new ArrayList<Loan>();
	}

	public ArrayList<Loan> getData() {
		return data;
	}

	public void printStreetBitMap() {
		System.out.println(streetBitMap);
	}

	public void printCityBitMap() {
		System.out.println(cityBitMap);
	}

	public void printBranchNameBitMap() {
		System.out.println(branchNameBitMap);
	}

	public void addLast(Loan loan) {
		data.add(loan);
		updateBitMap();
	}

	public Loan popFirst() {
		Loan removed = data.removeFirst();
		updateBitMap();
		return removed;
	}

	public void updateBitMap() {
		// TODO

		streetBitMap = new HashMap<String, BitSet>();

		cityBitMap = new HashMap<String, BitSet>();

		branchNameBitMap = new HashMap<String, BitSet>();

		for (Loan v : data) {
			streetBitMap.put(v.getStreet(), new BitSet(data.size()));
			cityBitMap.put(v.getCity(), new BitSet(data.size()));
			branchNameBitMap.put(v.getBranchName(), new BitSet(data.size()));
		}

		for (int i = 0; i < data.size(); i++) {
			Loan v = data.get(i);
			String street = v.getStreet();
			streetBitMap.get(street).set(i);
			String city = v.getCity();
			cityBitMap.get(city).set(i);
			String branch = v.getBranchName();
			branchNameBitMap.get(branch).set(i);
		}
	}

	public List<Loan> WhereAND(String street, String city, String branchName) {
		// SELECT * FROM loan WHERE customer_street = street AND customer_city = city
		// AND branch_name = branchName;
		// TODO

		BitSet base = new BitSet(data.size());
		base.set(0, data.size());
		if (street != null) {
			base.and(streetBitMap.get(street));
		}
		if (city != null) {
			base.and(cityBitMap.get(city));
		}
		if (branchName != null) {
			base.and(branchNameBitMap.get(branchName));
		}

		List<Loan> list = new ArrayList<>();
		for (int i = base.nextSetBit(0); i >= 0; i = base.nextSetBit(i + 1)) {
			list.add(data.get(i));
		}

		return list;
	}

	public List<Loan> WhereOR(String street, String city, String branchName) {
		// SELECT * FROM loan WHERE customer_street = street AND customer_city = city
		// AND branch_name = branchName;
		// TODO

		BitSet base = new BitSet(data.size());
		if (street != null) {
			base.or(streetBitMap.get(street));
		}
		if (city != null) {
			base.or(cityBitMap.get(city));
		}
		if (branchName != null) {
			base.or(branchNameBitMap.get(branchName));
		}

		List<Loan> list = new ArrayList<>();
		for (int i = base.nextSetBit(0); i >= 0; i = base.nextSetBit(i + 1)) {
			list.add(data.get(i));
		}

		return list;
	}

}

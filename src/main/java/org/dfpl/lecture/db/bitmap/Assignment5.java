package org.dfpl.lecture.db.bitmap;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import com.github.javafaker.Faker;

public class Assignment5 {

	public static void main(String[] args) {
		Faker faker = new Faker();
		LoanManager loan = new LoanManager();
		int count = 0;
		Random r = new Random();

		HashSet<String> nameSet = new HashSet<String>();
		HashSet<String> streetSet = new HashSet<String>();
		HashSet<String> citySet = new HashSet<String>();
		HashSet<String> branchSet = new HashSet<String>();

		for (int i = 0; i < 400; i++) {
			nameSet.add(faker.name().fullName());
			streetSet.add(faker.address().streetName());
			citySet.add(faker.address().cityName());
			branchSet.add(faker.company().name());
		}

		List<String> names = nameSet.parallelStream().limit(100).toList();
		List<String> streets = streetSet.parallelStream().limit(100).toList();
		List<String> cities = citySet.parallelStream().limit(100).toList();
		List<String> branches = branchSet.parallelStream().limit(100).toList();

		for (int i = 0; i < 400; i++) {
			if (i % 50 == 0) {
				System.out.println("...");
			}
			loan.addLast(new Loan(names.get(r.nextInt(100)), streets.get(r.nextInt(100)), cities.get(r.nextInt(100)),
					"L-" + (count++), branches.get(r.nextInt(100)), String.valueOf(r.nextInt(10000))));
		}

		System.out.println("---street---");
		loan.printStreetBitMap();
		System.out.println("---city---");
		loan.printCityBitMap();
		System.out.println("---branch_name---");
		loan.printBranchNameBitMap();

		try {
			int cnt = 0;
			while (true) {
				if (cnt++ % 50 == 0) {
					System.out.println("...");
				}
				loan.popFirst();
			}
		} catch (NoSuchElementException e) {

		}
		System.out.println("---street---");
		loan.printStreetBitMap();
		System.out.println("---city---");
		loan.printCityBitMap();
		System.out.println("---branch_name---");
		loan.printBranchNameBitMap();

	}
}
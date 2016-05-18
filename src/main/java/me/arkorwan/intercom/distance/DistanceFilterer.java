package me.arkorwan.intercom.distance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.arkorwan.intercom.distance.Customer.ParseException;

public class DistanceFilterer {

	/**
	 * Read a file and parse each line into a Customer.
	 * 
	 * @param filepath
	 *            path to the customer text file.
	 * @return a List of Customers.
	 */
	List<Customer> readCustomersFromFile(Path filepath) {
		List<String> lines;
		try {
			lines = Files.readAllLines(filepath);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}

		List<Customer> customers = new ArrayList<>();
		for (String json : lines) {
			try {
				customers.add(Customer.fromJson(json));
			} catch (ParseException e) {
				// skip invalid lines
				e.printStackTrace();
			}
		}
		return customers;
	}

	/**
	 * Filter for customers whose coordinate are within the given range from the
	 * given Coordinate.
	 * 
	 * @param customers
	 *            a List of Customers.
	 * @param centre
	 *            a Coordinate
	 * @param radiusKM
	 *            a distance to filter the customers, in kilometers.
	 * @return a List of Customers within the given range, sorted by their user
	 *         ids.
	 */
	public List<Customer> filterAndSortCustomersWithinRadius(
			List<Customer> customers, Coordinate centre, double radiusKM) {
		Stream<Customer> filtered = customers.stream()
				.filter(customer -> customer.getCoordinate()
						.distanceKM(centre) <= radiusKM);
		Stream<Customer> sorted = filtered.sorted(
				(cus1, cus2) -> Integer.compare(cus1.userId, cus2.userId));
		return sorted.collect(Collectors.toList());
	}

	public static void main(String[] args) {

		// the given coordinate and range
		double filterDistance = 100;
		Coordinate centre = Coordinate.of(53.3381985, -6.2592576);

		Path filepath = Paths.get(DistanceFilterer.class.getClassLoader()
				.getResource("gistfile1.txt").getPath());

		DistanceFilterer filterer = new DistanceFilterer();

		// read customers from file
		List<Customer> allCustomers = filterer.readCustomersFromFile(filepath);
		// filter and sort
		List<Customer> filteredCustomers = filterer
				.filterAndSortCustomersWithinRadius(allCustomers, centre,
						filterDistance);

		// output to stdout
		for (Customer customer : filteredCustomers) {
			System.out.println(
					String.format("%d, %s", customer.userId, customer.name));
		}

	}

}

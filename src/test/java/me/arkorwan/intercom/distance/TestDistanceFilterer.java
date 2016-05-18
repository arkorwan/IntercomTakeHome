package me.arkorwan.intercom.distance;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.testng.annotations.Test;

import com.beust.jcommander.internal.Lists;

import me.arkorwan.intercom.distance.Coordinate;
import me.arkorwan.intercom.distance.Customer;
import me.arkorwan.intercom.distance.DistanceFilterer;

public class TestDistanceFilterer {

	DistanceFilterer filterer = new DistanceFilterer();

	// distance large enough to allow any coordinates to pass filtering.
	static final double NO_FILTER = 1000000;

	@Test
	public void testSorted() {

		// create customer with random permutation of user ids.
		List<Integer> userIds = IntStream.range(0, 10).boxed()
				.collect(Collectors.toList());
		Collections.shuffle(userIds);

		List<Customer> customers = new ArrayList<>();
		for (int id : userIds) {
			Customer c = new Customer();
			c.userId = id;
			customers.add(c);
		}

		// filter and sort
		List<Customer> sorted = filterer.filterAndSortCustomersWithinRadius(
				customers, Coordinate.of(0, 0), NO_FILTER);

		// should be sorted
		assertEquals(sorted.size(), userIds.size());
		for (int i = 0; i < sorted.size() - 1; i++) {
			assertTrue(sorted.get(i).userId < sorted.get(i + 1).userId);
		}

	}

	// create mock customer object that returns a given distance from the given
	// coordinate
	private Customer mockCustomerForFilter(Coordinate centre,
			double mockDistance) {
		Customer customer = mock(Customer.class);
		Coordinate coord = mock(Coordinate.class);
		when(customer.getCoordinate()).thenReturn(coord);
		when(coord.distanceKM(centre)).thenReturn(mockDistance);
		return customer;
	}

	@Test
	public void testFilter() {

		Coordinate centre = Coordinate.of(0, 0);
		double radius = 100;

		// customer just inside the range
		Customer insideCustomer = mockCustomerForFilter(centre, radius - 0.1);
		// customer just outside the range
		Customer outsideCustomer = mockCustomerForFilter(centre, radius + 0.1);

		List<Customer> customers = Lists.newArrayList(insideCustomer,
				outsideCustomer);
		// filter
		List<Customer> filtered = filterer
				.filterAndSortCustomersWithinRadius(customers, centre, radius);

		// only the 'just inside' customer should remain.
		assertEquals(filtered.size(), 1);
		assertSame(filtered.get(0), insideCustomer);

	}

}

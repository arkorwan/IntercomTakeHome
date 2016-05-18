package me.arkorwan.intercom.distance;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import me.arkorwan.intercom.distance.Customer;
import me.arkorwan.intercom.distance.Customer.ParseException;

public class TestCustomer {

	static final double e = 0.0001;

	// test parsing Customer from json strings

	// same customer, with json fields in different ordering
	@DataProvider
	public Object[][] jsonCustomerProvider() {
		return new Object[][] {
				{ "{\"latitude\": \"1.0\", \"user_id\": 2, \"name\": \"Hello World\", \"longitude\": \"0.1\"}" },
				{ "  {\"user_id\": 2, \"name\": \"Hello World\", \"longitude\": \"0.1\", \"latitude\": \"1.0\"}  " },
				{ "{\"name\": \"Hello World\", \"longitude\": \"0.1\", \"latitude\": \"1.0\", \"user_id\": 2}" },
				{ "{\"longitude\": \"0.1\", \"latitude\": \"1.0\", \"user_id\": 2, \"name\": \"Hello World\"}" } };
	}

	@Test(dataProvider = "jsonCustomerProvider")
	public void testParseJsonCustomer(String json) throws ParseException {

		Customer customer = Customer.fromJson(json);
		assertEquals(customer.latitude, 1.0, e);
		assertEquals(customer.longitude, 0.1, e);
		assertEquals(customer.userId, 2);
		assertEquals(customer.name, "Hello World");
	}

	// negative cases - malformed or invalid data type/range.

	@DataProvider
	public Object[][] invalidJsonCustomerProvider() {
		return new Object[][] { { "{\"latitude\": \"1.0\", " },
				{ "\"latitude\": \"1.0\", \"user_id\": 2, \"name\": \"Hello World\", \"longitude\": \"0.1\"" },
				{ "\"latitude\": \"1.0N\", \"user_id\": 2, \"name\": \"Hello World\", \"longitude\": \"0.1E\"" },
				{ "\"latitude\": \"100\", \"user_id\": 2, \"name\": \"Hello World\", \"longitude\": \"400\"" }, };
	}

	@Test(expectedExceptions = Customer.ParseException.class, dataProvider = "invalidJsonCustomerProvider")
	public void testInvalidJsonCustomer(String json) throws ParseException {
		Customer.fromJson(json);
	}

}

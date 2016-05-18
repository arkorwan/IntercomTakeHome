package me.arkorwan.intercom.distance;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

/**
 * A POJO for Customer.
 * 
 */
public class Customer {

	public static class ParseException extends Exception {

		public ParseException(RuntimeException e) {
			// TODO Auto-generated constructor stub
		}

	}

	double latitude;

	double longitude;

	@SerializedName("user_id")
	int userId;

	String name;

	static Gson gson = new Gson();

	/**
	 * Create a new Customer object from a given json String. Use GSON library
	 * to parse.
	 * 
	 * @param json
	 *            a json string describing a customer.
	 * @return a new Customer described by the string.
	 * @throws ParseException
	 */
	public static Customer fromJson(String json) throws ParseException {
		try {
			Customer customer = gson.fromJson(json, Customer.class);
			return customer;
		} catch (JsonSyntaxException | IllegalArgumentException e) {
			throw new ParseException(e);
		}

	}

	/**
	 * @return a Coordinate object for this Customer.
	 */
	public Coordinate getCoordinate() {
		return Coordinate.of(latitude, longitude);
	}

}

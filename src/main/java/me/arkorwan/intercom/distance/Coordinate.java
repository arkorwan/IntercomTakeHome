package me.arkorwan.intercom.distance;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import java.util.Objects;

public class Coordinate {

	private double latitude;
	private double longitude;

	static final double EARTH_R = 6371.0; // km

	/**
	 * Create a Coordinate from lat/lng, with validation.
	 * 
	 * @param latitude
	 *            must be within [-90, 90]
	 * @param longitude
	 *            must be within [-180, 180]
	 * @return a new Coordinate with the given latitude/longitude
	 */
	public static Coordinate of(double latitude, double longitude) {
		if (abs(latitude) > 90) {
			throw new IllegalArgumentException(
					"latitude must be within range [-90, 90]");
		}
		if (abs(longitude) > 180) {
			throw new IllegalArgumentException(
					"longitude must be within range [-180, 180]");
		}

		Coordinate coord = new Coordinate();
		coord.latitude = latitude;
		coord.longitude = longitude;
		return coord;
	}

	/**
	 * Calculate distance from this coordinate to another.
	 * 
	 * @param other
	 * @return distance, in kilometers.
	 */
	public double distanceKM(Coordinate other) {
		Objects.requireNonNull(other);

		double sinLat1 = sin(toRadians(this.latitude));
		double sinLat2 = sin(toRadians(other.latitude));
		double cosLat1 = cos(toRadians(this.latitude));
		double cosLat2 = cos(toRadians(other.latitude));
		double cosDeltaLng = cos(toRadians(other.longitude - this.longitude));

		double angle = acos(
				sinLat1 * sinLat2 + cosLat1 * cosLat2 * cosDeltaLng);

		return EARTH_R * angle;

	}
}

package me.arkorwan.intercom.distance;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import me.arkorwan.intercom.distance.Coordinate;

public class TestCoordinate {

	// allowed error
	static final double e = 0.0001;

	// test the distance calculation for some simple cases
	@DataProvider
	public Object[][] angleProvider() {
		return new Object[][] { { 1 }, { 45 }, { 90 }, { 179 }, { 180 } };
	}

	@Test
	public void testDistanceSamePoint() {
		Coordinate coord = Coordinate.of(0, 0);
		assertEquals(coord.distanceKM(coord), 0, e);
	}

	// fixed latitude, variable positive longitude
	@Test(dataProvider = "angleProvider")
	public void testPositiveLongitudeAtEquator(double degree) {
		assertEquals(Coordinate.of(0, 0).distanceKM(Coordinate.of(0, degree)),
				Math.toRadians(degree) * Coordinate.EARTH_R, e);
	}

	// fixed latitude, variable negative longitude
	@Test(dataProvider = "angleProvider")
	public void testNegativeLongitudeAtEquator(double degree) {
		assertEquals(Coordinate.of(0, 0).distanceKM(Coordinate.of(0, -degree)),
				Math.toRadians(degree) * Coordinate.EARTH_R, e);
	}

	// fixed longitude, variable latitude
	@Test(dataProvider = "angleProvider")
	public void testDistanceAtALongitude(double degree) {
		assertEquals(
				Coordinate.of(0, 0).distanceKM(Coordinate.of(degree - 90, 0)),
				Math.abs(Math.toRadians(degree - 90)) * Coordinate.EARTH_R, e);
	}

	// negative cases - null and out-of-range coordinates
	
	@Test(expectedExceptions = NullPointerException.class)
	public void testNull() {
		Coordinate.of(0, 0).distanceKM(null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testLatitudeTooLow() {
		Coordinate.of(-90.01, 0);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testLatitudeTooHigh() {
		Coordinate.of(90.01, 0);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testLongitudeTooLow() {
		Coordinate.of(0, -180.01);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testLongitudeTooHigh() {
		Coordinate.of(0, 180.01);
	}

}

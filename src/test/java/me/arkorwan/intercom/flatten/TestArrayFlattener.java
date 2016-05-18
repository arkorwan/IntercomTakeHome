package me.arkorwan.intercom.flatten;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import me.arkorwan.intercom.flatten.ArrayFlattener;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class TestArrayFlattener {

	// a helper class to display a nest array, when used as test parameters,
	// in readable format.
	static class NestedArray {

		Object[] array;

		NestedArray(Object[] array) {
			this.array = array;
		}

		@Override
		public String toString() {
			return Arrays.deepToString(array);
		}

	}

	ArrayFlattener flattener = new ArrayFlattener();
	Random random = ThreadLocalRandom.current();

	@Test
	public void testEmptyArray() {
		assertEquals(flattener.flatten(new Object[0]), new int[0]);
	}

	@Test
	public void testSingleLevelArray() {

		assertEquals(flattener.flatten(new Object[] { 1, 2, 3, 4, 5 }),
				IntStream.rangeClosed(1, 5).toArray());
	}

	@Test
	public void testNestedObjectArray() {

		assertEquals(
				flattener.flatten(
						new Object[] { new Object[] { 1, 2, 3, 4, 5 } }),
				IntStream.rangeClosed(1, 5).toArray());

	}

	@Test
	public void testNestedIntArray() {

		assertEquals(
				flattener.flatten(new Object[] { new int[] { 1, 2, 3, 4, 5 } }),
				IntStream.rangeClosed(1, 5).toArray());

	}

	@Test
	public void testMixedArrayTypeAndLevel() {
		assertEquals(
				flattener.flatten(new Object[][] {
						new Object[] { 1, new Object[] { new int[] { 2, 3 } },
								new Object[] { 4, 5 } } }),
				IntStream.rangeClosed(1, 5).toArray());
	}

	/**
	 * Create a randomly nested array, by turning a subrange of a list into an
	 * array repeatedly.
	 * 
	 * @param count
	 *            the number of items
	 * @param nestingCount
	 *            the number of times to do the nesting process
	 */
	private Object[] buildRandomNestedArray(int count, int nestingCount) {

		List<Object> nestedList = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			nestedList.add(i);
		}

		for (int i = 0; i < nestingCount; i++) {

			// pick a range
			int lowBound = random.nextInt(nestedList.size());
			int upBound = random.nextInt(nestedList.size());
			int innerCount = upBound - lowBound + 1;
			if (upBound < lowBound) {
				innerCount = lowBound - upBound + 1;
				lowBound = upBound;
			}

			// remove the objects in the range and add to a nested array
			Object[] nested = new Object[innerCount];
			for (int j = 0; j < innerCount; j++) {
				nested[j] = nestedList.remove(lowBound);
			}

			// insert the nested array
			nestedList.add(lowBound, nested);
		}

		return nestedList.toArray();
	}

	// create 10 randomly nested array to be used as test parameters
	@DataProvider
	public Object[][] randomArrayProvider() {
		Object[][] data = new Object[10][1];
		for (int i = 0; i < 10; i++) {
			data[i][0] = new NestedArray(buildRandomNestedArray(30, 5));
		}
		return data;
	}

	@Test(dataProvider = "randomArrayProvider")
	public void testRandomNestedArray(NestedArray randomArray) {

		assertEquals(flattener.flatten(randomArray.array),
				IntStream.rangeClosed(1, 30).toArray());

	}

	// negative cases, all should throw an IllegalArgumentException.
	@DataProvider
	public Object[][] illegalArrayProvider() {
		Object[][] data = new Object[5][1];
		data[0][0] = new NestedArray(null);
		data[1][0] = new NestedArray(new Object[] { null });
		data[2][0] = new NestedArray(new Object[] { "one" });
		data[3][0] = new NestedArray(new Object[] { 1.0 });
		data[4][0] = new NestedArray(
				new Object[] { 1, 2, new Object[] { 3, "four" } });
		return data;
	}

	@Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "illegalArrayProvider")
	public void testException(NestedArray illegalArgument) {
		flattener.flatten(illegalArgument.array);
	}

}

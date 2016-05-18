package me.arkorwan.intercom.flatten;

import java.util.ArrayList;
import java.util.List;

public class ArrayFlattener {

	/**
	 * Flatten the source array into an int array. Each leaf-level item in the
	 * source array must be either an Integer, or a primitive int array.
	 * 
	 * @param source
	 *            the source array.
	 * @return a flattened int array.
	 * @throws IllegalArgumentException
	 *             when any of the arguments are not integer or integer array.
	 */
	public int[] flatten(Object[] source) {
		if (source == null)
			throw new IllegalArgumentException("Argument cannot be null");

		List<Integer> flattened = new ArrayList<>();
		// a recursive method that add to this flattened list.
		flattenToList(flattened, source);
		// use Stream API to turn List<Integer> into int[]
		return flattened.stream().mapToInt(i -> i).toArray();
	}

	private void flattenToList(List<Integer> flattened, Object[] source) {

		for (Object item : source) {
			if (item instanceof Object[]) {
				flattenToList(flattened, (Object[]) item);
			} else if (item instanceof int[]) {
				for (Integer i : (int[]) item) {
					flattened.add(i);
				}
			} else if (item instanceof Integer) {
				flattened.add((Integer) item);
			} else {
				throw new IllegalArgumentException(
						"All members must be integers");
			}
		}

	}

}

package me.dymax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HashList<V> {

	public static void benchmark() {
		/*
		 * basically, add and remove some points
		 *
		 * then find the highest point and lowest from these points
		 *
		 * can work from a simple set
		 */

		Set<Integer> random_add_values = new HashSet<Integer>();
		Set<Integer> random_remove_values = new HashSet<Integer>();
		Random r = new Random(124124123);
		for (int i = 0; i < 1000; i++) {
			random_add_values.add(r.nextInt(10000));
			random_remove_values.add(r.nextInt(10000));
		}

		long start = System.currentTimeMillis();
		Set<Integer> test_set = new HashSet<Integer>();
		for (int add : random_add_values) {
			test_set.add(add);
		}
		for (int remove : random_remove_values) {
			test_set.remove(remove);
		}
		int highest = 0;
		for (int t = 0; t < 256; t++) {
			Iterator<Integer> itr = test_set.iterator();
			highest = itr.next();
			for (int i : test_set) {
				if (i > highest) {
					highest = i;
				}
			}
		}
		System.out.println("Highest Value = " + highest);
		System.out.println("Time Taken <HashSet> = " + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();
		HashList<Integer> hash_test = new HashList<Integer>();
		for (int add : random_add_values) {
			hash_test.add(add, add);
		}
		for (int remove : random_remove_values) {
			hash_test.remove(remove);
		}
		for (int t = 0; t < 256; t++) {
			highest = hash_test.getLastValue();
		}
		System.out.println("Highest Value = " + highest);
		System.out.println("Time Taken <HashList> = " + (System.currentTimeMillis() - start));
	}

	public static void test() {
		HashList<Integer> hash_test = new HashList<Integer>();
		hash_test.add(1, 1);
		hash_test.add(3, 3);
		hash_test.add(6, 6);
		int highest = hash_test.getLastValue();
		System.out.println("Highest Test = " + highest);
		int lowest = hash_test.getFirstValue();
		System.out.println("Lowest Test = " + lowest);
	}

	/**
	 * could try using a long or and int to set if there is a value there
	 */


	private final HashMap<Integer, V> value_map = new HashMap<Integer, V>();
	/*
	 * this essentially works like a list of booleans, but taking advantage of how integers work for finding specific conditions
	 */
	private final List<Integer> bit_list = new ArrayList<Integer>();

	/**
	 *
	 * @param initial_capacity, Multiple of 32
	 */

	public HashList() {
		this(1);
	}

	public HashList(int initial_capacity) {
		for (int i = 0; i < initial_capacity; i++) {
			bit_list.add(0);
		}
	}

	public V getLastValue() {
		if (value_map.isEmpty()) {
			return null;
		}
		int last_index = bit_list.size() - 1;
		//int value = bit_list.get(last_index);
		int index = 31 - Integer.numberOfLeadingZeros(bit_list.get(last_index));
		//System.out.println("Value = " + value);
		//System.out.println("Index = " + index);
		for (int i = 0; i < last_index; i++) {
			index += 32;
		}
		//System.out.println("Index = " + index);
		//System.out.println("Map Size = " + value_map.size());
		return value_map.get(index);
	}

	public V getFirstValue() {
		if (value_map.isEmpty()) {
			return null;
		}
		int valid_index = 0;
		int bit = bit_list.get(valid_index);
		while (bit == 0) {
			bit = bit_list.get(++valid_index);
		}
		//System.out.println("Bit = " + Integer.toBinaryString(bit));
		int index = Integer.lowestOneBit(bit) - 1;
		//System.out.println("Index = " + index);
		for (int i = 0; i < valid_index; i++) {
			index += 32;
		}
		return value_map.get(index);
	}

	public void add(int key, V value) {
		V previous_value = value_map.put(key, value);
		if (previous_value == null) {
			//System.out.println("Set Bit");
//			if (key == 9990) {
//				System.out.println("Right Key.......................");
//			}
			setBit(key, true);
		}
	}

	public void remove(int key) {
		V value = value_map.remove(key);
		if (value != null) {
			setBit(key, false);
		}

	}

	private void setBit(int index, boolean set) {
		int bit_index = 0;
		//if its more than 32
		//that means need to minus
		while (index >= 32) {
			bit_index++;
			index -= 32;
		}
		//System.out.println("Bit Index = " + bit_index);
		while (bit_index >= bit_list.size()) {
			bit_list.add(0);
		}
		int bit_array = bit_list.get(bit_index);
		//will setting this int change the int in the list?
		int set_bit = getSetBit(index);
		//System.out.println("Set Bit = " + Integer.toBinaryString(set_bit));
		bit_array = set ? bit_array | set_bit : bit_array & ~set_bit;
		//System.out.println("Bit String = " + Integer.toBinaryString(bit_array));
		if (bit_array == 0 && bit_index == bit_list.size() - 1) {
			bit_list.remove(bit_index);
		}
		else {
			bit_list.set(bit_index, bit_array);
		}
//		else {
//			Integer t = 0;
//			t.va
//		}

	}

	private int getSetBit(int index) {
		//to set bit 0
		//then its 2 * 0
		//if its set bit 1
		//then its 2 * 1
//		if (index >= 32) {
//			return -1;
//		}
//		if (index == 0) {
//			return 0;
//		}
//		int set = 1;
//		for (int i = 0; i < index; i++) {
//			set *= 2;
//		}
//		return set;
		return 1 << index;
	}

}

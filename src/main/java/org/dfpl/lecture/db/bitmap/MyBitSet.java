package org.dfpl.lecture.db.bitmap;

public interface MyBitSet {

	/**
	 * 
	 * @return cloned BitSet
	 */
	public MyBitSet clone();

	/**
	 * Sets the bit at the specified index to the complement of its current value.
	 *
	 * @param bitIndex the index of the bit to flip
	 * @throws IndexOutOfBoundsException if the specified index is negative
	 */
	public void flip(int bitIndex);

	/**
	 * Sets the bit at the specified index to {@code true}.
	 *
	 * @param bitIndex a bit index
	 * @throws IndexOutOfBoundsException if the specified index is negative
	 * @since 1.0
	 */
	public void set(int bitIndex);

	/**
	 * Sets the bit specified by the index to {@code false}.
	 *
	 * @param bitIndex the index of the bit to be cleared
	 * @throws IndexOutOfBoundsException if the specified index is negative
	 * @since 1.0
	 */
	public void clear(int bitIndex);

	/**
	 * Returns the value of the bit with the specified index. The value is
	 * {@code true} if the bit with the index {@code bitIndex} is currently set in
	 * this {@code BitSet}; otherwise, the result is {@code false}.
	 *
	 * @param bitIndex the bit index
	 * @return the value of the bit with the specified index
	 * @throws IndexOutOfBoundsException if the specified index is negative
	 */
	public boolean get(int bitIndex);

	/**
	 * Returns true if the specified {@code BitSet} has any bits set to {@code true}
	 * that are also set to {@code true} in this {@code BitSet}.
	 *
	 * @param set {@code BitSet} to intersect with
	 * @return boolean indicating whether this {@code BitSet} intersects the
	 *         specified {@code BitSet}
	 * @since 1.4
	 */
	public boolean intersects(MyBitSet set);

	/**
	 * Returns the number of bits set to {@code true} in this {@code BitSet}.
	 *
	 * @return the number of bits set to {@code true} in this {@code BitSet}
	 * @since 1.4
	 */
	public int cardinality();

	/**
	 * Performs a logical <b>AND</b> of this target bit set with the argument bit
	 * set. This bit set is modified so that each bit in it has the value
	 * {@code true} if and only if it both initially had the value {@code true} and
	 * the corresponding bit in the bit set argument also had the value
	 * {@code true}.
	 *
	 * @param set a bit set
	 */
	public void and(MyBitSet set);

	/**
	 * Performs a logical <b>OR</b> of this bit set with the bit set argument. This
	 * bit set is modified so that a bit in it has the value {@code true} if and
	 * only if it either already had the value {@code true} or the corresponding bit
	 * in the bit set argument has the value {@code true}.
	 *
	 * @param set a bit set
	 */
	public void or(MyBitSet set);

	/**
	 * Performs a logical <b>XOR</b> of this bit set with the bit set argument. This
	 * bit set is modified so that a bit in it has the value {@code true} if and
	 * only if one of the following statements holds:
	 * <ul>
	 * <li>The bit initially has the value {@code true}, and the corresponding bit
	 * in the argument has the value {@code false}.
	 * <li>The bit initially has the value {@code false}, and the corresponding bit
	 * in the argument has the value {@code true}.
	 * </ul>
	 *
	 * @param set a bit set
	 */
	public void xor(MyBitSet set);

	/**
	 * Sets the bits from the specified {@code fromIndex} (inclusive) to the
	 * specified {@code toIndex} (exclusive) to {@code true}.
	 *
	 * @param fromIndex index of the first bit to be set
	 * @param toIndex   index after the last bit to be set
	 * @throws IndexOutOfBoundsException if {@code fromIndex} is negative, or
	 *                                   {@code toIndex} is negative, or
	 *                                   {@code fromIndex} is larger than
	 *                                   {@code toIndex}
	 * @since 1.4
	 */
	public void set(int fromIndex, int toIndex);

	/**
	 * Returns the index of the first bit that is set to {@code true} that occurs on
	 * or after the specified starting index. If no such bit exists then {@code -1}
	 * is returned.
	 *
	 * <p>
	 * To iterate over the {@code true} bits in a {@code BitSet}, use the following
	 * loop:
	 *
	 * <pre> {@code
	 * for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
	 * 	// operate on index i here
	 * 	if (i == Integer.MAX_VALUE) {
	 * 		break; // or (i+1) would overflow
	 * 	}
	 * }
	 * }</pre>
	 *
	 * @param fromIndex the index to start checking from (inclusive)
	 * @return the index of the next set bit, or {@code -1} if there is no such bit
	 * @throws IndexOutOfBoundsException if the specified index is negative
	 * @since 1.4
	 */
	public int nextSetBit(int fromIndex);

	/**
	 * Return a string representation of bitset e.g., if 0, 2, 4 are set {0,2,4}
	 * 
	 * @return {idx1,idx2,idx3,...}
	 */
	public String toString();
}

package org.dfpl.lecture.db.bitmap;

public class MyBitSetImpl implements MyBitSet, Cloneable {

	/**
	 * bitset 파일들이 들어갈 곳
	 *
	 * 형식: street attribute의 Round Hill이라면
	 * street_Round Hill 이라는 파일에 bitmap이 들어가 있음
	 * Faker라는 라이브러리로 임의의 가짜 문자열을 만들어내므로,
	 * 이름 escape은 적절히 수행해야 할 수 있다.
	 *
	 */
	private String baseDirectory;

	public MyBitSetImpl(String baseDirectory, int size) {
		// TODO Auto-generated method stub
	}

	@Override
	public MyBitSet clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flip(int bitIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void set(int bitIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear(int bitIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean get(int bitIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(MyBitSet set) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int cardinality() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void and(MyBitSet set) {
		// TODO Auto-generated method stub

	}

	@Override
	public void or(MyBitSet set) {
		// TODO Auto-generated method stub

	}

	@Override
	public void xor(MyBitSet set) {
		// TODO Auto-generated method stub

	}

	@Override
	public void set(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public int nextSetBit(int fromIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}

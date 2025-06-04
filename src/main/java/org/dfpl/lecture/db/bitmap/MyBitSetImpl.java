package org.dfpl.lecture.db.bitmap;

import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

/**
 * Simple bitmap implementation which persists every change to disk.
 * <p>
 * 모든 연산은 파일 기반으로 이루어진다. 메모리에만 데이터를 보관하지 않기
 * 위하여 비트를 변경할 때마다 파일을 즉시 갱신한다. 과제 조건에 따라
 * {@link LoanManager}에서 호출되는 메서드는 이 구현만 수정하여 동작하도록
 * 구성하였다.
 */
public class MyBitSetImpl implements MyBitSet, Cloneable {

    /** directory where bitset files are stored */
    private final String baseDirectory;
    /** file which actually stores the bitmap */
    private final Path file;
    /** number of bits managed by this bitset */
    private final int size;
    /** number of bytes required to store {@link #size} bits */
    private final int byteSize;

    /**
     * Create a temporary bitset. The data is stored in a unique file inside the
     * provided directory.
     */
    public MyBitSetImpl(String baseDirectory, int size) {
        this.baseDirectory = baseDirectory;
        this.size = size;
        this.byteSize = (size + 7) / 8;
        ensureDirectory();
        this.file = Paths.get(baseDirectory, "tmp_" + UUID.randomUUID() + ".bit");
        initFile();
    }

    /**
     * Create a bitset with a specific attribute/value name. The file name is
     * composed of attribute and value to meet the assignment requirement.
     */
    public MyBitSetImpl(String baseDirectory, String attr, String value, int size) {
        this.baseDirectory = baseDirectory;
        this.size = size;
        this.byteSize = (size + 7) / 8;
        ensureDirectory();
        String safe = sanitize(attr + "_" + value);
        this.file = Paths.get(baseDirectory, safe + ".bit");
        initFile();
    }

    /** make sure base directory exists */
    private void ensureDirectory() {
        File dir = new File(baseDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Create the backing file if needed and adjust its length to
     * {@link #byteSize}. The file content is meaningless when first
     * created, so it is initialized with zeros.
     */
    private void initFile() {
        try {
            byte[] data;
            if (Files.exists(file)) {
                data = Files.readAllBytes(file);
                if (data.length == byteSize) {
                    return; // file already initialized with correct size
                }
            } else {
                data = new byte[0];
            }

            byte[] newData = Arrays.copyOf(data, byteSize);
            Files.write(file, newData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9\\-]", "_");
    }

    /** read all bytes representing the bitmap */
    private byte[] readBytes() {
        try {
            if (!Files.exists(file)) {
                initFile();
            }
            byte[] data = Files.readAllBytes(file);
            if (data.length < byteSize) {
                data = Arrays.copyOf(data, byteSize);
                Files.write(file, data);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** write entire bitmap byte array to the backing file */
    private void writeBytes(byte[] data) {
        try {
            if (data.length != byteSize) {
                data = Arrays.copyOf(data, byteSize);
            }
            Files.write(file, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** helper to modify a single bit */
    private void setBit(int index, boolean value) {
        byte[] data = readBytes();
        int byteIdx = index / 8;
        int bit = index % 8;
        if (value) {
            data[byteIdx] |= (1 << bit);
        } else {
            data[byteIdx] &= ~(1 << bit);
        }
        writeBytes(data);
    }

    @Override
    public MyBitSet clone() {
        MyBitSetImpl cloned = new MyBitSetImpl(baseDirectory, size);
        cloned.writeBytes(readBytes());
        return cloned;
    }

    @Override
    public void flip(int bitIndex) {
        checkIndex(bitIndex);
        byte[] data = readBytes();
        int byteIdx = bitIndex / 8;
        int bit = bitIndex % 8;
        data[byteIdx] ^= (1 << bit);
        writeBytes(data);
    }

    @Override
    public void set(int bitIndex) {
        checkIndex(bitIndex);
        setBit(bitIndex, true);
    }

    @Override
    public void clear(int bitIndex) {
        checkIndex(bitIndex);
        setBit(bitIndex, false);
    }

    @Override
    public boolean get(int bitIndex) {
        checkIndex(bitIndex);
        byte[] data = readBytes();
        int byteIdx = bitIndex / 8;
        int bit = bitIndex % 8;
        return (data[byteIdx] & (1 << bit)) != 0;
    }

    private void checkIndex(int idx) {
        if (idx < 0 || idx >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean intersects(MyBitSet set) {
        if (!(set instanceof MyBitSetImpl other)) {
            return false;
        }
        byte[] a = readBytes();
        byte[] b = other.readBytes();
        int len = Math.min(a.length, b.length);
        for (int i = 0; i < len; i++) {
            if ((a[i] & b[i]) != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int cardinality() {
        byte[] data = readBytes();
        int count = 0;
        for (byte b : data) {
            count += Integer.bitCount(b & 0xFF);
        }
        return count;
    }

    @Override
    public void and(MyBitSet set) {
        if (!(set instanceof MyBitSetImpl other)) {
            return;
        }
        byte[] a = readBytes();
        byte[] b = other.readBytes();
        int len = Math.min(a.length, b.length);
        for (int i = 0; i < len; i++) {
            a[i] &= b[i];
        }
        if (a.length > byteSize) {
            a = Arrays.copyOf(a, byteSize);
        }
        writeBytes(a);
    }

    @Override
    public void or(MyBitSet set) {
        if (!(set instanceof MyBitSetImpl other)) {
            return;
        }
        byte[] a = readBytes();
        byte[] b = other.readBytes();
        int len = Math.min(a.length, b.length);
        for (int i = 0; i < len; i++) {
            a[i] |= b[i];
        }
        if (a.length > byteSize) {
            a = Arrays.copyOf(a, byteSize);
        }
        writeBytes(a);
    }

    @Override
    public void xor(MyBitSet set) {
        if (!(set instanceof MyBitSetImpl other)) {
            return;
        }
        byte[] a = readBytes();
        byte[] b = other.readBytes();
        int len = Math.min(a.length, b.length);
        for (int i = 0; i < len; i++) {
            a[i] ^= b[i];
        }
        if (a.length > byteSize) {
            a = Arrays.copyOf(a, byteSize);
        }
        writeBytes(a);
    }

    @Override
    public void set(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        byte[] data = readBytes();
        for (int i = fromIndex; i < toIndex; i++) {
            int byteIdx = i / 8;
            int bit = i % 8;
            data[byteIdx] |= (1 << bit);
        }
        writeBytes(data);
    }

    @Override
    public int nextSetBit(int fromIndex) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        byte[] data = readBytes();
        for (int i = fromIndex; i < size; i++) {
            int byteIdx = i / 8;
            int bit = i % 8;
            if ((data[byteIdx] & (1 << bit)) != 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        byte[] data = readBytes();
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (int i = 0; i < size; i++) {
            int byteIdx = i / 8;
            int bit = i % 8;
            if ((data[byteIdx] & (1 << bit)) != 0) {
                if (!first) {
                    sb.append(',');
                }
                sb.append(i);
                first = false;
            }
        }
        sb.append('}');
        return sb.toString();
    }
}


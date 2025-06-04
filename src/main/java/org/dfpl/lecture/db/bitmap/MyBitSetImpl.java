package org.dfpl.lecture.db.bitmap;

import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    /**
     * Create a temporary bitset. The data is stored in a unique file inside the
     * provided directory.
     */
    public MyBitSetImpl(String baseDirectory, int size) {
        this.baseDirectory = baseDirectory;
        this.size = size;
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

    /** create file with zeros when it does not exist */
    private void initFile() {
        if (Files.exists(file)) {
            return;
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb.append('0');
            }
            Files.writeString(file, sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9\\-]", "_");
    }

    /** read entire bitmap string from file */
    private String readBits() {
        try {
            if (!Files.exists(file)) {
                initFile();
            }
            return Files.readString(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** write entire bitmap string to file */
    private void writeBits(String bits) {
        try {
            Files.writeString(file, bits);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** helper to modify a single index */
    private void setChar(int index, char value) {
        String bits = readBits();
        StringBuilder sb = new StringBuilder(bits);
        while (sb.length() < size) {
            sb.append('0');
        }
        sb.setCharAt(index, value);
        writeBits(sb.toString());
    }

    @Override
    public MyBitSet clone() {
        MyBitSetImpl cloned = new MyBitSetImpl(baseDirectory, size);
        cloned.writeBits(readBits());
        return cloned;
    }

    @Override
    public void flip(int bitIndex) {
        checkIndex(bitIndex);
        String bits = readBits();
        char c = bits.charAt(bitIndex);
        setChar(bitIndex, c == '1' ? '0' : '1');
    }

    @Override
    public void set(int bitIndex) {
        checkIndex(bitIndex);
        setChar(bitIndex, '1');
    }

    @Override
    public void clear(int bitIndex) {
        checkIndex(bitIndex);
        setChar(bitIndex, '0');
    }

    @Override
    public boolean get(int bitIndex) {
        checkIndex(bitIndex);
        String bits = readBits();
        return bits.charAt(bitIndex) == '1';
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
        String a = readBits();
        String b = other.readBits();
        int len = Math.min(a.length(), b.length());
        for (int i = 0; i < len; i++) {
            if (a.charAt(i) == '1' && b.charAt(i) == '1') {
                return true;
            }
        }
        return false;
    }

    @Override
    public int cardinality() {
        String bits = readBits();
        int count = 0;
        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '1') {
                count++;
            }
        }
        return count;
    }

    @Override
    public void and(MyBitSet set) {
        if (!(set instanceof MyBitSetImpl other)) {
            return;
        }
        String a = readBits();
        String b = other.readBits();
        StringBuilder sb = new StringBuilder();
        int len = Math.min(a.length(), b.length());
        for (int i = 0; i < len; i++) {
            sb.append((a.charAt(i) == '1' && b.charAt(i) == '1') ? '1' : '0');
        }
        while (sb.length() < size) {
            sb.append('0');
        }
        writeBits(sb.toString());
    }

    @Override
    public void or(MyBitSet set) {
        if (!(set instanceof MyBitSetImpl other)) {
            return;
        }
        String a = readBits();
        String b = other.readBits();
        StringBuilder sb = new StringBuilder();
        int len = Math.min(a.length(), b.length());
        for (int i = 0; i < len; i++) {
            sb.append((a.charAt(i) == '1' || b.charAt(i) == '1') ? '1' : '0');
        }
        while (sb.length() < size) {
            sb.append('0');
        }
        writeBits(sb.toString());
    }

    @Override
    public void xor(MyBitSet set) {
        if (!(set instanceof MyBitSetImpl other)) {
            return;
        }
        String a = readBits();
        String b = other.readBits();
        StringBuilder sb = new StringBuilder();
        int len = Math.min(a.length(), b.length());
        for (int i = 0; i < len; i++) {
            sb.append((a.charAt(i) != b.charAt(i)) ? '1' : '0');
        }
        while (sb.length() < size) {
            sb.append('0');
        }
        writeBits(sb.toString());
    }

    @Override
    public void set(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        String bits = readBits();
        StringBuilder sb = new StringBuilder(bits);
        while (sb.length() < size) {
            sb.append('0');
        }
        for (int i = fromIndex; i < toIndex; i++) {
            sb.setCharAt(i, '1');
        }
        writeBits(sb.toString());
    }

    @Override
    public int nextSetBit(int fromIndex) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        String bits = readBits();
        for (int i = fromIndex; i < bits.length(); i++) {
            if (bits.charAt(i) == '1') {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        String bits = readBits();
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '1') {
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


package com.ciwen.bloom;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import com.ciwen.bloom.bitset.CustomBitSet;
import java.util.BitSet;
import java.util.List;

/*实现 BloomFilter 接口的具体类*/
public class SimpleBloomFilter<T> implements BloomFilter<T> {
    //private final BitSet bitSet;
    private final CustomBitSet bitSet;
    private final int size;
    private final int numHashFunctions;
    private int count;
    private final List<HashFunction<T>> hashFunctions;

    /**
     * 创建一个布隆过滤器
     * @param expectedElements 预期元素数量
     * @param falsePositiveRate 可接受的误判率
     */
    public SimpleBloomFilter(int expectedElements, double falsePositiveRate) {
        this.size = optimalSize(expectedElements, falsePositiveRate);
        this.numHashFunctions = optimalHashFunctions(expectedElements, size);
        this.bitSet = new CustomBitSet(size);
        //this.bitSet = new BitSet(size);
        this.count = 0;

        // 初始化哈希函数列表
        this.hashFunctions = new ArrayList<>();
        hashFunctions.add(new MurmurHash<>());
        hashFunctions.add(new FNVHash<>());
        hashFunctions.add(new DJBHash<>());
        hashFunctions.add(new JenkinsHash<>());
    }

    @Override
    public void add(@NotNull T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        for (int i = 0; i < numHashFunctions; i++) {
            bitSet.set(hash(item, i));
        }
        count++;
    }

    @Override
    public boolean mightContain(@NotNull T item) {
        if (item == null) {
            return false;
        }

        for (int i = 0; i < numHashFunctions; i++) {
            if (!bitSet.get(hash(item, i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double getFalsePositiveRate() {
        return Math.pow(1 - Math.exp(-numHashFunctions * count / (double) size), numHashFunctions);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void clear() {
        bitSet.clear();
        count = 0;
    }

    // 计算最优布隆过滤器大小
    private int optimalSize(int n, double p) {
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    // 计算最优哈希函数数量
    private int optimalHashFunctions(int n, int m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    // 哈希函数
    private int hash(T item, int functionIndex) {
        // 使用不同的种子值来获取不同的哈希值
        int seed = functionIndex * 0xbc9f1d34;
        return Math.abs(hashFunctions.get(functionIndex % hashFunctions.size()).hash(item, seed) % size);
    }
}
package com.ciwen.bloom;

/*哈希函数的接口定义*/
public interface HashFunction<T> {
    /**
     * 计算哈希值
     * @param item 要哈希的数据
     * @param seed 种子值，用于生成不同的哈希值
     * @return 哈希值
     */
    int hash(Object item, int seed);
}

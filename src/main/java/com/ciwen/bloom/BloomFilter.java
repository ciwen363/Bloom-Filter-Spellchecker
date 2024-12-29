package com.ciwen.bloom;

/*布隆过滤器的接口定义*/
public interface BloomFilter<T> {
    /**
     * 添加一个元素到布隆过滤器
     * @param item 要添加的元素
     */
    void add(T item);

    /**
     * 检查一个元素是否可能存在于集合中
     * @param item 要检查的元素
     * @return 如果元素可能存在返回true，如果元素一定不存在返回false
     */
    boolean mightContain(T item);

    /**
     * 获取预期的误判率
     * @return 误判率
     */
    double getFalsePositiveRate();

    /**
     * 获取布隆过滤器的大小
     * @return 位数组的大小
     */
    int getSize();

    /**
     * 获取已添加元素的数量
     * @return 元素数量
     */
    int getCount();

    /**
     * 清空布隆过滤器
     */
    void clear();
}
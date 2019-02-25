package com.phantoms.framework.cloudbase.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/***
 * 唯一主键ID 生成算法
 * 
 * @version <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月7日 	Created
 *
 * </pre>
 * @since 1.
 */
@ToString
@Slf4j
public class SnowflakeIdFactory {

    private final long twepoch            = 1288834974657L;
    private final long workerIdBits       = 5L;
    private final long datacenterIdBits   = 5L;
    private final long maxWorkerId        = -1L ^ (-1L << workerIdBits);
    private final long maxDatacenterId    = -1L ^ (-1L << datacenterIdBits);
    private final long sequenceBits       = 12L;
    private final long workerIdShift      = sequenceBits;
    private final long datacenterIdShift  = sequenceBits + workerIdBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private final long sequenceMask       = -1L ^ (-1L << sequenceBits);

    // 用户ID 10位，刚好小于 1024
    private final long initialIdBits         = 10L;
    private final long maxInitialId          = -1L ^ (-1L << initialIdBits);                     // 应该等于1023;
    private final long userSequenceIdBits = 2L;                                            //
    private final long userSequenceMask   = -1L ^ (-1L << userSequenceIdBits);             // 等于4

    private long       workerId;
    private long       datacenterId;
    private long       sequence           = 0L;                                            // 初始化就是这样
    private long       lastTimestamp      = -1L;

    /**
     * 如果是用于生产用户ID，使用1024的初始化值
     * 
     * @param position 大于0即可，在集群中是唯一且有序，集群总数不能超过1024
     */
    public SnowflakeIdFactory(int position){
        position = position % 1024;
        int datacenterId = position / 32;
        int workerId = position % 32;
        if (position < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",
                maxWorkerId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = maxInitialId + 1; // 初始化sequence 就从1024开始升序; 跟初始化 initialId 值分开来，避免冲突
    }
    
    private boolean hasInitialId = true;
    
    /**
     * 过时的方式，不推荐在项目使用。
     * @param position
     * @param hasInitialId
     */
    @Deprecated
    public SnowflakeIdFactory(int position, boolean hasInitialId){
        position = position % 1024;
        int datacenterId = position / 32;
        int workerId = position % 32;
        if (position < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",
                maxWorkerId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.hasInitialId = hasInitialId;
        if(hasInitialId){
            this.sequence = maxInitialId + 1; 
        }
    }

    /**
     * 基础demo，不能再用了
     * 
     * @param relationId
     * @return
     */
    @Deprecated
    private synchronized long nextIdFromZero() {
        if(hasInitialId) {
            throw new RuntimeException("hasInitialId was congfig，please use nextId() or nextId(initialId)");
        }
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            // 服务器时钟被调整了,ID生成器停止服务.
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
               | (workerId << workerIdShift) | sequence;
    }

    /**
     * TODO Add comments here.
     * 
     * @return
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            // 服务器时钟被调整了,ID生成器停止服务.
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                sequence = maxInitialId + 1; // 重置ID
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = maxInitialId + 1;// 重置ID，普通ID从1024开始，跟用户USERID值分开。避免与用户ID冲突
        }
        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
               | (workerId << workerIdShift) | sequence;
    }

    /**
     * 关联其他ID，比如关联用户USERID, 后10位ID放在ORDERID后面
     * 
     * @param relationId make sure relationId > 0
     * @return
     */
    public synchronized long nextId(long relationId) {
        long timestamp = timeGen();
        // 这里条件太多，效率慢了一些，请自行确保relationId > 0
        if (timestamp < lastTimestamp || relationId > maxInitialId) {
            // 服务器时钟被调整了,ID生成器停止服务.
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            // userSequence = (userSequence + 1) & userSequenceMask;
            // if(userSequence == 0){
            // timestamp = tilNextMillis(lastTimestamp);
            // }
            // sequence = (userSequence << 11L) | relationId;
            // 那么按用户的USERID进行,上面的代码最多只有可以有4个，所以这里简单粗暴，直接换下一毫秒，而且还能避免跟其他的sequence产生冲突
            timestamp = tilNextMillis(lastTimestamp);
        }
        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
               | (workerId << workerIdShift) | relationId;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public static void testProductIdByMoreThread(int position, int n) throws InterruptedException {
        List<Thread> tlist = new ArrayList<>();
        Set<Long> setAll = new HashSet<>();
        CountDownLatch cdLatch = new CountDownLatch(10);
        long start = System.currentTimeMillis();
        int threadNo = position;
        Map<String, SnowflakeIdFactory> idFactories = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            // 用线程名称做map key.
            idFactories.put("snowflake" + i, new SnowflakeIdFactory(threadNo++));
        }
        for (int i = 0; i < 10; i++) {
            Thread temp = new Thread(new Runnable() {

                @Override
                public void run() {
                    Set<Long> setId = new HashSet<>();
                    SnowflakeIdFactory idWorker = idFactories.get(Thread.currentThread().getName());
                    for (int j = 0; j < n; j++) {
                        setId.add(idWorker.nextId());
                        // log.info("id-" + getUserIdValue(idWorker.nextId()));
                        getIntFormLastBit(idWorker.nextId(), 12);
                        // printInfo(idWorker.nextId());
                    }
                    synchronized (setAll) {
                        setAll.addAll(setId);
                        log.info("{}生产了{}个id,并成功加入到setAll中.", Thread.currentThread().getName(), n);
                    }
                    cdLatch.countDown();
                }
            }, "snowflake" + i);
            tlist.add(temp);
        }
        for (int j = 0; j < 10; j++) {
            tlist.get(j).start();
        }
        cdLatch.await();

        long end1 = System.currentTimeMillis() - start;

        log.info("共耗时:{}毫秒,预期应该生产{}个id, 实际合并总计生成ID个数:{}", end1, 10 * n, setAll.size());

    }

    public static void testProductId(int dataCenterId, int workerId, int n) {
        SnowflakeIdFactory idWorker = new SnowflakeIdFactory(2);
        SnowflakeIdFactory idWorker2 = new SnowflakeIdFactory(3);
        Set<Long> setOne = new HashSet<>();
        Set<Long> setTow = new HashSet<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            setOne.add(idWorker.nextId(0));// 加入set
        }
        long end1 = System.currentTimeMillis() - start;
        log.info("第一批ID预计生成{}个,实际生成{}个<<<<*>>>>共耗时:{}", n, setOne.size(), end1);

        for (int i = 0; i < n; i++) {
            setTow.add(idWorker2.nextId(0));// 加入set
        }
        long end2 = System.currentTimeMillis() - start;
        log.info("第二批ID预计生成{}个,实际生成{}个<<<<*>>>>共耗时:{}", n, setTow.size(), end2);

        setOne.addAll(setTow);
        log.info("合并总计生成ID个数:{}", setOne.size());

    }

    public static void testPerSecondProductIdNums() {
        SnowflakeIdFactory idWorker = new SnowflakeIdFactory(2);
        long start = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; System.currentTimeMillis() - start < 1000; i++, count = i) {
            /** 测试方法一: 此用法纯粹的生产ID,每秒生产ID个数为300w+ */
            idWorker.nextId(0);
            /**
             * 测试方法二: 在log中打印,同时获取ID,此用法生产ID的能力受限于log.error()的吞吐能力. 每秒徘徊在10万左右.
             */
            // log.error("{}",idWorker.nextId());
        }
        long end = System.currentTimeMillis() - start;
        System.out.println(end);
        System.out.println(count);
    }

    private static void printInfo(long num) {
        System.out.println("num -int:" + num);
        System.out.print(Long.toBinaryString(num));
        System.out.println("  -  " + Long.toBinaryString(num).length());
    }

    
    public static int getIntFormLastBit(Long number, int lastBit) {
        long move = 64L - lastBit;
        number = number << move >>> move;// 由于第lastBit 位可能是1，所以需要无符号 >>> 移动,否则出现负数
        return number.intValue();
    }
    
    // 获取long型二进制的最后10位。
    public static int getIntFormLast10Bit(Long number) {
        int move = 54;
        number = number << move >>> move;// 由于第十位可能是1，所以需要无符号 >>> 移动
        return number.intValue();
    }

    public static void main(String[] args) {
    	
    	SnowflakeIdFactory snowflakeIdFactory = new SnowflakeIdFactory(13);
    	
        long number = snowflakeIdFactory.nextId(542); 
//        System.out.println("ddd= " + getIntFormLast10Bit(number));
        
        printInfo(number);
        number = (number << 54L); //1069451370437943517L
        number = (number >>> 54L);
        printInfo(number);

       
        if (true) return;

        /**
         * case1: 测试每秒生产id个数? 结论: 每秒生产id个数300w+
         */
        // testPerSecondProductIdNums();

        /**
         * case2: 单线程-测试多个生产者同时生产N个id,验证id是否有重复? 结论: 验证通过,没有重复.
         */
        // testProductId(1,2,10000);//验证通过!
        // testProductId(1,2,20000);//验证通过!

        /**
         * case3: 多线程-测试多个生产者同时生产N个id, 全部id在全局范围内是否会重复? 结论: 验证通过,没有重复.
         */
        try {
            testProductIdByMoreThread(1, 100);// 单机测试此场景,性能损失至少折半!
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

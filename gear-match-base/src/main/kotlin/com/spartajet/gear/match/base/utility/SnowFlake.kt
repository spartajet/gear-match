package com.spartajet.gear.match.base.utility

/**
 * @description
 * @create 2017-05-30 下午3:43
 * @email spartajet.guo@gmail.com
 */
class SnowFlake(val datacenterId: Long, val machineId: Long) {
    /**
     * 起始的时间戳
     */
    private val START_STMP = 1480166465631L

    /**
     * 每一部分占用的位数
     */
    private val SEQUENCE_BIT: Int = 12 //序列号占用的位数
    /**
     * The constant MACHINE_BIT.
     */
    private val MACHINE_BIT: Int = 5  //机器标识占用的位数
    /**
     * The constant DATACENTER_BIT.
     */
    private val DATACENTER_BIT: Int = 5//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private val MAX_DATACENTER_NUM = -1L xor (-1L shl DATACENTER_BIT)
    /**
     * The constant MAX_MACHINE_NUM.
     */
    private val MAX_MACHINE_NUM = -1L xor (-1L shl MACHINE_BIT)
    /**
     * The constant MAX_SEQUENCE.
     */
    private val MAX_SEQUENCE = -1L xor (-1L shl SEQUENCE_BIT)

    /**
     * 每一部分向左的位移
     */
    private val MACHINE_LEFT = SEQUENCE_BIT
    /**
     * The constant DATACENTER_LEFT.
     */
    private val DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT
    /**
     * The constant TIMESTMP_LEFT.
     */
    private val TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT

    /**
     * The Sequence.
     */
    private var sequence = 0L //序列号
    /**
     * The Last stmp.
     */
    private var lastStmp = -1L//上一次时间戳

    init {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0")
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0")
        }
    }


    /**
     * 产生下一个ID

     * @return long
     */
    @Synchronized fun nextId(): Long {
        var currStmp = getNewstmp()
        if (currStmp < lastStmp) {
            throw RuntimeException("Clock moved backwards.  Refusing to generate id")
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = sequence + 1 and MAX_SEQUENCE
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill()
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L
        }

        lastStmp = currStmp

        return currStmp - START_STMP shl TIMESTMP_LEFT or (datacenterId shl DATACENTER_LEFT) or (machineId shl MACHINE_LEFT) or sequence
    }

    /**
     * Gets next mill.

     * @return the next mill
     */
    private fun getNextMill(): Long {
        var mill = getNewstmp()
        while (mill <= lastStmp) {
            mill = getNewstmp()
        }
        return mill
    }

    /**
     * Gets newstmp.

     * @return the newstmp
     */
    private fun getNewstmp(): Long {
        return System.currentTimeMillis()
    }
}
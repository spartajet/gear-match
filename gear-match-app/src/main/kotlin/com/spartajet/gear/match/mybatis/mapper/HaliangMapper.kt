package com.spartajet.gear.match.mybatis.mapper

import com.spartajet.gear.match.mybatis.bean.Haliang
import org.apache.ibatis.annotations.Mapper

@Mapper
interface HaliangMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table haliang

     * @mbggenerated
     */
    fun deleteByPrimaryKey(id: Long?): Int

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table haliang

     * @mbggenerated
     */
    fun insert(record: Haliang): Int

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table haliang

     * @mbggenerated
     */
    fun insertSelective(record: Haliang): Int

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table haliang

     * @mbggenerated
     */
    fun selectByPrimaryKey(id: Long?): Haliang

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table haliang

     * @mbggenerated
     */
    fun updateByPrimaryKeySelective(record: Haliang): Int

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table haliang

     * @mbggenerated
     */
    fun updateByPrimaryKey(record: Haliang): Int
}
package com.jingqueyimu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.jingqueyimu.model.Admin;

import tk.mybatis.mapper.common.Mapper;

/**
 * 管理员数据映射
 *
 * @author zhuangyilian
 */
public interface AdminMapper extends Mapper<Admin> {

    /**
     * 查询接收邮件的管理员
     *
     * @return
     */
    @Select("SELECT * FROM t_admin WHERE is_lock = 0 AND email IS NOT NULL AND email <> ''")
    List<Admin> listReceiveEmailAdmin();
}

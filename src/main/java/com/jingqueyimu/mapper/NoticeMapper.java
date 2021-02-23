package com.jingqueyimu.mapper;

import org.apache.ibatis.annotations.Select;

import com.jingqueyimu.model.Notice;

import tk.mybatis.mapper.common.Mapper;

/**
 * 公告数据映射
 *
 * @author zhuangyilian
 */
public interface NoticeMapper extends Mapper<Notice> {

    /**
     * 上一篇公告
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM t_notice WHERE id < #{id} ORDER BY id DESC LIMIT 1")
    Notice prevNotice(long id);
    
    /**
     * 下一篇公告
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM t_notice WHERE id > #{id} LIMIT 1")
    Notice nextNotice(long id);
}

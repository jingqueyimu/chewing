package com.jingqueyimu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingqueyimu.mapper.NoticeMapper;
import com.jingqueyimu.model.Notice;

/**
 * 公告服务
 *
 * @author zhuangyilian
 */
@Service
public class NoticeService extends BaseService<Notice> {
    
    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * 浏览公告详情
     *
     * @param id
     * @return
     */
    public Notice viewDetail(long id) {
        Notice notice = getById(id);
        if (notice == null) {
            return null;
        }
        notice.setReadCount(notice.getReadCount() + 1);
        return update(notice);
    }
    
    /**
     * 上一篇公告
     *
     * @param id
     * @return
     */
    public Notice prevNotice(long id) {
        return noticeMapper.prevNotice(id);
    }
    
    /**
     * 下一篇公告
     *
     * @param id
     * @return
     */
    public Notice nextNotice(long id) {
        return noticeMapper.nextNotice(id);
    }
}

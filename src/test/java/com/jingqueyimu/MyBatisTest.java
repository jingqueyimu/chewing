package com.jingqueyimu;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jingqueyimu.mapper.SqlMapper;
import com.jingqueyimu.mapper.SqlMapper2;

/**
 * MyBatis测试
 *
 * @author zhuangyilian
 */
public class MyBatisTest extends BaseTest {
    
    @Autowired
    private SqlMapper sqlMapper;
    @Autowired
    private SqlMapper2 sqlMapper2;
    
    @Test
    public void testInsert() {
        String sql = "INSERT INTO t_site_config(code, name, content, public_flag, gmt_create) VALUES('test', '测试', '测试内容', true, NOW())";
        int count = sqlMapper.insert(sql);
        System.out.println("---------testInsert---------: " + count);
    }
    
    @Test
    public void testUpdate() {
        String sql = "UPDATE t_site_config SET description = '测试更新' WHERE code = 'test'";
        int count = sqlMapper.update(sql);
        System.out.println("---------testUpdate---------: " + count);
    }
    
    @Test
    public void testSelect() {
        String sql = "SELECT * FROM t_site_config WHERE code = 'test';";
        List<Map<String, Object>> datas = sqlMapper.select(sql);
        System.out.println("---------testSelect---------: " + datas);
    }
    
    @Test
    public void testCount() {
        String sql = "SELECT COUNT(*) FROM t_site_config WHERE code = 'test';";
        int count = sqlMapper.count(sql);
        System.out.println("---------testCount---------: " + count);
    }
    
    @Test
    public void testDelete() {
        String sql = "DELETE FROM t_site_config WHERE code = 'test'";
        int count = sqlMapper.delete(sql);
        System.out.println("---------testDelete---------: " + count);
    }
    
    @Test
    public void testInsert2() {
        String sql = "INSERT INTO t_site_config(code, name, content, public_flag, gmt_create) VALUES('test', '测试', '测试内容', true, NOW())";
        int count = sqlMapper2.insert(sql);
        System.out.println("---------testInsert2---------: " + count);
    }
    
    @Test
    public void testUpdate2() {
        String sql = "UPDATE t_site_config SET description = '测试更新' WHERE code = 'test'";
        int count = sqlMapper2.update(sql);
        System.out.println("---------testUpdate2---------: " + count);
    }
    
    @Test
    public void testSelect2() {
        String sql = "SELECT * FROM t_site_config WHERE code = 'test';";
        List<Map<String, Object>> datas = sqlMapper2.select(sql);
        System.out.println("---------testSelect2---------: " + datas);
    }
    
    @Test
    public void testCount2() {
        String sql = "SELECT COUNT(*) FROM t_site_config WHERE code = 'test';";
        int count = sqlMapper2.count(sql);
        System.out.println("---------testCount2---------: " + count);
    }
    
    @Test
    public void testDelete2() {
        String sql = "DELETE FROM t_site_config WHERE code = 'test'";
        int count = sqlMapper2.delete(sql);
        System.out.println("---------testDelete2---------: " + count);
    }
}

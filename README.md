# 前言

闲来无事，整一个 Java 项目快速开发脚手架。

# 正文

## 一、简介

Chewing 是一个简单的 Java 项目快速开发脚手架。既适合需要开发小型项目的小伙伴使用，也适合刚入门的新手用来学习一些常用的技术。

## 二、源码

源码地址：[https://github.com/jingqueyimu/chewing](https://github.com/jingqueyimu/chewing)。

## 三、核心技术

### 1、后端

* SpringBoot：Web 应用框架。
* Mybatis：持久层框架。
* MySQL：关系型数据库。
* Redis：缓存数据库。
* RabbitMQ：消息队列中间件。

### 2、前端

* Themeleaf：模板引擎。
* Bootstrap：UI 框架。

## 四、环境部署

### 1、准备工作

* JDK 1.8
* MySQL 5.7
* Maven 3.x
* Redis
* RabbitMQ

### 2、必要配置

* application.properties 配置文件：数据库、Redis、RabbitMQ、邮箱等配置。
* config/myconfig.properties 配置文件：系统相关的自定义配置。

## 五、项目介绍

### 1、文件结构

```
src/main/java
    |— com.jingqueyimu
        |— annotation        // 注解
        |— aspect        // 切面
        |— config        // 配置
        |— constant        // 常量
        |— context        // 上下文
        |— controller        // 控制层
        |— exception        // 异常
        |— factory        // 工厂
        |— filter        // 过滤器
        |— handler        // 处理器
        |— init        // 初始化
        |— interceptor        // 拦截器
        |— mapper        // 持久层
        |— model        // 数据模型
        |— mq        // 消息队列
        |— schedule        // 调度
        |— service        // 服务层
        |— util        // 工具
        MyAppcation.java        // 应用启动类
src/main/resources
    |— config        // 配置文件
    |— mapper        // 映射文件
    |— static        // 静态文件
    |— templates        // 页面文件
    application.properties        // 应用配置文件
    quartz.properties        // 调度配置文件
```

### 2、代码说明

#### （1）路由

* /api/xxx：需要用户登录。
* /console/xxx：需要管理员登录，登录、登出等部分接口除外。

#### （2）接口规范

* 页面请求：GET、URL 参数。
* 接口请求：POST、JSON 参数。

#### （3）数据库初始化

* 初始化配置文件：config/dbinit.json。
* 配置初始化标识及 SQL 语句。

初始化示例：

```
[
    {
        "initKey": "site_config_20210110",
        "sqls": [
            "INSERT INTO t_site_config (id, code, name, content, description, public_flag, gmt_create) VALUES(NULL, 'site_record_no', '网站备案号', '<a href=\"https://beian.miit.gov.cn\" class=\"ml-2\" target=\"_blank\">备案号</a>', '网站备案号', true, NOW());"
        ]
    }
]
```

#### （4）文件上传下载

* 上传单个文件：/file/upload。
* 上传多个文件：/file/uploads。
* 下载文件：/file/download。

#### （5）Excel 导入导出

* Excel 导入导出方法：ExcelUtil.importExcel()、ExcelUtil.exportExcel()。
* Excel 导入导出处理器接口：IExcelImportHandler、IExcelExportHandler。

导入示例：

```
String msg = null;
try {
    String[] keys = new String[] {"username", "realName", "mobile"};
    msg = ExcelUtil.importExcel(file.getInputStream(), keys, new IExcelImportHandler() {
        
        @Override
        public void handle(JSONObject data) {
            if (StringUtils.isBlank(data.getString("username"))) {
                throw new RuntimeException("用户名不能为空");
            }
            if (StringUtils.isBlank(data.getString("mobile"))) {
                throw new RuntimeException("手机号不能为空");
            }
            // 业务处理
        }
    });
    log.info(msg);
} catch (IOException e) {
    e.printStackTrace();
}
```

导出示例：

```
ServletOutputStream os = null;
try {
    String fileName = "用户列表";
    ...
    // 用户列表数据
    List<User> list = userService.list(params);
    // 表头
    String[] headers = new String[] {"编号 ", "用户名", "姓名", "手机号", "邮箱", "注册方式", "注册时间", "上次登录时间", "是否VIP"};
    os = response.getOutputStream();
    // 导出
    ExcelUtil.exportExcel(fileName, list, headers, os, new IExcelExportHandler<User>() {
        
        @Override
        public List<Object> handle(User user) {
            List<Object> rowDatas = new ArrayList<>();
            rowDatas.add(user.getId());
            rowDatas.add(user.getUsername());
            rowDatas.add(user.getRealName());
            rowDatas.add(user.getMobile());
            rowDatas.add(user.getEmail());
            rowDatas.add(RegisterType.getEnum(user.getRegisterType()).getValue());
            rowDatas.add(DateUtil.format(user.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));
            rowDatas.add(user.getLastLoginTime() == null ? "" : DateUtil.format(user.getLastLoginTime(), "yyyy-MM-dd HH:mm:ss"));
            rowDatas.add(Boolean.TRUE.equals(user.getVipFlag()) ? "是" : "否");
            return rowDatas;
        }
    });
} catch (IOException e) {
    e.printStackTrace();
} finally {
    ...
}
```

#### （6）获取字典

* 获取单个字典：/common/dict。
* 获取多个字典：/common/dicts。

#### （7）上下文信息

* UserContext：用户上下文信息。
* AdminContext：管理员上下文信息。

#### （8）调度

* 新增调度任务：继承 BaseJob。
* 调度接口
    * 执行调度任务：/console/schedule_job/run。
    * 修改调度状态：/console/schedule_job/update_status。
    * 修改调度频率：/console/schedule_job/update_cron。

#### （9）注解

* @Lock：分布式锁。
* @Perm：标注需要管理员权限的接口。

#### （10）配置文件

* 由于个人习惯，项目中使用的是 properties 配置文件，对于习惯使用 yml 配置文件的，请手动修改。
* 如果要加载自定义的 yml 配置文件，可以使用项目中提供的 YAML 属性源工厂类：YamlPropertySourceFactory。

代码示例：

```
@Component
@ConfigurationProperties(prefix="test")
@PropertySource(value="classpath:config/test.yml", encoding="UTF-8", factory=YamlPropertySourceFactory.class)
public class TestYmlConfig {
    ...
}
```
#### （11）属性名后缀匹配查询条件

BaseService 中以 JSON 对象为参数的方法，可通过在属性名后面添加后缀，来匹配查询条件。

* xxx_begin：大于等于。
* xxx_end：小于等于。
* xxx_in：IN 查询。
* xxx_like：模糊查询。
* xxx_llike：左模糊查询。
* xxx_rlike：右模糊查询。
* 其他：等于。
* 属性值为空：不参与查询。

代码示例：

```
@Test
public void test() {
    JSONObject params = new JSONObject();
    params.put("username_like", "test");
    List<User> user = userService.list(params);
    System.out.println(user);
}

@Test
public void test2() {
    JSONObject params = new JSONObject();
    params.put("username_in", Arrays.asList("test"));
    List<User> user = userService.list(params);
    System.out.println(user);
}
```

#### （12）...

## 六、演示图

### 1、前台

![frontindex.jpg](https://www.jingqueyimu.com/upload/2021/02/01/front-index-62449408.jpg)

![frontregister.jpg](https://www.jingqueyimu.com/upload/2021/02/01/front-register-46640493.jpg)

![frontlogin.jpg](https://www.jingqueyimu.com/upload/2021/02/01/front-login-59444485.jpg)

### 2、后台

![backlogin.jpg](https://www.jingqueyimu.com/upload/2021/02/01/back-login-04437295.jpg)

![backhome.jpg](https://www.jingqueyimu.com/upload/2021/02/01/back-home-23328407.jpg)

![backuser.jpg](https://www.jingqueyimu.com/upload/2021/02/01/back-user-33608935.jpg)

![backuserdetail.jpg](https://www.jingqueyimu.com/upload/2021/02/01/back-user-detail-88238395.jpg)

![backadminadd.jpg](https://www.jingqueyimu.com/upload/2021/02/01/back-admin-add-70612669.jpg)

![backadmininfo.jpg](https://www.jingqueyimu.com/upload/2021/02/01/back-admin-info-47481487.jpg)

![backadminpwd.jpg](https://www.jingqueyimu.com/upload/2021/02/01/back-admin-pwd-76484126.jpg)

# 结语

目前，Chewing 还只是提供了一些较为常用的功能（实在是肝不动了）。但是，后续会不断完善、新增功能。有时间的话，也会整一个微服务版的。

敬请期待~

# 交流区

<p align="center">
    <img src="https://www.jingqueyimu.com/images/qrcode.jpg" height="250px" width="250px"><br/>
    微信公众号：惊却一目<br/>
    个人博客：<a href="https://www.jingqueyimu.com">惊却一目</a>
</p>


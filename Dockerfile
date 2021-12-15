# 基础镜像
FROM java:8
# 添加文件到容器中
ADD chewing.jar /package/chewing.jar
# 暴露端口
EXPOSE 9000
# 执行命令（运行jar包）
ENTRYPOINT ["java","-jar","/package/chewing.jar"]

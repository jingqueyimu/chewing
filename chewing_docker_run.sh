#!/bin/bash

# --------------------------------------------------------------------------------------
# docker启动项目脚本
# --------------------------------------------------------------------------------------

# -------------------------------------------------------------------------------------
# 克隆到windows再上传到linux，或者在windows下编辑shell脚本后上传到linux时，
# 由于linux与windows的换行符不同，可能会导致续行符（\）失效，从而导致shell脚本执行报错。
# 此时，可以在linux下使用dos2unix命令（没有的话需要先安装）对文件进行转换。
# -------------------------------------------------------------------------------------

# 应用名
PROJECT=chewing
# 应用版本
VERSION=v1.0.0
# 应用jar包路径
JAR_PATH=/home/wwwroot/chewing
# 应用上传文件路径
FILE_PATH=/data/chewing/file

cd $JAR_PATH
# 构建镜像
docker build -t ${PROJECT}:${VERSION} .
# 停止容器
docker stop ${PROJECT} 2>/dev/null
# 删除容器
docker rm ${PROJECT} 2>/dev/null
# 启动容器
docker run -d -p 9000:9000 -v ${FILE_PATH}:${FILE_PATH} -v ${JAR_PATH}:/package \
  -e spring.datasource.url="jdbc:mysql://localhost:3306/chewing?useAffectedRows=true&serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8" \
  -e spring.datasource.username="root" \
  -e spring.datasource.password="123456" \
  -e spring.redis.host="127.0.0.1" \
  -e myconfig.fileStoragePath=${FILE_PATH} \
  --name ${PROJECT} ${PROJECT}:${VERSION}

# 删除悬挂镜像
docker rmi $(docker images -f "dangling=true" -q) 2>/dev/null

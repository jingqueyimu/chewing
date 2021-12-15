# 应用版本
VERSION=v1.0.0
# 应用jar包路径
JAR_PATH=/home/wwwroot/chewing
# 应用上传文件路径
FILE_PATH=/data/chewing/file

# cd $JAR_PATH
# 构建镜像
# docker build -t chewing:${VERSION} .
# 启动容器
docker run -d -p 9000:9000 -v ${FILE_PATH}:${FILE_PATH} -v ${JAR_PATH}:/package \
  -e spring.datasource.url="jdbc:mysql://localhost:3306/chewing?useAffectedRows=true&serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8" \
  -e spring.datasource.username="root" \
  -e spring.datasource.password="123456" \
  -e spring.redis.host="127.0.0.1" \
  -e myconfig.fileStoragePath=${FILE_PATH} \
  --name chewing chewing:${VERSION}


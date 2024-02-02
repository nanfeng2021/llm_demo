# basic image
FROM openjdk:8-jre-slim
# author
MAINTAINER nanfeng
# config
ENV PARAMS=""
# timezone
ENV TZ=RPC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# add application
ADD target/chatgpt-api.jar /chatgpt-api.jar
## after image runs, exec command
ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /chatgpt-api.jar $PARAMS"]
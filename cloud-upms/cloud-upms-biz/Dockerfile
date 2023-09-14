FROM moxm/java:1.8-full

MAINTAINER djl

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /cloud

WORKDIR /cloud

EXPOSE 4000

ADD ./target/cloud-upms-biz.jar ./

CMD sleep 60;java $JAVA_OPTS -jar cloud-upms-biz.jar

FROM moxm/java:1.8-full

MAINTAINER djl

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /cloud

WORKDIR /cloud

EXPOSE 3000

ADD ./target/cloud-auth.jar ./

CMD sleep 120;java $JAVA_OPTS -jar cloud-auth.jar

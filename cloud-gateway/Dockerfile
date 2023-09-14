FROM moxm/java:1.8-full

MAINTAINER djl

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /cloud

WORKDIR /cloud

EXPOSE 9999

ADD ./target/cloud-gateway.jar ./
ADD ./publicCerts.keystore ./

CMD sleep 180;java $JAVA_OPTS -jar cloud-gateway.jar

FROM seataio/seata-server:1.3.0
WORKDIR /usr/local/bin/
COPY config.txt /usr/local/bin/nacos/config.txt
#COPY nacos/nacos-config.sh /data/nacos/nacos-config.sh
COPY entrypoint.sh /usr/local/bin/nacos/entrypoint.sh
#RUN chmod -R 777 /data/nacos/nacos-config.sh
RUN chmod -R 777 /usr/local/bin/nacos/entrypoint.sh
RUN chmod -R 777 /usr/local/bin/nacos/config.txt
RUN chmod -R +x /usr/local/bin/nacos/entrypoint.sh
ARG seata_port=8091
ARG store_mode=db
ENV SEATA_PORT=$seata_port
ENV SEATA_PORT=$store_mode
EXPOSE 8091
RUN echo  $seata_port $store_mode


  seata-server1:
    build:
      context: ./docker/dockerfile/seata-server-nacos-config/
      args:
        seata_port: 8901
        store_mode: db
    container_name: seata-server1
    environment:
      TZ: Asia/Shanghai
      SLEEP_SECOND: 4
    ports:
      - 8091:8091
    depends_on:
      - mysql1
      - nginx-nacos
    volumes:
      - ./docker/conf/seata/registry.conf:/seata-server/resources/registry.conf
      - ./docker/temp/seat-server1/logs:/root/logs/seata
    entrypoint: sh /usr/local/bin/nacos/entrypoint.sh -d 172.20.1.61:8848,172.20.1.62:8848,172.20.1.63:8848,172.20.1.101:9999
    networks:
      mynetwork:
        # 宿主机会自动配置网关IP：172.20.1.1
        ipv4_address: 172.20.1.201
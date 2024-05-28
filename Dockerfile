FROM java:openjdk-8u111-jdk-alpine

RUN apk update
RUN apk add --no-cache openssh
RUN apk add --no-cache wget
RUN apk add bash

RUN wget --no-check-certificate https://dlcdn.apache.org/hadoop/common/hadoop-3.4.0/hadoop-3.4.0.tar.gz
RUN tar -xzvf hadoop-3.4.0.tar.gz
RUN mv hadoop-3.4.0 /usr/local/hadoop
RUN rm hadoop-3.4.0.tar.gz

ENV JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk
ENV HADOOP_HOME=/usr/local/hadoop
ENV PATH "$PATH:$HADOOP_HOME/sbin"
ENV HDFS_NAMENODE_USER="root"
ENV HDFS_DATANODE_USER="root"
ENV HDFS_SECONDARYNAMENODE_USER="root"
ENV YARN_RESOURCEMANAGER_USER="root"
ENV YARN_NODEMANAGER_USER="root"

RUN mkdir /root/script

ADD configs/start-all.sh /root/scripts/start-all.sh
RUN chmod u+r+x /root/scripts/start-all.sh

ADD configs/hadoop-env.sh $HADOOP_HOME/etc/hadoop/hadoop-env.sh
ADD configs/hdfs-site.xml $HADOOP_HOME/etc/hadoop/hdfs-site.xml
ADD configs/core-site.xml $HADOOP_HOME/etc/hadoop/core-site.xml
ADD configs/mapred-site.xml $HADOOP_HOME/etc/hadoop/mapred-site.xml
ADD configs/yarn-site.xml $HADOOP_HOME/etc/hadoop/yarn-site.xml

RUN ssh-keygen -t rsa -f /root/.ssh/id_rsa -P '' && \
    cat /root/.ssh/id_rsa.pub >> /root/.ssh/authorized_keys
RUN chmod 0600 ~/.ssh/authorized_keys

RUN export PDSH_RCMD_TYPE=ssh
RUN $HADOOP_HOME/bin/hdfs namenode -format

CMD ["sh","-c","/root/scripts/start-all.sh"]
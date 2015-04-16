#!/bin/sh

boolval=0
count=0

while [ $boolval -eq 0 ]
do

	
	IFS=' '

	echo '' > /home/sbcd90/Documents/programs/flume-client/src/main/java/com/sap/i076326/flume_client.java
	while read line
	do
        	case "$line" in
                	*row* ) echo 'stringBuilder.append("row'$count';catalina.2014-12-19.log;");' >> /home/sbcd90/Documents/programs/flume-client/src/main/java/com/sap/i076326/flume_client.java;;
                	* ) echo $line >> /home/sbcd90/Documents/programs/flume-client/src/main/java/com/sap/i076326/flume_client.java;;
        	esac

	done < /home/sbcd90/Documents/programs/flume-client/src/main/java/com/sap/i076326/flume_client.java.template


	sh ~/Documents/softwares/apache-maven-3.1.1/bin/mvn --file ~/Documents/programs/flume-client/pom.xml clean install

	/usr/lib/jvm/java-1.7.0-openjdk-amd64/bin/java -Didea.launcher.port=7534 -Didea.launcher.bin.path=/home/sbcd90/Documents/softwares/idea-IC-141.178.9/bin -Dfile.encoding=UTF-8 -classpath /usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/jce.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/rhino.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/javazic.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/jsse.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/compilefontconfig.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/resources.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/management-agent.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/rt.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/charsets.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/ext/sunjce_provider.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/ext/zipfs.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/ext/localedata.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/ext/java-atk-wrapper.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/ext/sunpkcs11.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/ext/dnsns.jar:/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/ext/icedtea-sound.jar:/home/sbcd90/Documents/programs/flume-client/target/classes:/home/sbcd90/.m2/repository/org/apache/flume/flume-ng-sdk/1.5.2/flume-ng-sdk-1.5.2.jar:/home/sbcd90/.m2/repository/org/apache/avro/avro/1.7.3/avro-1.7.3.jar:/home/sbcd90/.m2/repository/org/codehaus/jackson/jackson-core-asl/1.8.8/jackson-core-asl-1.8.8.jar:/home/sbcd90/.m2/repository/org/codehaus/jackson/jackson-mapper-asl/1.8.8/jackson-mapper-asl-1.8.8.jar:/home/sbcd90/.m2/repository/com/thoughtworks/paranamer/paranamer/2.3/paranamer-2.3.jar:/home/sbcd90/.m2/repository/org/xerial/snappy/snappy-java/1.0.4.1/snappy-java-1.0.4.1.jar:/home/sbcd90/.m2/repository/org/apache/avro/avro-ipc/1.7.3/avro-ipc-1.7.3.jar:/home/sbcd90/.m2/repository/org/apache/velocity/velocity/1.7/velocity-1.7.jar:/home/sbcd90/.m2/repository/commons-collections/commons-collections/3.2.1/commons-collections-3.2.1.jar:/home/sbcd90/.m2/repository/io/netty/netty/3.5.12.Final/netty-3.5.12.Final.jar:/home/sbcd90/.m2/repository/org/apache/thrift/libthrift/0.7.0/libthrift-0.7.0.jar:/home/sbcd90/.m2/repository/org/apache/httpcomponents/httpclient/4.0.1/httpclient-4.0.1.jar:/home/sbcd90/.m2/repository/org/apache/httpcomponents/httpcore/4.0.1/httpcore-4.0.1.jar:/home/sbcd90/.m2/repository/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar:/home/sbcd90/.m2/repository/org/apache/flume/flume-ng-core/1.5.2/flume-ng-core-1.5.2.jar:/home/sbcd90/.m2/repository/org/apache/flume/flume-ng-configuration/1.5.2/flume-ng-configuration-1.5.2.jar:/home/sbcd90/.m2/repository/commons-io/commons-io/2.1/commons-io-2.1.jar:/home/sbcd90/.m2/repository/commons-codec/commons-codec/1.8/commons-codec-1.8.jar:/home/sbcd90/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:/home/sbcd90/.m2/repository/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:/home/sbcd90/.m2/repository/commons-lang/commons-lang/2.5/commons-lang-2.5.jar:/home/sbcd90/.m2/repository/joda-time/joda-time/2.1/joda-time-2.1.jar:/home/sbcd90/.m2/repository/org/mortbay/jetty/servlet-api/2.5-20110124/servlet-api-2.5-20110124.jar:/home/sbcd90/.m2/repository/org/mortbay/jetty/jetty-util/6.1.26/jetty-util-6.1.26.jar:/home/sbcd90/.m2/repository/org/mortbay/jetty/jetty/6.1.26/jetty-6.1.26.jar:/home/sbcd90/.m2/repository/com/google/code/gson/gson/2.2.2/gson-2.2.2.jar:/home/sbcd90/.m2/repository/org/apache/mina/mina-core/2.0.4/mina-core-2.0.4.jar:/home/sbcd90/.m2/repository/org/slf4j/slf4j-api/1.7.7/slf4j-api-1.7.7.jar:/home/sbcd90/.m2/repository/org/slf4j/slf4j-log4j12/1.7.7/slf4j-log4j12-1.7.7.jar:/home/sbcd90/.m2/repository/com/google/guava/guava/18.0/guava-18.0.jar:/home/sbcd90/Documents/softwares/idea-IC-141.178.9/lib/idea_rt.jar com.intellij.rt.execution.application.AppMain com.sap.i076326.flume_client

	count=$((count+1))
done

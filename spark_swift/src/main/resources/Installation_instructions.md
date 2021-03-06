Installation Instructions
=========================

- Install Openstack-Swift

- Download Hadoop 2.6.0 & get the patch for Hadoop-Openstack integration

1. [Hadoop-2.6.0-src] (https://www.apache.org/dist/hadoop/core/hadoop-2.6.0/)
2. Download & extract into the folder named hadoop-2.6.0
2. Download patch [Hadoop-Openstack patch] (https://issues.apache.org/jira/secure/attachment/12754028/HADOOP-10420-012.patch)
3. Put the file under hadoop-2.6.0/hadoop-tools folder
4. Execute the following git commands in a sequence

        git apply --stat HADOOP-10420-012.patch
        git apply --check HADOOP-10420-012.patch
        git apply HADOOP-10420-012.patch

5. Navigate to hadoop-2.6.0/hadoop-tools/hadoop-openstack folder & execute the following commands in a sequence

        mvn -DskipTests clean package
        mvn -DskipTests install

- Download & install Spark from source

1. [Spark source code] (http://spark.apache.org/downloads.html)
2. Add the following dependency in pom.xml in the section dependencyMangement

        <dependencyManagement>
          ...
          <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-openstack</artifactId>
            <version>2.6.0</version>
          </dependency>
          ...
        </dependencyManagement>

3. Build Spark with the following command
        build/mvn -Pyarn -Phadoop-2.6 -Dhadoop.version=2.6.0 -DskipTests clean package


- Add a file called core-site.xml to $SPARK_HOME/conf. A sample core-site.xml can be found in project's src/main/resources folder.

- Run the application

1. Get the jar hadoop-openstack-2.6.0.jar from .m2 & put it in $SPARK_HOME/bin folder
2. Create the jar for the application using mvn clean install
3. Run the application using the following command

        $SPARK_HOME/bin/spark-submit --class "com.sap.appiot.main.TextFileApplication" --jars /home/sbcd90/Documents/programs/spark-1.5.2/bin/hadoop-openstack-2.6.0.jar, target/spark_swift-1.0-SNAPSHOT.jar
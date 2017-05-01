# CS-6367-final-projects
We use common-dbutils-trunk https://github.com/apache/commons-dbutils to do our project.
We have 5 steps.

First, add plugin to the pom.xml. The code-coverage-1.0-SNAPSHOT.jar should be your own path on your own pc.


<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-surefire-plugin</artifactId>
<configuration>
<excludes>
<exclude>**/BaseTestCase.java</exclude>
</excludes>
<argLine>-javaagent:/Users/jiangong/Desktop/testing/AutomatedCodeCoverage-master/Project\ Code/CodeCoverage/target/code-coverage-1.0-SNAPSHOT.jar=${project.groupId}</argLine>
<properties>
<property>
<name>listener</name>
<value>edu.utdallas.JUnitExecutionListener</value>
</property>
</properties>
</configuration>
</plugin>


Second, run the mvn test to get the original hash value.

Third, change the commons-dbutils project's code.

Fourth, rerun the mvn test to check the changed files.

Fifth, run mvn clean test -Dtest=SelectedTestSuite -DfailIfNoTests=false, to rerun the selected test cases.

Then the whole progress has been finished.

# Lahiya
*Test Case Report Library for JUnit*

Lahiya test case report maven plugin for : Java + JUnit + maven-surefire-plugin

Usage: 
```
 <plugin>
    <groupId>com.github.cyildirim</groupId>
    <artifactId>lahiya-test-report-maven-plugin</artifactId>
    <version>0.1</version>
    <configuration>
        <packageName>com.packagename</packageName>
        <suites>
            <suite>SeleniumRegressionTestSuite</suite>
            <suite>ApiRegressionTestSuite</suite>
        </suites>
    </configuration>
</plugin>

```

Command line execution: 

```
mvn com.lahiyareport:lahiya-test-report-maven-plugin:lahiya -DskipTests 

```
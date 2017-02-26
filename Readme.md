# Lahiya
*Test Case Report Library for JUnit*

Lahiya test case report maven plugin for : Java + JUnit + maven-surefire-plugin

##Usage: 
```
 <plugin>
    <groupId>com.github.cyildirim</groupId>
    <artifactId>lahiya-test-report-maven-plugin</artifactId>
    <version>0.1-SNAPSHOT</version>
    <configuration>
        <packageName>com.packagename</packageName>
        <suites>
            <suite>SeleniumRegressionTestSuite</suite>
            <suite>ApiRegressionTestSuite</suite>
        </suites>
    </configuration>
</plugin>

```

## Command line execution: 

```
mvn com.github.cyildirim:lahiya-test-report-maven-plugin:lahiya -DskipTests 

```


##Screenshot 

![lahiya_screenshot](https://raw.githubusercontent.com/cyildirim/Lahiya/34ce8032832a50520adf404394a2202461e5094e/screenshots/lahiya-usage.gif)


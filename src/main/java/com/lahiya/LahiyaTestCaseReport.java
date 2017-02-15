package com.lahiya;

import com.lahiya.annotations.Description;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.junit.experimental.categories.Category;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mojo(name = "lahiya")
public class LahiyaTestCaseReport extends AbstractMojo
{
    private static Reflections reflections;
    private static Logger logger = LoggerFactory.getLogger(LahiyaTestCaseReport.class);

    private static boolean isAnnotationArrayContainsClassName(String className, Class<?>[] annotationArray)
    {
        for (Class<?> aClass : annotationArray)
        {
            if (aClass.getName().contains(className))
            {
                return true;
            }
        }

        return false;
    }

    private static boolean isAnnotationKeyContains(String annotationName, Annotation[] annotations)
    {
        for (Annotation annotation : annotations)
        {
            if (annotation.annotationType().getName().contains(annotationName))
            {
                return true;
            }
        }

        return false;

    }

    private static Map<String, TestClass> printMethods(String searchClass)
    {


        Map<String, TestClass> testClassesMap = new HashMap<>();

        Set<Method> categorySet = reflections.getMethodsAnnotatedWith(Category.class);

        for (Method method : categorySet)
        {
            if (isAnnotationArrayContainsClassName(searchClass, method.getDeclaredAnnotation(Category.class).value()))
            {

                if (testClassesMap.get(method.getDeclaringClass().getName()) == null)
                {
                    Description annotationDesc = method.getDeclaredAnnotation(Description.class);
                    String annotationDescText = "";
                    if (null != annotationDesc)
                    {
                        annotationDescText = annotationDesc.value();
                    }
                    TestMethod tempTestMethod = new TestMethod(method.getName(), annotationDescText, searchClass);
                    TestClass tempTestClass = new TestClass(method.getDeclaringClass().getName(), new ArrayList<>(Arrays.asList(tempTestMethod)));

                    testClassesMap.put(method.getDeclaringClass().getName(), tempTestClass);
                }
                else
                {
                    List<TestMethod> tempList = testClassesMap.get(method.getDeclaringClass().getName()).getTestMethodList();
                    Description annotationDesc = method.getDeclaredAnnotation(Description.class);
                    String annotationDescText = "";
                    if (null != annotationDesc)
                    {
                        annotationDescText = annotationDesc.value();
                    }
                    tempList.add(new TestMethod(method.getName(), annotationDescText, searchClass));
                    testClassesMap.put(method.getDeclaringClass().getName(), new TestClass(method.getDeclaringClass().getName(), tempList));
                }
            }
        }


        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Category.class);

        for (Class classTek : classSet)
        {
            if (classTek.getDeclaredAnnotation(Category.class).toString().contains(searchClass))
            {

                if (testClassesMap.get(classTek.getName()) == null)
                {
                    List<TestMethod> tempList = new ArrayList<>();

                    for (Method method : classTek.getMethods())
                    {
                        if (isAnnotationKeyContains("Test", method.getAnnotations()))
                        {
                            Description annotationDesc = method.getDeclaredAnnotation(Description.class);
                            String annotationDescText = "";
                            if (null != annotationDesc)
                            {
                                annotationDescText = annotationDesc.value();
                            }
                            tempList.add(new TestMethod(method.getName(), annotationDescText, searchClass));
                        }
                    }

                    testClassesMap.put(classTek.getName(), new TestClass(classTek.getName(), tempList));
                }
                else
                {
                    TestClass testClass = testClassesMap.get(classTek.getName());
                    List<TestMethod> tempTestMethodList = testClass.getTestMethodList();
                    tempTestMethodList.add(new TestMethod(classTek.getName(), null, searchClass));

                    testClass.setTestMethodList(tempTestMethodList);

                    testClassesMap.put(classTek.getDeclaringClass().getName(), testClass);
                }

            }

        }

        return testClassesMap;
    }

    public void generateReport(String packageName,List<String> flagList) throws IOException
    {

        reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new MethodAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner())
        );


        List<Map<String, TestClass>> list = new ArrayList<>();

        for (String flag : flagList)
        {
            list.add(printMethods(flag));
        }

        Gson gson = new Gson();
        String overviewTemplate = IOUtils.toString(getClass().getResourceAsStream("/index.tpl.html"));


        String editedTemplate = overviewTemplate.replace("##TEST_DATA##", gson.toJson(list));

        FileUtils.writeStringToFile(new File("target/test-list-html-report/index.html"), editedTemplate);
        logger.info("report file generated");
    }


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
//            TODO parameters should add as list to this area on maven
        List<String> list = Arrays.asList("SeleniumRegressionTestSuite");
        try
        {
//            TODO parameters should add to as string this area on maven
            generateReport("com.sahibinden",list);
        }
        catch (IOException e)
        {
            getLog().error("sorry, report cannot created");
        }
    }
}

import java.util.List;


public class TestClass
{
    private String name;
    private List<TestMethod> testMethodList;

    public TestClass(String name, List<TestMethod> testMethodList)
    {
        this.name = name;
        this.testMethodList = testMethodList;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<TestMethod> getTestMethodList()
    {
        return testMethodList;
    }

    public void setTestMethodList(List<TestMethod> testMethodList)
    {
        this.testMethodList = testMethodList;
    }

}


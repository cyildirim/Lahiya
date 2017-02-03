public class TestMethod
{
    private String name;
    private String description;
    private String testSuite;

    public TestMethod(String name, String description, String testSuite)
    {
        this.name = name;
        this.description = description;
        this.testSuite = testSuite;
    }

    public String getTestSuite()
    {
        return testSuite;
    }

    public void setTestSuite(String testSuite)
    {
        this.testSuite = testSuite;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}

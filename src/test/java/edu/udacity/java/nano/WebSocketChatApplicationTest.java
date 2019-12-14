package edu.udacity.java.nano;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.net.URL;

@RunWith(SpringJUnit4ClassRunner.class) // @RunWith: integrate spring with junit
@SpringBootTest(classes = {WebSocketChatApplication.class},webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // @SpringBootTest: this class is spring boot test.

public class WebSocketChatApplicationTest {

    private WebDriver driver;
    private WebDriver driver2;

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setupTest() {
        driver = new ChromeDriver();
        driver2 = new ChromeDriver();
    }

    @After
    public void teardown() throws Exception{

        if (driver != null) {
            driver.close();

        }
        if(driver2!=null){
            driver2.close();

        }
    }

    public void loginUser(WebDriver driver,String user){
        driver.get("http://localhost:8080");
        driver.findElement(By.id("username")).sendKeys(user);
        driver.findElement(By.tagName("a")).click();

    }

    public String getChatNum(WebDriver driver){
       return driver.findElement(By.className("chat-num")).getText();
    }

    public void leaveChat(WebDriver driver){
        driver.findElement(By.xpath("/html/body/div[1]/div/a")).click();
    }

    public void sendText(WebDriver driver,String msg) throws Exception{
        driver.findElement(By.id("msg")).sendKeys(msg);
        driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div[2]/div[2]/button[1]")).click();

    }



    @Test
    public void testlogin() throws Exception{
        loginUser(driver,"Srini");
        Assert.assertTrue("User is not redirected to index.html after login",driver.getCurrentUrl().contains("index?username=Srini"));

    }
    @Test
    public void testJoinChat() throws Exception{
        loginUser(driver,"Srini");
        Assert.assertEquals("chat number is not incremented after user joins chat","1",getChatNum(driver));
        loginUser(driver2,"Chella");
        Assert.assertEquals("chat number is not incremented in the previous user page after new user joins chat","2",getChatNum(driver));
        Assert.assertEquals("chat number is not shown properly in the new user page","2",getChatNum(driver2));
    }
    @Test
    public void testLeaveChat() throws Exception{
        loginUser(driver,"Srini");
        loginUser(driver2,"Chella");
        leaveChat(driver);
        Assert.assertEquals("chat number is not decremented after a user leaves chat","1",getChatNum(driver2));

    }
    public String getMessageContent(WebDriver driver,int index){
       return driver.findElements(By.className("message-content")).get(index).getText().toString().trim();
    }
    @Test
    public void testChatMessage() throws Exception{
        loginUser(driver,"Srini");
        loginUser(driver2,"Chella");

        sendText(driver,"I am User1");

        sendText(driver2,"I am User2");
        Thread.sleep(2000);

        Assert.assertTrue("User1 Message should appear should appear in User1 chat window",getMessageContent(driver,0).contains("I am User1"));
        Assert.assertTrue("User2 Message should appear should appear in User1 chat window",getMessageContent(driver,1).contains("I am User2"));


        Assert.assertTrue("User1 Message should appear should appear in User2 chat window",getMessageContent(driver2,0).contains("I am User1"));
        Assert.assertTrue("User2 Message should appear should appear in User2 chat window",getMessageContent(driver2,1).contains("I am User2"));


    }

}

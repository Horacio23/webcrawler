import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class WebCrawler {

    private WebDriver driver;

    public WebCrawler(WebDriver driver){
        this.driver = driver;
    }

    public static void main(String ...args){

        System.setProperty("webdriver.chrome.driver","C:\\Users\\Horacio\\Downloads\\chromedriver_win32\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebCrawler crawler = new WebCrawler(driver);
        String csvFile = args[0];

        try {
            CSVUtils csv = new CSVUtils(csvFile);
            while(csv.hasNext()){
                List<String> instruction = csv.next();
                if(instruction.size()>1) {
                    crawler.executeAction(crawler.determineSelector(instruction.get(2), instruction.get(3)), instruction.get(0), instruction.get(1));
                }else{
                    crawler.goToUrl(instruction.get(0));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Program Finished");
        //action    actionInput    criteria    target

    }

    public void goToUrl(String url){
        driver.get(url);

    }
    public  void executeAction(WebElement element, String action, String actionInput) {
        switch (action){
            case "click":
                element.click();
                break;
            case "sendKeys":
                element.sendKeys(actionInput);
                break;
            default:
                System.out.println("");
                throw new UnsupportedOperationException("Action not supported: "+action);
        }
    }

    public  WebElement determineSelector(String type, String criteria){
       By selector = null;

       switch (type){
           case "name":
               selector = By.name(criteria);
               break;
           case "className":
               selector = By.className(criteria);
               break;
           case "cssSelector":
               selector = By.cssSelector(criteria);
               break;
           case "id":
               selector = By.id(criteria);
               break;
           case "tagName":
               selector = By.tagName(criteria);
               break;
           case "xpath":
               selector = By.xpath(criteria);
               break;
           default:
               throw new UnsupportedOperationException("Selector type not supported: " + type);
       }

       WebElement element = driver.findElement(selector);
       return element;
    }
}

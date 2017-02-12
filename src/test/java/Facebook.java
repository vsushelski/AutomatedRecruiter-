import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;
import Objects.*;

/**
 * Created by vsushelski on 2/1/2017.
 */

public class Facebook {

    public WebDriver driver;

    @BeforeTest
    public void before(){

        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);

        System.setProperty("webdriver.chrome.driver", "chromedriver_win32/chromedriver.exe");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    public void testFacebook() throws InterruptedException, AWTException {

        driver.get("https://www.facebook.com");
        WebElement username = driver.findElement(By.id("email"));
        WebElement password = driver.findElement(By.id("pass"));
        WebElement btnLogIn = driver.findElement(By.id("loginbutton")).findElement(By.tagName("input"));
        username.sendKeys("v_sushelski@yahoo.com");
        password.sendKeys("Sus1212361_w550");
        btnLogIn.click();
        Thread.sleep(1500);
        driver.get("https://www.facebook.com/pages/%D0%98%D0%B7%D0%BB%D0%B5%D1%82/195120673854320");
        //driver.get("https://www.facebook.com/pages/Endava/1664697343809431");
        Thread.sleep(1500);


        Robot robot = new Robot();
        for(int i=0; i<40000; i++){
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            Thread.sleep(1);
        }

        Thread.sleep(1500);

        WebElement divPosts = driver.findElement(By.id("vertex_feed_container"));
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        Set<String> people = new HashSet<String>();
        List<WebElement> listLinks = divPosts.findElements(By.cssSelector("a[class='profileLink']"));
        List<WebElement> listOthers = divPosts.findElements(By.cssSelector("a[data-hover='tooltip'][role='button'][data-tooltip-position='below'][rel='dialog']"));

        for (WebElement other:listOthers) {
            String namesAll = other.getAttribute("data-tooltip-content");
            String[] names = namesAll.split("\n");
            for(int j=0; j<names.length; j++){
                list2.add(names[j]);
            }
        }

        for (WebElement profile:listLinks) {
            String str = profile.getText();
            people.add(profile.getText());
            list1.add(str);
        }

        people.addAll(list2);

        makeExcel(people);
        System.out.println("dsf");


    }

    @Test
    public void checkLinkedin() throws InterruptedException {


        List<LinkedinProfile> finalList = new ArrayList<LinkedinProfile>();
        String name1 = "Nena";
        String surname1 = "Dimovska";
        driver.get("https://www.linkedin.com/");
        WebElement username = driver.findElement(By.id("login-email"));
        WebElement password = driver.findElement(By.id("login-password"));
        username.sendKeys("v_sushelski@yahoo.com");
        password.sendKeys("Sus1212361_w550");
        WebElement btnLogIn = driver.findElement(By.id("login-submit"));
        btnLogIn.click();
        Thread.sleep(1000);
        WebElement search = driver.findElement(By.id("main-search-box"));
        search.clear();
        search.sendKeys(name1 + " " + surname1);
        WebElement searchButton = driver.findElement(By.cssSelector("button[name='search']"));
        searchButton.click();
        Thread.sleep(1000);

        WebElement divResults = driver.findElement(By.cssSelector("ol[id='results']"));
        List<WebElement> listresults = divResults.findElements(By.tagName("li"));
        List<WebElement> results = new ArrayList<WebElement>();
        for (WebElement li:listresults) {
            String className = li.getAttribute("class");
            if(className.contains("mod result idx")){
                results.add(li);
            }
        }

        for (WebElement contact:results) {
            try {


                LinkedinProfile profile = new LinkedinProfile();
                WebElement link = contact.findElement(By.cssSelector("a[class='title main-headline']"));
                profile.setLink(link.getAttribute("href"));
                List<WebElement> namesurname = link.findElements(By.tagName("b"));
                WebElement description = contact.findElement(By.className("description"));
                profile.setDescription(description.getText());
                profile.setName(namesurname.get(0).getText());
                profile.setSurname(namesurname.get(1).getText());
                WebElement demo = contact.findElement(By.className("demographic"));
                List<WebElement> listDetails = demo.findElements(By.tagName("dd"));
                profile.setIndustry(listDetails.get(1).getText());
                profile.setLocation(listDetails.get(0).findElement(By.tagName("bdi")).getText());
                finalList.add(profile);
            }
            catch (Exception e){
                System.out.println("Exception");
            }
        }

        makeExcelLinkedin(finalList);

        System.out.println("fdsgd");


    }

    public void makeExcelLinkedin(List<LinkedinProfile> people) {
        try {
            String filename = "src/main/resources/Employee/LinkedinPeople5.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("People");

            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell(0).setCellValue("No.");
            rowhead.createCell(1).setCellValue("Name");
            rowhead.createCell(2).setCellValue("Surname");
            rowhead.createCell(3).setCellValue("Description");
            rowhead.createCell(4).setCellValue("Location");
            rowhead.createCell(5).setCellValue("Industry");
            rowhead.createCell(6).setCellValue("Link");


            for (int i = 0; i < people.size(); i++) {
                HSSFRow row = sheet.createRow((short) i + 1);
                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(people.get(i).getName());
                row.createCell(2).setCellValue(people.get(i).getSurname());
                row.createCell(3).setCellValue(people.get(i).getDescription());
                row.createCell(4).setCellValue(people.get(i).getLocation());
                row.createCell(5).setCellValue(people.get(i).getIndustry());
                row.createCell(6).setCellValue(people.get(i).getLink());

            }


            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("Your excel file has been generated!");

        } catch ( Exception ex ) {
            System.out.println("Exception");
        }
    }

    public void makeExcel(Set people){
        List<String> list = new ArrayList<String>(people);
        try {
            String filename = "src/main/resources/Employee/IzletPeople5.xls" ;
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("People");

            HSSFRow rowhead = sheet.createRow((short)0);
            rowhead.createCell(0).setCellValue("No.");
            rowhead.createCell(1).setCellValue("Name");
            rowhead.createCell(2).setCellValue("Surname");

            for (int i=0;i<list.size();i++){
                if(list.get(i).contains(" ")){
                    String[] nameSurname = list.get(i).split(" ");
                    HSSFRow row = sheet.createRow((short)i+1);
                    row.createCell(0).setCellValue(i);
                    row.createCell(1).setCellValue(nameSurname[0]);
                    row.createCell(2).setCellValue(nameSurname[1]);
                }
            }

            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("Your excel file has been generated!");

        } catch ( Exception ex ) {
            System.out.println(ex);
        }
    }

}

package com.bars.tests;
import com.bars.pages.NewKreditFoPage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.StringContains.containsString;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class KreditFoTest {
    private static WebDriver driver;
    public static NewKreditFoPage newKreditFoPage;
    private Select select;

    @BeforeClass
    public static void setup() {
        String browser = new File(KreditFoTest.class.getResource("/IEDriverServer.exe").getFile()).getPath();
        System.setProperty("webdriver.ie.driver", browser);
        driver = new InternetExplorerDriver();
        newKreditFoPage = new NewKreditFoPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("http://10.10.17.22:8080/barsroot/account/login/");

    }
    public void userDelay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Select getSelect(WebElement element) {
        select = new Select(element);
        return select;
    }

    @Test
    public void KreditFo() {

        WebElement loginField = driver.findElement(By.id("txtUserName"));
        loginField.clear();
        loginField.sendKeys("absadm01");
        WebElement passwordField = driver.findElement(By.id("txtPassword"));
        passwordField.sendKeys("qwerty");
        WebElement loginButton = driver.findElement(By.id("btLogIn"));
        loginButton.click();
        WebElement ProdovjButton = driver.findElement(By.xpath("//input[@value = 'Продовжити']"));
        ProdovjButton.click();

        driver.switchTo().frame(driver.findElement(By.id("mainFrame")));
        userDelay(1000);
        WebElement H1 = driver.findElement(By.xpath(".//*[text()='Оголошення']"));
        (new WebDriverWait(driver, 60))
                .until(ExpectedConditions.visibilityOf(H1));
        driver.switchTo().defaultContent();

        //!!!!Портфель НОВИХ кредитів ФО!!!!
        userDelay(1000);
        newKreditFoPage.findOpersInput("Портфель НОВИХ кредитів ФО");
        Actions builder = new Actions(driver);
        builder.sendKeys(Keys.ENTER).perform();
        WebElement PortKreditFo = driver.findElement(By.xpath("//*[@class='oper-name']/span[text()='Портфель НОВИХ кредитів ФО']"));
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOf(PortKreditFo));
        PortKreditFo.click();
        userDelay(1000);

        //!!!! Перехід на фрейм!!!!
        driver.switchTo().frame(driver.findElement(By.id("mainFrame")));
        userDelay(1000);
//        WebElement FilterWindowQ = driver.findElement(By.xpath(".//*[text()='Фільтр перед населенням таблиці']"));
//        (new WebDriverWait(driver, 100))
//                .until(ExpectedConditions.visibilityOf(FilterWindowQ));
//
//        WebElement FurtherButtonQ = driver.findElement(By.xpath("(//*[@class='x-btn-inner x-btn-inner-center'])[text()='Далі']"));
//        (new WebDriverWait(driver, 30))
//                .until(ExpectedConditions.visibilityOf(FurtherButtonQ));
//        FurtherButtonQ.click();
//        userDelay(1000);

        WebElement AddKreditFo = driver.findElement(By.cssSelector("a[data-qtip='КП: Введення Нового КД']"));
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(AddKreditFo));
        String PortfNewKD = driver.getWindowHandle();
        final Set<String> oldWindowsSet = driver.getWindowHandles();
        AddKreditFo.click();
        userDelay(500);

        for(String windowsHandls : driver.getWindowHandles()) {
            if(!windowsHandls.equals(PortfNewKD)){
                driver.switchTo().window(windowsHandls);
                driver.manage().window().maximize();
            }
        }

        userDelay(4000);


        // !!!!Кредитний договір!!!!
        WebElement Num = driver.findElement(By.xpath("//*[text()='№ договору']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(Num));
        WebElement NumDog = driver.findElement(By.xpath("//*[@ng-model='credit.numValue']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(NumDog));
        //Вставка рандомного числа
        Random random = new Random();
        int num = random.nextInt((1000)+100);
        String str = Integer.toString(num);
        NumDog.sendKeys(str);

        System.out.println(str);

        WebElement Summ = driver.findElement(By.xpath("//*[@class='k-numeric-wrap k-state-default'][1]"));
        Summ.click();
        Summ.sendKeys(str);
        //выбираем бранч
        WebElement Inic = driver.findElement(By.xpath("//label[text()='Ініціатива']//following-sibling::button"));
        String ParamKdWindow = driver.getWindowHandle();
        Inic.click();
        userDelay(1000);
        for(String windowsHandls : driver.getWindowHandles()) {
            driver.switchTo().window(windowsHandls);
        }
        userDelay(1000);

            //нажимаем на фильтр Бранча
        WebElement BranchFilter = driver.findElement(By.xpath("//th[@data-field='BRANCH']/a[@class='k-grid-filter']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(BranchFilter));
        BranchFilter.click();
        userDelay(1000);
        newKreditFoPage.FilterInput("/300465/");
        builder.sendKeys(Keys.ENTER).perform();
        driver.switchTo().window(ParamKdWindow);
        //нажимаем на кнопку ОКПО
        userDelay(1000);
        WebElement OKPObutton = driver.findElement(By.xpath("//label[text()='OKПO']/preceding::button[@class='pf-icon pf-16 pf-book k-button'][1]"));
        OKPObutton.click();
        userDelay(1000);
        for(String windowsHandls : driver.getWindowHandles()) {
            driver.switchTo().window(windowsHandls);
        }
            //нажимаем на фильтр ОКПО
        WebElement OKPOfilter = driver.findElement(By.xpath("//th[@data-field='RNK']/a[@class='k-grid-filter']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(OKPOfilter));
        OKPOfilter.click();
        userDelay(1000);

        newKreditFoPage.FіnputClick();
        userDelay(500);
        newKreditFoPage.FilterInput("96281701");
        userDelay(500);
        builder.sendKeys(Keys.ENTER).perform();
        driver.switchTo().window(ParamKdWindow);
        userDelay(500);
        //Блок Відсотки
        //нажимаем на кнопку відсотки
        WebElement RateButton = driver.findElement(By.xpath("//input[@id='refBaseNameRate']/preceding-sibling::button"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(RateButton));
        RateButton.click();
        userDelay(500);
        for(String windowsHandls : driver.getWindowHandles()) {
            driver.switchTo().window(windowsHandls);
        }
        //нажимаем на фильтр відсотки
        WebElement Ratefilter = driver.findElement(By.xpath("//th[@data-field='BR_ID']/a[@class='k-grid-filter']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(Ratefilter));
        Ratefilter.click();
        userDelay(500);
        newKreditFoPage.FіnputClick();
        userDelay(500);
        newKreditFoPage.FilterInput("9999");
        builder.sendKeys(Keys.ENTER).perform();
        driver.switchTo().window(ParamKdWindow);

        //выбираем тип кредита
        userDelay(500);
        WebElement TipKr = driver.findElement(By.xpath("//span[text()='Самостійно залучені кошти']/preceding::span[@class='k-select'][1]"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(TipKr));
        TipKr.click();
        userDelay(1000);
        WebElement FlStandart = driver.findElement(By.xpath("//li[text()='ФЛ стандарт']"));
        FlStandart.click();
        //выбираем цель
        WebElement GoalKr = driver.findElement(By.xpath("//span[text()='Самостійно залучені кошти']/following::span[@class='k-select'][2]"));
        GoalKr.click();
        userDelay(1000);
        WebElement Potoch = driver.findElement(By.xpath("//li[text()='Поточна дiяльнiсть']"));
        Potoch.click();

        //нажимаем на кнопку Продукт
        WebElement ProdButton = driver.findElement(By.xpath("//input[@id='refProd']/following-sibling::button"));
        ProdButton.click();
        userDelay(1000);
        for(String windowsHandls : driver.getWindowHandles()) {
            driver.switchTo().window(windowsHandls);
        }
        WebElement VidminButton = driver.findElement(By.xpath("//button[text()='Відмінити']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(VidminButton));
        VidminButton.click();
        driver.switchTo().window(ParamKdWindow);
        userDelay(1000);
        ProdButton.click();
        userDelay(1000);
        for(String windowsHandls : driver.getWindowHandles()) {
            driver.switchTo().window(windowsHandls);
        }
        //нажимаем на фильтр продукт
        WebElement Prodfilter = driver.findElement(By.xpath("//th[@data-field='ID']/a[@class='k-grid-filter']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(Prodfilter));
        Prodfilter.click();
        userDelay(1000);
        newKreditFoPage.FilterInput("220301");
        builder.sendKeys(Keys.ENTER).perform();
        driver.switchTo().window(ParamKdWindow);

        WebElement DateZakl = driver.findElement(By.xpath("//input[@k-ng-model='credit.conslValue']"));
        DateZakl.click();
        String Den = DateZakl.getAttribute("value");

        //перехід на вкладку Дані про погашення
        WebElement Pogash = driver.findElement(By.xpath("//*[text()='Дані про погашення']"));
        Pogash.click();
        WebElement FirtDay = driver.findElement(By.xpath("//input[@name='tbDayOfPay']/preceding-sibling::input"));
        FirtDay.sendKeys("1");
        WebElement FirtsPayDate = driver.findElement(By.xpath("//input[@name='dpFirtsPayDate']"));
        FirtsPayDate.click();
        FirtsPayDate.sendKeys(Den);

        System.out.println(Den);


        //перехід на вкладку Дод. параметри КД
        // Вкладка Основні
        userDelay(3000);
        WebElement DodParam = driver.findElement(By.xpath("//span[text()='Дод. параметри КД']"));
        DodParam.click();
        WebElement StrahKred = driver.findElement(By.xpath("(//*[text()='Страхування кредиту']/following::a)[1]"));
        String DodParamWindow = driver.getWindowHandle();
        StrahKred.click();
        userDelay(500);
        for(String windowsHandls : driver.getWindowHandles()) {
            driver.switchTo().window(windowsHandls);
        }
        //нажимаем на фильтр Страхування кредиту
        WebElement Strahfilter = driver.findElement(By.xpath("//th[@data-field='ID']/a[@class='k-grid-filter']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(Strahfilter));
        Strahfilter.click();
        userDelay(500);
        newKreditFoPage.FilterInput("YES");
        builder.sendKeys(Keys.ENTER).perform();
        driver.switchTo().window(DodParamWindow);
        userDelay(500);
        newKreditFoPage.clickOnovButon();
        // Вкладка Додаткові
        WebElement Dodatkovi = driver.findElement(By.xpath("//span[text()='Додаткові']"));
        Dodatkovi.click();
        WebElement KredProd = driver.findElement(By.xpath("(//*[text()='Кредитний продукт']/following::a)[1]"));
        String DodatkoviWindow = driver.getWindowHandle();
        KredProd.click();

        for(String windowsHandls : driver.getWindowHandles()) {
            driver.switchTo().window(windowsHandls);
        }
        //нажимаем на фильтр Кредитний Продукт
        WebElement CprodId = driver.findElement(By.xpath("//th[@data-field='CPROD_ID']/a[@class='k-grid-filter']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(CprodId));
        CprodId.click();
        userDelay(500);

        newKreditFoPage.FіnputClick();
        userDelay(500);
        newKreditFoPage.FilterInput("2");
        builder.sendKeys(Keys.ENTER).perform();
        driver.switchTo().window(DodatkoviWindow);
        userDelay(500);
        newKreditFoPage.clickOnovButon();

        // Вкладка Застава
        WebElement Zastava = driver.findElement(By.xpath("//span[text()='Застава']"));
        Zastava.click();
        WebElement Notarius = driver.findElement(By.xpath("(//*[text()='ПІБ нотаріуса']/following::a)[1]"));
        String ZastavaWindow = driver.getWindowHandle();
        Notarius.click();

        for(String windowsHandls : driver.getWindowHandles()) {
            driver.switchTo().window(windowsHandls);
        }
        //нажимаем на фильтр Кредитний Продукт
        WebElement NotariusId = driver.findElement(By.xpath("//th[@data-field='ID']/a[@class='k-grid-filter']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(NotariusId));
        NotariusId.click();
        userDelay(500);

        newKreditFoPage.FіnputClick();
        userDelay(500);
        newKreditFoPage.FilterInput("2134");
        builder.sendKeys(Keys.ENTER).perform();
        driver.switchTo().window(DodatkoviWindow);
        userDelay(500);
        newKreditFoPage.clickOnovButon();

        //Нажимаем на кнопку "Зберігти"
        WebElement SaveButton = driver.findElement(By.xpath("//button[@class='k-button k-toolbar-first-visible k-toolbar-last-visible']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(SaveButton));
        SaveButton.click();
        userDelay(15000);

        //Сообщение о создании КД
        WebElement KDmessage = driver.findElement(By.xpath("//*[contains(text(), 'Створено КД')]"));
        (new WebDriverWait(driver, 200))
                .until(ExpectedConditions.visibilityOf(KDmessage));
        WebElement OkButton = driver.findElement(By.xpath("//button[@class='delete-confirm k-button k-primary']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(OkButton));
        OkButton.click();

        userDelay(3000);
        System.out.println("New window title: " + driver.getTitle());
        driver.close();

        driver.switchTo().window(PortfNewKD);
        System.out.println("Old window title: " + driver.getTitle());

        driver.switchTo().frame(driver.findElement(By.id("mainFrame")));
        userDelay(500);
        WebElement OnovGrid = driver.findElement(By.xpath("//span[@class='x-btn-icon-el x-tbar-loading ']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(OnovGrid));
        OnovGrid.click();
        userDelay(2000);
        String fileName = String.format( "//*[text()='ФЛ стандарт']/preceding::*[text()='%s']", str);
        WebElement ZovNumKD = driver.findElement(By.xpath(fileName));
                (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(ZovNumKD));
        ZovNumKD.click();
        WebElement AvtorButton = driver.findElement(By.xpath("//a[@data-qtip='КД: Авторизація КД']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(AvtorButton));
        AvtorButton.click();
        WebElement TakButton = driver.findElement(By.xpath("(//*[@class='x-btn-inner x-btn-inner-center'])[text()='Так']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(TakButton));
        TakButton.click();
        WebElement VidAvtor = driver.findElement(By.xpath("//input[@name='M']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(VidAvtor));
        VidAvtor.sendKeys("0");
        WebElement VikonButton = driver.findElement(By.xpath("(//*[@class= 'x-btn-inner x-btn-inner-center'])[text()='Виконати']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(VikonButton));
        VikonButton.click();
        userDelay(4000);
        WebElement OnovOK = driver.findElement(By.xpath("(//*[@class='x-btn-inner x-btn-inner-center'])[text()='OK']"));
        OnovOK.click();

        // Портфель робочих кредитів ФО
        driver.switchTo().defaultContent();

        newKreditFoPage.findOpersInput("Портфель РОБОЧИХ кредитів ФО");

//        Actions builder = new Actions(driver);

        builder.sendKeys(Keys.ENTER).perform();
        WebElement PortRobKreditFo = driver.findElement(By.xpath("//*[@class='oper-name']/span[text()='Портфель РОБОЧИХ кредитів ФО']"));
        (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.visibilityOf(PortRobKreditFo));
        PortRobKreditFo.click();
        userDelay(1000);

        //!!!! Перехід на фрейм!!!!
        driver.switchTo().frame(driver.findElement(By.id("mainFrame")));
        userDelay(1000);
        WebElement ClearFilt = driver.findElement(By.xpath("(//*[@class='x-btn-inner x-btn-inner-center'])[text()='Скасувати фільтри']"));
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(ClearFilt));
        ClearFilt.click();

        WebElement ZvichFilt = driver.findElement(By.xpath("(//*[@class='x-tab-inner x-tab-inner-center'])[text()='Звичайні']"));
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(ZvichFilt));
        ZvichFilt.click();
        WebElement ZvichFiltZovnN  = driver.findElement(By.xpath("//input[@name='CC_ID']"));
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(ZvichFiltZovnN));
        ZvichFiltZovnN.sendKeys(str);
        WebElement FurtherButton = driver.findElement(By.xpath("(//*[@class='x-btn-inner x-btn-inner-center'])[text()='Далі']"));
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(FurtherButton));
        FurtherButton.click();
        userDelay(3000);

        String NKD = String.format( "//*[text()='ФЛ стандарт']/preceding::*[text()='%s']", str);
        WebElement ZovNRobKD = driver.findElement(By.xpath(NKD));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(ZovNRobKD));
        ZovNRobKD.click();
        WebElement GPKButton = driver.findElement(By.xpath("//a[@data-qtip='КД: Побудова ГПК для обраного КД']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(GPKButton));


        String PortfRobKD = driver.getWindowHandle();
        final Set<String> PortfRobKDWindows = driver.getWindowHandles();
        GPKButton.click();


        for(String windowsHandls : driver.getWindowHandles()) {
            if(!windowsHandls.equals(PortfRobKD)){
                driver.switchTo().window(windowsHandls);
                driver.manage().window().maximize();
            }
        }
        userDelay(4000);
        String Title = driver.getTitle();
        System.out.println(Title);
        userDelay(5000);
        WebElement PogashBorg  = driver.findElement(By.xpath("(//tfoot/tr/td/div)[6]"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(PogashBorg));
        String SumPogashBorg = PogashBorg.getText();
        System.out.println(SumPogashBorg);
        Assert.assertEquals(str+".00", SumPogashBorg);

        System.out.println("New window title: " + driver.getTitle());
        userDelay(3000);
        driver.close();

        driver.switchTo().window(PortfRobKD);
        System.out.println("Old window title: " + driver.getTitle());

        driver.switchTo().frame(driver.findElement(By.id("mainFrame")));
        userDelay(500);

        //Графік подій по портфелю
        ZovNRobKD.click();
        WebElement GPPortf = driver.findElement(By.xpath("//a[@data-qtip='КП: Графік подій по портфелю']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(GPPortf));
        GPPortf.click();


        for(String windowsHandls : driver.getWindowHandles()) {
            if(!windowsHandls.equals(PortfRobKD)){
                driver.switchTo().window(windowsHandls);
                driver.manage().window().maximize();
            }
        }
        userDelay(2000);
        WebElement Z = driver.findElement(By.xpath("//input[@name ='B']"));
        Z.sendKeys(Den);

        WebElement Po = driver.findElement(By.xpath("//input[@name ='E']"));
        Po.sendKeys(Den);

        WebElement VikonButton1 = driver.findElement(By.xpath("(//*[@class= 'x-btn-inner x-btn-inner-center'])[text()='Виконати']"));
        (new WebDriverWait(driver, 50))
                .until(ExpectedConditions.visibilityOf(VikonButton1));
        VikonButton1.click();

        WebElement ClearFilt1 = driver.findElement(By.xpath("(//*[@class='x-btn-inner x-btn-inner-center'])[text()='Скасувати фільтри']"));
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(ClearFilt1));
        ClearFilt1.click();

        WebElement ZvichFilt1 = driver.findElement(By.xpath("(//*[@class='x-tab-inner x-tab-inner-center'])[text()='Звичайні']"));
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(ZvichFilt1));
        ZvichFilt1.click();
        WebElement ZvichFiltZovnN1  = driver.findElement(By.xpath("//input[@name='CC_ID']"));
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(ZvichFiltZovnN1));
        ZvichFiltZovnN1.sendKeys(str);  // Потом Раскоментировать!!!!
        WebElement FurtherButton1 = driver.findElement(By.xpath("(//*[@class='x-btn-inner x-btn-inner-center'])[text()='Далі']"));
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(FurtherButton1));
        FurtherButton1.click();

        userDelay(10000);

        WebElement ProgrBar = driver.findElement(By.xpath("//*[@class = 'x-mask-msg-text']"));
        (new WebDriverWait(driver, 300))
                .until(ExpectedConditions.invisibilityOf(ProgrBar));
        List<WebElement> elements = driver.findElements(By.xpath("//div[@class='x-grid-cell-inner ']"));
        (new WebDriverWait(driver, 300))
                .until(ExpectedConditions.visibilityOfAllElements(elements));

        List<String> texts = elements.stream().map(WebElement::getText).collect(Collectors.toList());
        String expected = str;
        assertThat("None of elements contains sub-string", texts, hasItem(containsString(expected)));
        driver.close();
        driver.switchTo().window(PortfRobKD);
        //!!!! Вихід з фрейму!!!!
        driver.switchTo().defaultContent();


    }
    @AfterClass
    public static void tearDown() {
        WebElement profileButton = driver.findElement(By.id("btnProfile"));
        profileButton.click();
        WebElement logoutButton = driver.findElement(By.xpath("//*[@id='userProfile']/div[2]/a[2]"));
        logoutButton.click();
        driver.quit();
    }

}


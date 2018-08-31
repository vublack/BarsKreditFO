package com.bars.pages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class NewKreditFoPage {
    public NewKreditFoPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }
    public WebDriver driver;

    @FindBy(xpath = "//div[text()='Рядки із записами']/following-sibling::span[@class='k-widget k-numerictextbox']")
    private WebElement Fіnput;


    public void FіnputClick (){
        Fіnput.click();
    }

    @FindBy(xpath = "//input[@data-bind='value:filters[0].value']")
    private WebElement FilterInputField;


    public void FilterInput(String znach) {
        FilterInputField.sendKeys(znach);
    }


    @FindBy(xpath = "//a[@class='k-button k-button-icontext k-primary k-grid-update']")
    private WebElement OnovButon;

    public void clickOnovButon() {
        OnovButon.click();
    }

// Пошук функцій
    @FindBy(id = "findOpersText")
    private WebElement findOpers;

    public void findOpersInput(String znach) {
        findOpers.clear();
        findOpers.sendKeys(znach);
    }


}

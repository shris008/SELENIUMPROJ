package com.devskiller.selenium;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumExecutor implements Executor {

	private final WebDriver driver;

	public SeleniumExecutor(WebDriver driver) {
		this.driver = driver;
	}
	
	// wait for the visibility of the element
	public void waitForElementVisibility(WebElement webElement) {

		WebDriverWait wait = new WebDriverWait(driver,500); 
		wait.until(ExpectedConditions.visibilityOf(webElement)); 
	}

	/// Page 1
	@Override
	public void SetLoginAndClickNext(String login){

		//Enter login details
		WebElement loginElement = driver.findElement(By.id("emailBox"));
		waitForElementVisibility(loginElement);
		loginElement.sendKeys(login);
		//Click on next button
		WebElement nextButton = driver.findElement(By.xpath("//button[contains(text(),'Next')]"));
		waitForElementVisibility(nextButton);
		nextButton.click();
	}

	/// Page 2
	@Override
	public String OpenCodePageAndReturnCode(){
		//storing main page
		String mainWindow = driver.getWindowHandle();
		//click on open page
		WebElement OpenPageLink = driver.findElement(By.xpath("//a[contains(text(),'open page')]"));
		waitForElementVisibility(OpenPageLink);
		OpenPageLink.click();
		//switch to second window
		for (String childWindow : driver.getWindowHandles()) {
			if(!childWindow.equals(mainWindow)){
				driver.switchTo().window(childWindow);
			}
		} 
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//get the auth code 
		WebElement authCode = driver.findElement(By.id("code"));
		String code = authCode.getAttribute("value"); 
		// switch to main window
		driver.switchTo().window(mainWindow);
		return code;
	}

	@Override
	public void SetCodeAndClickNext(String code){

		// enter auth code in the codebox
		driver.findElement(By.id("codeBox")).sendKeys(code); 
		// click on next button
		WebElement nextButton =driver.findElement(By.xpath("//*[@id='carouselExampleControls']/div/div[2]/div/div[3]/a/button"));
		waitForElementVisibility(nextButton);
		nextButton.click(); 
	}

	/// Page 3
	@Override
	public void FillMaskedPasswordAndClickLogin(String password){

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String[] passwordArray = password.split("");
		driver.switchTo().frame(0);
		List<WebElement> passwordField = driver.findElements(By.xpath("//div[1]/div[2]/div/input"));
		
		for (int i=0; i<passwordArray.length;i++) {
			if(passwordField.get(i).isEnabled()) {

				passwordField.get(i).sendKeys(passwordArray[i]); // enter the password
			}
		}
	}

	@Override
	public String GetLoggedInText(){
		//click on login button
		WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Log in')]"));
		waitForElementVisibility(loginButton);
		loginButton.click();
		//get the login message
		WebElement loginMsg = driver.findElement(By.id("loggedIn"));
		waitForElementVisibility(loginMsg);
		return loginMsg.getText();
	}

}

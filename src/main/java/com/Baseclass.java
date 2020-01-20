package com;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;


import io.github.bonigarcia.wdm.WebDriverManager;

public class Baseclass {

	public static WebDriver driver;
	String baseURL;

	public void webLaunch(String browser) {

		if (browser.equalsIgnoreCase("Edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else if (browser.equalsIgnoreCase("Chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("Firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}

		EventFiringWebDriver eventdriver = new EventFiringWebDriver(driver);
		EventListener event = new EventListener();
		eventdriver.register(event);
		driver = eventdriver;

		Util.implicitlyWait(driver);

		driver.manage().window().maximize();

		Logutil.info(Constants.url);

		driver.get(Constants.url);

	}

}

package com.masterdata.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.Baseclass;
import com.Constants;
import com.Exceloperations;
import com.Util;

public class Masterdataoperation extends Baseclass {

	By createnewbtn = By.cssSelector("button.btnWithIcon");

	By desctextbox = By.xpath("//input[@formcontrolname='description']");

	By regiontextbox = By.xpath("//input[@formcontrolname='regionName']");

	By createbtn = By.xpath("//button[@type='submit']");

//	By deleteicon = By.xpath("//img[contains(@src,'assets/images/delete-icon.svg')]");

//	By editicon = By.xpath("//img[contains(@src,'assets/images/edit-icon.svg')]");

	By yesbtn = By.xpath("//span[contains(text(),'Yes')]");

	By tblrows = By.xpath("//tbody[@class=\"ant-table-tbody\"]/tr");

	public void addMasterdata(String desc) {

		Util.click(driver, createnewbtn);

		Util.sendkeys(driver, desctextbox, desc);

		Util.click(driver, createbtn);

	}

	public void addRegion(String desc) {

		Util.click(driver, createnewbtn);

		Util.sendkeys(driver, regiontextbox, desc);

		Util.click(driver, createbtn);

	}

	public void editRegion(String desc) {

		clickonEditicon();

		driver.findElement(regiontextbox).clear();
		Util.sendkeys(driver, regiontextbox, desc + "edit");
		Util.click(driver, createbtn);

		try {
			Exceloperations.writeExcel(Constants.testdatafilePath, Constants.testcasefilename,
					Constants.masterDatasheet, 7, 2, desc + "edit");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void clickonEditicon() {

		WebElement editicon = driver.findElement(
				By.xpath("//tbody[@class=\"ant-table-tbody\"]/tr[" + countofRows(tblrows) + "]/td[4]/div/button[1]"));

		Util.clickElementwithjs(driver, editicon);
	}

	public void editMasterdata(String desc, int rownum) throws InterruptedException {
		clickonEditicon();
		driver.findElement(desctextbox).clear();
		Util.sendkeys(driver, desctextbox, desc + "edit");
		Util.click(driver, createbtn);
		try {
			Exceloperations.writeExcel(Constants.testdatafilePath, Constants.testdatafilename,
					Constants.masterDatasheet, rownum, 2, desc + "edit");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteMasterdata(String desc) throws InterruptedException {

		WebElement deletedataicon = driver.findElement(
				By.xpath("//tbody[@class=\"ant-table-tbody\"]/tr[" + countofRows(tblrows) + "]/td[4]/div/button[2]"));

		Util.clickElementwithjs(driver, deletedataicon);

		WebElement yes = driver.findElement(yesbtn);
		Util.clickElementwithjs(driver, yes);
	}

	public int countofRows(By rowpath) {

		Util.explicitwait(driver, rowpath);
		List<WebElement> rows = driver.findElements(rowpath);
		int rc = rows.size();

		return rc;

	}
}

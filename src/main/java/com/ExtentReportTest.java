package com;

import java.io.File;


import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.http.Consts;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportTest extends Baseclass {

	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static ExtentTest parenttest;
	public static ExtentTest childtest;

	public static Connection con;

	@BeforeSuite
	public void setUp() {
		htmlReporter = new ExtentHtmlReporter(Constants.extentreport);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		extent.setSystemInfo("OS", "Windows 10");
		extent.setSystemInfo("Host Name", "Yuvraj");
		extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("User Name", "Yuvraj Rajput");

		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setDocumentTitle("Automation Test Report");
		htmlReporter.config().setReportName("Automation Report");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.STANDARD);

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(Constants.dbUrl);
			Logutil.info("Connection Established");

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@BeforeTest
	public void getTestname() {

		parenttest = extent.createTest(getClass().getName());

	}

	@BeforeMethod
	public void getMethodname(Method method) throws Exception {

		childtest = parenttest.createNode(method.getName());
		MyScreenRecorder.startRecording(method.getName());

		Logutil.startTestCase("Start" + method.getName());

	}

	public void testStep(String status, String stepdesc) {

		if (status.equalsIgnoreCase("INFO")) {

			childtest.log(Status.INFO, MarkupHelper.createLabel(stepdesc, ExtentColor.YELLOW));

		} else if (status.equalsIgnoreCase("ERROR")) {
			childtest.log(Status.ERROR, MarkupHelper.createLabel(stepdesc, ExtentColor.PURPLE));
		} else if (status.equalsIgnoreCase("FAIL")) {
			childtest.log(Status.FAIL, MarkupHelper.createLabel(stepdesc, ExtentColor.RED));
		} else if (status.equalsIgnoreCase("FATAL")) {
			childtest.log(Status.FATAL, MarkupHelper.createLabel(stepdesc, ExtentColor.ORANGE));
		} else if (status.equalsIgnoreCase("PASS")) {
			childtest.log(Status.PASS, MarkupHelper.createLabel(stepdesc, ExtentColor.GREEN));
		} else if (status.equalsIgnoreCase("SKIP")) {
			childtest.log(Status.SKIP, MarkupHelper.createLabel(stepdesc, ExtentColor.BLUE));
		} else if (status.equalsIgnoreCase("UNKNOWN")) {
			childtest.log(Status.UNKNOWN, MarkupHelper.createLabel(stepdesc, ExtentColor.LIME));
		} else if (status.equalsIgnoreCase("WARNING")) {
			childtest.log(Status.WARNING, MarkupHelper.createLabel(stepdesc, ExtentColor.BLACK));
		}

	}

	@AfterMethod
	public void getResult(ITestResult result) throws Exception {

		if (result.getStatus() == ITestResult.FAILURE) {
			childtest.log(Status.FAIL, MarkupHelper
					.createLabel(result.getName() + " Test case FAILED due to below issues:", ExtentColor.RED));
			childtest.fail(result.getThrowable());
			String screenshotPath = ExtentReportTest.getfailedScreenshot(result.getName());
			childtest.addScreenCaptureFromPath(screenshotPath);

		} else if (result.getStatus() == ITestResult.SUCCESS) {
			childtest.log(Status.PASS, MarkupHelper.createLabel(result.getName() + "PASSED", ExtentColor.GREEN));

		} else {
			childtest.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + "SKIPPED", ExtentColor.ORANGE));
			childtest.skip(result.getThrowable());
		}

		Exceloperations.updateTestresultinExcel(Constants.testcasefilepath, Constants.testcasefilename,
				Constants.testcaseresultSheet, result);
		extent.flush();
		MyScreenRecorder.stopRecording();
		driver.quit();
	}

	public void endTestcase(Method method) {
		Logutil.endTestCase("End" + method.getName());

	}

	public static String getfailedScreenshot(String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		// after execution, you could see a folder "FailedTestsScreenshots"
		// under src folder
		String destination = Constants.failedscreenshotpath + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	@AfterSuite
	public void tearDown() {
		extent.close();
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

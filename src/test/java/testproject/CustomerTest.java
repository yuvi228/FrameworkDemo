package testproject;

import java.io.FileOutputStream;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.Baseclass;
import com.Constants;
import com.Exceloperations;
import com.ExtentReportTest;

public class CustomerTest extends ExtentReportTest {

	public Sheet sheet;
	public FileOutputStream fout;

	Baseclass base = new Baseclass();

	@DataProvider(name = "customerdata")
	public Object[][] getallcustomerdata() throws InvalidFormatException {
		Object data[][] = Exceloperations.getTestData(Constants.customerDatasheet,
				Constants.testdatafilePath + Constants.testdatafilename);
		return data;
	}

	String[] requiredvalues = new String[] { "accountname" };

	@DataProvider(name = "accountname")
	public Object[][] getspecificcustomerdata() throws InvalidFormatException, IOException {
		Object data[] = Exceloperations.getspecificcolumndata(Constants.customerDatasheet,
				Constants.testdatafilePath + Constants.testdatafilename, requiredvalues);
		Object[][] temp = new Object[1][requiredvalues.length];

		for (int i = 0; i < requiredvalues.length; i++) {
			temp[0][i] = data[i];

		}

		return temp;

	}

	@Parameters("browser")
	@Test
	public void demoTest(String browser) {
		base.webLaunch(browser);
	}
}

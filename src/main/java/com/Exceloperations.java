package com;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestResult;

public class Exceloperations {

	// Read Data From File
	public static Object[][] getTestData(String sheetName, String dataProviderPath) throws InvalidFormatException {

		Workbook book = null;
		Sheet sheet = null;
		FileInputStream file = null;
		try {
			file = new FileInputStream(dataProviderPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {

			book = WorkbookFactory.create(file);

		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = book.getSheet(sheetName);
		Object[][] data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

		int lastNRowNumber = sheet.getLastRowNum();
		// int lastCellNumber = sheet.getRow(0).getLastCellNum();

		for (int i = 0; i < lastNRowNumber; i++) {
			for (int k = 0; k < sheet.getRow(i).getLastCellNum(); k++) {

				try {
					if (!sheet.getRow(i + 1).getCell(k).equals("")) {
						data[i][k] = sheet.getRow(i + 1).getCell(k).toString();
					}

					System.out.println(data[i][k]);
				}

				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return data;

	}

	public static Object[] getmasterdata(String sheetName, String dataProviderPath, String[] requiredvalues)
			throws InvalidFormatException {

		Workbook book = null;
		Sheet sheet = null;
		FileInputStream file = null;
		try {
			file = new FileInputStream(dataProviderPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {

			book = WorkbookFactory.create(file);

		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = book.getSheet(sheetName);
		int totalreq = (requiredvalues.length) * 2;
		Object[] requiredata = new Object[totalreq]; // [X]Number of element to fetch from excel.
		int lastNRowNumber = sheet.getLastRowNum();

		int p = 0;

		for (int rowno = 1; rowno <= lastNRowNumber;) {
			for (String reqv : requiredvalues) {
//				System.out.println("array req data : " + reqv);
				String mdval = sheet.getRow(rowno).getCell(0).toString().toLowerCase();
//				System.out.println("Excel req data : " + mdval);
				if (mdval.equalsIgnoreCase(reqv)) {
					requiredata[p] = sheet.getRow(rowno).getCell(1).toString();
					System.out.println(requiredata[p]);
					requiredata[p + 1] = sheet.getRow(rowno).getCell(2).toString();
					System.out.println(requiredata[p + 1]);

					p += 2;
//					System.out.println(p);
					rowno++;

				}

			}
			break;
		}

		return requiredata;

	}

	public static Object[] getspecificcolumndata(String sheetName, String dataProviderPath, String[] requiredvalues)
			throws InvalidFormatException, IOException {

		Workbook book = null;
		Sheet sheet = null;
		FileInputStream file = null;
		try {
			file = new FileInputStream(dataProviderPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {

			book = WorkbookFactory.create(file);

		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = book.getSheet(sheetName);

		Object[] requiredata = new Object[requiredvalues.length]; // [X]Number of element to fetch from excel.

		int lastcol = sheet.getRow(0).getLastCellNum();

		for (int colreq = 0; colreq < requiredvalues.length;) {
			for (String reqv : requiredvalues) {
				for (int cellindex = 0; cellindex < lastcol; cellindex++) {

					if (sheet.getRow(0).getCell(cellindex).toString().equalsIgnoreCase(reqv)) {

						requiredata[colreq] = sheet.getRow(1).getCell(cellindex).toString(); // values from array
						colreq++;
					}

				}

			}

		}

		return requiredata;
	}

	public static String updateTestresultinExcel(String filePath, String fileName, String sheetname, ITestResult result)
			throws IOException {

		File file = new File(filePath + "\\" + fileName);

		FileInputStream inputStream = new FileInputStream(file);
		Workbook Workbook = null;

		String fileExtensionName = fileName.substring(fileName.indexOf("."));

		if (fileExtensionName.equals(".xlsx")) {

			Workbook = new XSSFWorkbook(inputStream);
		}

		else if (fileExtensionName.equals(".xls")) {

			Workbook = new HSSFWorkbook(inputStream);
		}

		Sheet Sheet = Workbook.getSheet(sheetname);

		int rowCount = Sheet.getLastRowNum();

		String testcasename = null;
		for (int i = 1; i < rowCount + 1; i++) {

			Row row = Sheet.getRow(i);

			testcasename = row.getCell(2).getStringCellValue();
//			System.out.print(row.getCell(2).getStringCellValue() + "|| ");
			if (testcasename.equalsIgnoreCase(result.getName())) {

				if (result.getStatus() == ITestResult.FAILURE) {
					writeExcel(filePath, fileName, sheetname, i, 3, "Fail");
				} else if (result.getStatus() == ITestResult.SUCCESS) {
					writeExcel(filePath, fileName, sheetname, i, 3, "Pass");
				} else {
					writeExcel(filePath, fileName, sheetname, i, 3, "Skipped");
				}

			}

//		        }

			System.out.println();
		}

		return testcasename;
	}

	public static void writeExcel(String filePath, String fileName, String sheetname, int rownum, int colnum,
			String cellvalue) throws IOException {
		File file = new File(filePath + "\\" + fileName);

		FileInputStream inputStream = new FileInputStream(file);
		Workbook Workbook = null;

		String fileExtensionName = fileName.substring(fileName.indexOf("."));

		if (fileExtensionName.equals(".xlsx")) {

			Workbook = new XSSFWorkbook(inputStream);
		}

		else if (fileExtensionName.equals(".xls")) {

			Workbook = new HSSFWorkbook(inputStream);
		}

		Sheet Sheet = Workbook.getSheet(sheetname);
		FileOutputStream fout = new FileOutputStream(file);
		Sheet.getRow(rownum).createCell(colnum).setCellValue(cellvalue);
		Workbook.write(fout);
		Workbook.close();

	}

}

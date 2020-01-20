package com.db.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ExtentReportTest;

public class DbCustoperation extends ExtentReportTest {

	public String VerifyCustnameTest() {
		Statement stmt;
		String custname = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select top 1 * from Customer order by insertDate desc");
			while (rs.next()) {
				custname = rs.getString("CustomerName");

				System.out.println(custname);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return custname;
	}

}

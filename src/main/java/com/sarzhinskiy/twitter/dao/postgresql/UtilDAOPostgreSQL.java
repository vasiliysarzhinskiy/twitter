package com.sarzhinskiy.twitter.dao.postgresql;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class UtilDAOPostgreSQL {
	
	public UtilDAOPostgreSQL() {
	}
	
	public static Timestamp getTimestampFrom(DateTime dateTime) {
		return new Timestamp(dateTime.getMillis());
	}
	
	public static Date getDateFrom(LocalDate date) {
		return new Date(date.toDate().getTime());
	}

	
	public static boolean isNotZero(int number) {
		if (number != 0) {
			return true;
		}
		return false;
	}
	
	public static void close(ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		}
		catch (SQLException exc) {
			exc.printStackTrace();
		}
	}
	
	public static void close(PreparedStatement prepStatement) {
		try {
			if (prepStatement != null) {
				prepStatement.close();
			}
		}
		catch (SQLException exc) {
			exc.printStackTrace();
		}
	}
	
	public static void close(InputStream is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}

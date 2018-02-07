package com.btd;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//Date tracker to keep track of dates
public class DateTracker {

	// Initial date
	private static Date currentDate = new Date();

	public DateTracker() {
		currentDate = generateRandomDateAfter(currentDate);
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	// generate a date randomly after the given date
	public Date generateRandomDateAfter(Date startDate) {

		Date date = null;

		if (startDate != null) {

			// get a calendar instance with the timezone UTC
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			calendar.setTime(startDate);

			int offset = 1 + (int) (Math.random() * 1000);
			date = addDay(calendar.getTime(), offset);
		}

		return date;
	}

	// add a given number of days to a given date
	public Date addDay(Date startDate, int days) {

		// get a calendar instance with the timezone UTC
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		// set the time with the given date
		calendar.setTime(startDate);

		// then add the given days to the date
		calendar.add(Calendar.DATE, days);

		// return the computed date converted to date object
		return calendar.getTime();
	}
}
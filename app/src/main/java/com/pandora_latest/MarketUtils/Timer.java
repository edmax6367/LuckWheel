package com.pandora_latest.MarketUtils;
import java.util.Calendar;
import java.util.Date;

public class Timer {

        public static Date addDaysToCurrentTime(long days) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); // Set current time as the base

            calendar.add(Calendar.DAY_OF_YEAR, (int) days); // Add the specified number of days

            return calendar.getTime();
        }

        public static void main(String[] args) {
            long daysToAdd = 7; // Change this to the desired number of days
            Date futureDate = addDaysToCurrentTime(daysToAdd);

            System.out.println("Current time: " + new Date());
            System.out.println("Future time: " + futureDate);
        }

}

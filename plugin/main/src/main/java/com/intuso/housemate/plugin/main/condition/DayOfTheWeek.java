package com.intuso.housemate.plugin.main.condition;

import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.annotation.Property;
import com.intuso.housemate.client.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.api.internal.type.Day;
import org.slf4j.Logger;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Condition which is true iff the current day of the week matches
 * those specified by the user
 *
 */
@Id(value = "day-of-the-week", name = "Day of the Week", description = "Condition that is true on certain days of the week")
public class DayOfTheWeek implements ConditionDriver {

    private final Map<Day, Integer> DAY_MAP = new HashMap<Day, Integer>() {
        {
            put(Day.Monday, Calendar.MONDAY);
            put(Day.Tuesday, Calendar.TUESDAY);
            put(Day.Wednesday, Calendar.WEDNESDAY);
            put(Day.Thursday, Calendar.THURSDAY);
            put(Day.Friday, Calendar.FRIDAY);
            put(Day.Saturday, Calendar.SATURDAY);
            put(Day.Sunday, Calendar.SUNDAY);
        }
    };
    
	/**
	 * The days that the condition is satisfied for. Left-most bit isn't used, next one is sunday,
	 * then monday etc. Right-most bit is saturday
	 */
    @Property
    @Id(value = "days", name = "Days", description = "The days that satisfy the condition")
	private Set<Day> days;

    private Logger logger;
    private ConditionDriver.Callback callback;
    
	/**
	 * thread to monitor the day of the week
	 */
	private Thread monitor;

    @Override
    public boolean hasChildConditions() {
        return false;
    }

	/**
	 * Check if the current day satisfies the condition
	 * @return true if the current day satisfies the condition
	 */
	private final boolean doesTodaySatisfy() {
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		boolean result = false;
        for(Day day : days)
            result |= DAY_MAP.get(day) == currentDay;
		logger.debug("Current day of the week is " + Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + " (1 is Sunday). Condition is " + (result ? "" : "un") + "satisfied");
		return result;
	}
	
	@Override
	public void init(Logger logger, ConditionDriver.Callback callback) {
		this.logger = logger;
		this.callback = callback;
		// start monitoring the day of the week
        logger.debug("Condition satisfied when day is " + days);
		monitor = new DayMonitorThread();
		monitor.start();
		callback.conditionSatisfied(doesTodaySatisfy());
	}
	
	@Override
	public void uninit() {
        if(monitor != null) {
		    monitor.interrupt();
		    monitor = null;
        }
		this.logger = null;
		this.callback = null;
	}
	
	/**
	 * Thread that monitors the time of the day and calls conditionSatisfied() as appropriate based on
	 * the days that the super class parsed
	 * @author Tom Clabon
	 *
	 */
	private class DayMonitorThread extends Thread {
		@Override
		public void run() {

            try {
                // while we're not shutting down
                while(!isInterrupted()) {

                    // wait until the next day starts
                    logger.debug("Waiting until midnight");
                    TimeOfTheDay.waitUntilNext(TimeOfTheDay.DAY_START, logger);

                    logger.debug("Past midnight, checking if current day is in set");

                    // check if this condition is now satisfied or not
                    callback.conditionSatisfied(doesTodaySatisfy());
                }
            } catch(InterruptedException e) {
                logger.warn("DayMonitor thread interrupted");
            }
		}
	}
}
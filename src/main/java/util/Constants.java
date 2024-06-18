package util;

import java.util.HashMap;

public class Constants {
	public static final HashMap<Integer, String> DAY_NUMBER_VS_DAY =new HashMap<Integer, String>() {{
			put(1, "Sunday");
			put(2, "Monday");
			put(3, "Tuesday");
			put(4, "Wednesday");
			put(5, "Thursday");
			put(6, "Friday");
			put(7, "Saturday");
	}};
	
	public static final Double PREMIUM_FARE_MULTIPLIER = 1.25d;
	
	
	public static final String SEATER = "Seater";
	
	public static final Integer H_MAX = 5;
	public static final Integer A_MAX = 15;
	public static final Integer B_MAX = 30;
	public static final Integer C_MAX = 40;
	public static final Integer E_MAX = 10;
	public static final Integer S_MAX = 50;
	
	public static final String FIRST_AC = "First AC";
	public static final String SECOND_AC = "Second AC";
	public static final String THIRD_AC = "Third AC";
	public static final String SLEEPER = "Sleeper";
	public static final String EXECUTIVE_CHAIR_CAR = "Executive Chair Car";
	public static final String AC_CHAIR_CAR = "AC Chair Car";
	
	public static final String FIRST_AC_SHORTNAME = "H";
	public static final String SECOND_AC_SHORTNAME = "A";
	public static final String THIRD_AC_SHORTNAME = "B";
	public static final String SLEEPER_SHORTNAME = "S";
	public static final String EXECUTIVE_CHAIR_CAR_SHORTNAME = "E";
	public static final String AC_CHAIR_CAR_SHORTNAME = "C";
	
	public static final String SMTP_CONFIG_FILE = "/Users/sutanubhattacharya/Documents/mailConfigs.properties";
	public static final String CREDENTIALS_PROPERTIES_FILE = "/Users/sutanubhattacharya/Documents/credentials.properties";
	
	public static final String PAST_JOURNEYS = "PAST_JOURNEYS";
	public static final String UPCOMING_JOURNEYS = "UPCOMING_JOURNEYS";
	public static final String CANCELLED_BOOKINGS = "CANCELLED_BOOKINGS";
	public static final String ACTIVE_BOOKINGS = "ACTIVE_BOOKINGS";
	public static final String TRAVELLER_COUNT_EXCEEDED_LIMIT = "TRAVELLER_COUNT_EXCEEDED_LIMIT";
	public static final Double CANCELLATION_PERCENT = 0.95d;
}


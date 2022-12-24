package superapp.data.enums;

import java.time.temporal.ChronoUnit;

public enum CreationEnum {
	LAST_MINUTE,
	LAST_HOUR,
	LAST_DAY;


	public static ChronoUnit MapCreationEnumToChronoUnit(CreationEnum creation)
	{
		switch(creation)
		{
		case LAST_MINUTE:
			return ChronoUnit.MINUTES;
		case LAST_DAY:
			return ChronoUnit.DAYS;
		case LAST_HOUR:
			return ChronoUnit.HOURS;
		default:
			return ChronoUnit.MINUTES;
		}
	}
}
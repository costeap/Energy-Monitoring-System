package ro.tuc.ds2020.Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateConvertor {

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}

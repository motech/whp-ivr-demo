package org.motechproject.whp.ivr;

import org.motechproject.ivr.message.IVRMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class WHPIVRMessage implements IVRMessage {

    public static final String WAV = ".wav";

    public static final String NUMBER_WAV_FORMAT = "Num_%03d";
    public static final String MINUTES = "timeOfDayMinutes";
    public static final String SIGNATURE_MUSIC = "signature_music";
    public static final String END_OF_CALL = "end_of_call";

    public static final String  welcome_message= "001_01_01_welcomeMessage";
    public static final String  instructional_message_1= "001_01_02_instructionalMessage1";
    public static final String  instructional_message_2= "001_01_04_instructionalMessage2";
    public static final String  instructional_message_3= "001_02_01_instructionalMessage3";
    public static final String  patientList= "001_02_02_patientList";   //"PatientNo"
    public static final String  enterAdherence= "001_02_05_enterAdherence";
    public static final String  completionMessage1= "001_03_01_completionMessage1";
    public static final String  replayAdherence= "001_03_02_replayAdherence";
    public static final String  adherence= "001_03_04_adherence";
    public static final String  skipped= "001_03_06_completionMessage2";
    public static final String  finalMessage= "001_04_01_completionMessage3";
    public static final String  nextPatient= "001_003_07_nextPatient";
    public static final String  musicEndNote= "musicEndNote";

    public static final String  patientNumbervariable= "patientNumbervariable"; //"1,2,3 etc"
    public static final String  patientId= "patientId";
    public static final String  patientCount= "PatientCount";  // "4-total no of patients"


    public static final String CONTENT_LOCATION_URL = "content.location.url";

    /* ---------- Four Day Recall ----------*/
    private Properties properties;

    @Autowired
    public WHPIVRMessage(@Qualifier("ivrProperties") Properties properties) {
        this.properties = properties;
    }

    public String get(String key) {
        return (String) properties.get(key.toLowerCase());
    }

    @Override
    public String getText(String key) {
        String text = get(key);
        return text == null ? key : text;
    }

    @Override
    public String getWav(String key, String preferredLangCode) {
        //String file = get(key) != null ? get(key) : FileUtil.sanitizeFilename(key);
        return String.format("%s%s/%s%s", properties.get(CONTENT_LOCATION_URL), preferredLangCode, key, WAV);
    }

    public static String getNumberFilename(int n) {
        return String.format(NUMBER_WAV_FORMAT, n);
    }

    public static List<String> getAllNumberFileNames(String number) {
        List<String> wavFiles = new ArrayList<String>();
        for (Character dijit : number.toCharArray()) {
            wavFiles.add(getNumberFilename(Integer.parseInt(dijit.toString())));
        }
        return wavFiles;
    }

    public static List<String> getAllFileNames(String number) {
        List<String> wavFiles = new ArrayList<String>();
        for (Character dijit : number.toCharArray()) {
            try{
                wavFiles.add(getNumberFilename(Integer.parseInt(dijit.toString())));
            }catch(Exception e){
                wavFiles.add(dijit.toString());
            }
        }
        return wavFiles;
    }

    private static String getFilename(String alphabet) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public static String getDayOfWeekFile(String dayOfWeek) {
        return "weekday_" + dayOfWeek;
    }

    public static String getMonthOfYearFile(String monthOfYear) {
        return "month_" + monthOfYear;
    }

    public static class DateMessage {

        private int dayOfMonth;

        public DateMessage(int dayOfMonth) {
            this.dayOfMonth = dayOfMonth;
        }

        public String value() {
            return "dates_" + dayOfMonth + suffix();
        }

        private String suffix() {
            if (dayOfMonth % 10 == 1) {
                return "st";
            } else if (dayOfMonth % 10 == 2) {
                return "nd";
            } else if (dayOfMonth % 10 == 3) {
                return "rd";
            } else {
                return "th";
            }
        }
    }
}

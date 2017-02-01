package event.reminder;

import static java.lang.Thread.sleep;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Meem
 */
public class AlarmClock {

    static Thread t;
    static int str = 0;
    String remind;

    public AlarmClock(final String remindTime) throws Exception {
        remind = remindTime;
    }

    public void CALL() throws Exception {
        int dd, MM, hh, mm;
        String ampm;
        int i;
        
        /** extracting date, month, hour and minute from remind string 
         * 
         */
        
        ampm = Character.toString(remind.charAt(remind.length() - 2)) + Character.toString(remind.charAt(remind.length() - 1));
        String s = Character.toString(remind.charAt(0)) + Character.toString(remind.charAt(1));
        dd = Integer.parseInt(s);

        s = "";
        for (i = 3; remind.charAt(i) != ' '; i++) {
            s += Character.toString(remind.charAt(i));
        }
        Date date = new SimpleDateFormat("MMMM").parse(s);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        MM = cal.get(Calendar.MONTH);

        s = Character.toString(remind.charAt(i + 1)) + Character.toString(remind.charAt(i + 2));
        hh = Integer.parseInt(s);

        s = Character.toString(remind.charAt(i + 4)) + Character.toString(remind.charAt(i + 5));
        mm = Integer.parseInt(s);

        //System.out.println(dd + " " + MM + " " + hh + " " + mm + " " + ampm);
        if (ampm.equals("AM")) {
            if(hh==12) hh = 0;
         
        } else if (ampm.equals("PM")) {
            if(hh!=12) hh+=12;
        }

        boolean whileloop = true;
        while ( true) {
            try {
                Calendar d = Calendar.getInstance();
                int Date = d.get(Calendar.DAY_OF_MONTH);
                int month = d.get(Calendar.MONTH);
                int hours = d.get(Calendar.HOUR_OF_DAY);
                int mins = d.get(Calendar.MINUTE);
              
                if (dd == Date && MM == month && hh == hours && mm == mins) {
                    break;
                }
                /** thread going to sleep for 1 min = 60000 milliseconds
                 */
                sleep(60000); 
            } catch (InterruptedException ex) {
               ex.printStackTrace();
            }
        }

    }
}
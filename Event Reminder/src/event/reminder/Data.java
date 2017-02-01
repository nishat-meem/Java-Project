package event.reminder;

import java.util.Calendar;

/**
 *
 * @author Meem
 */
public class Data {

    private String event_name;
    private String event_at;
    private String remind_me_at;
    boolean datePassed;
    
    public Data() {
        this.event_name = "";
        this.event_at = "";
        this.remind_me_at = "";
        this.datePassed = false;
    }

    public Data(String event_name, String event_at, String remind_me_at) {
        this.event_name = event_name;
        this.event_at = event_at;
        this.remind_me_at = remind_me_at;
        this.datePassed = false;

    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_at() {
        return event_at;
    }

    public void setEvent_at(String event_at) {
        this.event_at = event_at;
    }

    public String getRemind_me_at() {
        return remind_me_at;
    }

    public void setRemind_me_at(String remind_me_at) {
        this.remind_me_at = remind_me_at;
    }

    public void setDatePassed(int m1, int d1) {
        Calendar now = Calendar.getInstance();
        int m2 = now.get(Calendar.MONTH) + 1;
        int d2 = now.get(Calendar.DATE);
        if(m1 == m2 && d1>=d2)
        {
            this.datePassed = false;
        }
        else this.datePassed = m1 <= m2;
    }

    public boolean isDatePassed() {
        return datePassed;
    }
}

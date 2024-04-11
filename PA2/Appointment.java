import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private final LocalDate date;
    private final LocalTime time;
    private final String withWhom;

    public Appointment(String date, String time, String withWhom) {
        this.date = LocalDate.parse(date);
        this.time = LocalTime.parse(time);
        this.withWhom = withWhom;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getWithWhom() {
        return withWhom;
    }

    @Override
    public String toString() {
        return String.format("%s at %s with %s", date, time, withWhom);
    }
}

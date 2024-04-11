import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentDatabase {
    private final List<Appointment> appointments = new ArrayList<>();

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void deleteAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    public List<Appointment> searchAppointments(String date) {
        LocalDate searchDate = LocalDate.parse(date);
        return appointments.stream()
                .filter(appointment -> appointment.getDate().isEqual(searchDate))
                .collect(Collectors.toList());
    }
}

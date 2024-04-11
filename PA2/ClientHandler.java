import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final AppointmentDatabase appointmentDatabase;

    public ClientHandler(Socket socket, AppointmentDatabase appointmentDatabase) {
        this.socket = socket;
        this.appointmentDatabase = appointmentDatabase;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            writer.println(AnsiUtils.clearScreen());
            writer.println(AnsiUtils.color(AnsiUtils.Color.CYAN, AnsiUtils.moveTo(1, 1) + "Welcome to the Appointment Manager!"));

            while (true) {
                writer.println(AnsiUtils.color(AnsiUtils.Color.YELLOW, "Please choose an option:"));
                writer.println("1. List all appointments");
                writer.println("2. Add an appointment");
                writer.println("3. Delete an appointment");
                writer.println("4. Search appointments by date");
                writer.println("5. Exit");
                writer.print("Enter your choice (1-5): ");
                writer.flush();

                String input = reader.readLine();
                if (input == null || input.equals("5")) {
                    writer.println(AnsiUtils.color(AnsiUtils.Color.GREEN, "Goodbye!"));
                    break;
                }

                switch (input) {
                    case "1":
                        listAppointments(writer);
                        break;
                    case "2":
                        addAppointment(reader, writer);
                        break;
                    case "3":
                        deleteAppointment(reader, writer);
                        break;
                    case "4":
                        searchAppointments(reader, writer);
                        break;
                    default:
                        writer.println(AnsiUtils.color(AnsiUtils.Color.RED, "Invalid option. Please try again."));
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }

    private void listAppointments(PrintWriter writer) {
        List<Appointment> appointments = appointmentDatabase.getAllAppointments();
        if (appointments.isEmpty()) {
            writer.println("No appointments found.");
        } else {
            writer.println("List of appointments:");
            for (Appointment appointment : appointments) {
                writer.println(appointment);
            }
        }
    }

    private void addAppointment(BufferedReader reader, PrintWriter writer) throws IOException {
        writer.print("Enter appointment date (YYYY-MM-DD): ");
        writer.flush();
        String date = reader.readLine();

        writer.print("Enter appointment time (HH:mm): ");
        writer.flush();
        String time = reader.readLine();

        writer.print("Enter appointment description: ");
        writer.flush();
        String withWhom = reader.readLine();

        Appointment appointment = new Appointment(date, time, withWhom);
        appointmentDatabase.addAppointment(appointment);
        writer.println("Appointment added successfully.");
    }

    private void deleteAppointment(BufferedReader reader, PrintWriter writer) throws IOException {
        writer.print("Enter appointment date (YYYY-MM-DD) to delete: ");
        writer.flush();
        String date = reader.readLine();

        List<Appointment> appointments = appointmentDatabase.searchAppointments(date);
        if (appointments.isEmpty()) {
            writer.println("No appointments found on this date.");
            return;
        }

        writer.println("Appointments on this date:");
        for (int i = 0; i < appointments.size(); i++) {
            writer.printf("%d. %s\n", i + 1, appointments.get(i));
        }

        writer.print("Enter the number of the appointment to delete: ");
        writer.flush();
        int index;
        try {
            index = Integer.parseInt(reader.readLine());
            if (index < 1 ||index > appointments.size()) {
                throw new NumberFormatException();
            }
            } catch (NumberFormatException e) {
                writer.println("Invalid selection. Please try again.");
                return;
            }
            Appointment appointmentToDelete = appointments.get(index - 1);
            appointmentDatabase.deleteAppointment(appointmentToDelete);
            writer.println("Appointment deleted successfully.");
    }
    private void searchAppointments(BufferedReader reader, PrintWriter writer) throws IOException {
        writer.print("Enter appointment date (YYYY-MM-DD) to search: ");
        writer.flush();
        String date = reader.readLine();
    
        List<Appointment> appointments = appointmentDatabase.searchAppointments(date);
        if (appointments.isEmpty()) {
            writer.println("No appointments found on this date.");
        } else {
            writer.println("Appointments on this date:");
            for (Appointment appointment : appointments) {
                writer.println(appointment);
            }
        }
    }
    
}
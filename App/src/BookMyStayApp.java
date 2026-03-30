import java.util.*;

// Reservation Model
class Reservation {
    private String reservationId;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId;
    }
}

// Booking History (Storage)
class BookingHistory {

    // Maintains insertion order
    private List<Reservation> reservations = new ArrayList<>();

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        System.out.println("Reservation stored: " + reservation.getReservationId());
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations); // return copy (immutability safety)
    }
}

// Booking Report Service
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation res : reservations) {
            System.out.println(res);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        System.out.println("\n--- Booking Summary Report ---");

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation res : reservations) {
            roomTypeCount.put(
                    res.getRoomType(),
                    roomTypeCount.getOrDefault(res.getRoomType(), 0) + 1
            );
        }

        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Total Bookings: " + entry.getValue());
        }

        System.out.println("Total Reservations: " + reservations.size());
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings (from Use Case 6)
        Reservation r1 = new Reservation("RES-101", "Single", "SI-12345");
        Reservation r2 = new Reservation("RES-102", "Double", "DO-54321");
        Reservation r3 = new Reservation("RES-103", "Single", "SI-67890");
        Reservation r4 = new Reservation("RES-104", "Suite", "SU-11111");

        // Store in booking history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);
        history.addReservation(r4);

        // Admin views booking history
        List<Reservation> allReservations = history.getAllReservations();

        // Display detailed bookings
        reportService.displayAllBookings(allReservations);

        // Generate summary report
        reportService.generateSummary(allReservations);
    }
}
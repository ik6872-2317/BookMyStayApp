import java.util.LinkedList;
import java.util.Queue;

// Reservation class (represents a booking request)
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Room Type: " + roomType);
    }
}

// Booking Request Queue (FIFO handling)
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void viewRequests() {
        System.out.println("\nCurrent Booking Requests (In Order):\n");

        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests.");
            return;
        }

        for (Reservation r : requestQueue) {
            r.displayReservation();
        }
    }

    // Get next request (for future processing, no allocation here)
    public Reservation getNextRequest() {
        return requestQueue.peek(); // only view, not remove
    }
}

// Main Driver Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate guest booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single"));
        bookingQueue.addRequest(new Reservation("Bob", "Double"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite"));

        // Display queue (FIFO order)
        bookingQueue.viewRequests();

        // Show next request (without removing)
        Reservation next = bookingQueue.getNextRequest();
        if (next != null) {
            System.out.println("\nNext request to process:");
            next.displayReservation();
        }
    }
}
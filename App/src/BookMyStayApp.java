import java.util.*;

// Reservation Model
class Reservation {
    private String reservationId;
    private String roomType;
    private String roomId;
    private boolean isActive;

    public Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
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

    public boolean isActive() {
        return isActive;
    }

    public void cancel() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId +
                ", Status: " + (isActive ? "CONFIRMED" : "CANCELLED");
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 1);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation res) {
        reservations.put(res.getReservationId(), res);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }

    public void displayAll() {
        System.out.println("\n--- Booking History ---");
        for (Reservation res : reservations.values()) {
            System.out.println(res);
        }
    }
}

// Cancellation Service
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();
    private InventoryService inventoryService;
    private BookingHistory bookingHistory;

    public CancellationService(InventoryService inventoryService, BookingHistory bookingHistory) {
        this.inventoryService = inventoryService;
        this.bookingHistory = bookingHistory;
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing Cancellation for: " + reservationId);

        Reservation res = bookingHistory.getReservation(reservationId);

        // Validation
        if (res == null) {
            System.out.println("Cancellation Failed: Reservation does not exist.");
            return;
        }

        if (!res.isActive()) {
            System.out.println("Cancellation Failed: Reservation already cancelled.");
            return;
        }

        // Step 1: Push room ID to rollback stack
        rollbackStack.push(res.getRoomId());

        // Step 2: Restore inventory
        inventoryService.increment(res.getRoomType());

        // Step 3: Update reservation status
        res.cancel();

        System.out.println("Cancellation Successful for Room ID: " + res.getRoomId());
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingHistory bookingHistory = new BookingHistory();

        // Simulated confirmed bookings
        Reservation r1 = new Reservation("RES-101", "Single", "SI-111");
        Reservation r2 = new Reservation("RES-102", "Double", "DO-222");

        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);

        // Reduce inventory as if allocated earlier
        inventoryService.decrement("Single");
        inventoryService.decrement("Double");

        CancellationService cancellationService =
                new CancellationService(inventoryService, bookingHistory);

        // Valid cancellation
        cancellationService.cancelBooking("RES-101");

        // Duplicate cancellation
        cancellationService.cancelBooking("RES-101");

        // Invalid reservation
        cancellationService.cancelBooking("RES-999");

        // Display final state
        bookingHistory.displayAll();
        inventoryService.displayInventory();
        cancellationService.displayRollbackStack();
    }
}
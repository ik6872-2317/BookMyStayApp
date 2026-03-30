import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Booking Request Model
class BookingRequest {
    private String requestId;
    private String roomType;

    public BookingRequest(String requestId, String roomType) {
        this.requestId = requestId;
        this.roomType = roomType;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 1);
        inventory.put("Double", 1);
        inventory.put("Suite", 0);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementInventory(String roomType) throws InvalidBookingException {
        int count = inventory.get(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("Inventory cannot go below zero for room type: " + roomType);
        }

        inventory.put(roomType, count - 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Validator Class
class BookingValidator {

    public static void validate(BookingRequest request, InventoryService inventoryService)
            throws InvalidBookingException {

        if (request.getRoomType() == null || request.getRoomType().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        if (!inventoryService.isValidRoomType(request.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + request.getRoomType());
        }

        if (!inventoryService.isAvailable(request.getRoomType())) {
            throw new InvalidBookingException("No availability for room type: " + request.getRoomType());
        }
    }
}

// Booking Service
class BookingService {

    private Queue<BookingRequest> queue = new LinkedList<>();
    private InventoryService inventoryService;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void addRequest(BookingRequest request) {
        queue.offer(request);
    }

    public void processBookings() {
        while (!queue.isEmpty()) {
            BookingRequest request = queue.poll();

            System.out.println("\nProcessing Request: " + request.getRequestId());

            try {
                // Step 1: Validate input (Fail-Fast)
                BookingValidator.validate(request, inventoryService);

                // Step 2: Safe inventory update
                inventoryService.decrementInventory(request.getRoomType());

                // Step 3: Confirm booking
                System.out.println("Booking Confirmed for Room Type: " + request.getRoomType());

            } catch (InvalidBookingException e) {
                // Graceful failure handling
                System.out.println("Booking Failed: " + e.getMessage());
            }
        }
    }
}

// Main Class
public class BookMyStayApp{

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(inventoryService);

        // Valid request
        bookingService.addRequest(new BookingRequest("REQ1", "Single"));

        // Invalid room type
        bookingService.addRequest(new BookingRequest("REQ2", "Triple"));

        // No availability (Suite = 0)
        bookingService.addRequest(new BookingRequest("REQ3", "Suite"));

        // Empty room type
        bookingService.addRequest(new BookingRequest("REQ4", ""));

        // Valid but will exhaust inventory
        bookingService.addRequest(new BookingRequest("REQ5", "Single"));

        // Process all requests
        bookingService.processBookings();

        // Final inventory state
        inventoryService.displayInventory();
    }
}
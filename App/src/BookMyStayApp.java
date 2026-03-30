import java.util.*;

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
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementInventory(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Room Allocation Service
class RoomAllocationService {

    private Queue<BookingRequest> requestQueue = new LinkedList<>();
    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomTypeToRooms = new HashMap<>();
    private InventoryService inventoryService;

    public RoomAllocationService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Add booking request (FIFO queue)
    public void addBookingRequest(BookingRequest request) {
        requestQueue.offer(request);
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);
        } while (allocatedRoomIds.contains(roomId));
        return roomId;
    }

    // Process bookings
    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            BookingRequest request = requestQueue.poll();

            System.out.println("\nProcessing Request: " + request.getRequestId());

            String roomType = request.getRoomType();

            // Check availability
            if (!inventoryService.isAvailable(roomType)) {
                System.out.println("No rooms available for type: " + roomType);
                continue;
            }

            // Atomic Allocation Block
            String roomId = generateRoomId(roomType);

            // Ensure uniqueness
            allocatedRoomIds.add(roomId);

            // Map room type to allocated rooms
            roomTypeToRooms.putIfAbsent(roomType, new HashSet<>());
            roomTypeToRooms.get(roomType).add(roomId);

            // Update inventory immediately
            inventoryService.decrementInventory(roomType);

            // Confirm reservation
            System.out.println("Booking Confirmed!");
            System.out.println("Room Type: " + roomType);
            System.out.println("Allocated Room ID: " + roomId);
        }
    }

    public void displayAllocations() {
        System.out.println("\nFinal Room Allocations:");
        for (Map.Entry<String, Set<String>> entry : roomTypeToRooms.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Main Class
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        RoomAllocationService allocationService = new RoomAllocationService(inventoryService);

        // Add sample booking requests
        allocationService.addBookingRequest(new BookingRequest("REQ1", "Single"));
        allocationService.addBookingRequest(new BookingRequest("REQ2", "Double"));
        allocationService.addBookingRequest(new BookingRequest("REQ3", "Single"));
        allocationService.addBookingRequest(new BookingRequest("REQ4", "Suite"));
        allocationService.addBookingRequest(new BookingRequest("REQ5", "Single")); // Should fail (inventory over)

        // Process bookings
        allocationService.processBookings();

        // Display final state
        allocationService.displayAllocations();
        inventoryService.displayInventory();
    }
}
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

// Thread-Safe Inventory Service
class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
    }

    // Synchronized method ensures only one thread modifies inventory at a time
    public synchronized boolean allocateRoom(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);

        if (count <= 0) {
            return false;
        }

        // Critical section
        inventory.put(roomType, count - 1);
        return true;
    }

    public synchronized void displayInventory() {
        System.out.println("Final Inventory: " + inventory);
    }
}

// Thread-Safe Booking Processor
class BookingProcessor {

    private Queue<BookingRequest> requestQueue = new LinkedList<>();
    private Set<String> allocatedRooms = new HashSet<>();
    private InventoryService inventoryService;

    public BookingProcessor(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Add requests (synchronized for safety)
    public synchronized void addRequest(BookingRequest request) {
        requestQueue.offer(request);
    }

    // Fetch request safely
    public synchronized BookingRequest getNextRequest() {
        return requestQueue.poll();
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" +
                UUID.randomUUID().toString().substring(0, 4);
    }

    // Process booking (called by multiple threads)
    public void processBooking() {
        while (true) {

            BookingRequest request;

            // Critical section: fetching request
            synchronized (this) {
                request = getNextRequest();
            }

            if (request == null) {
                break;
            }

            System.out.println(Thread.currentThread().getName() +
                    " processing " + request.getRequestId());

            // Critical section: allocation + uniqueness
            synchronized (this) {

                boolean allocated = inventoryService.allocateRoom(request.getRoomType());

                if (allocated) {
                    String roomId;

                    do {
                        roomId = generateRoomId(request.getRoomType());
                    } while (allocatedRooms.contains(roomId));

                    allocatedRooms.add(roomId);

                    System.out.println("✅ " + request.getRequestId() +
                            " confirmed with Room ID: " + roomId);

                } else {
                    System.out.println("❌ " + request.getRequestId() +
                            " failed (No availability)");
                }
            }
        }
    }
}

// Worker Thread
class BookingWorker extends Thread {

    private BookingProcessor processor;

    public BookingWorker(BookingProcessor processor, String name) {
        super(name);
        this.processor = processor;
    }

    @Override
    public void run() {
        processor.processBooking();
    }
}

// Main Class
public class BookMyStayapp {

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingProcessor processor = new BookingProcessor(inventoryService);

        // Simulate multiple guest requests
        processor.addRequest(new BookingRequest("REQ1", "Single"));
        processor.addRequest(new BookingRequest("REQ2", "Single"));
        processor.addRequest(new BookingRequest("REQ3", "Single"));
        processor.addRequest(new BookingRequest("REQ4", "Double"));
        processor.addRequest(new BookingRequest("REQ5", "Double"));

        // Create multiple threads (simulating concurrent users)
        Thread t1 = new BookingWorker(processor, "Thread-1");
        Thread t2 = new BookingWorker(processor, "Thread-2");
        Thread t3 = new BookingWorker(processor, "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventoryService.displayInventory();
    }
}
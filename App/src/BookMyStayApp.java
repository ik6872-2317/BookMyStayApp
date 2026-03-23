import java.util.HashMap;

// Room Domain Model
class Room {

    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: " + price);
        System.out.println("Amenities: " + amenities);
    }
}

// Centralized Inventory (Read-focused usage here)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Read-only method
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// Search Service (Read-only logic)
class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, Room[] rooms) {

        System.out.println("\nAvailable Rooms:\n");

        for (Room room : rooms) {

            String type = room.getType();
            int available = inventory.getAvailability(type);

            // Show only available rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available Count: " + available);
                System.out.println("--------------------------");
            }
        }
    }
}

// Main Driver Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 10);
        inventory.addRoomType("Double", 0); // unavailable
        inventory.addRoomType("Suite", 3);

        // Room data (domain objects)
        Room[] rooms = {
                new Room("Single", 2000, "WiFi, TV, AC"),
                new Room("Double", 3500, "WiFi, TV, AC, Mini Bar"),
                new Room("Suite", 5000, "WiFi, TV, AC, Mini Bar, Jacuzzi")
        };

        // Search service
        SearchService searchService = new SearchService();

        // Guest searches
        searchService.searchAvailableRooms(inventory, rooms);
    }
}
import java.util.HashMap;
import java.util.Map;

class RoomInventory {

    // Centralized data structure
    private HashMap<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Register a room type with count
    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (controlled update)
    public void updateAvailability(String roomType, int change) {
        if (inventory.containsKey(roomType)) {
            int current = inventory.get(roomType);
            int updated = current + change;

            if (updated >= 0) {
                inventory.put(roomType, updated);
            } else {
                System.out.println("Error: Cannot reduce below zero for " + roomType);
            }
        } else {
            System.out.println("Error: Room type not found: " + roomType);
        }
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
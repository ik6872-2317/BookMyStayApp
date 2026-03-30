import java.io.*;
import java.util.*;

// Reservation Model (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId;
    }
}

// Wrapper class to persist full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> reservations;

    public SystemState(Map<String, Integer> inventory, List<Reservation> reservations) {
        this.inventory = inventory;
        this.reservations = reservations;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save system state
    public void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("✅ System state saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving system state: " + e.getMessage());
        }
    }

    // Load system state
    public SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("✅ System state loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("⚠️ No previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error loading state. Starting with safe defaults.");
        }

        // Safe fallback
        return new SystemState(new HashMap<>(), new ArrayList<>());
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        PersistenceService persistenceService = new PersistenceService();

        // STEP 1: Load previous state (if exists)
        SystemState state = persistenceService.load();

        Map<String, Integer> inventory = state.inventory;
        List<Reservation> reservations = state.reservations;

        // Initialize defaults if empty
        if (inventory.isEmpty()) {
            inventory.put("Single", 2);
            inventory.put("Double", 1);
        }

        // STEP 2: Simulate system activity
        System.out.println("\n--- Current Inventory ---");
        System.out.println(inventory);

        System.out.println("\n--- Existing Reservations ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }

        // Add new booking (simulate new activity)
        Reservation newRes = new Reservation(
                "RES-" + (reservations.size() + 1),
                "Single",
                "SI-" + new Random().nextInt(1000)
        );

        reservations.add(newRes);
        inventory.put("Single", inventory.get("Single") - 1);

        System.out.println("\nNew Booking Added: " + newRes);

        // STEP 3: Save updated state before shutdown
        persistenceService.save(new SystemState(inventory, reservations));

        System.out.println("\n--- Final Inventory ---");
        System.out.println(inventory);
    }
}
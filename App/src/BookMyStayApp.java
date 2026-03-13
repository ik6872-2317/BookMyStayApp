/**
 * UseCase2RoomInitialization
 *
 * This class demonstrates object-oriented modeling of hotel rooms using
 * inheritance and abstraction. It shows basic room types and static availability.
 *
 * @author Student
 * @version 2.1
 */

abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double size; // in square meters
    private double pricePerNight;

    public Room(String roomType, int numberOfBeds, double size, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getSize() {
        return size;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Number of Beds: " + numberOfBeds);
        System.out.println("Size: " + size + " sqm");
        System.out.println("Price per Night: $" + pricePerNight);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 20.0, 50.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 30.0, 80.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 50.0, 150.0);
    }
}

public class BookMyStayApp {

    // Static availability variables
    private static int availableSingleRooms = 10;
    private static int availableDoubleRooms = 5;
    private static int availableSuiteRooms = 2;

    public static void main(String[] args) {

        // Create room objects
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        System.out.println("=== Hotel Room Types & Availability ===\n");

        // Display Single Room details and availability
        singleRoom.displayRoomDetails();
        System.out.println("Available: " + availableSingleRooms + "\n");

        // Display Double Room details and availability
        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + availableDoubleRooms + "\n");

        // Display Suite Room details and availability
        suiteRoom.displayRoomDetails();
        System.out.println("Available: " + availableSuiteRooms + "\n");

        System.out.println("Application terminated.");
    }
}
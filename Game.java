import java.util.Scanner;
import java.util.ArrayList;

public class Game {
    private Room currentRoom;
    private ArrayList<Item> inventory;
    private ArrayList<Customer> waitingLine; 
    private double cafeFunds;
    private boolean playing;

    public Game() {
        this.inventory = new ArrayList<>();
        this.waitingLine = new ArrayList<>();
        this.cafeFunds = 0;
        this.playing = true;
        createRooms();
    }

    private void createRooms() {
        Room cafe = new Room("the Main Matcha Bar");
        Room alley = new Room("a dark alleyway behind the shop");
        Room storage = new Room("the locked storage closet");

        // Exits
        cafe.setExit("north", storage);
        storage.setExit("south", cafe); 
        cafe.setExit("south", alley);
        alley.setExit("north", cafe);

        // Items
        alley.addItem(new Item("key", "A bronze key."));
        storage.addItem(new Item("matcha", "Ceremonial powder."));
        
        // Controlled Customer Flow (Man -> Woman -> Man)
        waitingLine.add(new MaleCustomer("Chad"));
        waitingLine.add(new LesbianCustomer("Thea"));
        waitingLine.add(new MaleCustomer("Jungkook"));
        waitingLine.add(new LesbianCustomer("Miu"));

        currentRoom = cafe;
        currentRoom.setCustomer(waitingLine.remove(0)); 
    }

    public void play() {
        Scanner reader = new Scanner(System.in);
        System.out.println("==========================================");
        System.out.println("   WELCOME TO THE SAPPHIC MATCHA BAR      ");
        System.out.println("==========================================");
        System.out.println("Goal: Find matcha, serve women, and make bank.");
        System.out.println("Type 'map' or 'help' if you need guidance.");
        System.out.println(currentRoom.getLongDescription());

        while (playing) {
            System.out.print("\n> ");
            String input = reader.nextLine().toLowerCase();
            processCommand(input);
        }
        System.out.println("\nFinal Funds: $" + cafeFunds + ". Goodbye!");
        reader.close();
    }

    private void processCommand(String input) {
        if (input.trim().isEmpty()) return;
        String[] words = input.split(" ");
        String command = words[0];

        if (command.equals("map")) drawMap();
        else if (command.equals("help")) printHelp();
        else if (command.equals("go")) {
            if (words.length < 2) System.out.println("Go where?");
            else move(words[1]);
        } 
        else if (command.equals("take")) {
            if (words.length < 2) System.out.println("Take what?");
            else takeItem(words[1]);
        } 
        else if (command.equals("serve")) serveCustomer();
        else if (command.equals("inventory") || command.equals("i")) showInventory();
        else if (command.equals("look")) System.out.println(currentRoom.getLongDescription());
        else if (command.equals("quit")) playing = false;
        else System.out.println("Unknown command. Type 'help'.");
    }

    private void drawMap() {
        System.out.println("\n--- CAFE MAP ---");
        System.out.println(" [ STORAGE ]  (North)");
        System.out.println("      |         ");
        System.out.println(" [ MAIN BAR ] (Center - You are here)");
        System.out.println("      |         ");
        System.out.println(" [  ALLEY   ] (South)");
        System.out.println("----------------");
    }

    private void printHelp() {
        System.out.println("\nCOMMANDS: go [north/south], take [item], look, serve, inventory, map, quit");
    }

    private void move(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom == null) {
            System.out.println("You hit a wall.");
            return;
        }
        if (nextRoom.getDescription().contains("storage") && !hasItem("key")) {
            System.out.println("Locked! Find the key in the Alley (south).");
        } else {
            currentRoom = nextRoom;
            System.out.println("Moved " + direction + ". " + currentRoom.getLongDescription());
        }
    }

    private void takeItem(String itemName) {
        Item toTake = null;
        for (Item i : currentRoom.getItems()) {
            if (i.getName().equalsIgnoreCase(itemName)) {
                toTake = i;
                break;
            }
        }
        if (toTake != null) {
            inventory.add(toTake);
            currentRoom.getItems().remove(toTake);
            System.out.println("You took the " + toTake.getName());
        } else System.out.println("Not here.");
    }

    private void showInventory() {
        if (inventory.isEmpty()) System.out.println("Pockets are empty.");
        else {
            System.out.print("Inventory: ");
            for (Item i : inventory) System.out.print("[" + i.getName() + "] ");
            System.out.println();
        }
    }

    private void serveCustomer() {
        Customer c = currentRoom.getCustomer();
        if (c == null) {
            System.out.println("No one here.");
            return;
        }
        if (!hasItem("matcha")) {
            System.out.println("Get matcha from storage first!");
            return;
        }
        
        double price = c.calculateBill();
        cafeFunds += price;
        System.out.println("\n--- SERVING " + c.getName() + " ---");
        System.out.println("Total: $" + price + (price > 10 ? " (Man Tax applied)" : " (Sapphic discount applied)"));
        System.out.println("Reaction: " + c.getReaction());
        
        if (!waitingLine.isEmpty()) {
            currentRoom.setCustomer(waitingLine.remove(0));
            System.out.println("\n*** Next up: " + currentRoom.getCustomer().getName() + " ***");
        } else {
            currentRoom.setCustomer(null);
            System.out.println("\nLine is empty!");
        }
    }

    private boolean hasItem(String name) {
        for (Item i : inventory) if (i.getName().equalsIgnoreCase(name)) return true;
        return false;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
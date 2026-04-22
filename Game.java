import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class Game {
    private Room currentRoom;
    private ArrayList<Item> inventory;
    private ArrayList<Customer> waitingLine; 
    private double cafeFunds;
    private boolean playing;

    // --- Win/Loss Constants ---
    private final double WIN_GOAL = 100.00;
    private final int MAX_CUSTOMERS = 15;
    private int customersServedCount = 0;
    private int matchaServings = 0; // New serving size tracker

    public Game() {
        this.inventory = new ArrayList<>();
        this.waitingLine = new ArrayList<>();
        this.cafeFunds = 0;
        this.playing = true;
        createRooms();
        generateUniqueLine(); 
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
        storage.addItem(new Item("matcha", "Ceremonial powder (5 servings)."));

        currentRoom = cafe;
    }

    private void generateUniqueLine() {
        ArrayList<String> maleNames = new ArrayList<>(Arrays.asList("Chad", "Jungkook", "Brad", "Jae", "Clavicular"));
        ArrayList<String> sapphicNames = new ArrayList<>(Arrays.asList("Thea", "Miu", "Milan", "Bella", "Jojo Siwa", "Sasha", "Kai", "Jade", "Elena", "Muna"));

        Collections.shuffle(maleNames);
        Collections.shuffle(sapphicNames);

        for (int i = 0; i < MAX_CUSTOMERS; i++) {
            if (i % 2 == 0 && !maleNames.isEmpty()) {
                waitingLine.add(new MaleCustomer(maleNames.remove(0)));
            } else if (!sapphicNames.isEmpty()) {
                waitingLine.add(new LesbianCustomer(sapphicNames.remove(0)));
            }
        }
        
        if (!waitingLine.isEmpty()) {
            currentRoom.setCustomer(waitingLine.remove(0));
        }
    }

    public void play() {
        Scanner reader = new Scanner(System.in);
        System.out.println("==========================================");
        System.out.println("     WELCOME TO THE SAPPHIC MATCHA BAR     ");
        System.out.println("==========================================");
        System.out.println("Goal: Earn $" + WIN_GOAL + " by serving customers.");
        System.out.println("Type 'help' to see what you can do!");
        System.out.println("==========================================");

        while (playing) {
            System.out.print("\n[Matcha Servings: " + matchaServings + " | Funds: $" + cafeFunds + "]\n> ");
            String input = reader.nextLine().toLowerCase();
            processCommand(input);
            checkWinLossConditions();
        }
        reader.close();
    }

    private void checkWinLossConditions() {
        if (cafeFunds >= WIN_GOAL) {
            System.out.println("\n VICTORY! ");
            System.out.println("You made $" + cafeFunds + ". The bar is a legend!");
            playing = false;
        } 
        else if (customersServedCount >= MAX_CUSTOMERS && cafeFunds < WIN_GOAL) {
            System.out.println("\n SHIFT OVER: NOT ENOUGH PROFIT ");
            System.out.println("You served everyone but didn't meet the goal.");
            playing = false;
        }
    }

    private void processCommand(String input) {
        if (input.trim().isEmpty()) return;
        String[] words = input.split(" ");
        String command = words[0];

        if (command.equals("map")) drawMap();
        else if (command.equals("help")) printHelp();
        else if (command.equals("go")) {
            if (words.length < 2) System.out.println("Go where? (north, south, east, west)");
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
        else System.out.println("Unknown command. Type 'help' for a list of commands.");
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
            
            // Logic to restock the storage room so you can always go back for more
            if (currentRoom.getDescription().contains("storage") && currentRoom.getItems().isEmpty()) {
                currentRoom.addItem(new Item("matcha", "A fresh tin of ceremonial powder (5 servings)."));
                System.out.println("There is a fresh tin of matcha on the shelf.");
            }
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
            if (toTake.getName().equalsIgnoreCase("matcha")) {
                matchaServings += 5; // Add servings instead of just an item
                currentRoom.getItems().remove(toTake);
                System.out.println("You restocked! You now have " + matchaServings + " servings of matcha.");
            } else {
                inventory.add(toTake);
                currentRoom.getItems().remove(toTake);
                System.out.println("You took the " + toTake.getName());
            }
        } else System.out.println("That item isn't here.");
    }

    private void serveCustomer() {
        Customer c = currentRoom.getCustomer();
        if (c == null) {
            System.out.println("No one here to serve.");
            return;
        }

        if (matchaServings <= 0) {
            System.out.println("Out of matcha! Go back to the storage closet (north) to get more.");
            return;
        }
        
        matchaServings--; // Use one serving

        String identityLabel = "";
        if (c instanceof MaleCustomer) {
            identityLabel = "[MALE]";
        } else if (c instanceof LesbianCustomer) {
            identityLabel = "[WOMAN]";
        }

        double price = c.calculateBill();
        cafeFunds += price;
        customersServedCount++;
        
        System.out.println("\n--- SERVING " + identityLabel + " " + c.getName() + " ---");
        System.out.println("Total: $" + price);
        System.out.println("Reaction: " + c.getReaction());
        System.out.println("Servings remaining: " + matchaServings);
        
        if (!waitingLine.isEmpty()) {
            currentRoom.setCustomer(waitingLine.remove(0));
            System.out.println("\n*** Next up: " + currentRoom.getCustomer().getName() + " ***");
        } else {
            currentRoom.setCustomer(null);
            System.out.println("\nNo more customers in line!");
        }
    }

    private void showInventory() {
        System.out.println("Matcha Servings: " + matchaServings);
        if (inventory.isEmpty()) System.out.println("Your pockets are empty (except for your key if you have it).");
        else {
            System.out.print("Items: ");
            for (Item i : inventory) System.out.print("[" + i.getName() + "] ");
            System.out.println();
        }
    }

    private boolean hasItem(String name) {
        for (Item i : inventory) {
            if (i.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    private void drawMap() {
        System.out.println("\n--- CAFE MAP ---");
        System.out.println(" [ STORAGE ]  (North - Needs Key)");
        System.out.println("      |         ");
        System.out.println(" [ MAIN BAR ] (Center)");
        System.out.println("      |         ");
        System.out.println(" [  ALLEY   ] (South - Find Key Here)");
        System.out.println("----------------");
        System.out.println("You are currently at: " + currentRoom.getDescription());
    }

    private void printHelp() {
        System.out.println("\n--- HOW TO PLAY ---");
        System.out.println("go [north/south] - Move between rooms");
        System.out.println("take [item]      - Pick up items (like the key or matcha)");
        System.out.println("serve            - Serve the customer at the counter");
        System.out.println("look             - Check your surroundings");
        System.out.println("inventory        - Check your items and matcha count");
        System.out.println("map              - See the layout of the cafe");
        System.out.println("quit             - Exit the game");
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
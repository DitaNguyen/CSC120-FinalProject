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
    private GameWindow window; 

    private final double WIN_GOAL = 100.00;
    private final int MAX_CUSTOMERS = 15;
    private int customersServedCount = 0;
    private int matchaServings = 0; 

    public Game() {
        this.inventory = new ArrayList<>();
        this.waitingLine = new ArrayList<>();
        this.window = new GameWindow(); 
        this.cafeFunds = 0;
        this.playing = true;
        createRooms();
        generateUniqueLine(); 
        
        window.updateDisplay("bar_view.gif"); 
    }

    private void createRooms() {
        Room cafe = new Room("the Main Matcha Bar");
        Room alley = new Room("a dark alleyway behind the shop");
        Room storage = new Room("the locked storage closet");

        cafe.setExit("north", storage);
        storage.setExit("south", cafe); 
        cafe.setExit("south", alley);
        alley.setExit("north", cafe);

        alley.addItem(new Item("key", "A bronze key."));
        storage.addItem(new Item("matcha", "Ceremonial powder (5 servings)."));

        currentRoom = cafe;
    }

    private void generateUniqueLine() {
        ArrayList<String> maleNames = new ArrayList<>(Arrays.asList("Chad", "Jungkook", "Brad", "Jae", "Clavicular"));
        ArrayList<String> sapphicNames = new ArrayList<>(Arrays.asList("Thea", "Miu", "Milan", "Bella", "Jojo Siwa", "Katie", "Jade", "Gabby", "Maeve"));

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
        System.out.println("Type 'help' to see all of the commands you can use.");
        System.out.println("Goal: Earn $" + WIN_GOAL + " by serving women!");
        System.out.println("==========================================");

        while (playing) {

            // FIXED UI (prevents merged text bug)
            System.out.println("\n------------------------------------------");

            if (currentRoom.getDescription().contains("Main Matcha Bar") 
                && currentRoom.getCustomer() != null) {

                System.out.println("Standing at the bar: " 
                    + currentRoom.getCustomer().getName());
            }

            if (!currentRoom.getItems().isEmpty()) {
                for (Item i : currentRoom.getItems()) {
                    System.out.println(" You see a " + i.getName() 
                        + " here! Type 'take " + i.getName() + "'");
                }
            }

            System.out.println("------------------------------------------");

            System.out.print("\n[Matcha Servings: " + matchaServings 
                + " | Funds: $" + cafeFunds + "]\n> ");

            String input = reader.nextLine().toLowerCase();
            processCommand(input);
            checkWinLossConditions();
        }

        reader.close();
    }

    private void checkWinLossConditions() {
        if (cafeFunds >= WIN_GOAL) {
            System.out.println("\n VICTORY! ");
            window.updateDisplay("victory.gif");
            playing = false;
        } 
        else if (customersServedCount >= MAX_CUSTOMERS && cafeFunds < WIN_GOAL) {
            System.out.println("\n SHIFT OVER: NOT ENOUGH PROFIT ");
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
    }

    private void move(String direction) {
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("You hit a wall.");
            return;
        }

        if (nextRoom.getDescription().contains("storage") && !hasItem("key")) {
            System.out.println("Locked! You need a key. Maybe check the alley to the south?");
            return;
        }

        currentRoom = nextRoom;

        System.out.println("Moved " + direction);
        System.out.println("You are now in " + currentRoom.getDescription() + ".");

        if (currentRoom.getDescription().contains("alley")) window.updateDisplay("alley.gif");
        else if (currentRoom.getDescription().contains("storage")) window.updateDisplay("storage.gif");
        else window.updateDisplay("bar_view.gif");
    }

    private void serveCustomer() {
        Customer c = currentRoom.getCustomer();

        if (c == null) {
            System.out.println("No one here.");
            return;
        }

        if (matchaServings <= 0) {
            System.out.println("Out of matcha! You need to go north to the storage room.");
            return;
        }

        matchaServings--;

        if (c instanceof LesbianCustomer) {
            window.updateDisplay("happy-pusheen.gif"); 
            System.out.println(" Serving a woman! Pusheen approves.");
        } else {
            window.updateDisplay("pusheen-angry.gif");
        }

        double price = c.calculateBill();
        cafeFunds += price;
        customersServedCount++;

        System.out.println("\n--- SERVING " + c.getName() + " ---");
        System.out.println("Total: $" + price);

        if (c instanceof LesbianCustomer) {
            if (price > 15.0) {
                System.out.println(c.getName() + ": \"Totally worth it!\" ");
            } else {
                System.out.println(c.getName() + ": \"You are so sweet! I will come back;)\" ");
            }
        } else {
            if (price > 10.0) {
                System.out.println(c.getName() + ": \"Man what the hell.\" ");
            } else {
                System.out.println(c.getName() + ": \"Thanks. Here's your money.\"");
            }
        }

        if (!waitingLine.isEmpty()) {
            currentRoom.setCustomer(waitingLine.remove(0));
            System.out.println("\n*** Next up: " + currentRoom.getCustomer().getName() + " ***");
        } else {
            currentRoom.setCustomer(null);
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
                matchaServings += 5;
                System.out.println("Matcha restocked! (5 servings obtained)");
                // IMPORTANT: do NOT remove item → infinite supply
            } else {
                inventory.add(toTake);
                currentRoom.getItems().remove(toTake);
                System.out.println("Took " + toTake.getName());
            }
        } else {
            System.out.println("That item isn't here.");
        }
    }

    private void showInventory() {
        System.out.println("Matcha servings: " + matchaServings);
        System.out.print("Inventory: ");
        for (Item i : inventory) System.out.print("[" + i.getName() + "] ");
        System.out.println();
    }

    private boolean hasItem(String name) {
        for (Item i : inventory) {
            if (i.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    private void drawMap() {
        System.out.println("\n      [ STORAGE ] (North - Matcha)");
        System.out.println("          |");
        System.out.println("      [  BAR    ] (Center - Customers)");
        System.out.println("          |");
        System.out.println("      [  ALLEY  ] (South - Key)");
    }

    private void printHelp() {
        System.out.println("Commands: go [north/south], take [item], serve, look, inventory, map, quit");
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
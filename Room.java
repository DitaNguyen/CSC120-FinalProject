import java.util.HashMap;
import java.util.ArrayList;

public class Room {
    private String description;
    private HashMap<String, Room> exits;
    private ArrayList<Item> items;
    private Customer residentCustomer;

    public Room(String description) {
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
    }

    public String getDescription() { return description; }
    public void setExit(String direction, Room neighbor) { exits.put(direction, neighbor); }
    public Room getExit(String direction) { return exits.get(direction); }
    public void addItem(Item item) { items.add(item); }
    public ArrayList<Item> getItems() { return items; }
    public void setCustomer(Customer c) { this.residentCustomer = c; }
    public Customer getCustomer() { return residentCustomer; }

    public String getLongDescription() {
        String desc = "You are in " + description + ".\nItems here: ";
        if (items.isEmpty()) desc += "none";
        else for (Item i : items) desc += "[" + i.getName() + "] ";
        
        if (residentCustomer != null) {
            desc += "\nAt the counter: " + residentCustomer.getName();
        }
        return desc;
    }
}
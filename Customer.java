/**
 * Abstract class representing a customer. 
 * Demonstrates inheritance and polymorphism.
 */
public abstract class Customer {
    protected String name;
    protected boolean hasBeenServed = false;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    
    /**
     * Abstract method: each customer type calculates their own bill logic.
     */
    public abstract double calculateBill();
    
    public abstract String getReaction();
}
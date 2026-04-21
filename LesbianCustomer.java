public class LesbianCustomer extends Customer {
    public LesbianCustomer(String name) { super(name); }

    @Override
    public double calculateBill() {
        return 6.50; // Fair price
    }

    @Override
    public String getReaction() {
        return "She gives you a friendly nod and a great tip. The vibes are immaculate.";
    }
}
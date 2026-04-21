public class MaleCustomer extends Customer {
    public MaleCustomer(String name) { super(name); }

    @Override
    public double calculateBill() {
        return 25.00; // The "Man Tax" 
    }

    @Override
    public String getReaction() {
        return "He looks confused by the price but pays anyway. The cafe is funded!";
    }
}
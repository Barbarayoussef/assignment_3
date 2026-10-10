package bad;

import java.util.List;

public class BadOrderExample {

    static class Customer {
        private final String name;
        private final String email;

        Customer(String name, String email) {
            this.name = name;
            this.email = email;
        }

        String getName() { return name; }
        String getEmail() { return email; }
    }

    static class Payment {
        private final String type;

        Payment(String type) { this.type = type; }

        String getType() { return type; }
    }

    static class Order {
        private final String name;
        private final double totalPrice;

        Order(String name, double totalPrice) {
            this.name = name;
            this.totalPrice = totalPrice;
        }

        String getName() { return name; }
        double getTotalPrice() { return totalPrice; }

        void ship() {
            System.out.println("Shipping " + name);
        }
    }

    static class DeliveryOrder extends Order {
        DeliveryOrder(String name, double price) { super(name, price); }

        @Override
        void ship() {
            System.out.println("Delivering " + getName() + " to customer's address");
        }
    }

    static class PickUpOrder extends Order {
        PickUpOrder(String name, double price) { super(name, price); }

        @Override
        void ship() {
            throw new UnsupportedOperationException("Pick-up orders can't be shipped");
        }
    }

    static class OrderManager {

        public void processOrder(Order order) {
            System.out.println("Processing order: " + order.getName() + " now...");
        }

        public void processPayment(Order order, Payment payment) {
            System.out.println("Processing payment of order: " + order.getName());
            System.out.println("Issuing payment for amount: " + order.getTotalPrice());
            if (payment.getType().equalsIgnoreCase("VISA")) {
                System.out.println("Processing visa card payments...");
            } else if (payment.getType().equalsIgnoreCase("MASTER_CARD")) {
                System.out.println("Processing master card payments...");
            } else if (payment.getType().equalsIgnoreCase("AMERICAN_EXPRESS")) {
                System.out.println("Processing american express card payments...");
            } else {
                throw new UnsupportedOperationException("Un supported payment...");
            }
        }

        void sendEmailNotification(Customer customer, String message) {
            System.out.println("Sending email notification to: " + customer.getEmail()
                    + " with message: " + message);
        }
    }

    interface UserOperations {
        void register(String name);
        void login(String name);
        void subscribe(String name);
    }

    static class Subscriber implements UserOperations {
        public void register(String name) {
            System.out.println(name + " registered");
        }

        public void login(String name) {
            throw new UnsupportedOperationException("Subscribers can't login");
        }

        public void subscribe(String name) {
            System.out.println(name + " subscribed to newsletter");
        }
    }

    static class UserManagement {
        private final OrderManager orderManager = new OrderManager();

        void placeOrder(Customer customer, Order order, Payment payment) {
            orderManager.processOrder(order);
            orderManager.processPayment(order, payment);
            orderManager.sendEmailNotification(customer, "Order " + order.getName() + " confirmed");
        }
    }

    static void shipAll(List<Order> orders) {
        for (Order order : orders) {
            order.ship();
        }
    }

    public static void main(String[] args) {
        Customer customer = new Customer("Sara", "sara@example.com");
        Order order = new DeliveryOrder("Laptop", 1200.0);

        new UserManagement().placeOrder(customer, order, new Payment("VISA"));

        try {
            new OrderManager().processPayment(order, new Payment("PAYPAL"));
        } catch (UnsupportedOperationException e) {
            System.out.println("Payment failed: " + e.getMessage());
        }

        try {
            shipAll(List.of(new DeliveryOrder("Phone", 800.0), new PickUpOrder("Book", 20.0)));
        } catch (UnsupportedOperationException e) {
            System.out.println("Shipping failed: " + e.getMessage());
        }

        try {
            new Subscriber().login("Omar");
        } catch (UnsupportedOperationException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }
}

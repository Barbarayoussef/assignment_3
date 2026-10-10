package good;



import java.util.List;

public class GoodOrderExample {

    static class Customer {
        private final String name;
        private final String email;

        Customer(String name, String email) {
            this.name = name;
            this.email = email;
        }

        String getName() {
            return name;
        }

        String getEmail() {
            return email;
        }
    }

    static class Payment {
        private final String type;

        Payment(String type) {
            this.type = type;
        }

        String getType() {
            return type;
        }
    }

    static class Order {
        private final String name;
        private final double totalPrice;

        Order(String name, double totalPrice) {
            this.name = name;
            this.totalPrice = totalPrice;
        }

        String getName() {
            return name;
        }

        double getTotalPrice() {
            return totalPrice;
        }

    }

    //first fix to implment ship from interface because not all types of orders are shipable(LSP)
    static class DeliveryOrder extends Order implements Shipable {
        DeliveryOrder(String name, double price) {
            super(name, price);
        }


        public void ship() {
            System.out.println("Delivering " + getName() + " to customer's address");
        }
    }

    static class PickUpOrder extends Order {
        PickUpOrder(String name, double price) {
            super(name, price);
        }

    }
    static class OrderManager {

        public void processOrder(GoodOrderExample.Order order) {
            System.out.println("Processing order: " + order.getName() + " now...");
        }
    }
    static class PaymentService {
        private final PaymentMethod paymentMethod;

        PaymentService(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
        }
        public void processPayment(Order order, Payment payment) {
            System.out.println(
                    "Processing payment of order: " + order.getName()
            );
            System.out.println(
                    "Issuing payment for amount: " + order.getTotalPrice()
            );
            paymentMethod.pay(order,payment);
        }
    }
    static class VisaPayment implements PaymentMethod{
        @Override
        public void pay(Order order, Payment payment) {
            System.out.println("Processing visa card payments...");

        }
    }
    static class MasterCardPayment implements PaymentMethod{
        public void pay(Order order,Payment payment) {
            System.out.println("Processing Master card payments...");

        }
    }
    static class AmeriacnExpressPayment implements PaymentMethod{
        public void pay(Order order,Payment payment) {
            System.out.println("Processing Ameriacn express payment...");
        }


}
static class SendEmailNotification{
        void sendEmail(GoodOrderExample.Customer customer, String message) {
            System.out.println("Sending email notification to: " + customer.getEmail()
                    + " with message: " + message);
        }
}
    static class Subscriber implements Register,Subscribe {
        public void register(String name) {
            System.out.println(name + " registered");
        }
        public void subscribe(String name) {
            System.out.println(name + " subscribed to newsletter");
        }
    }
    static class UserManagement {
        private final OrderManager orderManager;
        private  final PaymentService  paymentService;
        private  final SendEmailNotification sendEmailNotification;

        UserManagement(OrderManager orderManager,PaymentService paymentService,SendEmailNotification sendEmailNotification) {
            this.orderManager = orderManager;
            this.paymentService = paymentService;
            this.sendEmailNotification = sendEmailNotification;
        }
        void placeOrder(Customer customer, Order order,Payment payment) {
            orderManager.processOrder(order);
            paymentService.processPayment(order, payment);
            sendEmailNotification.sendEmail(customer, "Order " + order.getName() + " confirmed");
        }
    }
    static void shipAll(List<? extends Shipable> orders) {
        for (Shipable order : orders) {
            order.ship();
        }
    }
    public static void main(String[] args) {
        Customer customer = new Customer("Sara", "sara@example.com");
        Order order = new DeliveryOrder("Laptop", 1200.0);



        UserManagement userManagement = new UserManagement(
                new OrderManager(),
                new PaymentService(new VisaPayment()),
                new SendEmailNotification()
        );

        userManagement.placeOrder(
                customer, order, new Payment("VISA")
        );
        try {
            new PaymentService(new VisaPayment()).processPayment(order, new Payment("PAYPAL"));
        } catch (UnsupportedOperationException e) {
            System.out.println("Payment failed: " + e.getMessage());
        }

        try {
            shipAll((List<? extends Shipable>) List.of(new DeliveryOrder("Phone", 800.0), new PickUpOrder("Book", 20.0)));
        } catch (UnsupportedOperationException e) {
            System.out.println("Shipping failed: " + e.getMessage());
        }


    }
}








package good;

public interface PaymentMethod {
  void pay(GoodOrderExample.Order order, GoodOrderExample.Payment payment);
}

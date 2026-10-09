package fi.metropolia.omagr.grimaux.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="orders")
public class ShopOrder {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Integer id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="customer_id",nullable=false) public Customer customer;
 @Column(name="order_date",nullable=false) public LocalDateTime orderDate;
 @Column(name="delivery_date") public LocalDateTime deliveryDate;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="shipping_address_id") public CustomerAddress shippingAddress;
 @Column(length=50) public String status;
}

package fi.metropolia.omagr.grimaux.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity @Table(name="orderitems")
public class OrderItem {
 @EmbeddedId public OrderItemId id;
 @MapsId("orderId") @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="order_id") public ShopOrder order;
 @MapsId("productId") @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id") public Product product;
 @Column(nullable=false) public int quantity;
 @Column(name="unit_price",nullable=false,precision=10,scale=2) public BigDecimal unitPrice;
}

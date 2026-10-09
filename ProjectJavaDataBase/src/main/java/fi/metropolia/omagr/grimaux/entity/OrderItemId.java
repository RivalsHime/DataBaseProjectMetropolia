package fi.metropolia.omagr.grimaux.entity;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
@Embeddable
public class OrderItemId implements Serializable {
 @Column(name="order_id") public Integer orderId;
 @Column(name="product_id") public Integer productId;
 public OrderItemId(){}
 public OrderItemId(Integer orderId,Integer productId){this.orderId=orderId;this.productId=productId;}
 @Override public boolean equals(Object o){return o instanceof OrderItemId i && Objects.equals(orderId,i.orderId)&&Objects.equals(productId,i.productId);}
 @Override public int hashCode(){return Objects.hash(orderId,productId);}
}

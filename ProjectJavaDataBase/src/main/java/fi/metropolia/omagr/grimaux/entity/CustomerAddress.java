package fi.metropolia.omagr.grimaux.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity @Table(name="customeraddresses")
public class CustomerAddress {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Integer id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="customer_id",nullable=false) public Customer customer;
 @Column(name="street_address",nullable=false) public String streetAddress;
 @Column(name="postal_code") public String postalCode;
 @Column(nullable=false) public String city;
 public String country;
}

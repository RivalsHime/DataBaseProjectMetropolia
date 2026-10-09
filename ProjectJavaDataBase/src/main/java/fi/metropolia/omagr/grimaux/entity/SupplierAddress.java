package fi.metropolia.omagr.grimaux.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity @Table(name="supplieraddresses")
public class SupplierAddress {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Integer id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="supplier_id",nullable=false) public Supplier supplier;
 @Column(name="street_address",nullable=false) public String streetAddress;
 @Column(name="postal_code") public String postalCode;
 @Column(nullable=false) public String city;
 public String country;
}

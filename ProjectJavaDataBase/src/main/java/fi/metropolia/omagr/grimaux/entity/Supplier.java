package fi.metropolia.omagr.grimaux.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity @Table(name="suppliers")
public class Supplier {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Integer id;
 @Column(nullable=false) public String name;
 @Column(name="contact_name") public String contactName;
 public String phone;
 public String email;
}

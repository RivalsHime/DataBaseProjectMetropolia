package fi.metropolia.omagr.grimaux.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity @Table(name="customers")
public class Customer {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Integer id;
 @Column(name="first_name",nullable=false) public String firstName;
 @Column(name="last_name",nullable=false) public String lastName;
 @Column(nullable=false) public String email;
 public String phone;
}

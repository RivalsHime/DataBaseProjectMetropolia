package fi.metropolia.omagr.grimaux.entity;
import jakarta.persistence.*;
@Entity @Table(name="contacts")
public class Contact {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Integer id;
 @Column(nullable=false) public String email;
 @Column(name="reference", nullable=false, columnDefinition="CHAR(32)") public String reference;
}

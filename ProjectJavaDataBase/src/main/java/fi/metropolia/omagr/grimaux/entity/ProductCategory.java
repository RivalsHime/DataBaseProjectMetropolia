package fi.metropolia.omagr.grimaux.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity @Table(name="productcategories")
public class ProductCategory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Integer id;
 @Column(nullable=false) public String name;
 @Column(columnDefinition="text") public String description;
}

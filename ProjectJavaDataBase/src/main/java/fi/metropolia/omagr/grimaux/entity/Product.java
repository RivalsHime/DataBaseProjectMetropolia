package fi.metropolia.omagr.grimaux.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity @Table(name="products")
public class Product {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Integer id;
 @Column(nullable=false) public String name;
 @Column(columnDefinition="text") public String description;
 @Column(nullable=false,precision=10,scale=2) public BigDecimal price;
 @Column(name="stock_quantity",nullable=false) public int stockQuantity;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="category_id") public ProductCategory category;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="supplier_id") public Supplier supplier;
}

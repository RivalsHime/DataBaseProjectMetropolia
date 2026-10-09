package fi.metropolia.omagr.grimaux.dto;
import java.math.BigDecimal;
public record ProductView(Integer id, String name, BigDecimal price, int stockQuantity) {}

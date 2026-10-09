package fi.metropolia.omagr.grimaux.repository;

import fi.metropolia.omagr.grimaux.entity.OrderItem;
import fi.metropolia.omagr.grimaux.entity.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
    List<OrderItem> findByOrder_Id(Integer orderId);
}

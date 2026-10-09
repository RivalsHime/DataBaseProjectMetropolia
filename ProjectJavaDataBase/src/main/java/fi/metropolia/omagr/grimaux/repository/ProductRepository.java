package fi.metropolia.omagr.grimaux.repository;
import fi.metropolia.omagr.grimaux.entity.*;
import org.springframework.data.jpa.repository.*;
import jakarta.persistence.LockModeType;
import java.util.*;

public interface ProductRepository extends JpaRepository<Product,Integer>{
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 Optional<Product> findById(Integer id);
}

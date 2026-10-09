package fi.metropolia.omagr.grimaux.repository;
import fi.metropolia.omagr.grimaux.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress,Integer>{}

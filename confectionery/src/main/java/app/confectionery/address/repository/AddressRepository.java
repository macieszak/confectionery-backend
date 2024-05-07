package app.confectionery.address.repository;

import app.confectionery.address.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findByUserId(UUID userId);

    List<Address> findAllByUserIdAndIdNot(UUID userId, Integer id);

    Optional<Address> findByAddressNameAndUserId(String address, UUID userId);

}

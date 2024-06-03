package app.confectionery.modules.address.controller;

import app.confectionery.modules.address.model.Address;
import app.confectionery.modules.address.model.DTO.AddressDTO;
import app.confectionery.modules.address.model.DTO.NewAddressDTO;
import app.confectionery.modules.address.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<AddressDTO>> getAllUserAddresses(@PathVariable UUID userId) {
        List<AddressDTO> addresses = addressService.getAllAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<AddressDTO> createAddress(@PathVariable UUID userId, @RequestBody NewAddressDTO newAddressDTO) {
        Address address = addressService.addNewAddress(userId, newAddressDTO);
        AddressDTO addressDTO = mapToDTO(address);
        return ResponseEntity.ok(addressDTO);
    }

    @PutMapping("/{userId}/addresses/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable UUID userId, @PathVariable Integer id, @Valid @RequestBody NewAddressDTO newAddressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(userId, id, newAddressDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{userId}/addresses/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID userId, @PathVariable Integer id) {
        addressService.deleteAddress(userId, id);
        return ResponseEntity.ok().build();
    }

    private AddressDTO mapToDTO(Address address) {
        return new AddressDTO(address.getId(), address.getAddressName(), address.getUser().getId());
    }

}

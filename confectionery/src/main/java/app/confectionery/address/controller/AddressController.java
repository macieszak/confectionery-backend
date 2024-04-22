package app.confectionery.address.controller;

import app.confectionery.address.model.Address;
import app.confectionery.address.model.AddressDTO;
import app.confectionery.address.model.NewAddressDTO;
import app.confectionery.address.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/user/{userId}")   //OK
    public ResponseEntity<List<AddressDTO>> getAllUserAddresses(@PathVariable UUID userId) {
        List<AddressDTO> addresses = addressService.getAllAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/add")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody NewAddressDTO newAddressDTO) {
        Address address = addressService.addNewAddress(newAddressDTO);
        AddressDTO addressDTO = mapToDTO(address);
        return ResponseEntity.ok(addressDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Integer id, @Valid @RequestBody NewAddressDTO newAddressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(id, newAddressDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok().build();
    }

    private AddressDTO mapToDTO(Address address) {
        return new AddressDTO(address.getId(), address.getAddressName(), address.getUser().getId());
    }

}

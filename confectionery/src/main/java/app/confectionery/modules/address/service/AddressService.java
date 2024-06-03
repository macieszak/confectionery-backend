package app.confectionery.modules.address.service;

import app.confectionery.modules.address.model.Address;
import app.confectionery.modules.address.model.DTO.AddressDTO;
import app.confectionery.modules.address.model.DTO.NewAddressDTO;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    List<AddressDTO> getAllAddressesByUserId(UUID userId);

    Address addNewAddress(UUID userId, NewAddressDTO newAddressDTO);

    AddressDTO updateAddress(UUID userId, Integer id, NewAddressDTO newAddressDTO);

    void deleteAddress(UUID userId, Integer id);

}

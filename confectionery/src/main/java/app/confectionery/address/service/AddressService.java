package app.confectionery.address.service;

import app.confectionery.address.model.Address;
import app.confectionery.address.model.AddressDTO;
import app.confectionery.address.model.NewAddressDTO;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    List<AddressDTO> getAllAddressesByUserId(UUID userId);

    Address addNewAddress(NewAddressDTO newAddressDTO);

    AddressDTO updateAddress(Integer id, NewAddressDTO newAddressDTO);

    void deleteAddress(Integer id);

}

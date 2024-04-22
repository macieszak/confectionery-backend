package app.confectionery.address.service;

import app.confectionery.address.model.Address;
import app.confectionery.address.model.AddressDTO;
import app.confectionery.address.model.NewAddressDTO;
import app.confectionery.address.repository.AddressRepository;
import app.confectionery.exception.UserNotFoundException;
import app.confectionery.user.model.User;
import app.confectionery.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public List<AddressDTO> getAllAddressesByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return addressRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }

    @Override
    public Address addNewAddress(NewAddressDTO newAddressDTO) {
        if (newAddressDTO.getAddress() == null || newAddressDTO.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }

        User user = userRepository.findById(newAddressDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = new Address(null, newAddressDTO.getAddress(), user);
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public AddressDTO updateAddress(Integer id, NewAddressDTO newAddressDTO) {
        if (newAddressDTO.getAddress() == null || newAddressDTO.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + id));

        User user = userRepository.findById(newAddressDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        address.setAddressName(newAddressDTO.getAddress());
        Address updatedAddress = addressRepository.save(address);

        return convertToDTO(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Integer id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + id));
        addressRepository.delete(address);
    }

    private AddressDTO convertToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setAddress(address.getAddressName());
        dto.setUserId(address.getUser().getId());
        return dto;
    }

}

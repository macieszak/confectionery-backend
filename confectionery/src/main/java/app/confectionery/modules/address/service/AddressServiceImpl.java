package app.confectionery.modules.address.service;

import app.confectionery.modules.address.model.Address;
import app.confectionery.modules.address.model.DTO.AddressDTO;
import app.confectionery.modules.address.model.DTO.NewAddressDTO;
import app.confectionery.modules.address.repository.AddressRepository;
import app.confectionery.exception.DuplicateAddressException;
import app.confectionery.exception.UserNotFoundException;
import app.confectionery.modules.user.model.User;
import app.confectionery.modules.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    public Address addNewAddress(UUID userId, NewAddressDTO newAddressDTO) {
        if (newAddressDTO.getAddress() == null || newAddressDTO.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Optional<Address> existingAddress = addressRepository.findByAddressNameAndUserId(newAddressDTO.getAddress(), userId);
        if (existingAddress.isPresent()) {
            throw new DuplicateAddressException("You already have such an address.");
        }

        Address address = new Address(null, newAddressDTO.getAddress(), user);
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public AddressDTO updateAddress(UUID userId, Integer id, NewAddressDTO newAddressDTO) {
        if (newAddressDTO.getAddress() == null || newAddressDTO.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + id));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Address> existingAddresses = addressRepository.findAllByUserIdAndIdNot(userId, id);
        boolean addressExists = existingAddresses.stream()
                .anyMatch(a -> a.getAddressName().equalsIgnoreCase(newAddressDTO.getAddress()));
        if (addressExists) {
            throw new IllegalArgumentException("You already have another address with the same name.");
        }

        address.setAddressName(newAddressDTO.getAddress());
        Address updatedAddress = addressRepository.save(address);

        return convertToDTO(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(UUID userId, Integer id) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

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

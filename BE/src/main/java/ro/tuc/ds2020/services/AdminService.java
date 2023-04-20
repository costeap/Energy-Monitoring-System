package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.AdminDTO;
import ro.tuc.ds2020.dtos.AdminDTOforUpdate;
import ro.tuc.ds2020.dtos.builders.AdminBuilder;
import ro.tuc.ds2020.entities.Admin;
import ro.tuc.ds2020.entities.Client;
import ro.tuc.ds2020.repositories.AdminRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.adminRepository = adminRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public AdminDTO findAdminById(UUID id) {
        Optional<Admin> prosumerOptional = adminRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Admin with id {} was not found in db", id);
            throw new ResourceNotFoundException(Client.class.getSimpleName() + " with id: " + id);
        }
        return AdminBuilder.toAdminDTO(prosumerOptional.get());
    }

    public AdminDTO findAdminByUsername(String username) {
        Optional<Admin> prosumerOptional = adminRepository.findByUsername(username);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Admin with username {} was not found in db", username);
            throw new ResourceNotFoundException(Client.class.getSimpleName() + " with username: " + username);
        }
        return AdminBuilder.toAdminDTO(prosumerOptional.get());
    }

    public UUID insert(AdminDTO adminDTO) {
        Admin admin = AdminBuilder.toEntity(adminDTO);
        String encodePassword = bCryptPasswordEncoder.encode(adminDTO.getPassword());
        admin.setPassword(encodePassword);
        admin = adminRepository.save(admin);
        LOGGER.debug("Admin with id {} was inserted in db", admin.getId());
        return admin.getId();
    }

    public void delete(UUID id) {
        adminRepository.deleteAdminById(id);
        LOGGER.debug("Admin with id {} was deleted from db", id);
    }

    public List<AdminDTO> findAdmins() {
        List<Admin> adminList = adminRepository.findAll();
        return adminList.stream()
                .map(AdminBuilder::toAdminDTO)
                .collect(Collectors.toList());
    }

    public AdminDTOforUpdate updateAdmin(AdminDTOforUpdate adminDTOforUpdate, UUID id) {

        Admin admin = adminRepository.findAdminById(id);
        admin.setName(adminDTOforUpdate.getName());
        admin.setPassword(adminDTOforUpdate.getPassword());

        Admin save = adminRepository.save(admin);
        return AdminBuilder.toAdminDTOforUpdate(admin);
    }
}
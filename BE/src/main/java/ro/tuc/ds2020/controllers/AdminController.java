package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.AdminDTO;
import ro.tuc.ds2020.dtos.AdminDTOforUpdate;
import ro.tuc.ds2020.services.AdminService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping()
    public ResponseEntity<List<AdminDTO>> getAdmins() {
        List<AdminDTO> dtos = adminService.findAdmins();
        for (AdminDTO dto : dtos) {
            Link userLink = linkTo(methodOn(AdminController.class)
                    .getAdmin(dto.getId())).withRel("admin ");
            dto.add(userLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertAdmin(@Valid @RequestBody AdminDTO adminDTO) {
        UUID adminID = adminService.insert(adminDTO);
        return new ResponseEntity<>(adminID, HttpStatus.CREATED);
    }

    @DeleteMapping("/del/{id}")
    @Transactional
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") String id) {
        adminService.delete(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AdminDTO> getAdmin(@PathVariable("id") UUID adminId) {
        AdminDTO dto = adminService.findAdminById(adminId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<AdminDTOforUpdate> updateAdmin(@Valid @RequestBody AdminDTOforUpdate adminDTOforUpdate, @PathVariable UUID id) {
        AdminDTOforUpdate updateAdmin = adminService.updateAdmin(adminDTOforUpdate, id);
        return new ResponseEntity<>(updateAdmin, HttpStatus.OK);
    }

}

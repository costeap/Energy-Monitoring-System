package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.AdminDTO;
import ro.tuc.ds2020.dtos.AdminDTOforUpdate;
import ro.tuc.ds2020.entities.Admin;

public class AdminBuilder {

    private AdminBuilder() {
    }

    public static AdminDTO toAdminDTO(Admin admin) {
        return new AdminDTO(admin.getId(), admin.getName(), admin.getUsername(), admin.getPassword());
    }

    public static AdminDTOforUpdate toAdminDTOforUpdate(Admin admin) {
        return new AdminDTOforUpdate(admin.getName(), admin.getPassword());
    }

    public static Admin toEntity(AdminDTO adminDTO) {
        return new Admin(adminDTO.getId(), adminDTO.getName(), adminDTO.getUsername(), adminDTO.getPassword());
    }
}

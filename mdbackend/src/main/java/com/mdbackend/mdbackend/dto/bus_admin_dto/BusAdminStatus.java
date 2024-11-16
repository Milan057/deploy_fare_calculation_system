package com.mdbackend.mdbackend.dto.bus_admin_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusAdminStatus {
    private Integer total;
    private Integer active;
    private Integer inactive;

    public BusAdminStatus(Integer total, Integer active, Integer inactive) {
        this.total = total;
        this.active = active;
        this.inactive = inactive;
    }

}

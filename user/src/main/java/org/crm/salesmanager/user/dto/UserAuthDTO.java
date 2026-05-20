package org.crm.salesmanager.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.crm.salesmanager.user.entity.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDTO {
    private Long id;
    private String email;
    private String password;
    private Role role;
    private Boolean active;
}
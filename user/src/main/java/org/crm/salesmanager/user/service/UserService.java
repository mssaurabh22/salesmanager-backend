package org.crm.salesmanager.user.service;


import org.crm.salesmanager.user.dto.UserAuthDTO;

public interface UserService {
    UserAuthDTO getUserForAuth(String email);
    UserAuthDTO getUserForAuthById(Long id);
}
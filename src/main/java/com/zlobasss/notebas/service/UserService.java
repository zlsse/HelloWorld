package com.zlobasss.notebas.service;

import com.zlobasss.notebas.dto.RegisterForm;
import com.zlobasss.notebas.dto.RegisterResponse;
import com.zlobasss.notebas.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ResponseEntity create(RegisterForm form);
}

package com.zlobasss.notebas.service;

import com.zlobasss.notebas.dto.RegisterForm;
import com.zlobasss.notebas.dto.RegisterResponse;
import com.zlobasss.notebas.entity.URole;
import com.zlobasss.notebas.entity.User;
import com.zlobasss.notebas.repository.UserRepo;
import com.zlobasss.notebas.security.JwtHelper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private final PasswordEncoder encoder;
    @Autowired
    private final JwtHelper jwtHelper;
    @Autowired
    private final UserDetailsService userDetailsService;

    public static String format(String str, char[] chars) {
        str = str.toLowerCase();
        for (char sym: chars) {
            str = str.replaceAll(String.valueOf(sym), "");
        }
        return str;
    }

    @Override
    public ResponseEntity<?> create(RegisterForm form) {
        RegisterResponse response = new RegisterResponse();
        int lengthLogin = form.getUsername().length();
        int lengthPass = form.getPassword().length();
        char[] symLogin = "qwertyuiopasdfghjklzxcvbnm1234567890_".toCharArray();
        char[] symPassword = "qwertyuiopasdfghjklzxcvbnm1234567890_!@#%&.".toCharArray();

        String login = format(form.getUsername(), symLogin);
        String password = format(form.getPassword(), symPassword);

        if (lengthLogin < 3 || lengthLogin > 32 || !login.isEmpty() || lengthPass < 3 || lengthPass > 32 || !password.isEmpty()) {
            response.setMessage("Incorrect username or password!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userRepo.findByUsername(form.getUsername());
        if (optionalUser.isPresent()) {
            response.setMessage("Account with current username already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        User user = User
                .builder()
                .username(form.getUsername())
                .password(encoder.encode(form.getPassword()))
                .role(URole.U_ROLE)
                .build();
        userRepo.save(user);
        String token = jwtHelper.generateToken(userDetailsService.loadUserByUsername(user.getUsername()));
        response.setMessage("Account created!");
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

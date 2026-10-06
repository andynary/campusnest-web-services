package edu.upc.campusnest.service;

import edu.upc.campusnest.dto.request.LoginRequest;
import edu.upc.campusnest.dto.request.RegisterRequest;
import edu.upc.campusnest.dto.response.AuthResponse;
import edu.upc.campusnest.exception.BusinessRuleException;
import edu.upc.campusnest.model.Role;
import edu.upc.campusnest.model.StudentProfile;
import edu.upc.campusnest.model.User;
import edu.upc.campusnest.repository.StudentProfileRepository;
import edu.upc.campusnest.repository.UserRepository;
import edu.upc.campusnest.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/** US 09: registro con correo universitario + login con JWT. */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${app.institutional-domains}")
    private String domainsCsv;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (req.role() == Role.ADMIN) {
            throw new BusinessRuleException("El rol ADMIN no puede auto-registrarse");
        }
        if (!isInstitutional(req.email())) {
            throw new BusinessRuleException("Debes usar un correo institucional universitario valido");
        }
        if (userRepository.existsByEmail(req.email())) {
            throw new BusinessRuleException("El correo ya esta registrado");
        }
        User user = userRepository.save(User.builder()
                .fullName(req.fullName())
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))   // se guarda encriptada
                .role(req.role())
                .emailVerified(true)    // verificado por dominio institucional
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build());
        profileRepository.save(StudentProfile.builder()             // 2da tabla, como en el laboratorio
                .user(user).university(req.university())
                .cleanlinessLevel(req.cleanlinessLevel()).noiseLevel(req.noiseLevel())
                .sleepSchedule(req.sleepSchedule()).studyRoutine(req.studyRoutine())
                .build());
        return toAuth(user);
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .filter(u -> passwordEncoder.matches(req.password(), u.getPassword()))
                .filter(User::isEnabled)
                .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas"));
        return toAuth(user);
    }

    private boolean isInstitutional(String email) {
        String domain = email.substring(email.lastIndexOf('@') + 1).toLowerCase();
        List<String> allowed = Arrays.stream(domainsCsv.split(",")).map(String::trim).toList();
        return allowed.stream().anyMatch(d -> domain.equals(d) || domain.endsWith("." + d));
    }

    private AuthResponse toAuth(User u) {
        return new AuthResponse(jwtService.generateToken(u.getEmail(), u.getRole().name()),
                u.getId(), u.getFullName(), u.getEmail(), u.getRole().name());
    }
}

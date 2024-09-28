package sn.edu.isepdiamniadio.com.blog.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import sn.edu.isepdiamniadio.com.blog.Repository.UserRepository;
import sn.edu.isepdiamniadio.com.blog.entities.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        logger.info("Tentative de connexion pour l'utilisateur : {}", loginRequest.getUsername());

        User user = userRepository.findByUsername(loginRequest.getUsername());

        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.info("Connexion réussie pour l'utilisateur : {}", loginRequest.getUsername());
            return ResponseEntity.ok("Connexion réussie");
        } else {

            logger.warn("Échec de la connexion pour l'utilisateur : {}", loginRequest.getUsername());
            return ResponseEntity.status(401).body("Échec de la connexion");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User signupRequest) {
        logger.info("Tentative d'inscription pour l'utilisateur : {}", signupRequest.getUsername());

        if (userRepository.findByUsername(signupRequest.getUsername()) != null) {
            logger.warn("Nom d'utilisateur déjà pris : {}", signupRequest.getUsername());
            return ResponseEntity.status(400).body("Nom d'utilisateur déjà pris");
        }

        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        userRepository.save(signupRequest);
        logger.info("Inscription réussie pour l'utilisateur : {}", signupRequest.getUsername());

        return ResponseEntity.ok("Inscription réussie");
    }
}



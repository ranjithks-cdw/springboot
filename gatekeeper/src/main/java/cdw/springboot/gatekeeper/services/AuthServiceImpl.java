package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.configs.JwtServiceImpl;
import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.entities.Roles;
import cdw.springboot.gatekeeper.entities.Token;
import cdw.springboot.gatekeeper.entities.UserInfo;
import cdw.springboot.gatekeeper.entities.Users;
import cdw.springboot.gatekeeper.exceptions.GatekeeperException;
import cdw.springboot.gatekeeper.model.GeneralSuccess;
import cdw.springboot.gatekeeper.model.SigninRequest;
import cdw.springboot.gatekeeper.model.UserRegistrationRequest;
import cdw.springboot.gatekeeper.repositories.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private BlacklistRepository blacklistRepository;
    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private JwtServiceImpl jwtServiceImpl;
    @Autowired
    private TokenRepository tokenRepository;

    public GeneralSuccess registerUser(UserRegistrationRequest userRegistrationRequest) {
        if(userRepository.existsByEmail(userRegistrationRequest.getEmail())) {
            throw new GatekeeperException(AppConstants.ERROR_USER_EXISTS_ALREADY, HttpStatus.BAD_REQUEST);
        }
        // Encode password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userRegistrationRequest.getPassword());

        // Add role
        Roles role = rolesRepository.findByRoleName(userRegistrationRequest.getUserRole());
        if(role == null) {
            throw new GatekeeperException(AppConstants.ERROR_BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }
        List<Roles> rolesList = new ArrayList<>();
        rolesList.add(role);

        Users userRegistration = new Users(userRegistrationRequest.getUserName(), userRegistrationRequest.getEmail(), encodedPassword, rolesList);
        UserInfo userInfo = new UserInfo(userRegistrationRequest.getAge(), userRegistrationRequest.getAddress(), userRegistrationRequest.getGender(), userRegistrationRequest.getMobileNumber(), userRegistration);

        userInfoRepository.save(userInfo);

        GeneralSuccess response = new GeneralSuccess();
        response.success(true);
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setData(AppConstants.SUCCESS_CREATED);

        return response;
    }

    /**
     * @param login
     * @return
     */
    @Override
    public GeneralSuccess userLogin(SigninRequest login) {
        UserInfo user = userInfoRepository.findValidUsersByEmail(login.getEmail()).orElse(null);
        if(user == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        // Create Token
        List<Roles> roles = user.getUser().getRolesList();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.stream().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
        String jwtToken = jwtServiceImpl.generateToken(user.getUser(), authorities);

        Token token = new Token(jwtToken);
        tokenRepository.save(token);

        GeneralSuccess response = new GeneralSuccess();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(jwtToken);

        return response;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GeneralSuccess userLogout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring("Bearer ".length());
        tokenRepository.removeByToken(token);
        GeneralSuccess response = new GeneralSuccess();

        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(AppConstants.SUCCESS_LOGOUT);

        return response;
    }
}

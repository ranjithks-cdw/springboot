package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.configs.JwtServiceImpl;
import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.constants.ErrorResponseConstants;
import cdw.springboot.gatekeeper.constants.SuccessResponseConstants;
import cdw.springboot.gatekeeper.entities.Roles;
import cdw.springboot.gatekeeper.entities.Token;
import cdw.springboot.gatekeeper.entities.UserInfo;
import cdw.springboot.gatekeeper.entities.Users;
import cdw.springboot.gatekeeper.exceptions.GatekeeperException;
import cdw.springboot.gatekeeper.model.*;
import cdw.springboot.gatekeeper.repositories.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtServiceImpl jwtServiceImpl;
    @Autowired
    private TokenRepository tokenRepository;

    public RequestRegistrationSuccess registerUser(UserRequestRegistration userRegistrationRequest) {
        RequestRegistrationSuccess response = null;
        try {
            if(blacklistRepository.existsByEmail(userRegistrationRequest.getEmail()) > 0) {
                throw new GatekeeperException(ErrorResponseConstants.BLACKLISTED_USER, HttpStatus.UNAUTHORIZED);
            }
            if(userRepository.existsByEmail(userRegistrationRequest.getEmail())) {
                throw new GatekeeperException(ErrorResponseConstants.USER_EXISTS_ALREADY);
            }
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(userRegistrationRequest.getPassword());
            Roles role = rolesRepository.findByRoleName(userRegistrationRequest.getUserRole());
            List<Roles> rolesList = new ArrayList<>();
            rolesList.add(role);
            Users userRegistration = new Users(userRegistrationRequest.getUserName(), userRegistrationRequest.getEmail(), encodedPassword, rolesList);
            userRepository.save(userRegistration);
            Optional<Integer> userId = userRepository.findByEmail(userRegistrationRequest.getEmail()).stream().map(Users::getUserId).findFirst();
            UserInfo userInfo;
            if(userId.isPresent()) {
                userInfo = new UserInfo(userRegistrationRequest.getAge(), userRegistrationRequest.getAddress(), userRegistrationRequest.getGender(), userRegistrationRequest.getMobileNumber(), userRegistration);
            }
            else {
                throw new GatekeeperException(ErrorResponseConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            userInfoRepository.save(userInfo);
            response = new RequestRegistrationSuccess();
            response.setMessage(SuccessResponseConstants.SUCCESS_REGISTRATION_REQUEST);
        } catch (TransactionSystemException ex) {
            if (ex.getRootCause() instanceof ConstraintViolationException) {
                throw new ConstraintViolationException(((ConstraintViolationException) ex.getRootCause()).getConstraintViolations());
            }
            throw new GatekeeperException(ex.getMessage());
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage());
        }
        return response;
    }

    public LoginSuccess userLogin(Login login) {
        try {
            UserInfo user = userInfoRepository.findValidUsersByEmail(login.getEmail()).orElse(null);
            if(user == null) {
                throw new GatekeeperException(ErrorResponseConstants.NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));

            List<Roles> roles = user.getUser().getRolesList();
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            roles.stream().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
            String jwtToken = jwtServiceImpl.generateToken(user.getUser(), authorities);
            Token token = new Token(jwtToken);
            tokenRepository.save(token);
            LoginSuccess loginSuccessResponse = new LoginSuccess();
            loginSuccessResponse.setToken(jwtToken);
            return loginSuccessResponse;
        } catch (GatekeeperException e) {
            throw new GatekeeperException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new GatekeeperException(e.getMessage());
        }
    }

    @Transactional
    public LogoutSuccess userLogout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring("Bearer ".length());
        tokenRepository.removeByToken(token);
        LogoutSuccess logoutSuccessResponse = new LogoutSuccess();
        logoutSuccessResponse.setMessage(SuccessResponseConstants.SUCCESS_LOGOUT_RESPONSE);
        return logoutSuccessResponse;
    }
}

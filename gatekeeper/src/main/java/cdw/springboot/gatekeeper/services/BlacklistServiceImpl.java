package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.configs.JwtServiceImpl;
import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.entities.Blacklist;
import cdw.springboot.gatekeeper.entities.Users;
import cdw.springboot.gatekeeper.entities.Visitors;
import cdw.springboot.gatekeeper.exceptions.GatekeeperException;
import cdw.springboot.gatekeeper.model.BlackList;
import cdw.springboot.gatekeeper.model.BlacklistRequest;
import cdw.springboot.gatekeeper.model.GeneralSuccess;
import cdw.springboot.gatekeeper.model.GetBlacklist200Response;
import cdw.springboot.gatekeeper.repositories.BlacklistRepository;
import cdw.springboot.gatekeeper.repositories.UserRepository;
import cdw.springboot.gatekeeper.repositories.VisitorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlacklistServiceImpl implements BlacklistService {
    @Autowired
    BlacklistRepository blacklistRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VisitorsRepository visitorsRepository;

    @Autowired
    JwtServiceImpl jwtService;

    /**
     * @param blacklistRequest
     * @return
     */
    @Override
    public GeneralSuccess blacklistUser(BlacklistRequest blacklistRequest) {
        Visitors visitor = visitorsRepository.findById(blacklistRequest.getUserId()).orElse(null);
        if(visitor == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if(blacklistRepository.existsByVisitor(visitor)) {
            throw new GatekeeperException(AppConstants.ERROR_USER_EXISTS_ALREADY, HttpStatus.BAD_REQUEST);
        }

        String blacklisterEmail = jwtService.getUserFromJwt();
        Users blacklistedBy = userRepository.findByEmail(blacklisterEmail).orElse(null);

        if(blacklistedBy == null) {
            throw new GatekeeperException(AppConstants.ERROR_NOT_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        Blacklist blacklist = new Blacklist();
        blacklist.setVisitor(visitor);
        blacklist.setBlacklistedBy(blacklistedBy);

        blacklistRepository.save(blacklist);

        GeneralSuccess response = new GeneralSuccess();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setData(AppConstants.SUCCESS_BLACKLIST);

        return response;
    }

    /**
     * @return
     */
    @Override
    public GetBlacklist200Response getBlacklist() {
        List<Blacklist> blacklist = blacklistRepository.findAll();

        List<BlackList> data = blacklist.stream()
                .map(user -> {
                    BlackList userDetail = new BlackList();
                    userDetail.setUserId(user.getVisitor().getVisitorId());
                    userDetail.setUserName(user.getVisitor().getVisitorName());
                    userDetail.setEmail(user.getVisitor().getEmail());
                    userDetail.setBlacklistedBy(user.getBlacklistedBy().getEmail());

                    return userDetail;
                })
                .collect(Collectors.toList());

        GetBlacklist200Response response = new GetBlacklist200Response();
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(data);

        return response;
    }
}

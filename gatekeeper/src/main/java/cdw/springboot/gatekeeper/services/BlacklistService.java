package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.model.BlacklistRequest;
import cdw.springboot.gatekeeper.model.GeneralSuccess;
import cdw.springboot.gatekeeper.model.GetBlacklist200Response;

public interface BlacklistService {
    public GeneralSuccess blacklistUser(BlacklistRequest blacklistRequest);
    public GetBlacklist200Response getBlacklist();
}

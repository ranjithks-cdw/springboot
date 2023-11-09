package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.model.*;

public interface ResidentService {
    public ScheduleResponseSuccess createVisitRequest(VisitRequest visitRequest);
    public UpdateSuccess modifyVisitRequest(Integer requestId, VisitRequest updateVisitRequest);
    public DeleteSuccess removeVisitRequest(Integer requestId);
}

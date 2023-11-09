package cdw.springboot.gatekeeper.services;

import cdw.springboot.gatekeeper.model.ScheduleResponse;

import java.time.LocalDate;
import java.util.List;

public interface VisitorService {
    public List<ScheduleResponse> getPasskey(LocalDate date, String email);
}

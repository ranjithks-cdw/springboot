package cdw.springboot.gatekeeper.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "visit_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitRequests {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.MERGE})
    @JoinColumn(name = "requested_by", referencedColumnName = "user_id")
    private Users requestedBy;

    @ManyToOne
    @JoinColumn(name = "approved_by", referencedColumnName = "user_id")
    private Users approvedBy;

    @Column(name = "requested_date")
    @FutureOrPresent(message = "Enter valid date")
    private LocalDate date;

    @Column(name = "is_approved")
    private String isApproved;

    @Column(name = "passkey")
    @Pattern(regexp = "^\\d{6}$", message = "Invalid Passkey")
    private String passkey;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "visitorId")
    private Visitors visitor;

    @Column(name = "entry_time")
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;
}

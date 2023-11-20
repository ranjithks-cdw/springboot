package cdw.springboot.gatekeeper.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blacklist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blacklist_id")
    private int blackListId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "visitor_id", referencedColumnName = "visitor_id")
    private Visitors visitor;

    @ManyToOne
    @JoinColumn(name = "blacklisted_by", referencedColumnName = "user_id")
    private Users blacklistedBy;
}

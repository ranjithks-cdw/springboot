package cdw.springboot.gatekeeper.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "visitors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Visitors {
    @Id
    @Column(name = "visitor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int visitorId;

    @Column(name = "name")
    @NotNull(message = "Name cannot be blank")
    private String visitorName;

    @Column(name = "age")
    @NotNull(message = "Age is required")
    @Range(min = 18, max = 100, message = "Age must be greater than 18 and less than 100")
    private int age;

    @Column(name = "address")
    @NotNull(message = "Address is required")
    private String address;

    @Column(name = "gender")
    @NotNull(message = "Gender is required")
    @Pattern(regexp = "male|female|others", message = "Enter valid gender('male', 'female', 'others')")
    private String gender;

    @Column(name = "mobile_number")
    @NotNull(message = "Mobile number is required")
    @Digits(integer = 10, fraction = 0, message = "Mobile number must be a 10-digit number")
    private Long mobileNumber;

    @Column(name = "email", unique = true)
    @NotNull(message = "Email is required")
    private String email;

    @OneToMany(mappedBy = "visitor", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<VisitRequests> requestsList = new ArrayList<>();
}

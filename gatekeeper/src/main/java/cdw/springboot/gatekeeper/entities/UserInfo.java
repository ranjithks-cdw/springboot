package cdw.springboot.gatekeeper.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name="user_info")
public class UserInfo {
    @Id
    @Column(name = "user_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userInfoId;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users user;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "approved_by", referencedColumnName = "user_id")
    private Users approvedBy;

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


    public UserInfo(int age, String address, String gender, Long mobileNumber, Users user) {
        this.age = age;
        this.address = address;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.user = user;
    }
}

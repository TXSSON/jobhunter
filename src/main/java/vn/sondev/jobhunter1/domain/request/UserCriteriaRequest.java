package vn.sondev.jobhunter1.domain.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import vn.sondev.jobhunter1.util.enums.GenderEnum;
@Setter
@Getter
public class UserCriteriaRequest {
    String name;
    String email;
    Integer age;
    @Enumerated(EnumType.STRING)
    GenderEnum gender;
    String address;
}

package vn.sondev.jobhunter1.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.sondev.jobhunter1.util.enums.GenderEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String name;
    String email;
    String password;
    Integer age;
    @Enumerated(EnumType.STRING)
    GenderEnum gender;
    String address;
}

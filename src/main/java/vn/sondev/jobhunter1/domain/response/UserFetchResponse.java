package vn.sondev.jobhunter1.domain.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.sondev.jobhunter1.util.enums.GenderEnum;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFetchResponse {
    long id;
    String name;
    String email;
    Integer age;
    @Enumerated(EnumType.STRING)
    GenderEnum gender;
    String address;
    Instant createAt;
    Instant updateAt;
}

package vn.sondev.jobhunter1.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
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
public class UserCreationResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    String email;
    Integer age;
    @Enumerated(EnumType.STRING)
    GenderEnum gender;
    String address;
    Instant createdAt;
}

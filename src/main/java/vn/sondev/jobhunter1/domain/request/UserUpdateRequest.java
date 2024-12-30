package vn.sondev.jobhunter1.domain.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.sondev.jobhunter1.util.enums.GenderEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    Integer age;
    @Enumerated(EnumType.STRING)
    GenderEnum gender;
    String address;
}

package vn.sondev.jobhunter1.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.sondev.jobhunter1.domain.request.UserCreationRequest;
import vn.sondev.jobhunter1.util.SecurityUtil;
import vn.sondev.jobhunter1.util.enums.GenderEnum;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    String email;
    String password;
    Integer age;
    @Enumerated(EnumType.STRING)
    GenderEnum gender;
    @Column(columnDefinition = "MEDIUMTEXT")
    String address;
    String refreshToken;
    Instant createAt;
    Instant updateAt;
    String createdBy;
    String updatedBy;

    @PrePersist
    void prePersist() {
        createAt = Instant.now();
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "anonymous";
    }
    @PreUpdate
    void preUpdate() {
        updateAt = Instant.now();
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "anonymous";
    }
}


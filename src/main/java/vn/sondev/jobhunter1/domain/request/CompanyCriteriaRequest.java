package vn.sondev.jobhunter1.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyCriteriaRequest {
    String name;
    List<String> address;
}

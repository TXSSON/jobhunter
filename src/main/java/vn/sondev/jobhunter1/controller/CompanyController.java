package vn.sondev.jobhunter1.controller;

import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import vn.sondev.jobhunter1.domain.Company;
import vn.sondev.jobhunter1.domain.request.CompanyCriteriaRequest;
import vn.sondev.jobhunter1.service.CompanyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/companies")
public class CompanyController {

    CompanyService companyService;

    @GetMapping
    public List<Company> getCompanies(@ModelAttribute CompanyCriteriaRequest companyCriteriaRequest, Pageable pageable) {
        return this.companyService.fetchAllCompany(pageable, companyCriteriaRequest);
    }
    @PostMapping
    public Company createCompany(@RequestBody Company company) {
        return this.companyService.handleCreateCompany(company);
    }
    @PutMapping
    public Company updateCompany(@RequestBody Company company) {
        return this.companyService.handleUpdateCompany(company);
    }
    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable Long id) {
        this.companyService.handleDeleteCompany(id);
    }
}

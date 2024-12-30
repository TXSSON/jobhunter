package vn.sondev.jobhunter1.service;

import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.sondev.jobhunter1.domain.Company;
import vn.sondev.jobhunter1.domain.request.CompanyCriteriaRequest;
import vn.sondev.jobhunter1.repository.CompanyRepository;
import vn.sondev.jobhunter1.service.specification.SpecificationBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyService {

    CompanyRepository companyRepository;
    SpecificationBuilder<Company> specificationBuilder;

    public Company handleCreateCompany(Company company) {
        return companyRepository.save(company);
    }
    public List<Company> fetchAllCompany(Pageable pageable, CompanyCriteriaRequest companyCriteriaRequest) {
        if (companyCriteriaRequest == null) {
            // Trả về tất cả công ty nếu không có tiêu chí lọc
            return companyRepository.findAll(pageable).getContent();
        }

        Specification<Company> combinedSpec = null;

        // Thêm điều kiện lọc tên
        if (companyCriteriaRequest.getName() != null) {
            Specification<Company> nameSpec = specificationBuilder.whereNameLike("name", companyCriteriaRequest.getName());
            combinedSpec = Specification.where(combinedSpec).and(nameSpec);
        }

        // Thêm điều kiện lọc địa chỉ
        if (companyCriteriaRequest.getAddress() != null) {
            Specification<Company> addressSpec = specificationBuilder.whereNameIn("address", companyCriteriaRequest.getAddress());
            combinedSpec = Specification.where(combinedSpec).and(addressSpec);
        }

        // Thực hiện truy vấn với điều kiện và phân trang
        return companyRepository.findAll(combinedSpec, pageable).getContent();
    }


    public Company handleUpdateCompany(Company company) {

        Company otherCompany = this.companyRepository.findById(company.getId());
        if (otherCompany != null) {
            otherCompany.setAddress(company.getAddress());
            otherCompany.setLogo(company.getLogo());
            otherCompany.setName(company.getName());
            otherCompany.setDescription(company.getDescription());
        }
        return companyRepository.save(otherCompany);
    }
    public void handleDeleteCompany(Long id) {
        Company company = new Company();
        company.setId(id);
        companyRepository.delete(company);
    }



}

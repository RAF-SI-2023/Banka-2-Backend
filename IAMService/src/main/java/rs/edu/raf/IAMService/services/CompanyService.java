package rs.edu.raf.IAMService.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.CompanyDto;

@Service
public interface CompanyService {
    CompanyDto getCompanyById(Long id);
}

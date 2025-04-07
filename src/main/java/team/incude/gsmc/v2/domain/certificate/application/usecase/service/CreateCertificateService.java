package team.incude.gsmc.v2.domain.certificate.application.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.incude.gsmc.v2.domain.certificate.application.usecase.CreateCertificateUseCase;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateCertificateService implements CreateCertificateUseCase {

    @Override
    public void execute(String name, LocalDate acquisitionDate, MultipartFile file) {

    }
}
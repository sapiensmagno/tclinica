package br.com.tclinica.service.impl;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tclinica.domain.Exam;
import br.com.tclinica.domain.ExamStatus;
import br.com.tclinica.domain.enumeration.ExamStatuses;
import br.com.tclinica.repository.ExamStatusRepository;
import br.com.tclinica.service.ExamStatusService;

@Service
@Transactional
public class ExamStatusServiceImpl implements ExamStatusService{

    private final Logger log = LoggerFactory.getLogger(ExamStatusServiceImpl.class);

    private final ExamStatusRepository examStatusRepository;
    
    public ExamStatusServiceImpl(ExamStatusRepository examStatusRepository) {
        this.examStatusRepository = examStatusRepository;
    }

	@Override
	public ExamStatus create(Exam exam, ExamStatuses examStatus) {
		ExamStatus examStatusObj = new ExamStatus();
    	examStatusObj.setCreationDate(ZonedDateTime.now());
    	examStatusObj.setExam(exam);
    	examStatusObj.setName(examStatus.toString());
    	examStatusRepository.save(examStatusObj);
    	return examStatusObj;
	}

}

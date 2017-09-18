package br.com.tclinica.service;

import br.com.tclinica.domain.Exam;
import br.com.tclinica.domain.ExamStatus;
import br.com.tclinica.domain.enumeration.ExamStatuses;

public interface ExamStatusService {


	ExamStatus create(Exam exam, ExamStatuses examStatus);

//    List<ExamStatus> findAll();
//
//    ExamStatus findOne(Long id);
//
//    void delete(Long id);

}

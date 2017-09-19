package br.com.tclinica.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.tclinica.domain.enumeration.ExamStatuses;

@RunWith(SpringRunner.class)
public class ExamTest  {
	
	private static final ZonedDateTime date1 = ZonedDateTime.now().minusDays(1L);
	private static final ZonedDateTime date2 = ZonedDateTime.now().minusDays(2L);
	private static final ZonedDateTime date3 = ZonedDateTime.now().minusDays(3L);
	
	@Test
	public void testGetLastExamStatus_manyStatuses() throws Exception {
		ExamStatus status1 = new ExamStatus();
		status1.setCreationDate(date1);
		status1.setId(3L);
		status1.setName(ExamStatuses.ARCHIVE.toString());
		
		ExamStatus status2 = new ExamStatus();
		status2.setCreationDate(date2);
		status2.setId(2L);
		status2.setName(ExamStatuses.ARCHIVE.toString());
		
		ExamStatus status3 = new ExamStatus();
		status3.setCreationDate(date3);
		status3.setId(1L);
		status3.setName(ExamStatuses.ARCHIVE.toString());
		
		List<ExamStatus> statuses = new ArrayList<>();
		statuses.add(status3);
		statuses.add(status2);
		statuses.add(status1);		
		
		Exam exam = new Exam();
		exam.setExamStatuses(statuses);
		
		ExamStatus result = exam.getCurrentExamStatus();
		
		assertThat(result).isEqualTo(status1);
		assertThat(result).isNotEqualTo(status3);
	}
	
	@Test
	public void testGetLastExamStatus_noStatus() throws Exception {
		List<ExamStatus> statuses = new ArrayList<>();
		Exam exam = new Exam();
		exam.setExamStatuses(statuses);
		
		ExamStatus result = exam.getCurrentExamStatus();
		
		assertThat(result).isEqualTo(null);
	}
}























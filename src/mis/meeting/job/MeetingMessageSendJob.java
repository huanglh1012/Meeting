package mis.meeting.job;

import mis.meeting.service.MeetingService;
import mis.shortmessage.dto.ShortMessageSendDTO;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Service;

import ecp.bsp.system.framework.context.ContextBeanFactory;

@Service
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MeetingMessageSendJob implements Job{
	
	private MeetingService meetingService ;
	
	public MeetingService getMeetingService() {
		if (this.meetingService == null) {
			this.meetingService = (MeetingService) ContextBeanFactory.getBeanByClass(MeetingService.class);
		}
		
		return meetingService;
	}

	@Override
	public void execute(JobExecutionContext context) {
		ShortMessageSendDTO tmpShortMessageSendDTO = (ShortMessageSendDTO) context.getJobDetail().getJobDataMap().get("shortMessageSendDTO");
		try {
			this.getMeetingService().sendMeetingMessage(tmpShortMessageSendDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
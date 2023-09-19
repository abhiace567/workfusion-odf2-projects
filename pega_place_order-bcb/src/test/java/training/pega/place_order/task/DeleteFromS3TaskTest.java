package training.pega.place_order.task;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.workfusion.odf.test.junit.WorkerJUnitConfig;
import com.workfusion.odf2.junit.BotTaskFactory;

@WorkerJUnitConfig
class DeleteFromS3TaskTest {

	@Test
	@DisplayName("should delete file from s3")
	void shouldDeleteFileFromS3(BotTaskFactory botTaskFactory) {
		Map<String, String> firstRecord = botTaskFactory.fromClass(DeleteFromS3Task.class).withS3(
				cfg -> cfg.withEndpointUrl("http://localhost:15110").withAccessKey("admin").withSecretKey("admin123"))
				.buildAndRun().getFirstRecord();
		
		assertThat(firstRecord.get("status").equals("success"));
	}

}

package training.pega.place_order.task;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.SingleResult;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;
import com.workfusion.odf2.service.s3.S3Bucket;
import com.workfusion.odf2.service.s3.S3Module;
import com.workfusion.odf2.service.s3.S3Service;

@BotTask
@Requires(S3Module.class)
public class DeleteFromS3Task implements GenericTask {
	
	private final S3Service s3Service;
	private final Logger log;
	
	@Inject
	public DeleteFromS3Task(S3Service s3Service, Logger log) {
		this.s3Service = s3Service;
		this.log = log;
	}

	@Override
	public TaskRunnerOutput run() {
		S3Bucket bucket = s3Service.getBucket("public");
		String fileName = "laptop invoice.pdf";
		bucket.delete(fileName);
		log.info("Deleted file - {}", fileName);
		return new SingleResult().withColumn("status", "success");
	}

}

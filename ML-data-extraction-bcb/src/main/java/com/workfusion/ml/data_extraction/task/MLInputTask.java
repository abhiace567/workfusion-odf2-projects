package com.workfusion.ml.data_extraction.task;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.settings.Configuration;
import com.workfusion.odf2.core.task.TaskInput;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.SingleResult;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;

@BotTask(fileName = "data-extraction-ml-input.xml")
public class MLInputTask implements GenericTask {

	private final Logger logger;
	private final TaskInput taskInput;
	private final Configuration configuration;

	@Inject
	public MLInputTask(Logger logger, TaskInput taskInput, Configuration configuration) {
		this.logger = logger;
		this.taskInput = taskInput;
		this.configuration = configuration;
	}

	@Override
	public TaskRunnerOutput run() {
		SingleResult result = new SingleResult();
		
		String document = taskInput.getRequiredVariable("message");
        String modelId = configuration.getRequiredProperty("model_id");
        result.setColumn("model_id", modelId);
        result.setColumn("document", document);
		
        return result;
	}

}

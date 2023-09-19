package com.workfusion.ml.data_extraction.task;

import java.util.Iterator;

import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workfusion.ml.data_extraction.model.ExtractionModelResult;
import com.workfusion.ml.data_extraction.module.RepositoryModule;
import com.workfusion.ml.data_extraction.repository.ExtractionModelResultsRepository;
import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.task.TaskInput;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.SingleResult;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;

@BotTask(fileName = "data-extraction-ml-Output.xml")
@Requires(RepositoryModule.class)
public class MLOutputTask implements GenericTask {

	private final Logger logger;
	private final TaskInput taskInput;
	private final ExtractionModelResultsRepository extractionModelResultsRepository;

	@Inject
	public MLOutputTask(Logger logger, TaskInput taskInput,
			ExtractionModelResultsRepository extractionModelResultsRepository) {
		this.logger = logger;
		this.taskInput = taskInput;
		this.extractionModelResultsRepository = extractionModelResultsRepository;
	}

	@Override
	public TaskRunnerOutput run() {
		String modelResultJson = taskInput.getRequiredVariable("model_result");
		String extractedValue = org.apache.commons.lang.StringUtils.EMPTY;

		JsonObject jsonObject = new Gson().fromJson(modelResultJson, JsonObject.class);

		Iterator<JsonElement> iterator = jsonObject.get("tags").getAsJsonArray().iterator();
		while (iterator.hasNext()) {
			JsonObject jsonObj = iterator.next().getAsJsonObject();
			if (jsonObj.get("tag").getAsString().equalsIgnoreCase("invoice_amount")) {
				extractedValue = jsonObj.get("text").getAsString();
				break;
			}
		}

		Document jsoup = Jsoup.parse(taskInput.getRequiredVariable("document"));
		String goldValue = jsoup.select("invoice_amount").attr("data-value");

		ExtractionModelResult extractionModelResult = new ExtractionModelResult();

		extractionModelResult.setExtractedValue(extractedValue);
		extractionModelResult.setGoldValue(goldValue);

		extractionModelResultsRepository.create(extractionModelResult);

		return new SingleResult().withColumn("extractedValue", extractedValue).withColumn("goldValue", goldValue);

	}

}

package com.workfusion.ml.data_extraction.task;

import java.util.List;

import javax.inject.Inject;

import com.workfusion.ml.data_extraction.model.ExtractionModelResult;
import com.workfusion.ml.data_extraction.module.RepositoryModule;
import com.workfusion.ml.data_extraction.repository.ExtractionModelResultsRepository;
import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.SingleResult;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;

@BotTask
@Requires(RepositoryModule.class)
public class CaculatePerformanceTask implements GenericTask {

	private final ExtractionModelResultsRepository extractionModelResultsRepository;

	@Inject
	public CaculatePerformanceTask(ExtractionModelResultsRepository extractionModelResultsRepository) {
		this.extractionModelResultsRepository = extractionModelResultsRepository;
	}

	@Override
	public TaskRunnerOutput run() {
		List<ExtractionModelResult> values = extractionModelResultsRepository.findAll();
		int truePositive = 0;
		int trueNegative = 0;
		int falsePositive = 0;
		int falseNegavite = 0;

		for (ExtractionModelResult item : values) {
			String goldValue = item.getGoldValue();
			String extractedValue = item.getExtractedValue().replace("$", "");

			if (goldValue.equalsIgnoreCase(extractedValue))
				truePositive++;
			else
				falsePositive++;
		}

		float precision = (float) truePositive / (truePositive + falsePositive);
		float recall = (float) truePositive / (truePositive + falseNegavite);

		SingleResult result = new SingleResult();
		result.withColumn("precision", "" + precision);
		result.withColumn("recall", "" + recall);
		result.withColumn("truePositive", "" + truePositive);
		result.withColumn("trueNegative", "" + trueNegative);
		result.withColumn("falsePositive", "" + falsePositive);
		result.withColumn("falseNegavite", "" + falseNegavite);
		
		return result;
	}

}

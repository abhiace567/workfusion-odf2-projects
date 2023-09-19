package com.ebay.products.common;

import com.workfusion.odf2.core.task.OdfTaskRunner;
import com.workfusion.odf2.core.task.output.MultipleResults;
import com.workfusion.odf2.core.task.output.SingleResult;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;

public class GenericTaskMultipleResultsRunner implements OdfTaskRunner<GenericTaskMultipleResults> {

	@Override
	public TaskRunnerOutput run(GenericTaskMultipleResults task) {
		MultipleResults results = new MultipleResults();
		task.run().forEach(row -> results.addRow(new SingleResult(row)));
		return results;
	}
}

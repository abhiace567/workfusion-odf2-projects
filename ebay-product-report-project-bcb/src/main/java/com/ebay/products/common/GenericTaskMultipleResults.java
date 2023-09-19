package com.ebay.products.common;

import java.util.List;
import java.util.Map;

import com.workfusion.odf2.core.task.OdfTask;
import com.workfusion.odf2.core.task.OdfTaskRunner;

public interface GenericTaskMultipleResults extends OdfTask {
	
	@Override
	default Class<? extends OdfTaskRunner<?>> getRunnerClass() {
		return GenericTaskMultipleResultsRunner.class;
	}
	
	List<Map<String, String>> run();

}

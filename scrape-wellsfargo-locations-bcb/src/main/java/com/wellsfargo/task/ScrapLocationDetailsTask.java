package com.wellsfargo.task;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.wellsfargo.automation.LocationScrapperRobot;
import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.MultipleResults;
import com.workfusion.odf2.core.task.output.SingleResult;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;
import com.workfusion.odf2.core.webharvest.rpa.RpaDriver;
import com.workfusion.odf2.core.webharvest.rpa.RpaFactory;
import com.workfusion.odf2.core.webharvest.rpa.RpaRunner;

@BotTask(requireRpa = true)
public class ScrapLocationDetailsTask implements GenericTask {

	private RpaRunner rpaRunner;
	private Logger logger;
//	private final TaskInput taskInput;

	@Inject
	public ScrapLocationDetailsTask(RpaFactory rpaFactory, Logger logger) {
		rpaRunner = rpaFactory.builder(RpaDriver.UNIVERSAL).maximizeOnStartup(true).startInPrivate(true)
				.closeOnCompletion(true).build();
		this.logger = logger;
//		this.taskInput = taskInput;
	}

	@Override
	public TaskRunnerOutput run() {
		MultipleResults results = new MultipleResults();
		rpaRunner.execute(driver -> {
			LocationScrapperRobot locationScrapperRobot = new LocationScrapperRobot(driver, logger);
			List<Map<String, String>> atmLocations = locationScrapperRobot.getAtmLocations();
			String dummyLoc = atmLocations.get(0).get("Address");
			atmLocations.forEach(location -> results.addRow(
					new SingleResult(location).withColumn("sendmail", "" + dummyLoc.equals(location.get("Address")))));
		});

		return results;
	}

}

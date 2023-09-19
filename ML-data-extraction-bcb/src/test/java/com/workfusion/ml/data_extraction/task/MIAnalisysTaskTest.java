package com.workfusion.ml.data_extraction.task;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.workfusion.odf.test.junit.IacDeveloperJUnitConfig;
import com.workfusion.odf2.junit.BotTaskFactory;

@IacDeveloperJUnitConfig
class MIAnalisysTaskTest {

	@Test
	void test(BotTaskFactory botTaskFactory) {
		botTaskFactory.fromClass(MIAnalisysTask.class).buildAndRun();
	}

}

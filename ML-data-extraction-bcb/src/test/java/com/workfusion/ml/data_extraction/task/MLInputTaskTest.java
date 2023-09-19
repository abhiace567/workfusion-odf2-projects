package com.workfusion.ml.data_extraction.task;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.workfusion.odf.test.junit.WorkerJUnitConfig;
import com.workfusion.odf.test.launch.InputData;
import com.workfusion.odf.test.launch.OutputData;
import com.workfusion.odf2.core.settings.ConfigEntity;
import com.workfusion.odf2.junit.BotTaskFactory;
import com.workfusion.odf2.junit.OrmSupport;

@WorkerJUnitConfig
class MLInputTaskTest {

	@BeforeEach
	void setUp(OrmSupport ormSupport) {
		ormSupport.createTables(ConfigEntity.class);
		
		Collection<ConfigEntity> cfg =  new ArrayList<>();
		cfg.add(new ConfigEntity("model_id","message_bde08f43-0507-4259-931e-e53764b07f51"));
		
		ormSupport.getConfigRepository().createAll(cfg);
	}
	@Test
	void test(BotTaskFactory botTaskFactory) {
		OutputData outputData = botTaskFactory.fromClass(MLInputTask.class)
				.withInputData(InputData.of("message", "Input Msg")).withTimeout(Duration.ofSeconds(120)).buildAndRun();
		
		System.out.println(outputData.getFirstRecord());
	}

}

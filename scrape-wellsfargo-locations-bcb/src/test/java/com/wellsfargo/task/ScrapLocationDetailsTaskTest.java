package com.wellsfargo.task;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.workfusion.odf.test.junit.IacDeveloperJUnitConfig;
import com.workfusion.odf2.junit.BotTaskFactory;

@IacDeveloperJUnitConfig
class ScrapLocationDetailsTaskTest {

	@Test
	@DisplayName("Should Exctract Ten Location Details")
	void shouldExctractTenLocationDetails(BotTaskFactory botTaskFactory) {
		List<Map<String,String>> records = botTaskFactory.fromClass(ScrapLocationDetailsTask.class).buildAndRun().getRecords();
		
		assertThat(records.size()).isEqualTo(10);
	}

}

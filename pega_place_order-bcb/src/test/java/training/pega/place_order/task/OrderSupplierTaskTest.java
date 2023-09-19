package training.pega.place_order.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.workfusion.odf.test.junit.WorkerJUnitConfig;
import com.workfusion.odf2.junit.BotTaskFactory;
import com.workfusion.odf2.junit.OrmSupport;
import com.workfusion.odf2.transaction.model.Transaction;

import training.pega.place_order.model.Order;

@WorkerJUnitConfig
class OrderSupplierTaskTest {

	@BeforeEach
	void setUp(OrmSupport ormSupport) {
		ormSupport.createTables(Order.class, Transaction.class);
	}

	@Test
	@DisplayName("should get orders from excel file")
	void shouldGetOrdersFromExcelFile(BotTaskFactory botTaskFactory, OrmSupport ormSupport) {
		List<Map<String, String>> outputRecords = botTaskFactory.fromClass(OrderSupplierTask.class).withS3(
				cfg -> cfg.withEndpointUrl("http://localhost:15110").withAccessKey("admin").withSecretKey("admin123"))
				.buildAndRun().getRecords();
		System.out.println(outputRecords.toString());
		ormSupport.getRepository(Order.class).findAll().forEach(System.out::println);
		ormSupport.getRepository(Transaction.class).findAll().forEach(System.out::println);
		assertAll("Assert database state after generetor task execution", () -> {
			assertThat(ormSupport.getRepository(Order.class).count()).isEqualTo(7);
		});
	}

}

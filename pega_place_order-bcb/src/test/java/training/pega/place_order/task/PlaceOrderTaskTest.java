package training.pega.place_order.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.workfusion.odf.test.junit.IacDeveloperJUnitConfig;
import com.workfusion.odf.test.launch.InputData;
import com.workfusion.odf.test.launch.OutputData;
import com.workfusion.odf2.core.orm.OrmLiteRepository;
import com.workfusion.odf2.core.settings.ConfigEntity;
import com.workfusion.odf2.core.webharvest.TaskVariable;
import com.workfusion.odf2.junit.BotTaskFactory;
import com.workfusion.odf2.junit.OrmSupport;
import com.workfusion.odf2.transaction.model.Transaction;
import com.workfusion.odf2.transaction.repository.TransactionRepository;

import training.pega.place_order.model.Order;

@IacDeveloperJUnitConfig
class PlaceOrderTaskTest {

	private TransactionRepository transactionRepository;
	private OrmLiteRepository<Order> orderRepository;

	@BeforeEach
	void setUp(OrmSupport ormSupport) {
		transactionRepository = ormSupport.getTransactionRepository();
		orderRepository = ormSupport.getRepository(Order.class);

		ormSupport.createTables(Transaction.class, Order.class, ConfigEntity.class);
		
		Collection<ConfigEntity> cfg =  new ArrayList<>();
		cfg.add(new ConfigEntity("HTTP_OPENSPAN_LOGIN", "https://training.openspan.com/login"));
		
		ormSupport.getConfigRepository().createAll(cfg);
		
	}

	@Test
	@DisplayName("should place order")
	void shouldPlaceOrder(BotTaskFactory botTaskFactory) {

		OutputData outputData = botTaskFactory.fromClass(PlaceOrderTask.class)
				.withSecureEntries(cfg -> cfg.withEntry("pega_alias", "12345", "12345"))
				.withInputData(createTransaction()).withTimeout(Duration.ofSeconds(120)).buildAndRun();
		Order order = orderRepository.findAll().get(0);
		System.out.println("****" + order);
		assertAll("Transaction status and order id after processing", () -> {
			assertThat(order.getStatus()).isEqualTo("Ordered");
			assertThat(order.getOrderId()).isNotNull();
//			assertThat(order.getTransaction().getStatus()).isEqualTo("COMPLETED");
		});
		
		System.out.println("********" + outputData.getRecords());
	}

	private InputData createTransaction() {
		Transaction transaction1 = new Transaction();
		transaction1.setUuid(UUID.randomUUID());
		transaction1.setStatus("NEW");
		Order order = new Order();
		order.setItem("Sirop d'erable");
		order.setType("Seasonings");
		order.setQuantity("2");
		order.setStatus("NEW");
		order.setTransaction(transaction1);

		transactionRepository.create(transaction1);
		orderRepository.create(order);

		return InputData.of(TaskVariable.TRANSACTION_ID.toString(), transaction1.getUuid().toString());
	}

}


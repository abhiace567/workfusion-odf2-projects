package training.pega.place_order.task;

import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.settings.Configuration;
import com.workfusion.odf2.core.task.TaskInput;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.SingleResult;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;
import com.workfusion.odf2.core.webharvest.rpa.RpaDriver;
import com.workfusion.odf2.core.webharvest.rpa.RpaFactory;
import com.workfusion.odf2.core.webharvest.rpa.RpaRunner;
import com.workfusion.odf2.service.ControlTowerServicesModule;
import com.workfusion.odf2.service.vault.SecretsVaultService;
import com.workfusion.odf2.transaction.TransactionModule;
import com.workfusion.odf2.transaction.repository.TransactionRepository;

import training.pega.place_order.automation.PegaRobot;
import training.pega.place_order.model.Order;
import training.pega.place_order.module.RepositoryModule;
import training.pega.place_order.repository.OrderRepository;

@BotTask(requireRpa = true)
@Requires({ RepositoryModule.class, TransactionModule.class, ControlTowerServicesModule.class})
public class PlaceOrderTask implements GenericTask {
	
	private final RpaRunner rpaRunner;
	private final SecretsVaultService secretsVault;
	private final OrderRepository orderRepository;
	private final TransactionRepository transactionRepository;
	private final Configuration configuration;
	private final Logger logger;
	private final TaskInput taskInput;

	@Inject
	public PlaceOrderTask(RpaFactory rpaFactory, SecretsVaultService secretsVault, Configuration configuration, OrderRepository orderRepository,
			TransactionRepository transactionRepository, Logger logger, TaskInput taskInput) {
		this.secretsVault = secretsVault;
		this.configuration = configuration;
		this.orderRepository = orderRepository;
		this.transactionRepository = transactionRepository;
		this.logger = logger;
		this.taskInput = taskInput;

		rpaRunner = rpaFactory.builder(RpaDriver.UNIVERSAL).closeOnCompletion(true).maximizeOnStartup(true)
				.startInPrivate(true).build();
	}

	@Override
	public TaskRunnerOutput run() {
		String transactionId = taskInput.getRequiredVariable("_sys_transaction_id");
		rpaRunner.execute(driver -> {
			Order order = orderRepository.findFirstByTransactionId(UUID.fromString(transactionId));
			logger.info("Strated processing order - {}", order);;
			final PegaRobot pegaRobot = new PegaRobot(driver, secretsVault, configuration, logger);
			order = pegaRobot.placeOrder(order);
			
			transactionRepository.update(order.getTransaction());
			orderRepository.update(order);
		});
		return new SingleResult().withColumn("_sys_transaction_id", transactionId).withColumn("status", "Processed");
	}

}

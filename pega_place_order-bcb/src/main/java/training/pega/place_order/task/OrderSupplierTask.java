package training.pega.place_order.task;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.MultipleResults;
import com.workfusion.odf2.core.task.output.SingleResult;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;
import com.workfusion.odf2.service.s3.S3Bucket;
import com.workfusion.odf2.service.s3.S3Module;
import com.workfusion.odf2.service.s3.S3Service;
import com.workfusion.odf2.transaction.TransactionModule;
import com.workfusion.odf2.transaction.model.Transaction;
import com.workfusion.odf2.transaction.repository.TransactionRepository;

import training.pega.place_order.model.Order;
import training.pega.place_order.module.RepositoryModule;
import training.pega.place_order.repository.OrderRepository;
import training.pega.place_order.utils.ExcelPerser;

@BotTask(fileName = "order-items-supplier.xml")
@Requires({ RepositoryModule.class, TransactionModule.class, S3Module.class })
public class OrderSupplierTask implements GenericTask {

	private final Logger logger;
	private final OrderRepository orderRepository;
	private final TransactionRepository transactionRepository;
	private final S3Service s3Service;

//	private static final String INPUT_FILE_PATH = "C:\\Users\\abhij\\Desktop\\orders.xlsx";

	@Inject
	public OrderSupplierTask(TransactionRepository transactionRepository, OrderRepository orderRepository,
			S3Service s3Service, Logger logger) {
		this.orderRepository = orderRepository;
		this.transactionRepository = transactionRepository;
		this.logger = logger;
		this.s3Service = s3Service;
	}

	@Override
	public TaskRunnerOutput run() {
		MultipleResults results = new MultipleResults();
		try {
			S3Bucket ordersBucket = s3Service.getBucket("orders");
			logger.info("Getting Input file from s3 path - orders/Input-file");
			byte[] bytesFromS3 = ordersBucket.get("Input-file/orders.xlsx");
//			List<Map<String, String>> ordersList = new ExcelPerser().parseExcel(INPUT_FILE_PATH);
			List<Map<String, String>> ordersList = new ExcelPerser().parseExcel(bytesFromS3);
			logger.info("Input file parsed");

			ordersList.forEach(item -> {
				Transaction transaction = new Transaction();
				transaction.setUuid(UUID.randomUUID());
				transaction.setStatus("NEW");

				Order order = new Order();
				order.setItem(item.get("item"));
				order.setType(item.get("type"));
				order.setQuantity(item.get("quantity"));
				order.setStatus("NEW");
				order.setTransaction(transaction);
				
				logger.info("Order created: {}", order);

				transactionRepository.create(transaction);
				orderRepository.create(order);

				results.addRow(new SingleResult().withColumn("_sys_transaction_id", transaction.getUuid().toString()));
			});

		} catch (IOException ioe) {
			logger.debug(ioe.getMessage());
		}

		return results;
//		return orders.stream().map(order -> new TransactionResult(taskInput, order.getTransaction())).collect(MultipleResults.toMultipleResults());
	}

}

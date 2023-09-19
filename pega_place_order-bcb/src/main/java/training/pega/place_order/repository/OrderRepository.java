package training.pega.place_order.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.j256.ormlite.support.ConnectionSource;
import com.workfusion.odf2.transaction.model.OdfTransactionalEntity;
import com.workfusion.odf2.transaction.repository.TransactionalEntityRepository;

import training.pega.place_order.model.Order;

public class OrderRepository extends TransactionalEntityRepository<Order> {

	public OrderRepository(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Order.class);

	}

	public List<Order> findAll(UUID trancastionId) throws SQLException {
		return dao.queryForEq(OdfTransactionalEntity.TRANSACTION_UUID_COLUMN, trancastionId);
	}

	public Order getOrder(UUID trancastionId) throws SQLException {
		return dao.queryForEq(OdfTransactionalEntity.TRANSACTION_UUID_COLUMN, trancastionId).get(0);
	}
	
	public List<Order> findAllNew() throws SQLException {
		return dao.queryForEq(Order.STATUS, "NEW");
	}

}

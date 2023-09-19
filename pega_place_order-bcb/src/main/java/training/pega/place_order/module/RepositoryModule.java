package training.pega.place_order.module;

import java.sql.SQLException;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.support.ConnectionSource;
import com.workfusion.odf2.core.cdi.OdfModule;

import training.pega.place_order.repository.OrderRepository;

public class RepositoryModule implements OdfModule {

	@Provides
	@Singleton
	public OrderRepository orderRepository(ConnectionSource connectionSource) throws SQLException {
		return new OrderRepository(connectionSource);
	}

}

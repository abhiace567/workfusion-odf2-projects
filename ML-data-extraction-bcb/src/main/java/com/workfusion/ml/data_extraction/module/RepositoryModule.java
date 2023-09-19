package com.workfusion.ml.data_extraction.module;

import java.sql.SQLException;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.support.ConnectionSource;
import com.workfusion.ml.data_extraction.repository.ExtractionModelResultsRepository;
import com.workfusion.odf2.core.cdi.OdfModule;

public class RepositoryModule implements OdfModule {
	
	@Provides
	@Singleton
	public ExtractionModelResultsRepository extractionModelResultRepository(ConnectionSource connectionSource) throws SQLException {
		return new ExtractionModelResultsRepository(connectionSource);
	}

}

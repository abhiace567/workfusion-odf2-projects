package com.workfusion.ml.data_extraction.repository;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;
import com.workfusion.ml.data_extraction.model.ExtractionModelResult;
import com.workfusion.odf2.core.orm.OrmLiteRepository;

public class ExtractionModelResultsRepository extends OrmLiteRepository<ExtractionModelResult> {

	public ExtractionModelResultsRepository(ConnectionSource connectionSource)
			throws SQLException {
		super(connectionSource, ExtractionModelResult.class);
	}

}

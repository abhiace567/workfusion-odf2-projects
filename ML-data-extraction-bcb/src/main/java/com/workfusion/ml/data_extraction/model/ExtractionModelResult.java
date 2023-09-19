package com.workfusion.ml.data_extraction.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.workfusion.odf2.core.orm.Datastore;
import com.workfusion.odf2.core.orm.DatastoreType;

@DatabaseTable(tableName = "extraction_model_results")
@Datastore(type = DatastoreType.VERSIONED)
public class ExtractionModelResult {

	@DatabaseField(columnName = "gold_value", dataType = DataType.STRING)
	private String goldValue;

	@DatabaseField(columnName = "extracted_value", dataType = DataType.STRING)
	private String extractedValue;

	public String getGoldValue() {
		return goldValue;
	}

	public void setGoldValue(String goldValue) {
		this.goldValue = goldValue;
	}

	public String getExtractedValue() {
		return extractedValue;
	}

	public void setExtractedValue(String extractedValu) {
		this.extractedValue = extractedValu;
	}
	
}

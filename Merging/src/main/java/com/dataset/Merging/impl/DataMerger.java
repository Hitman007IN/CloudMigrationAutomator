package com.dataset.Merging.impl;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class DataMerger {

	public static void mergeDatasets(SparkSession sparkSession, String inputPath1, String inputPath2, String outputPath) {
		
		Dataset<Row> userDataset = sparkSession.read().parquet(inputPath1);
		Dataset<Row> addressDataset = sparkSession.read().parquet(inputPath2);
		
		Dataset<Row> result = userDataset.join(addressDataset, userDataset.col("addressid").equalTo(addressDataset.col("addressid")), "left_outer");
		
		result.write().format("parquet").save(outputPath);
	}
}

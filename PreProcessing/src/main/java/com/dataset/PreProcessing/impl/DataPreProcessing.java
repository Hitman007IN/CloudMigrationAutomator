package com.dataset.PreProcessing.impl;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class DataPreProcessing {

	public static void preprocessing(SparkSession sparkSession, String inputPath, String outputPath) {
		
		Dataset<Row> addressDataset = sparkSession.read().parquet(inputPath);
		
		addressDataset.createOrReplaceTempView("ADDRESS");
		
		Dataset<Row> result = sparkSession.sql("select * from ADDRESS where zipcode in ('99501','35801')");
		
		result.write().format("parquet").save(outputPath);
	}
}

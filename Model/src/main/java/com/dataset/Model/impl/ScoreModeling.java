package com.dataset.Model.impl;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class ScoreModeling {

	public static void generateScore(String inputPath, String outputPath, SparkSession sparkSession) {
		
		Dataset<Row> userDataset = sparkSession.read().csv(inputPath);
		
		userDataset.write().format("parquet").save(outputPath);
		
	}
}

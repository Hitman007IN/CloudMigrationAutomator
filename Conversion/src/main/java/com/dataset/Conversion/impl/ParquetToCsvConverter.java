package com.dataset.Conversion.impl;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class ParquetToCsvConverter {

	public static void convertToCsv(String inputPath, String outputPath, JavaSparkContext jsc) {
	
		SparkSession session = new SparkSession(jsc.sc());
		Dataset<Row> dataset = session.read().parquet(inputPath);
		
		dataset.write().csv(outputPath);
		
		session.close();
	}
}

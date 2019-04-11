package com.dataset.PreProcessing;

import java.util.Properties;

import org.apache.spark.sql.SparkSession;

import com.dataset.PreProcessing.impl.DataPreProcessing;
import com.dataset.PreProcessing.utils.PropertyReader;

public class DatasetPreProcessingDriver 
{
    public static void main( String[] args )
    {
    	final String inputPath = args[0];
    	final String outputPath = args[1];
    	//final String propertyPath = args[2];
    	
    	//PropertyReader scp = new PropertyReader();
		//Properties prop = scp.loadProperties(propertyPath);
		
		SparkSession sparkSession = SparkSession.builder().master("yarn").appName("dataset-preprocessing-app").getOrCreate();
		
		DataPreProcessing.preprocessing(sparkSession, inputPath, outputPath);
		
		sparkSession.close();
    }
}

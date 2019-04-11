package com.dataset.Model;

import java.util.Properties;
import org.apache.spark.sql.SparkSession;

import com.dataset.Model.impl.ScoreModeling;
import com.dataset.Model.utils.PropertyReader;

public class ScoreDriver 
{
    public static void main( String[] args )
    {
    	final String inputPath = args[0];
		final String outputPath = args[1];
		//final String propertyPath = args[2];
		
		//PropertyReader scp = new PropertyReader();
		//Properties prop = scp.loadProperties(propertyPath);
		
		SparkSession sparkSession = SparkSession.builder().master("yarn").appName("dataset-modeling-app").getOrCreate();
		
		ScoreModeling.generateScore(inputPath, outputPath, sparkSession);
		
		sparkSession.close();
    }
}

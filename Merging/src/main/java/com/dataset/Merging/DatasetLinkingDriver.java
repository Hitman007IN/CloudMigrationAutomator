package com.dataset.Merging;

import java.util.Properties;
import org.apache.spark.sql.SparkSession;
import com.dataset.Merging.impl.DataMerger;
import com.dataset.Merging.utils.PropertyReader;


public class DatasetLinkingDriver 
{
    public static void main( String[] args )
    {
    	final String inputPath1 = args[0];
		final String inputPath2 = args[1];
		final String outputPath = args[2];
		final String propertyPath = args[3];
		
		PropertyReader scp = new PropertyReader();
		Properties prop = scp.loadProperties(propertyPath);
		
		SparkSession sparkSession = SparkSession.builder().master(prop.getProperty("spark.master")).appName(prop.getProperty("dataset-linking-app")).getOrCreate();
		
		DataMerger.mergeDatasets(sparkSession, inputPath1, inputPath2, outputPath);
    }
}

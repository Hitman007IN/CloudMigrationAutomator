package com.dataset.Conversion;

import java.util.Properties;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import com.dataset.Conversion.impl.ParquetToCsvConverter;
import com.dataset.Conversion.impl.TextToParquetConverter;
import com.dataset.Conversion.utils.PropertyReader;

public class ParquetConversionDriver 
{
    public static void main( String[] args )
    {
    	
    	final String datasource = args[0];
		final String inputPath = args[1];
		final String outputPath = args[2];
		final String delimiter = args[3];
		//final String propertyPath = args[4];
		final String fileExtension = args[4];
		
		//PropertyReader scp = new PropertyReader();
		//Properties prop = scp.loadProperties(propertyPath);
		
		final SparkConf conf = new SparkConf().setMaster("yarn").setAppName("dataset-conversion-app");
		
		JavaSparkContext jsc = new JavaSparkContext(conf);
		
		if("parquet".equalsIgnoreCase(fileExtension))
			ParquetToCsvConverter.convertToCsv(inputPath, outputPath, jsc);
		else
			TextToParquetConverter.convertToParquet(inputPath, outputPath, delimiter, jsc, null, datasource);
		
		jsc.close();
    }
}

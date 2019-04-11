package com.dataset.Conversion.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class TextToParquetConverter {

	public static void convertToParquet(final String inputPath, final String outputPath, final String delimiter,
			final JavaSparkContext jsc, Properties prop, String datasource) {
		
		System.out.println("Inside convertToParquet");

		String schemaString = "";

		SparkSession sparkSession = new SparkSession(jsc.sc());
		JavaRDD<String> data = jsc.textFile(inputPath);

		JavaRDD<Row> rows = data.map(new Function<String, Row>() {
			private static final long serialVersionUID = -4332903997027358601L;

			@Override
			public Row call(String line) throws Exception {
				/*
				 * If row ends with delimiter, add space to prevent array index out of bounds.
				 * This is because spark assumes that there has been no data except the
				 * delimiter.
				 * 
				 */
				if (line.endsWith(delimiter)) {
					line = line + " ";
				}
				Object[] objs = (Object[]) line.split(null != delimiter ? delimiter : "");
				List<Object> newObjs = new ArrayList<Object>();
				Arrays.stream(objs).forEach(obj -> {
					
					newObjs.add(obj);
					
				});
				
				return RowFactory.create(newObjs.toArray());
			}
		});

		if ("user".equalsIgnoreCase(datasource)) {

			schemaString = "userid,firstname,lastname,addressid";//prop.getProperty("layout.users.data");

		} else if ("address".equalsIgnoreCase(datasource)) {

			schemaString = "addrid,houseno,streetname,city,state,zipcode";//prop.getProperty("layout.address.data");

		}

		List<StructField> fields = new ArrayList<StructField>();

		for (String fieldName : schemaString.split(",")) {
			fields.add(DataTypes.createStructField(fieldName, DataTypes.StringType, true));
		}

		StructType schema = DataTypes.createStructType(fields);
		Dataset<Row> dataSet = sparkSession.createDataFrame(rows, schema);
		dataSet.write().parquet(outputPath);
	}
}

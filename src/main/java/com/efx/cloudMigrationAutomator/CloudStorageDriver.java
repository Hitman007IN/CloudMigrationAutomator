package com.efx.cloudMigrationAutomator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.CopyWriter;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageClass;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;

import static java.nio.charset.StandardCharsets.UTF_8;


public class CloudStorageDriver {
	
	public static void main(String[] args) throws IOException {
		// Instantiates a client
		FileInputStream fis = new FileInputStream("src/main/resources/My Project-dc7992684edf.json");

		Storage storage = StorageOptions.newBuilder()
				.setCredentials(ServiceAccountCredentials.fromStream(fis))
				.setProjectId("divine-ceremony-225408")
				.build()
				.getService();

		String bucketName = args[0];
		String inputFiles = args[1];
		String jars = args[2];
		String templates = args[3];

		Boolean isBucketCreated = createStorageBucketInCloud(storage, bucketName);
		
		HashMap<String, String> filesInDirectory = new HashMap<String,String>();
		filesInDirectory.put("input/", inputFiles);
		filesInDirectory.put("output/", null);
		filesInDirectory.put("jars/", jars);
		filesInDirectory.put("template/", templates);
		
		if(isBucketCreated) {
			
			filesInDirectory.forEach((directory,filePath)->{
				
				System.out.println("directory : " + directory + " files : " + filePath);
				
				if(createDirectory(storage, bucketName, directory)) {
					
					System.out.println("directory created successfully");
					
					try {
						if(uploadObjectsToBucket(storage, bucketName, directory, filePath))
							System.out.println("file uploaded successfully");
						else
							System.out.println("Failed to upload files because of detailed exception listed in console");
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				} else
					System.out.println("Failed to create directory because of detailed exception listed in console");
			});
			
		} else
			System.out.println("Failed to create bucket because of detailed exception listed in console");

	}

	public static boolean createStorageBucketInCloud(Storage storage, String bucketName) {
		
		System.out.println("inside createStorageBucketInCloud");
		System.out.println("bucketName:::" + bucketName);

		Bucket bucket = storage.create(BucketInfo.newBuilder(bucketName).setStorageClass(StorageClass.REGIONAL).setLocation("us-east1").build());

		System.out.printf("Bucket %s created.%n", bucket.getName());
		
		if(!bucket.getName().isEmpty())
			return true;
		else
			return false;
	}
	
	
	public static boolean createDirectory(Storage storage, String bucketName, String directory) {
		
		System.out.println("inside createDirectory");
		System.out.println("create directories under "+bucketName);
		
		BlobId blobId = BlobId.of(bucketName, directory);
	    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
	    Blob blob = storage.create(blobInfo);
	    
	    return blob.exists();
	 
	}

	
	public static boolean uploadObjectsToBucket(Storage storage, String bucketName, String directory, String filePath) throws IOException {
		
		System.out.println("inside uploadObjectsToBucket");
		System.out.println("upload files under "+bucketName+"/"+directory);		
		
		Blob blob = null;
		Blob copiedBlob = null;
		boolean isUploadSuccess = false;
		
		if(null != filePath) {
			
			File folder = new File(filePath);
			
	        File[] files = folder.listFiles();
	        
	        FileInputStream fis = null;
	 
	        for (File file : files)
	        {
	            System.out.println("File present inside local FS::: "+file.getName());
	            
	            fis = new FileInputStream(file);
	            
	            BlobId blobId = BlobId.of(bucketName, directory+file.getName());
		        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
		    	blob = storage.create(blobInfo, fis);
	    		
	            CopyWriter copyWriter = blob.copyTo(bucketName);
	            copiedBlob = copyWriter.getResult();
	    		
	            if(copiedBlob.exists()) {
	            	isUploadSuccess = true;
	            	System.out.println("File "+file.getName()+" uploaded successfully");
	            	continue;
	            } else
	            	return false;
	        }

		}
		
		return isUploadSuccess;

	}

}

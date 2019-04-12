# CloudMigrationAutomator

This project deals Automating the entire flow from moving the necessary data to cloud storage using GCP clinet libraries which is used in java program to upload the input files, Jars, Templates etc to cloud storage to running a series of Spark Jobs on Cloud Dataproc with the help of Workflow template.

Stackdriver is used for email notofcation, monitoring and logs

# project Structure
The project is split into mutiple modules:-

1) Java program - to create bucket, upload files
2) Conversion - for transforming different file formats like csv, txt to parquet for faster big data processing
3) Pre Processing - to deal with making all the dataset to a specific type (like person specific or area specific)
4) Merging - After preprocessing the datasets need to linked together based on some attribute
5) Model - to generate the points for each person based on some condition

# How to run
1) Go to APIs and Services page of GCP Console and click on credentials
2) create credentials with servie account keys
3) Select json and new service account
4) provide role of project owner
5) Build Conversion, linking, pre processing and model maven projects in local
2) Copy input files, jars to specific folders in the main project src/main/resources/filesToUpload
3) Main project has all the folders necessary folders created
4) Run the main project with args to be given (input, jars, template location in src/main/resources etc)
5) verify bucket is created with necessary files uploaded to bucket
6) Open shell in GCP
7) create a yaml file and copy the template content to it
8) Run 
	gcloud dataproc workflow-templates create cloud-migration-demo-template

	gcloud dataproc workflow-templates import cloud-migration-demo-template --source cloud-migration-demo.yaml

	gcloud dataproc workflow-templates instantiate-from-file --file cloud-migration-demo.yaml

	gcloud dataproc workflow-templates delete cloud-migration-demo-template

# Snapshots
Cloud Storage Bucket
![alt text](https://github.com/Hitman007IN/CloudMigrationAutomator/blob/master/image/cloudStorageBucket.png)

Cloud Dataflow Template running in Cloud Shell
![alt text](https://github.com/Hitman007IN/CloudMigrationAutomator/blob/master/image/runningTemplateInShell.png)

Stack Driver Dashboard
![alt text](https://github.com/Hitman007IN/CloudMigrationAutomator/blob/master/image/StackDriverDashboard1.png)

Stack Driver Dashboard
![alt text](https://github.com/Hitman007IN/CloudMigrationAutomator/blob/master/image/StackDriverDashboard2.png)

Alert Policy
![alt text](https://github.com/Hitman007IN/CloudMigrationAutomator/blob/master/image/alertPolicy.png)

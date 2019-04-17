exports.startWorkflow = (event, callback) => {

  const {google} = require('googleapis');

  const region = 'global'
  const zone = 'us-east1-b'
  const clusterName = 'spark-conversion-cluster'

  console.log("Creating auth client: ");
  google.auth.getApplicationDefault(
    (err, authClient, projectId) => {
      if (authClient.createScopedRequired && authClient.createScopedRequired()) {
        authClient = authClient.createScoped([
          'https://www.googleapis.com/auth/cloud-platform',
          'https://www.googleapis.com/auth/userinfo.email'
        ]);
      }

      const request = {
        parent: "projects/" + projectId + "/regions/" + region,
        resource: {
          "placement": {
            "managedCluster": {
              "clusterName": clusterName,
              "config": {
                "gceClusterConfig": {
                  "zoneUri" : zone,
                },
				"masterConfig": {
					"diskConfig": {
						"bootDiskSizeGb": 500
					},
					"machineTypeUri": "n1-standard-4"
				},
				"workerConfig": {
					"diskConfig": {
						"bootDiskSizeGb": 500
					},
					"machineTypeUri": "n1-standard-4",
					"numInstances": 2
				}
              }
            }
          },
          "jobs": [
            {
              "stepId": "csv-to-parquet-step1",
              "sparkJob": {
                "args": ["user","gs://cloud_migration_demo/input/users.csv","gs://cloud_migration_demo/output/userParquet",",","csv"],
				"jarFileUris": "gs://cloud_migration_demo/jars/Conversion.jar",
				"mainClass": "com.dataset.Conversion.ParquetConversionDriver"
              },
              "prerequisiteStepIds": [],
            },
			{
              "stepId": "txt-to-parquet-step2",
              "sparkJob": {
                "args": ["address","gs://cloud_migration_demo/input/address.txt","gs://cloud_migration_demo/output/addressParquet",",","txt"],
				"jarFileUris": "gs://cloud_migration_demo/jars/Conversion.jar",
				"mainClass": "com.dataset.Conversion.ParquetConversionDriver"
              },
              "prerequisiteStepIds": [],
            },
			{
              "stepId": "preprocess-address-step3",
              "sparkJob": {
                "args": ["gs://cloud_migration_demo/output/addressParquet","gs://cloud_migration_demo/output/preprocessedAddress"],
				"jarFileUris": "gs://cloud_migration_demo/jars/PreProcessing.jar",
				"mainClass": "com.dataset.PreProcessing.DatasetPreProcessingDriver"
              },
              "prerequisiteStepIds": ["csv-to-parquet-step1","txt-to-parquet-step2"],
            },
			{
              "stepId": "linking-data-step4",
              "sparkJob": {
                "args": ["gs://cloud_migration_demo/output/userParquet","gs://cloud_migration_demo/output/preprocessedAddress","gs://cloud_migration_demo/output/master","merge"],
				"jarFileUris": "gs://cloud_migration_demo/jars/Merging.jar",
				"mainClass": "com.dataset.Merging.DatasetLinkingDriver"
              },
              "prerequisiteStepIds": ["preprocess-address-step3"],
            },
			{
              "stepId": "generate-score-step5",
              "sparkJob": {
                "args": ["gs://cloud_migration_demo/input/score.csv","gs://cloud_migration_demo/output/score"],
				"jarFileUris": "gs://cloud_migration_demo/jars/Model.jar",
				"mainClass": "com.dataset.Model.ScoreDriver"
              },
              "prerequisiteStepIds": ["linking-data-step4"],
            },
			{
              "stepId": "merging-master-score-step6",
              "sparkJob": {
                "args": ["gs://cloud_migration_demo/output/master","gs://cloud_migration_demo/output/score","gs://cloud_migration_demo/output/final","score"],
				"jarFileUris": "gs://cloud_migration_demo/jars/Merging.jar",
				"mainClass": "com.dataset.Merging.DatasetLinkingDriver"
              },
              "prerequisiteStepIds": ["generate-score-step5"],
            },
			{
              "stepId": "convert-final-csv-step7",
              "sparkJob": {
                "args": ["userAddress","gs://cloud_migration_demo/output/final","gs://cloud_migration_demo/output/finalReportToCsv",",","parquet"],
				"jarFileUris": "gs://cloud_migration_demo/jars/Conversion.jar",
				"mainClass": "com.dataset.Conversion.ParquetConversionDriver"
              },
              "prerequisiteStepIds": ["merging-master-score-step6"],
            }
          ]
        }
      };

      const dataproc = google.dataproc({ version: 'v1beta2', auth: authClient});
      dataproc.projects.regions.workflowTemplates.instantiateInline(
        request, (err, result) => {
          if (err) {
            throw err;
          }
          console.log(result);
          callback();
        });
    });
};
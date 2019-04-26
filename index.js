/**
 * Responds to any HTTP request.
 *
 * @param {!express:Request} req HTTP request context.
 * @param {!express:Response} res HTTP response context.
 */

const dataproc = require('@google-cloud/dataproc');

exports.helloWorld = (req, res) => {


const client = new dataproc.v1.WorkflowTemplateServiceClient({
  // optional auth parameters.
});


const formattedName = client.workflowTemplatePath('myinspiration', 'global', 'cloud-migration-demo-template');

// Handle the operation using the promise pattern.
client.instantiateWorkflowTemplate({name: formattedName})
  .then(responses => {
    const [operation, initialApiResponse] = responses;

    // Operation#promise starts polling for the completion of the LRO.
    return operation.promise();
  })
  .then(responses => {
    const result = responses[0];
    const metadata = responses[1];
    const finalApiResponse = responses[2];
  })
  .catch(err => {
    console.error(err);
  });


};

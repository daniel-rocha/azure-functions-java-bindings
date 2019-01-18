# Triggers and Bindings examples for Java Azure Functions

This repository contains 20+ usage examples of [*triggers* and *bindings*](https://docs.microsoft.com/en-us/azure/azure-functions/functions-triggers-bindings) in Java serverless [Azure Functions](https://azure.microsoft.com/en-us/documentation/articles/functions-overview/). 

**Triggers** define how your function will be invoked: by an HTTP request, a new blob created on a Storage container, a new message in Event Grid, etc. 

**Bindings** provide a declarative way to connect data to your function code. They can be used to declare *input* parameters to your function, for example, a set of CosmosDB documents, or a message from a Service Bus queue. *Output* bindings allow your function to supply the Functions runtime with parameters that the runtime will use to perform a data operation. Those can be: a JSON document to be added to a CosmosDB collection, an image file to be added to Blob storage, and so on.

Together, triggers and bindings allow you to greatly reduce data-management boilerplate code in your functions, freeing you to focus on the business problem that needs to be solved.

## Pre-requisites

In order to use these examples, you will need:

1. An active Azure Account ([get a free trial account here](https://azure.microsoft.com/en-us/offers/ms-azr-0044p/)).
2. Java SDK 1.8+ installed.
3. Maven.
2. Some knowledge of Java and Git.
3. [Azure CLI installed](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest).
4. [Visual Studio Code](https://code.visualstudio.com/) to modify the source code to suit your own needs. VS Code is optional; you can use [any supported IDE](https://docs.microsoft.com/en-us/azure/azure-functions/functions-reference). If using Visual Studio Code, please ensure to install the [Azure Functions extension](https://marketplace.visualstudio.com/items?itemName=ms-azuretools.vscode-azurefunctions).
5. An HTTP client, such as [Postman](https://www.getpostman.com/) for testing the GET and POST calls in HTTP-triggered functions. 


## Deploying the support infrastructure

The first step is to deploy the infrastructure components that will support the examples: a Function App, Cosmos DB account, Storage account, etc. There are many ways to do this:

- Click the "Deploy to Azure" button below. You can then fill out the required parameters, and Azure Resource Manager will deploy all services that need to be in place for the examples to run.

	<a href="https://portal.azure.com/#create/Microsoft.Template/uri/https%3A%2F%2Fraw.githubusercontent.com%2Fdaniel-rocha%2Fazure-functions-java-bindings%2Fmaster%2Fazuredeploy.json" target="_blank">
    <img src="http://azuredeploy.net/deploybutton.png"/>
</a>

- You can also deploy the support infrastructure from the Azure CLI, by executing the following commands: 

	```shell 
    az group create --name ExampleResourceGroup --location "Central US"
    ```
    
    ```shell 
    az group deployment create \
  --name ExampleDeployment \
  --resource-group ExampleResourceGroup \
  --template-uri "https://raw.githubusercontent.com/daniel-rocha/azure-functions-java-bindings/master/azuredeploy.json" \
  --parameters "Function App Name"=<yourappname> "Database Account Name"=<yourdatabasename> "storageAccountName"=<yourstorageaccountname>
  ```

	See below for the description of each mandatory parameter.
    
The deployment can take a few minutes to complete. Once it is successful, you can move to the next section.

###### Required Parameters

These are all required parameters for a successful deployment:

| Parameter             | Description                                                                                                                                                                              |
|-----------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Subscription          | The name or ID of the Azure subscription to which the examples are being deployed. Can be empty if only subscription is active.                                                          |
| Resource Group        | Name of the resource group that will be created to host all the support services.                                                                                                        |
| Location              | Azure region where the resource group (and associated services) will live.                                                                                                               |
| Function App Name     | The name of your function app, for example, "myfunctionapp". This will be the hostname of your function app when testing HTTP calls. The function app will be created during deployment. |
| Database Account Name | The name of your CosmosDB database account that will be created during deployment.                                                                                                       |
| storageAccountName    | The name of the storage account that will be created during deployment.                                                                                                                  |

## Deploying the support data

Our examples rely on some data to be placed in the right places, for the bindings to work. Here is how to deploy it:

1. Clone this repository to your local machine.
2. Run the following commands:

	**Windows**:

	```shell
    C:> cd <project path>\env
    ```
    ```shell
    C:> setupenvironment.ps1 -group <myresourcegroup> -s <subscription name>
    ```
    
    **Mac OSX and Linux** 
    ```shell
    $ cd <project path>/env
    ```
    ```shell
    $ setupenvironment.sh
    ```

## Deploying the code

The first step is to deploy the infrastructure components that will support the examples: a Function App, Cosmos DB account, Storage account, etc. There are many ways to do this:

- [Deploy to Azure using Azure Functions and Visual Studio Code](https://code.visualstudio.com/tutorials/functions-extension/getting-started)
- [Deploy an Azure Functions app using Maven](https://docs.microsoft.com/en-us/azure/azure-functions/functions-create-first-java-maven#deploy-the-function-to-azure)

Once your functions project is deployed, you can test if everything is working by going to the following URL with your browser or Postman:

[https://\<yourfunctionappname\>.azurewebsites.net/api/WriteOneDoc?desc=MyData+Here](https://<yourfunctionappname>.azurewebsites.net/api/WriteOneDoc?desc=MyData+Here)

This function takes a `desc` query string parameter, creates a CosmosDB document with it, and a randomly generated ID, and returns the document to the caller as JSON:

`{"id":"1860707505", "description": "MyData Here2"}`

If you got here, congratulations! Now you're ready to explore all examples listed below. Instructions on how to test them are included in the comments section of each source file.

## List of examples

Here is a complete list of the examples currently in this repository:

 * [cosmos](./src/main/java/com/danielrocks/function/cosmos)
   * [input](./src/main/java/com/danielrocks/function/cosmos/input)
     * [DocByIdFromRouteSqlQuery.java](./src/main/java/com/danielrocks/function/cosmos/input/DocByIdFromRouteSqlQuery.java)
     * [DocByIdFromQueryString.java](./src/main/java/com/danielrocks/function/cosmos/input/DocByIdFromQueryString.java)
     * [DocByIdFromQueryStringPojo.java](./src/main/java/com/danielrocks/function/cosmos/input/DocByIdFromQueryStringPojo.java)
     * [DocByIdFromRoute.java](./src/main/java/com/danielrocks/function/cosmos/input/DocByIdFromRoute.java)
     * [DocsFromRouteSqlQuery.java](./src/main/java/com/danielrocks/function/cosmos/input/DocsFromRouteSqlQuery.java)
   * [trigger](./src/main/java/com/danielrocks/function/cosmos/trigger)
     * [Trigger.java](./src/main/java/com/danielrocks/function/cosmos/trigger/Trigger.java)
   * [output](./src/main/java/com/danielrocks/function/cosmos/output)
   * [WriteOneDoc.java](./src/main/java/com/danielrocks/function/cosmos/output/WriteOneDoc.java)
   * [WriteOneDocOutputBinding.java](./src/main/java/com/danielrocks/function/cosmos/output/WriteOneDocOutputBinding.java)
   * [WriteMultipleDocsOutputBinding.java](./src/main/java/com/danielrocks/function/cosmos/output/WriteMultipleDocsOutputBinding.java)
 * [common](./src/main/java/com/danielrocks/function/common)
   * [ToDoItem.java](./common/src/main/java/com/danielrocks/function/ToDoItem.java)
 * [blob](./src/main/java/com/danielrocks/function/blob)
   * [input](./src/main/java/com/danielrocks/function/blob/input)
     * [HttpTriggerBlobInput.java](./src/main/java/com/danielrocks/function/blob/input/HttpTriggerBlobInput.java)
     * [QueueTriggerBlobInput.java](./src/main/java/com/danielrocks/function/blob/input/QueueTriggerBlobInput.java)
   * [output](./src/main/java/com/danielrocks/function/blob/output)
     * [HttpTriggerBlobOutput.java](./src/main/java/com/danielrocks/function/blob/output/HttpTriggerBlobOutput.java)
     * [QueueTriggerBlobOutput.java](./src/main/java/com/danielrocks/function/blob/output/QueueTriggerBlobOutput.java)
   * [trigger](./src/main/java/com/danielrocks/function/blob/trigger)
   * [TriggerByteArray.java](./src/main/java/com/danielrocks/function/blob/trigger/TriggerByteArray.java)
   * [TriggerPojo.java](./src/main/java/com/danielrocks/function/blob/trigger/TriggerPojo.java)
   * [TriggerString.java](./src/main/java/com/danielrocks/function/blob/trigger/TriggerString.java)
 * [event](./event)
   * [trigger](./src/main/java/com/danielrocks/function/event/trigger)
     * [TriggerString.java](./src/main/java/com/danielrocks/function/event/trigger/TriggerString.java)
     * [TriggerPojo.java](./src/main/java/com/danielrocks/function/event/trigger/TriggerPojo.java)
   * [EventSchema.java](.src/main/java/com/danielrocks/function//event/EventSchema.java)
 * [http](./src/main/java/com/danielrocks/function/http)
     * [output](./src/main/java/com/danielrocks/function/http/output)
     * [trigger](./src/main/java/com/danielrocks/function/http/trigger)
         * [TriggerStringGet.java](./src/main/java/com/danielrocks/function/http/trigger/TriggerStringGet.java)
         * [TriggerStringPost.java](./src/main/java/com/danielrocks/function/http/trigger/TriggerStringPost.java)
         * [TriggerPojoPost.java](./src/main/java/com/danielrocks/function/http/trigger/TriggerPojoPost.java)
         * [TriggerStringRoute.java](./src/main/java/com/danielrocks/function/http/trigger/TriggerStringRoute.java)

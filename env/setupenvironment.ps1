az login

$resourceGroup = "functions-java-samples-resource-group"
$location = "westus"
$uniqueAppendix = (New-Guid).ToString() -replace "-", "";
$storageAccountName =  ("functionsample" + $uniqueAppendix).Substring(0,24)
$storageContainerName = "samples-workitems"
$testdataFileName = "testdata.json";
$testdataFilePath = "./" + $testdataFileName;
$cosmosDBAccountName = ("functionsamples" + $uniqueAppendix).Substring(0,24);
$cosmosDBName = "ToDoList";
$cosmosDBCollectionName = "Items";
$storageQueueName = "myqueue-items-sample"

#create resource group
az group create -n $resourceGroup -l $location

#create storage account
az storage account create -n $storageAccountName -l $location -g $resourceGroup --sku Standard_LRS --kind StorageV2

#get storage account connection string
$storageAccountConnectionString = (az storage account show-connection-string -n $storageAccountName -g $resourceGroup | ConvertFrom-Json).connectionString

#create storage account containers
az storage container create -n $storageContainerName --connection-string $storageAccountConnectionString
az storage container create -n "samples-workitems-outputs" --connection-string $storageAccountConnectionString

#upload test dat file
az storage blob upload -n $testdataFileName -f $testdataFilePath -c $storageContainerName --connection-string $storageAccountConnectionString

#create storage queues
az storage queue create -n $storageQueueName --connection-string $storageAccountConnectionString

#add a message to the queue
$encodedMessageContent = [System.Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes($testdataFileName))
az storage message put --content $encodedMessageContent --queue-name $storageQueueName --connection-string $storageAccountConnectionString

#create the cosmos db 
az cosmosdb create -n $cosmosDBAccountName -g $resourceGroup 
$cosmosDBAccountKey = (az cosmosdb list-keys -n $cosmosDBAccountName -g $resourceGroup | ConvertFrom-Json).primaryMasterKey

#create database
az cosmosdb database create -d $cosmosDBName -n $cosmosDBAccountName -g $resourceGroup

#create collections
az cosmosdb collection create -c $cosmosDBCollectionName -d $cosmosDBName -n $cosmosDBAccountName -g $resourceGroup
az cosmosdb collection create -c "leases" -d $cosmosDBName -n $cosmosDBAccountName -g $resourceGroup

#create cosmosdb connection string
$cosmosDbConnectionString = "AccountEndpoint=https://" + $cosmosDBAccountName + ".documents.azure.com:443/;AccountKey=" + $cosmosDBAccountKey + ";"

#generate local settings file
$settingsFileTemplate = Get-Content template.local.settings.json
$settingsFileTemplate `
  -replace "{storageConnectionString}", $storageAccountConnectionString `
  -replace "{cosmosDbConnectionString}", $cosmosDbConnectionString `
  | Set-Content ../local.settings.json


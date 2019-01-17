# 
# usage: setupdata.ps1 -group <myresourcegroup> -s <subscription name>

param (
  [Parameter(Mandatory=$true)][string]$subscription,
  [Parameter(Mandatory=$true)][string]$group
)

Write-Output "ResourceGroup: $group Subscription: $subscription"

az login

$storageInputContainerName = "samples-workitems"
$storageOutputContainerName = "samples-workitems-outputs"
$storageQueueTriggerName = "myqueue-items-sample"
$storageQueueOutputName = "myqueue-items"
$testdataFileName = "testdata.json";
$testdataFilePath = "./" + $testdataFileName;
$cosmosDBName = "ToDoList";
$cosmosDBCollectionName = "Items";
$resourceGroup = $group;

#storage account name
$storageAccountName = (az storage account list --resource-group $resourceGroup --subscription "$subscription" | ConvertFrom-Json).name

#get storage account connection string
$storageAccountConnectionString = (az storage account show-connection-string -n $storageAccountName -g $resourceGroup --subscription "$subscription" | ConvertFrom-Json).connectionString

#create storage account containers
Write-Output "Creating storage containers..."
az storage container create -n $storageInputContainerName --connection-string $storageAccountConnectionString
az storage container create -n $storageOutputContainerName --connection-string $storageAccountConnectionString

#upload test dat file
Write-Output "Uploading test file to blob storage..."
az storage blob upload -n $testdataFileName -f $testdataFilePath -c $storageInputContainerName --connection-string $storageAccountConnectionString

#create storage queues
Write-Output "Creating queue storage containers..."
az storage queue create -n $storageQueueTriggerName --connection-string $storageAccountConnectionString
az storage queue create -n $storageQueueOutputName --connection-string $storageAccountConnectionString

#add a message to the queue
Write-Output "Adding message to queue..."
$encodedMessageContent = [System.Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes($testdataFileName))
az storage message put --content $encodedMessageContent --queue-name $storageQueueTriggerName --connection-string $storageAccountConnectionString

#get cosmos db connection parameters
$cosmosDBAccountName = (az cosmosdb list --resource-group $resourceGroup --subscription "$subscription" | ConvertFrom-Json).name
$cosmosDBAccountKey = (az cosmosdb list-keys -n $cosmosDBAccountName -g $resourceGroup --subscription "$subscription" | ConvertFrom-Json).primaryMasterKey

#create cosmos db
Write-Output "Creating CosmosDB database..."
az cosmosdb database create -d $cosmosDBName -n $cosmosDBAccountName -g $resourceGroup --subscription "$subscription"

#create collections
Write-Output "Creating CosmosDB collection..."
az cosmosdb collection create -c $cosmosDBCollectionName -d $cosmosDBName --partition-key-path '/id' -n $cosmosDBAccountName -g $resourceGroup --subscription "$subscription"
az cosmosdb collection create -c "leases" -d $cosmosDBName -n $cosmosDBAccountName -g $resourceGroup --subscription "$subscription"

#create cosmosdb connection string
$cosmosDbConnectionString = "AccountEndpoint=https://" + $cosmosDBAccountName + ".documents.azure.com:443/;AccountKey=" + $cosmosDBAccountKey + ";"

#generate local settings file
Write-Output "Generating local settings file..."
$settingsFileTemplate = Get-Content template.local.settings.json
$settingsFileTemplate `
  -replace "{storageConnectionString}", $storageAccountConnectionString `
  -replace "{cosmosDbConnectionString}", $cosmosDbConnectionString `
  | Set-Content ../local.settings.json


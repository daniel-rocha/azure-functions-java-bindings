Connect-AzAccount

$resourceGroup = "functions-java-sample-resource-group"
$location = "westus"
$storageAccountName =  ("functionsample" + (New-Guid).ToString() -replace "-", "").Substring(0,24)

New-AzResourceGroup -Name $resourceGroup -Location $location


$storageAccount = New-AzStorageAccount -ResourceGroupName $resourceGroup `
  -Name $storageAccountName `
  -Location $location `
  -SkuName Standard_LRS `
  -Kind StorageV2 

$storageAccountKey = `
  (Get-AzStorageAccountKey `
  -ResourceGroupName $resourceGroup `
  -Name $storageAccountName).Value[0]
  

$storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=" + $storageAccountName + ";AccountKey=" + $storageAccountKey;
$storageContainerName = "samples-workitems"
New-AzStorageContainer -Name $storageContainerName -Permission Container -Context $storageAccount.Context
Set-AzStorageBlobContent -File "./testdata.txt" -Blob "testdata.txt" -Container $storageContainerName -Context $storageAccount.Context -Force

$storageQueueName = "myqueue-items-sample"
$storageQueue = New-AzStorageQueue -Name $storageQueueName -Context $storageAccount.Context

# Create a new message using a constructor of the CloudQueueMessage class
$queueMessage = New-Object -TypeName Microsoft.WindowsAzure.Storage.Queue.CloudQueueMessage `
  -ArgumentList "testdata.txt"

  # Add a new message to the queue
$storageQueue.CloudQueue.AddMessageAsync($QueueMessage)


$settingsFileTemplate = Get-Content template.local.settings.json
$settingsFileTemplate -replace "{storageConnectionString}", $storageConnectionString | Set-Content ../local.settings.json


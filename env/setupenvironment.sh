#!/bin/bash
# usage: setupenvironment.sh <myresourcegroup> <subscription name>

function getKey {
  CLEANED=${1//[$'\t\r\n']}

  if [ -z "$3" ]; then 
    resultVal=`python3 -c "import sys, json; print(json.loads('$CLEANED')['"$2"'])"`
  else 
    resultVal=`python3 -c "import sys, json; print(json.loads('$CLEANED')[0]['"$2"'])"`
  fi
}

function sedEscaped {
  sed -i "s/$(echo $1 | sed -e 's/\([[\/.*]\|\]\)/\\&/g')/$(echo $2 | sed -e 's/[\/&]/\\&/g')/g" $3
}

resourceGroup=$1
subscription=$2

if [ -z "$resourceGroup" ] || [ -z "$subscription" ] ; then
  echo "usage: setupenvironment.sh <myresourcegroup> <subscription name>"
  exit
fi

echo "ResourceGroup: $resourceGroup Subscription: $subscription"

storageInputContainerName="samples-workitems"
storageOutputContainerName="samples-workitems-outputs"
storageQueueTriggerName="myqueue-items-sample"
storageQueueOutputName="myqueue-items"
testdataFileName="testdata.json"
testdataFilePath="./$testdataFileName"
cosmosDBName="ToDoList"
cosmosDBCollectionName="Items"

#storage account name
storageAccountName=`az storage account list --resource-group "$resourceGroup" --subscription "$subscription"`
getKey "$storageAccountName" 'name' true
storageAccountName=$resultVal

#get storage account connection string
storageAccountConnectionString=`az storage account show-connection-string -n "$storageAccountName" -g "$resourceGroup" --subscription "$subscription"`
getKey "$storageAccountConnectionString" 'connectionString'
storageAccountConnectionString=$resultVal

#create storage account containers
echo "Creating storage containers..."
az storage container create -n $storageInputContainerName --connection-string $storageAccountConnectionString
az storage container create -n $storageOutputContainerName --connection-string $storageAccountConnectionString

#upload test dat file
echo "Uploading test file to blob storage..."
az storage blob upload -n $testdataFileName -f $testdataFilePath -c $storageInputContainerName --connection-string $storageAccountConnectionString

#create storage queues
echo "Creating queue storage containers..."
az storage queue create -n $storageQueueTriggerName --connection-string $storageAccountConnectionString
az storage queue create -n $storageQueueOutputName --connection-string $storageAccountConnectionString

#add a message to the queue
echo "Adding message to queue..."
encodedMessageContent=`base64 testdata.json`
az storage message put --content "$encodedMessageContent" --queue-name $storageQueueTriggerName --connection-string $storageAccountConnectionString

#get cosmos db connection parameters
cosmosDBAccountName=`az cosmosdb list --resource-group "$resourceGroup" --subscription "$subscription"`
getKey "$cosmosDBAccountName" 'name' true
cosmosDBAccountName=$resultVal

#get key
cosmosDBAccountKey=`az cosmosdb list-keys -n $cosmosDBAccountName -g "$resourceGroup" --subscription "$subscription"`
getKey "$cosmosDBAccountKey" 'primaryMasterKey' 
cosmosDBAccountKey=$resultVal
echo $cosmosDBAccountKey

#create cosmos db
echo "Creating CosmosDB database..."
az cosmosdb database create -d $cosmosDBName -n $cosmosDBAccountName -g "$resourceGroup" --subscription "$subscription"

#create collections
echo "Creating CosmosDB collection..."
az cosmosdb collection create -c $cosmosDBCollectionName -d $cosmosDBName --partition-key-path '/id' -n $cosmosDBAccountName -g "$resourceGroup" --subscription "$subscription"
az cosmosdb collection create -c "leases" -d $cosmosDBName -n $cosmosDBAccountName -g "$resourceGroup" --subscription "$subscription"

#create cosmosdb connection string
cosmosDbConnectionString="AccountEndpoint=https://$cosmosDBAccountName.documents.azure.com:443/;AccountKey=$cosmosDBAccountKey;"

#generate local settings file
echo "Generating local settings file..."
cp template.local.settings.json ../local.settings.json

sedEscaped "{storageConnectionString}" "$storageAccountConnectionString" ../local.settings.json
sedEscaped "{cosmosDbConnectionString}" "$cosmosDbConnectionString" ../local.settings.json


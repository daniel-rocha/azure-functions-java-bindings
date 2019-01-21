package com.danielrocks.function.event.trigger;

import com.danielrocks.function.event.EventSchema;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.EventGridTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

/**
 * Azure Functions with Event Grid Trigger.
 * 
 * The following example shows a Java function that uses the EventGridTrigger
 * annotation to be notified of a certain event, cast to a Pojo (EventSchema)
 *
 */

public class TriggerPojo {

  @FunctionName("eventGridMonitor")
  public void logEvent(
    @EventGridTrigger(
      name = "event"
    ) 
    EventSchema event, 
    final ExecutionContext context) {
      // log 
      context.getLogger().info("Event content: ");
      context.getLogger().info("Subject: " + event.subject);
      context.getLogger().info("Time: " + event.eventTime);
      context.getLogger().info("Id: " + event.id);
      context.getLogger().info("Data: " + event.data);
  }
}

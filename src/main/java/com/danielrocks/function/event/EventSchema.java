package com.danielrocks.function.event;

import java.util.Date;
import java.util.Map;

/**
 * EventSchema
 */
public class EventSchema {

  public String topic;
  public String subject;
  public String eventType;
  public Date eventTime;
  public String id;
  public String dataVersion;
  public String metadataVersion;
  public Map<String, Object> data;
  
}
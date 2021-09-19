package org.apache.http;

public interface HttpConnectionMetrics {
  long getRequestCount();
  
  long getResponseCount();
  
  long getSentBytesCount();
  
  long getReceivedBytesCount();
  
  Object getMetric(String paramString);
  
  void reset();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\HttpConnectionMetrics.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
package org.fourthline.cling.model.message.control;

public interface ActionMessage {
  String getActionNamespace();
  
  boolean isBodyNonEmptyString();
  
  String getBodyString();
  
  void setBody(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\control\ActionMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
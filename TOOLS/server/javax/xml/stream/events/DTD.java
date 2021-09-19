package javax.xml.stream.events;

import java.util.List;

public interface DTD extends XMLEvent {
  String getDocumentTypeDeclaration();
  
  Object getProcessedDTD();
  
  List getNotations();
  
  List getEntities();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\stream\events\DTD.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
package javax.xml.stream.events;

public interface StartDocument extends XMLEvent {
  String getSystemId();
  
  String getCharacterEncodingScheme();
  
  boolean encodingSet();
  
  boolean isStandalone();
  
  boolean standaloneSet();
  
  String getVersion();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\stream\events\StartDocument.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
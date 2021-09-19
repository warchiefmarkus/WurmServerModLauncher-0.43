package javax.xml.stream.events;

public interface Namespace extends Attribute {
  String getPrefix();
  
  String getNamespaceURI();
  
  boolean isDefaultNamespaceDeclaration();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\stream\events\Namespace.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
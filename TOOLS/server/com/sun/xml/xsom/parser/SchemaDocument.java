package com.sun.xml.xsom.parser;

import com.sun.xml.xsom.XSSchema;
import java.util.Set;

public interface SchemaDocument {
  String getSystemId();
  
  String getTargetNamespace();
  
  XSSchema getSchema();
  
  Set<SchemaDocument> getReferencedDocuments();
  
  Set<SchemaDocument> getIncludedDocuments();
  
  Set<SchemaDocument> getImportedDocuments(String paramString);
  
  boolean includes(SchemaDocument paramSchemaDocument);
  
  boolean imports(SchemaDocument paramSchemaDocument);
  
  Set<SchemaDocument> getReferers();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\parser\SchemaDocument.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
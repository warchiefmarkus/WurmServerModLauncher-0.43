package com.sun.tools.xjc.api;

import java.io.IOException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;

public interface J2SJAXBModel extends JAXBModel {
  QName getXmlTypeName(Reference paramReference);
  
  void generateSchema(SchemaOutputResolver paramSchemaOutputResolver, ErrorListener paramErrorListener) throws IOException;
  
  void generateEpisodeFile(Result paramResult);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\J2SJAXBModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
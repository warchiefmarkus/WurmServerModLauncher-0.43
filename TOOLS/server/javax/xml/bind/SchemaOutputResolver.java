package javax.xml.bind;

import java.io.IOException;
import javax.xml.transform.Result;

public abstract class SchemaOutputResolver {
  public abstract Result createOutput(String paramString1, String paramString2) throws IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\SchemaOutputResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
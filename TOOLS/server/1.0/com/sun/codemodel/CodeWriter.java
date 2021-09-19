package 1.0.com.sun.codemodel;

import com.sun.codemodel.JPackage;
import java.io.IOException;
import java.io.OutputStream;

public interface CodeWriter {
  OutputStream open(JPackage paramJPackage, String paramString) throws IOException;
  
  void close() throws IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\CodeWriter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
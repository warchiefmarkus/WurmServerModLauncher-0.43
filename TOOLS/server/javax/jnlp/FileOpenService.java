package javax.jnlp;

import java.io.IOException;

public interface FileOpenService {
  FileContents openFileDialog(String paramString, String[] paramArrayOfString) throws IOException;
  
  FileContents[] openMultiFileDialog(String paramString, String[] paramArrayOfString) throws IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\FileOpenService.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
package javax.jnlp;

import java.io.IOException;
import java.io.InputStream;

public interface FileSaveService {
  FileContents saveFileDialog(String paramString1, String[] paramArrayOfString, InputStream paramInputStream, String paramString2) throws IOException;
  
  FileContents saveAsFileDialog(String paramString, String[] paramArrayOfString, FileContents paramFileContents) throws IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\FileSaveService.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
package javax.jnlp;

import java.io.File;
import java.io.IOException;

public interface ExtendedService {
  FileContents openFile(File paramFile) throws IOException;
  
  FileContents[] openFiles(File[] paramArrayOfFile) throws IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\ExtendedService.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
package javax.jnlp;

import java.awt.datatransfer.Transferable;

public interface ClipboardService {
  Transferable getContents();
  
  void setContents(Transferable paramTransferable);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\ClipboardService.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
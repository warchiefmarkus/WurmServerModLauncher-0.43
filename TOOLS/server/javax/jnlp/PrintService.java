package javax.jnlp;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;

public interface PrintService {
  PageFormat getDefaultPage();
  
  PageFormat showPageFormatDialog(PageFormat paramPageFormat);
  
  boolean print(Pageable paramPageable);
  
  boolean print(Printable paramPrintable);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\PrintService.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
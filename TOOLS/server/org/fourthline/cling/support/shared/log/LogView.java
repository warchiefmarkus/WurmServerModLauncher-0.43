package org.fourthline.cling.support.shared.log;

import java.util.List;
import org.fourthline.cling.support.shared.View;
import org.seamless.swing.logging.LogCategory;
import org.seamless.swing.logging.LogMessage;

public interface LogView extends View<LogView.Presenter> {
  void pushMessage(LogMessage paramLogMessage);
  
  void dispose();
  
  public static interface LogCategories extends List<LogCategory> {}
  
  public static interface Presenter {
    void init();
    
    void onExpand(LogMessage param1LogMessage);
    
    void pushMessage(LogMessage param1LogMessage);
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\shared\log\LogView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
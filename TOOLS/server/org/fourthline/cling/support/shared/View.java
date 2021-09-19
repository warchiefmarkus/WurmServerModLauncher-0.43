package org.fourthline.cling.support.shared;

import java.awt.Component;

public interface View<P> {
  Component asUIComponent();
  
  void setPresenter(P paramP);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\shared\View.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
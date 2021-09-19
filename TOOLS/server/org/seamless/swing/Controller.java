package org.seamless.swing;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.List;
import javax.swing.AbstractButton;

public interface Controller<V extends java.awt.Container> extends ActionListener, WindowListener {
  V getView();
  
  Controller getParentController();
  
  List<Controller> getSubControllers();
  
  void dispose();
  
  void registerEventListener(Class paramClass, EventListener paramEventListener);
  
  void fireEvent(Event paramEvent);
  
  void fireEventGlobal(Event paramEvent);
  
  void fireEvent(Event paramEvent, boolean paramBoolean);
  
  void registerAction(AbstractButton paramAbstractButton, DefaultAction paramDefaultAction);
  
  void registerAction(AbstractButton paramAbstractButton, String paramString, DefaultAction paramDefaultAction);
  
  void preActionExecute();
  
  void postActionExecute();
  
  void failedActionExecute();
  
  void finalActionExecute();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\Controller.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
package org.fourthline.cling.model;

import java.beans.PropertyChangeSupport;
import java.util.Collection;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.state.StateVariableValue;

public interface ServiceManager<T> {
  public static final String EVENTED_STATE_VARIABLES = "_EventedStateVariables";
  
  LocalService<T> getService();
  
  T getImplementation();
  
  void execute(Command<T> paramCommand) throws Exception;
  
  PropertyChangeSupport getPropertyChangeSupport();
  
  Collection<StateVariableValue> getCurrentState() throws Exception;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\ServiceManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
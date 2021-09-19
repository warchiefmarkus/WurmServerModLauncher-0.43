package org.controlsfx.control.action;

import java.lang.reflect.Method;

public interface AnnotatedActionFactory {
  AnnotatedAction createAction(ActionProxy paramActionProxy, Method paramMethod, Object paramObject);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\action\AnnotatedActionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
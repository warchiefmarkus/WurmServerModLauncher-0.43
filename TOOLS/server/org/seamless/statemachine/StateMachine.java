package org.seamless.statemachine;

public interface StateMachine<S> {
  public static final String METHOD_CURRENT_STATE = "getCurrentState";
  
  public static final String METHOD_FORCE_STATE = "forceState";
  
  S getCurrentState();
  
  void forceState(Class<? extends S> paramClass);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\statemachine\StateMachine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
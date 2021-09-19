package 1.0.com.sun.tools.xjc.generator.unmarshaller.automaton;

import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
import com.sun.tools.xjc.generator.unmarshaller.automaton.State;

public interface TransitionVisitor {
  void onEnterElement(Alphabet.EnterElement paramEnterElement, State paramState);
  
  void onLeaveElement(Alphabet.LeaveElement paramLeaveElement, State paramState);
  
  void onEnterAttribute(Alphabet.EnterAttribute paramEnterAttribute, State paramState);
  
  void onLeaveAttribute(Alphabet.LeaveAttribute paramLeaveAttribute, State paramState);
  
  void onInterleave(Alphabet.Interleave paramInterleave, State paramState);
  
  void onChild(Alphabet.Child paramChild, State paramState);
  
  void onDispatch(Alphabet.Dispatch paramDispatch, State paramState);
  
  void onSuper(Alphabet.SuperClass paramSuperClass, State paramState);
  
  void onExternal(Alphabet.External paramExternal, State paramState);
  
  void onBoundText(Alphabet.BoundText paramBoundText, State paramState);
  
  void onIgnoredText(Alphabet.IgnoredText paramIgnoredText, State paramState);
  
  void onEverythingElse(Alphabet.EverythingElse paramEverythingElse, State paramState);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\automaton\TransitionVisitor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
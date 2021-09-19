package 1.0.com.sun.tools.xjc.generator.unmarshaller.automaton;

import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;

public interface AlphabetVisitor {
  void onEnterElement(Alphabet.EnterElement paramEnterElement);
  
  void onLeaveElement(Alphabet.LeaveElement paramLeaveElement);
  
  void onEnterAttribute(Alphabet.EnterAttribute paramEnterAttribute);
  
  void onLeaveAttribute(Alphabet.LeaveAttribute paramLeaveAttribute);
  
  void onInterleave(Alphabet.Interleave paramInterleave);
  
  void onChild(Alphabet.Child paramChild);
  
  void onSuper(Alphabet.SuperClass paramSuperClass);
  
  void onDispatch(Alphabet.Dispatch paramDispatch);
  
  void onExternal(Alphabet.External paramExternal);
  
  void onBoundText(Alphabet.BoundText paramBoundText);
  
  void onIgnoredText(Alphabet.IgnoredText paramIgnoredText);
  
  void onEverythingElse(Alphabet.EverythingElse paramEverythingElse);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\automaton\AlphabetVisitor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
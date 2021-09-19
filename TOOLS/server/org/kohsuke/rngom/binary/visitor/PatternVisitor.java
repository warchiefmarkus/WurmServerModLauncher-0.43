package org.kohsuke.rngom.binary.visitor;

import org.kohsuke.rngom.binary.Pattern;
import org.kohsuke.rngom.nc.NameClass;
import org.relaxng.datatype.Datatype;

public interface PatternVisitor {
  void visitEmpty();
  
  void visitNotAllowed();
  
  void visitError();
  
  void visitAfter(Pattern paramPattern1, Pattern paramPattern2);
  
  void visitGroup(Pattern paramPattern1, Pattern paramPattern2);
  
  void visitInterleave(Pattern paramPattern1, Pattern paramPattern2);
  
  void visitChoice(Pattern paramPattern1, Pattern paramPattern2);
  
  void visitOneOrMore(Pattern paramPattern);
  
  void visitElement(NameClass paramNameClass, Pattern paramPattern);
  
  void visitAttribute(NameClass paramNameClass, Pattern paramPattern);
  
  void visitData(Datatype paramDatatype);
  
  void visitDataExcept(Datatype paramDatatype, Pattern paramPattern);
  
  void visitValue(Datatype paramDatatype, Object paramObject);
  
  void visitText();
  
  void visitList(Pattern paramPattern);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\visitor\PatternVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
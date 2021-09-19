package org.kohsuke.rngom.binary.visitor;

import org.kohsuke.rngom.binary.AfterPattern;
import org.kohsuke.rngom.binary.AttributePattern;
import org.kohsuke.rngom.binary.ChoicePattern;
import org.kohsuke.rngom.binary.DataExceptPattern;
import org.kohsuke.rngom.binary.DataPattern;
import org.kohsuke.rngom.binary.ElementPattern;
import org.kohsuke.rngom.binary.EmptyPattern;
import org.kohsuke.rngom.binary.ErrorPattern;
import org.kohsuke.rngom.binary.GroupPattern;
import org.kohsuke.rngom.binary.InterleavePattern;
import org.kohsuke.rngom.binary.ListPattern;
import org.kohsuke.rngom.binary.NotAllowedPattern;
import org.kohsuke.rngom.binary.OneOrMorePattern;
import org.kohsuke.rngom.binary.RefPattern;
import org.kohsuke.rngom.binary.TextPattern;
import org.kohsuke.rngom.binary.ValuePattern;

public interface PatternFunction {
  Object caseEmpty(EmptyPattern paramEmptyPattern);
  
  Object caseNotAllowed(NotAllowedPattern paramNotAllowedPattern);
  
  Object caseError(ErrorPattern paramErrorPattern);
  
  Object caseGroup(GroupPattern paramGroupPattern);
  
  Object caseInterleave(InterleavePattern paramInterleavePattern);
  
  Object caseChoice(ChoicePattern paramChoicePattern);
  
  Object caseOneOrMore(OneOrMorePattern paramOneOrMorePattern);
  
  Object caseElement(ElementPattern paramElementPattern);
  
  Object caseAttribute(AttributePattern paramAttributePattern);
  
  Object caseData(DataPattern paramDataPattern);
  
  Object caseDataExcept(DataExceptPattern paramDataExceptPattern);
  
  Object caseValue(ValuePattern paramValuePattern);
  
  Object caseText(TextPattern paramTextPattern);
  
  Object caseList(ListPattern paramListPattern);
  
  Object caseRef(RefPattern paramRefPattern);
  
  Object caseAfter(AfterPattern paramAfterPattern);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\visitor\PatternFunction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
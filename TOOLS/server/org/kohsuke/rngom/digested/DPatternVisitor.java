package org.kohsuke.rngom.digested;

public interface DPatternVisitor<V> {
  V onAttribute(DAttributePattern paramDAttributePattern);
  
  V onChoice(DChoicePattern paramDChoicePattern);
  
  V onData(DDataPattern paramDDataPattern);
  
  V onElement(DElementPattern paramDElementPattern);
  
  V onEmpty(DEmptyPattern paramDEmptyPattern);
  
  V onGrammar(DGrammarPattern paramDGrammarPattern);
  
  V onGroup(DGroupPattern paramDGroupPattern);
  
  V onInterleave(DInterleavePattern paramDInterleavePattern);
  
  V onList(DListPattern paramDListPattern);
  
  V onMixed(DMixedPattern paramDMixedPattern);
  
  V onNotAllowed(DNotAllowedPattern paramDNotAllowedPattern);
  
  V onOneOrMore(DOneOrMorePattern paramDOneOrMorePattern);
  
  V onOptional(DOptionalPattern paramDOptionalPattern);
  
  V onRef(DRefPattern paramDRefPattern);
  
  V onText(DTextPattern paramDTextPattern);
  
  V onValue(DValuePattern paramDValuePattern);
  
  V onZeroOrMore(DZeroOrMorePattern paramDZeroOrMorePattern);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DPatternVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
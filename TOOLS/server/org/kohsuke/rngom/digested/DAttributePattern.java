/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import org.kohsuke.rngom.nc.NameClass;
/*    */ 
/*    */ 
/*    */ public class DAttributePattern
/*    */   extends DXmlTokenPattern
/*    */ {
/*    */   public DAttributePattern(NameClass name) {
/* 10 */     super(name);
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 13 */     return visitor.onAttribute(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DAttributePattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import org.kohsuke.rngom.nc.NameClass;
/*    */ 
/*    */ 
/*    */ public class DElementPattern
/*    */   extends DXmlTokenPattern
/*    */ {
/*    */   public DElementPattern(NameClass name) {
/* 10 */     super(name);
/*    */   }
/*    */   
/*    */   public Object accept(DPatternVisitor visitor) {
/* 14 */     return visitor.onElement(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DElementPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
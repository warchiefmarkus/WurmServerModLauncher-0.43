/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import org.kohsuke.rngom.nc.NameClass;
/*    */ 
/*    */ 
/*    */ public abstract class DXmlTokenPattern
/*    */   extends DUnaryPattern
/*    */ {
/*    */   private final NameClass name;
/*    */   
/*    */   public DXmlTokenPattern(NameClass name) {
/* 12 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NameClass getName() {
/* 19 */     return this.name;
/*    */   }
/*    */   
/*    */   public final boolean isNullable() {
/* 23 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DXmlTokenPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
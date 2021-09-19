/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import org.kohsuke.rngom.parse.Context;
/*    */ 
/*    */ 
/*    */ public class DValuePattern
/*    */   extends DPattern
/*    */ {
/*    */   private String datatypeLibrary;
/*    */   private String type;
/*    */   private String value;
/*    */   private Context context;
/*    */   private String ns;
/*    */   
/*    */   public DValuePattern(String datatypeLibrary, String type, String value, Context context, String ns) {
/* 16 */     this.datatypeLibrary = datatypeLibrary;
/* 17 */     this.type = type;
/* 18 */     this.value = value;
/* 19 */     this.context = context;
/* 20 */     this.ns = ns;
/*    */   }
/*    */   
/*    */   public String getDatatypeLibrary() {
/* 24 */     return this.datatypeLibrary;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 28 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 32 */     return this.value;
/*    */   }
/*    */   
/*    */   public Context getContext() {
/* 36 */     return this.context;
/*    */   }
/*    */   
/*    */   public String getNs() {
/* 40 */     return this.ns;
/*    */   }
/*    */   
/*    */   public boolean isNullable() {
/* 44 */     return false;
/*    */   }
/*    */   
/*    */   public Object accept(DPatternVisitor visitor) {
/* 48 */     return visitor.onValue(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DValuePattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
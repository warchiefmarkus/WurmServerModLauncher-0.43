/*    */ package org.kohsuke.rngom.ast.util;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ public class LocatorImpl
/*    */   implements Locator, Location
/*    */ {
/*    */   private final String systemId;
/*    */   private final int lineNumber;
/*    */   private final int columnNumber;
/*    */   
/*    */   public LocatorImpl(String systemId, int lineNumber, int columnNumber) {
/* 15 */     this.systemId = systemId;
/* 16 */     this.lineNumber = lineNumber;
/* 17 */     this.columnNumber = columnNumber;
/*    */   }
/*    */   
/*    */   public String getPublicId() {
/* 21 */     return null;
/*    */   }
/*    */   
/*    */   public String getSystemId() {
/* 25 */     return this.systemId;
/*    */   }
/*    */   
/*    */   public int getLineNumber() {
/* 29 */     return this.lineNumber;
/*    */   }
/*    */   
/*    */   public int getColumnNumber() {
/* 33 */     return this.columnNumber;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\as\\util\LocatorImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
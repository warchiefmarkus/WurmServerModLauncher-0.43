/*    */ package org.kohsuke.rngom.parse.xml;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import org.relaxng.datatype.ValidationContext;
/*    */ import org.xml.sax.DTDHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public abstract class DtdContext
/*    */   implements DTDHandler, ValidationContext {
/*    */   private final Hashtable notationTable;
/*    */   private final Hashtable unparsedEntityTable;
/*    */   
/*    */   public DtdContext() {
/* 14 */     this.notationTable = new Hashtable<Object, Object>();
/* 15 */     this.unparsedEntityTable = new Hashtable<Object, Object>();
/*    */   }
/*    */   
/*    */   public DtdContext(DtdContext dc) {
/* 19 */     this.notationTable = dc.notationTable;
/* 20 */     this.unparsedEntityTable = dc.unparsedEntityTable;
/*    */   }
/*    */ 
/*    */   
/*    */   public void notationDecl(String name, String publicId, String systemId) throws SAXException {
/* 25 */     this.notationTable.put(name, name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
/* 34 */     this.unparsedEntityTable.put(name, name);
/*    */   }
/*    */   
/*    */   public boolean isNotation(String notationName) {
/* 38 */     return (this.notationTable.get(notationName) != null);
/*    */   }
/*    */   
/*    */   public boolean isUnparsedEntity(String entityName) {
/* 42 */     return (this.unparsedEntityTable.get(entityName) != null);
/*    */   }
/*    */   
/*    */   public void clearDtdContext() {
/* 46 */     this.notationTable.clear();
/* 47 */     this.unparsedEntityTable.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\xml\DtdContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
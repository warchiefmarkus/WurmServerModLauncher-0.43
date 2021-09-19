/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class UName
/*    */ {
/*    */   private final String nsUri;
/*    */   private final String localName;
/*    */   private final String qname;
/*    */   
/*    */   public UName(String _nsUri, String _localName, String _qname) {
/* 16 */     if (_nsUri == null || _localName == null || _qname == null) {
/* 17 */       throw new NullPointerException(_nsUri + " " + _localName + " " + _qname);
/*    */     }
/* 19 */     this.nsUri = _nsUri.intern();
/* 20 */     this.localName = _localName.intern();
/* 21 */     this.qname = _qname.intern();
/*    */   }
/*    */   
/*    */   public UName(String nsUri, String localName) {
/* 25 */     this(nsUri, localName, localName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 32 */     return this.localName;
/* 33 */   } public String getNamespaceURI() { return this.nsUri; } public String getQualifiedName() {
/* 34 */     return this.qname;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public static final Comparator comparator = (Comparator)new Object();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\UName.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
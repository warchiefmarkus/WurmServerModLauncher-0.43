/*    */ package com.sun.xml.xsom.impl;
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
/* 39 */   public static final Comparator comparator = new Comparator() {
/*    */       public int compare(Object o1, Object o2) {
/* 41 */         UName lhs = (UName)o1;
/* 42 */         UName rhs = (UName)o2;
/* 43 */         int r = lhs.nsUri.compareTo(rhs.nsUri);
/* 44 */         if (r != 0) return r; 
/* 45 */         return lhs.localName.compareTo(rhs.localName);
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\UName.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
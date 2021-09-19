/*    */ package com.sun.xml.bind.v2.runtime;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NameList
/*    */ {
/*    */   public final String[] namespaceURIs;
/*    */   public final boolean[] nsUriCannotBeDefaulted;
/*    */   public final String[] localNames;
/*    */   public final int numberOfElementNames;
/*    */   public final int numberOfAttributeNames;
/*    */   
/*    */   public NameList(String[] namespaceURIs, boolean[] nsUriCannotBeDefaulted, String[] localNames, int numberElementNames, int numberAttributeNames) {
/* 78 */     this.namespaceURIs = namespaceURIs;
/* 79 */     this.nsUriCannotBeDefaulted = nsUriCannotBeDefaulted;
/* 80 */     this.localNames = localNames;
/* 81 */     this.numberOfElementNames = numberElementNames;
/* 82 */     this.numberOfAttributeNames = numberAttributeNames;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\NameList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
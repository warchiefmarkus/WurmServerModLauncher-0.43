/*    */ package org.seamless.xhtml;
/*    */ 
/*    */ import java.net.URI;
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
/*    */ public class Href
/*    */ {
/*    */   private URI uri;
/*    */   
/*    */   public Href(URI uri) {
/* 27 */     this.uri = uri;
/*    */   }
/*    */   
/*    */   public URI getURI() {
/* 31 */     return this.uri;
/*    */   }
/*    */   
/*    */   public static Href fromString(String string) {
/* 35 */     if (string == null) return null; 
/* 36 */     string = string.replaceAll(" ", "%20");
/* 37 */     return new Href(URI.create(string));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return getURI().toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 47 */     if (this == o) return true; 
/* 48 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 50 */     Href href = (Href)o;
/*    */     
/* 52 */     if (!this.uri.equals(href.uri)) return false;
/*    */     
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 59 */     return this.uri.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xhtml\Href.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
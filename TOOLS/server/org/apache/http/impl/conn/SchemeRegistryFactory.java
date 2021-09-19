/*    */ package org.apache.http.impl.conn;
/*    */ 
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.conn.scheme.PlainSocketFactory;
/*    */ import org.apache.http.conn.scheme.Scheme;
/*    */ import org.apache.http.conn.scheme.SchemeRegistry;
/*    */ import org.apache.http.conn.scheme.SchemeSocketFactory;
/*    */ import org.apache.http.conn.ssl.SSLSocketFactory;
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
/*    */ @ThreadSafe
/*    */ public final class SchemeRegistryFactory
/*    */ {
/*    */   public static SchemeRegistry createDefault() {
/* 46 */     SchemeRegistry registry = new SchemeRegistry();
/* 47 */     registry.register(new Scheme("http", 80, (SchemeSocketFactory)PlainSocketFactory.getSocketFactory()));
/*    */     
/* 49 */     registry.register(new Scheme("https", 443, (SchemeSocketFactory)SSLSocketFactory.getSocketFactory()));
/*    */     
/* 51 */     return registry;
/*    */   }
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
/*    */   public static SchemeRegistry createSystemDefault() {
/* 79 */     SchemeRegistry registry = new SchemeRegistry();
/* 80 */     registry.register(new Scheme("http", 80, (SchemeSocketFactory)PlainSocketFactory.getSocketFactory()));
/*    */     
/* 82 */     registry.register(new Scheme("https", 443, (SchemeSocketFactory)SSLSocketFactory.getSystemSocketFactory()));
/*    */     
/* 84 */     return registry;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\SchemeRegistryFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
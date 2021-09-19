/*    */ package org.apache.http.impl.conn;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import org.apache.http.conn.DnsResolver;
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
/*    */ public class SystemDefaultDnsResolver
/*    */   implements DnsResolver
/*    */ {
/*    */   public InetAddress[] resolve(String host) throws UnknownHostException {
/* 45 */     return InetAddress.getAllByName(host);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\SystemDefaultDnsResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
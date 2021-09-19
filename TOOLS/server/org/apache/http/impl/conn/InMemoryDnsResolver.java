/*    */ package org.apache.http.impl.conn;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class InMemoryDnsResolver
/*    */   implements DnsResolver
/*    */ {
/* 47 */   private final Log log = LogFactory.getLog(InMemoryDnsResolver.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Map<String, InetAddress[]> dnsMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InMemoryDnsResolver() {
/* 60 */     this.dnsMap = (Map)new ConcurrentHashMap<String, InetAddress>();
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
/*    */   public void add(String host, InetAddress... ips) {
/* 74 */     if (host == null) {
/* 75 */       throw new IllegalArgumentException("Host name may not be null");
/*    */     }
/* 77 */     if (ips == null) {
/* 78 */       throw new IllegalArgumentException("Array of IP addresses may not be null");
/*    */     }
/* 80 */     this.dnsMap.put(host, ips);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InetAddress[] resolve(String host) throws UnknownHostException {
/* 87 */     InetAddress[] resolvedAddresses = this.dnsMap.get(host);
/* 88 */     if (this.log.isInfoEnabled()) {
/* 89 */       this.log.info("Resolving " + host + " to " + Arrays.deepToString((Object[])resolvedAddresses));
/*    */     }
/* 91 */     if (resolvedAddresses == null) {
/* 92 */       throw new UnknownHostException(host + " cannot be resolved");
/*    */     }
/* 94 */     return resolvedAddresses;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\InMemoryDnsResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
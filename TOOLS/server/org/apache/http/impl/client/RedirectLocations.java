/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.apache.http.annotation.NotThreadSafe;
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
/*    */ @NotThreadSafe
/*    */ public class RedirectLocations
/*    */ {
/* 52 */   private final Set<URI> unique = new HashSet<URI>();
/* 53 */   private final List<URI> all = new ArrayList<URI>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean contains(URI uri) {
/* 60 */     return this.unique.contains(uri);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(URI uri) {
/* 67 */     this.unique.add(uri);
/* 68 */     this.all.add(uri);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean remove(URI uri) {
/* 75 */     boolean removed = this.unique.remove(uri);
/* 76 */     if (removed) {
/* 77 */       Iterator<URI> it = this.all.iterator();
/* 78 */       while (it.hasNext()) {
/* 79 */         URI current = it.next();
/* 80 */         if (current.equals(uri)) {
/* 81 */           it.remove();
/*    */         }
/*    */       } 
/*    */     } 
/* 85 */     return removed;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<URI> getAll() {
/* 96 */     return new ArrayList<URI>(this.all);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\RedirectLocations.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
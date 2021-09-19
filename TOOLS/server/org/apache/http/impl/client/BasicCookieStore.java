/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.TreeSet;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.client.CookieStore;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieIdentityComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class BasicCookieStore
/*     */   implements CookieStore, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7581093305228232025L;
/*     */   @GuardedBy("this")
/*  59 */   private final TreeSet<Cookie> cookies = new TreeSet<Cookie>((Comparator<? super Cookie>)new CookieIdentityComparator());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addCookie(Cookie cookie) {
/*  73 */     if (cookie != null) {
/*     */       
/*  75 */       this.cookies.remove(cookie);
/*  76 */       if (!cookie.isExpired(new Date())) {
/*  77 */         this.cookies.add(cookie);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addCookies(Cookie[] cookies) {
/*  93 */     if (cookies != null) {
/*  94 */       for (Cookie cooky : cookies) {
/*  95 */         addCookie(cooky);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized List<Cookie> getCookies() {
/* 108 */     return new ArrayList<Cookie>(this.cookies);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean clearExpired(Date date) {
/* 120 */     if (date == null) {
/* 121 */       return false;
/*     */     }
/* 123 */     boolean removed = false;
/* 124 */     for (Iterator<Cookie> it = this.cookies.iterator(); it.hasNext();) {
/* 125 */       if (((Cookie)it.next()).isExpired(date)) {
/* 126 */         it.remove();
/* 127 */         removed = true;
/*     */       } 
/*     */     } 
/* 130 */     return removed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void clear() {
/* 137 */     this.cookies.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String toString() {
/* 142 */     return this.cookies.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\BasicCookieStore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
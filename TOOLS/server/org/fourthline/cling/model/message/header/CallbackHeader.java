/*     */ package org.fourthline.cling.model.message.header;
/*     */ 
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public class CallbackHeader
/*     */   extends UpnpHeader<List<URL>>
/*     */ {
/*  31 */   private static final Logger log = Logger.getLogger(CallbackHeader.class.getName());
/*     */   
/*     */   public CallbackHeader() {
/*  34 */     setValue(new ArrayList<>());
/*     */   }
/*     */   
/*     */   public CallbackHeader(List<URL> urls) {
/*  38 */     this();
/*  39 */     getValue().addAll(urls);
/*     */   }
/*     */   
/*     */   public CallbackHeader(URL url) {
/*  43 */     this();
/*  44 */     getValue().add(url);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setString(String s) throws InvalidHeaderException {
/*  49 */     if (s.length() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  54 */     if (!s.contains("<") || !s.contains(">")) {
/*  55 */       throw new InvalidHeaderException("URLs not in brackets: " + s);
/*     */     }
/*     */     
/*  58 */     s = s.replaceAll("<", "");
/*  59 */     String[] split = s.split(">");
/*     */     try {
/*  61 */       List<URL> urls = new ArrayList<>();
/*  62 */       for (String sp : split) {
/*  63 */         sp = sp.trim();
/*     */         
/*  65 */         if (!sp.startsWith("http://")) {
/*  66 */           log.warning("Discarding non-http callback URL: " + sp);
/*     */         }
/*     */         else {
/*     */           
/*  70 */           URL url = new URL(sp);
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
/*     */           try {
/*  82 */             url.toURI();
/*  83 */           } catch (URISyntaxException ex) {
/*  84 */             log.log(Level.WARNING, "Discarding callback URL, not a valid URI on this platform: " + url, ex);
/*     */           } 
/*     */ 
/*     */           
/*  88 */           urls.add(url);
/*     */         } 
/*  90 */       }  setValue(urls);
/*  91 */     } catch (MalformedURLException ex) {
/*  92 */       throw new InvalidHeaderException("Can't parse callback URLs from '" + s + "': " + ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getString() {
/*  97 */     StringBuilder s = new StringBuilder();
/*  98 */     for (URL url : getValue()) {
/*  99 */       s.append("<").append(url.toString()).append(">");
/*     */     }
/* 101 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\CallbackHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
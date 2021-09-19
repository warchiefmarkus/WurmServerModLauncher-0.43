/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
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
/*     */ public class IllegalAnnotationException
/*     */   extends JAXBException
/*     */ {
/*     */   private final List<List<Location>> pos;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public IllegalAnnotationException(String message, Locatable src) {
/*  65 */     super(message);
/*  66 */     this.pos = build(new Locatable[] { src });
/*     */   }
/*     */   
/*     */   public IllegalAnnotationException(String message, Annotation src) {
/*  70 */     this(message, cast(src));
/*     */   }
/*     */   
/*     */   public IllegalAnnotationException(String message, Locatable src1, Locatable src2) {
/*  74 */     super(message);
/*  75 */     this.pos = build(new Locatable[] { src1, src2 });
/*     */   }
/*     */   
/*     */   public IllegalAnnotationException(String message, Annotation src1, Annotation src2) {
/*  79 */     this(message, cast(src1), cast(src2));
/*     */   }
/*     */   
/*     */   public IllegalAnnotationException(String message, Annotation src1, Locatable src2) {
/*  83 */     this(message, cast(src1), src2);
/*     */   }
/*     */   
/*     */   public IllegalAnnotationException(String message, Throwable cause, Locatable src) {
/*  87 */     super(message, cause);
/*  88 */     this.pos = build(new Locatable[] { src });
/*     */   }
/*     */   
/*     */   private static Locatable cast(Annotation a) {
/*  92 */     if (a instanceof Locatable) {
/*  93 */       return (Locatable)a;
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */   
/*     */   private List<List<Location>> build(Locatable... srcs) {
/*  99 */     List<List<Location>> r = new ArrayList<List<Location>>();
/* 100 */     for (Locatable l : srcs) {
/* 101 */       if (l != null) {
/* 102 */         List<Location> ll = convert(l);
/* 103 */         if (ll != null && !ll.isEmpty())
/* 104 */           r.add(ll); 
/*     */       } 
/*     */     } 
/* 107 */     return Collections.unmodifiableList(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Location> convert(Locatable src) {
/* 114 */     if (src == null) return null;
/*     */     
/* 116 */     List<Location> r = new ArrayList<Location>();
/* 117 */     for (; src != null; src = src.getUpstream())
/* 118 */       r.add(src.getLocation()); 
/* 119 */     return Collections.unmodifiableList(r);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<List<Location>> getSourcePos() {
/* 175 */     return this.pos;
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
/*     */   public String toString() {
/* 188 */     StringBuilder sb = new StringBuilder(getMessage());
/*     */     
/* 190 */     for (List<Location> locs : this.pos) {
/* 191 */       sb.append("\n\tthis problem is related to the following location:");
/* 192 */       for (Location loc : locs) {
/* 193 */         sb.append("\n\t\tat ").append(loc.toString());
/*     */       }
/*     */     } 
/* 196 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\IllegalAnnotationException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.sun.tools.xjc.util;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.activation.MimeType;
/*     */ import javax.activation.MimeTypeParseException;
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
/*     */ public class MimeTypeRange
/*     */ {
/*     */   public final String majorType;
/*     */   public final String subType;
/*  56 */   public final Map<String, String> parameters = new HashMap<String, String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float q;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<MimeTypeRange> parseRanges(String s) throws ParseException {
/*  72 */     StringCutter cutter = new StringCutter(s, true);
/*  73 */     List<MimeTypeRange> r = new ArrayList<MimeTypeRange>();
/*  74 */     while (cutter.length() > 0) {
/*  75 */       r.add(new MimeTypeRange(cutter));
/*     */     }
/*  77 */     return r;
/*     */   }
/*     */   
/*     */   public MimeTypeRange(String s) throws ParseException {
/*  81 */     this(new StringCutter(s, true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static MimeTypeRange create(String s) {
/*     */     try {
/*  89 */       return new MimeTypeRange(s);
/*  90 */     } catch (ParseException e) {
/*     */       
/*  92 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MimeTypeRange(StringCutter cutter) throws ParseException {
/* 101 */     this.majorType = cutter.until("/");
/* 102 */     cutter.next("/");
/* 103 */     this.subType = cutter.until("[;,]");
/*     */     
/* 105 */     float q = 1.0F;
/*     */     
/* 107 */     while (cutter.length() > 0) {
/* 108 */       String value, sep = cutter.next("[;,]");
/* 109 */       if (sep.equals(",")) {
/*     */         break;
/*     */       }
/* 112 */       String key = cutter.until("=");
/* 113 */       cutter.next("=");
/*     */       
/* 115 */       char ch = cutter.peek();
/* 116 */       if (ch == '"') {
/*     */         
/* 118 */         cutter.next("\"");
/* 119 */         value = cutter.until("\"");
/* 120 */         cutter.next("\"");
/*     */       } else {
/* 122 */         value = cutter.until("[;,]");
/*     */       } 
/*     */       
/* 125 */       if (key.equals("q")) {
/* 126 */         q = Float.parseFloat(value); continue;
/*     */       } 
/* 128 */       this.parameters.put(key, value);
/*     */     } 
/*     */ 
/*     */     
/* 132 */     this.q = q;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType toMimeType() throws MimeTypeParseException {
/* 138 */     return new MimeType(toString());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 142 */     StringBuilder sb = new StringBuilder(this.majorType + '/' + this.subType);
/* 143 */     if (this.q != 1.0F) {
/* 144 */       sb.append("; q=").append(this.q);
/*     */     }
/* 146 */     for (Map.Entry<String, String> p : this.parameters.entrySet())
/*     */     {
/* 148 */       sb.append("; ").append(p.getKey()).append('=').append(p.getValue());
/*     */     }
/* 150 */     return sb.toString();
/*     */   }
/*     */   
/* 153 */   public static final MimeTypeRange ALL = create("*/*");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MimeTypeRange merge(Collection<MimeTypeRange> types) {
/* 159 */     if (types.size() == 0) throw new IllegalArgumentException(); 
/* 160 */     if (types.size() == 1) return types.iterator().next();
/*     */     
/* 162 */     String majorType = null;
/* 163 */     for (MimeTypeRange mt : types) {
/* 164 */       if (majorType == null) majorType = mt.majorType; 
/* 165 */       if (!majorType.equals(mt.majorType)) {
/* 166 */         return ALL;
/*     */       }
/*     */     } 
/* 169 */     return create(majorType + "/*");
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws ParseException {
/* 173 */     for (MimeTypeRange m : parseRanges(args[0]))
/* 174 */       System.out.println(m.toString()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xj\\util\MimeTypeRange.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
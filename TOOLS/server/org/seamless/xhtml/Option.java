/*     */ package org.seamless.xhtml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class Option
/*     */ {
/*     */   private String key;
/*     */   private String[] values;
/*     */   
/*     */   public Option(String key, String[] values) {
/*  31 */     this.key = key;
/*  32 */     this.values = values;
/*     */   }
/*     */   
/*     */   public static Option[] fromString(String string) {
/*  36 */     if (string == null || string.length() == 0) return new Option[0];
/*     */     
/*  38 */     List<Option> options = new ArrayList<Option>();
/*     */ 
/*     */     
/*     */     try {
/*  42 */       String[] fields = string.split(";");
/*  43 */       for (String field : fields) {
/*  44 */         field = field.trim();
/*  45 */         if (field.contains(":")) {
/*     */           
/*  47 */           String[] keyValues = field.split(":");
/*  48 */           if (keyValues.length == 2) {
/*     */             
/*  50 */             String key = keyValues[0].trim();
/*  51 */             String[] values = keyValues[1].split(",");
/*  52 */             List<String> cleanValues = new ArrayList<String>();
/*  53 */             for (String s : values) {
/*  54 */               String value = s.trim();
/*  55 */               if (value.length() > 0) {
/*  56 */                 cleanValues.add(value);
/*     */               }
/*     */             } 
/*  59 */             options.add(new Option(key, cleanValues.<String>toArray(new String[cleanValues.size()])));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  64 */       return options.<Option>toArray(new Option[options.size()]);
/*  65 */     } catch (Exception ex) {
/*  66 */       throw new IllegalArgumentException("Can't parse options string: " + string, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  71 */     return this.key;
/*     */   }
/*     */   
/*     */   public String[] getValues() {
/*  75 */     return this.values;
/*     */   }
/*     */   
/*     */   public boolean isTrue() {
/*  79 */     return ((getValues()).length == 1 && getValues()[0].toLowerCase().equals("true"));
/*     */   }
/*     */   
/*     */   public boolean isFalse() {
/*  83 */     return ((getValues()).length == 1 && getValues()[0].toLowerCase().equals("false"));
/*     */   }
/*     */   
/*     */   public String getFirstValue() {
/*  87 */     return getValues()[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  92 */     StringBuilder sb = new StringBuilder();
/*  93 */     sb.append(getKey()).append(": ");
/*  94 */     Iterator<String> it = Arrays.<String>asList(getValues()).iterator();
/*  95 */     while (it.hasNext()) {
/*  96 */       sb.append(it.next());
/*  97 */       if (it.hasNext()) sb.append(", "); 
/*     */     } 
/*  99 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 104 */     if (this == o) return true; 
/* 105 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 107 */     Option that = (Option)o;
/*     */     
/* 109 */     if (!this.key.equals(that.key)) return false; 
/* 110 */     if (!Arrays.equals((Object[])this.values, (Object[])that.values)) return false;
/*     */     
/* 112 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 117 */     int result = this.key.hashCode();
/* 118 */     result = 31 * result + Arrays.hashCode((Object[])this.values);
/* 119 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xhtml\Option.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
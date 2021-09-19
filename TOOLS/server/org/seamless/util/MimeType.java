/*     */ package org.seamless.util;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
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
/*     */ public class MimeType
/*     */ {
/*     */   public static final String WILDCARD = "*";
/*     */   private String type;
/*     */   private String subtype;
/*     */   private Map<String, String> parameters;
/*     */   
/*     */   public MimeType() {
/*  35 */     this("*", "*");
/*     */   }
/*     */   
/*     */   public MimeType(String type, String subtype, Map<String, String> parameters) {
/*  39 */     this.type = (type == null) ? "*" : type;
/*  40 */     this.subtype = (subtype == null) ? "*" : subtype;
/*  41 */     if (parameters == null) {
/*  42 */       this.parameters = Collections.EMPTY_MAP;
/*     */     } else {
/*  44 */       Map<String, String> map = new TreeMap<String, String>(new Comparator<String>() {
/*     */             public int compare(String o1, String o2) {
/*  46 */               return o1.compareToIgnoreCase(o2);
/*     */             }
/*     */           });
/*  49 */       for (Map.Entry<String, String> e : parameters.entrySet()) {
/*  50 */         map.put(e.getKey(), e.getValue());
/*     */       }
/*  52 */       this.parameters = Collections.unmodifiableMap(map);
/*     */     } 
/*     */   }
/*     */   
/*     */   public MimeType(String type, String subtype) {
/*  57 */     this(type, subtype, Collections.EMPTY_MAP);
/*     */   }
/*     */   
/*     */   public String getType() {
/*  61 */     return this.type;
/*     */   }
/*     */   
/*     */   public boolean isWildcardType() {
/*  65 */     return getType().equals("*");
/*     */   }
/*     */   
/*     */   public String getSubtype() {
/*  69 */     return this.subtype;
/*     */   }
/*     */   
/*     */   public boolean isWildcardSubtype() {
/*  73 */     return getSubtype().equals("*");
/*     */   }
/*     */   
/*     */   public Map<String, String> getParameters() {
/*  77 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public boolean isCompatible(MimeType other) {
/*  81 */     if (other == null)
/*  82 */       return false; 
/*  83 */     if (this.type.equals("*") || other.type.equals("*"))
/*  84 */       return true; 
/*  85 */     if (this.type.equalsIgnoreCase(other.type) && (this.subtype.equals("*") || other.subtype.equals("*"))) {
/*  86 */       return true;
/*     */     }
/*  88 */     return (this.type.equalsIgnoreCase(other.type) && this.subtype.equalsIgnoreCase(other.subtype));
/*     */   }
/*     */   
/*     */   public static MimeType valueOf(String stringValue) throws IllegalArgumentException {
/*  92 */     if (stringValue == null) throw new IllegalArgumentException("String value is null");
/*     */     
/*  94 */     String params = null;
/*  95 */     int semicolonIndex = stringValue.indexOf(";");
/*  96 */     if (semicolonIndex > -1) {
/*  97 */       params = stringValue.substring(semicolonIndex + 1).trim();
/*  98 */       stringValue = stringValue.substring(0, semicolonIndex);
/*     */     } 
/* 100 */     String major = null;
/* 101 */     String subtype = null;
/* 102 */     String[] paths = stringValue.split("/");
/*     */     
/* 104 */     if (paths.length < 2 && stringValue.equals("*")) {
/*     */       
/* 106 */       major = "*";
/* 107 */       subtype = "*";
/*     */     }
/* 109 */     else if (paths.length == 2) {
/*     */       
/* 111 */       major = paths[0].trim();
/* 112 */       subtype = paths[1].trim();
/*     */     }
/* 114 */     else if (paths.length != 2) {
/*     */       
/* 116 */       throw new IllegalArgumentException("Error parsing string: " + stringValue);
/*     */     } 
/*     */     
/* 119 */     if (params != null && params.length() > 0) {
/* 120 */       HashMap<String, String> map = new HashMap<String, String>();
/*     */       
/* 122 */       int start = 0;
/*     */       
/* 124 */       while (start < params.length()) {
/* 125 */         start = readParamsIntoMap(map, params, start);
/*     */       }
/* 127 */       return new MimeType(major, subtype, map);
/*     */     } 
/* 129 */     return new MimeType(major, subtype);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int readParamsIntoMap(Map<String, String> map, String params, int start) {
/* 134 */     boolean quote = false;
/* 135 */     boolean backslash = false;
/*     */     
/* 137 */     int end = getEnd(params, start);
/* 138 */     String name = params.substring(start, end).trim();
/* 139 */     if (end < params.length() && params.charAt(end) == '=') end++;
/*     */     
/* 141 */     StringBuilder buffer = new StringBuilder(params.length() - end);
/* 142 */     int i = end;
/* 143 */     for (; i < params.length(); i++) {
/* 144 */       char c = params.charAt(i);
/*     */       
/* 146 */       switch (c) {
/*     */         case '"':
/* 148 */           if (backslash) {
/* 149 */             backslash = false;
/* 150 */             buffer.append(c); break;
/*     */           } 
/* 152 */           quote = !quote;
/*     */           break;
/*     */ 
/*     */         
/*     */         case '\\':
/* 157 */           if (backslash) {
/* 158 */             backslash = false;
/* 159 */             buffer.append(c); break;
/*     */           } 
/* 161 */           backslash = true;
/*     */           break;
/*     */ 
/*     */         
/*     */         case ';':
/* 166 */           if (!quote) {
/* 167 */             String str = buffer.toString().trim();
/* 168 */             map.put(name, str);
/* 169 */             return i + 1;
/*     */           } 
/* 171 */           buffer.append(c);
/*     */           break;
/*     */ 
/*     */         
/*     */         default:
/* 176 */           buffer.append(c);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 181 */     String value = buffer.toString().trim();
/* 182 */     map.put(name, value);
/* 183 */     return i;
/*     */   }
/*     */   
/*     */   protected static int getEnd(String params, int start) {
/* 187 */     int equals = params.indexOf('=', start);
/* 188 */     int semicolon = params.indexOf(';', start);
/* 189 */     if (equals == -1 && semicolon == -1) return params.length(); 
/* 190 */     if (equals == -1) return semicolon; 
/* 191 */     if (semicolon == -1) return equals; 
/* 192 */     return (equals < semicolon) ? equals : semicolon;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 197 */     if (this == o) return true; 
/* 198 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 200 */     MimeType mimeType = (MimeType)o;
/*     */     
/* 202 */     if ((this.parameters != null) ? !this.parameters.equals(mimeType.parameters) : (mimeType.parameters != null)) return false; 
/* 203 */     if (!this.subtype.equalsIgnoreCase(mimeType.subtype)) return false; 
/* 204 */     if (!this.type.equalsIgnoreCase(mimeType.type)) return false;
/*     */     
/* 206 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 211 */     int result = this.type.toLowerCase().hashCode();
/* 212 */     result = 31 * result + this.subtype.toLowerCase().hashCode();
/* 213 */     result = 31 * result + ((this.parameters != null) ? this.parameters.hashCode() : 0);
/* 214 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 219 */     StringBuilder sb = new StringBuilder();
/* 220 */     sb.append(toStringNoParameters());
/* 221 */     if (getParameters() != null || getParameters().size() > 0) {
/* 222 */       for (String name : getParameters().keySet()) {
/* 223 */         sb.append(";").append(name).append("=\"").append(getParameters().get(name)).append("\"");
/*     */       }
/*     */     }
/* 226 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String toStringNoParameters() {
/* 230 */     return getType() + "/" + getSubtype();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\MimeType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
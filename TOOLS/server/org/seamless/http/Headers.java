/*     */ package org.seamless.http;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class Headers
/*     */   implements Map<String, List<String>>
/*     */ {
/*     */   static final byte CR = 13;
/*     */   static final byte LF = 10;
/*  42 */   final Map<String, List<String>> map = new HashMap<String, List<String>>(32);
/*     */ 
/*     */   
/*     */   private boolean normalizeHeaders = true;
/*     */ 
/*     */   
/*     */   public Headers(Map<String, List<String>> map) {
/*  49 */     putAll(map);
/*     */   }
/*     */   
/*     */   public Headers(ByteArrayInputStream inputStream) {
/*  53 */     StringBuilder sb = new StringBuilder(256);
/*  54 */     Headers headers = new Headers();
/*  55 */     String line = readLine(sb, inputStream);
/*  56 */     String lastHeader = null;
/*  57 */     if (line.length() != 0) {
/*     */       do {
/*  59 */         char firstChar = line.charAt(0);
/*  60 */         if (lastHeader != null && (firstChar == ' ' || firstChar == '\t')) {
/*  61 */           List<String> current = headers.get(lastHeader);
/*  62 */           int lastPos = current.size() - 1;
/*  63 */           String newString = (String)current.get(lastPos) + line.trim();
/*  64 */           current.set(lastPos, newString);
/*     */         } else {
/*  66 */           String[] header = splitHeader(line);
/*  67 */           headers.add(header[0], header[1]);
/*  68 */           lastHeader = header[0];
/*     */         } 
/*     */         
/*  71 */         sb.delete(0, sb.length());
/*  72 */         line = readLine(sb, inputStream);
/*  73 */       } while (line.length() != 0);
/*     */     }
/*  75 */     putAll(headers);
/*     */   }
/*     */   
/*     */   public Headers(boolean normalizeHeaders) {
/*  79 */     this.normalizeHeaders = normalizeHeaders;
/*     */   }
/*     */   
/*     */   public int size() {
/*  83 */     return this.map.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  87 */     return this.map.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  91 */     return (key != null && key instanceof String && this.map.containsKey(normalize((String)key)));
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  95 */     return this.map.containsValue(value);
/*     */   }
/*     */   
/*     */   public List<String> get(Object key) {
/*  99 */     return this.map.get(normalize((String)key));
/*     */   }
/*     */   
/*     */   public List<String> put(String key, List<String> value) {
/* 103 */     return this.map.put(normalize(key), value);
/*     */   }
/*     */   
/*     */   public List<String> remove(Object key) {
/* 107 */     return this.map.remove(normalize((String)key));
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends List<String>> t) {
/* 112 */     for (Map.Entry<? extends String, ? extends List<String>> entry : t.entrySet()) {
/* 113 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/* 118 */     this.map.clear();
/*     */   }
/*     */   
/*     */   public Set<String> keySet() {
/* 122 */     return this.map.keySet();
/*     */   }
/*     */   
/*     */   public Collection<List<String>> values() {
/* 126 */     return this.map.values();
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<String, List<String>>> entrySet() {
/* 130 */     return this.map.entrySet();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 134 */     return this.map.equals(o);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 138 */     return this.map.hashCode();
/*     */   }
/*     */   
/*     */   public String getFirstHeader(String key) {
/* 142 */     List<String> l = this.map.get(normalize(key));
/* 143 */     return (l != null && l.size() > 0) ? l.get(0) : null;
/*     */   }
/*     */   
/*     */   public void add(String key, String value) {
/* 147 */     String k = normalize(key);
/* 148 */     List<String> l = this.map.get(k);
/* 149 */     if (l == null) {
/* 150 */       l = new LinkedList<String>();
/* 151 */       this.map.put(k, l);
/*     */     } 
/* 153 */     l.add(value);
/*     */   }
/*     */   
/*     */   public void set(String key, String value) {
/* 157 */     LinkedList<String> l = new LinkedList<String>();
/* 158 */     l.add(value);
/* 159 */     put(key, l);
/*     */   }
/*     */   
/*     */   private String normalize(String key) {
/* 163 */     String result = key;
/*     */     
/* 165 */     if (this.normalizeHeaders) {
/* 166 */       if (key == null) return null; 
/* 167 */       if (key.length() == 0) return key;
/*     */       
/* 169 */       char[] b = key.toCharArray();
/* 170 */       int caseDiff = 32;
/*     */       
/* 172 */       if (b[0] >= 'a' && b[0] <= 'z') {
/* 173 */         b[0] = (char)(b[0] - 32);
/*     */       }
/* 175 */       int length = key.length();
/* 176 */       for (int i = 1; i < length; i++) {
/* 177 */         if (b[i] >= 'A' && b[i] <= 'Z') {
/* 178 */           b[i] = (char)(b[i] + 32);
/*     */         }
/*     */       } 
/* 181 */       result = new String(b);
/*     */     } 
/* 183 */     return result;
/*     */   }
/*     */   
/*     */   public static String readLine(ByteArrayInputStream is) {
/* 187 */     return readLine(new StringBuilder(256), is);
/*     */   }
/*     */   
/*     */   public static String readLine(StringBuilder sb, ByteArrayInputStream is) {
/*     */     int nextByte;
/* 192 */     while ((nextByte = is.read()) != -1) {
/* 193 */       char nextChar = (char)nextByte;
/* 194 */       if (nextChar == '\r') {
/* 195 */         nextByte = (char)is.read();
/* 196 */         if (nextByte == 10) {
/*     */           break;
/*     */         }
/* 199 */       } else if (nextChar == '\n') {
/*     */         break;
/*     */       } 
/*     */       
/* 203 */       sb.append(nextChar);
/*     */     } 
/* 205 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] splitHeader(String sb) {
/* 215 */     int nameStart = findNonWhitespace(sb, 0); int nameEnd;
/* 216 */     for (nameEnd = nameStart; nameEnd < sb.length(); nameEnd++) {
/* 217 */       char ch = sb.charAt(nameEnd);
/* 218 */       if (ch == ':' || Character.isWhitespace(ch)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     int colonEnd;
/* 223 */     for (colonEnd = nameEnd; colonEnd < sb.length(); colonEnd++) {
/* 224 */       if (sb.charAt(colonEnd) == ':') {
/* 225 */         colonEnd++;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 230 */     int valueStart = findNonWhitespace(sb, colonEnd);
/* 231 */     int valueEnd = findEndOfString(sb);
/*     */ 
/*     */     
/* 234 */     return new String[] { sb.substring(nameStart, nameEnd), (sb.length() >= valueStart && sb.length() >= valueEnd && valueStart < valueEnd) ? sb.substring(valueStart, valueEnd) : null };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findNonWhitespace(String sb, int offset) {
/*     */     int result;
/* 245 */     for (result = offset; result < sb.length() && 
/* 246 */       Character.isWhitespace(sb.charAt(result)); result++);
/*     */ 
/*     */ 
/*     */     
/* 250 */     return result;
/*     */   }
/*     */   
/*     */   protected int findEndOfString(String sb) {
/*     */     int result;
/* 255 */     for (result = sb.length(); result > 0 && 
/* 256 */       Character.isWhitespace(sb.charAt(result - 1)); result--);
/*     */ 
/*     */ 
/*     */     
/* 260 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 265 */     StringBuilder headerString = new StringBuilder(512);
/* 266 */     for (Map.Entry<String, List<String>> headerEntry : entrySet()) {
/*     */       
/* 268 */       headerString.append(headerEntry.getKey()).append(": ");
/*     */       
/* 270 */       for (String v : headerEntry.getValue()) {
/* 271 */         headerString.append(v).append(",");
/*     */       }
/* 273 */       headerString.delete(headerString.length() - 1, headerString.length());
/* 274 */       headerString.append("\r\n");
/*     */     } 
/* 276 */     return headerString.toString();
/*     */   }
/*     */   
/*     */   public Headers() {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\http\Headers.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
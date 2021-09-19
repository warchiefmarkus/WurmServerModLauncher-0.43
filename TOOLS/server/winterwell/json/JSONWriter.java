/*     */ package winterwell.json;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ public class JSONWriter
/*     */ {
/*     */   private static final int maxdepth = 20;
/*     */   private boolean comma;
/*     */   protected char mode;
/*     */   private char[] stack;
/*     */   private int top;
/*     */   protected Writer writer;
/*     */   
/*     */   public JSONWriter(Writer w) {
/*  97 */     this.comma = false;
/*  98 */     this.mode = 'i';
/*  99 */     this.stack = new char[20];
/* 100 */     this.top = 0;
/* 101 */     this.writer = w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JSONWriter append(String s) throws JSONException {
/* 111 */     if (s == null) {
/* 112 */       throw new JSONException("Null pointer");
/*     */     }
/* 114 */     if (this.mode == 'o' || this.mode == 'a') {
/*     */       try {
/* 116 */         if (this.comma && this.mode == 'a') {
/* 117 */           this.writer.write(44);
/*     */         }
/* 119 */         this.writer.write(s);
/* 120 */       } catch (IOException e) {
/* 121 */         throw new JSONException(e);
/*     */       } 
/* 123 */       if (this.mode == 'o') {
/* 124 */         this.mode = 'k';
/*     */       }
/* 126 */       this.comma = true;
/* 127 */       return this;
/*     */     } 
/* 129 */     throw new JSONException("Value out of sequence.");
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
/*     */   public JSONWriter array() throws JSONException {
/* 142 */     if (this.mode == 'i' || this.mode == 'o' || this.mode == 'a') {
/* 143 */       push('a');
/* 144 */       append("[");
/* 145 */       this.comma = false;
/* 146 */       return this;
/*     */     } 
/* 148 */     throw new JSONException("Misplaced array.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JSONWriter end(char m, char c) throws JSONException {
/* 159 */     if (this.mode != m) {
/* 160 */       throw new JSONException((m == 'o') ? "Misplaced endObject." : 
/* 161 */           "Misplaced endArray.");
/*     */     }
/* 163 */     pop(m);
/*     */     try {
/* 165 */       this.writer.write(c);
/* 166 */     } catch (IOException e) {
/* 167 */       throw new JSONException(e);
/*     */     } 
/* 169 */     this.comma = true;
/* 170 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONWriter endArray() throws JSONException {
/* 180 */     return end('a', ']');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONWriter endObject() throws JSONException {
/* 190 */     return end('k', '}');
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
/*     */   public JSONWriter key(String s) throws JSONException {
/* 202 */     if (s == null) {
/* 203 */       throw new JSONException("Null key.");
/*     */     }
/* 205 */     if (this.mode == 'k') {
/*     */       try {
/* 207 */         if (this.comma) {
/* 208 */           this.writer.write(44);
/*     */         }
/* 210 */         this.writer.write(JSONObject.quote(s));
/* 211 */         this.writer.write(58);
/* 212 */         this.comma = false;
/* 213 */         this.mode = 'o';
/* 214 */         return this;
/* 215 */       } catch (IOException e) {
/* 216 */         throw new JSONException(e);
/*     */       } 
/*     */     }
/* 219 */     throw new JSONException("Misplaced key.");
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
/*     */   public JSONWriter object() throws JSONException {
/* 233 */     if (this.mode == 'i') {
/* 234 */       this.mode = 'o';
/*     */     }
/* 236 */     if (this.mode == 'o' || this.mode == 'a') {
/* 237 */       append("{");
/* 238 */       push('k');
/* 239 */       this.comma = false;
/* 240 */       return this;
/*     */     } 
/* 242 */     throw new JSONException("Misplaced object.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pop(char c) throws JSONException {
/* 253 */     if (this.top <= 0 || this.stack[this.top - 1] != c) {
/* 254 */       throw new JSONException("Nesting error.");
/*     */     }
/* 256 */     this.top--;
/* 257 */     this.mode = (this.top == 0) ? 'd' : this.stack[this.top - 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void push(char c) throws JSONException {
/* 266 */     if (this.top >= 20) {
/* 267 */       throw new JSONException("Nesting too deep.");
/*     */     }
/* 269 */     this.stack[this.top] = c;
/* 270 */     this.mode = c;
/* 271 */     this.top++;
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
/*     */   public JSONWriter value(boolean b) throws JSONException {
/* 283 */     return append(b ? "true" : "false");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONWriter value(double d) throws JSONException {
/* 293 */     return value(new Double(d));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONWriter value(long l) throws JSONException {
/* 303 */     return append(Long.toString(l));
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
/*     */   public JSONWriter value(Object o) throws JSONException {
/* 316 */     return append(JSONObject.valueToString(o));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\json\JSONWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
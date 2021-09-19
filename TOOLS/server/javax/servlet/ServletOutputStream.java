/*     */ package javax.servlet;
/*     */ 
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ServletOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private static final String LSTRING_FILE = "javax.servlet.LocalStrings";
/*  87 */   private static ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.LocalStrings");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(String s) throws IOException {
/* 114 */     if (s == null) s = "null"; 
/* 115 */     int len = s.length();
/* 116 */     for (int i = 0; i < len; i++) {
/* 117 */       char c = s.charAt(i);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 125 */       if ((c & 0xFF00) != 0) {
/* 126 */         String errMsg = lStrings.getString("err.not_iso8859_1");
/* 127 */         Object[] errArgs = new Object[1];
/* 128 */         errArgs[0] = Character.valueOf(c);
/* 129 */         errMsg = MessageFormat.format(errMsg, errArgs);
/* 130 */         throw new CharConversionException(errMsg);
/*     */       } 
/* 132 */       write(c);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(boolean b) throws IOException {
/*     */     String msg;
/* 152 */     if (b) {
/* 153 */       msg = lStrings.getString("value.true");
/*     */     } else {
/* 155 */       msg = lStrings.getString("value.false");
/*     */     } 
/* 157 */     print(msg);
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
/*     */   public void print(char c) throws IOException {
/* 174 */     print(String.valueOf(c));
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
/*     */   public void print(int i) throws IOException {
/* 193 */     print(String.valueOf(i));
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
/*     */   public void print(long l) throws IOException {
/* 213 */     print(String.valueOf(l));
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
/*     */   public void print(float f) throws IOException {
/* 232 */     print(String.valueOf(f));
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
/*     */   public void print(double d) throws IOException {
/* 250 */     print(String.valueOf(d));
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
/*     */   public void println() throws IOException {
/* 266 */     print("\r\n");
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
/*     */   public void println(String s) throws IOException {
/* 283 */     print(s);
/* 284 */     println();
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
/*     */   public void println(boolean b) throws IOException {
/* 305 */     print(b);
/* 306 */     println();
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
/*     */   public void println(char c) throws IOException {
/* 323 */     print(c);
/* 324 */     println();
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
/*     */   public void println(int i) throws IOException {
/* 342 */     print(i);
/* 343 */     println();
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
/*     */   public void println(long l) throws IOException {
/* 361 */     print(l);
/* 362 */     println();
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
/*     */   public void println(float f) throws IOException {
/* 382 */     print(f);
/* 383 */     println();
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
/*     */   public void println(double d) throws IOException {
/* 402 */     print(d);
/* 403 */     println();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\ServletOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
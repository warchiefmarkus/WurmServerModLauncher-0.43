/*     */ package com.sun.activation.registries;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.StringReader;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MimeTypeFile
/*     */ {
/*  47 */   private String fname = null;
/*  48 */   private Hashtable type_hash = new Hashtable();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeTypeFile(String new_fname) throws IOException {
/*  56 */     File mime_file = null;
/*  57 */     FileReader fr = null;
/*     */     
/*  59 */     this.fname = new_fname;
/*     */     
/*  61 */     mime_file = new File(this.fname);
/*     */     
/*  63 */     fr = new FileReader(mime_file);
/*     */     
/*     */     try {
/*  66 */       parse(new BufferedReader(fr));
/*     */     } finally {
/*     */       try {
/*  69 */         fr.close();
/*  70 */       } catch (IOException e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeTypeFile(InputStream is) throws IOException {
/*  77 */     parse(new BufferedReader(new InputStreamReader(is, "iso-8859-1")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeTypeFile() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeTypeEntry getMimeTypeEntry(String file_ext) {
/*  90 */     return (MimeTypeEntry)this.type_hash.get(file_ext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMIMETypeString(String file_ext) {
/*  97 */     MimeTypeEntry entry = getMimeTypeEntry(file_ext);
/*     */     
/*  99 */     if (entry != null) {
/* 100 */       return entry.getMIMEType();
/*     */     }
/* 102 */     return null;
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
/*     */   public void appendToRegistry(String mime_types) {
/*     */     try {
/* 122 */       parse(new BufferedReader(new StringReader(mime_types)));
/* 123 */     } catch (IOException ex) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parse(BufferedReader buf_reader) throws IOException {
/* 132 */     String line = null, prev = null;
/*     */     
/* 134 */     while ((line = buf_reader.readLine()) != null) {
/* 135 */       if (prev == null) {
/* 136 */         prev = line;
/*     */       } else {
/* 138 */         prev = prev + line;
/* 139 */       }  int end = prev.length();
/* 140 */       if (prev.length() > 0 && prev.charAt(end - 1) == '\\') {
/* 141 */         prev = prev.substring(0, end - 1);
/*     */         continue;
/*     */       } 
/* 144 */       parseEntry(prev);
/* 145 */       prev = null;
/*     */     } 
/* 147 */     if (prev != null) {
/* 148 */       parseEntry(prev);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseEntry(String line) {
/* 155 */     String mime_type = null;
/* 156 */     String file_ext = null;
/* 157 */     line = line.trim();
/*     */     
/* 159 */     if (line.length() == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 163 */     if (line.charAt(0) == '#') {
/*     */       return;
/*     */     }
/*     */     
/* 167 */     if (line.indexOf('=') > 0) {
/*     */       
/* 169 */       LineTokenizer lt = new LineTokenizer(line);
/* 170 */       while (lt.hasMoreTokens()) {
/* 171 */         String name = lt.nextToken();
/* 172 */         String value = null;
/* 173 */         if (lt.hasMoreTokens() && lt.nextToken().equals("=") && lt.hasMoreTokens())
/*     */         {
/* 175 */           value = lt.nextToken(); } 
/* 176 */         if (value == null) {
/* 177 */           if (LogSupport.isLoggable())
/* 178 */             LogSupport.log("Bad .mime.types entry: " + line); 
/*     */           return;
/*     */         } 
/* 181 */         if (name.equals("type")) {
/* 182 */           mime_type = value; continue;
/* 183 */         }  if (name.equals("exts")) {
/* 184 */           StringTokenizer st = new StringTokenizer(value, ",");
/* 185 */           while (st.hasMoreTokens()) {
/* 186 */             file_ext = st.nextToken();
/* 187 */             MimeTypeEntry entry = new MimeTypeEntry(mime_type, file_ext);
/*     */             
/* 189 */             this.type_hash.put(file_ext, entry);
/* 190 */             if (LogSupport.isLoggable()) {
/* 191 */               LogSupport.log("Added: " + entry.toString());
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 198 */       StringTokenizer strtok = new StringTokenizer(line);
/* 199 */       int num_tok = strtok.countTokens();
/*     */       
/* 201 */       if (num_tok == 0) {
/*     */         return;
/*     */       }
/* 204 */       mime_type = strtok.nextToken();
/*     */       
/* 206 */       while (strtok.hasMoreTokens()) {
/* 207 */         MimeTypeEntry entry = null;
/*     */         
/* 209 */         file_ext = strtok.nextToken();
/* 210 */         entry = new MimeTypeEntry(mime_type, file_ext);
/* 211 */         this.type_hash.put(file_ext, entry);
/* 212 */         if (LogSupport.isLoggable())
/* 213 */           LogSupport.log("Added: " + entry.toString()); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\activation\registries\MimeTypeFile.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */
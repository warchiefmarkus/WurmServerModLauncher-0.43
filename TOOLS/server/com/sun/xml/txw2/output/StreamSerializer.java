/*     */ package com.sun.xml.txw2.output;
/*     */ 
/*     */ import com.sun.xml.txw2.TxwException;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StreamSerializer
/*     */   implements XmlSerializer
/*     */ {
/*     */   private final SaxSerializer serializer;
/*     */   private final XMLWriter writer;
/*     */   
/*     */   public StreamSerializer(OutputStream out) {
/*  47 */     this(createWriter(out));
/*     */   }
/*     */   
/*     */   public StreamSerializer(OutputStream out, String encoding) throws UnsupportedEncodingException {
/*  51 */     this(createWriter(out, encoding));
/*     */   }
/*     */   
/*     */   public StreamSerializer(Writer out) {
/*  55 */     this(new StreamResult(out));
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamSerializer(StreamResult streamResult) {
/*  60 */     final OutputStream[] autoClose = new OutputStream[1];
/*     */     
/*  62 */     if (streamResult.getWriter() != null) {
/*  63 */       this.writer = createWriter(streamResult.getWriter());
/*  64 */     } else if (streamResult.getOutputStream() != null) {
/*  65 */       this.writer = createWriter(streamResult.getOutputStream());
/*  66 */     } else if (streamResult.getSystemId() != null) {
/*  67 */       String fileURL = streamResult.getSystemId();
/*     */       
/*  69 */       fileURL = convertURL(fileURL);
/*     */       
/*     */       try {
/*  72 */         FileOutputStream fos = new FileOutputStream(fileURL);
/*  73 */         autoClose[0] = fos;
/*  74 */         this.writer = createWriter(fos);
/*  75 */       } catch (IOException e) {
/*  76 */         throw new TxwException(e);
/*     */       } 
/*     */     } else {
/*  79 */       throw new IllegalArgumentException();
/*     */     } 
/*     */     
/*  82 */     this.serializer = new SaxSerializer(this.writer, this.writer, false) {
/*     */         public void endDocument() {
/*  84 */           super.endDocument();
/*  85 */           if (autoClose[0] != null) {
/*     */             try {
/*  87 */               autoClose[0].close();
/*  88 */             } catch (IOException e) {
/*  89 */               throw new TxwException(e);
/*     */             } 
/*  91 */             autoClose[0] = null;
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private StreamSerializer(XMLWriter writer) {
/*  98 */     this.writer = writer;
/*     */     
/* 100 */     this.serializer = new SaxSerializer(writer, writer, false);
/*     */   }
/*     */   
/*     */   private String convertURL(String url) {
/* 104 */     url = url.replace('\\', '/');
/* 105 */     url = url.replaceAll("//", "/");
/* 106 */     url = url.replaceAll("//", "/");
/* 107 */     if (url.startsWith("file:/"))
/* 108 */       if (url.substring(6).indexOf(":") > 0) {
/* 109 */         url = url.substring(6);
/*     */       } else {
/* 111 */         url = url.substring(5);
/*     */       }  
/* 113 */     return url;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument() {
/* 118 */     this.serializer.startDocument();
/*     */   }
/*     */   
/*     */   public void beginStartTag(String uri, String localName, String prefix) {
/* 122 */     this.serializer.beginStartTag(uri, localName, prefix);
/*     */   }
/*     */   
/*     */   public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
/* 126 */     this.serializer.writeAttribute(uri, localName, prefix, value);
/*     */   }
/*     */   
/*     */   public void writeXmlns(String prefix, String uri) {
/* 130 */     this.serializer.writeXmlns(prefix, uri);
/*     */   }
/*     */   
/*     */   public void endStartTag(String uri, String localName, String prefix) {
/* 134 */     this.serializer.endStartTag(uri, localName, prefix);
/*     */   }
/*     */   
/*     */   public void endTag() {
/* 138 */     this.serializer.endTag();
/*     */   }
/*     */   
/*     */   public void text(StringBuilder text) {
/* 142 */     this.serializer.text(text);
/*     */   }
/*     */   
/*     */   public void cdata(StringBuilder text) {
/* 146 */     this.serializer.cdata(text);
/*     */   }
/*     */   
/*     */   public void comment(StringBuilder comment) {
/* 150 */     this.serializer.comment(comment);
/*     */   }
/*     */   
/*     */   public void endDocument() {
/* 154 */     this.serializer.endDocument();
/*     */   }
/*     */   
/*     */   public void flush() {
/* 158 */     this.serializer.flush();
/*     */     try {
/* 160 */       this.writer.flush();
/* 161 */     } catch (IOException e) {
/* 162 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static XMLWriter createWriter(Writer w) {
/* 169 */     DataWriter dw = new DataWriter(new BufferedWriter(w));
/* 170 */     dw.setIndentStep("  ");
/* 171 */     return dw;
/*     */   }
/*     */   
/*     */   private static XMLWriter createWriter(OutputStream os, String encoding) throws UnsupportedEncodingException {
/* 175 */     XMLWriter writer = createWriter(new OutputStreamWriter(os, encoding));
/* 176 */     writer.setEncoding(encoding);
/* 177 */     return writer;
/*     */   }
/*     */   
/*     */   private static XMLWriter createWriter(OutputStream os) {
/*     */     try {
/* 182 */       return createWriter(os, "UTF-8");
/* 183 */     } catch (UnsupportedEncodingException e) {
/*     */       
/* 185 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\output\StreamSerializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
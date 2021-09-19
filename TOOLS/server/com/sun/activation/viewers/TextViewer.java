/*     */ package com.sun.activation.viewers;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Panel;
/*     */ import java.awt.TextArea;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.activation.CommandObject;
/*     */ import javax.activation.DataHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextViewer
/*     */   extends Panel
/*     */   implements CommandObject
/*     */ {
/*  50 */   private TextArea text_area = null;
/*     */ 
/*     */   
/*  53 */   private File text_file = null;
/*  54 */   private String text_buffer = null;
/*     */   
/*  56 */   private DataHandler _dh = null;
/*     */   
/*     */   private boolean DEBUG = false;
/*     */ 
/*     */   
/*     */   public TextViewer() {
/*  62 */     setLayout(new GridLayout(1, 1));
/*     */     
/*  64 */     this.text_area = new TextArea("", 24, 80, 1);
/*     */     
/*  66 */     this.text_area.setEditable(false);
/*     */     
/*  68 */     add(this.text_area);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCommandContext(String verb, DataHandler dh) throws IOException {
/*  73 */     this._dh = dh;
/*  74 */     setInputStream(this._dh.getInputStream());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInputStream(InputStream ins) throws IOException {
/*  84 */     int bytes_read = 0;
/*     */     
/*  86 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  87 */     byte[] data = new byte[1024];
/*     */     
/*  89 */     while ((bytes_read = ins.read(data)) > 0) {
/*  90 */       baos.write(data, 0, bytes_read);
/*     */     }
/*  92 */     ins.close();
/*     */ 
/*     */ 
/*     */     
/*  96 */     this.text_buffer = baos.toString();
/*     */ 
/*     */     
/*  99 */     this.text_area.setText(this.text_buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addNotify() {
/* 104 */     super.addNotify();
/* 105 */     invalidate();
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize() {
/* 109 */     return this.text_area.getMinimumSize(24, 80);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\activation\viewers\TextViewer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */
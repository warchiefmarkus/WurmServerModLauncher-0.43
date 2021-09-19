/*     */ package com.sun.activation.viewers;
/*     */ 
/*     */ import java.awt.Button;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Panel;
/*     */ import java.awt.TextArea;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ public class TextEditor
/*     */   extends Panel
/*     */   implements CommandObject, ActionListener
/*     */ {
/*  52 */   private TextArea text_area = null;
/*  53 */   private GridBagLayout panel_gb = null;
/*  54 */   private Panel button_panel = null;
/*  55 */   private Button save_button = null;
/*     */   
/*  57 */   private File text_file = null;
/*  58 */   private String text_buffer = null;
/*  59 */   private InputStream data_ins = null;
/*  60 */   private FileInputStream fis = null;
/*     */   
/*  62 */   private DataHandler _dh = null;
/*     */   
/*     */   private boolean DEBUG = false;
/*     */ 
/*     */   
/*     */   public TextEditor() {
/*  68 */     this.panel_gb = new GridBagLayout();
/*  69 */     setLayout(this.panel_gb);
/*     */     
/*  71 */     this.button_panel = new Panel();
/*     */     
/*  73 */     this.button_panel.setLayout(new FlowLayout());
/*  74 */     this.save_button = new Button("SAVE");
/*  75 */     this.button_panel.add(this.save_button);
/*  76 */     addGridComponent(this, this.button_panel, this.panel_gb, 0, 0, 1, 1, 1, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     this.text_area = new TextArea("This is text", 24, 80, 1);
/*     */ 
/*     */     
/*  87 */     this.text_area.setEditable(true);
/*     */     
/*  89 */     addGridComponent(this, this.text_area, this.panel_gb, 0, 1, 1, 2, 1, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     this.save_button.addActionListener(this);
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
/*     */   private void addGridComponent(Container cont, Component comp, GridBagLayout mygb, int gridx, int gridy, int gridw, int gridh, int weightx, int weighty) {
/* 114 */     GridBagConstraints c = new GridBagConstraints();
/* 115 */     c.gridx = gridx;
/* 116 */     c.gridy = gridy;
/* 117 */     c.gridwidth = gridw;
/* 118 */     c.gridheight = gridh;
/* 119 */     c.fill = 1;
/* 120 */     c.weighty = weighty;
/* 121 */     c.weightx = weightx;
/* 122 */     c.anchor = 10;
/* 123 */     mygb.setConstraints(comp, c);
/* 124 */     cont.add(comp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCommandContext(String verb, DataHandler dh) throws IOException {
/* 129 */     this._dh = dh;
/* 130 */     setInputStream(this._dh.getInputStream());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInputStream(InputStream ins) throws IOException {
/* 141 */     byte[] data = new byte[1024];
/* 142 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 143 */     int bytes_read = 0;
/*     */ 
/*     */     
/* 146 */     while ((bytes_read = ins.read(data)) > 0)
/* 147 */       baos.write(data, 0, bytes_read); 
/* 148 */     ins.close();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     this.text_buffer = baos.toString();
/*     */ 
/*     */     
/* 156 */     this.text_area.setText(this.text_buffer);
/*     */   }
/*     */   
/*     */   private void performSaveOperation() {
/* 160 */     OutputStream fos = null;
/*     */     try {
/* 162 */       fos = this._dh.getOutputStream();
/* 163 */     } catch (Exception e) {}
/*     */     
/* 165 */     String buffer = this.text_area.getText();
/*     */ 
/*     */     
/* 168 */     if (fos == null) {
/* 169 */       System.out.println("Invalid outputstream in TextEditor!");
/* 170 */       System.out.println("not saving!");
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 175 */       fos.write(buffer.getBytes());
/* 176 */       fos.flush();
/* 177 */       fos.close();
/* 178 */     } catch (IOException e) {
/*     */       
/* 180 */       System.out.println("TextEditor Save Operation failed with: " + e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addNotify() {
/* 186 */     super.addNotify();
/* 187 */     invalidate();
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize() {
/* 191 */     return this.text_area.getMinimumSize(24, 80);
/*     */   }
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent evt) {
/* 196 */     if (evt.getSource() == this.save_button)
/*     */     {
/*     */       
/* 199 */       performSaveOperation();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\activation\viewers\TextEditor.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */
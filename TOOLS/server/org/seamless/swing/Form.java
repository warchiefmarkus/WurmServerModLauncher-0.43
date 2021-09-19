/*     */ package org.seamless.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JSeparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Form
/*     */ {
/*  27 */   public GridBagConstraints lastConstraints = null;
/*  28 */   public GridBagConstraints middleConstraints = null;
/*  29 */   public GridBagConstraints labelConstraints = null;
/*  30 */   public GridBagConstraints separatorConstraints = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Form(int padding) {
/*  38 */     this.lastConstraints = new GridBagConstraints();
/*     */ 
/*     */     
/*  41 */     this.lastConstraints.fill = 2;
/*     */ 
/*     */ 
/*     */     
/*  45 */     this.lastConstraints.anchor = 18;
/*     */ 
/*     */     
/*  48 */     this.lastConstraints.weightx = 1.0D;
/*     */ 
/*     */     
/*  51 */     this.lastConstraints.gridwidth = 0;
/*     */ 
/*     */     
/*  54 */     this.lastConstraints.insets = new Insets(padding, padding, padding, padding);
/*     */ 
/*     */     
/*  57 */     this.middleConstraints = (GridBagConstraints)this.lastConstraints.clone();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     this.middleConstraints.gridwidth = -1;
/*     */ 
/*     */ 
/*     */     
/*  66 */     this.labelConstraints = (GridBagConstraints)this.lastConstraints.clone();
/*     */ 
/*     */ 
/*     */     
/*  70 */     this.labelConstraints.weightx = 0.0D;
/*  71 */     this.labelConstraints.gridwidth = 1;
/*     */     
/*  73 */     this.separatorConstraints = new GridBagConstraints();
/*  74 */     this.separatorConstraints.fill = 2;
/*  75 */     this.separatorConstraints.gridwidth = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLastField(Component c, Container parent) {
/*  84 */     GridBagLayout gbl = (GridBagLayout)parent.getLayout();
/*  85 */     gbl.setConstraints(c, this.lastConstraints);
/*  86 */     parent.add(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLabel(Component c, Container parent) {
/*  96 */     GridBagLayout gbl = (GridBagLayout)parent.getLayout();
/*  97 */     gbl.setConstraints(c, this.labelConstraints);
/*  98 */     parent.add(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JLabel addLabel(String s, Container parent) {
/* 105 */     JLabel c = new JLabel(s);
/* 106 */     addLabel(c, parent);
/* 107 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMiddleField(Component c, Container parent) {
/* 117 */     GridBagLayout gbl = (GridBagLayout)parent.getLayout();
/* 118 */     gbl.setConstraints(c, this.middleConstraints);
/* 119 */     parent.add(c);
/*     */   }
/*     */   
/*     */   public void addLabelAndLastField(String s, Container c, Container parent) {
/* 123 */     addLabel(s, parent);
/* 124 */     addLastField(c, parent);
/*     */   }
/*     */   
/*     */   public void addLabelAndLastField(String s, String value, Container parent) {
/* 128 */     addLabel(s, parent);
/* 129 */     addLastField(new JLabel(value), parent);
/*     */   }
/*     */   
/*     */   public void addSeparator(Container parent) {
/* 133 */     JSeparator separator = new JSeparator();
/* 134 */     GridBagLayout gbl = (GridBagLayout)parent.getLayout();
/* 135 */     gbl.setConstraints(separator, this.separatorConstraints);
/* 136 */     parent.add(separator);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\Form.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
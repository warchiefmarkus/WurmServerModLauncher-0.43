/*     */ package org.seamless.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.net.URL;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Application
/*     */ {
/*     */   public static void showError(Component parent, Throwable ex) {
/*  38 */     JTextArea textArea = new JTextArea();
/*  39 */     textArea.setFont(new Font("Sans-Serif", 0, 10));
/*  40 */     textArea.setEditable(false);
/*  41 */     StringWriter writer = new StringWriter();
/*  42 */     ex.printStackTrace(new PrintWriter(writer));
/*  43 */     textArea.setText(writer.toString());
/*  44 */     JScrollPane scrollPane = new JScrollPane(textArea);
/*  45 */     scrollPane.setPreferredSize(new Dimension(350, 150));
/*  46 */     JOptionPane.showMessageDialog(parent, scrollPane, "An Error Has Occurred", 0);
/*     */   }
/*     */   
/*     */   public static void showWarning(Component parent, String... warningLines) {
/*  50 */     JTextArea textArea = new JTextArea();
/*  51 */     textArea.setFont(new Font("Sans-Serif", 0, 10));
/*  52 */     textArea.setEditable(false);
/*  53 */     for (String s : warningLines) textArea.append(s + "\n"); 
/*  54 */     JScrollPane scrollPane = new JScrollPane(textArea);
/*  55 */     scrollPane.setPreferredSize(new Dimension(350, 150));
/*  56 */     JOptionPane.showMessageDialog(parent, scrollPane, "Warning", 0);
/*     */   }
/*     */   
/*     */   public static void showInfo(Component parent, String... infoLines) {
/*  60 */     JTextArea textArea = new JTextArea();
/*  61 */     textArea.setFont(new Font("Sans-Serif", 0, 10));
/*  62 */     textArea.setEditable(false);
/*  63 */     for (String s : infoLines) textArea.append(s + "\n"); 
/*  64 */     JScrollPane scrollPane = new JScrollPane(textArea);
/*  65 */     scrollPane.setPreferredSize(new Dimension(350, 150));
/*  66 */     JOptionPane.showMessageDialog(parent, scrollPane, "Info", 1);
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
/*     */   public static void increaseFontSize(JComponent l) {
/*  79 */     l.setFont(new Font(l.getFont().getFontName(), l.getFont().getStyle(), l.getFont().getSize() + 2));
/*     */   }
/*     */   
/*     */   public static void decreaseFontSize(JComponent l) {
/*  83 */     l.setFont(new Font(l.getFont().getFontName(), l.getFont().getStyle(), l.getFont().getSize() - 2));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Window center(Window w) {
/*  88 */     Dimension us = w.getSize(), them = Toolkit.getDefaultToolkit().getScreenSize();
/*  89 */     int newX = (them.width - us.width) / 2;
/*  90 */     int newY = (them.height - us.height) / 2;
/*  91 */     if (newX < 0) newX = 0; 
/*  92 */     if (newY < 0) newY = 0; 
/*  93 */     w.setLocation(newX, newY);
/*  94 */     return w;
/*     */   }
/*     */   
/*     */   public static Window center(Window w, Window reference) {
/*  98 */     double refCenterX = reference.getX() + reference.getSize().getWidth() / 2.0D;
/*  99 */     double refCenterY = reference.getY() + reference.getSize().getHeight() / 2.0D;
/* 100 */     int newX = (int)(refCenterX - w.getSize().getWidth() / 2.0D);
/* 101 */     int newY = (int)(refCenterY - w.getSize().getHeight() / 2.0D);
/* 102 */     w.setLocation(newX, newY);
/* 103 */     return w;
/*     */   }
/*     */   
/*     */   public static Window maximize(Window w) {
/* 107 */     Dimension us = w.getSize(), them = Toolkit.getDefaultToolkit().getScreenSize();
/* 108 */     w.setBounds(0, 0, them.width, them.height);
/* 109 */     return w;
/*     */   }
/*     */   
/*     */   public static ImageIcon createImageIcon(Class base, String path, String description) {
/* 113 */     URL imgURL = base.getResource(path);
/* 114 */     if (imgURL != null) {
/* 115 */       return new ImageIcon(imgURL, description);
/*     */     }
/* 117 */     throw new RuntimeException("Couldn't find image icon on path: " + path);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImageIcon createImageIcon(Class base, String path) {
/* 122 */     return createImageIcon(base, path, null);
/*     */   }
/*     */   
/*     */   public static void copyToClipboard(String s) {
/* 126 */     Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 127 */     StringSelection data = new StringSelection(s);
/* 128 */     clipboard.setContents(data, data);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\Application.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
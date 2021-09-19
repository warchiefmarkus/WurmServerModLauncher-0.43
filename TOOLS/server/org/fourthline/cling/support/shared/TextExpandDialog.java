/*    */ package org.fourthline.cling.support.shared;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Frame;
/*    */ import java.util.logging.Logger;
/*    */ import javax.swing.JDialog;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.JTextArea;
/*    */ import org.fourthline.cling.model.ModelUtil;
/*    */ import org.seamless.swing.Application;
/*    */ import org.seamless.xml.DOM;
/*    */ import org.seamless.xml.DOMParser;
/*    */ import org.w3c.dom.Document;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TextExpandDialog
/*    */   extends JDialog
/*    */ {
/* 38 */   private static Logger log = Logger.getLogger(TextExpandDialog.class.getName());
/*    */   
/*    */   public TextExpandDialog(Frame frame, String text) {
/* 41 */     super(frame); String pretty;
/* 42 */     setResizable(true);
/*    */     
/* 44 */     JTextArea textArea = new JTextArea();
/* 45 */     JScrollPane textPane = new JScrollPane(textArea);
/* 46 */     textPane.setPreferredSize(new Dimension(500, 400));
/* 47 */     add(textPane);
/*    */ 
/*    */     
/* 50 */     if (text.startsWith("<") && text.endsWith(">")) {
/*    */ 
/*    */       
/*    */       try {
/*    */ 
/*    */ 
/*    */         
/* 57 */         pretty = (new DOMParser() { protected DOM createDOM(Document document) { return null; } }).print(text, 2, false);
/* 58 */       } catch (Exception ex) {
/* 59 */         log.severe("Error pretty printing XML: " + ex.toString());
/* 60 */         pretty = text;
/*    */       } 
/* 62 */     } else if (text.startsWith("http-get")) {
/* 63 */       pretty = ModelUtil.commaToNewline(text);
/*    */     } else {
/* 65 */       pretty = text;
/*    */     } 
/*    */     
/* 68 */     textArea.setEditable(false);
/* 69 */     textArea.setText(pretty);
/*    */     
/* 71 */     pack();
/* 72 */     Application.center(this, getOwner());
/* 73 */     setVisible(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\shared\TextExpandDialog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
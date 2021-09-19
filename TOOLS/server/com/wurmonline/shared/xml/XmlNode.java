/*    */ package com.wurmonline.shared.xml;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import org.xml.sax.Attributes;
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
/*    */ public class XmlNode
/*    */ {
/*    */   private final String name;
/*    */   private final Attributes attributes;
/* 27 */   private final List<XmlNode> children = new ArrayList<>();
/*    */   
/*    */   private String text;
/*    */   
/*    */   public XmlNode(String localName, Attributes attributes) {
/* 32 */     this.name = localName;
/* 33 */     this.attributes = attributes;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addChild(XmlNode child) {
/* 38 */     this.children.add(child);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setText(String text) {
/* 43 */     this.text = text;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<XmlNode> getAll(String aName) {
/* 48 */     List<XmlNode> list = new ArrayList<>();
/* 49 */     for (Iterator<XmlNode> it = this.children.iterator(); it.hasNext(); ) {
/*    */       
/* 51 */       XmlNode xmlNode = it.next();
/* 52 */       if (xmlNode.name.equals(aName))
/* 53 */         list.add(xmlNode); 
/*    */     } 
/* 55 */     return list;
/*    */   }
/*    */ 
/*    */   
/*    */   public XmlNode getFirst(String aName) {
/* 60 */     for (Iterator<XmlNode> it = this.children.iterator(); it.hasNext(); ) {
/*    */       
/* 62 */       XmlNode xmlNode = it.next();
/* 63 */       if (xmlNode.name.equals(aName)) {
/* 64 */         return xmlNode;
/*    */       }
/*    */     } 
/* 67 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttribute(String aName) {
/* 72 */     return this.attributes.getValue(aName);
/*    */   }
/*    */ 
/*    */   
/*    */   public Attributes getAttributes() {
/* 77 */     return this.attributes;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<XmlNode> getChildren() {
/* 82 */     return this.children;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 87 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getText() {
/* 92 */     return this.text;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue(String string) {
/* 97 */     XmlNode node = getFirst(string);
/* 98 */     return (node == null) ? null : node.getText();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\xml\XmlNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
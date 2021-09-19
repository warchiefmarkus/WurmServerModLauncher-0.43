/*     */ package org.fourthline.cling.support.messagebox.model;
/*     */ 
/*     */ import java.util.Random;
/*     */ import org.fourthline.cling.support.messagebox.parser.MessageDOM;
/*     */ import org.fourthline.cling.support.messagebox.parser.MessageDOMParser;
/*     */ import org.fourthline.cling.support.messagebox.parser.MessageElement;
/*     */ import org.seamless.xml.DOM;
/*     */ import org.seamless.xml.ParserException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Message
/*     */   implements ElementAppender
/*     */ {
/*     */   private final int id;
/*     */   private final Category category;
/*     */   private DisplayType displayType;
/*  32 */   protected final Random randomGenerator = new Random();
/*     */   
/*     */   public enum Category {
/*  35 */     SMS("SMS"),
/*  36 */     INCOMING_CALL("Incoming Call"),
/*  37 */     SCHEDULE_REMINDER("Schedule Reminder");
/*     */     
/*     */     public String text;
/*     */     
/*     */     Category(String text) {
/*  42 */       this.text = text;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum DisplayType
/*     */   {
/*  48 */     MINIMUM("Minimum"),
/*  49 */     MAXIMUM("Maximum");
/*     */     
/*     */     public String text;
/*     */     
/*     */     DisplayType(String text) {
/*  54 */       this.text = text;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message(Category category, DisplayType displayType) {
/*  63 */     this(0, category, displayType);
/*     */   }
/*     */   
/*     */   public Message(int id, Category category, DisplayType displayType) {
/*  67 */     if (id == 0) id = this.randomGenerator.nextInt(2147483647); 
/*  68 */     this.id = id;
/*  69 */     this.category = category;
/*  70 */     this.displayType = displayType;
/*     */   }
/*     */   
/*     */   public int getId() {
/*  74 */     return this.id;
/*     */   }
/*     */   
/*     */   public Category getCategory() {
/*  78 */     return this.category;
/*     */   }
/*     */   
/*     */   public DisplayType getDisplayType() {
/*  82 */     return this.displayType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  87 */     if (this == o) return true; 
/*  88 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/*  90 */     Message message = (Message)o;
/*     */     
/*  92 */     if (this.id != message.id) return false;
/*     */     
/*  94 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*     */     try {
/* 105 */       MessageDOMParser mp = new MessageDOMParser();
/* 106 */       MessageDOM dom = (MessageDOM)mp.createDocument();
/*     */       
/* 108 */       MessageElement root = dom.createRoot(mp.createXPath(), "Message");
/* 109 */       ((MessageElement)root.createChild("Category")).setContent((getCategory()).text);
/* 110 */       ((MessageElement)root.createChild("DisplayType")).setContent((getDisplayType()).text);
/* 111 */       appendMessageElements(root);
/*     */       
/* 113 */       String s = mp.print((DOM)dom, 0, false);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       return s.replaceAll("<Message xmlns=\"urn:samsung-com:messagebox-1-0\">", "").replaceAll("</Message>", "");
/*     */     
/*     */     }
/* 121 */     catch (ParserException ex) {
/* 122 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\messagebox\model\Message.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.wurmonline.shared.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MulticolorLineSegment
/*    */ {
/*    */   private byte color;
/*    */   private String text;
/*    */   
/*    */   public MulticolorLineSegment(String text, byte color) {
/* 35 */     this.text = text.replaceAll("\\p{C}", "?");
/* 36 */     this.color = color;
/*    */   }
/*    */ 
/*    */   
/*    */   public ColoredChar[] convertToCharArray() {
/* 41 */     ColoredChar[] arr = new ColoredChar[getText().length()];
/* 42 */     for (int i = 0; i < getText().length(); i++)
/* 43 */       arr[i] = new ColoredChar(getText().charAt(i), getColor()); 
/* 44 */     return arr;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setText(String text) {
/* 49 */     this.text = text;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getText() {
/* 57 */     return this.text;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setColor(byte color) {
/* 62 */     this.color = color;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getColor() {
/* 70 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\share\\util\MulticolorLineSegment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
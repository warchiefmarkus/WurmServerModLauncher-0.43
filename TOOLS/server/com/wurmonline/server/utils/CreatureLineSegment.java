/*    */ package com.wurmonline.server.utils;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.shared.util.MulticolorLineSegment;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CreatureLineSegment
/*    */   extends MulticolorLineSegment
/*    */ {
/*    */   private static final String YOU_STRING = "you";
/*    */   private Creature creature;
/*    */   
/*    */   public CreatureLineSegment(Creature c) {
/* 18 */     super((c == null) ? "something" : c.getName(), (byte)0);
/* 19 */     this.creature = c;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getText(Creature sendingTo) {
/* 24 */     if (sendingTo != this.creature) {
/* 25 */       return getText();
/*    */     }
/* 27 */     return "you";
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getColor(Creature sendingTo) {
/* 32 */     if (this.creature == null || sendingTo == null) {
/* 33 */       return getColor();
/*    */     }
/* 35 */     switch (this.creature.getAttitude(sendingTo)) {
/*    */       
/*    */       case 2:
/*    */       case 4:
/* 39 */         return 4;
/*    */       case 1:
/*    */       case 5:
/* 42 */         return 9;
/*    */       case 7:
/* 44 */         return 14;
/*    */       case 0:
/* 46 */         return 12;
/*    */       case 3:
/*    */       case 6:
/* 49 */         return 8;
/*    */     } 
/* 51 */     return getColor();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ArrayList<MulticolorLineSegment> cloneLineList(ArrayList<MulticolorLineSegment> list) {
/* 58 */     ArrayList<MulticolorLineSegment> toReturn = new ArrayList<>(list.size());
/* 59 */     for (MulticolorLineSegment s : list) {
/* 60 */       if (s instanceof CreatureLineSegment) {
/* 61 */         toReturn.add(new CreatureLineSegment(((CreatureLineSegment)s).creature)); continue;
/*    */       } 
/* 63 */       toReturn.add(new MulticolorLineSegment(s.getText(), s.getColor()));
/*    */     } 
/* 65 */     return toReturn;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\CreatureLineSegment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
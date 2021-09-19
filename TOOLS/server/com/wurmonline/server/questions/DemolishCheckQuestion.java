/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.structures.NoSuchStructureException;
/*    */ import com.wurmonline.server.structures.Structure;
/*    */ import com.wurmonline.server.structures.Structures;
/*    */ import com.wurmonline.server.zones.Zones;
/*    */ import java.util.Properties;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ public final class DemolishCheckQuestion
/*    */   extends Question
/*    */ {
/* 37 */   private static final Logger logger = Logger.getLogger(GmTool.class.getName());
/* 38 */   private Structure building = null;
/*    */ 
/*    */   
/*    */   public DemolishCheckQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/* 42 */     super(aResponder, aTitle, aQuestion, 127, aTarget);
/*    */     
/*    */     try {
/* 45 */       this.building = Structures.getStructure(aTarget);
/*    */     }
/* 47 */     catch (NoSuchStructureException nss) {
/*    */       
/* 49 */       logger.log(Level.WARNING, nss.getMessage(), (Throwable)nss);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties answers) {
/* 56 */     String val = answers.getProperty("demolish");
/* 57 */     if (val != null)
/*    */     {
/* 59 */       if (val.equals("true")) {
/*    */         
/* 61 */         if (getResponder().getPower() >= 2)
/*    */         {
/* 63 */           getResponder().getLogger().log(Level.INFO, 
/*    */               
/* 65 */               getResponder().getName() + " destroyed structure " + this.building.getName() + " at " + this.building
/* 66 */               .getCenterX() + ", " + this.building.getCenterY());
/*    */         }
/*    */         
/* 69 */         if (this.building.isOnSurface())
/* 70 */           Zones.flash(this.building.getCenterX(), this.building.getCenterY(), false); 
/* 71 */         if (!this.building.hasWalls() || getResponder().getPower() >= 2 || this.building
/* 72 */           .isOwner(getResponder())) {
/* 73 */           this.building.totallyDestroy();
/*    */         }
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 81 */     StringBuilder buf = new StringBuilder();
/* 82 */     buf.append(getBmlHeader());
/* 83 */     buf.append("header{type=\"bold\";text=\"Demolish warning:\"}");
/* 84 */     buf.append("text{text=\"\"}");
/*    */     
/* 86 */     buf.append("text{text=\"You are about to demolish a building.\"}");
/* 87 */     buf.append("text{text=\"Are you really sure you want to?\"}");
/*    */     
/* 89 */     buf.append("harray{button{text=\"Demolish it!\";id=\"demolish\"}label{text=\"  \"}button{text=\"No, was a mistake\";id=\"no\"}}}};null;null;}");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 95 */     getResponder().getCommunicator().sendBml(200, 200, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\DemolishCheckQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
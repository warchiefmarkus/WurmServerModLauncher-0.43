/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.Items;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.skills.Affinities;
/*    */ import com.wurmonline.server.skills.Skill;
/*    */ import com.wurmonline.server.skills.SkillSystem;
/*    */ import com.wurmonline.server.utils.BMLBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AffinityQuestion
/*    */   extends Question
/*    */ {
/*    */   private final Item targetItem;
/*    */   private String[] allOptions;
/*    */   
/*    */   public AffinityQuestion(Creature aResponder, Item target) {
/* 23 */     super(aResponder, "Claim Affinity Token", "Which affinity should you gain?", 155, target.getWurmId());
/* 24 */     this.targetItem = target;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties answers) {
/* 30 */     System.out.println("??");
/*    */     
/* 32 */     boolean accepted = Boolean.parseBoolean(answers.getProperty("send"));
/* 33 */     if (this.targetItem == null || this.targetItem.deleted) {
/*    */       
/* 35 */       getResponder().getCommunicator().sendNormalServerMessage("Something went wrong when claiming this token.");
/*    */       
/*    */       return;
/*    */     } 
/* 39 */     if (accepted) {
/*    */       
/* 41 */       int ddVal = Integer.parseInt(answers.getProperty("affdrop"));
/* 42 */       if (ddVal == 0) {
/*    */         
/* 44 */         getResponder().getCommunicator().sendNormalServerMessage("You decide against gaining a new skill affinity.");
/*    */         
/*    */         return;
/*    */       } 
/* 48 */       int skillNum = SkillSystem.getSkillByName(this.allOptions[ddVal]);
/* 49 */       Skill s = getResponder().getSkills().getSkillOrLearn(skillNum);
/* 50 */       if (s.affinity >= 5) {
/*    */         
/* 52 */         getResponder().getCommunicator().sendNormalServerMessage("You cannot gain any more affinities in " + s.getName() + ".");
/*    */         
/*    */         return;
/*    */       } 
/* 56 */       Affinities.setAffinity(getResponder().getWurmId(), skillNum, s.affinity + 1, false);
/* 57 */       Items.destroyItem(this.targetItem.getWurmId());
/* 58 */       getResponder().getCommunicator().sendNormalServerMessage("You successfully gain a new affinity in " + s.getName() + ".");
/*    */     }
/*    */     else {
/*    */       
/* 62 */       getResponder().getCommunicator().sendNormalServerMessage("You decide against gaining a new skill affinity.");
/*    */       return;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 70 */     ArrayList<String> options = new ArrayList<>();
/* 71 */     options.add("None");
/* 72 */     for (Skill s : getResponder().getSkills().getSkills()) {
/* 73 */       if (s.affinity < 5)
/* 74 */         options.add(s.getName()); 
/* 75 */     }  this.allOptions = new String[options.size()];
/* 76 */     for (int i = 0; i < this.allOptions.length; i++) {
/* 77 */       this.allOptions[i] = options.get(i);
/*    */     }
/* 79 */     BMLBuilder toSend = BMLBuilder.createBMLBorderPanel(null, 
/*    */         
/* 81 */         BMLBuilder.createHorizArrayNode(false)
/* 82 */         .addPassthrough("id", Integer.toString(getId()))
/* 83 */         .addLabel(""), 
/* 84 */         BMLBuilder.createCenteredNode(BMLBuilder.createVertArrayNode(false)
/* 85 */           .addText("\r\nChoose an affinity from the list below:\r\n\r\n", null, null, null, 200, 50)
/* 86 */           .addDropdown("affdrop", "0", this.allOptions)), null, 
/*    */         
/* 88 */         BMLBuilder.createCenteredNode(BMLBuilder.createHorizArrayNode(false)
/* 89 */           .addButton("close", "Cancel", 80, 20, true)
/* 90 */           .addLabel("", null, null, null, 20, 20)
/* 91 */           .addButton("send", "Accept", 80, 20, true)));
/*    */     
/* 93 */     getResponder().getCommunicator().sendBml(270, 150, true, false, toSend.toString(), 200, 200, 200, this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\AffinityQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
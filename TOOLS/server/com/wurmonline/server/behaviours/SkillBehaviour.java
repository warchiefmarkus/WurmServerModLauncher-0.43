/*    */ package com.wurmonline.server.behaviours;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.skills.Skill;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
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
/*    */ public class SkillBehaviour
/*    */   extends Behaviour
/*    */ {
/*    */   public SkillBehaviour() {
/* 35 */     super((short)42);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, Skill skill) {
/* 41 */     List<ActionEntry> toReturn = new LinkedList<>();
/*    */     
/* 43 */     return toReturn;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ActionEntry> getBehavioursFor(Creature performer, Skill skill) {
/* 49 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 50 */     return toReturn;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean action(Action act, Creature performer, Item source, Skill skill, short action, float counter) {
/* 57 */     return action(act, performer, skill, action, counter);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean action(Action act, Creature performer, Skill skill, short action, float counter) {
/* 63 */     if (action == 1)
/*    */     {
/* 65 */       performer.getCommunicator().sendNormalServerMessage("This is the skill " + skill
/* 66 */           .getName() + ". Use 'Find on Wurmpedia' to see an explanation.");
/*    */     }
/* 68 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\SkillBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
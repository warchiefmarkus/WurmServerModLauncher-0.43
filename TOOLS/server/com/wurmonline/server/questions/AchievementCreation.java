/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Achievement;
/*     */ import com.wurmonline.server.players.AchievementTemplate;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AchievementCreation
/*     */   extends Question
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(CreateZoneQuestion.class.getName());
/*  39 */   private LinkedList<AchievementTemplate> myAchievements = null;
/*  40 */   private AchievementTemplate toWorkOn = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AchievementCreation(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  51 */     super(aResponder, aTitle, aQuestion, 99, aResponder.getWurmId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  62 */     String edita = answers.getProperty("edita");
/*     */     
/*  64 */     boolean changing = (this.toWorkOn != null);
/*     */     
/*  66 */     if (this.toWorkOn == null) {
/*     */       
/*     */       try {
/*  69 */         if (edita != null && edita.length() > 0)
/*     */         {
/*  71 */           if (this.myAchievements != null) {
/*     */             
/*  73 */             int index = Integer.parseInt(edita);
/*  74 */             if (index >= this.myAchievements.size()) {
/*     */               
/*  76 */               changing = false;
/*     */             }
/*     */             else {
/*     */               
/*  80 */               this.toWorkOn = this.myAchievements.get(index);
/*     */               
/*  82 */               AchievementCreation m = new AchievementCreation(getResponder(), "Edit Achievement", "Achievement management", getTarget());
/*  83 */               m.myAchievements = this.myAchievements;
/*  84 */               m.toWorkOn = this.toWorkOn;
/*  85 */               m.sendQuestion();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         }
/*  91 */       } catch (NumberFormatException nfe) {
/*     */         
/*  93 */         getResponder().getCommunicator().sendNormalServerMessage("The values were incorrect.");
/*     */       } 
/*     */     }
/*  96 */     String name = answers.getProperty("newName");
/*  97 */     String description = answers.getProperty("newDesc");
/*     */     
/*  99 */     String triggerOn = answers.getProperty("newTriggeron");
/* 100 */     int triggerInt = 1;
/*     */     
/*     */     try {
/* 103 */       if (triggerOn != null && triggerOn.length() > 0)
/*     */       {
/* 105 */         triggerInt = Integer.parseInt(triggerOn);
/*     */       }
/*     */     }
/* 108 */     catch (NumberFormatException nfe) {
/*     */       
/* 110 */       getResponder().getCommunicator().sendNormalServerMessage("The value for trigger on was incorrect.");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     if (this.toWorkOn == null) {
/*     */       
/* 118 */       if (name == null || name.length() < 2) {
/*     */         
/* 120 */         getResponder()
/* 121 */           .getCommunicator()
/* 122 */           .sendAlertServerMessage("The name " + name + " needs at least 2 characters.");
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 128 */       if (description == null || description.length() < 10) {
/*     */         
/* 130 */         getResponder()
/* 131 */           .getCommunicator()
/* 132 */           .sendAlertServerMessage("The description " + description + " needs at least 10 characters.");
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 138 */       String creator = getResponder().getName();
/* 139 */       if (getResponder().getPower() > 0) {
/* 140 */         creator = "GM " + creator;
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/* 145 */           Item ruler = Items.getItem(getTarget());
/* 146 */           if (ruler.getAuxData() < 10) {
/*     */             
/* 148 */             getResponder()
/* 149 */               .getCommunicator()
/* 150 */               .sendAlertServerMessage("The " + ruler
/*     */                 
/* 152 */                 .getName() + " needs at least 10 charges in order to create an Achievement. It currently contains " + ruler
/*     */                 
/* 154 */                 .getAuxData() + " charges.");
/*     */             
/*     */             return;
/*     */           } 
/* 158 */           ruler.setAuxData((byte)(ruler.getAuxData() - 10));
/*     */         }
/* 160 */         catch (NoSuchItemException nsi) {
/*     */           
/* 162 */           getResponder()
/* 163 */             .getCommunicator()
/* 164 */             .sendAlertServerMessage("Failed to locate the item for that request.");
/*     */           return;
/*     */         } 
/*     */       } 
/* 168 */       this.toWorkOn = new AchievementTemplate(AchievementTemplate.getNextAchievementId(), name, false, triggerInt, description, creator, false, false);
/*     */ 
/*     */ 
/*     */       
/* 172 */       getResponder()
/* 173 */         .getCommunicator()
/* 174 */         .sendSafeServerMessage("You successfully create the Achievement " + this.toWorkOn.getName() + ".");
/*     */       
/*     */       return;
/*     */     } 
/* 178 */     String delete = answers.getProperty("deletecb");
/* 179 */     if (delete != null && delete.equals("true"))
/*     */     {
/* 181 */       if (changing)
/*     */       {
/* 183 */         if (this.toWorkOn != null) {
/*     */           
/* 185 */           this.toWorkOn.delete();
/* 186 */           getResponder()
/* 187 */             .getCommunicator()
/* 188 */             .sendSafeServerMessage("You successfully delete the Achievement " + this.toWorkOn.getName() + ".");
/*     */           return;
/*     */         } 
/*     */       }
/*     */     }
/* 193 */     changing = false;
/* 194 */     if (name != null && name.length() > 0 && 
/* 195 */       !name.equals(this.toWorkOn.getName())) {
/*     */       
/* 197 */       changing = true;
/* 198 */       this.toWorkOn.setName(name);
/*     */     } 
/* 200 */     if (description != null && description.length() > 0 && 
/* 201 */       !description.equals(this.toWorkOn.getDescription())) {
/*     */       
/* 203 */       changing = true;
/* 204 */       this.toWorkOn.setDescription(description);
/*     */     } 
/* 206 */     if (triggerInt != this.toWorkOn.getTriggerOnCounter()) {
/*     */       
/* 208 */       changing = true;
/* 209 */       this.toWorkOn.setTriggerOnCounter(triggerInt);
/*     */     } 
/* 211 */     if (changing) {
/* 212 */       getResponder()
/* 213 */         .getCommunicator()
/* 214 */         .sendSafeServerMessage("You successfully update the Achievement " + this.toWorkOn.getName() + ".");
/*     */     } else {
/* 216 */       getResponder()
/* 217 */         .getCommunicator()
/* 218 */         .sendSafeServerMessage("You change nothing on the Achievement " + this.toWorkOn.getName() + ".");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 229 */     StringBuilder buf = new StringBuilder();
/* 230 */     buf.append(getBmlHeader());
/* 231 */     buf.append("text{text=\"\"}");
/* 232 */     if (this.toWorkOn == null) {
/*     */       
/* 234 */       this.myAchievements = Achievement.getSteelAchievements(getResponder());
/* 235 */       if (this.myAchievements != null && this.myAchievements.size() > 0) {
/*     */         
/* 237 */         buf.append("text{text=\"Select if you wish to edit an existing Achievement:\"};");
/*     */         
/* 239 */         buf.append("harray{label{text='Existing achievements'}dropdown{id='edita';options=\"");
/* 240 */         for (AchievementTemplate template : this.myAchievements)
/*     */         {
/* 242 */           buf.append(template.getName() + " (" + template.getCreator() + "),");
/*     */         }
/* 244 */         buf.append("None");
/* 245 */         buf.append("\"}}");
/*     */         
/* 247 */         buf.append("text{text=\"\"}");
/*     */       } 
/*     */     } 
/* 250 */     String oldName = "";
/* 251 */     if (this.toWorkOn != null) {
/*     */       
/* 253 */       oldName = this.toWorkOn.getName();
/* 254 */       buf.append("checkbox{id='deletecb';selected='false';text='Delete the selected achievement'}");
/*     */     } 
/* 256 */     buf.append("text{text=\"What name should the Achievement have?\"};");
/* 257 */     buf.append("harray{input{maxchars='40';id='newName';text=\"" + oldName + "\"};label{text=\" Name\"}}");
/*     */     
/* 259 */     String oldDesc = "";
/* 260 */     if (this.toWorkOn != null)
/* 261 */       oldDesc = this.toWorkOn.getDescription(); 
/* 262 */     buf.append("text{text=\"What description should the Achievement show?\"};");
/* 263 */     buf.append("harray{input{maxchars='200';id='newDesc';text=\"" + oldDesc + "\"};label{text=\" Description\"}}");
/* 264 */     buf.append("text{text=\"\"}");
/*     */     
/* 266 */     int oldTriggerOn = 1;
/* 267 */     if (this.toWorkOn != null)
/* 268 */       oldTriggerOn = this.toWorkOn.getTriggerOnCounter(); 
/* 269 */     buf.append("text{text=\"On which count should the Achievement trigger? Usually the trigger should be 1.\"};");
/* 270 */     buf.append("harray{input{maxchars='5';id='newTriggeron';text=\"" + oldTriggerOn + "\"};label{text=\" Triggered effects\"}}");
/*     */ 
/*     */     
/* 273 */     buf.append(createAnswerButton2());
/* 274 */     getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\AchievementCreation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
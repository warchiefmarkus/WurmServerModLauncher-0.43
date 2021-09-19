/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.Properties;
/*     */ import java.util.Random;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AscensionQuestion
/*     */   extends Question
/*     */   implements MiscConstants
/*     */ {
/*     */   private final String deityName;
/*     */   
/*     */   public AscensionQuestion(Creature aResponder, long aTarget, String _deityName) {
/*  45 */     super(aResponder, "Ascension", "Are you prepared to become a DEMIGOD?", 91, aTarget);
/*  46 */     this.deityName = _deityName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswers) {
/*  57 */     setAnswer(aAnswers);
/*  58 */     QuestionParser.parseAscensionQuestion(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final String getNewPrefix(String currentName) {
/*  63 */     int hash = currentName.hashCode();
/*  64 */     Random r = new Random(hash);
/*  65 */     int result = r.nextInt(20);
/*  66 */     String prefix = "Evi";
/*  67 */     switch (result) {
/*     */       
/*     */       case 0:
/*  70 */         prefix = "Ana";
/*     */         break;
/*     */       case 1:
/*  73 */         prefix = "Anti";
/*     */         break;
/*     */       case 2:
/*  76 */         prefix = "Dega";
/*     */         break;
/*     */       case 3:
/*  79 */         prefix = "Deri";
/*     */         break;
/*     */       case 4:
/*  82 */         prefix = "Raxa";
/*     */         break;
/*     */       case 5:
/*  85 */         prefix = "Meni";
/*     */         break;
/*     */       case 6:
/*  88 */         prefix = "Doco";
/*     */         break;
/*     */       case 7:
/*  91 */         prefix = "Dedi";
/*     */         break;
/*     */       case 8:
/*  94 */         prefix = "Ani";
/*     */         break;
/*     */       case 9:
/*  97 */         prefix = "Mono";
/*     */         break;
/*     */       case 10:
/* 100 */         prefix = "Hani";
/*     */         break;
/*     */       case 11:
/* 103 */         prefix = "Vidi";
/*     */         break;
/*     */       case 12:
/* 106 */         prefix = "Zase";
/*     */         break;
/*     */       case 13:
/* 109 */         prefix = "Omo";
/*     */         break;
/*     */       case 14:
/* 112 */         prefix = "Lono";
/*     */         break;
/*     */       case 15:
/* 115 */         prefix = "Togo";
/*     */         break;
/*     */       case 16:
/* 118 */         prefix = "Paly";
/*     */         break;
/*     */       case 17:
/* 121 */         prefix = "Parme";
/*     */         break;
/*     */       case 18:
/* 124 */         prefix = "Daga";
/*     */         break;
/*     */       case 19:
/* 127 */         prefix = "Jora";
/*     */         break;
/*     */       case 20:
/* 130 */         prefix = "Easy";
/*     */         break;
/*     */       case 21:
/* 133 */         prefix = "High";
/*     */         break;
/*     */       case 22:
/* 136 */         prefix = "Sta";
/*     */         break;
/*     */       case 23:
/* 139 */         prefix = "Cha";
/*     */         break;
/*     */       case 24:
/* 142 */         prefix = "Flo";
/*     */         break;
/*     */       case 25:
/* 145 */         prefix = "Tru";
/*     */         break;
/*     */       default:
/* 148 */         prefix = "Nami";
/*     */         break;
/*     */     } 
/*     */     
/* 152 */     if ("aeiouyAEIOUY".contains(currentName.substring(0, 1)))
/*     */     {
/* 154 */       prefix = prefix.substring(0, prefix.length() - 1);
/*     */     }
/*     */     
/* 157 */     return prefix;
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
/*     */   public void sendQuestion() {
/* 169 */     StringBuilder buf = new StringBuilder();
/* 170 */     buf.append(getBmlHeader());
/* 171 */     buf.append("text{type=\"bold\";text=\"YOU ONLY HAVE 13 MINUTES TO HANDLE THIS SO LISTEN WELL:\"};");
/* 172 */     buf.append("text{text=\"" + this.deityName + " has given you a FANTASTIC AND UNIQUE CHANCE chance to become a demigod!\"};");
/* 173 */     buf.append("text{text=\"This would require that part of your soul ascends to Valrei, the home of the gods.\"};");
/* 174 */     buf.append("text{text=''}");
/* 175 */     buf
/* 176 */       .append("text{text=\"The demigod" + getResponder().getName() + " would be in charge of and protecting a special area of Valrei and aiding your deity.\"};");
/*     */     
/* 178 */     buf.append("text{text=''}");
/*     */     
/* 180 */     buf.append("text{text=\"Nothing will be changed on your current character except the name.\"};");
/* 181 */     buf.append("text{text=''}");
/* 182 */     buf.append("text{text=\"" + getResponder().getName() + " will play on as a demigod of " + this.deityName + " and if all goes well be elevated to true Deity.\"};");
/*     */     
/* 184 */     buf
/* 185 */       .append("text{text=\"If a demigod later becomes a true Deity it will travel Valrei itself and also have its own very special religion.\"};");
/* 186 */     buf
/* 187 */       .append("text{text=\"There are no promises made that " + this.deityName + " will be able to elevate your demigod to a true Deity and In the worst case the demigod is even killed or demoted.\"};");
/*     */ 
/*     */     
/* 190 */     buf.append("text{text=''}");
/*     */     
/* 192 */     buf
/* 193 */       .append("text{text=\"One thing remains clear though: whatever happens your demigod will play a very important part in the history of Wurm.\"};");
/* 194 */     buf.append("text{text=''}");
/* 195 */     buf
/* 196 */       .append("text{text=\"The chance is gone if you have not answered Yes within cirka 13 minutes of receiving this notice.\"};");
/* 197 */     buf.append("text{text=''}");
/* 198 */     buf.append("text{type='italic';text='Do you want " + getResponder().getName() + " to become a demigod of " + this.deityName + "?'}");
/*     */     
/* 200 */     buf.append("text{text=''}");
/*     */     
/* 202 */     buf.append("radio{ group='demig'; id='true';text='Yes'}");
/* 203 */     buf.append("radio{ group='demig'; id='false';text='No';selected='true'}");
/* 204 */     buf.append(createAnswerButton2());
/*     */     
/* 206 */     getResponder().getCommunicator().sendBml(500, 600, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\AscensionQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
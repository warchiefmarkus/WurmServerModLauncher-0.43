/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.Properties;
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
/*     */ public final class WurmInfo
/*     */   extends Question
/*     */ {
/*     */   public WurmInfo(Creature aResponder) {
/*  34 */     super(aResponder, "Cooking test info", "Change test info", 15, -10L);
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
/*     */   public void answer(Properties answers) {}
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
/*  56 */     StringBuilder buf = new StringBuilder();
/*  57 */     buf.append(getBmlHeader());
/*  58 */     buf.append("label{type=\"bold\";text=\"New command\"}");
/*  59 */     buf.append("label{text=\"/toggleccfp\"}");
/*  60 */     buf.append("label{text=\"    will toggle visibility of the ccfp bar.\"}");
/*  61 */     buf.append("label{text=\"\"}");
/*  62 */     buf.append("label{type=\"bold\";text=\"Cooking Test commands\"}");
/*  63 */     buf.append("label{text=\"/changelog\"}");
/*  64 */     buf.append("label{text=\"    will show the full changelog.\"}");
/*  65 */     buf.append("label{text=\"/info\"}");
/*  66 */     buf.append("label{text=\"    will show this information.\"}");
/*  67 */     buf.append("label{text=\"/news\"}");
/*  68 */     buf.append("label{text=\"    will show each update if i remember to change it.\"}");
/*  69 */     buf.append("label{text=\"/resetccfp\"}");
/*  70 */     buf.append("label{text=\"    will reset your CCFP Values to zero, will not show immediatly, \"}");
/*  71 */     buf.append("label{text=\"    as needs a hunger change before the values are sent to client.\"}");
/*  72 */     buf.append("label{text=\"/resetfood\"}");
/*  73 */     buf.append("label{text=\"    to set your food to approx 31%.\"}");
/*  74 */     buf.append("label{text=\"    does not set to zero as may cause issues with fat layers.\"}");
/*  75 */     buf.append("label{text=\"/resetthirst\"}");
/*  76 */     buf.append("label{text=\"    to set your water bar to approx 0%.\"}");
/*     */     
/*  78 */     if (getResponder().getPower() >= 2) {
/*     */       
/*  80 */       buf.append("label{text=\"#listwildhives x\"}");
/*  81 */       buf.append("label{text=\"    Lists the hives that have two queens, does not matter what the parm is.\"}");
/*  82 */       buf.append("label{text=\"    Will give wild hives then domestic ones together with x,y.\"}");
/*  83 */       buf.append("label{text=\"#listwildhives\"}");
/*  84 */       buf.append("label{text=\"    List of wild hives and any honey in them!\"}");
/*  85 */       buf.append("label{text=\"    Will give wild hives only with their x and y, amount of honey, wax and number of queens.\"}");
/*  86 */       buf.append("label{text=\"#removewildhives\"}");
/*  87 */       buf.append("label{text=\"    Will remove all wild hives and anything in them.\"}");
/*  88 */       buf.append("label{text=\"    SO USE WITH CARE!!\"}");
/*  89 */       buf.append("label{text=\"    Outputs the list of hives that were destroyed.\"}");
/*  90 */       buf.append("label{text=\"#removeknownrecipes [name]\"}");
/*  91 */       buf.append("label{text=\"    Removes known recipes from the specified player, if name is specified.\"}");
/*  92 */       buf.append("label{text=\"    If no name given, then will remove all known recipes from everyone...\"}");
/*  93 */       buf.append("label{text=\"    SO USE WITH CARE!!\"}");
/*  94 */       buf.append("label{text=\"Note: New tab added for debugging wild hives.\"}");
/*     */     } 
/*  96 */     buf.append("label{text=\"\"}");
/*  97 */     buf.append("label{type=\"bold\";text=\"CCFP Bonuses...\"}");
/*  98 */     buf.append("label{text=\"    Calories => reduced stamina drain.\"}");
/*  99 */     buf.append("label{text=\"    Carbs => reduced water usage.\"}");
/* 100 */     buf.append("label{text=\"    Fats => increased favour regeneration.\"}");
/* 101 */     buf.append("label{text=\"    Proteins => reduced food usage.\"}");
/* 102 */     buf.append("label{text=\"\"}");
/* 103 */     buf.append("label{type=\"bold\";text=\"Result Bonus value...\"}");
/* 104 */     buf.append("label{text=\"It is calculated from each ingredients bonus, plus\"}");
/* 105 */     buf.append("label{text=\"    Each ingredient template id, real template, pstate, cstate, material\"}");
/* 106 */     buf.append("label{text=\"    plus Cooker template id,\"}");
/* 107 */     buf.append("label{text=\"    plus Container template id.\"}");
/* 108 */     buf.append("label{text=\"Note: Fresh ingredient.v.non-fresh will give a different bonus.\"}");
/* 109 */     buf.append("label{text=\"      Fresh state gets lost when you put something in a fsb.\"}");
/* 110 */     buf.append("label{text=\"Also when the result item is eaten, it adds in your player id, and uses that for a pointer \"}");
/* 111 */     buf.append("label{text=\"into the skill list, thus giving you a timed affinity.\"}");
/* 112 */     buf.append("label{text=\"Note: if the bonus was -1 on the item being eaten, then no timed affinity is given.\"}");
/*     */     
/* 114 */     buf.append(createAnswerButton2());
/* 115 */     getResponder().getCommunicator().sendBml(480, 500, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getInfo() {
/* 125 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\WurmInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
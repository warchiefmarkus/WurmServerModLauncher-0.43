/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.items.Recipe;
/*    */ import com.wurmonline.server.items.Recipes;
/*    */ import com.wurmonline.shared.constants.ItemMaterials;
/*    */ import java.util.Arrays;
/*    */ import java.util.Comparator;
/*    */ import java.util.Properties;
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
/*    */ public final class WriteRecipeQuestion
/*    */   extends Question
/*    */   implements ItemMaterials
/*    */ {
/*    */   private Recipe[] recipes;
/*    */   private Item paper;
/*    */   
/*    */   public WriteRecipeQuestion(Creature aResponder, Item apaper) {
/* 42 */     super(aResponder, "Select Recipe", "Select Recipe", 138, -10L);
/* 43 */     this.paper = apaper;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void answer(Properties answers) {
/* 49 */     setAnswer(answers);
/* 50 */     String sel = answers.getProperty("recipe");
/* 51 */     int selId = Integer.parseInt(sel);
/* 52 */     Recipe recipe = this.recipes[selId];
/* 53 */     this.paper.setInscription(recipe, getResponder().getName(), 0);
/* 54 */     getResponder().getCommunicator().sendNormalServerMessage("You carefully finish writing the recipe \"" + recipe
/* 55 */         .getName() + "\" and sign it.");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendQuestion() {
/* 67 */     this.recipes = Recipes.getUnknownRecipes();
/* 68 */     Arrays.sort(this.recipes, new Comparator<Recipe>()
/*    */         {
/*    */           
/*    */           public int compare(Recipe param1, Recipe param2)
/*    */           {
/* 73 */             return param1.getName().compareTo(param2.getName());
/*    */           }
/*    */         });
/*    */     
/* 77 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 78 */     buf.append("harray{label{text=\"Recipe\"};");
/* 79 */     buf.append("dropdown{id=\"recipe\";default=\"0\";options=\"");
/* 80 */     for (int i = 0; i < this.recipes.length; i++) {
/*    */       
/* 82 */       if (i > 0)
/* 83 */         buf.append(","); 
/* 84 */       Recipe recipe = this.recipes[i];
/* 85 */       buf.append(recipe.getName().replace(",", "") + " - " + recipe.getRecipeId());
/*    */     } 
/* 87 */     buf.append("\"}}");
/* 88 */     buf.append("label{text=\"\"}");
/* 89 */     buf.append(createAnswerButton2());
/* 90 */     getResponder().getCommunicator().sendBml(300, 120, true, true, buf.toString(), 200, 200, 200, this.title);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\WriteRecipeQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
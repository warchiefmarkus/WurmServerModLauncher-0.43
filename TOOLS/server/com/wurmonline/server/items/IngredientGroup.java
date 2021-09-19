/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ public class IngredientGroup
/*     */ {
/*  32 */   private static final Logger logger = Logger.getLogger(IngredientGroup.class.getName());
/*     */   
/*     */   public static final byte INGREDIENT_GROUP_RESULT = -3;
/*     */   
/*     */   public static final byte INGREDIENT_GROUP_ACTIVE = -2;
/*     */   public static final byte INGREDIENT_GROUP_TARGET = -1;
/*     */   public static final byte INGREDIENT_GROUP_NONE = 0;
/*     */   public static final byte INGREDIENT_GROUP_MANDATORY = 1;
/*     */   public static final byte INGREDIENT_GROUP_ZERO_OR_ONE = 2;
/*     */   public static final byte INGREDIENT_GROUP_ONE_OF = 3;
/*     */   public static final byte INGREDIENT_GROUP_ONE_OR_MORE = 4;
/*     */   public static final byte INGREDIENT_GROUP_OPTIONAL = 5;
/*     */   public static final byte INGREDIENT_GROUP_ANY = 6;
/*  45 */   private final Map<String, Ingredient> ingredientsByName = new HashMap<>();
/*     */   
/*  47 */   private final Map<Integer, Ingredient> ingredients = new HashMap<>();
/*     */   private final byte groupType;
/*  49 */   private int groupDifficulty = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IngredientGroup(byte groupType) {
/*  58 */     this.groupType = groupType;
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
/*     */   public IngredientGroup(DataInputStream dis) throws IOException, NoSuchTemplateException {
/*  70 */     this.groupType = dis.readByte();
/*  71 */     byte icount = dis.readByte();
/*  72 */     for (int i = 0; i < icount; i++) {
/*  73 */       add(new Ingredient(dis));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pack(DataOutputStream dos) throws IOException {
/*  84 */     dos.writeByte(this.groupType);
/*  85 */     dos.writeByte(this.ingredients.size());
/*  86 */     for (Ingredient ii : this.ingredients.values()) {
/*  87 */       ii.pack(dos);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Ingredient ingredient) {
/*  97 */     if (this.groupType == 1)
/*  98 */       this.ingredientsByName.put(ingredient.getTemplateName(), ingredient); 
/*  99 */     this.ingredients.put(Integer.valueOf(ingredient.getIngredientId()), ingredient);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getGroupType() {
/* 108 */     return this.groupType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGroupTypeName() {
/* 117 */     switch (this.groupType) {
/*     */       
/*     */       case 1:
/* 120 */         return "Mandatory";
/*     */       case 2:
/* 122 */         return "Zero or one";
/*     */       case 3:
/* 124 */         return "One of";
/*     */       case 4:
/* 126 */         return "One or more";
/*     */       case 5:
/* 128 */         return "Optional";
/*     */       case 6:
/* 130 */         return "Any";
/*     */     } 
/* 132 */     return "unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ingredient[] getIngredients() {
/* 141 */     return (Ingredient[])this.ingredients.values().toArray((Object[])new Ingredient[this.ingredients.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 150 */     return this.ingredients.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(String ingredientName) {
/* 160 */     return this.ingredientsByName.containsKey(ingredientName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Ingredient getIngredientByName(String ingredientName) {
/* 171 */     return this.ingredientsByName.get(ingredientName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void clearFound() {
/* 180 */     for (Ingredient i : this.ingredients.values())
/* 181 */       i.setFound(false); 
/* 182 */     this.groupDifficulty = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean matches(Item item) {
/* 193 */     for (Ingredient i : this.ingredients.values()) {
/*     */       
/* 195 */       if (i.matches(item)) {
/*     */         
/* 197 */         this.groupDifficulty += i.setFound(true);
/* 198 */         return true;
/*     */       } 
/*     */     } 
/* 201 */     return false;
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
/*     */ 
/*     */ 
/*     */   
/*     */   boolean wasFound() {
/* 216 */     int count = 0;
/* 217 */     switch (this.groupType) {
/*     */       
/*     */       case 1:
/* 220 */         for (Ingredient i : this.ingredients.values()) {
/*     */           
/* 222 */           if (!i.wasFound(false, false))
/* 223 */             return false; 
/*     */         } 
/* 225 */         return true;
/*     */       
/*     */       case 2:
/* 228 */         return (getFound(false) <= 1);
/*     */       
/*     */       case 3:
/* 231 */         return (getFound(false) == 1);
/*     */       
/*     */       case 4:
/* 234 */         return (getFound(true) >= 1);
/*     */ 
/*     */       
/*     */       case 6:
/* 238 */         return true;
/*     */       
/*     */       case 5:
/* 241 */         for (Ingredient i : this.ingredients.values()) {
/*     */           
/* 243 */           if (!i.wasFound(false, true))
/* 244 */             return false; 
/*     */         } 
/* 246 */         return true;
/*     */     } 
/* 248 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getFound(boolean any) {
/* 257 */     int count = 0;
/* 258 */     for (Ingredient i : this.ingredients.values()) {
/*     */       
/* 260 */       if (i.wasFound(any, false))
/* 261 */         count++; 
/*     */     } 
/* 263 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getGroupDifficulty() {
/* 272 */     return this.groupDifficulty;
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
/*     */   protected IngredientGroup clone() {
/* 284 */     IngredientGroup ig = new IngredientGroup(this.groupType);
/* 285 */     return ig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 296 */     StringBuilder buf = new StringBuilder();
/* 297 */     buf.append("{group:" + getGroupTypeName() + "(" + getGroupType() + ")");
/* 298 */     boolean first = true;
/* 299 */     for (Ingredient i : this.ingredients.values()) {
/*     */       
/* 301 */       if (first) {
/* 302 */         first = false;
/*     */       } else {
/* 304 */         buf.append(",");
/* 305 */       }  buf.append(i.toString());
/*     */     } 
/* 307 */     buf.append("}");
/* 308 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\IngredientGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
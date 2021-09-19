/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.creatures.CreatureTemplate;
/*      */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Ingredient
/*      */   implements MiscConstants
/*      */ {
/*   39 */   private static final Logger logger = Logger.getLogger(Ingredient.class.getName());
/*      */   
/*      */   private ItemTemplate itemTemplate;
/*      */   
/*      */   private final boolean isResult;
/*   44 */   private String cstateName = "";
/*   45 */   private byte cstate = -1;
/*   46 */   private String pstateName = "";
/*   47 */   private byte pstate = -1;
/*      */   
/*   49 */   private int amount = 0;
/*   50 */   private int ratio = 0;
/*      */   
/*   52 */   private String materialName = "";
/*   53 */   private byte material = -1;
/*      */   
/*      */   private boolean hasRealTemplate = false;
/*   56 */   private ItemTemplate realItemTemplate = null;
/*      */   
/*   58 */   private String corpseDataName = "";
/*   59 */   private int corpseData = -1;
/*      */   
/*   61 */   private String materialRef = "";
/*   62 */   private String realTemplateRef = "";
/*      */   
/*   64 */   private int difficulty = 0;
/*      */   
/*   66 */   private int loss = 0;
/*      */   
/*   68 */   private String resultName = "";
/*   69 */   private String resultDescription = "";
/*      */   
/*      */   private boolean useResultTemplateWeight = false;
/*      */   
/*      */   private byte groupId;
/*   74 */   private byte ingredientId = -1;
/*      */   
/*   76 */   private int found = 0;
/*   77 */   private short icon = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ingredient(ItemTemplate itemTemplate, boolean isResult, byte groupId) {
/*   84 */     this.itemTemplate = itemTemplate;
/*   85 */     this.isResult = isResult;
/*   86 */     if (isResult)
/*   87 */       this.difficulty = -100; 
/*   88 */     this.groupId = groupId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ingredient(int templateId, byte cstate, byte pstate, byte material, boolean hasRealTemplate, int realTemplateId, int corpseType) throws NoSuchTemplateException {
/*  104 */     this.itemTemplate = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*  105 */     this.isResult = false;
/*  106 */     this.groupId = 0;
/*  107 */     this.cstate = cstate;
/*  108 */     this.cstateName = generateCookingStateName(cstate);
/*  109 */     this.pstate = pstate;
/*  110 */     this.pstateName = generatePhysicalStateName(pstate);
/*  111 */     this.material = material;
/*  112 */     this.materialName = generateMaterialName();
/*  113 */     this.hasRealTemplate = hasRealTemplate;
/*  114 */     if (realTemplateId != -10L)
/*      */     {
/*  116 */       this.realItemTemplate = ItemTemplateFactory.getInstance().getTemplate(realTemplateId);
/*      */     }
/*  118 */     this.corpseData = corpseType;
/*  119 */     this.corpseDataName = generateCorpseName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ingredient(DataInputStream dis) throws IOException, NoSuchTemplateException {
/*  132 */     this.ingredientId = dis.readByte();
/*  133 */     this.groupId = dis.readByte();
/*      */     
/*  135 */     short templateId = dis.readShort();
/*  136 */     this.itemTemplate = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*  137 */     this.isResult = false;
/*  138 */     this.cstate = dis.readByte();
/*  139 */     this.cstateName = generateCookingStateName(this.cstate);
/*  140 */     this.pstate = dis.readByte();
/*  141 */     this.pstateName = generatePhysicalStateName(this.pstate);
/*  142 */     this.material = dis.readByte();
/*  143 */     this.materialName = generateMaterialName();
/*  144 */     if (templateId == 272) {
/*      */       
/*  146 */       this.corpseData = dis.readShort();
/*  147 */       this.corpseDataName = generateCorpseName();
/*      */     }
/*      */     else {
/*      */       
/*  151 */       this.hasRealTemplate = dis.readBoolean();
/*  152 */       short realItemTemplateId = dis.readShort();
/*  153 */       if (realItemTemplateId != -10L) {
/*  154 */         this.realItemTemplate = ItemTemplateFactory.getInstance().getTemplate(realItemTemplateId);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void pack(DataOutputStream dos) throws IOException {
/*  167 */     dos.writeByte(this.ingredientId);
/*  168 */     dos.writeByte(this.groupId);
/*  169 */     dos.writeShort(this.itemTemplate.getTemplateId());
/*  170 */     dos.writeByte(this.cstate);
/*  171 */     dos.writeByte(this.pstate);
/*  172 */     dos.writeByte(this.material);
/*  173 */     if (this.itemTemplate.getTemplateId() == 272) {
/*      */       
/*  175 */       dos.writeShort(this.corpseData);
/*      */     }
/*      */     else {
/*      */       
/*  179 */       dos.writeBoolean(this.hasRealTemplate);
/*  180 */       dos.writeShort(getRealTemplateId());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getGroupId() {
/*  190 */     return this.groupId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setIngredientId(byte ingredientId) {
/*  199 */     this.ingredientId = ingredientId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getIngredientId() {
/*  208 */     return this.ingredientId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean matches(Item item) {
/*  218 */     if ((this.itemTemplate.isFoodGroup() && item.getTemplate().getFoodGroup() == this.itemTemplate.getTemplateId()) || 
/*  219 */       getTemplateId() == item.getTemplateId())
/*      */     {
/*  221 */       if (checkState(item))
/*      */       {
/*  223 */         if (checkMaterial(item))
/*      */         {
/*  225 */           if (checkRealTemplate(item))
/*      */           {
/*  227 */             if (checkCorpseData(item))
/*  228 */               return true; 
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  233 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ingredient clone(@Nullable Item item) {
/*  245 */     Ingredient ingredient = new Ingredient((item != null) ? item.getTemplate() : this.itemTemplate, this.isResult, this.groupId);
/*  246 */     ingredient.setIngredientId(this.ingredientId);
/*      */     
/*  248 */     if (item != null)
/*  249 */       ingredient.setAmount(getAmount()); 
/*  250 */     ingredient.setRatio(getRatio());
/*  251 */     ingredient.setLoss(getLoss());
/*  252 */     if (hasCState())
/*      */     {
/*      */       
/*  255 */       ingredient.setCState(this.cstate, this.cstateName);
/*      */     }
/*  257 */     if (hasPState())
/*      */     {
/*      */       
/*  260 */       ingredient.setPState(this.pstate, this.pstateName);
/*      */     }
/*  262 */     if (hasMaterial())
/*      */     {
/*      */       
/*  265 */       ingredient.setMaterial(this.material, this.materialName);
/*      */     }
/*  267 */     if (hasRealTemplate())
/*      */     {
/*      */       
/*  270 */       ingredient.setRealTemplate(this.realItemTemplate);
/*      */     }
/*  272 */     if (hasCorpseData())
/*      */     {
/*      */       
/*  275 */       ingredient.setCorpseData(this.corpseData, this.corpseDataName);
/*      */     }
/*  277 */     return ingredient;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResultName(String resultName) {
/*  286 */     this.resultName = resultName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getResultName() {
/*  294 */     return this.resultName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName(boolean withAmount) {
/*  303 */     StringBuilder buf = new StringBuilder();
/*  304 */     if (hasCState()) {
/*      */       
/*  306 */       buf.append(this.cstateName);
/*  307 */       if (hasPState() && this.pstateName.length() > 0)
/*  308 */         buf.append("+" + this.pstateName); 
/*  309 */       buf.append(" ");
/*      */     }
/*  311 */     else if (hasPState() && this.pstateName.length() > 0) {
/*  312 */       buf.append(this.pstateName + " ");
/*      */     } 
/*  314 */     if (hasCorpseData())
/*  315 */       buf.append(this.corpseDataName + " "); 
/*  316 */     buf.append(this.itemTemplate.getName());
/*  317 */     if (hasMaterial()) {
/*  318 */       buf.append(" (" + this.materialName + ")");
/*      */     }
/*  320 */     if (hasRealTemplate());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  326 */     if (withAmount && getAmount() > 1)
/*      */     {
/*  328 */       if (getAmount() > 2) {
/*      */         
/*  330 */         buf.append(" (x3+)");
/*      */       } else {
/*      */         
/*  333 */         buf.append(" (x" + getAmount() + ")");
/*      */       }  } 
/*  335 */     return buf.toString().trim();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getSubMenuName() {
/*  343 */     if (this.resultName.length() > 0)
/*  344 */       return this.resultName; 
/*  345 */     if (hasCState()) {
/*      */       
/*  347 */       StringBuilder buf = new StringBuilder();
/*  348 */       buf.append(this.cstateName);
/*  349 */       if (hasPState())
/*  350 */         buf.append("+" + this.pstateName); 
/*  351 */       buf.append(" ");
/*  352 */       buf.append(this.itemTemplate.getName());
/*  353 */       return buf.toString();
/*      */     } 
/*  355 */     if (hasPState())
/*  356 */       return this.pstateName + " " + this.itemTemplate.getName(); 
/*  357 */     return this.itemTemplate.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getCStateName() {
/*  366 */     return this.cstateName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getPStateName() {
/*  375 */     return this.pstateName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setUseResultTemplateWeight(boolean useTemplateWeight) {
/*  385 */     this.useResultTemplateWeight = useTemplateWeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean useResultTemplateWeight() {
/*  395 */     return this.useResultTemplateWeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setResultDescription(String description) {
/*  404 */     this.resultDescription = description;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean hasResultDescription() {
/*  412 */     return (this.resultDescription.length() > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getResultDescription() {
/*  421 */     if (this.resultDescription.length() > 0)
/*  422 */       return this.resultDescription; 
/*  423 */     return this.itemTemplate.getDescriptionLong();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getResultDescription(Item resultItem) {
/*  433 */     if (this.resultDescription.length() > 0) {
/*      */       
/*  435 */       String desc = this.resultDescription;
/*  436 */       if (desc.indexOf('#') >= 0)
/*      */       {
/*      */ 
/*      */         
/*  440 */         if (resultItem.getRealTemplateId() != -10 && resultItem.getRealTemplate() != null) {
/*  441 */           desc = desc.replace("#", resultItem.getRealTemplate().getName());
/*      */         } else {
/*  443 */           desc = desc.replace("# ", "").replace(" #", "");
/*      */         }  } 
/*  445 */       if (desc.indexOf('$') >= 0)
/*      */       {
/*      */         
/*  448 */         desc = desc.replace("$", generateMaterialName(resultItem.getMaterial()));
/*      */       }
/*  450 */       return desc;
/*      */     } 
/*  452 */     return this.itemTemplate.getDescriptionLong();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaterialRef(String materialRef) {
/*  462 */     this.materialRef = materialRef;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMaterialRef() {
/*  471 */     return this.materialRef;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasMaterialRef() {
/*  479 */     return (this.materialRef.length() > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRealTemplateRef(String realTemplateRef) {
/*  489 */     this.realTemplateRef = realTemplateRef;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getRealTemplateRef() {
/*  498 */     return this.realTemplateRef;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasRealTemplateRef() {
/*  506 */     return (this.realTemplateRef.length() > 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCorpseData(int data) {
/*  511 */     this.corpseData = data;
/*  512 */     this.corpseDataName = generateCorpseName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCorpseData(int data, String dataName) {
/*  523 */     this.corpseData = data;
/*  524 */     this.corpseDataName = dataName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCorpseData() {
/*  532 */     return this.corpseData;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasCorpseData() {
/*  540 */     return (this.corpseData != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCorpseName() {
/*  549 */     return this.corpseDataName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCState(byte state) {
/*  558 */     this.cstate = state;
/*  559 */     this.cstateName = generateCookingStateName(this.cstate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCState(byte state, String stateName) {
/*  569 */     this.cstate = state;
/*  570 */     this.cstateName = generateCookingStateName(this.cstate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getCState() {
/*  578 */     return this.cstate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPState(byte state) {
/*  587 */     this.pstate = state;
/*  588 */     this.pstateName = generatePhysicalStateName(this.pstate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPState(byte state, String stateName) {
/*  598 */     this.pstate = state;
/*  599 */     this.pstateName = generatePhysicalStateName(this.pstate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getPState() {
/*  607 */     return this.pstate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasCState() {
/*  615 */     return (this.cstate != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasPState() {
/*  623 */     return (this.pstate != -1);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getXState() {
/*  628 */     if (hasCState()) {
/*      */       
/*  630 */       if (hasPState())
/*      */       {
/*  632 */         return (byte)(getCState() + getPState());
/*      */       }
/*  634 */       return getCState();
/*      */     } 
/*      */     
/*  637 */     return getPState();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasXState() {
/*  642 */     return (this.cstate != -1 || this.pstate != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasRealTemplate() {
/*  650 */     return this.hasRealTemplate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasRealTemplateId() {
/*  658 */     return (this.realItemTemplate != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAmount(int numb) {
/*  667 */     this.amount = numb;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAmount() {
/*  675 */     if (this.amount == 0)
/*  676 */       return 1; 
/*  677 */     return this.amount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRatio(int numb) {
/*  687 */     this.ratio = numb;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRatio() {
/*  695 */     return this.ratio;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaterial(byte material) {
/*  704 */     this.material = material;
/*  705 */     this.materialName = generateMaterialName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaterial(byte material, String materialName) {
/*  715 */     this.material = material;
/*  716 */     this.materialName = generateMaterialName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getMaterial() {
/*  724 */     return this.material;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMaterialName() {
/*  732 */     return this.materialName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasMaterial() {
/*  740 */     return (this.material != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDifficulty(int difficulty) {
/*  750 */     this.difficulty = difficulty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDifficulty() {
/*  759 */     return this.difficulty;
/*      */   }
/*      */ 
/*      */   
/*      */   void setIcon(short icon) {
/*  764 */     this.icon = icon;
/*      */   }
/*      */ 
/*      */   
/*      */   public short getIcon() {
/*  769 */     if (this.icon > -1)
/*  770 */       return this.icon; 
/*  771 */     return this.itemTemplate.getImageNumber();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLoss(int loss) {
/*  780 */     this.loss = Math.min(100, Math.max(0, loss));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLoss() {
/*  790 */     return this.loss;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTemplate(ItemTemplate itemTemplate) {
/*  799 */     this.itemTemplate = itemTemplate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRealTemplate(@Nullable ItemTemplate itemTemplate) {
/*  808 */     this.realItemTemplate = itemTemplate;
/*  809 */     this.hasRealTemplate = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public ItemTemplate getRealItemTemplate() {
/*  818 */     return this.realItemTemplate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemTemplate getTemplate() {
/*  826 */     return this.itemTemplate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTemplateName() {
/*  834 */     return this.itemTemplate.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTemplateId() {
/*  842 */     return this.itemTemplate.getTemplateId();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRealTemplateId() {
/*  850 */     if (this.realItemTemplate == null)
/*  851 */       return -10; 
/*  852 */     return this.realItemTemplate.getTemplateId();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLiquid() {
/*  860 */     return this.itemTemplate.isLiquid();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDrinkable() {
/*  868 */     return this.itemTemplate.drinkable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFoodGroup() {
/*  876 */     return this.itemTemplate.isFoodGroup();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int setFound(boolean found) {
/*  886 */     if (found) {
/*  887 */       this.found++;
/*      */     } else {
/*  889 */       this.found = 0;
/*  890 */     }  return this.difficulty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean wasFound(boolean any, boolean optional) {
/*  898 */     if (any || this.itemTemplate.isLiquid())
/*  899 */       return (this.found > 0 || optional); 
/*  900 */     if (this.amount >= 3 && this.found >= 3)
/*  901 */       return true; 
/*  902 */     if (this.found == 0)
/*  903 */       return optional; 
/*  904 */     return (this.amount == this.found || (this.amount == 0 && this.found == 1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFound() {
/*  913 */     return this.found;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean checkFoodGroup(Item target) {
/*  918 */     if (isFoodGroup())
/*  919 */       return (target.getTemplate().getFoodGroup() == getTemplateId()); 
/*  920 */     return (target.getTemplateId() == getTemplateId());
/*      */   }
/*      */ 
/*      */   
/*      */   boolean checkState(Item target) {
/*  925 */     if (hasCState() || hasPState())
/*  926 */       return target.isCorrectFoodState(getCState(), getPState()); 
/*  927 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean checkMaterial(Item target) {
/*  932 */     if (hasMaterial())
/*  933 */       return (getMaterial() == target.getMaterial()); 
/*  934 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean checkCorpseData(Item target) {
/*  939 */     if (hasCorpseData())
/*  940 */       return (getCorpseData() == target.getData1() && !target.isButchered()); 
/*  941 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean checkRealTemplate(Item target) {
/*  946 */     if (hasRealTemplate()) {
/*      */       
/*  948 */       if (getRealTemplateId() == target.getRealTemplateId())
/*  949 */         return true; 
/*  950 */       return (target.getRealTemplate() != null && target.getRealTemplate().getFoodGroup() == getRealTemplateId());
/*      */     } 
/*  952 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   String generateCookingStateName(byte state) {
/*  957 */     StringBuilder builder = new StringBuilder();
/*  958 */     if (state != -1) {
/*      */ 
/*      */       
/*  961 */       switch ((byte)(state & 0xF)) {
/*      */         
/*      */         case 0:
/*  964 */           if ((state & 0xF0) == 0)
/*  965 */             builder.append("raw"); 
/*      */           break;
/*      */         case 1:
/*  968 */           builder.append("fried");
/*      */           break;
/*      */         case 2:
/*  971 */           builder.append("grilled");
/*      */           break;
/*      */         case 3:
/*  974 */           builder.append("boiled");
/*      */           break;
/*      */         case 4:
/*  977 */           builder.append("roasted");
/*      */           break;
/*      */         case 5:
/*  980 */           builder.append("steamed");
/*      */           break;
/*      */         case 6:
/*  983 */           builder.append("baked");
/*      */           break;
/*      */         case 7:
/*  986 */           builder.append("cooked");
/*      */           break;
/*      */         case 8:
/*  989 */           builder.append("candied");
/*      */           break;
/*      */         case 9:
/*  992 */           builder.append("chocolate coated");
/*      */           break;
/*      */       } 
/*  995 */       if (state >= 16)
/*      */       {
/*  997 */         logger.info("Bad cooked state " + state + " for ingredient " + getName(true));
/*      */       }
/*      */     } 
/* 1000 */     return builder.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   String generatePhysicalStateName(byte state) {
/* 1005 */     StringBuilder builder = new StringBuilder();
/* 1006 */     if (state != -1) {
/*      */       
/* 1008 */       if ((state & 0x10) != 0) {
/*      */         
/* 1010 */         if (builder.length() > 0)
/* 1011 */           builder.append("+"); 
/* 1012 */         if (this.itemTemplate.isHerb() || this.itemTemplate.isVegetable() || this.itemTemplate.isFish() || this.itemTemplate.isMushroom()) {
/* 1013 */           builder.append("chopped");
/* 1014 */         } else if (this.itemTemplate.isMeat()) {
/* 1015 */           builder.append("diced");
/* 1016 */         } else if (this.itemTemplate.isSpice()) {
/* 1017 */           builder.append("ground");
/* 1018 */         } else if (this.itemTemplate.canBeFermented()) {
/* 1019 */           builder.append("unfermented");
/* 1020 */         } else if (this.itemTemplate.getTemplateId() == 1249) {
/* 1021 */           builder.append("whipped");
/*      */         } else {
/* 1023 */           builder.append("zombified");
/*      */         } 
/* 1025 */       }  if ((state & 0x20) != 0) {
/*      */         
/* 1027 */         if (builder.length() > 0)
/* 1028 */           builder.append("+"); 
/* 1029 */         if (this.itemTemplate.isMeat()) {
/* 1030 */           builder.append("minced");
/* 1031 */         } else if (this.itemTemplate.isVegetable()) {
/* 1032 */           builder.append("mashed");
/* 1033 */         } else if (this.itemTemplate.canBeFermented()) {
/* 1034 */           builder.append("fermenting");
/*      */         } else {
/* 1036 */           builder.append("clotted");
/*      */         } 
/* 1038 */       }  if ((state & 0x40) != 0) {
/*      */         
/* 1040 */         if (builder.length() > 0)
/* 1041 */           builder.append("+"); 
/* 1042 */         if (this.itemTemplate.canBeDistilled()) {
/* 1043 */           builder.append("undistilled");
/*      */         } else {
/*      */           
/* 1046 */           builder.append("wrapped");
/*      */         } 
/* 1048 */       }  if ((state & Byte.MIN_VALUE) != 0) {
/*      */         
/* 1050 */         if (builder.length() > 0)
/* 1051 */           builder.append("+"); 
/* 1052 */         if (this.itemTemplate.isDish) {
/* 1053 */           builder.append("salted");
/* 1054 */         } else if (this.itemTemplate.isHerb() || this.itemTemplate.isSpice()) {
/* 1055 */           builder.append("fresh");
/*      */         } 
/* 1057 */       }  if (state < 16 && state != 0)
/*      */       {
/* 1059 */         logger.info("Bad physical state " + state + " for ingredient " + getName(true));
/*      */       }
/*      */     } 
/* 1062 */     return builder.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   String generateMaterialName() {
/* 1067 */     return generateMaterialName(this.material);
/*      */   }
/*      */ 
/*      */   
/*      */   String generateMaterialName(byte mat) {
/* 1072 */     if (mat != -1)
/*      */     {
/*      */       
/* 1075 */       return Materials.convertMaterialByteIntoString(mat);
/*      */     }
/* 1077 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   String generateCorpseName() {
/* 1082 */     if (this.corpseData != -1) {
/*      */       
/*      */       try {
/*      */         
/* 1086 */         CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(this.corpseData);
/* 1087 */         return ct.getName();
/*      */       }
/* 1089 */       catch (NoSuchCreatureTemplateException e) {
/*      */         
/* 1091 */         return "unknown";
/*      */       } 
/*      */     }
/* 1094 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1105 */     StringBuilder buf = new StringBuilder();
/* 1106 */     buf.append("{name='" + getTemplateName() + "'(" + getTemplateId());
/* 1107 */     if (isFoodGroup())
/* 1108 */       buf.append("(isFoodGroup)"); 
/* 1109 */     buf.append(")");
/* 1110 */     if (this.cstate != -1)
/* 1111 */       buf.append(",cstate='" + this.cstateName + "'(" + this.cstate + ")"); 
/* 1112 */     if (this.pstate != -1)
/* 1113 */       buf.append(",pstate='" + this.pstateName + "'(" + this.pstate + ")"); 
/* 1114 */     if (this.material != -1)
/* 1115 */       buf.append(",material='" + this.materialName + "'(" + this.material + ")"); 
/* 1116 */     if (this.isResult) {
/*      */       
/* 1118 */       if (this.difficulty != -100) {
/* 1119 */         buf.append(",baseDifficulty='" + this.difficulty);
/*      */       
/*      */       }
/*      */     }
/* 1123 */     else if (this.difficulty != 0) {
/* 1124 */       buf.append(",addDifficulty='" + this.difficulty);
/*      */     } 
/* 1126 */     if (isLiquid()) {
/*      */       
/* 1128 */       buf.append(",ratio=" + this.ratio + "%");
/*      */     }
/* 1130 */     else if (this.amount > 1) {
/* 1131 */       buf.append(",need=" + this.amount);
/* 1132 */     }  if (this.corpseData != -1)
/* 1133 */       buf.append(",creature='" + this.corpseDataName + "'(" + this.corpseData + ")"); 
/* 1134 */     if (this.hasRealTemplate) {
/*      */       
/* 1136 */       buf.append(",realTemplate='");
/* 1137 */       if (this.realItemTemplate != null) {
/* 1138 */         buf.append(this.realItemTemplate.getName());
/*      */       } else {
/* 1140 */         buf.append("null");
/* 1141 */       }  buf.append("'(" + getRealTemplateId() + ")");
/*      */     } 
/* 1143 */     if (this.isResult) {
/*      */       
/* 1145 */       if (this.materialRef.length() > 0)
/* 1146 */         buf.append(",materialRef='" + this.materialRef + "'"); 
/* 1147 */       if (this.realTemplateRef.length() > 0)
/* 1148 */         buf.append(",realTemplateRef='" + this.realTemplateRef + "'"); 
/* 1149 */       if (this.resultName.length() > 0) {
/* 1150 */         buf.append(",resultName='" + this.resultName + "'");
/*      */       } else {
/* 1152 */         buf.append(",resultName='" + getName(true) + "'");
/*      */       } 
/* 1154 */     }  buf.append("}");
/* 1155 */     return buf.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\Ingredient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
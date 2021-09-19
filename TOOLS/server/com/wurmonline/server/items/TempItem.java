/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.combat.ArmourTemplate;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import java.io.IOException;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Level;
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
/*      */ public final class TempItem
/*      */   extends Item
/*      */ {
/*   45 */   private static final Logger logger = Logger.getLogger(TempItem.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TempItem() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void bless(int blesser) {
/*   58 */     if (this.bless == 0) {
/*   59 */       this.bless = (byte)blesser;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOwnerStuff(ItemTemplate templ) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void enchant(byte ench) {
/*   70 */     if (this.enchantment != ench) {
/*   71 */       this.enchantment = ench;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setColor(int _color) {
/*   77 */     this.color = _color;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColor2(int _color) {
/*   83 */     this.color2 = _color;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastOwnerId(long oid) {
/*   89 */     this.lastOwner = oid;
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
/*      */   public TempItem(String aName, ItemTemplate aTemplate, float qLevel, @Nullable String aCreator) throws IOException {
/*  105 */     super(-10L, aName, aTemplate, qLevel, (byte)0, (byte)0, -10L, aCreator);
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
/*      */ 
/*      */   
/*      */   public TempItem(long wurmId, short aPlace, String aName, ItemTemplate aTemplate, float qLevel, @Nullable String aCreator) throws IOException {
/*  123 */     super(wurmId, aName, aTemplate, qLevel, (byte)1, (byte)0, -10L, aCreator);
/*  124 */     setPlace(aPlace);
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
/*      */   public TempItem(String aName, short aPlace, ItemTemplate aTemplate, float aQualityLevel, String aCreator) throws IOException {
/*  140 */     super(aName, aPlace, aTemplate, aQualityLevel, (byte)0, (byte)0, -10L, aCreator);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public TempItem(String aName, ItemTemplate aTemplate, float aQualityLevel, float aPosX, float aPosY, float aPosZ, float aRotation, long bridgeId, String aCreator) throws IOException {
/*  159 */     super(aName, aTemplate, aQualityLevel, aPosX, aPosY, aPosZ, aRotation, (byte)0, (byte)0, bridgeId, aCreator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void create(float aQualityLevel, long aCreationDate) throws IOException {
/*  170 */     this.qualityLevel = aQualityLevel;
/*  171 */     this.lastMaintained = aCreationDate;
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
/*      */   void load() throws Exception {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadEffects() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setPlace(short aPlace) {
/*  202 */     this.place = aPlace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getPlace() {
/*  211 */     return this.place;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastMaintained(long time) {
/*  220 */     this.lastMaintained = time;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastMaintained() {
/*  229 */     return this.lastMaintained;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setQualityLevel(float newLevel) {
/*  238 */     this.qualityLevel = newLevel;
/*  239 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getOwnerId() {
/*  248 */     return this.ownerId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setOwnerId(long aOwnerId) {
/*  257 */     this.ownerId = aOwnerId;
/*  258 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getLocked() {
/*  267 */     return this.locked;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLocked(boolean aLocked) {
/*  276 */     this.locked = aLocked;
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
/*      */ 
/*      */   
/*      */   public int getTemplateId() {
/*  294 */     return this.template.getTemplateId();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTemplateId(int aId) {
/*      */     try {
/*  302 */       this.template = ItemTemplateFactory.getInstance().getTemplate(aId);
/*      */     }
/*  304 */     catch (NoSuchTemplateException nst) {
/*      */       
/*  306 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setZoneId(int aId, boolean isOnSurface) {
/*  316 */     this.surfaced = isOnSurface;
/*  317 */     this.zoneId = aId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getZoneId() {
/*  326 */     if (this.parentId != -10L)
/*      */     {
/*  328 */       if (Items.isItemLoaded(this.parentId)) {
/*      */         
/*      */         try {
/*      */           
/*  332 */           Item parent = Items.getItem(this.parentId);
/*  333 */           return parent.getZoneId();
/*      */         }
/*  335 */         catch (NoSuchItemException nsi) {
/*      */           
/*  337 */           logger.log(Level.WARNING, "This REALLY shouldn't happen! parentId: " + this.parentId, (Throwable)nsi);
/*      */         } 
/*      */       }
/*      */     }
/*  341 */     return this.zoneId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setDescription(String desc) {
/*  350 */     this.description = desc;
/*  351 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDescription() {
/*  360 */     return this.description;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String newname) {
/*  366 */     this.name = newname;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String newname, boolean sendUpdate) {
/*  372 */     setName(newname);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setInscription(String aInscription, String aInscriber) {
/*  378 */     return setInscription(aInscription, aInscriber, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setInscription(String aInscription, String aInscriber, int penColour) {
/*  384 */     this.inscription.setInscription(aInscription);
/*  385 */     this.inscription.setInscriber(aInscriber);
/*  386 */     this.inscription.setPenColour(penColour);
/*  387 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getRotation() {
/*  396 */     return this.rotation;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPos(float aPosX, float aPosY, float aPosZ, float aRotation, long bridgeId) {
/*  402 */     this.posX = aPosX;
/*  403 */     this.posY = aPosY;
/*  404 */     this.posZ = aPosZ;
/*  405 */     this.rotation = aRotation;
/*  406 */     this.onBridge = bridgeId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosXYZRotation(float _posX, float _posY, float _posZ, float _rot) {
/*  415 */     this.posX = _posX;
/*  416 */     this.posY = _posY;
/*  417 */     this.posZ = _posZ;
/*  418 */     this.rotation = _rot;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosXYZ(float _posX, float _posY, float _posZ) {
/*  427 */     this.posX = _posX;
/*  428 */     this.posY = _posY;
/*  429 */     this.posZ = _posZ;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosXY(float _posX, float _posY) {
/*  438 */     this.posX = _posX;
/*  439 */     this.posY = _posY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosX(float aPosX) {
/*  448 */     this.posX = aPosX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosY(float aPosY) {
/*  457 */     this.posY = aPosY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPosZ(float aPosZ) {
/*  466 */     this.posZ = aPosZ;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRotation(float aRotation) {
/*  475 */     this.rotation = aRotation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getQualityLevel() {
/*  484 */     return this.qualityLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getDamage() {
/*  493 */     return this.damage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Item> getItems() {
/*  502 */     if (this.items == null)
/*  503 */       this.items = new HashSet<>(); 
/*  504 */     return this.items;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Item[] getItemsAsArray() {
/*  510 */     if (this.items == null)
/*  511 */       return new Item[0]; 
/*  512 */     return this.items.<Item>toArray(new Item[this.items.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParentId(long pid, boolean isOnSurface) {
/*  521 */     this.surfaced = isOnSurface;
/*  522 */     if (this.parentId != pid) {
/*      */       
/*  524 */       if (pid == -10L) {
/*      */         
/*  526 */         if (this.watchers != null)
/*      */         {
/*  528 */           for (Creature watcher : this.watchers)
/*      */           {
/*  530 */             watcher.getCommunicator().sendRemoveFromInventory(this);
/*      */           }
/*      */         }
/*  533 */         this.watchers = null;
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/*  539 */           Item parent = Items.getItem(pid);
/*      */ 
/*      */           
/*  542 */           if (this.ownerId != parent.getOwnerId())
/*      */           {
/*  544 */             if (parent.getPosX() != getPosX() || parent.getPosY() != getPosY()) {
/*  545 */               setPosXYZ(getPosX(), getPosY(), getPosZ());
/*      */             }
/*      */           }
/*  548 */         } catch (NoSuchItemException nsi) {
/*      */           
/*  550 */           logger.log(Level.WARNING, nsi.getMessage(), (Throwable)nsi);
/*      */         } 
/*      */       } 
/*      */       
/*  554 */       this.parentId = pid;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getParentId() {
/*  564 */     return this.parentId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setSizeX(int sizex) {
/*  573 */     this.sizeX = sizex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setSizeY(int sizey) {
/*  582 */     this.sizeY = sizey;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setSizeZ(int sizez) {
/*  591 */     this.sizeZ = sizez;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSizeX() {
/*  600 */     if (this.sizeX > 0)
/*  601 */       return this.sizeX; 
/*  602 */     return this.template.getSizeX();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSizeY() {
/*  611 */     if (this.sizeY > 0)
/*  612 */       return this.sizeY; 
/*  613 */     return this.template.getSizeY();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSizeZ() {
/*  622 */     if (this.sizeZ > 0)
/*  623 */       return this.sizeZ; 
/*  624 */     return this.template.getSizeZ();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOriginalQualityLevel(float qlevel) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getOriginalQualityLevel() {
/*  641 */     return this.originalQualityLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setDamage(float dam) {
/*  650 */     float modifier = 1.0F;
/*  651 */     float difference = dam - this.damage;
/*  652 */     if (difference > 0.0F)
/*      */     {
/*  654 */       if (getSpellEffects() != null) {
/*      */         
/*  656 */         modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_DAMAGETAKEN);
/*  657 */         difference *= modifier;
/*      */       } 
/*      */     }
/*      */     
/*  661 */     return setDamage(this.damage + difference, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setDamage(float dam, boolean override) {
/*  670 */     this.damage = dam;
/*  671 */     if (dam >= 100.0F) {
/*      */       
/*  673 */       setQualityLevel(0.0F);
/*  674 */       checkDecay();
/*  675 */       return true;
/*      */     } 
/*  677 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setData1(int data1) {
/*  686 */     if (this.data == null)
/*  687 */       this.data = new ItemData(this.id, data1, -1, -1, -1); 
/*  688 */     this.data.data1 = data1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setData2(int data2) {
/*  697 */     if (this.data == null)
/*  698 */       this.data = new ItemData(this.id, -1, data2, -1, -1); 
/*  699 */     this.data.data2 = data2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setData(int data1, int data2) {
/*  708 */     if (this.data == null)
/*  709 */       this.data = new ItemData(this.id, data1, data2, -1, -1); 
/*  710 */     this.data.data1 = data1;
/*  711 */     this.data.data2 = data2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getData1() {
/*  720 */     if (this.data != null) {
/*  721 */       return this.data.data1;
/*      */     }
/*  723 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getData2() {
/*  732 */     if (this.data != null) {
/*  733 */       return this.data.data2;
/*      */     }
/*  735 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getWeightGrams() {
/*  741 */     if (getSpellEffects() == null) {
/*  742 */       return this.weight;
/*      */     }
/*  744 */     float modifier = getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_WEIGHT);
/*  745 */     return (int)(this.weight * modifier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setWeight(int w, boolean destroyOnWeightZero) {
/*  756 */     return setWeight(w, destroyOnWeightZero, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setWeight(int w, boolean destroyOnWeightZero, boolean sameOwner) {
/*  767 */     if (this.weight <= 0) {
/*      */       
/*  769 */       Items.destroyItem(this.id);
/*  770 */       return true;
/*      */     } 
/*      */     
/*  773 */     this.weight = w;
/*  774 */     if (this.parentId != -10L)
/*  775 */       updateParents(); 
/*  776 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getMaterial() {
/*  782 */     return this.material;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaterial(byte mat) {
/*  788 */     this.material = mat;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLockId() {
/*  794 */     return this.lockid;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLockId(long lid) {
/*  800 */     this.lockid = lid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void addItem(@Nullable Item item, boolean loading) {
/*  807 */     if (item != null) {
/*      */       
/*  809 */       getItems().add(item);
/*  810 */       if (this.parentId != -10L) {
/*  811 */         updateParents();
/*      */       }
/*      */     } else {
/*      */       
/*  815 */       logger.warning("Ignored attempt to add a null item to " + this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void removeItem(Item item) {
/*  822 */     if (this.items != null)
/*  823 */       this.items.remove(item); 
/*  824 */     if (item.wornAsArmour)
/*  825 */       item.setWornAsArmour(false, getOwnerId()); 
/*  826 */     if (this.parentId != -10L) {
/*  827 */       updateParents();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPrice(int newPrice) {
/*  833 */     this.price = newPrice;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTemperature(short temp) {
/*  839 */     this.temperature = temp;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBanked(boolean bank) {
/*  845 */     this.banked = bank;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAuxData(byte auxdata) {
/*  851 */     this.auxbyte = auxdata;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreationState(byte newState) {
/*  857 */     this.creationState = newState;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRealTemplate(int rTemplate) {
/*  863 */     this.realTemplate = rTemplate;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void setWornAsArmour(boolean wornArmour, long newOwner) {
/*  869 */     if (this.wornAsArmour != wornArmour) {
/*      */       
/*  871 */       this.wornAsArmour = wornArmour;
/*  872 */       if (this.wornAsArmour) {
/*      */ 
/*      */         
/*      */         try {
/*  876 */           Creature creature = Server.getInstance().getCreature(newOwner);
/*  877 */           ArmourTemplate armour = ArmourTemplate.getArmourTemplate(this.template.templateId);
/*  878 */           if (armour != null)
/*      */           {
/*  880 */             float moveModChange = armour.getMoveModifier();
/*  881 */             if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */               
/*  883 */               moveModChange *= ArmourTemplate.getMaterialMovementModifier(getMaterial());
/*      */             }
/*  885 */             else if (Servers.localServer.isChallengeOrEpicServer()) {
/*      */               
/*  887 */               if (getMaterial() == 57 || getMaterial() == 67) {
/*  888 */                 moveModChange *= 0.9F;
/*  889 */               } else if (getMaterial() == 56) {
/*  890 */                 moveModChange *= 0.95F;
/*      */               } 
/*      */             } 
/*  893 */             (creature.getMovementScheme()).armourMod.setModifier((creature.getMovementScheme()).armourMod.getModifier() - moveModChange);
/*      */           }
/*      */         
/*      */         }
/*  897 */         catch (NoSuchPlayerException nsp) {
/*      */           
/*  899 */           logger.log(Level.WARNING, "Worn armour on unknown player: ", (Throwable)nsp);
/*      */         }
/*  901 */         catch (NoSuchCreatureException cnf) {
/*      */           
/*  903 */           logger.log(Level.WARNING, "Worn armour on unknown creature: ", (Throwable)cnf);
/*      */         } 
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/*  910 */           Creature creature = Server.getInstance().getCreature(getOwnerId());
/*  911 */           ArmourTemplate armour = ArmourTemplate.getArmourTemplate(this.template.templateId);
/*  912 */           if (armour != null)
/*      */           {
/*  914 */             float moveModChange = armour.getMoveModifier();
/*  915 */             if (Features.Feature.METALLIC_ITEMS.isEnabled()) {
/*      */               
/*  917 */               moveModChange *= ArmourTemplate.getMaterialMovementModifier(getMaterial());
/*      */             }
/*  919 */             else if (Servers.localServer.isChallengeOrEpicServer()) {
/*      */               
/*  921 */               if (getMaterial() == 57 || getMaterial() == 67) {
/*  922 */                 moveModChange *= 0.9F;
/*  923 */               } else if (getMaterial() == 56) {
/*  924 */                 moveModChange *= 0.95F;
/*      */               } 
/*      */             } 
/*  927 */             (creature.getMovementScheme()).armourMod.setModifier((creature.getMovementScheme()).armourMod.getModifier() + moveModChange);
/*      */           }
/*      */         
/*      */         }
/*  931 */         catch (NoSuchPlayerException nsp) {
/*      */           
/*  933 */           logger.log(Level.WARNING, "Worn armour on unknown player: ", (Throwable)nsp);
/*      */         }
/*  935 */         catch (NoSuchCreatureException cnf) {
/*      */           
/*  937 */           logger.log(Level.WARNING, "Worn armour on unknown creature: ", (Throwable)cnf);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void savePosition() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFemale(boolean _female) {
/*  951 */     this.female = _female;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransferred(boolean trans) {
/*  957 */     this.transferred = trans;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void addNewKey(long keyId) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void removeNewKey(long keyId) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMailed(boolean _mailed) {
/*  973 */     this.mailed = _mailed;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreator(String _creator) {
/*  979 */     this.creator = _creator;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHidden(boolean _hidden) {
/*  985 */     this.hidden = _hidden;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDbStrings(DbStrings dbStrings) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DbStrings getDbStrings() {
/*  997 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void clear(long wurmId, String _creator, float posx, float posy, float posz, float _rot, String _desc, String _name, float _qualitylevel, byte _material, byte aRarity, long bridgeId) {
/* 1005 */     this.id = wurmId;
/* 1006 */     this.creator = _creator;
/* 1007 */     this.posX = posx;
/* 1008 */     this.posY = posy;
/* 1009 */     this.posZ = posz;
/* 1010 */     this.description = _desc;
/* 1011 */     this.name = _name;
/* 1012 */     this.qualityLevel = _qualitylevel;
/* 1013 */     this.originalQualityLevel = this.qualityLevel;
/* 1014 */     this.rotation = _rot;
/* 1015 */     this.zoneId = -10;
/* 1016 */     this.parentId = -10L;
/* 1017 */     this.sizeX = this.template.getSizeX();
/* 1018 */     this.sizeY = this.template.getSizeY();
/* 1019 */     this.sizeZ = this.template.getSizeZ();
/* 1020 */     this.weight = this.template.getWeightGrams();
/* 1021 */     this.lastMaintained = WurmCalendar.currentTime;
/* 1022 */     this.creationDate = WurmCalendar.currentTime;
/* 1023 */     this.creationState = 0;
/* 1024 */     this.banked = false;
/* 1025 */     this.damage = 0.0F;
/* 1026 */     this.enchantment = 0;
/* 1027 */     this.material = _material;
/* 1028 */     this.rarity = aRarity;
/* 1029 */     this.onBridge = bridgeId;
/* 1030 */     this.creationState = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMailTimes(byte times) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void returnFromFreezer() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveToFreezer() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteInDatabase() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setRarity(byte newRarity) {
/* 1068 */     if (newRarity != this.rarity) {
/*      */       
/* 1070 */       this.rarity = newRarity;
/* 1071 */       return true;
/*      */     } 
/* 1073 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void savePermissions() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean saveInscription() {
/* 1090 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtra1(int extra1) {
/* 1096 */     if (this.data == null)
/* 1097 */       this.data = new ItemData(this.id, -1, -1, -1, -1); 
/* 1098 */     this.data.extra1 = extra1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtra2(int extra2) {
/* 1104 */     if (this.data == null)
/* 1105 */       this.data = new ItemData(this.id, -1, -1, -1, -1); 
/* 1106 */     this.data.extra2 = extra2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtra(int extra1, int extra2) {
/* 1112 */     if (this.data == null)
/* 1113 */       this.data = new ItemData(this.id, -1, -1, -1, -1); 
/* 1114 */     this.data.extra1 = extra1;
/* 1115 */     this.data.extra2 = extra2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getExtra1() {
/* 1121 */     if (this.data != null) {
/* 1122 */       return this.data.extra1;
/*      */     }
/* 1124 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getExtra2() {
/* 1130 */     if (this.data != null) {
/* 1131 */       return this.data.extra2;
/*      */     }
/* 1133 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllData(int data1, int data2, int extra1, int extra2) {
/* 1139 */     if (this.data == null)
/* 1140 */       this.data = new ItemData(this.id, -1, -1, -1, -1); 
/* 1141 */     this.data.data1 = data1;
/* 1142 */     this.data.data2 = data2;
/* 1143 */     this.data.extra1 = extra1;
/* 1144 */     this.data.extra2 = extra2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPlacedOnParent(boolean onParent) {
/* 1150 */     this.placedOnParent = onParent;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isItem() {
/* 1156 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\TempItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
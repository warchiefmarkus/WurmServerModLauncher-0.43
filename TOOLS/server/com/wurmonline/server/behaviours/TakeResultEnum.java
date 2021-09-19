/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.utils.StringUtil;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public enum TakeResultEnum
/*     */ {
/*  31 */   SUCCESS(false, false, false),
/*  32 */   TARGET_HAS_NO_OWNER(false, false, false),
/*  33 */   PERFORMER_IS_OWNER(false, false, false),
/*  34 */   TARGET_IS_UNREACHABLE("You can't reach that now.", true, true, false),
/*  35 */   TARGET_IS_LIQUID("You need to pour that into container.", true, false, false),
/*  36 */   TARGET_FILLED_BULK_CONTAINER("It is too heavy now.", true, false, false),
/*  37 */   MAY_NOT_LOOT_THAT_ITEM("You may not loot that item.", true, false, false),
/*  38 */   VEHICLE_IS_WATCHED("The %s is being watched too closely. You cannot take items from it.", true, true, true),
/*  39 */   NEEDS_TO_STEAL("You have to steal the %s.", true, true, true),
/*  40 */   IN_LEGAL_MODE(false, true, false),
/*  41 */   MAY_NOT_STEAL("You need more body control to steal things.", true, true, false),
/*  42 */   NEED_TO_BE_EMPTY_BEFORE_THEFT("You must empty the %s before you steal it.", true, true, true),
/*  43 */   PREVENTED_THEFT(false, true, false),
/*  44 */   TOO_FAR_AWAY("You are now too far away to get the %s.", true, true, true, true),
/*  45 */   TARGET_BLOCKED("You can't reach the %s through the %s.", true, true, true, true),
/*  46 */   INVENTORY_FULL("Your inventory contains too many items already.", true, true, false),
/*  47 */   INVENTORY_FULL_PLACED("Your inventory contains too many items to carry all of the placed items on the %s.", true, true, false),
/*  48 */   CARRYING_TOO_MUCH("You are carrying too much to pick up the %s.", true, false, true, true),
/*  49 */   HITCHED("There are hitched creatures.", true, true, true, true), TARGET_IN_WATER("You can't put that in the water.", true, false, false),
/*     */   
/*  51 */   TARGET_WEIRD("For some weird reason the %s won't budge.", true, false, false),
/*     */   
/*  53 */   TARGET_BULK_ITEM("You have to use drag and drop instead.", true, true, false),
/*  54 */   TARGET_IN_USE("You cannot take the %s as it is in use.", true, true, true),
/*  55 */   UNKNOWN_FAILURE(false, true, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private final Map<Long, String[]> texts = (Map)new ConcurrentHashMap<>();
/*     */   private final String message;
/*     */   private final boolean print;
/*     */   
/*     */   TakeResultEnum(boolean print, boolean abortTakeFromPile, boolean formatted) {
/*  68 */     this.print = print;
/*  69 */     this.abortTakeFromPile = abortTakeFromPile;
/*  70 */     this.formatted = formatted;
/*  71 */     this.safePrint = false;
/*  72 */     this.message = "";
/*     */   }
/*     */   private final boolean abortTakeFromPile;
/*     */   private final boolean formatted;
/*     */   private final boolean safePrint;
/*     */   
/*     */   TakeResultEnum(boolean print, boolean abortTakeFromPile, boolean formatted, boolean safePrint) {
/*  79 */     this.print = print;
/*  80 */     this.abortTakeFromPile = abortTakeFromPile;
/*  81 */     this.formatted = formatted;
/*  82 */     this.safePrint = safePrint;
/*  83 */     this.message = "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TakeResultEnum(String message, boolean print, boolean abortTakeFromPile, boolean formatted) {
/*  90 */     this.message = message;
/*  91 */     this.print = print;
/*  92 */     this.abortTakeFromPile = abortTakeFromPile;
/*  93 */     this.formatted = formatted;
/*  94 */     this.safePrint = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TakeResultEnum(String message, boolean print, boolean abortTakeFromPile, boolean formatted, boolean safePrint) {
/* 101 */     this.message = message;
/* 102 */     this.print = print;
/* 103 */     this.abortTakeFromPile = abortTakeFromPile;
/* 104 */     this.formatted = formatted;
/* 105 */     this.safePrint = safePrint;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getResultStringFormattedWith(String text) {
/* 110 */     return StringUtil.format(this.message, new Object[] { text });
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getMessage(long performerId) {
/* 115 */     if (haveMessageParameters() && this.texts.containsKey(Long.valueOf(performerId))) {
/*     */       
/* 117 */       String[] vars = this.texts.get(Long.valueOf(performerId));
/* 118 */       this.texts.remove(Long.valueOf(performerId));
/* 119 */       return StringUtil.format(this.message, (Object[])vars);
/*     */     } 
/*     */     
/* 122 */     return this.message;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean haveMessageParameters() {
/* 127 */     return this.formatted;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIndexText(long performerId, String... strings) {
/* 132 */     this.texts.put(Long.valueOf(performerId), strings);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean shouldPrint() {
/* 137 */     return this.print;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean safePrint() {
/* 142 */     return this.safePrint;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean abortsTakeFromPile() {
/* 147 */     return this.abortTakeFromPile;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendToPerformer(Creature performer) {
/* 152 */     if (shouldPrint())
/*     */     {
/* 154 */       if (this.safePrint) {
/* 155 */         performer.getCommunicator().sendSafeServerMessage(getMessage(performer.getWurmId()));
/*     */       } else {
/* 157 */         performer.getCommunicator().sendNormalServerMessage(getMessage(performer.getWurmId()));
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TakeResultEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
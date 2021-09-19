/*     */ package com.wurmonline.shared.constants;
/*     */ 
/*     */ import java.util.Locale;
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
/*     */ public interface BridgeConstants
/*     */ {
/*     */   public enum BridgeMaterial
/*     */   {
/*  34 */     ROPE((byte)1, "rope", "Rope", 621),
/*  35 */     BRICK((byte)2, "stone", "Stone brick", 60, 9),
/*  36 */     MARBLE((byte)3, "marble", "Marble brick", 60, 9),
/*  37 */     WOOD((byte)4, "wood", "Wood", 60, 6),
/*  38 */     SLATE((byte)5, "slate", "Slate brick", 60, 9),
/*  39 */     ROUNDED_STONE((byte)6, "roundedstone", "Rounded stone", 60, 9),
/*  40 */     POTTERY((byte)7, "pottery", "Pottery brick", 60, 9),
/*  41 */     SANDSTONE((byte)8, "sandstone", "Sandstone brick", 60, 9),
/*  42 */     RENDERED((byte)9, "rendered", "Rendered brick", 60, 9);
/*     */     
/*     */     private final byte material;
/*     */     
/*     */     private final String texture;
/*     */     
/*     */     private final int supportExtensionOffset;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final int icon;
/*  53 */     private static final BridgeMaterial[] types = values();
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     BridgeMaterial(byte newMaterial, String newTexture, String newName, int newIcon, int newSupportExtensionOffset) {
/*  59 */       this.material = newMaterial;
/*  60 */       this.texture = newTexture;
/*  61 */       this.supportExtensionOffset = newSupportExtensionOffset;
/*  62 */       this.name = newName;
/*  63 */       this.icon = newIcon;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte getCode() {
/*  73 */       return this.material;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getTextureName() {
/*  78 */       return this.texture;
/*     */     }
/*     */ 
/*     */     
/*     */     public final float getExtensionOffset() {
/*  83 */       return this.supportExtensionOffset;
/*     */     }
/*     */ 
/*     */     
/*     */     private final int getIcon() {
/*  88 */       return this.icon;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static BridgeMaterial fromByte(byte typeByte) {
/*  94 */       for (int i = 0; i < types.length; i++) {
/*     */         
/*  96 */         if (typeByte == types[i].getCode())
/*  97 */           return types[i]; 
/*     */       } 
/*  99 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getName() {
/* 104 */       return this.name;
/*     */     }
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
/*     */     public static final String getTextureName(BridgeConstants.BridgeType type, BridgeMaterial material) {
/* 118 */       return "img.texture.floor." + type.getTextureName() + "." + material.getTextureName().replace(" ", "");
/*     */     }
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
/*     */   
/*     */   public enum BridgeType
/*     */   {
/* 136 */     ABUTMENT_NARROW(0, "abutment.narrow", "abutment"),
/* 137 */     BRACING_NARROW(1, "bracing.narrow", "bracing"),
/* 138 */     CROWN_NARROW(2, "crown.narrow", "crown"),
/* 139 */     DOUBLE_NARROW(3, "double.narrow", "double bracing"),
/* 140 */     END_NARROW(4, "end.narrow", "double abutment"),
/* 141 */     FLOATING_NARROW(5, "floating.narrow", "floating"),
/* 142 */     SUPPORT_NARROW(6, "support.narrow", "support", "extension.narrow"),
/*     */     
/* 144 */     ABUTMENT_CENTER(7, "abutment.center", "abutment"),
/* 145 */     BRACING_CENTER(8, "bracing.center", "bracing"),
/* 146 */     CROWN_CENTER(9, "crown.center", "crown"),
/* 147 */     DOUBLE_CENTER(10, "double.center", "double bracing"),
/* 148 */     END_CENTER(11, "end.center", "double abutment"),
/* 149 */     FLOATING_CENTER(12, "floating.center", "floating"),
/* 150 */     SUPPORT_CENTER(13, "support.center", "support", "extension.center"),
/*     */     
/* 152 */     ABUTMENT_LEFT(14, "abutment.left", "abutment"),
/* 153 */     ABUTMENT_RIGHT(15, "abutment.right", "abutment"),
/* 154 */     BRACING_LEFT(16, "bracing.left", "bracing"),
/* 155 */     BRACING_RIGHT(17, "bracing.right", "bracing"),
/*     */ 
/*     */     
/* 158 */     CROWN_SIDE(18, "crown.side", "crown"),
/* 159 */     DOUBLE_SIDE(19, "double.side", "double bracing"),
/* 160 */     END_SIDE(20, "end.side", "double abutment"),
/* 161 */     FLOATING_SIDE(21, "floating.side", "floating"),
/* 162 */     SUPPORT_SIDE(22, "support.side", "support", "extension.side");
/*     */     
/*     */     private final byte type;
/*     */     
/*     */     private final String texture;
/*     */     
/*     */     private final String extensionTexture;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final boolean isNarrow;
/*     */     
/*     */     private final boolean isSide;
/*     */     
/*     */     private final boolean isLeft;
/*     */     
/*     */     private final boolean isRight;
/*     */     
/*     */     private final boolean isCenter;
/*     */     
/*     */     private final boolean isAbutment;
/*     */     
/*     */     private final boolean isBracing;
/*     */     
/*     */     private final boolean isCrown;
/*     */     
/*     */     private final boolean isFloating;
/*     */     
/*     */     private final boolean isEnd;
/*     */     
/*     */     private final boolean isDouble;
/*     */ 
/*     */     
/*     */     BridgeType(int newType, String newTexture, String newName, String newExtensionTexture) {
/*     */       this.type = (byte)newType;
/*     */       this.texture = newTexture;
/*     */       this.extensionTexture = newExtensionTexture;
/*     */       this.name = newName;
/*     */       this.isNarrow = this.texture.contains(".narrow");
/*     */       this.isSide = this.texture.contains(".side");
/*     */       this.isLeft = this.texture.contains(".left");
/*     */       this.isRight = this.texture.contains(".right");
/*     */       this.isCenter = this.texture.contains(".center");
/*     */       this.isAbutment = this.texture.startsWith("abutment.");
/*     */       this.isBracing = this.texture.startsWith("bracing.");
/*     */       this.isCrown = this.texture.startsWith("crown.");
/*     */       this.isFloating = this.texture.startsWith("floating.");
/*     */       this.isEnd = this.texture.startsWith("end.");
/*     */       this.isDouble = this.texture.startsWith("double.");
/*     */     }
/*     */ 
/*     */     
/* 214 */     private static final BridgeType[] types = values();
/*     */     public byte getCode() {
/*     */       return this.type;
/*     */     }
/*     */     
/*     */     public static BridgeType fromByte(byte typeByte) {
/* 220 */       for (int i = 0; i < types.length; i++) {
/*     */         
/* 222 */         if (typeByte == types[i].getCode())
/* 223 */           return types[i]; 
/*     */       } 
/* 225 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getTextureName() {
/* 230 */       return this.texture;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getExtensionTextureName() {
/* 235 */       return this.extensionTexture;
/*     */     } static {
/*     */     
/*     */     }
/*     */     public final String getName() {
/* 240 */       return this.name.toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isSupportType() {
/* 245 */       return (this.extensionTexture.length() > 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final boolean isNarrow() {
/* 251 */       return this.isNarrow;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final boolean isSide() {
/* 257 */       return this.isSide;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final boolean isLeft() {
/* 263 */       return this.isLeft;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final boolean isRight() {
/* 269 */       return this.isRight;
/*     */     }
/*     */ 
/*     */     
/*     */     public final int wallCount() {
/* 274 */       if (isNarrow())
/* 275 */         return 2; 
/* 276 */       if (isLeft() || isRight() || isSide())
/* 277 */         return 1; 
/* 278 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isCenter() {
/* 283 */       return this.isCenter;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isAbutment() {
/* 288 */       return this.isAbutment;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isBracing() {
/* 293 */       return this.isBracing;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isCrown() {
/* 298 */       return this.isCrown;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isFloating() {
/* 303 */       return this.isFloating;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isDoubleAbutment() {
/* 308 */       return this.isEnd;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isDoubleBracing() {
/* 313 */       return this.isDouble;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String getModelName(BridgeType type, BridgeConstants.BridgeMaterial material, BridgeConstants.BridgeState state) {
/* 325 */       String plan = "";
/* 326 */       if (state == BridgeConstants.BridgeState.PLANNED)
/* 327 */         plan = ".plan"; 
/* 328 */       if (state.isBeingBuilt())
/* 329 */         plan = ".build"; 
/* 330 */       String modelName = "model.structure.bridge" + plan + "." + type.getTextureName() + "." + material.getTextureName().replace(" ", "");
/* 331 */       return modelName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int getIconId(BridgeType type, BridgeConstants.BridgeMaterial material, BridgeConstants.BridgeState state) {
/* 343 */       switch (material) {
/*     */         
/*     */         case WOOD:
/* 346 */           switch (type) {
/*     */             
/*     */             case WOOD:
/*     */             case BRICK:
/*     */             case MARBLE:
/* 351 */               return 440;
/*     */             case SLATE:
/*     */             case ROUNDED_STONE:
/* 354 */               return 441;
/*     */             case POTTERY:
/*     */             case SANDSTONE:
/* 357 */               return 442;
/*     */           } 
/* 359 */           return 60;
/*     */         
/*     */         case BRICK:
/* 362 */           switch (type) {
/*     */             
/*     */             case WOOD:
/*     */             case BRICK:
/*     */             case MARBLE:
/*     */             case RENDERED:
/* 368 */               return 443;
/*     */             case ROPE:
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 373 */               return 444;
/*     */             case SLATE:
/*     */             case ROUNDED_STONE:
/*     */             case null:
/* 377 */               return 445;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 381 */               return 446;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 385 */               return 447;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 389 */               return 448;
/*     */             case POTTERY:
/*     */             case SANDSTONE:
/*     */             case null:
/* 393 */               return 449;
/*     */           } 
/* 395 */           return 60;
/*     */         
/*     */         case MARBLE:
/* 398 */           switch (type) {
/*     */             
/*     */             case WOOD:
/*     */             case BRICK:
/*     */             case MARBLE:
/*     */             case RENDERED:
/* 404 */               return 450;
/*     */             case ROPE:
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 409 */               return 451;
/*     */             case SLATE:
/*     */             case ROUNDED_STONE:
/*     */             case null:
/* 413 */               return 452;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 417 */               return 453;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 421 */               return 454;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 425 */               return 455;
/*     */             case POTTERY:
/*     */             case SANDSTONE:
/*     */             case null:
/* 429 */               return 456;
/*     */           } 
/* 431 */           return 60;
/*     */         
/*     */         case SLATE:
/* 434 */           switch (type) {
/*     */             
/*     */             case WOOD:
/*     */             case BRICK:
/*     */             case MARBLE:
/*     */             case RENDERED:
/* 440 */               return 430;
/*     */             case ROPE:
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 445 */               return 431;
/*     */             case SLATE:
/*     */             case ROUNDED_STONE:
/*     */             case null:
/* 449 */               return 432;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 453 */               return 433;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 457 */               return 434;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 461 */               return 435;
/*     */             case POTTERY:
/*     */             case SANDSTONE:
/*     */             case null:
/* 465 */               return 436;
/*     */           } 
/* 467 */           return 60;
/*     */         
/*     */         case ROUNDED_STONE:
/* 470 */           switch (type) {
/*     */             
/*     */             case WOOD:
/*     */             case BRICK:
/*     */             case MARBLE:
/*     */             case RENDERED:
/* 476 */               return 410;
/*     */             case ROPE:
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 481 */               return 411;
/*     */             case SLATE:
/*     */             case ROUNDED_STONE:
/*     */             case null:
/* 485 */               return 412;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 489 */               return 413;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 493 */               return 414;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 497 */               return 415;
/*     */             case POTTERY:
/*     */             case SANDSTONE:
/*     */             case null:
/* 501 */               return 416;
/*     */           } 
/* 503 */           return 60;
/*     */         
/*     */         case POTTERY:
/* 506 */           switch (type) {
/*     */             
/*     */             case WOOD:
/*     */             case BRICK:
/*     */             case MARBLE:
/*     */             case RENDERED:
/* 512 */               return 390;
/*     */             case ROPE:
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 517 */               return 391;
/*     */             case SLATE:
/*     */             case ROUNDED_STONE:
/*     */             case null:
/* 521 */               return 392;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 525 */               return 393;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 529 */               return 394;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 533 */               return 395;
/*     */             case POTTERY:
/*     */             case SANDSTONE:
/*     */             case null:
/* 537 */               return 396;
/*     */           } 
/* 539 */           return 60;
/*     */         
/*     */         case SANDSTONE:
/* 542 */           switch (type) {
/*     */             
/*     */             case WOOD:
/*     */             case BRICK:
/*     */             case MARBLE:
/*     */             case RENDERED:
/* 548 */               return 370;
/*     */             case ROPE:
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 553 */               return 371;
/*     */             case SLATE:
/*     */             case ROUNDED_STONE:
/*     */             case null:
/* 557 */               return 372;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 561 */               return 373;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 565 */               return 374;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 569 */               return 375;
/*     */             case POTTERY:
/*     */             case SANDSTONE:
/*     */             case null:
/* 573 */               return 376;
/*     */           } 
/* 575 */           return 60;
/*     */         
/*     */         case RENDERED:
/* 578 */           switch (type) {
/*     */             
/*     */             case WOOD:
/*     */             case BRICK:
/*     */             case MARBLE:
/*     */             case RENDERED:
/* 584 */               return 350;
/*     */             case ROPE:
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 589 */               return 351;
/*     */             case SLATE:
/*     */             case ROUNDED_STONE:
/*     */             case null:
/* 593 */               return 352;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 597 */               return 353;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 601 */               return 354;
/*     */             case null:
/*     */             case null:
/*     */             case null:
/* 605 */               return 355;
/*     */             case POTTERY:
/*     */             case SANDSTONE:
/*     */             case null:
/* 609 */               return 356;
/*     */           } 
/* 611 */           return 60;
/*     */         
/*     */         case ROPE:
/* 614 */           switch (type) {
/*     */             
/*     */             case WOOD:
/* 617 */               return 457;
/*     */             case SLATE:
/* 619 */               return 458;
/*     */             case null:
/* 621 */               return 459;
/*     */           } 
/* 623 */           return 60;
/*     */       } 
/*     */       
/* 626 */       return 60;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String getExtensionModelName(BridgeType type, BridgeConstants.BridgeMaterial material, BridgeConstants.BridgeState state) {
/* 633 */       String modelName = "";
/* 634 */       if (type.isSupportType()) {
/*     */         
/* 636 */         String plan = "";
/* 637 */         if (state == BridgeConstants.BridgeState.PLANNED)
/* 638 */           plan = ".plan"; 
/* 639 */         if (state.isBeingBuilt()) {
/* 640 */           plan = ".build";
/*     */         }
/* 642 */         modelName = "model.structure.bridge" + plan + "." + type.getExtensionTextureName() + "." + material.getTextureName().replace(" ", "");
/*     */       } 
/* 644 */       return modelName;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum BridgeState
/*     */   {
/* 657 */     PLANNED((byte)-1, false, ""),
/* 658 */     STAGE1((byte)0, true, "first "),
/* 659 */     STAGE2((byte)1, true, "second "),
/* 660 */     STAGE3((byte)2, true, "third "),
/* 661 */     STAGE4((byte)3, true, "fourth "),
/* 662 */     STAGE5((byte)4, true, "fifth "),
/* 663 */     STAGE6((byte)5, true, "sixth "),
/* 664 */     STAGE7((byte)6, true, "seventh "),
/* 665 */     COMPLETED(127, false, "");
/*     */ 
/*     */     
/*     */     private byte state;
/*     */     
/*     */     private boolean beingBuilt;
/*     */     
/*     */     private String desc;
/*     */     
/* 674 */     private static final BridgeState[] types = values();
/*     */     static {
/*     */     
/*     */     }
/*     */     BridgeState(byte newState, boolean newBeingBuilt, String description) {
/* 679 */       this.state = newState;
/* 680 */       this.beingBuilt = newBeingBuilt;
/* 681 */       this.desc = description;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte getCode() {
/* 686 */       return this.state;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBeingBuilt() {
/* 691 */       return this.beingBuilt;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDescription() {
/* 696 */       return this.desc;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static BridgeState fromByte(byte bridgeStateByte) {
/* 702 */       for (int i = 0; i < types.length; i++) {
/*     */         
/* 704 */         if (bridgeStateByte == types[i].getCode()) {
/* 705 */           return types[i];
/*     */         }
/*     */       } 
/* 708 */       return PLANNED;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\BridgeConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
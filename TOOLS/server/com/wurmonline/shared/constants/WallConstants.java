/*      */ package com.wurmonline.shared.constants;
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
/*      */ public final class WallConstants
/*      */ {
/*      */   public static final byte DECAY_VALUE = 60;
/*      */   
/*      */   public static final short translateWallType(StructureTypeEnum type, String material) {
/*  305 */     boolean isStone = "stone".equals(material);
/*  306 */     boolean isPlainStone = "plain stone".equals(material);
/*  307 */     boolean isTimber = "timber framed".equals(material);
/*  308 */     boolean isSlate = "slate".equals(material);
/*  309 */     boolean isRoundedStone = "rounded stone".equals(material);
/*  310 */     boolean isPottery = "pottery".equals(material);
/*  311 */     boolean isSandstone = "sandstone".equals(material);
/*  312 */     boolean isRendered = "rendered".equals(material);
/*  313 */     boolean isMarble = "marble".equals(material);
/*      */     
/*  315 */     switch (type) {
/*      */       
/*      */       case FENCE_WOODEN:
/*  318 */         return StructureConstantsEnum.WALL_RUBBLE.value;
/*      */       
/*      */       case FENCE_WOODEN_CRUDE:
/*  321 */         if (isStone)
/*  322 */           return StructureConstantsEnum.WALL_DOOR_STONE_DECORATED.value; 
/*  323 */         if (isTimber)
/*  324 */           return StructureConstantsEnum.WALL_DOOR_TIMBER_FRAMED.value; 
/*  325 */         if (isPlainStone)
/*  326 */           return StructureConstantsEnum.WALL_DOOR_STONE.value; 
/*  327 */         if (isSlate)
/*  328 */           return StructureConstantsEnum.WALL_DOOR_SLATE.value; 
/*  329 */         if (isRoundedStone)
/*  330 */           return StructureConstantsEnum.WALL_DOOR_ROUNDED_STONE.value; 
/*  331 */         if (isPottery)
/*  332 */           return StructureConstantsEnum.WALL_DOOR_POTTERY.value; 
/*  333 */         if (isSandstone)
/*  334 */           return StructureConstantsEnum.WALL_DOOR_SANDSTONE.value; 
/*  335 */         if (isRendered)
/*  336 */           return StructureConstantsEnum.WALL_DOOR_RENDERED.value; 
/*  337 */         if (isMarble)
/*  338 */           return StructureConstantsEnum.WALL_DOOR_MARBLE.value; 
/*  339 */         return StructureConstantsEnum.WALL_DOOR_WOODEN.value;
/*      */ 
/*      */       
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/*  343 */         if (isStone)
/*  344 */           return StructureConstantsEnum.WALL_WINDOW_STONE_DECORATED.value; 
/*  345 */         if (isTimber)
/*  346 */           return StructureConstantsEnum.WALL_WINDOW_TIMBER_FRAMED.value; 
/*  347 */         if (isPlainStone)
/*  348 */           return StructureConstantsEnum.WALL_WINDOW_STONE.value; 
/*  349 */         if (isSlate)
/*  350 */           return StructureConstantsEnum.WALL_WINDOW_SLATE.value; 
/*  351 */         if (isRoundedStone)
/*  352 */           return StructureConstantsEnum.WALL_WINDOW_ROUNDED_STONE.value; 
/*  353 */         if (isPottery)
/*  354 */           return StructureConstantsEnum.WALL_WINDOW_POTTERY.value; 
/*  355 */         if (isSandstone)
/*  356 */           return StructureConstantsEnum.WALL_WINDOW_SANDSTONE.value; 
/*  357 */         if (isRendered)
/*  358 */           return StructureConstantsEnum.WALL_WINDOW_RENDERED.value; 
/*  359 */         if (isMarble)
/*  360 */           return StructureConstantsEnum.WALL_WINDOW_MARBLE.value; 
/*  361 */         return StructureConstantsEnum.WALL_WINDOW_WOODEN.value;
/*      */       
/*      */       case FENCE_PALISADE:
/*  364 */         return StructureConstantsEnum.WALL_WINDOW_WIDE_WOODEN.value;
/*      */       
/*      */       case FENCE_STONEWALL:
/*  367 */         if (isStone)
/*  368 */           return StructureConstantsEnum.WALL_SOLID_STONE_DECORATED.value; 
/*  369 */         if (isPlainStone)
/*  370 */           return StructureConstantsEnum.WALL_SOLID_STONE.value; 
/*  371 */         if (isTimber)
/*  372 */           return StructureConstantsEnum.WALL_SOLID_TIMBER_FRAMED.value; 
/*  373 */         if (isSlate)
/*  374 */           return StructureConstantsEnum.WALL_SOLID_SLATE.value; 
/*  375 */         if (isRoundedStone)
/*  376 */           return StructureConstantsEnum.WALL_SOLID_ROUNDED_STONE.value; 
/*  377 */         if (isPottery)
/*  378 */           return StructureConstantsEnum.WALL_SOLID_POTTERY.value; 
/*  379 */         if (isSandstone)
/*  380 */           return StructureConstantsEnum.WALL_SOLID_SANDSTONE.value; 
/*  381 */         if (isRendered)
/*  382 */           return StructureConstantsEnum.WALL_SOLID_RENDERED.value; 
/*  383 */         if (isMarble)
/*  384 */           return StructureConstantsEnum.WALL_SOLID_MARBLE.value; 
/*  385 */         return StructureConstantsEnum.WALL_SOLID_WOODEN.value;
/*      */ 
/*      */       
/*      */       case FENCE_WOODEN_GATE:
/*  389 */         if (isStone)
/*  390 */           return StructureConstantsEnum.WALL_DOUBLE_DOOR_STONE_DECORATED.value; 
/*  391 */         if (isTimber)
/*  392 */           return StructureConstantsEnum.WALL_DOUBLE_DOOR_TIMBER_FRAMED.value; 
/*  393 */         if (isPlainStone)
/*  394 */           return StructureConstantsEnum.WALL_DOUBLE_DOOR_STONE.value; 
/*  395 */         if (isSlate)
/*  396 */           return StructureConstantsEnum.WALL_DOUBLE_DOOR_SLATE.value; 
/*  397 */         if (isRoundedStone)
/*  398 */           return StructureConstantsEnum.WALL_DOUBLE_DOOR_ROUNDED_STONE.value; 
/*  399 */         if (isPottery)
/*  400 */           return StructureConstantsEnum.WALL_DOUBLE_DOOR_POTTERY.value; 
/*  401 */         if (isSandstone)
/*  402 */           return StructureConstantsEnum.WALL_DOUBLE_DOOR_SANDSTONE.value; 
/*  403 */         if (isRendered)
/*  404 */           return StructureConstantsEnum.WALL_DOUBLE_DOOR_RENDERED.value; 
/*  405 */         if (isMarble)
/*  406 */           return StructureConstantsEnum.WALL_DOUBLE_DOOR_MARBLE.value; 
/*  407 */         return StructureConstantsEnum.WALL_DOUBLE_DOOR_WOODEN.value;
/*      */ 
/*      */       
/*      */       case FENCE_PALISADE_GATE:
/*  411 */         if (isStone)
/*  412 */           return StructureConstantsEnum.WALL_DOOR_ARCHED_STONE_DECORATED.value; 
/*  413 */         if (isTimber)
/*  414 */           return StructureConstantsEnum.WALL_DOOR_ARCHED_TIMBER_FRAMED.value; 
/*  415 */         if (isPlainStone)
/*  416 */           return StructureConstantsEnum.WALL_DOOR_ARCHED_STONE.value; 
/*  417 */         if (isSlate)
/*  418 */           return StructureConstantsEnum.WALL_ARCHED_SLATE.value; 
/*  419 */         if (isRoundedStone)
/*  420 */           return StructureConstantsEnum.WALL_ARCHED_ROUNDED_STONE.value; 
/*  421 */         if (isPottery)
/*  422 */           return StructureConstantsEnum.WALL_ARCHED_POTTERY.value; 
/*  423 */         if (isSandstone)
/*  424 */           return StructureConstantsEnum.WALL_ARCHED_SANDSTONE.value; 
/*  425 */         if (isRendered)
/*  426 */           return StructureConstantsEnum.WALL_ARCHED_RENDERED.value; 
/*  427 */         if (isMarble)
/*  428 */           return StructureConstantsEnum.WALL_ARCHED_MARBLE.value; 
/*  429 */         return StructureConstantsEnum.WALL_DOOR_ARCHED_WOODEN.value;
/*      */ 
/*      */       
/*      */       case FENCE_STONEWALL_HIGH:
/*  433 */         if (isPlainStone)
/*  434 */           return StructureConstantsEnum.WALL_PLAIN_NARROW_WINDOW.value; 
/*  435 */         if (isSlate)
/*  436 */           return StructureConstantsEnum.WALL_NARROW_WINDOW_SLATE.value; 
/*  437 */         if (isRoundedStone)
/*  438 */           return StructureConstantsEnum.WALL_NARROW_WINDOW_ROUNDED_STONE.value; 
/*  439 */         if (isPottery)
/*  440 */           return StructureConstantsEnum.WALL_NARROW_WINDOW_POTTERY.value; 
/*  441 */         if (isSandstone)
/*  442 */           return StructureConstantsEnum.WALL_NARROW_WINDOW_SANDSTONE.value; 
/*  443 */         if (isRendered)
/*  444 */           return StructureConstantsEnum.WALL_NARROW_WINDOW_RENDERED.value; 
/*  445 */         if (isMarble)
/*  446 */           return StructureConstantsEnum.WALL_NARROW_WINDOW_MARBLE.value; 
/*  447 */         return StructureConstantsEnum.WALL_PLAIN_NARROW_WINDOW.value;
/*      */ 
/*      */       
/*      */       case FENCE_IRON:
/*  451 */         if (isStone)
/*  452 */           return StructureConstantsEnum.WALL_SOLID_STONE_PLAN.value; 
/*  453 */         if (isTimber)
/*  454 */           return StructureConstantsEnum.WALL_SOLID_TIMBER_FRAMED_PLAN.value; 
/*  455 */         if (isSlate)
/*  456 */           return StructureConstantsEnum.WALL_SOLID_STONE_PLAN.value; 
/*  457 */         if (isRoundedStone)
/*  458 */           return StructureConstantsEnum.WALL_SOLID_STONE_PLAN.value; 
/*  459 */         if (isPottery)
/*  460 */           return StructureConstantsEnum.WALL_SOLID_STONE_PLAN.value; 
/*  461 */         if (isSandstone)
/*  462 */           return StructureConstantsEnum.WALL_SOLID_STONE_PLAN.value; 
/*  463 */         if (isRendered)
/*  464 */           return StructureConstantsEnum.WALL_SOLID_STONE_PLAN.value; 
/*  465 */         if (isMarble)
/*  466 */           return StructureConstantsEnum.WALL_SOLID_STONE_PLAN.value; 
/*  467 */         return StructureConstantsEnum.WALL_SOLID_WOODEN_PLAN.value;
/*      */ 
/*      */       
/*      */       case FENCE_SLATE_IRON:
/*  471 */         if (isPlainStone)
/*  472 */           return StructureConstantsEnum.WALL_PORTCULLIS_STONE.value; 
/*  473 */         if (isStone)
/*  474 */           return StructureConstantsEnum.WALL_PORTCULLIS_STONE_DECORATED.value; 
/*  475 */         if (isSlate)
/*  476 */           return StructureConstantsEnum.WALL_PORTCULLIS_SLATE.value; 
/*  477 */         if (isRoundedStone)
/*  478 */           return StructureConstantsEnum.WALL_PORTCULLIS_ROUNDED_STONE.value; 
/*  479 */         if (isPottery)
/*  480 */           return StructureConstantsEnum.WALL_PORTCULLIS_POTTERY.value; 
/*  481 */         if (isSandstone)
/*  482 */           return StructureConstantsEnum.WALL_PORTCULLIS_SANDSTONE.value; 
/*  483 */         if (isRendered)
/*  484 */           return StructureConstantsEnum.WALL_PORTCULLIS_RENDERED.value; 
/*  485 */         if (isMarble)
/*  486 */           return StructureConstantsEnum.WALL_PORTCULLIS_MARBLE.value; 
/*  487 */         return StructureConstantsEnum.WALL_PORTCULLIS_WOOD.value;
/*      */ 
/*      */       
/*      */       case FENCE_ROUNDED_STONE_IRON:
/*  491 */         if (isPlainStone)
/*  492 */           return StructureConstantsEnum.WALL_BARRED_STONE.value; 
/*  493 */         if (isSlate)
/*  494 */           return StructureConstantsEnum.WALL_BARRED_SLATE.value; 
/*  495 */         if (isRoundedStone)
/*  496 */           return StructureConstantsEnum.WALL_BARRED_ROUNDED_STONE.value; 
/*  497 */         if (isPottery)
/*  498 */           return StructureConstantsEnum.WALL_BARRED_POTTERY.value; 
/*  499 */         if (isSandstone)
/*  500 */           return StructureConstantsEnum.WALL_BARRED_SANDSTONE.value; 
/*  501 */         if (isRendered)
/*  502 */           return StructureConstantsEnum.WALL_BARRED_RENDERED.value; 
/*  503 */         if (isMarble)
/*  504 */           return StructureConstantsEnum.WALL_BARRED_MARBLE.value; 
/*  505 */         return StructureConstantsEnum.WALL_BARRED_STONE.value;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_POTTERY_IRON:
/*  512 */         return StructureConstantsEnum.WALL_BALCONY_TIMBER_FRAMED.value;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_SANDSTONE_IRON:
/*  519 */         return StructureConstantsEnum.WALL_JETTY_TIMBER_FRAMED.value;
/*      */ 
/*      */       
/*      */       case FENCE_RENDERED_IRON:
/*  523 */         if (isPlainStone)
/*  524 */           return StructureConstantsEnum.WALL_ORIEL_STONE_PLAIN.value; 
/*  525 */         if (isStone)
/*  526 */           return StructureConstantsEnum.WALL_ORIEL_STONE_DECORATED.value; 
/*  527 */         if (isSlate)
/*  528 */           return StructureConstantsEnum.WALL_ORIEL_SLATE.value; 
/*  529 */         if (isRoundedStone)
/*  530 */           return StructureConstantsEnum.WALL_ORIEL_ROUNDED_STONE.value; 
/*  531 */         if (isPottery)
/*  532 */           return StructureConstantsEnum.WALL_ORIEL_POTTERY.value; 
/*  533 */         if (isSandstone)
/*  534 */           return StructureConstantsEnum.WALL_ORIEL_SANDSTONE.value; 
/*  535 */         if (isRendered)
/*  536 */           return StructureConstantsEnum.WALL_ORIEL_RENDERED.value; 
/*  537 */         if (isMarble)
/*  538 */           return StructureConstantsEnum.WALL_ORIEL_MARBLE.value; 
/*  539 */         return StructureConstantsEnum.WALL_ORIEL_STONE_DECORATED.value;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_MARBLE_IRON:
/*  550 */         return StructureConstantsEnum.WALL_CANOPY_WOODEN.value;
/*      */ 
/*      */       
/*      */       case FENCE_IRON_HIGH:
/*  554 */         return StructureConstantsEnum.WALL_DOOR_ARCHED_WOODEN.value;
/*      */       
/*      */       case FENCE_IRON_GATE:
/*  557 */         if (isStone)
/*  558 */           return StructureConstantsEnum.WALL_LEFT_ARCH_STONE_DECORATED.value; 
/*  559 */         if (isTimber)
/*  560 */           return StructureConstantsEnum.WALL_LEFT_ARCH_TIMBER_FRAMED.value; 
/*  561 */         if (isPlainStone)
/*  562 */           return StructureConstantsEnum.WALL_LEFT_ARCH_STONE.value; 
/*  563 */         if (isSlate)
/*  564 */           return StructureConstantsEnum.WALL_LEFT_ARCH_SLATE.value; 
/*  565 */         if (isRoundedStone)
/*  566 */           return StructureConstantsEnum.WALL_LEFT_ARCH_ROUNDED_STONE.value; 
/*  567 */         if (isPottery)
/*  568 */           return StructureConstantsEnum.WALL_LEFT_ARCH_POTTERY.value; 
/*  569 */         if (isSandstone)
/*  570 */           return StructureConstantsEnum.WALL_LEFT_ARCH_SANDSTONE.value; 
/*  571 */         if (isRendered)
/*  572 */           return StructureConstantsEnum.WALL_LEFT_ARCH_RENDERED.value; 
/*  573 */         if (isMarble)
/*  574 */           return StructureConstantsEnum.WALL_LEFT_ARCH_MARBLE.value; 
/*  575 */         return StructureConstantsEnum.WALL_LEFT_ARCH_WOODEN.value;
/*      */ 
/*      */       
/*      */       case FENCE_SLATE_IRON_GATE:
/*  579 */         if (isStone)
/*  580 */           return StructureConstantsEnum.WALL_RIGHT_ARCH_STONE_DECORATED.value; 
/*  581 */         if (isTimber)
/*  582 */           return StructureConstantsEnum.WALL_RIGHT_ARCH_TIMBER_FRAMED.value; 
/*  583 */         if (isPlainStone)
/*  584 */           return StructureConstantsEnum.WALL_RIGHT_ARCH_STONE.value; 
/*  585 */         if (isSlate)
/*  586 */           return StructureConstantsEnum.WALL_RIGHT_ARCH_SLATE.value; 
/*  587 */         if (isRoundedStone)
/*  588 */           return StructureConstantsEnum.WALL_RIGHT_ARCH_ROUNDED_STONE.value; 
/*  589 */         if (isPottery)
/*  590 */           return StructureConstantsEnum.WALL_RIGHT_ARCH_POTTERY.value; 
/*  591 */         if (isSandstone)
/*  592 */           return StructureConstantsEnum.WALL_RIGHT_ARCH_SANDSTONE.value; 
/*  593 */         if (isRendered)
/*  594 */           return StructureConstantsEnum.WALL_RIGHT_ARCH_RENDERED.value; 
/*  595 */         if (isMarble)
/*  596 */           return StructureConstantsEnum.WALL_RIGHT_ARCH_MARBLE.value; 
/*  597 */         return StructureConstantsEnum.WALL_RIGHT_ARCH_WOODEN.value;
/*      */ 
/*      */       
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*  601 */         if (isStone)
/*  602 */           return StructureConstantsEnum.WALL_T_ARCH_STONE_DECORATED.value; 
/*  603 */         if (isTimber)
/*  604 */           return StructureConstantsEnum.WALL_T_ARCH_TIMBER_FRAMED.value; 
/*  605 */         if (isPlainStone)
/*  606 */           return StructureConstantsEnum.WALL_T_ARCH_STONE.value; 
/*  607 */         if (isSlate)
/*  608 */           return StructureConstantsEnum.WALL_T_ARCH_SLATE.value; 
/*  609 */         if (isRoundedStone)
/*  610 */           return StructureConstantsEnum.WALL_T_ARCH_ROUNDED_STONE.value; 
/*  611 */         if (isPottery)
/*  612 */           return StructureConstantsEnum.WALL_T_ARCH_POTTERY.value; 
/*  613 */         if (isSandstone)
/*  614 */           return StructureConstantsEnum.WALL_T_ARCH_SANDSTONE.value; 
/*  615 */         if (isRendered)
/*  616 */           return StructureConstantsEnum.WALL_T_ARCH_RENDERED.value; 
/*  617 */         if (isMarble)
/*  618 */           return StructureConstantsEnum.WALL_T_ARCH_MARBLE.value; 
/*  619 */         return StructureConstantsEnum.WALL_T_ARCH_WOODEN.value;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  624 */     System.out.println("Not a legacy wall type: " + type);
/*  625 */     return 0;
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
/*      */   @Deprecated
/*      */   public static final short translateFenceType(short id) {
/*  717 */     return id;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getModelName(StructureConstantsEnum type, byte damageState, int pos) {
/*  722 */     return getModelName(type, damageState, pos, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getModelName(StructureConstantsEnum type, byte damageState, int pos, boolean initializing) {
/*      */     String modelName;
/*  729 */     if (!initializing) {
/*      */       
/*  731 */       String str = type.getModelPath();
/*      */ 
/*      */       
/*  734 */       if (pos > 0 && type.structureType == BuildingTypesEnum.HOUSE) {
/*  735 */         str = str + ".upper";
/*      */       }
/*  737 */       if (damageState >= 60) {
/*  738 */         str = str + ".decayed";
/*      */       }
/*  740 */       return str;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  745 */     switch (type) {
/*      */       
/*      */       case FENCE_WOODEN:
/*  748 */         modelName = "model.structure.wall.fence.fence";
/*      */         break;
/*      */       case FENCE_WOODEN_CRUDE:
/*  751 */         modelName = "model.structure.wall.fence.crude";
/*      */         break;
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/*  754 */         modelName = "model.structure.wall.fence.gate.crude";
/*      */         break;
/*      */       case FENCE_PALISADE:
/*  757 */         modelName = "model.structure.wall.fence.palisade";
/*      */         break;
/*      */       case FENCE_STONEWALL:
/*  760 */         modelName = "model.structure.wall.fence.stonewall.short";
/*      */         break;
/*      */       case FENCE_WOODEN_GATE:
/*  763 */         modelName = "model.structure.wall.fence.gate.fence";
/*      */         break;
/*      */       case FENCE_PALISADE_GATE:
/*  766 */         modelName = "model.structure.wall.fence.gate.palisade";
/*      */         break;
/*      */       case FENCE_STONEWALL_HIGH:
/*  769 */         modelName = "model.structure.wall.fence.stonewall.tall";
/*      */         break;
/*      */       case FENCE_IRON:
/*  772 */         modelName = "model.structure.wall.fence.iron";
/*      */         break;
/*      */       case FENCE_SLATE_IRON:
/*  775 */         modelName = "model.structure.wall.fence.iron.slate";
/*      */         break;
/*      */       case FENCE_ROUNDED_STONE_IRON:
/*  778 */         modelName = "model.structure.wall.fence.iron.roundedstone";
/*      */         break;
/*      */       case FENCE_POTTERY_IRON:
/*  781 */         modelName = "model.structure.wall.fence.iron.pottery";
/*      */         break;
/*      */       case FENCE_SANDSTONE_IRON:
/*  784 */         modelName = "model.structure.wall.fence.iron.sandstone";
/*      */         break;
/*      */       case FENCE_RENDERED_IRON:
/*  787 */         modelName = "model.structure.wall.fence.iron.rendered";
/*      */         break;
/*      */       case FENCE_MARBLE_IRON:
/*  790 */         modelName = "model.structure.wall.fence.iron.marble";
/*      */         break;
/*      */       case FENCE_IRON_HIGH:
/*  793 */         modelName = "model.structure.wall.fence.iron.high";
/*      */         break;
/*      */       case FENCE_IRON_GATE:
/*  796 */         modelName = "model.structure.wall.fence.gate.iron";
/*      */         break;
/*      */       case FENCE_SLATE_IRON_GATE:
/*  799 */         modelName = "model.structure.wall.fence.gate.iron.slate";
/*      */         break;
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*  802 */         modelName = "model.structure.wall.fence.gate.iron.roundedstone";
/*      */         break;
/*      */       case FENCE_POTTERY_IRON_GATE:
/*  805 */         modelName = "model.structure.wall.fence.gate.iron.pottery";
/*      */         break;
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/*  808 */         modelName = "model.structure.wall.fence.gate.iron.sandstone";
/*      */         break;
/*      */       case FENCE_RENDERED_IRON_GATE:
/*  811 */         modelName = "model.structure.wall.fence.gate.iron.rendered";
/*      */         break;
/*      */       case FENCE_MARBLE_IRON_GATE:
/*  814 */         modelName = "model.structure.wall.fence.gate.iron.marble";
/*      */         break;
/*      */       case FENCE_IRON_GATE_HIGH:
/*  817 */         modelName = "model.structure.wall.fence.gate.iron.high";
/*      */         break;
/*      */       case FENCE_WOVEN:
/*  820 */         modelName = "model.structure.wall.fence.woven";
/*      */         break;
/*      */       case FENCE_PLAN_WOODEN:
/*  823 */         modelName = "model.structure.wall.fence.plan.fence";
/*      */         break;
/*      */       case FENCE_PLAN_WOODEN_CRUDE:
/*  826 */         modelName = "model.structure.wall.fence.crude.plan";
/*      */         break;
/*      */       case FENCE_PLAN_WOODEN_GATE_CRUDE:
/*  829 */         modelName = "model.structure.wall.fence.crude.plan";
/*      */         break;
/*      */       case FENCE_PLAN_PALISADE:
/*  832 */         modelName = "model.structure.wall.fence.plan.palisade";
/*      */         break;
/*      */       case FENCE_PLAN_STONEWALL:
/*  835 */         modelName = "model.structure.wall.fence.plan.stonewall.short";
/*      */         break;
/*      */       case FENCE_PLAN_PALISADE_GATE:
/*  838 */         modelName = "model.structure.wall.fence.plan.gate.palisade";
/*      */         break;
/*      */       case FENCE_PLAN_WOODEN_GATE:
/*  841 */         modelName = "model.structure.wall.fence.plan.gate.fence";
/*      */         break;
/*      */       case FENCE_PLAN_STONEWALL_HIGH:
/*  844 */         modelName = "model.structure.wall.fence.plan.stonewall.tall";
/*      */         break;
/*      */       case FENCE_PLAN_IRON:
/*  847 */         modelName = "model.structure.wall.fence.plan.iron";
/*      */         break;
/*      */       case FENCE_PLAN_SLATE_IRON:
/*  850 */         modelName = "model.structure.wall.fence.plan.slate.iron";
/*      */         break;
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON:
/*  853 */         modelName = "model.structure.wall.fence.plan.roundedstone.iron";
/*      */         break;
/*      */       case FENCE_PLAN_POTTERY_IRON:
/*  856 */         modelName = "model.structure.wall.fence.plan.pottery.iron";
/*      */         break;
/*      */       case FENCE_PLAN_SANDSTONE_IRON:
/*  859 */         modelName = "model.structure.wall.fence.plan.sandstone.iron";
/*      */         break;
/*      */       case FENCE_PLAN_RENDERED_IRON:
/*  862 */         modelName = "model.structure.wall.fence.plan.rendered.iron";
/*      */         break;
/*      */       case FENCE_PLAN_MARBLE_IRON:
/*  865 */         modelName = "model.structure.wall.fence.plan.marble.iron";
/*      */         break;
/*      */       case FENCE_PLAN_IRON_HIGH:
/*  868 */         modelName = "model.structure.wall.fence.plan.iron.high";
/*      */         break;
/*      */       case FENCE_PLAN_IRON_GATE:
/*  871 */         modelName = "model.structure.wall.fence.plan.gate.iron";
/*      */         break;
/*      */       case FENCE_PLAN_SLATE_IRON_GATE:
/*  874 */         modelName = "model.structure.wall.fence.plan.slate.gate.iron";
/*      */         break;
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON_GATE:
/*  877 */         modelName = "model.structure.wall.fence.plan.roundedstone.gate.iron";
/*      */         break;
/*      */       case FENCE_PLAN_POTTERY_IRON_GATE:
/*  880 */         modelName = "model.structure.wall.fence.plan.pottery.gate.iron";
/*      */         break;
/*      */       case FENCE_PLAN_SANDSTONE_IRON_GATE:
/*  883 */         modelName = "model.structure.wall.fence.plan.sandstone.gate.iron";
/*      */         break;
/*      */       case FENCE_PLAN_RENDERED_IRON_GATE:
/*  886 */         modelName = "model.structure.wall.fence.plan.rendered.gate.iron";
/*      */         break;
/*      */       case FENCE_PLAN_MARBLE_IRON_GATE:
/*  889 */         modelName = "model.structure.wall.fence.plan.marble.gate.iron";
/*      */         break;
/*      */       case FENCE_PLAN_IRON_GATE_HIGH:
/*  892 */         modelName = "model.structure.wall.fence.plan.gate.iron.high";
/*      */         break;
/*      */       case FENCE_PLAN_WOVEN:
/*  895 */         modelName = "model.structure.wall.fence.plan.woven";
/*      */         break;
/*      */       case FENCE_STONE_PARAPET:
/*  898 */         modelName = "model.structure.wall.fence.parapet.stone";
/*      */         break;
/*      */       case FENCE_PLAN_STONE_PARAPET:
/*  901 */         modelName = "model.structure.wall.fence.plan.parapet.stone";
/*      */         break;
/*      */       case FENCE_STONE_IRON_PARAPET:
/*  904 */         modelName = "model.structure.wall.fence.parapet.stoneiron";
/*      */         break;
/*      */       case FENCE_PLAN_STONE_IRON_PARAPET:
/*  907 */         modelName = "model.structure.wall.fence.plan.parapet.stoneiron";
/*      */         break;
/*      */       case FENCE_WOODEN_PARAPET:
/*  910 */         modelName = "model.structure.wall.fence.parapet.wooden";
/*      */         break;
/*      */       case FENCE_PLAN_WOODEN_PARAPET:
/*  913 */         modelName = "model.structure.wall.fence.plan.parapet.wooden";
/*      */         break;
/*      */       case FENCE_ROPE_LOW:
/*  916 */         modelName = "model.structure.wall.fence.rope.low";
/*      */         break;
/*      */       case FENCE_GARDESGARD_LOW:
/*  919 */         modelName = "model.structure.wall.fence.garde.low";
/*      */         break;
/*      */       case FENCE_GARDESGARD_HIGH:
/*  922 */         modelName = "model.structure.wall.fence.garde.high";
/*      */         break;
/*      */       case FENCE_GARDESGARD_GATE:
/*  925 */         modelName = "model.structure.wall.fence.gate.garde";
/*      */         break;
/*      */       case FENCE_CURB:
/*  928 */         modelName = "model.structure.wall.fence.curb";
/*      */         break;
/*      */       case FENCE_ROPE_HIGH:
/*  931 */         modelName = "model.structure.wall.fence.rope.high";
/*      */         break;
/*      */       case FENCE_STONE:
/*  934 */         modelName = "model.structure.wall.fence.stone";
/*      */         break;
/*      */       case FENCE_SLATE:
/*  937 */         modelName = "model.structure.wall.fence.slate";
/*      */         break;
/*      */       case FENCE_ROUNDED_STONE:
/*  940 */         modelName = "model.structure.wall.fence.roundedstone";
/*      */         break;
/*      */       case FENCE_POTTERY:
/*  943 */         modelName = "model.structure.wall.fence.pottery";
/*      */         break;
/*      */       case FENCE_SANDSTONE:
/*  946 */         modelName = "model.structure.wall.fence.sandstone";
/*      */         break;
/*      */       case FENCE_RENDERED:
/*  949 */         modelName = "model.structure.wall.fence.rendered";
/*      */         break;
/*      */       case FENCE_MARBLE:
/*  952 */         modelName = "model.structure.wall.fence.marble";
/*      */         break;
/*      */       case FENCE_PLAN_STONE:
/*  955 */         modelName = "model.structure.wall.fence.plan.stone";
/*      */         break;
/*      */       case FENCE_PLAN_SLATE:
/*  958 */         modelName = "model.structure.wall.fence.plan.slate";
/*      */         break;
/*      */       case FENCE_PLAN_ROUNDED_STONE:
/*  961 */         modelName = "model.structure.wall.fence.plan.roundedstone";
/*      */         break;
/*      */       case FENCE_PLAN_POTTERY:
/*  964 */         modelName = "model.structure.wall.fence.plan.pottery";
/*      */         break;
/*      */       case FENCE_PLAN_SANDSTONE:
/*  967 */         modelName = "model.structure.wall.fence.plan.sandstone";
/*      */         break;
/*      */       case FENCE_PLAN_RENDERED:
/*  970 */         modelName = "model.structure.wall.fence.plan.rendered";
/*      */         break;
/*      */       case FENCE_PLAN_MARBLE:
/*  973 */         modelName = "model.structure.wall.fence.plan.marble";
/*      */         break;
/*      */       case FENCE_PLAN_ROPE_LOW:
/*  976 */         modelName = "model.structure.wall.fence.plan.rope.low";
/*      */         break;
/*      */       case FENCE_PLAN_GARDESGARD_LOW:
/*  979 */         modelName = "model.structure.wall.fence.plan.garde.low";
/*      */         break;
/*      */       case FENCE_PLAN_GARDESGARD_HIGH:
/*  982 */         modelName = "model.structure.wall.fence.plan.garde.high";
/*      */         break;
/*      */       case FENCE_PLAN_GARDESGARD_GATE:
/*  985 */         modelName = "model.structure.wall.fence.plan.garde.gate";
/*      */         break;
/*      */       case FENCE_PLAN_CURB:
/*  988 */         modelName = "model.structure.wall.fence.plan.curb";
/*      */         break;
/*      */       case FENCE_PLAN_ROPE_HIGH:
/*  991 */         modelName = "model.structure.wall.fence.plan.rope.high";
/*      */         break;
/*      */       
/*      */       case HEDGE_FLOWER1_LOW:
/*  995 */         modelName = "model.structure.wall.hedge.1.low";
/*      */         break;
/*      */       case HEDGE_FLOWER1_MEDIUM:
/*  998 */         modelName = "model.structure.wall.hedge.1.medium";
/*      */         break;
/*      */       case HEDGE_FLOWER1_HIGH:
/* 1001 */         modelName = "model.structure.wall.hedge.1.high";
/*      */         break;
/*      */       case HEDGE_FLOWER2_LOW:
/* 1004 */         modelName = "model.structure.wall.hedge.2.low";
/*      */         break;
/*      */       case HEDGE_FLOWER2_MEDIUM:
/* 1007 */         modelName = "model.structure.wall.hedge.2.medium";
/*      */         break;
/*      */       case HEDGE_FLOWER2_HIGH:
/* 1010 */         modelName = "model.structure.wall.hedge.2.high";
/*      */         break;
/*      */       case HEDGE_FLOWER3_LOW:
/* 1013 */         modelName = "model.structure.wall.hedge.3.low";
/*      */         break;
/*      */       case HEDGE_FLOWER3_MEDIUM:
/* 1016 */         modelName = "model.structure.wall.hedge.3.medium";
/*      */         break;
/*      */       case HEDGE_FLOWER3_HIGH:
/* 1019 */         modelName = "model.structure.wall.hedge.3.high";
/*      */         break;
/*      */       case HEDGE_FLOWER4_LOW:
/* 1022 */         modelName = "model.structure.wall.hedge.4.low";
/*      */         break;
/*      */       case HEDGE_FLOWER4_MEDIUM:
/* 1025 */         modelName = "model.structure.wall.hedge.4.medium";
/*      */         break;
/*      */       case HEDGE_FLOWER4_HIGH:
/* 1028 */         modelName = "model.structure.wall.hedge.4.high";
/*      */         break;
/*      */       case HEDGE_FLOWER5_LOW:
/* 1031 */         modelName = "model.structure.wall.hedge.5.low";
/*      */         break;
/*      */       case HEDGE_FLOWER5_MEDIUM:
/* 1034 */         modelName = "model.structure.wall.hedge.5.medium";
/*      */         break;
/*      */       case HEDGE_FLOWER5_HIGH:
/* 1037 */         modelName = "model.structure.wall.hedge.5.high";
/*      */         break;
/*      */       case HEDGE_FLOWER6_LOW:
/* 1040 */         modelName = "model.structure.wall.hedge.6.low";
/*      */         break;
/*      */       case HEDGE_FLOWER6_MEDIUM:
/* 1043 */         modelName = "model.structure.wall.hedge.6.medium";
/*      */         break;
/*      */       case HEDGE_FLOWER6_HIGH:
/* 1046 */         modelName = "model.structure.wall.hedge.6.high";
/*      */         break;
/*      */       case HEDGE_FLOWER7_LOW:
/* 1049 */         modelName = "model.structure.wall.hedge.7.low";
/*      */         break;
/*      */       case HEDGE_FLOWER7_MEDIUM:
/* 1052 */         modelName = "model.structure.wall.hedge.7.medium";
/*      */         break;
/*      */       case HEDGE_FLOWER7_HIGH:
/* 1055 */         modelName = "model.structure.wall.hedge.7.high";
/*      */         break;
/*      */       case FENCE_MAGIC_STONE:
/* 1058 */         modelName = "model.structure.wall.fence.magic.stone";
/*      */         break;
/*      */       
/*      */       case FENCE_MAGIC_FIRE:
/* 1062 */         modelName = "model.structure.wall.fence.magic.fire";
/*      */         break;
/*      */       case FENCE_MAGIC_ICE:
/* 1065 */         modelName = "model.structure.wall.fence.magic.ice";
/*      */         break;
/*      */       case FENCE_RUBBLE:
/* 1068 */         modelName = "model.structure.wall.fence.rubble";
/*      */         break;
/*      */       case FENCE_SIEGEWALL:
/* 1071 */         modelName = "model.structure.invismantletfence";
/*      */         break;
/*      */       case FLOWERBED_BLUE:
/* 1074 */         modelName = "model.structure.wall.flowerbed.blue";
/*      */         break;
/*      */       case FLOWERBED_GREENISH_YELLOW:
/* 1077 */         modelName = "model.structure.wall.flowerbed.greenish";
/*      */         break;
/*      */       case FLOWERBED_ORANGE_RED:
/* 1080 */         modelName = "model.structure.wall.flowerbed.orange";
/*      */         break;
/*      */       case FLOWERBED_PURPLE:
/* 1083 */         modelName = "model.structure.wall.flowerbed.purple";
/*      */         break;
/*      */       case FLOWERBED_WHITE:
/* 1086 */         modelName = "model.structure.wall.flowerbed.white";
/*      */         break;
/*      */       case FLOWERBED_WHITE_DOTTED:
/* 1089 */         modelName = "model.structure.wall.flowerbed.white.dotted";
/*      */         break;
/*      */       case FLOWERBED_YELLOW:
/* 1092 */         modelName = "model.structure.wall.flowerbed.yellow";
/*      */         break;
/*      */       
/*      */       case WALL_RUBBLE:
/* 1096 */         modelName = "model.structure.wall.rubble";
/*      */         break;
/*      */       case WALL_SOLID_WOODEN:
/* 1099 */         modelName = "model.structure.wall.house.wood";
/*      */         break;
/*      */       case WALL_CANOPY_WOODEN:
/* 1102 */         modelName = "model.structure.wall.house.canopy.wood";
/*      */         break;
/*      */       case WALL_WINDOW_WOODEN:
/* 1105 */         modelName = "model.structure.wall.house.window.wood";
/*      */         break;
/*      */       case WALL_WINDOW_WIDE_WOODEN:
/* 1108 */         modelName = "model.structure.wall.house.widewindow.wood";
/*      */         break;
/*      */       case WALL_DOOR_WOODEN:
/* 1111 */         modelName = "model.structure.wall.house.door.wood";
/*      */         break;
/*      */       case WALL_DOUBLE_DOOR_WOODEN:
/* 1114 */         modelName = "model.structure.wall.house.doubledoor.wood";
/*      */         break;
/*      */       case WALL_SOLID_STONE_DECORATED:
/* 1117 */         modelName = "model.structure.wall.house.stone";
/*      */         break;
/*      */       case WALL_ORIEL_STONE_DECORATED:
/* 1120 */         modelName = "model.structure.wall.house.oriel1.stone";
/*      */         break;
/*      */       case WALL_ORIEL_STONE_PLAIN:
/* 1123 */         modelName = "model.structure.wall.house.oriel2.stoneplain";
/*      */         break;
/*      */       case WALL_SOLID_STONE:
/* 1126 */         modelName = "model.structure.wall.house.stoneplain";
/*      */         break;
/*      */       case WALL_BARRED_STONE:
/* 1129 */         modelName = "model.structure.wall.house.bars1.stoneplain";
/*      */         break;
/*      */       case WALL_WINDOW_STONE:
/* 1132 */         modelName = "model.structure.wall.house.window.stoneplain";
/*      */         break;
/*      */       case WALL_PLAIN_NARROW_WINDOW:
/* 1135 */         modelName = "model.structure.wall.house.windownarrow.stoneplain";
/*      */         break;
/*      */       case WALL_DOOR_STONE:
/* 1138 */         modelName = "model.structure.wall.house.door.stoneplain";
/*      */         break;
/*      */       case WALL_WINDOW_STONE_DECORATED:
/* 1141 */         modelName = "model.structure.wall.house.window.stone";
/*      */         break;
/*      */       case WALL_DOOR_STONE_DECORATED:
/* 1144 */         modelName = "model.structure.wall.house.door.stone";
/*      */         break;
/*      */       case WALL_DOUBLE_DOOR_STONE_DECORATED:
/* 1147 */         modelName = "model.structure.wall.house.doubledoor.stone";
/*      */         break;
/*      */       case WALL_PLAIN_NARROW_WINDOW_PLAN:
/* 1150 */         modelName = "model.structure.wall";
/*      */         break;
/*      */       case WALL_PORTCULLIS_STONE:
/* 1153 */         modelName = "model.structure.wall.house.portcullis.stoneplain";
/*      */         break;
/*      */       case WALL_PORTCULLIS_WOOD:
/* 1156 */         modelName = "model.structure.wall.house.portcullis.wood";
/*      */         break;
/*      */       case WALL_PORTCULLIS_STONE_DECORATED:
/* 1159 */         modelName = "model.structure.wall.house.portcullis.stone";
/*      */         break;
/*      */       case WALL_DOUBLE_DOOR_STONE:
/* 1162 */         modelName = "model.structure.wall.house.doubledoor.stoneplain";
/*      */         break;
/*      */       case WALL_DOOR_ARCHED_WOODEN:
/* 1165 */         modelName = "model.structure.wall.house.arched.wood";
/*      */         break;
/*      */       case WALL_LEFT_ARCH_WOODEN:
/* 1168 */         modelName = "model.structure.wall.house.archleft.wood";
/*      */         break;
/*      */       case WALL_RIGHT_ARCH_WOODEN:
/* 1171 */         modelName = "model.structure.wall.house.archright.wood";
/*      */         break;
/*      */       case WALL_T_ARCH_WOODEN:
/* 1174 */         modelName = "model.structure.wall.house.archt.wood";
/*      */         break;
/*      */       case WALL_DOUBLE_DOOR_WOODEN_PLAN:
/* 1177 */         modelName = "model.structure.wall";
/*      */         break;
/*      */       case WALL_DOOR_ARCHED_WOODEN_PLAN:
/* 1180 */         modelName = "model.structure.wall.house.arched.plan.wood";
/*      */         break;
/*      */       case WALL_DOUBLE_DOOR_STONE_PLAN:
/* 1183 */         modelName = "model.structure.wall";
/*      */         break;
/*      */       case WALL_DOOR_ARCHED_PLAN:
/* 1186 */         modelName = "model.structure.wall.house.arched.plan.stone";
/*      */         break;
/*      */       case WALL_DOOR_ARCHED_STONE_DECORATED:
/* 1189 */         modelName = "model.structure.wall.house.arched.stone";
/*      */         break;
/*      */       case WALL_LEFT_ARCH_STONE_DECORATED:
/* 1192 */         modelName = "model.structure.wall.house.archleft.stone";
/*      */         break;
/*      */       case WALL_RIGHT_ARCH_STONE_DECORATED:
/* 1195 */         modelName = "model.structure.wall.house.archright.stone";
/*      */         break;
/*      */       case WALL_T_ARCH_STONE_DECORATED:
/* 1198 */         modelName = "model.structure.wall.house.archt.stone";
/*      */         break;
/*      */       case WALL_DOOR_ARCHED_STONE:
/* 1201 */         modelName = "model.structure.wall.house.arched.stoneplain";
/*      */         break;
/*      */       case WALL_LEFT_ARCH_STONE:
/* 1204 */         modelName = "model.structure.wall.house.archleft.stoneplain";
/*      */         break;
/*      */       case WALL_RIGHT_ARCH_STONE:
/* 1207 */         modelName = "model.structure.wall.house.archright.stoneplain";
/*      */         break;
/*      */       case WALL_T_ARCH_STONE:
/* 1210 */         modelName = "model.structure.wall.house.archt.stoneplain";
/*      */         break;
/*      */       
/*      */       case WALL_SOLID_WOODEN_PLAN:
/* 1214 */         modelName = "model.structure.wall.house.plan.wood";
/*      */         break;
/*      */       case WALL_WINDOW_WOODEN_PLAN:
/* 1217 */         modelName = "model.structure.wall.house.plan.wood";
/*      */         break;
/*      */       case WALL_DOOR_WOODEN_PLAN:
/* 1220 */         modelName = "model.structure.wall.house.plan.wood";
/*      */         break;
/*      */       case WALL_CANOPY_WOODEN_PLAN:
/* 1223 */         modelName = "model.structure.wall.house.plan.wood";
/*      */         break;
/*      */       case WALL_SOLID_STONE_PLAN:
/* 1226 */         modelName = "model.structure.wall.house.plan.stone";
/*      */         break;
/*      */       
/*      */       case WALL_ORIEL_STONE_DECORATED_PLAN:
/* 1230 */         modelName = "model.structure.wall.house.plan.stone";
/*      */         break;
/*      */       
/*      */       case WALL_WINDOW_STONE_PLAN:
/* 1234 */         modelName = "model.structure.wall.house.plan.stone";
/*      */         break;
/*      */       case WALL_DOOR_STONE_PLAN:
/* 1237 */         modelName = "model.structure.wall.house.plan.stone";
/*      */         break;
/*      */       
/*      */       case WALL_SOLID_TIMBER_FRAMED:
/* 1241 */         modelName = "model.structure.wall.house.timber";
/*      */         break;
/*      */       case WALL_WINDOW_TIMBER_FRAMED:
/* 1244 */         modelName = "model.structure.wall.house.window.timber";
/*      */         break;
/*      */       case WALL_DOOR_TIMBER_FRAMED:
/* 1247 */         modelName = "model.structure.wall.house.door.timber";
/*      */         break;
/*      */       case WALL_DOUBLE_DOOR_TIMBER_FRAMED:
/* 1250 */         modelName = "model.structure.wall.house.doubledoor.timber";
/*      */         break;
/*      */       case WALL_DOOR_ARCHED_TIMBER_FRAMED:
/* 1253 */         modelName = "model.structure.wall.house.arched.timber";
/*      */         break;
/*      */       case WALL_LEFT_ARCH_TIMBER_FRAMED:
/* 1256 */         modelName = "model.structure.wall.house.archleft.timber";
/*      */         break;
/*      */       case WALL_RIGHT_ARCH_TIMBER_FRAMED:
/* 1259 */         modelName = "model.structure.wall.house.archright.timber";
/*      */         break;
/*      */       case WALL_T_ARCH_TIMBER_FRAMED:
/* 1262 */         modelName = "model.structure.wall.house.archt.timber";
/*      */         break;
/*      */       case WALL_BALCONY_TIMBER_FRAMED:
/* 1265 */         modelName = "model.structure.wall.house.balcony.timber";
/*      */         break;
/*      */       case WALL_JETTY_TIMBER_FRAMED:
/* 1268 */         modelName = "model.structure.wall.house.jetty.timber";
/*      */         break;
/*      */       
/*      */       case WALL_SOLID_TIMBER_FRAMED_PLAN:
/*      */       case WALL_BALCONY_TIMBER_FRAMED_PLAN:
/*      */       case WALL_JETTY_TIMBER_FRAMED_PLAN:
/*      */       case WALL_WINDOW_TIMBER_FRAMED_PLAN:
/*      */       case WALL_DOOR_TIMBER_FRAMED_PLAN:
/*      */       case WALL_DOUBLE_DOOR_TIMBER_FRAMED_PLAN:
/*      */       case WALL_DOOR_ARCHED_TIMBER_FRAMED_PLAN:
/* 1278 */         modelName = "model.structure.wall.house.plan.wood";
/*      */         break;
/*      */       
/*      */       case WALL_SOLID_SLATE:
/* 1282 */         modelName = "model.structure.wall.house.slate";
/*      */         break;
/*      */       case WALL_WINDOW_SLATE:
/* 1285 */         modelName = "model.structure.wall.house.window.slate";
/*      */         break;
/*      */       case WALL_NARROW_WINDOW_SLATE:
/* 1288 */         modelName = "model.structure.wall.house.windownarrow.slate";
/*      */         break;
/*      */       case WALL_DOOR_SLATE:
/* 1291 */         modelName = "model.structure.wall.house.door.slate";
/*      */         break;
/*      */       case WALL_DOUBLE_DOOR_SLATE:
/* 1294 */         modelName = "model.structure.wall.house.doubledoor.slate";
/*      */         break;
/*      */       case WALL_ARCHED_SLATE:
/* 1297 */         modelName = "model.structure.wall.house.arched.slate";
/*      */         break;
/*      */       case WALL_PORTCULLIS_SLATE:
/* 1300 */         modelName = "model.structure.wall.house.portcullis.slate";
/*      */         break;
/*      */       case WALL_BARRED_SLATE:
/* 1303 */         modelName = "model.structure.wall.house.bars1.slate";
/*      */         break;
/*      */       case WALL_ORIEL_SLATE:
/* 1306 */         modelName = "model.structure.wall.house.oriel2.slate";
/*      */         break;
/*      */       case WALL_LEFT_ARCH_SLATE:
/* 1309 */         modelName = "model.structure.wall.house.archleft.slate";
/*      */         break;
/*      */       case WALL_RIGHT_ARCH_SLATE:
/* 1312 */         modelName = "model.structure.wall.house.archright.slate";
/*      */         break;
/*      */       case WALL_T_ARCH_SLATE:
/* 1315 */         modelName = "model.structure.wall.house.archt.slate";
/*      */         break;
/*      */       
/*      */       case WALL_SOLID_ROUNDED_STONE:
/* 1319 */         modelName = "model.structure.wall.house.roundedstone";
/*      */         break;
/*      */       case WALL_WINDOW_ROUNDED_STONE:
/* 1322 */         modelName = "model.structure.wall.house.window.roundedstone";
/*      */         break;
/*      */       case WALL_NARROW_WINDOW_ROUNDED_STONE:
/* 1325 */         modelName = "model.structure.wall.house.windownarrow.roundedstone";
/*      */         break;
/*      */       case WALL_DOOR_ROUNDED_STONE:
/* 1328 */         modelName = "model.structure.wall.house.door.roundedstone";
/*      */         break;
/*      */       case WALL_DOUBLE_DOOR_ROUNDED_STONE:
/* 1331 */         modelName = "model.structure.wall.house.doubledoor.roundedstone";
/*      */         break;
/*      */       case WALL_ARCHED_ROUNDED_STONE:
/* 1334 */         modelName = "model.structure.wall.house.arched.roundedstone";
/*      */         break;
/*      */       case WALL_PORTCULLIS_ROUNDED_STONE:
/* 1337 */         modelName = "model.structure.wall.house.portcullis.roundedstone";
/*      */         break;
/*      */       case WALL_BARRED_ROUNDED_STONE:
/* 1340 */         modelName = "model.structure.wall.house.bars1.roundedstone";
/*      */         break;
/*      */       case WALL_ORIEL_ROUNDED_STONE:
/* 1343 */         modelName = "model.structure.wall.house.oriel2.roundedstone";
/*      */         break;
/*      */       case WALL_LEFT_ARCH_ROUNDED_STONE:
/* 1346 */         modelName = "model.structure.wall.house.archleft.roundedstone";
/*      */         break;
/*      */       case WALL_RIGHT_ARCH_ROUNDED_STONE:
/* 1349 */         modelName = "model.structure.wall.house.archright.roundedstone";
/*      */         break;
/*      */       case WALL_T_ARCH_ROUNDED_STONE:
/* 1352 */         modelName = "model.structure.wall.house.archt.roundedstone";
/*      */         break;
/*      */       
/*      */       case WALL_SOLID_POTTERY:
/* 1356 */         modelName = "model.structure.wall.house.pottery";
/*      */         break;
/*      */       case WALL_WINDOW_POTTERY:
/* 1359 */         modelName = "model.structure.wall.house.window.pottery";
/*      */         break;
/*      */       case WALL_NARROW_WINDOW_POTTERY:
/* 1362 */         modelName = "model.structure.wall.house.windownarrow.pottery";
/*      */         break;
/*      */       case WALL_DOOR_POTTERY:
/* 1365 */         modelName = "model.structure.wall.house.door.pottery";
/*      */         break;
/*      */       case WALL_DOUBLE_DOOR_POTTERY:
/* 1368 */         modelName = "model.structure.wall.house.doubledoor.pottery";
/*      */         break;
/*      */       case WALL_ARCHED_POTTERY:
/* 1371 */         modelName = "model.structure.wall.house.arched.pottery";
/*      */         break;
/*      */       case WALL_PORTCULLIS_POTTERY:
/* 1374 */         modelName = "model.structure.wall.house.portcullis.pottery";
/*      */         break;
/*      */       case WALL_BARRED_POTTERY:
/* 1377 */         modelName = "model.structure.wall.house.bars1.pottery";
/*      */         break;
/*      */       case WALL_ORIEL_POTTERY:
/* 1380 */         modelName = "model.structure.wall.house.oriel2.pottery";
/*      */         break;
/*      */       case WALL_LEFT_ARCH_POTTERY:
/* 1383 */         modelName = "model.structure.wall.house.archleft.pottery";
/*      */         break;
/*      */       case WALL_RIGHT_ARCH_POTTERY:
/* 1386 */         modelName = "model.structure.wall.house.archright.pottery";
/*      */         break;
/*      */       case WALL_T_ARCH_POTTERY:
/* 1389 */         modelName = "model.structure.wall.house.archt.pottery";
/*      */         break;
/*      */       
/*      */       case WALL_SOLID_SANDSTONE:
/* 1393 */         modelName = "model.structure.wall.house.sandstone";
/*      */         break;
/*      */       case WALL_WINDOW_SANDSTONE:
/* 1396 */         modelName = "model.structure.wall.house.window.sandstone";
/*      */         break;
/*      */       case WALL_NARROW_WINDOW_SANDSTONE:
/* 1399 */         modelName = "model.structure.wall.house.windownarrow.sandstone";
/*      */         break;
/*      */       case WALL_DOOR_SANDSTONE:
/* 1402 */         modelName = "model.structure.wall.house.door.sandstone";
/*      */         break;
/*      */       case WALL_DOUBLE_DOOR_SANDSTONE:
/* 1405 */         modelName = "model.structure.wall.house.doubledoor.sandstone";
/*      */         break;
/*      */       case WALL_ARCHED_SANDSTONE:
/* 1408 */         modelName = "model.structure.wall.house.arched.sandstone";
/*      */         break;
/*      */       case WALL_PORTCULLIS_SANDSTONE:
/* 1411 */         modelName = "model.structure.wall.house.portcullis.sandstone";
/*      */         break;
/*      */       case WALL_BARRED_SANDSTONE:
/* 1414 */         modelName = "model.structure.wall.house.bars1.sandstone";
/*      */         break;
/*      */       case WALL_ORIEL_SANDSTONE:
/* 1417 */         modelName = "model.structure.wall.house.oriel2.sandstone";
/*      */         break;
/*      */       case WALL_LEFT_ARCH_SANDSTONE:
/* 1420 */         modelName = "model.structure.wall.house.archleft.sandstone";
/*      */         break;
/*      */       case WALL_RIGHT_ARCH_SANDSTONE:
/* 1423 */         modelName = "model.structure.wall.house.archright.sandstone";
/*      */         break;
/*      */       case WALL_T_ARCH_SANDSTONE:
/* 1426 */         modelName = "model.structure.wall.house.archt.sandstone";
/*      */         break;
/*      */       
/*      */       case WALL_SOLID_RENDERED:
/* 1430 */         modelName = "model.structure.wall.house.rendered";
/*      */         break;
/*      */       case WALL_WINDOW_RENDERED:
/* 1433 */         modelName = "model.structure.wall.house.window.rendered";
/*      */         break;
/*      */       case WALL_NARROW_WINDOW_RENDERED:
/* 1436 */         modelName = "model.structure.wall.house.windownarrow.rendered";
/*      */         break;
/*      */       case WALL_DOOR_RENDERED:
/* 1439 */         modelName = "model.structure.wall.house.door.rendered";
/*      */         break;
/*      */       case WALL_DOUBLE_DOOR_RENDERED:
/* 1442 */         modelName = "model.structure.wall.house.doubledoor.rendered";
/*      */         break;
/*      */       case WALL_ARCHED_RENDERED:
/* 1445 */         modelName = "model.structure.wall.house.arched.rendered";
/*      */         break;
/*      */       case WALL_PORTCULLIS_RENDERED:
/* 1448 */         modelName = "model.structure.wall.house.portcullis.rendered";
/*      */         break;
/*      */       case WALL_BARRED_RENDERED:
/* 1451 */         modelName = "model.structure.wall.house.bars1.rendered";
/*      */         break;
/*      */       case WALL_ORIEL_RENDERED:
/* 1454 */         modelName = "model.structure.wall.house.oriel2.rendered";
/*      */         break;
/*      */       case WALL_LEFT_ARCH_RENDERED:
/* 1457 */         modelName = "model.structure.wall.house.archleft.rendered";
/*      */         break;
/*      */       case WALL_RIGHT_ARCH_RENDERED:
/* 1460 */         modelName = "model.structure.wall.house.archright.rendered";
/*      */         break;
/*      */       case WALL_T_ARCH_RENDERED:
/* 1463 */         modelName = "model.structure.wall.house.archt.rendered";
/*      */         break;
/*      */       
/*      */       case WALL_SOLID_MARBLE:
/* 1467 */         modelName = "model.structure.wall.house.marble";
/*      */         break;
/*      */       case WALL_WINDOW_MARBLE:
/* 1470 */         modelName = "model.structure.wall.house.window.marble";
/*      */         break;
/*      */       case WALL_NARROW_WINDOW_MARBLE:
/* 1473 */         modelName = "model.structure.wall.house.windownarrow.marble";
/*      */         break;
/*      */       case WALL_DOOR_MARBLE:
/* 1476 */         modelName = "model.structure.wall.house.door.marble";
/*      */         break;
/*      */       case WALL_DOUBLE_DOOR_MARBLE:
/* 1479 */         modelName = "model.structure.wall.house.doubledoor.marble";
/*      */         break;
/*      */       case WALL_ARCHED_MARBLE:
/* 1482 */         modelName = "model.structure.wall.house.arched.marble";
/*      */         break;
/*      */       case WALL_PORTCULLIS_MARBLE:
/* 1485 */         modelName = "model.structure.wall.house.portcullis.marble";
/*      */         break;
/*      */       case WALL_BARRED_MARBLE:
/* 1488 */         modelName = "model.structure.wall.house.bars1.marble";
/*      */         break;
/*      */       case WALL_ORIEL_MARBLE:
/* 1491 */         modelName = "model.structure.wall.house.oriel2.marble";
/*      */         break;
/*      */       case WALL_LEFT_ARCH_MARBLE:
/* 1494 */         modelName = "model.structure.wall.house.archleft.marble";
/*      */         break;
/*      */       case WALL_RIGHT_ARCH_MARBLE:
/* 1497 */         modelName = "model.structure.wall.house.archright.marble";
/*      */         break;
/*      */       case WALL_T_ARCH_MARBLE:
/* 1500 */         modelName = "model.structure.wall.house.archt.marble";
/*      */         break;
/*      */       case NO_WALL:
/* 1503 */         modelName = "model.structure.wall.house.plan.incomplete";
/*      */         break;
/*      */       case FENCE_MEDIUM_CHAIN:
/* 1506 */         modelName = "model.structure.wall.fence.chains";
/*      */         break;
/*      */       case FENCE_PLAN_MEDIUM_CHAIN:
/* 1509 */         modelName = "model.structure.wall.fence.plan.chains";
/*      */         break;
/*      */       case FENCE_PLAN_PORTCULLIS:
/* 1512 */         modelName = "model.structure.wall.fence.plan.stonewallPortcullis";
/*      */         break;
/*      */       case FENCE_PORTCULLIS:
/* 1515 */         modelName = "model.structure.wall.fence.stonewallPortcullis";
/*      */         break;
/*      */       case FENCE_SLATE_TALL_STONE_WALL:
/* 1518 */         modelName = "model.structure.wall.fence.stonewall.tall.slate";
/*      */         break;
/*      */       case FENCE_SLATE_PORTCULLIS:
/* 1521 */         modelName = "model.structure.wall.fence.stonewallPortcullis.slate";
/*      */         break;
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE:
/* 1524 */         modelName = "model.structure.wall.fence.iron.high.slate";
/*      */         break;
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/* 1527 */         modelName = "model.structure.wall.fence.gate.iron.high.slate";
/*      */         break;
/*      */       case FENCE_SLATE_STONE_PARAPET:
/* 1530 */         modelName = "model.structure.wall.fence.parapet.slate";
/*      */         break;
/*      */       case FENCE_SLATE_CHAIN_FENCE:
/* 1533 */         modelName = "model.structure.wall.fence.chains.slate";
/*      */         break;
/*      */       case FENCE_ROUNDED_STONE_TALL_STONE_WALL:
/* 1536 */         modelName = "model.structure.wall.fence.stonewall.tall.roundedstone";
/*      */         break;
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/* 1539 */         modelName = "model.structure.wall.fence.stonewallPortcullis.roundedstone";
/*      */         break;
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE:
/* 1542 */         modelName = "model.structure.wall.fence.iron.high.roundedstone";
/*      */         break;
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/* 1545 */         modelName = "model.structure.wall.fence.gate.iron.high.roundedstone";
/*      */         break;
/*      */       case FENCE_ROUNDED_STONE_STONE_PARAPET:
/* 1548 */         modelName = "model.structure.wall.fence.parapet.roundedstone";
/*      */         break;
/*      */       case FENCE_ROUNDED_STONE_CHAIN_FENCE:
/* 1551 */         modelName = "model.structure.wall.fence.chains.roundedstone";
/*      */         break;
/*      */       case FENCE_SANDSTONE_TALL_STONE_WALL:
/* 1554 */         modelName = "model.structure.wall.fence.stonewall.tall.sandstone";
/*      */         break;
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/* 1557 */         modelName = "model.structure.wall.fence.stonewallPortcullis.sandstone";
/*      */         break;
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE:
/* 1560 */         modelName = "model.structure.wall.fence.iron.high.sandstone";
/*      */         break;
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/* 1563 */         modelName = "model.structure.wall.fence.gate.iron.high.sandstone";
/*      */         break;
/*      */       case FENCE_SANDSTONE_STONE_PARAPET:
/* 1566 */         modelName = "model.structure.wall.fence.parapet.sandstone";
/*      */         break;
/*      */       case FENCE_SANDSTONE_CHAIN_FENCE:
/* 1569 */         modelName = "model.structure.wall.fence.chains.sandstone";
/*      */         break;
/*      */       case FENCE_RENDERED_TALL_STONE_WALL:
/* 1572 */         modelName = "model.structure.wall.fence.stonewall.tall.rendered";
/*      */         break;
/*      */       case FENCE_RENDERED_PORTCULLIS:
/* 1575 */         modelName = "model.structure.wall.fence.stonewallPortcullis.rendered";
/*      */         break;
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE:
/* 1578 */         modelName = "model.structure.wall.fence.iron.high.rendered";
/*      */         break;
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/* 1581 */         modelName = "model.structure.wall.fence.gate.iron.high.rendered";
/*      */         break;
/*      */       case FENCE_RENDERED_STONE_PARAPET:
/* 1584 */         modelName = "model.structure.wall.fence.parapet.rendered";
/*      */         break;
/*      */       case FENCE_RENDERED_CHAIN_FENCE:
/* 1587 */         modelName = "model.structure.wall.fence.chains.rendered";
/*      */         break;
/*      */       case FENCE_POTTERY_TALL_STONE_WALL:
/* 1590 */         modelName = "model.structure.wall.fence.stonewall.tall.pottery";
/*      */         break;
/*      */       case FENCE_POTTERY_PORTCULLIS:
/* 1593 */         modelName = "model.structure.wall.fence.stonewallPortcullis.pottery";
/*      */         break;
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE:
/* 1596 */         modelName = "model.structure.wall.fence.iron.high.pottery";
/*      */         break;
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/* 1599 */         modelName = "model.structure.wall.fence.gate.iron.high.pottery";
/*      */         break;
/*      */       case FENCE_POTTERY_STONE_PARAPET:
/* 1602 */         modelName = "model.structure.wall.fence.parapet.pottery";
/*      */         break;
/*      */       case FENCE_POTTERY_CHAIN_FENCE:
/* 1605 */         modelName = "model.structure.wall.fence.chains.pottery";
/*      */         break;
/*      */       case FENCE_MARBLE_TALL_STONE_WALL:
/* 1608 */         modelName = "model.structure.wall.fence.stonewall.tall.marble";
/*      */         break;
/*      */       case FENCE_MARBLE_PORTCULLIS:
/* 1611 */         modelName = "model.structure.wall.fence.stonewallPortcullis.marble";
/*      */         break;
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE:
/* 1614 */         modelName = "model.structure.wall.fence.iron.high.marble";
/*      */         break;
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/* 1617 */         modelName = "model.structure.wall.fence.gate.iron.high.marble";
/*      */         break;
/*      */       case FENCE_MARBLE_STONE_PARAPET:
/* 1620 */         modelName = "model.structure.wall.fence.parapet.marble";
/*      */         break;
/*      */       case FENCE_MARBLE_CHAIN_FENCE:
/* 1623 */         modelName = "model.structure.wall.fence.chains.marble";
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_SLATE_TALL_STONE_WALL:
/* 1629 */         modelName = "model.structure.wall.fence.plan.stonewall.tall.slate";
/*      */         break;
/*      */       case FENCE_PLAN_SLATE_PORTCULLIS:
/* 1632 */         modelName = "model.structure.wall.fence.plan.stonewallPortcullis.slate";
/*      */         break;
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE:
/* 1635 */         modelName = "model.structure.wall.fence.plan.iron.high.slate";
/*      */         break;
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE:
/* 1638 */         modelName = "model.structure.wall.fence.plan.gate.iron.high.slate";
/*      */         break;
/*      */       case FENCE_PLAN_SLATE_STONE_PARAPET:
/* 1641 */         modelName = "model.structure.wall.fence.plan.parapet.slate";
/*      */         break;
/*      */       case FENCE_PLAN_SLATE_CHAIN_FENCE:
/* 1644 */         modelName = "model.structure.wall.fence.plan.chains.slate";
/*      */         break;
/*      */       case FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL:
/* 1647 */         modelName = "model.structure.wall.fence.plan.stonewall.tall.roundedstone";
/*      */         break;
/*      */       case FENCE_PLAN_ROUNDED_STONE_PORTCULLIS:
/* 1650 */         modelName = "model.structure.wall.fence.plan.stonewallPortcullis.roundedstone";
/*      */         break;
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE:
/* 1653 */         modelName = "model.structure.wall.fence.plan.iron.high.roundedstone";
/*      */         break;
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/* 1656 */         modelName = "model.structure.wall.fence.plan.gate.iron.high.roundedstone";
/*      */         break;
/*      */       case FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET:
/* 1659 */         modelName = "model.structure.wall.fence.plan.parapet.roundedstone";
/*      */         break;
/*      */       case FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE:
/* 1662 */         modelName = "model.structure.wall.fence.plan.chains.roundedstone";
/*      */         break;
/*      */       case FENCE_PLAN_SANDSTONE_TALL_STONE_WALL:
/* 1665 */         modelName = "model.structure.wall.fence.plan.stonewall.tall.sandstone";
/*      */         break;
/*      */       case FENCE_PLAN_SANDSTONE_PORTCULLIS:
/* 1668 */         modelName = "model.structure.wall.fence.plan.stonewallPortcullis.sandstone";
/*      */         break;
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE:
/* 1671 */         modelName = "model.structure.wall.fence.plan.iron.high.sandstone";
/*      */         break;
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE:
/* 1674 */         modelName = "model.structure.wall.fence.plan.gate.iron.high.sandstone";
/*      */         break;
/*      */       case FENCE_PLAN_SANDSTONE_STONE_PARAPET:
/* 1677 */         modelName = "model.structure.wall.fence.plan.parapet.sandstone";
/*      */         break;
/*      */       case FENCE_PLAN_SANDSTONE_CHAIN_FENCE:
/* 1680 */         modelName = "model.structure.wall.fence.plan.chains.sandstone";
/*      */         break;
/*      */       case FENCE_PLAN_RENDERED_TALL_STONE_WALL:
/* 1683 */         modelName = "model.structure.wall.fence.plan.stonewall.tall.rendered";
/*      */         break;
/*      */       case FENCE_PLAN_RENDERED_PORTCULLIS:
/* 1686 */         modelName = "model.structure.wall.fence.plan.stonewallPortcullis.rendered";
/*      */         break;
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE:
/* 1689 */         modelName = "model.structure.wall.fence.plan.iron.high.rendered";
/*      */         break;
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE_GATE:
/* 1692 */         modelName = "model.structure.wall.fence.plan.gate.iron.high.rendered";
/*      */         break;
/*      */       case FENCE_PLAN_RENDERED_STONE_PARAPET:
/* 1695 */         modelName = "model.structure.wall.fence.plan.parapet.rendered";
/*      */         break;
/*      */       case FENCE_PLAN_RENDERED_CHAIN_FENCE:
/* 1698 */         modelName = "model.structure.wall.fence.plan.chains.rendered";
/*      */         break;
/*      */       case FENCE_PLAN_POTTERY_TALL_STONE_WALL:
/* 1701 */         modelName = "model.structure.wall.fence.plan.stonewall.tall.pottery";
/*      */         break;
/*      */       case FENCE_PLAN_POTTERY_PORTCULLIS:
/* 1704 */         modelName = "model.structure.wall.fence.plan.stonewallPortcullis.pottery";
/*      */         break;
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE:
/* 1707 */         modelName = "model.structure.wall.fence.plan.iron.high.pottery";
/*      */         break;
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE:
/* 1710 */         modelName = "model.structure.wall.fence.plan.gate.iron.high.pottery";
/*      */         break;
/*      */       case FENCE_PLAN_POTTERY_STONE_PARAPET:
/* 1713 */         modelName = "model.structure.wall.fence.plan.parapet.pottery";
/*      */         break;
/*      */       case FENCE_PLAN_POTTERY_CHAIN_FENCE:
/* 1716 */         modelName = "model.structure.wall.fence.plan.chains.pottery";
/*      */         break;
/*      */       case FENCE_PLAN_MARBLE_TALL_STONE_WALL:
/* 1719 */         modelName = "model.structure.wall.fence.plan.stonewall.tall.marble";
/*      */         break;
/*      */       case FENCE_PLAN_MARBLE_PORTCULLIS:
/* 1722 */         modelName = "model.structure.wall.fence.plan.stonewallPortcullis.marble";
/*      */         break;
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE:
/* 1725 */         modelName = "model.structure.wall.fence.plan.iron.high.marble";
/*      */         break;
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE:
/* 1728 */         modelName = "model.structure.wall.fence.plan.gate.iron.high.marble";
/*      */         break;
/*      */       case FENCE_PLAN_MARBLE_STONE_PARAPET:
/* 1731 */         modelName = "model.structure.wall.fence.plan.parapet.marble";
/*      */         break;
/*      */       case FENCE_PLAN_MARBLE_CHAIN_FENCE:
/* 1734 */         modelName = "model.structure.wall.fence.plan.chains.marble";
/*      */         break;
/*      */       
/*      */       default:
/* 1738 */         modelName = "model.structure.wall";
/*      */         break;
/*      */     } 
/* 1741 */     if (pos > 0 && type.structureType == BuildingTypesEnum.HOUSE) {
/* 1742 */       modelName = modelName + ".upper";
/*      */     }
/* 1744 */     if (damageState >= 60) {
/* 1745 */       modelName = modelName + ".decayed";
/*      */     }
/* 1747 */     return modelName;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final String getTextureName(StructureConstantsEnum type) {
/* 1752 */     return getTextureName(type, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getTextureName(StructureConstantsEnum type, boolean initializing) {
/* 1759 */     if (!initializing)
/*      */     {
/* 1761 */       return type.getTexturePath();
/*      */     }
/*      */     
/* 1764 */     switch (type)
/*      */     
/*      */     { 
/*      */       case FENCE_WOODEN:
/* 1768 */         return "img.texture.fence.fence";
/*      */       case FENCE_PALISADE:
/* 1770 */         return "img.texture.fence.palisade";
/*      */       case FENCE_STONEWALL:
/* 1772 */         return "img.texture.fence.stonewall.short";
/*      */       case FENCE_WOODEN_GATE:
/* 1774 */         return "img.texture.fence.gate.fence";
/*      */       case FENCE_PALISADE_GATE:
/* 1776 */         return "img.texture.fence.gate.palisade";
/*      */       case FENCE_STONEWALL_HIGH:
/* 1778 */         return "img.texture.fence.stonewall.tall";
/*      */       case FENCE_IRON:
/* 1780 */         return "img.texture.fence.iron";
/*      */       case FENCE_SLATE_IRON:
/* 1782 */         return "img.texture.fence.slate.iron";
/*      */       case FENCE_ROUNDED_STONE_IRON:
/* 1784 */         return "img.texture.fence.roundedstone.iron";
/*      */       case FENCE_POTTERY_IRON:
/* 1786 */         return "img.texture.fence.pottery.iron";
/*      */       case FENCE_SANDSTONE_IRON:
/* 1788 */         return "img.texture.fence.sandstone.iron";
/*      */       case FENCE_RENDERED_IRON:
/* 1790 */         return "img.texture.fence.rendered.iron";
/*      */       case FENCE_MARBLE_IRON:
/* 1792 */         return "img.texture.fence.marble.iron";
/*      */       case FENCE_IRON_HIGH:
/* 1794 */         return "img.texture.fence.iron.high";
/*      */       case FENCE_IRON_GATE:
/* 1796 */         return "img.texture.fence.gate.iron";
/*      */       case FENCE_SLATE_IRON_GATE:
/* 1798 */         return "img.texture.fence.slate.iron.gate";
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/* 1800 */         return "img.texture.fence.roundedstone.iron.gate";
/*      */       case FENCE_POTTERY_IRON_GATE:
/* 1802 */         return "img.texture.fence.pottery.iron.gate";
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/* 1804 */         return "img.texture.fence.sandstone.iron.gate";
/*      */       case FENCE_RENDERED_IRON_GATE:
/* 1806 */         return "img.texture.fence.rendered.iron.gate";
/*      */       case FENCE_MARBLE_IRON_GATE:
/* 1808 */         return "img.texture.fence.marble.iron.gate";
/*      */       case FENCE_IRON_GATE_HIGH:
/* 1810 */         return "img.texture.fence.gate.iron.high";
/*      */       case FENCE_WOVEN:
/* 1812 */         return "img.texture.fence.woven";
/*      */       
/*      */       case FENCE_PLAN_WOODEN:
/* 1815 */         return "img.texture.fence.plan.fence";
/*      */       case FENCE_PLAN_WOODEN_CRUDE:
/* 1817 */         return "img.texture.fence.plan.crude";
/*      */       case FENCE_PLAN_WOODEN_GATE_CRUDE:
/* 1819 */         return "img.texture.fence.plan.gate.crude";
/*      */       case FENCE_WOODEN_CRUDE:
/* 1821 */         return "img.texture.fence.crude";
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/* 1823 */         return "img.texture.fence.gate.crude";
/*      */       case FENCE_PLAN_PALISADE:
/* 1825 */         return "img.texture.fence.plan.palisade";
/*      */       case FENCE_PLAN_STONEWALL:
/* 1827 */         return "img.texture.fence.plan.stonewall.short";
/*      */       case FENCE_PLAN_PALISADE_GATE:
/* 1829 */         return "img.texture.fence.plan.gate.palisade";
/*      */       case FENCE_PLAN_WOODEN_GATE:
/* 1831 */         return "img.texture.fence.plan.gate.fence";
/*      */       case FENCE_PLAN_STONEWALL_HIGH:
/* 1833 */         return "img.texture.fence.plan.stonewall.tall";
/*      */       case FENCE_PLAN_IRON:
/*      */       case FENCE_PLAN_SLATE_IRON:
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON:
/*      */       case FENCE_PLAN_POTTERY_IRON:
/*      */       case FENCE_PLAN_SANDSTONE_IRON:
/*      */       case FENCE_PLAN_MARBLE_IRON:
/* 1840 */         return "img.texture.fence.plan.iron";
/*      */       case FENCE_PLAN_IRON_HIGH:
/* 1842 */         return "img.texture.fence.plan.iron.high";
/*      */       case FENCE_PLAN_IRON_GATE:
/*      */       case FENCE_PLAN_SLATE_IRON_GATE:
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_PLAN_POTTERY_IRON_GATE:
/*      */       case FENCE_PLAN_SANDSTONE_IRON_GATE:
/*      */       case FENCE_PLAN_MARBLE_IRON_GATE:
/* 1849 */         return "img.texture.fence.plan.gate.iron";
/*      */       case FENCE_PLAN_IRON_GATE_HIGH:
/* 1851 */         return "img.texture.fence.plan.gate.iron.high";
/*      */       case FENCE_PLAN_WOVEN:
/* 1853 */         return "img.texture.fence.plan.woven";
/*      */       case FENCE_PLAN_WOODEN_PARAPET:
/* 1855 */         return "img.texture.fence.plan.parapet.wooden";
/*      */       case FENCE_WOODEN_PARAPET:
/* 1857 */         return "img.texture.fence.parapet.wooden";
/*      */       case FENCE_PLAN_STONE_PARAPET:
/* 1859 */         return "img.texture.fence.plan.parapet.stone";
/*      */       case FENCE_PLAN_STONE_IRON_PARAPET:
/* 1861 */         return "img.texture.fence.plan.parapet.stoneiron";
/*      */       case FENCE_STONE_IRON_PARAPET:
/* 1863 */         return "img.texture.fence.parapet.stoneiron";
/*      */       case FENCE_STONE_PARAPET:
/* 1865 */         return "img.texture.fence.parapet.stone";
/*      */       case FENCE_ROPE_LOW:
/* 1867 */         return "img.texture.fence.rope.low";
/*      */       case FENCE_GARDESGARD_LOW:
/* 1869 */         return "img.texture.fence.garde.low";
/*      */       case FENCE_GARDESGARD_HIGH:
/* 1871 */         return "img.texture.fence.garde.high";
/*      */       case FENCE_GARDESGARD_GATE:
/* 1873 */         return "img.texture.fence.garde.gate";
/*      */       case FENCE_CURB:
/* 1875 */         return "img.texture.fence.curb";
/*      */       case FENCE_ROPE_HIGH:
/* 1877 */         return "img.texture.fence.rope.high";
/*      */       case FENCE_PLAN_ROPE_LOW:
/* 1879 */         return "img.texture.fence.plan.rope.low";
/*      */       case FENCE_PLAN_GARDESGARD_LOW:
/* 1881 */         return "img.texture.fence.plan.garde.low";
/*      */       case FENCE_PLAN_GARDESGARD_HIGH:
/* 1883 */         return "img.texture.fence.plan.garde.high";
/*      */       case FENCE_PLAN_GARDESGARD_GATE:
/* 1885 */         return "img.texture.fence.plan.garde.gate";
/*      */       case FENCE_PLAN_CURB:
/* 1887 */         return "img.texture.fence.plan.curb";
/*      */       case FENCE_PLAN_ROPE_HIGH:
/* 1889 */         return "img.texture.fence.plan.rope.high";
/*      */       case FENCE_STONE:
/* 1891 */         return "img.texture.fence.stone";
/*      */       case FENCE_SLATE:
/* 1893 */         return "img.texture.fence.slate";
/*      */       case FENCE_ROUNDED_STONE:
/* 1895 */         return "img.texture.fence.roundedstone";
/*      */       case FENCE_POTTERY:
/* 1897 */         return "img.texture.fence.pottery";
/*      */       case FENCE_SANDSTONE:
/* 1899 */         return "img.texture.fence.sandstone";
/*      */       case FENCE_RENDERED:
/* 1901 */         return "img.texture.fence.rendered";
/*      */       case FENCE_MARBLE:
/* 1903 */         return "img.texture.fence.marble";
/*      */       case FENCE_PLAN_STONE:
/*      */       case FENCE_PLAN_SLATE:
/*      */       case FENCE_PLAN_ROUNDED_STONE:
/*      */       case FENCE_PLAN_POTTERY:
/*      */       case FENCE_PLAN_SANDSTONE:
/*      */       case FENCE_PLAN_RENDERED:
/*      */       case FENCE_PLAN_MARBLE:
/* 1911 */         return "img.texture.fence.plan.stone";
/*      */       
/*      */       case HEDGE_FLOWER1_LOW:
/* 1914 */         return "img.texture.hedge.1.low";
/*      */       case HEDGE_FLOWER1_MEDIUM:
/* 1916 */         return "img.texture.hedge.1.medium";
/*      */       case HEDGE_FLOWER1_HIGH:
/* 1918 */         return "img.texture.hedge.1.high";
/*      */       case HEDGE_FLOWER2_LOW:
/* 1920 */         return "img.texture.hedge.2.low";
/*      */       case HEDGE_FLOWER2_MEDIUM:
/* 1922 */         return "img.texture.hedge.2.medium";
/*      */       case HEDGE_FLOWER2_HIGH:
/* 1924 */         return "img.texture.hedge.2.high";
/*      */       case HEDGE_FLOWER3_LOW:
/* 1926 */         return "img.texture.hedge.3.low";
/*      */       case HEDGE_FLOWER3_MEDIUM:
/* 1928 */         return "img.texture.hedge.3.medium";
/*      */       case HEDGE_FLOWER3_HIGH:
/* 1930 */         return "img.texture.hedge.3.high";
/*      */       case HEDGE_FLOWER4_LOW:
/* 1932 */         return "img.texture.hedge.4.low";
/*      */       case HEDGE_FLOWER4_MEDIUM:
/* 1934 */         return "img.texture.hedge.4.medium";
/*      */       case HEDGE_FLOWER4_HIGH:
/* 1936 */         return "img.texture.hedge.4.high";
/*      */       case HEDGE_FLOWER5_LOW:
/* 1938 */         return "img.texture.hedge.5.low";
/*      */       case HEDGE_FLOWER5_MEDIUM:
/* 1940 */         return "img.texture.hedge.5.medium";
/*      */       case HEDGE_FLOWER5_HIGH:
/* 1942 */         return "img.texture.hedge.5.high";
/*      */       case HEDGE_FLOWER6_LOW:
/* 1944 */         return "img.texture.hedge.6.low";
/*      */       case HEDGE_FLOWER6_MEDIUM:
/* 1946 */         return "img.texture.hedge.6.medium";
/*      */       case HEDGE_FLOWER6_HIGH:
/* 1948 */         return "img.texture.hedge.6.high";
/*      */       case HEDGE_FLOWER7_LOW:
/* 1950 */         return "img.texture.hedge.7.low";
/*      */       case HEDGE_FLOWER7_MEDIUM:
/* 1952 */         return "img.texture.hedge.7.medium";
/*      */       case HEDGE_FLOWER7_HIGH:
/* 1954 */         return "img.texture.hedge.7.high";
/*      */       case FENCE_MAGIC_STONE:
/* 1956 */         return "img.texture.fence.magic.stone";
/*      */       case FENCE_MAGIC_FIRE:
/* 1958 */         return "img.texture.fence.magic.fire";
/*      */       case FENCE_MAGIC_ICE:
/* 1960 */         return "img.texture.fence.magic.ice";
/*      */       case FENCE_RUBBLE:
/* 1962 */         return "img.texture.fence.rubble";
/*      */       case FENCE_SIEGEWALL:
/* 1964 */         return "img.texture.fence.plan.curb";
/*      */       case WALL_RUBBLE:
/* 1966 */         return "img.texture.wall.rubble";
/*      */       case FLOWERBED_BLUE:
/* 1968 */         return "img.texture.flowerbed.blue";
/*      */       case FLOWERBED_GREENISH_YELLOW:
/* 1970 */         return "img.texture.flowerbed.greenish";
/*      */       case FLOWERBED_ORANGE_RED:
/* 1972 */         return "img.texture.flowerbed.orange";
/*      */       case FLOWERBED_PURPLE:
/* 1974 */         return "img.texture.flowerbed.purple";
/*      */       case FLOWERBED_WHITE:
/* 1976 */         return "img.texture.flowerbed.white";
/*      */       case FLOWERBED_WHITE_DOTTED:
/* 1978 */         return "img.texture.flowerbed.white.dotted";
/*      */       case FLOWERBED_YELLOW:
/* 1980 */         return "img.texture.flowerbed.yellow";
/*      */ 
/*      */       
/*      */       case WALL_SOLID_WOODEN:
/* 1984 */         return "img.texture.house.wall.solidwood";
/*      */       case WALL_CANOPY_WOODEN:
/* 1986 */         return "img.texture.house.wall.canopywood";
/*      */       case WALL_WINDOW_WOODEN:
/* 1988 */         return "img.texture.house.wall.windowwood";
/*      */       case WALL_WINDOW_WIDE_WOODEN:
/* 1990 */         return "img.texture.house.wall.widewindowwood";
/*      */       case WALL_DOOR_WOODEN:
/* 1992 */         return "img.texture.house.wall.doorwood";
/*      */       case WALL_DOUBLE_DOOR_WOODEN:
/* 1994 */         return "img.texture.house.wall.doubledoorwood";
/*      */       case WALL_PORTCULLIS_WOOD:
/* 1996 */         return "img.texture.house.wall.woodwallPortcullis";
/*      */       case WALL_SOLID_STONE_DECORATED:
/* 1998 */         return "img.texture.house.wall.solidstone";
/*      */       case WALL_ORIEL_STONE_DECORATED:
/* 2000 */         return "img.texture.house.wall.solidstoneoriel1";
/*      */       case WALL_ORIEL_STONE_PLAIN:
/* 2002 */         return "img.texture.house.wall.oriel2stoneplain";
/*      */       case WALL_SOLID_STONE:
/* 2004 */         return "img.texture.house.wall.solidstoneplain";
/*      */       case WALL_BARRED_STONE:
/* 2006 */         return "img.texture.house.wall.bars1stoneplain";
/*      */       case WALL_WINDOW_STONE:
/* 2008 */         return "img.texture.house.wall.windowstoneplain";
/*      */       case WALL_PLAIN_NARROW_WINDOW:
/* 2010 */         return "img.texture.house.wall.windownarrowstoneplain";
/*      */       case WALL_WINDOW_STONE_DECORATED:
/* 2012 */         return "img.texture.house.wall.windowstone";
/*      */       case WALL_DOOR_STONE:
/* 2014 */         return "img.texture.house.wall.doorstoneplain";
/*      */       case WALL_DOOR_STONE_DECORATED:
/* 2016 */         return "img.texture.house.wall.doorstone";
/*      */       case WALL_DOUBLE_DOOR_STONE_DECORATED:
/* 2018 */         return "img.texture.house.wall.doubledoorstone";
/*      */       case WALL_DOUBLE_DOOR_STONE:
/* 2020 */         return "img.texture.house.wall.doubledoorstoneplain";
/*      */       case WALL_PORTCULLIS_STONE:
/* 2022 */         return "img.texture.house.wall.portcullisstoneplain";
/*      */       case WALL_PORTCULLIS_STONE_DECORATED:
/* 2024 */         return "img.texture.house.wall.solidstonePortcullis";
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
/*      */ 
/*      */       
/*      */       case WALL_DOUBLE_DOOR_STONE_PLAN:
/* 2412 */         return "img.texture.house.wall";case WALL_DOOR_ARCHED_PLAN: return "img.texture.house.wall.plan.archedstone";case WALL_DOOR_ARCHED_STONE_DECORATED: return "img.texture.house.wall.archedstone";case WALL_DOOR_ARCHED_STONE: return "img.texture.house.wall.archedstoneplain";case WALL_LEFT_ARCH_STONE: return "img.texture.house.wall.archleftstoneplain";case WALL_RIGHT_ARCH_STONE: return "img.texture.house.wall.archrightstoneplain";case WALL_T_ARCH_STONE: return "img.texture.house.wall.archtstoneplain";case WALL_LEFT_ARCH_STONE_DECORATED: return "img.texture.house.wall.archleftstone";case WALL_RIGHT_ARCH_STONE_DECORATED: return "img.texture.house.wall.archrightstone";case WALL_T_ARCH_STONE_DECORATED: return "img.texture.house.wall.archtstone";case WALL_DOUBLE_DOOR_WOODEN_PLAN: return "img.texture.house.wall";case WALL_DOOR_ARCHED_WOODEN_PLAN: return "img.texture.house.wall.plan.archedwooden";case WALL_DOOR_ARCHED_WOODEN: return "img.texture.house.wall.archedwooden";case WALL_LEFT_ARCH_WOODEN: return "img.texture.house.wall.archleftwooden";case WALL_RIGHT_ARCH_WOODEN: return "img.texture.house.wall.archrightwooden";case WALL_T_ARCH_WOODEN: return "img.texture.house.wall.archtwooden";case WALL_SOLID_TIMBER_FRAMED: return "img.texture.house.wall.solidtimber";case WALL_WINDOW_TIMBER_FRAMED: return "img.texture.house.wall.windowtimber";case WALL_DOOR_TIMBER_FRAMED: return "img.texture.house.wall.doortimber";case WALL_DOUBLE_DOOR_TIMBER_FRAMED: return "img.texture.house.wall.doubledoortimber";case WALL_DOOR_ARCHED_TIMBER_FRAMED: return "img.texture.house.wall.archedtimber";case WALL_BALCONY_TIMBER_FRAMED: return "img.texture.house.wall.balconytimber";case WALL_JETTY_TIMBER_FRAMED: return "img.texture.house.wall.jettytimber";case WALL_LEFT_ARCH_TIMBER_FRAMED: return "img.texture.house.wall.archlefttimber";case WALL_RIGHT_ARCH_TIMBER_FRAMED: return "img.texture.house.wall.archrighttimber";case WALL_T_ARCH_TIMBER_FRAMED: return "img.texture.house.wall.archttimber";case WALL_SOLID_WOODEN_PLAN: return "img.texture.house.wall.outlinewood";case WALL_WINDOW_WOODEN_PLAN: return "img.texture.house.wall.outlinewood";case WALL_DOOR_WOODEN_PLAN: return "img.texture.house.wall.outlinewood";case WALL_CANOPY_WOODEN_PLAN: return "img.texture.house.wall.outlinewood";case WALL_SOLID_STONE_PLAN: return "img.texture.house.wall.outlinestone";case WALL_WINDOW_STONE_PLAN: return "img.texture.house.wall.outlinestone";case WALL_DOOR_STONE_PLAN: return "img.texture.house.wall.outlinestone";case WALL_ORIEL_STONE_DECORATED_PLAN: return "img.texture.house.wall.outlinestone";case WALL_SOLID_TIMBER_FRAMED_PLAN: case WALL_BALCONY_TIMBER_FRAMED_PLAN: case WALL_JETTY_TIMBER_FRAMED_PLAN: case WALL_WINDOW_TIMBER_FRAMED_PLAN: case WALL_DOOR_TIMBER_FRAMED_PLAN: case WALL_DOUBLE_DOOR_TIMBER_FRAMED_PLAN: case WALL_DOOR_ARCHED_TIMBER_FRAMED_PLAN: return "img.texture.house.wall.outlinewood";case WALL_SOLID_SLATE: return "img.texture.house.wall.solidslate";case WALL_WINDOW_SLATE: return "img.texture.house.wall.windowslate";case WALL_NARROW_WINDOW_SLATE: return "img.texture.house.wall.windownarrowslate";case WALL_DOOR_SLATE: return "img.texture.house.wall.doorslate";case WALL_DOUBLE_DOOR_SLATE: return "img.texture.house.wall.doubledoorslate";case WALL_ARCHED_SLATE: return "img.texture.house.wall.archedslate";case WALL_PORTCULLIS_SLATE: return "img.texture.house.wall.portcullisslate";case WALL_BARRED_SLATE: return "img.texture.house.wall.bars1slate";case WALL_ORIEL_SLATE: return "img.texture.house.wall.oriel2slate";case WALL_LEFT_ARCH_SLATE: return "img.texture.house.wall.archleftslate";case WALL_RIGHT_ARCH_SLATE: return "img.texture.house.wall.archrightslate";case WALL_T_ARCH_SLATE: return "img.texture.house.wall.archtslate";case WALL_SOLID_ROUNDED_STONE: return "img.texture.house.wall.solidroundedstone";case WALL_WINDOW_ROUNDED_STONE: return "img.texture.house.wall.windowroundedstone";case WALL_NARROW_WINDOW_ROUNDED_STONE: return "img.texture.house.wall.windownarrowroundedstone";case WALL_DOOR_ROUNDED_STONE: return "img.texture.house.wall.doorroundedstone";case WALL_DOUBLE_DOOR_ROUNDED_STONE: return "img.texture.house.wall.doubledoorroundedstone";case WALL_ARCHED_ROUNDED_STONE: return "img.texture.house.wall.archedroundedstone";case WALL_PORTCULLIS_ROUNDED_STONE: return "img.texture.house.wall.portcullisroundedstone";case WALL_BARRED_ROUNDED_STONE: return "img.texture.house.wall.bars1roundedstone";case WALL_ORIEL_ROUNDED_STONE: return "img.texture.house.wall.oriel2roundedstone";case WALL_LEFT_ARCH_ROUNDED_STONE: return "img.texture.house.wall.archleftroundedstone";case WALL_RIGHT_ARCH_ROUNDED_STONE: return "img.texture.house.wall.archrightroundedstone";case WALL_T_ARCH_ROUNDED_STONE: return "img.texture.house.wall.archtroundedstone";case WALL_SOLID_POTTERY: return "img.texture.house.wall.solidpottery";case WALL_WINDOW_POTTERY: return "img.texture.house.wall.windowpottery";case WALL_NARROW_WINDOW_POTTERY: return "img.texture.house.wall.windownarrowpottery";case WALL_DOOR_POTTERY: return "img.texture.house.wall.doorpottery";case WALL_DOUBLE_DOOR_POTTERY: return "img.texture.house.wall.doubledoorpottery";case WALL_ARCHED_POTTERY: return "img.texture.house.wall.archedpottery";case WALL_PORTCULLIS_POTTERY: return "img.texture.house.wall.portcullispottery";case WALL_BARRED_POTTERY: return "img.texture.house.wall.bars1pottery";case WALL_ORIEL_POTTERY: return "img.texture.house.wall.oriel2pottery";case WALL_LEFT_ARCH_POTTERY: return "img.texture.house.wall.archleftpottery";case WALL_RIGHT_ARCH_POTTERY: return "img.texture.house.wall.archrightpottery";case WALL_T_ARCH_POTTERY: return "img.texture.house.wall.archtpottery";case WALL_SOLID_SANDSTONE: return "img.texture.house.wall.solidsandstone";case WALL_WINDOW_SANDSTONE: return "img.texture.house.wall.windowsandstone";case WALL_NARROW_WINDOW_SANDSTONE: return "img.texture.house.wall.windownarrowsandstone";case WALL_DOOR_SANDSTONE: return "img.texture.house.wall.doorsandstone";case WALL_DOUBLE_DOOR_SANDSTONE: return "img.texture.house.wall.doubledoorsandstone";case WALL_ARCHED_SANDSTONE: return "img.texture.house.wall.archedsandstone";case WALL_PORTCULLIS_SANDSTONE: return "img.texture.house.wall.portcullissandstone";case WALL_BARRED_SANDSTONE: return "img.texture.house.wall.bars1sandstone";case WALL_ORIEL_SANDSTONE: return "img.texture.house.wall.oriel2sandstone";case WALL_LEFT_ARCH_SANDSTONE: return "img.texture.house.wall.archleftsandstone";case WALL_RIGHT_ARCH_SANDSTONE: return "img.texture.house.wall.archrightsandstone";case WALL_T_ARCH_SANDSTONE: return "img.texture.house.wall.archtsandstone";case WALL_SOLID_RENDERED: return "img.texture.house.wall.solidrendered";case WALL_WINDOW_RENDERED: return "img.texture.house.wall.windowrendered";case WALL_NARROW_WINDOW_RENDERED: return "img.texture.house.wall.windownarrowrendered";case WALL_DOOR_RENDERED: return "img.texture.house.wall.doorrendered";case WALL_DOUBLE_DOOR_RENDERED: return "img.texture.house.wall.doubledoorrendered";case WALL_ARCHED_RENDERED: return "img.texture.house.wall.archedrendered";case WALL_PORTCULLIS_RENDERED: return "img.texture.house.wall.portcullisrendered";case WALL_BARRED_RENDERED: return "img.texture.house.wall.bars1rendered";case WALL_ORIEL_RENDERED: return "img.texture.house.wall.oriel2rendered";case WALL_LEFT_ARCH_RENDERED: return "img.texture.house.wall.archleftrendered";case WALL_RIGHT_ARCH_RENDERED: return "img.texture.house.wall.archrightrendered";case WALL_T_ARCH_RENDERED: return "img.texture.house.wall.archtrendered";case WALL_SOLID_MARBLE: return "img.texture.house.wall.solidmarble";case WALL_WINDOW_MARBLE: return "img.texture.house.wall.windowmarble";case WALL_NARROW_WINDOW_MARBLE: return "img.texture.house.wall.windownarrowmarble";case WALL_DOOR_MARBLE: return "img.texture.house.wall.doormarble";case WALL_DOUBLE_DOOR_MARBLE: return "img.texture.house.wall.doubledoormarble";case WALL_ARCHED_MARBLE: return "img.texture.house.wall.archedmarble";case WALL_PORTCULLIS_MARBLE: return "img.texture.house.wall.portcullismarble";case WALL_BARRED_MARBLE: return "img.texture.house.wall.bars1marble";case WALL_ORIEL_MARBLE: return "img.texture.house.wall.oriel2marble";case WALL_LEFT_ARCH_MARBLE: return "img.texture.house.wall.archleftmarble";case WALL_RIGHT_ARCH_MARBLE: return "img.texture.house.wall.archrightmarble";case WALL_T_ARCH_MARBLE: return "img.texture.house.wall.archtmarble";case NO_WALL: return "img.texture.house.wall.outline";case FENCE_MEDIUM_CHAIN: return "img.texture.fence.chain";case FENCE_PLAN_MEDIUM_CHAIN: return "img.texture.fence.plan.chain";case FENCE_PORTCULLIS: return "img.texture.fence.stonewallPortcullis";case FENCE_PLAN_PORTCULLIS: return "img.texture.fence.plan.stonewallPortcullis";case WALL_SCAFFOLDING: return "img.texture.house.wall";case FENCE_SLATE_HIGH_IRON_FENCE: return "img.texture.fence.gate.iron.high.slate";case FENCE_SLATE_HIGH_IRON_FENCE_GATE: return "img.texture.fence.iron.high.slate";case FENCE_ROUNDED_STONE_TALL_STONE_WALL: return "img.texture.fence.stonewall.tall.roundedstone";case FENCE_ROUNDED_STONE_PORTCULLIS: return "img.texture.fence.stonewallPortcullis.roundedstone";case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE: return "img.texture.fence.iron.high.roundedstone";case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE: return "img.texture.fence.gate.iron.high.roundedstone";case FENCE_ROUNDED_STONE_STONE_PARAPET: return "img.texture.fence.parapet.stone.roundedstone";case FENCE_ROUNDED_STONE_CHAIN_FENCE: return "img.texture.fence.chain.roundedstone";case FENCE_SANDSTONE_TALL_STONE_WALL: return "img.texture.fence.stonewall.tall.sandstone";case FENCE_SANDSTONE_HIGH_IRON_FENCE: return "img.texture.fence.iron.high.sandstone";case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE: return "img.texture.fence.gate.iron.high.sandstone";case FENCE_SANDSTONE_STONE_PARAPET: return "img.texture.fence.parapet.stone.sandstone";case FENCE_SANDSTONE_CHAIN_FENCE: return "img.texture.fence.plan.chain.sandstone";case FENCE_RENDERED_TALL_STONE_WALL: return "img.texture.fence.stonewall.tall.rendered";case FENCE_RENDERED_HIGH_IRON_FENCE: return "img.texture.fence.iron.high.rendered";case FENCE_RENDERED_HIGH_IRON_FENCE_GATE: return "img.texture.fence.gate.iron.high.rendered";case FENCE_RENDERED_STONE_PARAPET: return "img.texture.fence.parapet.stone.rendered";case FENCE_POTTERY_TALL_STONE_WALL: return "img.texture.fence.stonewall.tall.pottery";case FENCE_POTTERY_HIGH_IRON_FENCE: return "img.texture.fence.iron.high.pottery";case FENCE_POTTERY_HIGH_IRON_FENCE_GATE: return "img.texture.fence.gate.iron.high.pottery";case FENCE_POTTERY_STONE_PARAPET: return "img.texture.fence.parapet.stone.pottery";case FENCE_MARBLE_TALL_STONE_WALL: return "img.texture.fence.stonewall.tall.marble ";case FENCE_MARBLE_HIGH_IRON_FENCE: return "img.texture.fence.iron.high.marble";case FENCE_MARBLE_HIGH_IRON_FENCE_GATE: return "img.texture.fence.gate.iron.high.marble";case FENCE_SLATE_PORTCULLIS: return "img.texture.fence.stonewallPortcullis.slate";case FENCE_MARBLE_PORTCULLIS: return "img.texture.fence.stonewallPortcullis.marble";case FENCE_SLATE_CHAIN_FENCE: return "img.texture.fence.chain.slate";case FENCE_MARBLE_CHAIN_FENCE: return "img.texture.fence.chain.marble";case WALL_PLAIN_NARROW_WINDOW_PLAN: return "img.texture.wall.house.plan";case FENCE_PLAN_SLATE_TALL_STONE_WALL: return "img.texture.fence.plan.stonewall.tall.slate";case FENCE_PLAN_SLATE_PORTCULLIS: return "img.texture.fence.plan.stonewallPortcullis.slate";case FENCE_PLAN_SLATE_HIGH_IRON_FENCE: return "img.texture.fence.plan.iron.high.slate";case FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE: return "img.texture.fence.plan.gate.iron.high.slate";case FENCE_PLAN_SLATE_STONE_PARAPET: return "img.texture.fence.plan.parapet.stone.slate";case FENCE_PLAN_SLATE_CHAIN_FENCE: return "img.texture.fence.plan.chain.slate";case FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL: return "img.texture.fence.stonewall.tall.roundedstone";case FENCE_PLAN_ROUNDED_STONE_PORTCULLIS: return "img.texture.fence.plan.stonewallPortcullis.roundedstone";case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE: return "img.texture.fence.plan.iron.high.roundedstone";case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE: return "img.texture.fence.plan.gate.iron.high.roundedstone";case FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET: return "img.texture.fence.plan.parapet.stone.roundedstone";case FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE: return "img.texture.fence.parapet.stone.roundedstone";case FENCE_PLAN_SANDSTONE_TALL_STONE_WALL: return "img.texture.fence.plan.stonewall.tall.sandstone";case FENCE_PLAN_SANDSTONE_PORTCULLIS: return "img.texture.fence.plan.stonewallPortcullis.sandstone";case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE: return "img.texture.fence.plan.iron.high.sandstone";case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE: return "img.texture.fence.plan.gate.iron.high.sandstone";case FENCE_PLAN_SANDSTONE_STONE_PARAPET: return "img.texture.fence.plan.parapet.stone.sandstone";case FENCE_PLAN_SANDSTONE_CHAIN_FENCE: return "img.texture.fence.parapet.stone.sandstone";case FENCE_PLAN_POTTERY_TALL_STONE_WALL: return "img.texture.fence.stonewall.tall.pottery";case FENCE_PLAN_POTTERY_PORTCULLIS: return "img.texture.fence.plan.stonewallPortcullis.pottery";case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE: return "img.texture.fence.plan.iron.high.pottery";case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE: return "img.texture.fence.plan.gate.iron.high.pottery";case FENCE_PLAN_POTTERY_STONE_PARAPET: return "img.texture.fence.plan.parapet.stone.pottery";case FENCE_PLAN_POTTERY_CHAIN_FENCE: return "img.texture.fence.plan.chain.pottery";case FENCE_PLAN_MARBLE_TALL_STONE_WALL: return "img.texture.fence.plan.stonewall.tall.marble";case FENCE_PLAN_MARBLE_PORTCULLIS: return "img.texture.fence.plan.stonewallPortcullis.marble";case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE: return "img.texture.fence.plan.iron.high.marble";case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE: return "img.texture.fence.plan.gate.iron.high.marble";case FENCE_PLAN_MARBLE_STONE_PARAPET: return "img.texture.fence.plan.parapet.stone.marble";case FENCE_PLAN_MARBLE_CHAIN_FENCE: return "img.texture.fence.plan.chain.marble";case FENCE_POTTERY_PORTCULLIS: return "img.texture.fence.stonewallPortcullis.pottery";
/*      */       case FENCE_POTTERY_CHAIN_FENCE: return "img.texture.fence.chain.pottery";
/*      */       case FENCE_RENDERED_PORTCULLIS: return "img.texture.fence.stonewallPortcullis.rendered";
/*      */       case FENCE_SLATE_STONE_PARAPET: return "img.texture.fence.parapet.stone.slate";
/*      */       case FENCE_MARBLE_STONE_PARAPET: return "img.texture.fence.parapet.stone.marble";
/*      */       case FENCE_RENDERED_CHAIN_FENCE: return "img.texture.fence.parapet.stone.rendered";
/*      */       case FENCE_SANDSTONE_PORTCULLIS: return "img.texture.fence.stonewallPortcullis.sandstone";
/* 2419 */       case FENCE_SLATE_TALL_STONE_WALL: return "img.texture.fence.stonewall.tall.slate"; }  return "img.texture.house.wall"; } public static final String getName(StructureConstantsEnum type) { switch (type)
/*      */     
/*      */     { case FENCE_WOODEN:
/* 2422 */         return "Wooden fence";
/*      */       case FENCE_WOODEN_CRUDE:
/* 2424 */         return "Crude wooden fence";
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/* 2426 */         return "Crude wooden fence gate";
/*      */       case FENCE_PALISADE:
/* 2428 */         return "Palisade";
/*      */       case FENCE_STONEWALL:
/* 2430 */         return "Stone wall";
/*      */       case FENCE_WOODEN_GATE:
/* 2432 */         return "Wooden fence gate";
/*      */       case FENCE_PALISADE_GATE:
/* 2434 */         return "Palisade gate";
/*      */       case FENCE_STONEWALL_HIGH:
/* 2436 */         return "Tall stone wall";
/*      */       case FENCE_IRON:
/* 2438 */         return "Iron fence";
/*      */       case FENCE_IRON_GATE:
/* 2440 */         return "Iron fence gate";
/*      */       case FENCE_WOVEN:
/* 2442 */         return "Woven fence";
/*      */       case FENCE_PLAN_WOODEN_PARAPET:
/* 2444 */         return "Incomplete wooden parapet";
/*      */       case FENCE_WOODEN_PARAPET:
/* 2446 */         return "Wooden parapet";
/*      */       case FENCE_PLAN_STONE_PARAPET:
/* 2448 */         return "Incomplete stone parapet";
/*      */       case FENCE_PLAN_STONE_IRON_PARAPET:
/* 2450 */         return "Incomplete stone and iron parapet";
/*      */       case FENCE_STONE_IRON_PARAPET:
/* 2452 */         return "Stone and iron parapet";
/*      */       case FENCE_STONE_PARAPET:
/* 2454 */         return "Stone parapet";
/*      */       case FENCE_PLAN_WOODEN:
/* 2456 */         return "Incomplete wooden fence";
/*      */       case FENCE_PLAN_WOODEN_CRUDE:
/* 2458 */         return "Incomplete crude wooden fence";
/*      */       case FENCE_PLAN_WOODEN_GATE_CRUDE:
/* 2460 */         return "Incomplete crude wooden fence gate";
/*      */       case FENCE_PLAN_PALISADE:
/* 2462 */         return "Incomplete palisade";
/*      */       case FENCE_PLAN_STONEWALL:
/* 2464 */         return "Incomplete stone wall";
/*      */       case FENCE_PLAN_PALISADE_GATE:
/* 2466 */         return "Incomplete palisade gate";
/*      */       case FENCE_PLAN_WOODEN_GATE:
/* 2468 */         return "Incomplete wooden fence gate";
/*      */       case FENCE_PLAN_STONEWALL_HIGH:
/* 2470 */         return "Incomplete tall stone wall";
/*      */       case FENCE_PLAN_IRON:
/* 2472 */         return "Incomplete iron fence";
/*      */       case FENCE_PLAN_SLATE_IRON:
/* 2474 */         return "Incomplete slate iron fence";
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON:
/* 2476 */         return "Incomplete rounded stone iron fence";
/*      */       case FENCE_PLAN_POTTERY_IRON:
/* 2478 */         return "Incomplete pottery iron fence";
/*      */       case FENCE_PLAN_SANDSTONE_IRON:
/* 2480 */         return "Incomplete sandstone iron fence";
/*      */       case FENCE_PLAN_MARBLE_IRON:
/* 2482 */         return "Incomplete marble iron fence";
/*      */       case FENCE_PLAN_IRON_GATE:
/* 2484 */         return "Incomplete iron fence gate";
/*      */       case FENCE_PLAN_SLATE_IRON_GATE:
/* 2486 */         return "Incomplete slate iron fence gate";
/*      */       case FENCE_PLAN_ROUNDED_STONE_IRON_GATE:
/* 2488 */         return "Incomplete rounded stone iron fence gate";
/*      */       case FENCE_PLAN_POTTERY_IRON_GATE:
/* 2490 */         return "Incomplete pottery iron fence gate";
/*      */       case FENCE_PLAN_SANDSTONE_IRON_GATE:
/* 2492 */         return "Incomplete sandstone iron fence gate";
/*      */       case FENCE_PLAN_MARBLE_IRON_GATE:
/* 2494 */         return "Incomplete marble iron fence gate";
/*      */       case FENCE_PLAN_WOVEN:
/* 2496 */         return "Incomplete woven fence";
/*      */       case FENCE_PLAN_STONE:
/* 2498 */         return "Incomplete stone fence";
/*      */       case FENCE_PLAN_SLATE:
/* 2500 */         return "Incomplete slate fence";
/*      */       case FENCE_PLAN_ROUNDED_STONE:
/* 2502 */         return "Incomplete rounded stone fence";
/*      */       case FENCE_PLAN_POTTERY:
/* 2504 */         return "Incomplete pottery fence";
/*      */       case FENCE_PLAN_SANDSTONE:
/* 2506 */         return "Incomplete sandstone fence";
/*      */       case FENCE_PLAN_MARBLE:
/* 2508 */         return "Incomplete marble fence";
/*      */       case FENCE_STONE:
/* 2510 */         return "Stone fence";
/*      */       case FENCE_PLAN_MEDIUM_CHAIN:
/* 2512 */         return "Incomplete chain fence";
/*      */       case FENCE_MEDIUM_CHAIN:
/* 2514 */         return "Chain fence";
/*      */       case FENCE_SLATE:
/* 2516 */         return "Slate fence";
/*      */       case FENCE_SLATE_IRON:
/* 2518 */         return "Slate iron fence";
/*      */       case FENCE_SLATE_IRON_GATE:
/* 2520 */         return "Slate iron fence gate";
/*      */       case FENCE_ROUNDED_STONE:
/* 2522 */         return "Rounded stone fence";
/*      */       case FENCE_ROUNDED_STONE_IRON:
/* 2524 */         return "Rounded stone iron fence";
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/* 2526 */         return "Rounded stone iron fence gate";
/*      */       case FENCE_POTTERY:
/* 2528 */         return "Pottery fence";
/*      */       case FENCE_POTTERY_IRON:
/* 2530 */         return "Pottery iron fence";
/*      */       case FENCE_POTTERY_IRON_GATE:
/* 2532 */         return "Pottery iron fence gate";
/*      */       case FENCE_SANDSTONE:
/* 2534 */         return "Sandstone fence";
/*      */       case FENCE_SANDSTONE_IRON:
/* 2536 */         return "Sandstone iron fence";
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/* 2538 */         return "Sandstone iron fence gate";
/*      */       case FENCE_RENDERED:
/* 2540 */         return "Rendered fence";
/*      */       case FENCE_RENDERED_IRON:
/* 2542 */         return "Rendered iron fence";
/*      */       case FENCE_RENDERED_IRON_GATE:
/* 2544 */         return "Rendered iron fence gate";
/*      */       case FENCE_MARBLE:
/* 2546 */         return "Marble fence";
/*      */       case FENCE_MARBLE_IRON:
/* 2548 */         return "Marble iron fence";
/*      */       case FENCE_MARBLE_IRON_GATE:
/* 2550 */         return "Marble fence gate";
/*      */       
/*      */       case FENCE_ROPE_LOW:
/* 2553 */         return "Low rope fence";
/*      */       case FENCE_GARDESGARD_LOW:
/* 2555 */         return "Low roundpole fence";
/*      */       case FENCE_GARDESGARD_HIGH:
/* 2557 */         return "High roundpole fence";
/*      */       case FENCE_GARDESGARD_GATE:
/* 2559 */         return "Roundpole fence gate";
/*      */       case FENCE_CURB:
/* 2561 */         return "Curb";
/*      */       case FENCE_ROPE_HIGH:
/* 2563 */         return "High rope fence";
/*      */       case FENCE_PLAN_ROPE_LOW:
/* 2565 */         return "Incomplete low rope fence";
/*      */       case FENCE_PLAN_GARDESGARD_LOW:
/* 2567 */         return "Incomplete low roundpole fence";
/*      */       case FENCE_PLAN_GARDESGARD_HIGH:
/* 2569 */         return "Incomplete high roundpole fence";
/*      */       case FENCE_PLAN_GARDESGARD_GATE:
/* 2571 */         return "Incomplete roundpole gate";
/*      */       case FENCE_PLAN_CURB:
/* 2573 */         return "Incomplete curb";
/*      */       case FENCE_PLAN_ROPE_HIGH:
/* 2575 */         return "Incomplete high rope fence";
/*      */       case FENCE_IRON_HIGH:
/* 2577 */         return "High iron fence";
/*      */       case FENCE_PLAN_IRON_HIGH:
/* 2579 */         return "Incomplete high iron fence";
/*      */       case FENCE_IRON_GATE_HIGH:
/* 2581 */         return "High iron fence gate";
/*      */       case FENCE_PLAN_IRON_GATE_HIGH:
/* 2583 */         return "Incomplete high iron fence gate";
/*      */       
/*      */       case HEDGE_FLOWER1_LOW:
/* 2586 */         return "Lavender plantation";
/*      */       case HEDGE_FLOWER1_MEDIUM:
/* 2588 */         return "Lavender plantation";
/*      */       case HEDGE_FLOWER1_HIGH:
/* 2590 */         return "Lavender plantation";
/*      */       case HEDGE_FLOWER2_LOW:
/* 2592 */         return "Oleander hedge";
/*      */       case HEDGE_FLOWER2_MEDIUM:
/* 2594 */         return "Oleander hedge";
/*      */       case HEDGE_FLOWER2_HIGH:
/* 2596 */         return "Oleander hedge";
/*      */       case HEDGE_FLOWER3_LOW:
/* 2598 */         return "Camellia hedge";
/*      */       case HEDGE_FLOWER3_MEDIUM:
/* 2600 */         return "Camellia hedge";
/*      */       case HEDGE_FLOWER3_HIGH:
/* 2602 */         return "Camellia hedge";
/*      */       case HEDGE_FLOWER4_LOW:
/* 2604 */         return "Rose hedge";
/*      */       case HEDGE_FLOWER4_MEDIUM:
/* 2606 */         return "Rose hedge";
/*      */       case HEDGE_FLOWER4_HIGH:
/* 2608 */         return "Rose hedge";
/*      */       case HEDGE_FLOWER5_LOW:
/* 2610 */         return "Thorn hedge";
/*      */       case HEDGE_FLOWER5_MEDIUM:
/* 2612 */         return "Thorn hedge";
/*      */       case HEDGE_FLOWER5_HIGH:
/* 2614 */         return "Thorn hedge";
/*      */       case HEDGE_FLOWER6_LOW:
/* 2616 */         return "Cedar hedge";
/*      */       case HEDGE_FLOWER6_MEDIUM:
/* 2618 */         return "Cedar hedge";
/*      */       case HEDGE_FLOWER6_HIGH:
/* 2620 */         return "Cedar hedge";
/*      */       case HEDGE_FLOWER7_LOW:
/* 2622 */         return "Maple hedge";
/*      */       case HEDGE_FLOWER7_MEDIUM:
/* 2624 */         return "Maple hedge";
/*      */       case HEDGE_FLOWER7_HIGH:
/* 2626 */         return "Maple hedge";
/*      */       case FENCE_MAGIC_STONE:
/* 2628 */         return "Magic stone wall";
/*      */       
/*      */       case FENCE_MAGIC_FIRE:
/* 2631 */         return "Magic wall of fire";
/*      */       case FENCE_MAGIC_ICE:
/* 2633 */         return "Magic wall of ice";
/*      */       case FLOWERBED_BLUE:
/* 2635 */         return "Blue flowerbed";
/*      */       case FLOWERBED_GREENISH_YELLOW:
/* 2637 */         return "Greenish-yellow flowerbed";
/*      */       case FLOWERBED_ORANGE_RED:
/* 2639 */         return "Orange-red flowerbed";
/*      */       case FLOWERBED_PURPLE:
/* 2641 */         return "Purple flowerbed";
/*      */       case FLOWERBED_WHITE:
/* 2643 */         return "White flowerbed";
/*      */       case FLOWERBED_WHITE_DOTTED:
/* 2645 */         return "White-dotted flowerbed";
/*      */       case FENCE_PLAN_SLATE_TALL_STONE_WALL:
/* 2647 */         return "Incomplete tall slate wall";
/*      */       case FENCE_PLAN_SLATE_PORTCULLIS:
/* 2649 */         return "Incomplete slate portcullis";
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE:
/* 2651 */         return "Incomplete slate high iron fence";
/*      */       case FENCE_PLAN_SLATE_HIGH_IRON_FENCE_GATE:
/* 2653 */         return "Incomplete slate high iron fence gate";
/*      */       case FENCE_PLAN_SLATE_STONE_PARAPET:
/* 2655 */         return "Incomplete slate parapet";
/*      */       case FENCE_PLAN_SLATE_CHAIN_FENCE:
/* 2657 */         return "Incomplete slate chain fence";
/*      */       case FENCE_PLAN_ROUNDED_STONE_TALL_STONE_WALL:
/* 2659 */         return "Incomplete tall rounded stone wall";
/*      */       case FENCE_PLAN_ROUNDED_STONE_PORTCULLIS:
/* 2661 */         return "Incomplete rounded stone portcullis";
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE:
/* 2663 */         return "Incomplete rounded stone high iron fence";
/*      */       case FENCE_PLAN_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/* 2665 */         return "Incomplete rounded stone high iron fence gate";
/*      */       case FENCE_PLAN_ROUNDED_STONE_STONE_PARAPET:
/* 2667 */         return "Incomplete rounded stone parapet";
/*      */       case FENCE_PLAN_ROUNDED_STONE_CHAIN_FENCE:
/* 2669 */         return "Incomplete rounded stone chain fence";
/*      */       case FENCE_PLAN_SANDSTONE_TALL_STONE_WALL:
/* 2671 */         return "Incomplete tall sandstone wall";
/*      */       case FENCE_PLAN_SANDSTONE_PORTCULLIS:
/* 2673 */         return "Incomplete sandstone portcullis";
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE:
/* 2675 */         return "Incomplete sandstone high iron fence";
/*      */       case FENCE_PLAN_SANDSTONE_HIGH_IRON_FENCE_GATE:
/* 2677 */         return "Incomplete sandstone high iron fence gate";
/*      */       case FENCE_PLAN_SANDSTONE_STONE_PARAPET:
/* 2679 */         return "Incomplete sandstone parapet";
/*      */       case FENCE_PLAN_SANDSTONE_CHAIN_FENCE:
/* 2681 */         return "Incomplete sandstone chain fence";
/*      */       case FENCE_PLAN_RENDERED_TALL_STONE_WALL:
/* 2683 */         return "Incomplete tall rendered stone wall";
/*      */       case FENCE_PLAN_RENDERED_PORTCULLIS:
/* 2685 */         return "Incomplete rendered portcullis";
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE:
/* 2687 */         return "Incomplete rendered hign iron fence";
/*      */       case FENCE_PLAN_RENDERED_HIGH_IRON_FENCE_GATE:
/* 2689 */         return "Incomplete rendered high iron fence gate";
/*      */       case FENCE_PLAN_RENDERED_STONE_PARAPET:
/* 2691 */         return "Incomplete rendered parapet";
/*      */       case FENCE_PLAN_RENDERED_CHAIN_FENCE:
/* 2693 */         return "Incomplete rendered chain fence";
/*      */       case FENCE_PLAN_POTTERY_TALL_STONE_WALL:
/* 2695 */         return "Incomplete tall pottery wall";
/*      */       case FENCE_PLAN_POTTERY_PORTCULLIS:
/* 2697 */         return "Incomplete pottery portcullis";
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE:
/* 2699 */         return "Incomplete pottery high iron fence";
/*      */       case FENCE_PLAN_POTTERY_HIGH_IRON_FENCE_GATE:
/* 2701 */         return "Incomplete pottery high iron fence gate";
/*      */       case FENCE_PLAN_POTTERY_STONE_PARAPET:
/* 2703 */         return "Incomplete pottery parapet";
/*      */       case FENCE_PLAN_POTTERY_CHAIN_FENCE:
/* 2705 */         return "incomplete pottery chain fence";
/*      */       case FENCE_PLAN_MARBLE_TALL_STONE_WALL:
/* 2707 */         return "Incomplete tall marble wall";
/*      */       case FENCE_PLAN_MARBLE_PORTCULLIS:
/* 2709 */         return "Incomplete marble portcullis";
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE:
/* 2711 */         return "Incomplete marble high iron fence";
/*      */       case FENCE_PLAN_MARBLE_HIGH_IRON_FENCE_GATE:
/* 2713 */         return "Incomplete marble high iron fence gate";
/*      */       case FENCE_PLAN_MARBLE_STONE_PARAPET:
/* 2715 */         return "Incomplete marble parapet";
/*      */       case FENCE_PLAN_MARBLE_CHAIN_FENCE:
/* 2717 */         return "incomplete marble chain fence";
/*      */       case FLOWERBED_YELLOW:
/* 2719 */         return "Yellow flowerbed";
/*      */       case FENCE_RUBBLE:
/* 2721 */         return "Debris";
/*      */       case FENCE_SIEGEWALL:
/* 2723 */         return "Siege Wall";
/*      */       case WALL_RUBBLE:
/* 2725 */         return "Debris";
/*      */       case WALL_SOLID_WOODEN:
/* 2727 */         return "Wooden wall";
/*      */       case WALL_WINDOW_WIDE_WOODEN:
/* 2729 */         return "Wooden wide window";
/*      */       case WALL_CANOPY_WOODEN:
/* 2731 */         return "Wooden canopy";
/*      */       case WALL_WINDOW_WOODEN:
/* 2733 */         return "Wooden window";
/*      */       case WALL_DOOR_WOODEN:
/* 2735 */         return "Wooden door";
/*      */       case WALL_DOUBLE_DOOR_WOODEN:
/* 2737 */         return "Wooden door";
/*      */       case WALL_PORTCULLIS_WOOD:
/* 2739 */         return "Wooden portcullis";
/*      */       case WALL_ORIEL_STONE_PLAIN:
/* 2741 */         return "Plain stone oriel";
/*      */       case WALL_SOLID_STONE_DECORATED:
/* 2743 */         return "Stone wall";
/*      */       case WALL_ORIEL_STONE_DECORATED:
/* 2745 */         return "Stone oriel";
/*      */       case WALL_SOLID_STONE:
/* 2747 */         return "Plain stone wall";
/*      */       case WALL_BARRED_STONE:
/* 2749 */         return "Plain barred wall";
/*      */       case WALL_WINDOW_STONE:
/* 2751 */         return "Plain stone window";
/*      */       case WALL_PLAIN_NARROW_WINDOW:
/* 2753 */         return "Plain narrow stone window";
/*      */       case WALL_WINDOW_STONE_DECORATED:
/* 2755 */         return "Stone window";
/*      */       case WALL_DOOR_STONE_DECORATED:
/* 2757 */         return "Stone door";
/*      */       case WALL_DOOR_STONE:
/* 2759 */         return "Plain stone door";
/*      */       case WALL_DOUBLE_DOOR_STONE_DECORATED:
/* 2761 */         return "Stone door";
/*      */       case WALL_DOUBLE_DOOR_STONE:
/* 2763 */         return "Plain stone double door";
/*      */       case WALL_PORTCULLIS_STONE:
/* 2765 */         return "Plain stone portcullis";
/*      */       case WALL_PORTCULLIS_STONE_DECORATED:
/* 2767 */         return "Stone portcullis";
/*      */       case WALL_SOLID_WOODEN_PLAN:
/* 2769 */         return "Wooden wall plan";
/*      */       case WALL_CANOPY_WOODEN_PLAN:
/* 2771 */         return "Wooden canopy door plan";
/*      */       case WALL_WINDOW_WOODEN_PLAN:
/* 2773 */         return "Wooden window plan";
/*      */       case WALL_DOOR_WOODEN_PLAN:
/* 2775 */         return "Wooden door plan";
/*      */       case WALL_SOLID_STONE_PLAN:
/* 2777 */         return "Stone wall plan";
/*      */       case WALL_WINDOW_STONE_PLAN:
/* 2779 */         return "Stone window plan";
/*      */       case WALL_DOOR_STONE_PLAN:
/* 2781 */         return "Stone door plan";
/*      */       case WALL_DOOR_ARCHED_PLAN:
/* 2783 */         return "Stone arch plan";
/*      */       case WALL_ORIEL_STONE_DECORATED_PLAN:
/* 2785 */         return "Stone oriel plan";
/*      */       
/*      */       case WALL_DOOR_ARCHED_STONE_DECORATED:
/* 2788 */         return "Stone arch wall";
/*      */       case WALL_LEFT_ARCH_STONE_DECORATED:
/* 2790 */         return "Stone left arch";
/*      */       case WALL_RIGHT_ARCH_STONE_DECORATED:
/* 2792 */         return "Stone right arch";
/*      */       case WALL_T_ARCH_STONE_DECORATED:
/* 2794 */         return "Stone T arch";
/*      */       case WALL_DOOR_ARCHED_STONE:
/* 2796 */         return "Plain stone arch wall";
/*      */       case WALL_LEFT_ARCH_STONE:
/* 2798 */         return "Plain stone left arch";
/*      */       case WALL_RIGHT_ARCH_STONE:
/* 2800 */         return "Plain stone right arch";
/*      */       case WALL_T_ARCH_STONE:
/* 2802 */         return "Plain stone T arch";
/*      */       case WALL_DOOR_ARCHED_WOODEN:
/* 2804 */         return "Wooden arched wall";
/*      */       case WALL_LEFT_ARCH_WOODEN:
/* 2806 */         return "Wooden left arch";
/*      */       case WALL_RIGHT_ARCH_WOODEN:
/* 2808 */         return "Wooden right arch";
/*      */       case WALL_T_ARCH_WOODEN:
/* 2810 */         return "Wooden T arch";
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
/*      */       case WALL_DOUBLE_DOOR_WOODEN_PLAN:
/* 3099 */         return "Unknown wall type " + type;case WALL_DOOR_ARCHED_WOODEN_PLAN: return "Wooden arched wall plan";case WALL_SOLID_TIMBER_FRAMED: return "Timber framed wall";case WALL_WINDOW_TIMBER_FRAMED: return "Timber framed window";case WALL_DOOR_TIMBER_FRAMED: return "Timber framed door";case WALL_DOUBLE_DOOR_TIMBER_FRAMED: return "Timber framed double door";case WALL_DOOR_ARCHED_TIMBER_FRAMED: return "Timber framed arched wall";case WALL_LEFT_ARCH_TIMBER_FRAMED: return "Timber framed left arch";case WALL_RIGHT_ARCH_TIMBER_FRAMED: return "Timber framed right arch";case WALL_T_ARCH_TIMBER_FRAMED: return "Timber framed T arch";case WALL_BALCONY_TIMBER_FRAMED: return "Timber framed balcony";case WALL_JETTY_TIMBER_FRAMED: return "Timber framed jetty";case NO_WALL: return "Missing wall";case WALL_SOLID_TIMBER_FRAMED_PLAN: return "Timber framed wall plan";case WALL_WINDOW_TIMBER_FRAMED_PLAN: return "Timber framed window plan";case WALL_DOOR_TIMBER_FRAMED_PLAN: return "Timber framed door plan";case WALL_DOUBLE_DOOR_TIMBER_FRAMED_PLAN: return "Timber framed double door plan";case WALL_DOOR_ARCHED_TIMBER_FRAMED_PLAN: return "Timber framed arch plan";case WALL_BALCONY_TIMBER_FRAMED_PLAN: return "Timber framed balcony plan";case WALL_JETTY_TIMBER_FRAMED_PLAN: return "Timber framed jetty plan";case WALL_SOLID_SLATE: return "Slate wall";case WALL_WINDOW_SLATE: return "Slate window";case WALL_NARROW_WINDOW_SLATE: return "Slate narrow window";case WALL_DOOR_SLATE: return "Slate door";case WALL_DOUBLE_DOOR_SLATE: return "Slate double door";case WALL_ARCHED_SLATE: return "Slate arched wall";case WALL_PORTCULLIS_SLATE: return "Slate portcullis";case WALL_BARRED_SLATE: return "Slate barred wall";case WALL_ORIEL_SLATE: return "Slate oriel window";case WALL_LEFT_ARCH_SLATE: return "Slate left arch";case WALL_RIGHT_ARCH_SLATE: return "Slate right arch";case WALL_T_ARCH_SLATE: return "Slate T arch";case WALL_SOLID_ROUNDED_STONE: return "Rounded stone wall";case WALL_WINDOW_ROUNDED_STONE: return "Rounded stone window";case WALL_NARROW_WINDOW_ROUNDED_STONE: return "Rounded stone narrow window";case WALL_DOOR_ROUNDED_STONE: return "Rounded stone door";case WALL_DOUBLE_DOOR_ROUNDED_STONE: return "Rounded stone double door";case WALL_ARCHED_ROUNDED_STONE: return "Rounded stone arched wall";case WALL_PORTCULLIS_ROUNDED_STONE: return "Rounded stone portcullis";case WALL_BARRED_ROUNDED_STONE: return "Rounded stone barred wall";case WALL_ORIEL_ROUNDED_STONE: return "Rounded stone oriel window";case WALL_LEFT_ARCH_ROUNDED_STONE: return "Rounded stone left arch";case WALL_RIGHT_ARCH_ROUNDED_STONE: return "Rounded stone right arch";case WALL_T_ARCH_ROUNDED_STONE: return "Rounded stone T arch";case WALL_SOLID_POTTERY: return "Pottery wall";case WALL_WINDOW_POTTERY: return "Pottery window";case WALL_NARROW_WINDOW_POTTERY: return "Pottery narrow window";case WALL_DOOR_POTTERY: return "Pottery door";case WALL_DOUBLE_DOOR_POTTERY: return "Pottery double door";case WALL_ARCHED_POTTERY: return "Pottery arched wall";case WALL_PORTCULLIS_POTTERY: return "Pottery portcullis";case WALL_BARRED_POTTERY: return "Pottery barred wall";case WALL_ORIEL_POTTERY: return "Pottery oriel window";case WALL_LEFT_ARCH_POTTERY: return "Pottery left arch";case WALL_RIGHT_ARCH_POTTERY: return "Pottery right arch";case WALL_T_ARCH_POTTERY: return "Pottery T arch";case WALL_SOLID_SANDSTONE: return "Sandstone wall";case WALL_WINDOW_SANDSTONE: return "Sandstone window";case WALL_NARROW_WINDOW_SANDSTONE: return "Sandstone narrow window";case WALL_DOOR_SANDSTONE: return "Sandstone door";case WALL_DOUBLE_DOOR_SANDSTONE: return "Sandstone double door";case WALL_ARCHED_SANDSTONE: return "Sandstone arched wall";case WALL_PORTCULLIS_SANDSTONE: return "Sandstone portcullis";case WALL_BARRED_SANDSTONE: return "Sandstone barred wall";case WALL_ORIEL_SANDSTONE: return "Sandstone oriel window";case WALL_LEFT_ARCH_SANDSTONE: return "Sandstone left arch";case WALL_RIGHT_ARCH_SANDSTONE: return "Sandstone right arch";case WALL_T_ARCH_SANDSTONE: return "Sandstone T arch";case WALL_SOLID_RENDERED: return "Rendered wall";case WALL_WINDOW_RENDERED: return "Rendered window";case WALL_NARROW_WINDOW_RENDERED: return "Rendered narrow window";case WALL_DOOR_RENDERED: return "Rendered door";case WALL_DOUBLE_DOOR_RENDERED: return "Rendered double door";case WALL_ARCHED_RENDERED: return "Rendered arched wall";case WALL_PORTCULLIS_RENDERED: return "Rendered portcullis";case WALL_BARRED_RENDERED: return "Rendered barred wall";case WALL_ORIEL_RENDERED: return "Rendered oriel window";case WALL_LEFT_ARCH_RENDERED: return "Rendered left arch";case WALL_RIGHT_ARCH_RENDERED: return "Rendered right arch";case WALL_T_ARCH_RENDERED: return "Rendered T arch";case WALL_SOLID_MARBLE: return "Marble wall";case WALL_WINDOW_MARBLE: return "Marble window";case WALL_NARROW_WINDOW_MARBLE: return "Marble narrow window";case WALL_DOOR_MARBLE: return "Marble door";case WALL_DOUBLE_DOOR_MARBLE: return "Marble double door";case WALL_ARCHED_MARBLE: return "Marble arched wall";case WALL_PORTCULLIS_MARBLE: return "Marble portcullis";case WALL_BARRED_MARBLE: return "Marble barred wall";case WALL_ORIEL_MARBLE: return "Marble oriel window";case WALL_LEFT_ARCH_MARBLE: return "Marble left arch";case WALL_RIGHT_ARCH_MARBLE: return "Marble right arch";case WALL_T_ARCH_MARBLE: return "Marble T arch";case FENCE_PLAN_PORTCULLIS: return "Incomplete portcullis";case FENCE_PORTCULLIS: return "Portcullis";case WALL_DOUBLE_DOOR_STONE_PLAN: return "Incomplete Plain stone double door";case WALL_PLAIN_NARROW_WINDOW_PLAN: return "Incomplete plain narrow window";case WALL_SCAFFOLDING: return "Wooden scaffolding";case FENCE_PLAN_RENDERED: return "Incomplete rendered fence";case FENCE_PLAN_RENDERED_IRON: return "Incomplete rencered iron fence";case FENCE_PLAN_RENDERED_IRON_GATE: return "Incomplete rencered iron gate";case FENCE_SLATE_TALL_STONE_WALL: return "Tall slate stone wall";case FENCE_SLATE_PORTCULLIS: return "Slate portcullis";case FENCE_SLATE_HIGH_IRON_FENCE: return "Slate high iron fence";case FENCE_SLATE_HIGH_IRON_FENCE_GATE: return "Slate high iron fence gate";case FENCE_SLATE_STONE_PARAPET: return "Slate stone parapet";case FENCE_SLATE_CHAIN_FENCE: return "Slate chain fence";case FENCE_ROUNDED_STONE_TALL_STONE_WALL: return "Tall rounded stone wall";case FENCE_ROUNDED_STONE_PORTCULLIS: return "Rounded stone portcullis";case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE: return "Rounded stone high iron fence";case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE: return "Rounded stone high iron fence gate";case FENCE_ROUNDED_STONE_STONE_PARAPET: return "Rounded stone parapet";case FENCE_ROUNDED_STONE_CHAIN_FENCE: return "Rounded stone chain fence";case FENCE_SANDSTONE_TALL_STONE_WALL: return "Tall sandstone wall";case FENCE_SANDSTONE_PORTCULLIS: return "Sandstone portcullis";case FENCE_SANDSTONE_HIGH_IRON_FENCE: return "Sandstone high iron fence";case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE: return "Sandstone high iron fence gate";case FENCE_SANDSTONE_STONE_PARAPET: return "Sandstone parapet";case FENCE_SANDSTONE_CHAIN_FENCE: return "Sandstone chain fence";case FENCE_RENDERED_TALL_STONE_WALL: return "Tall rendered stone wall";case FENCE_RENDERED_PORTCULLIS: return "Rendered portcullis";case FENCE_RENDERED_HIGH_IRON_FENCE: return "Rendered high iron fence";case FENCE_RENDERED_HIGH_IRON_FENCE_GATE: return "Rendered high iron fence gate";case FENCE_RENDERED_STONE_PARAPET: return "Rendered stone parapet";case FENCE_RENDERED_CHAIN_FENCE: return "Rendered chain fence";case FENCE_POTTERY_TALL_STONE_WALL: return "Tall pottery wall";case FENCE_POTTERY_PORTCULLIS: return "Pottery portcullis";case FENCE_POTTERY_HIGH_IRON_FENCE: return "Pottery high iron fence";case FENCE_POTTERY_HIGH_IRON_FENCE_GATE: return "Pottery high iron fence gate";case FENCE_POTTERY_STONE_PARAPET: return "Pottery parapet";case FENCE_POTTERY_CHAIN_FENCE: return "Pottery chain fence";case FENCE_MARBLE_TALL_STONE_WALL: return "Tall marble wall";
/*      */       case FENCE_MARBLE_PORTCULLIS: return "Marble portcullis";
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE: return "Marble high iron fence";
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE: return "Marble high iron fence gate";
/*      */       case FENCE_MARBLE_STONE_PARAPET: return "Marble parapet";
/* 3104 */       case FENCE_MARBLE_CHAIN_FENCE: return "Marble chain fence"; }  return "Unknown wall type " + type; } public static final int getIconId(StructureConstantsEnum type) { return getIconId(type, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int getIconId(StructureConstantsEnum type, boolean initializing) {
/* 3111 */     if (!initializing)
/*      */     {
/* 3113 */       return type.getIconId();
/*      */     }
/*      */     
/* 3116 */     switch (type) {
/*      */       
/*      */       case FENCE_WOODEN:
/* 3119 */         return 60;
/*      */       case FENCE_WOODEN_CRUDE:
/* 3121 */         return 60;
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/* 3123 */         return 60;
/*      */       case FENCE_PALISADE:
/* 3125 */         return 60;
/*      */       case FENCE_STONEWALL:
/* 3127 */         return 60;
/*      */       case FENCE_WOODEN_GATE:
/* 3129 */         return 60;
/*      */       case FENCE_PALISADE_GATE:
/* 3131 */         return 60;
/*      */       case FENCE_STONEWALL_HIGH:
/* 3133 */         return 60;
/*      */       case FENCE_IRON:
/* 3135 */         return 60;
/*      */       case FENCE_IRON_GATE:
/* 3137 */         return 60;
/*      */       case FENCE_WOVEN:
/* 3139 */         return 60;
/*      */       case FENCE_PLAN_WOODEN:
/* 3141 */         return 60;
/*      */       case FENCE_PLAN_WOODEN_CRUDE:
/* 3143 */         return 60;
/*      */       case FENCE_PLAN_WOODEN_GATE_CRUDE:
/* 3145 */         return 60;
/*      */       case FENCE_PLAN_PALISADE:
/* 3147 */         return 60;
/*      */       case FENCE_PLAN_STONEWALL:
/* 3149 */         return 60;
/*      */       case FENCE_PLAN_PALISADE_GATE:
/* 3151 */         return 60;
/*      */       case FENCE_PLAN_WOODEN_GATE:
/* 3153 */         return 60;
/*      */       case FENCE_PLAN_STONEWALL_HIGH:
/* 3155 */         return 60;
/*      */       case FENCE_PLAN_IRON:
/* 3157 */         return 60;
/*      */       case FENCE_PLAN_IRON_GATE:
/* 3159 */         return 60;
/*      */       case FENCE_PLAN_WOVEN:
/* 3161 */         return 60;
/*      */       case FENCE_PLAN_STONE:
/* 3163 */         return 60;
/*      */       case FENCE_STONE:
/* 3165 */         return 60;
/*      */       case FENCE_PLAN_MEDIUM_CHAIN:
/* 3167 */         return 60;
/*      */       case FENCE_PLAN_PORTCULLIS:
/* 3169 */         return 60;
/*      */       case FENCE_PORTCULLIS:
/* 3171 */         return 60;
/*      */       case FENCE_MEDIUM_CHAIN:
/* 3173 */         return 60;
/*      */       
/*      */       case FENCE_ROPE_LOW:
/* 3176 */         return 60;
/*      */       case FENCE_ROPE_HIGH:
/* 3178 */         return 60;
/*      */       case FENCE_GARDESGARD_LOW:
/* 3180 */         return 60;
/*      */       case FENCE_GARDESGARD_HIGH:
/* 3182 */         return 60;
/*      */       case FENCE_GARDESGARD_GATE:
/* 3184 */         return 60;
/*      */       case FENCE_CURB:
/* 3186 */         return 60;
/*      */       
/*      */       case FENCE_PLAN_ROPE_LOW:
/* 3189 */         return 60;
/*      */       case FENCE_PLAN_ROPE_HIGH:
/* 3191 */         return 60;
/*      */       case FENCE_PLAN_GARDESGARD_LOW:
/* 3193 */         return 60;
/*      */       case FENCE_PLAN_GARDESGARD_HIGH:
/* 3195 */         return 60;
/*      */       case FENCE_PLAN_GARDESGARD_GATE:
/* 3197 */         return 60;
/*      */       case FENCE_PLAN_CURB:
/* 3199 */         return 60;
/*      */       
/*      */       case FENCE_IRON_HIGH:
/* 3202 */         return 60;
/*      */       case FENCE_PLAN_IRON_HIGH:
/* 3204 */         return 60;
/*      */       case FENCE_IRON_GATE_HIGH:
/* 3206 */         return 60;
/*      */       case FENCE_PLAN_IRON_GATE_HIGH:
/* 3208 */         return 60;
/*      */       
/*      */       case FENCE_PLAN_WOODEN_PARAPET:
/* 3211 */         return 60;
/*      */       case FENCE_WOODEN_PARAPET:
/* 3213 */         return 60;
/*      */       case FENCE_PLAN_STONE_PARAPET:
/* 3215 */         return 60;
/*      */       case FENCE_STONE_PARAPET:
/* 3217 */         return 60;
/*      */       case FENCE_PLAN_STONE_IRON_PARAPET:
/* 3219 */         return 60;
/*      */       case FENCE_STONE_IRON_PARAPET:
/* 3221 */         return 60;
/*      */       
/*      */       case HEDGE_FLOWER1_LOW:
/* 3224 */         return 60;
/*      */       case HEDGE_FLOWER1_MEDIUM:
/* 3226 */         return 60;
/*      */       case HEDGE_FLOWER1_HIGH:
/* 3228 */         return 60;
/*      */       
/*      */       case HEDGE_FLOWER2_LOW:
/* 3231 */         return 60;
/*      */       case HEDGE_FLOWER2_MEDIUM:
/* 3233 */         return 60;
/*      */       case HEDGE_FLOWER2_HIGH:
/* 3235 */         return 60;
/*      */       
/*      */       case HEDGE_FLOWER3_LOW:
/* 3238 */         return 60;
/*      */       case HEDGE_FLOWER3_MEDIUM:
/* 3240 */         return 60;
/*      */       case HEDGE_FLOWER3_HIGH:
/* 3242 */         return 60;
/*      */       
/*      */       case HEDGE_FLOWER4_LOW:
/* 3245 */         return 60;
/*      */       case HEDGE_FLOWER4_MEDIUM:
/* 3247 */         return 60;
/*      */       case HEDGE_FLOWER4_HIGH:
/* 3249 */         return 60;
/*      */       
/*      */       case HEDGE_FLOWER5_LOW:
/* 3252 */         return 60;
/*      */       case HEDGE_FLOWER5_MEDIUM:
/* 3254 */         return 60;
/*      */       case HEDGE_FLOWER5_HIGH:
/* 3256 */         return 60;
/*      */       
/*      */       case HEDGE_FLOWER6_LOW:
/* 3259 */         return 60;
/*      */       case HEDGE_FLOWER6_MEDIUM:
/* 3261 */         return 60;
/*      */       case HEDGE_FLOWER6_HIGH:
/* 3263 */         return 60;
/*      */       
/*      */       case HEDGE_FLOWER7_LOW:
/* 3266 */         return 60;
/*      */       case HEDGE_FLOWER7_MEDIUM:
/* 3268 */         return 60;
/*      */       case HEDGE_FLOWER7_HIGH:
/* 3270 */         return 60;
/*      */       
/*      */       case FENCE_MAGIC_STONE:
/* 3273 */         return 60;
/*      */       case FENCE_MAGIC_FIRE:
/* 3275 */         return 60;
/*      */       case FENCE_MAGIC_ICE:
/* 3277 */         return 60;
/*      */       
/*      */       case FLOWERBED_BLUE:
/* 3280 */         return 60;
/*      */       case FLOWERBED_GREENISH_YELLOW:
/* 3282 */         return 60;
/*      */       case FLOWERBED_ORANGE_RED:
/* 3284 */         return 60;
/*      */       case FLOWERBED_PURPLE:
/* 3286 */         return 60;
/*      */       case FLOWERBED_WHITE:
/* 3288 */         return 60;
/*      */       case FLOWERBED_WHITE_DOTTED:
/* 3290 */         return 60;
/*      */       case FLOWERBED_YELLOW:
/* 3292 */         return 60;
/*      */       
/*      */       case FENCE_RUBBLE:
/* 3295 */         return 60;
/*      */       case FENCE_SIEGEWALL:
/* 3297 */         return 60;
/*      */       case WALL_RUBBLE:
/* 3299 */         return 60;
/*      */       case WALL_SOLID_WOODEN:
/* 3301 */         return 60;
/*      */       case WALL_CANOPY_WOODEN:
/* 3303 */         return 60;
/*      */       case WALL_WINDOW_WIDE_WOODEN:
/* 3305 */         return 60;
/*      */       case WALL_WINDOW_WOODEN:
/* 3307 */         return 60;
/*      */       case WALL_DOOR_WOODEN:
/* 3309 */         return 60;
/*      */       case WALL_DOUBLE_DOOR_WOODEN:
/* 3311 */         return 60;
/*      */       case WALL_DOOR_ARCHED_WOODEN:
/* 3313 */         return 60;
/*      */       case WALL_PORTCULLIS_WOOD:
/* 3315 */         return 60;
/*      */       case WALL_LEFT_ARCH_WOODEN:
/* 3317 */         return 60;
/*      */       case WALL_RIGHT_ARCH_WOODEN:
/* 3319 */         return 60;
/*      */       case WALL_T_ARCH_WOODEN:
/* 3321 */         return 60;
/*      */       
/*      */       case WALL_SOLID_STONE_DECORATED:
/* 3324 */         return 60;
/*      */       case WALL_ORIEL_STONE_DECORATED:
/* 3326 */         return 60;
/*      */       case WALL_ORIEL_STONE_PLAIN:
/* 3328 */         return 60;
/*      */       case WALL_WINDOW_STONE_DECORATED:
/* 3330 */         return 60;
/*      */       case WALL_DOOR_STONE_DECORATED:
/* 3332 */         return 60;
/*      */       case WALL_DOUBLE_DOOR_STONE_DECORATED:
/* 3334 */         return 60;
/*      */       case WALL_DOOR_ARCHED_STONE_DECORATED:
/* 3336 */         return 60;
/*      */       
/*      */       case WALL_SOLID_STONE:
/* 3339 */         return 60;
/*      */       case WALL_WINDOW_STONE:
/* 3341 */         return 60;
/*      */       case WALL_PLAIN_NARROW_WINDOW:
/* 3343 */         return 60;
/*      */       case WALL_DOOR_STONE:
/* 3345 */         return 60;
/*      */       case WALL_DOUBLE_DOOR_STONE:
/* 3347 */         return 60;
/*      */       case WALL_DOOR_ARCHED_STONE:
/* 3349 */         return 60;
/*      */       case WALL_PORTCULLIS_STONE:
/* 3351 */         return 60;
/*      */       case WALL_PORTCULLIS_STONE_DECORATED:
/* 3353 */         return 60;
/*      */       case WALL_BARRED_STONE:
/* 3355 */         return 60;
/*      */       case WALL_LEFT_ARCH_STONE:
/* 3357 */         return 60;
/*      */       case WALL_RIGHT_ARCH_STONE:
/* 3359 */         return 60;
/*      */       case WALL_T_ARCH_STONE:
/* 3361 */         return 60;
/*      */       
/*      */       case WALL_SOLID_SLATE:
/* 3364 */         return 60;
/*      */       case WALL_WINDOW_SLATE:
/* 3366 */         return 60;
/*      */       case WALL_NARROW_WINDOW_SLATE:
/* 3368 */         return 60;
/*      */       case WALL_DOOR_SLATE:
/* 3370 */         return 60;
/*      */       case WALL_DOUBLE_DOOR_SLATE:
/* 3372 */         return 60;
/*      */       case WALL_ARCHED_SLATE:
/* 3374 */         return 60;
/*      */       case WALL_PORTCULLIS_SLATE:
/* 3376 */         return 60;
/*      */       case WALL_ORIEL_SLATE:
/* 3378 */         return 60;
/*      */       case WALL_BARRED_SLATE:
/* 3380 */         return 60;
/*      */       case WALL_LEFT_ARCH_SLATE:
/* 3382 */         return 60;
/*      */       case WALL_RIGHT_ARCH_SLATE:
/* 3384 */         return 60;
/*      */       case WALL_T_ARCH_SLATE:
/* 3386 */         return 60;
/*      */       
/*      */       case WALL_SOLID_ROUNDED_STONE:
/* 3389 */         return 60;
/*      */       case WALL_WINDOW_ROUNDED_STONE:
/* 3391 */         return 60;
/*      */       case WALL_NARROW_WINDOW_ROUNDED_STONE:
/* 3393 */         return 60;
/*      */       case WALL_DOOR_ROUNDED_STONE:
/* 3395 */         return 60;
/*      */       case WALL_DOUBLE_DOOR_ROUNDED_STONE:
/* 3397 */         return 60;
/*      */       case WALL_ARCHED_ROUNDED_STONE:
/* 3399 */         return 60;
/*      */       case WALL_PORTCULLIS_ROUNDED_STONE:
/* 3401 */         return 60;
/*      */       case WALL_ORIEL_ROUNDED_STONE:
/* 3403 */         return 60;
/*      */       case WALL_BARRED_ROUNDED_STONE:
/* 3405 */         return 60;
/*      */       case WALL_LEFT_ARCH_ROUNDED_STONE:
/* 3407 */         return 60;
/*      */       case WALL_RIGHT_ARCH_ROUNDED_STONE:
/* 3409 */         return 60;
/*      */       case WALL_T_ARCH_ROUNDED_STONE:
/* 3411 */         return 60;
/*      */       
/*      */       case WALL_SOLID_POTTERY:
/* 3414 */         return 60;
/*      */       case WALL_WINDOW_POTTERY:
/* 3416 */         return 60;
/*      */       case WALL_NARROW_WINDOW_POTTERY:
/* 3418 */         return 60;
/*      */       case WALL_DOOR_POTTERY:
/* 3420 */         return 60;
/*      */       case WALL_DOUBLE_DOOR_POTTERY:
/* 3422 */         return 60;
/*      */       case WALL_ARCHED_POTTERY:
/* 3424 */         return 60;
/*      */       case WALL_PORTCULLIS_POTTERY:
/* 3426 */         return 60;
/*      */       case WALL_ORIEL_POTTERY:
/* 3428 */         return 60;
/*      */       case WALL_BARRED_POTTERY:
/* 3430 */         return 60;
/*      */       case WALL_LEFT_ARCH_POTTERY:
/* 3432 */         return 60;
/*      */       case WALL_RIGHT_ARCH_POTTERY:
/* 3434 */         return 60;
/*      */       case WALL_T_ARCH_POTTERY:
/* 3436 */         return 60;
/*      */       
/*      */       case WALL_SOLID_SANDSTONE:
/* 3439 */         return 60;
/*      */       case WALL_WINDOW_SANDSTONE:
/* 3441 */         return 60;
/*      */       case WALL_NARROW_WINDOW_SANDSTONE:
/* 3443 */         return 60;
/*      */       case WALL_DOOR_SANDSTONE:
/* 3445 */         return 60;
/*      */       case WALL_DOUBLE_DOOR_SANDSTONE:
/* 3447 */         return 60;
/*      */       case WALL_ARCHED_SANDSTONE:
/* 3449 */         return 60;
/*      */       case WALL_PORTCULLIS_SANDSTONE:
/* 3451 */         return 60;
/*      */       case WALL_ORIEL_SANDSTONE:
/* 3453 */         return 60;
/*      */       case WALL_BARRED_SANDSTONE:
/* 3455 */         return 60;
/*      */       case WALL_LEFT_ARCH_SANDSTONE:
/* 3457 */         return 60;
/*      */       case WALL_RIGHT_ARCH_SANDSTONE:
/* 3459 */         return 60;
/*      */       case WALL_T_ARCH_SANDSTONE:
/* 3461 */         return 60;
/*      */       
/*      */       case WALL_SOLID_RENDERED:
/* 3464 */         return 60;
/*      */       case WALL_WINDOW_RENDERED:
/* 3466 */         return 60;
/*      */       case WALL_NARROW_WINDOW_RENDERED:
/* 3468 */         return 60;
/*      */       case WALL_DOOR_RENDERED:
/* 3470 */         return 60;
/*      */       case WALL_DOUBLE_DOOR_RENDERED:
/* 3472 */         return 60;
/*      */       case WALL_ARCHED_RENDERED:
/* 3474 */         return 60;
/*      */       case WALL_PORTCULLIS_RENDERED:
/* 3476 */         return 60;
/*      */       case WALL_ORIEL_RENDERED:
/* 3478 */         return 60;
/*      */       case WALL_BARRED_RENDERED:
/* 3480 */         return 60;
/*      */       case WALL_LEFT_ARCH_RENDERED:
/* 3482 */         return 60;
/*      */       case WALL_RIGHT_ARCH_RENDERED:
/* 3484 */         return 60;
/*      */       case WALL_T_ARCH_RENDERED:
/* 3486 */         return 60;
/*      */       
/*      */       case WALL_SOLID_MARBLE:
/* 3489 */         return 60;
/*      */       case WALL_WINDOW_MARBLE:
/* 3491 */         return 60;
/*      */       case WALL_NARROW_WINDOW_MARBLE:
/* 3493 */         return 60;
/*      */       case WALL_DOOR_MARBLE:
/* 3495 */         return 60;
/*      */       case WALL_DOUBLE_DOOR_MARBLE:
/* 3497 */         return 60;
/*      */       case WALL_ARCHED_MARBLE:
/* 3499 */         return 60;
/*      */       case WALL_PORTCULLIS_MARBLE:
/* 3501 */         return 60;
/*      */       case WALL_ORIEL_MARBLE:
/* 3503 */         return 60;
/*      */       case WALL_BARRED_MARBLE:
/* 3505 */         return 60;
/*      */       case WALL_LEFT_ARCH_MARBLE:
/* 3507 */         return 60;
/*      */       case WALL_RIGHT_ARCH_MARBLE:
/* 3509 */         return 60;
/*      */       case WALL_T_ARCH_MARBLE:
/* 3511 */         return 60;
/*      */       
/*      */       case WALL_SOLID_WOODEN_PLAN:
/* 3514 */         return 60;
/*      */       case WALL_WINDOW_WOODEN_PLAN:
/* 3516 */         return 60;
/*      */       case WALL_DOOR_WOODEN_PLAN:
/* 3518 */         return 60;
/*      */       case WALL_CANOPY_WOODEN_PLAN:
/* 3520 */         return 60;
/*      */       case WALL_SOLID_STONE_PLAN:
/* 3522 */         return 60;
/*      */       case WALL_WINDOW_STONE_PLAN:
/* 3524 */         return 60;
/*      */       case WALL_DOOR_STONE_PLAN:
/* 3526 */         return 60;
/*      */       case WALL_DOOR_ARCHED_PLAN:
/* 3528 */         return 60;
/*      */       case WALL_DOOR_ARCHED_WOODEN_PLAN:
/* 3530 */         return 60;
/*      */       case WALL_ORIEL_STONE_DECORATED_PLAN:
/* 3532 */         return 60;
/*      */       
/*      */       case WALL_SOLID_TIMBER_FRAMED:
/* 3535 */         return 60;
/*      */       case WALL_WINDOW_TIMBER_FRAMED:
/* 3537 */         return 60;
/*      */       case WALL_DOOR_TIMBER_FRAMED:
/* 3539 */         return 60;
/*      */       case WALL_DOUBLE_DOOR_TIMBER_FRAMED:
/* 3541 */         return 60;
/*      */       case WALL_DOOR_ARCHED_TIMBER_FRAMED:
/* 3543 */         return 60;
/*      */       case WALL_BALCONY_TIMBER_FRAMED:
/* 3545 */         return 60;
/*      */       case WALL_JETTY_TIMBER_FRAMED:
/* 3547 */         return 60;
/*      */       case WALL_LEFT_ARCH_TIMBER_FRAMED:
/* 3549 */         return 60;
/*      */       case WALL_RIGHT_ARCH_TIMBER_FRAMED:
/* 3551 */         return 60;
/*      */       case WALL_T_ARCH_TIMBER_FRAMED:
/* 3553 */         return 60;
/*      */       
/*      */       case NO_WALL:
/* 3556 */         return 60;
/*      */       
/*      */       case WALL_SOLID_TIMBER_FRAMED_PLAN:
/* 3559 */         return 60;
/*      */       case WALL_WINDOW_TIMBER_FRAMED_PLAN:
/* 3561 */         return 60;
/*      */       case WALL_DOOR_TIMBER_FRAMED_PLAN:
/* 3563 */         return 60;
/*      */       case WALL_DOUBLE_DOOR_TIMBER_FRAMED_PLAN:
/* 3565 */         return 60;
/*      */       case WALL_DOOR_ARCHED_TIMBER_FRAMED_PLAN:
/* 3567 */         return 60;
/*      */       case WALL_BALCONY_TIMBER_FRAMED_PLAN:
/* 3569 */         return 60;
/*      */       case WALL_JETTY_TIMBER_FRAMED_PLAN:
/* 3571 */         return 60;
/*      */     } 
/*      */     
/* 3574 */     return 60;
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
/*      */   public static final float getCollisionWidth(StructureConstantsEnum type) {
/* 3590 */     switch (type) {
/*      */       
/*      */       case FENCE_WOODEN:
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/*      */       case FENCE_WOODEN_GATE:
/*      */       case FENCE_IRON:
/*      */       case FENCE_SLATE_IRON:
/*      */       case FENCE_ROUNDED_STONE_IRON:
/*      */       case FENCE_POTTERY_IRON:
/*      */       case FENCE_SANDSTONE_IRON:
/*      */       case FENCE_RENDERED_IRON:
/*      */       case FENCE_MARBLE_IRON:
/*      */       case FENCE_IRON_GATE:
/*      */       case FENCE_SLATE_IRON_GATE:
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_POTTERY_IRON_GATE:
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/*      */       case FENCE_RENDERED_IRON_GATE:
/*      */       case FENCE_MARBLE_IRON_GATE:
/*      */       case FENCE_GARDESGARD_LOW:
/*      */       case FENCE_GARDESGARD_HIGH:
/*      */       case FENCE_GARDESGARD_GATE:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/* 3624 */         return 0.055F;
/*      */       
/*      */       case FENCE_ROPE_HIGH:
/* 3627 */         return 0.005F;
/*      */       
/*      */       case FENCE_PALISADE:
/*      */       case FENCE_PALISADE_GATE:
/*      */       case FENCE_WOVEN:
/* 3632 */         return 0.055F;
/*      */       
/*      */       case FENCE_STONEWALL:
/*      */       case FENCE_IRON_HIGH:
/*      */       case FENCE_IRON_GATE_HIGH:
/*      */       case FENCE_ROPE_LOW:
/*      */       case FENCE_CURB:
/*      */       case FENCE_MEDIUM_CHAIN:
/*      */       case FENCE_SLATE_TALL_STONE_WALL:
/*      */       case FENCE_SLATE_CHAIN_FENCE:
/*      */       case FENCE_ROUNDED_STONE_TALL_STONE_WALL:
/*      */       case FENCE_ROUNDED_STONE_CHAIN_FENCE:
/*      */       case FENCE_SANDSTONE_TALL_STONE_WALL:
/*      */       case FENCE_SANDSTONE_CHAIN_FENCE:
/*      */       case FENCE_RENDERED_TALL_STONE_WALL:
/*      */       case FENCE_RENDERED_CHAIN_FENCE:
/*      */       case FENCE_POTTERY_TALL_STONE_WALL:
/*      */       case FENCE_POTTERY_CHAIN_FENCE:
/*      */       case FENCE_MARBLE_TALL_STONE_WALL:
/*      */       case FENCE_MARBLE_CHAIN_FENCE:
/* 3652 */         return 0.155F;
/*      */       
/*      */       case FENCE_STONEWALL_HIGH:
/*      */       case FENCE_STONE:
/*      */       case FENCE_SLATE:
/*      */       case FENCE_ROUNDED_STONE:
/*      */       case FENCE_POTTERY:
/*      */       case FENCE_SANDSTONE:
/*      */       case FENCE_RENDERED:
/*      */       case FENCE_MARBLE:
/*      */       case HEDGE_FLOWER1_LOW:
/*      */       case HEDGE_FLOWER1_MEDIUM:
/*      */       case HEDGE_FLOWER1_HIGH:
/*      */       case HEDGE_FLOWER2_LOW:
/*      */       case HEDGE_FLOWER2_MEDIUM:
/*      */       case HEDGE_FLOWER2_HIGH:
/*      */       case HEDGE_FLOWER3_LOW:
/*      */       case HEDGE_FLOWER3_MEDIUM:
/*      */       case HEDGE_FLOWER3_HIGH:
/*      */       case HEDGE_FLOWER4_LOW:
/*      */       case HEDGE_FLOWER4_MEDIUM:
/*      */       case HEDGE_FLOWER4_HIGH:
/*      */       case HEDGE_FLOWER5_LOW:
/*      */       case HEDGE_FLOWER5_MEDIUM:
/*      */       case HEDGE_FLOWER5_HIGH:
/*      */       case HEDGE_FLOWER6_LOW:
/*      */       case HEDGE_FLOWER6_MEDIUM:
/*      */       case HEDGE_FLOWER6_HIGH:
/*      */       case HEDGE_FLOWER7_LOW:
/*      */       case HEDGE_FLOWER7_MEDIUM:
/*      */       case HEDGE_FLOWER7_HIGH:
/*      */       case FENCE_MAGIC_STONE:
/*      */       case FENCE_MAGIC_ICE:
/*      */       case FENCE_SIEGEWALL:
/* 3686 */         return 0.105F;
/*      */       
/*      */       case FENCE_WOODEN_PARAPET:
/* 3689 */         return 0.04F;
/*      */       
/*      */       case FENCE_STONE_PARAPET:
/*      */       case FENCE_STONE_IRON_PARAPET:
/*      */       case FENCE_SLATE_STONE_PARAPET:
/*      */       case FENCE_ROUNDED_STONE_STONE_PARAPET:
/*      */       case FENCE_SANDSTONE_STONE_PARAPET:
/*      */       case FENCE_RENDERED_STONE_PARAPET:
/*      */       case FENCE_POTTERY_STONE_PARAPET:
/*      */       case FENCE_MARBLE_STONE_PARAPET:
/* 3699 */         return 0.04F;
/*      */       
/*      */       case WALL_SOLID_WOODEN:
/*      */       case WALL_CANOPY_WOODEN:
/*      */       case WALL_WINDOW_WOODEN:
/*      */       case WALL_WINDOW_WIDE_WOODEN:
/*      */       case WALL_DOOR_WOODEN:
/*      */       case WALL_DOUBLE_DOOR_WOODEN:
/*      */       case WALL_PORTCULLIS_WOOD:
/*      */       case WALL_SOLID_TIMBER_FRAMED:
/*      */       case WALL_WINDOW_TIMBER_FRAMED:
/*      */       case WALL_DOOR_TIMBER_FRAMED:
/*      */       case WALL_DOUBLE_DOOR_TIMBER_FRAMED:
/*      */       case WALL_BALCONY_TIMBER_FRAMED:
/*      */       case WALL_JETTY_TIMBER_FRAMED:
/* 3714 */         return 0.045F;
/*      */       
/*      */       case WALL_SOLID_STONE_DECORATED:
/*      */       case WALL_ORIEL_STONE_DECORATED:
/*      */       case WALL_ORIEL_STONE_PLAIN:
/*      */       case WALL_SOLID_STONE:
/*      */       case WALL_BARRED_STONE:
/*      */       case WALL_WINDOW_STONE:
/*      */       case WALL_PLAIN_NARROW_WINDOW:
/*      */       case WALL_DOOR_STONE:
/*      */       case WALL_WINDOW_STONE_DECORATED:
/*      */       case WALL_DOOR_STONE_DECORATED:
/*      */       case WALL_DOUBLE_DOOR_STONE_DECORATED:
/*      */       case WALL_PORTCULLIS_STONE:
/*      */       case WALL_PORTCULLIS_STONE_DECORATED:
/*      */       case WALL_DOUBLE_DOOR_STONE:
/*      */       case WALL_SOLID_SLATE:
/*      */       case WALL_WINDOW_SLATE:
/*      */       case WALL_NARROW_WINDOW_SLATE:
/*      */       case WALL_DOOR_SLATE:
/*      */       case WALL_DOUBLE_DOOR_SLATE:
/*      */       case WALL_PORTCULLIS_SLATE:
/*      */       case WALL_BARRED_SLATE:
/*      */       case WALL_ORIEL_SLATE:
/*      */       case WALL_SOLID_ROUNDED_STONE:
/*      */       case WALL_WINDOW_ROUNDED_STONE:
/*      */       case WALL_NARROW_WINDOW_ROUNDED_STONE:
/*      */       case WALL_DOOR_ROUNDED_STONE:
/*      */       case WALL_DOUBLE_DOOR_ROUNDED_STONE:
/*      */       case WALL_PORTCULLIS_ROUNDED_STONE:
/*      */       case WALL_BARRED_ROUNDED_STONE:
/*      */       case WALL_ORIEL_ROUNDED_STONE:
/*      */       case WALL_SOLID_POTTERY:
/*      */       case WALL_WINDOW_POTTERY:
/*      */       case WALL_NARROW_WINDOW_POTTERY:
/*      */       case WALL_DOOR_POTTERY:
/*      */       case WALL_DOUBLE_DOOR_POTTERY:
/*      */       case WALL_PORTCULLIS_POTTERY:
/*      */       case WALL_BARRED_POTTERY:
/*      */       case WALL_ORIEL_POTTERY:
/*      */       case WALL_SOLID_SANDSTONE:
/*      */       case WALL_WINDOW_SANDSTONE:
/*      */       case WALL_NARROW_WINDOW_SANDSTONE:
/*      */       case WALL_DOOR_SANDSTONE:
/*      */       case WALL_DOUBLE_DOOR_SANDSTONE:
/*      */       case WALL_PORTCULLIS_SANDSTONE:
/*      */       case WALL_BARRED_SANDSTONE:
/*      */       case WALL_ORIEL_SANDSTONE:
/*      */       case WALL_SOLID_RENDERED:
/*      */       case WALL_WINDOW_RENDERED:
/*      */       case WALL_NARROW_WINDOW_RENDERED:
/*      */       case WALL_DOOR_RENDERED:
/*      */       case WALL_DOUBLE_DOOR_RENDERED:
/*      */       case WALL_PORTCULLIS_RENDERED:
/*      */       case WALL_BARRED_RENDERED:
/*      */       case WALL_ORIEL_RENDERED:
/*      */       case WALL_SOLID_MARBLE:
/*      */       case WALL_WINDOW_MARBLE:
/*      */       case WALL_NARROW_WINDOW_MARBLE:
/*      */       case WALL_DOOR_MARBLE:
/*      */       case WALL_DOUBLE_DOOR_MARBLE:
/*      */       case WALL_PORTCULLIS_MARBLE:
/*      */       case WALL_BARRED_MARBLE:
/*      */       case WALL_ORIEL_MARBLE:
/*      */       case FENCE_PORTCULLIS:
/*      */       case FENCE_SLATE_PORTCULLIS:
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_RENDERED_PORTCULLIS:
/*      */       case FENCE_POTTERY_PORTCULLIS:
/*      */       case FENCE_MARBLE_PORTCULLIS:
/* 3785 */         return 0.06F;
/*      */       
/*      */       case WALL_DOOR_ARCHED_WOODEN:
/*      */       case WALL_LEFT_ARCH_WOODEN:
/*      */       case WALL_RIGHT_ARCH_WOODEN:
/*      */       case WALL_T_ARCH_WOODEN:
/*      */       case WALL_DOOR_ARCHED_WOODEN_PLAN:
/*      */       case WALL_DOOR_ARCHED_STONE_DECORATED:
/*      */       case WALL_LEFT_ARCH_STONE_DECORATED:
/*      */       case WALL_RIGHT_ARCH_STONE_DECORATED:
/*      */       case WALL_T_ARCH_STONE_DECORATED:
/*      */       case WALL_DOOR_ARCHED_STONE:
/*      */       case WALL_LEFT_ARCH_STONE:
/*      */       case WALL_RIGHT_ARCH_STONE:
/*      */       case WALL_T_ARCH_STONE:
/*      */       case WALL_DOOR_ARCHED_TIMBER_FRAMED:
/*      */       case WALL_LEFT_ARCH_TIMBER_FRAMED:
/*      */       case WALL_RIGHT_ARCH_TIMBER_FRAMED:
/*      */       case WALL_T_ARCH_TIMBER_FRAMED:
/*      */       case WALL_LEFT_ARCH_SLATE:
/*      */       case WALL_RIGHT_ARCH_SLATE:
/*      */       case WALL_T_ARCH_SLATE:
/*      */       case WALL_LEFT_ARCH_ROUNDED_STONE:
/*      */       case WALL_RIGHT_ARCH_ROUNDED_STONE:
/*      */       case WALL_T_ARCH_ROUNDED_STONE:
/*      */       case WALL_LEFT_ARCH_POTTERY:
/*      */       case WALL_RIGHT_ARCH_POTTERY:
/*      */       case WALL_T_ARCH_POTTERY:
/*      */       case WALL_LEFT_ARCH_SANDSTONE:
/*      */       case WALL_RIGHT_ARCH_SANDSTONE:
/*      */       case WALL_T_ARCH_SANDSTONE:
/*      */       case WALL_LEFT_ARCH_RENDERED:
/*      */       case WALL_RIGHT_ARCH_RENDERED:
/*      */       case WALL_T_ARCH_RENDERED:
/*      */       case WALL_LEFT_ARCH_MARBLE:
/*      */       case WALL_RIGHT_ARCH_MARBLE:
/*      */       case WALL_T_ARCH_MARBLE:
/* 3822 */         return 0.055F;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3827 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float getCollisionThickness(StructureConstantsEnum type) {
/* 3836 */     switch (type) {
/*      */       
/*      */       case FENCE_WOODEN:
/*      */       case FENCE_WOODEN_CRUDE:
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/*      */       case FENCE_WOODEN_GATE:
/*      */       case FENCE_GARDESGARD_LOW:
/*      */       case FENCE_GARDESGARD_HIGH:
/*      */       case FENCE_MEDIUM_CHAIN:
/*      */       case FENCE_SLATE_CHAIN_FENCE:
/*      */       case FENCE_ROUNDED_STONE_CHAIN_FENCE:
/*      */       case FENCE_SANDSTONE_CHAIN_FENCE:
/*      */       case FENCE_RENDERED_CHAIN_FENCE:
/*      */       case FENCE_POTTERY_CHAIN_FENCE:
/*      */       case FENCE_MARBLE_CHAIN_FENCE:
/* 3851 */         return 0.165F;
/*      */       case FENCE_IRON:
/*      */       case FENCE_SLATE_IRON:
/*      */       case FENCE_ROUNDED_STONE_IRON:
/*      */       case FENCE_POTTERY_IRON:
/*      */       case FENCE_SANDSTONE_IRON:
/*      */       case FENCE_RENDERED_IRON:
/*      */       case FENCE_MARBLE_IRON:
/*      */       case FENCE_IRON_HIGH:
/*      */       case FENCE_IRON_GATE:
/*      */       case FENCE_SLATE_IRON_GATE:
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_POTTERY_IRON_GATE:
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/*      */       case FENCE_RENDERED_IRON_GATE:
/*      */       case FENCE_MARBLE_IRON_GATE:
/*      */       case FENCE_IRON_GATE_HIGH:
/*      */       case FENCE_SIEGEWALL:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/* 3881 */         return 0.33F;
/*      */       case FENCE_PALISADE:
/*      */       case FENCE_PALISADE_GATE:
/*      */       case FENCE_WOVEN:
/* 3885 */         return 0.55F;
/*      */       case FENCE_ROPE_LOW:
/*      */       case FENCE_STONE:
/* 3888 */         return 0.385F;
/*      */       case FENCE_STONEWALL:
/*      */       case FENCE_SLATE:
/*      */       case FENCE_ROUNDED_STONE:
/*      */       case FENCE_POTTERY:
/*      */       case FENCE_SANDSTONE:
/*      */       case FENCE_RENDERED:
/*      */       case FENCE_MARBLE:
/* 3896 */         return 0.45F;
/*      */       case FENCE_STONEWALL_HIGH:
/*      */       case FENCE_SLATE_TALL_STONE_WALL:
/*      */       case FENCE_ROUNDED_STONE_TALL_STONE_WALL:
/*      */       case FENCE_SANDSTONE_TALL_STONE_WALL:
/*      */       case FENCE_RENDERED_TALL_STONE_WALL:
/*      */       case FENCE_POTTERY_TALL_STONE_WALL:
/*      */       case FENCE_MARBLE_TALL_STONE_WALL:
/* 3904 */         return 0.55F;
/*      */       case FENCE_CURB:
/*      */       case HEDGE_FLOWER1_LOW:
/*      */       case HEDGE_FLOWER1_MEDIUM:
/*      */       case HEDGE_FLOWER1_HIGH:
/*      */       case HEDGE_FLOWER2_LOW:
/*      */       case HEDGE_FLOWER2_MEDIUM:
/*      */       case HEDGE_FLOWER2_HIGH:
/*      */       case HEDGE_FLOWER3_LOW:
/*      */       case HEDGE_FLOWER3_MEDIUM:
/*      */       case HEDGE_FLOWER3_HIGH:
/*      */       case HEDGE_FLOWER4_LOW:
/*      */       case HEDGE_FLOWER4_MEDIUM:
/*      */       case HEDGE_FLOWER4_HIGH:
/*      */       case HEDGE_FLOWER5_LOW:
/*      */       case HEDGE_FLOWER5_MEDIUM:
/*      */       case HEDGE_FLOWER5_HIGH:
/*      */       case HEDGE_FLOWER6_LOW:
/*      */       case HEDGE_FLOWER6_MEDIUM:
/*      */       case HEDGE_FLOWER6_HIGH:
/*      */       case HEDGE_FLOWER7_LOW:
/*      */       case HEDGE_FLOWER7_MEDIUM:
/*      */       case HEDGE_FLOWER7_HIGH:
/* 3927 */         return 0.8F;
/*      */       case FENCE_WOODEN_PARAPET:
/*      */       case FENCE_MAGIC_STONE:
/*      */       case FENCE_MAGIC_ICE:
/* 3931 */         return 0.33F;
/*      */       case FENCE_STONE_PARAPET:
/*      */       case FENCE_STONE_IRON_PARAPET:
/*      */       case FENCE_SLATE_STONE_PARAPET:
/*      */       case FENCE_ROUNDED_STONE_STONE_PARAPET:
/*      */       case FENCE_SANDSTONE_STONE_PARAPET:
/*      */       case FENCE_RENDERED_STONE_PARAPET:
/*      */       case FENCE_POTTERY_STONE_PARAPET:
/*      */       case FENCE_MARBLE_STONE_PARAPET:
/* 3940 */         return 0.33F;
/*      */       case WALL_SOLID_WOODEN:
/*      */       case WALL_CANOPY_WOODEN:
/*      */       case WALL_WINDOW_WOODEN:
/*      */       case WALL_DOOR_WOODEN:
/*      */       case WALL_DOUBLE_DOOR_WOODEN:
/*      */       case WALL_PORTCULLIS_WOOD:
/*      */       case WALL_SOLID_TIMBER_FRAMED:
/*      */       case WALL_WINDOW_TIMBER_FRAMED:
/*      */       case WALL_DOOR_TIMBER_FRAMED:
/*      */       case WALL_DOUBLE_DOOR_TIMBER_FRAMED:
/*      */       case WALL_BALCONY_TIMBER_FRAMED:
/*      */       case WALL_JETTY_TIMBER_FRAMED:
/* 3953 */         return 0.275F;
/*      */       case FENCE_ROPE_HIGH:
/* 3955 */         return 0.11F;
/*      */       case WALL_SOLID_STONE_DECORATED:
/*      */       case WALL_ORIEL_STONE_DECORATED:
/*      */       case WALL_ORIEL_STONE_PLAIN:
/*      */       case WALL_SOLID_STONE:
/*      */       case WALL_BARRED_STONE:
/*      */       case WALL_WINDOW_STONE:
/*      */       case WALL_PLAIN_NARROW_WINDOW:
/*      */       case WALL_DOOR_STONE:
/*      */       case WALL_WINDOW_STONE_DECORATED:
/*      */       case WALL_DOOR_STONE_DECORATED:
/*      */       case WALL_DOUBLE_DOOR_STONE_DECORATED:
/*      */       case WALL_PORTCULLIS_STONE:
/*      */       case WALL_PORTCULLIS_STONE_DECORATED:
/*      */       case WALL_DOUBLE_DOOR_STONE:
/*      */       case WALL_SOLID_SLATE:
/*      */       case WALL_WINDOW_SLATE:
/*      */       case WALL_NARROW_WINDOW_SLATE:
/*      */       case WALL_DOOR_SLATE:
/*      */       case WALL_DOUBLE_DOOR_SLATE:
/*      */       case WALL_PORTCULLIS_SLATE:
/*      */       case WALL_BARRED_SLATE:
/*      */       case WALL_SOLID_ROUNDED_STONE:
/*      */       case WALL_WINDOW_ROUNDED_STONE:
/*      */       case WALL_NARROW_WINDOW_ROUNDED_STONE:
/*      */       case WALL_DOOR_ROUNDED_STONE:
/*      */       case WALL_DOUBLE_DOOR_ROUNDED_STONE:
/*      */       case WALL_PORTCULLIS_ROUNDED_STONE:
/*      */       case WALL_BARRED_ROUNDED_STONE:
/*      */       case WALL_SOLID_POTTERY:
/*      */       case WALL_WINDOW_POTTERY:
/*      */       case WALL_NARROW_WINDOW_POTTERY:
/*      */       case WALL_DOOR_POTTERY:
/*      */       case WALL_DOUBLE_DOOR_POTTERY:
/*      */       case WALL_PORTCULLIS_POTTERY:
/*      */       case WALL_BARRED_POTTERY:
/*      */       case WALL_SOLID_SANDSTONE:
/*      */       case WALL_WINDOW_SANDSTONE:
/*      */       case WALL_NARROW_WINDOW_SANDSTONE:
/*      */       case WALL_DOOR_SANDSTONE:
/*      */       case WALL_DOUBLE_DOOR_SANDSTONE:
/*      */       case WALL_PORTCULLIS_SANDSTONE:
/*      */       case WALL_BARRED_SANDSTONE:
/*      */       case WALL_SOLID_RENDERED:
/*      */       case WALL_WINDOW_RENDERED:
/*      */       case WALL_NARROW_WINDOW_RENDERED:
/*      */       case WALL_DOOR_RENDERED:
/*      */       case WALL_DOUBLE_DOOR_RENDERED:
/*      */       case WALL_PORTCULLIS_RENDERED:
/*      */       case WALL_BARRED_RENDERED:
/*      */       case WALL_SOLID_MARBLE:
/*      */       case WALL_WINDOW_MARBLE:
/*      */       case WALL_NARROW_WINDOW_MARBLE:
/*      */       case WALL_DOOR_MARBLE:
/*      */       case WALL_DOUBLE_DOOR_MARBLE:
/*      */       case WALL_PORTCULLIS_MARBLE:
/*      */       case WALL_BARRED_MARBLE:
/*      */       case FENCE_PORTCULLIS:
/*      */       case FENCE_SLATE_PORTCULLIS:
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_RENDERED_PORTCULLIS:
/*      */       case FENCE_POTTERY_PORTCULLIS:
/*      */       case FENCE_MARBLE_PORTCULLIS:
/* 4019 */         return 0.66F;
/*      */       case WALL_DOOR_ARCHED_WOODEN:
/*      */       case WALL_LEFT_ARCH_WOODEN:
/*      */       case WALL_RIGHT_ARCH_WOODEN:
/*      */       case WALL_T_ARCH_WOODEN:
/*      */       case WALL_DOOR_ARCHED_WOODEN_PLAN:
/*      */       case WALL_DOOR_ARCHED_PLAN:
/*      */       case WALL_DOOR_ARCHED_STONE_DECORATED:
/*      */       case WALL_LEFT_ARCH_STONE_DECORATED:
/*      */       case WALL_RIGHT_ARCH_STONE_DECORATED:
/*      */       case WALL_T_ARCH_STONE_DECORATED:
/*      */       case WALL_DOOR_ARCHED_STONE:
/*      */       case WALL_LEFT_ARCH_STONE:
/*      */       case WALL_RIGHT_ARCH_STONE:
/*      */       case WALL_T_ARCH_STONE:
/*      */       case WALL_DOOR_ARCHED_TIMBER_FRAMED:
/*      */       case WALL_LEFT_ARCH_TIMBER_FRAMED:
/*      */       case WALL_RIGHT_ARCH_TIMBER_FRAMED:
/*      */       case WALL_T_ARCH_TIMBER_FRAMED:
/*      */       case WALL_LEFT_ARCH_SLATE:
/*      */       case WALL_RIGHT_ARCH_SLATE:
/*      */       case WALL_T_ARCH_SLATE:
/*      */       case WALL_LEFT_ARCH_ROUNDED_STONE:
/*      */       case WALL_RIGHT_ARCH_ROUNDED_STONE:
/*      */       case WALL_T_ARCH_ROUNDED_STONE:
/*      */       case WALL_LEFT_ARCH_POTTERY:
/*      */       case WALL_RIGHT_ARCH_POTTERY:
/*      */       case WALL_T_ARCH_POTTERY:
/*      */       case WALL_LEFT_ARCH_SANDSTONE:
/*      */       case WALL_RIGHT_ARCH_SANDSTONE:
/*      */       case WALL_T_ARCH_SANDSTONE:
/*      */       case WALL_LEFT_ARCH_RENDERED:
/*      */       case WALL_RIGHT_ARCH_RENDERED:
/*      */       case WALL_T_ARCH_RENDERED:
/*      */       case WALL_LEFT_ARCH_MARBLE:
/*      */       case WALL_RIGHT_ARCH_MARBLE:
/*      */       case WALL_T_ARCH_MARBLE:
/* 4056 */         return 0.44F;
/*      */     } 
/*      */     
/* 4059 */     return 0.0F;
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
/*      */   public static final float getOpening(StructureConstantsEnum type) {
/* 4075 */     switch (type) {
/*      */       
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/*      */       case FENCE_WOODEN_GATE:
/*      */       case FENCE_PALISADE_GATE:
/* 4080 */         return 1.0F;
/*      */       case FENCE_GARDESGARD_GATE:
/* 4082 */         return 0.4F;
/*      */       case WALL_CANOPY_WOODEN:
/*      */       case WALL_DOOR_WOODEN:
/*      */       case WALL_DOOR_STONE:
/*      */       case WALL_DOOR_STONE_DECORATED:
/*      */       case WALL_DOOR_TIMBER_FRAMED:
/*      */       case WALL_DOOR_SLATE:
/*      */       case WALL_DOOR_ROUNDED_STONE:
/*      */       case WALL_DOOR_POTTERY:
/*      */       case WALL_DOOR_SANDSTONE:
/*      */       case WALL_DOOR_RENDERED:
/*      */       case WALL_DOOR_MARBLE:
/* 4094 */         return 0.33F;
/*      */       case FENCE_IRON_GATE:
/*      */       case FENCE_SLATE_IRON_GATE:
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_POTTERY_IRON_GATE:
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/*      */       case FENCE_RENDERED_IRON_GATE:
/*      */       case FENCE_MARBLE_IRON_GATE:
/*      */       case FENCE_IRON_GATE_HIGH:
/*      */       case WALL_DOUBLE_DOOR_WOODEN:
/*      */       case WALL_DOUBLE_DOOR_STONE_DECORATED:
/*      */       case WALL_PORTCULLIS_STONE:
/*      */       case WALL_PORTCULLIS_WOOD:
/*      */       case WALL_PORTCULLIS_STONE_DECORATED:
/*      */       case WALL_DOUBLE_DOOR_STONE:
/*      */       case WALL_DOUBLE_DOOR_TIMBER_FRAMED:
/*      */       case WALL_DOUBLE_DOOR_SLATE:
/*      */       case WALL_PORTCULLIS_SLATE:
/*      */       case WALL_DOUBLE_DOOR_ROUNDED_STONE:
/*      */       case WALL_PORTCULLIS_ROUNDED_STONE:
/*      */       case WALL_DOUBLE_DOOR_POTTERY:
/*      */       case WALL_PORTCULLIS_POTTERY:
/*      */       case WALL_DOUBLE_DOOR_SANDSTONE:
/*      */       case WALL_PORTCULLIS_SANDSTONE:
/*      */       case WALL_DOUBLE_DOOR_RENDERED:
/*      */       case WALL_PORTCULLIS_RENDERED:
/*      */       case WALL_DOUBLE_DOOR_MARBLE:
/*      */       case WALL_PORTCULLIS_MARBLE:
/*      */       case FENCE_PORTCULLIS:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_RENDERED_PORTCULLIS:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_POTTERY_PORTCULLIS:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_MARBLE_PORTCULLIS:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/* 4134 */         return 0.66F;
/*      */       case WALL_DOOR_ARCHED_STONE_DECORATED:
/*      */       case WALL_LEFT_ARCH_STONE_DECORATED:
/*      */       case WALL_RIGHT_ARCH_STONE_DECORATED:
/*      */       case WALL_T_ARCH_STONE_DECORATED:
/*      */       case WALL_DOOR_ARCHED_STONE:
/*      */       case WALL_LEFT_ARCH_STONE:
/*      */       case WALL_RIGHT_ARCH_STONE:
/*      */       case WALL_T_ARCH_STONE:
/*      */       case WALL_ARCHED_SLATE:
/*      */       case WALL_LEFT_ARCH_SLATE:
/*      */       case WALL_RIGHT_ARCH_SLATE:
/*      */       case WALL_T_ARCH_SLATE:
/*      */       case WALL_ARCHED_ROUNDED_STONE:
/*      */       case WALL_LEFT_ARCH_ROUNDED_STONE:
/*      */       case WALL_RIGHT_ARCH_ROUNDED_STONE:
/*      */       case WALL_T_ARCH_ROUNDED_STONE:
/*      */       case WALL_ARCHED_POTTERY:
/*      */       case WALL_LEFT_ARCH_POTTERY:
/*      */       case WALL_RIGHT_ARCH_POTTERY:
/*      */       case WALL_T_ARCH_POTTERY:
/*      */       case WALL_ARCHED_SANDSTONE:
/*      */       case WALL_LEFT_ARCH_SANDSTONE:
/*      */       case WALL_RIGHT_ARCH_SANDSTONE:
/*      */       case WALL_T_ARCH_SANDSTONE:
/*      */       case WALL_ARCHED_RENDERED:
/*      */       case WALL_LEFT_ARCH_RENDERED:
/*      */       case WALL_RIGHT_ARCH_RENDERED:
/*      */       case WALL_T_ARCH_RENDERED:
/*      */       case WALL_ARCHED_MARBLE:
/*      */       case WALL_LEFT_ARCH_MARBLE:
/*      */       case WALL_RIGHT_ARCH_MARBLE:
/*      */       case WALL_T_ARCH_MARBLE:
/* 4167 */         return 0.9F;
/*      */       case WALL_DOOR_ARCHED_WOODEN:
/*      */       case WALL_LEFT_ARCH_WOODEN:
/*      */       case WALL_RIGHT_ARCH_WOODEN:
/*      */       case WALL_T_ARCH_WOODEN:
/*      */       case WALL_DOOR_ARCHED_TIMBER_FRAMED:
/*      */       case WALL_LEFT_ARCH_TIMBER_FRAMED:
/*      */       case WALL_RIGHT_ARCH_TIMBER_FRAMED:
/*      */       case WALL_T_ARCH_TIMBER_FRAMED:
/* 4176 */         return 0.95F;
/*      */     } 
/* 4178 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isGate(StructureConstantsEnum type) {
/* 4186 */     switch (type) {
/*      */       
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/*      */       case FENCE_WOODEN_GATE:
/*      */       case FENCE_PALISADE_GATE:
/*      */       case FENCE_IRON_GATE:
/*      */       case FENCE_SLATE_IRON_GATE:
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_POTTERY_IRON_GATE:
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/*      */       case FENCE_RENDERED_IRON_GATE:
/*      */       case FENCE_MARBLE_IRON_GATE:
/*      */       case FENCE_IRON_GATE_HIGH:
/*      */       case FENCE_GARDESGARD_GATE:
/*      */       case WALL_CANOPY_WOODEN:
/*      */       case WALL_DOOR_WOODEN:
/*      */       case WALL_DOUBLE_DOOR_WOODEN:
/*      */       case WALL_DOOR_STONE:
/*      */       case WALL_DOOR_STONE_DECORATED:
/*      */       case WALL_DOUBLE_DOOR_STONE_DECORATED:
/*      */       case WALL_PORTCULLIS_STONE:
/*      */       case WALL_PORTCULLIS_WOOD:
/*      */       case WALL_PORTCULLIS_STONE_DECORATED:
/*      */       case WALL_DOUBLE_DOOR_STONE:
/*      */       case WALL_DOOR_TIMBER_FRAMED:
/*      */       case WALL_DOUBLE_DOOR_TIMBER_FRAMED:
/*      */       case WALL_DOOR_SLATE:
/*      */       case WALL_DOUBLE_DOOR_SLATE:
/*      */       case WALL_PORTCULLIS_SLATE:
/*      */       case WALL_DOOR_ROUNDED_STONE:
/*      */       case WALL_DOUBLE_DOOR_ROUNDED_STONE:
/*      */       case WALL_PORTCULLIS_ROUNDED_STONE:
/*      */       case WALL_DOOR_POTTERY:
/*      */       case WALL_DOUBLE_DOOR_POTTERY:
/*      */       case WALL_PORTCULLIS_POTTERY:
/*      */       case WALL_DOOR_SANDSTONE:
/*      */       case WALL_DOUBLE_DOOR_SANDSTONE:
/*      */       case WALL_PORTCULLIS_SANDSTONE:
/*      */       case WALL_DOOR_RENDERED:
/*      */       case WALL_DOUBLE_DOOR_RENDERED:
/*      */       case WALL_PORTCULLIS_RENDERED:
/*      */       case WALL_DOOR_MARBLE:
/*      */       case WALL_DOUBLE_DOOR_MARBLE:
/*      */       case WALL_PORTCULLIS_MARBLE:
/*      */       case FENCE_PORTCULLIS:
/*      */       case FENCE_SLATE_PORTCULLIS:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_RENDERED_PORTCULLIS:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_POTTERY_PORTCULLIS:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_MARBLE_PORTCULLIS:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/* 4243 */         return true;
/*      */     } 
/* 4245 */     return false;
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
/*      */   public static final float getCollisionHeight(StructureConstantsEnum type) {
/* 4260 */     switch (type) {
/*      */       
/*      */       case FENCE_PALISADE:
/* 4263 */         return 4.9F;
/*      */       case FENCE_PALISADE_GATE:
/* 4265 */         return 5.0F;
/*      */       case FENCE_IRON_HIGH:
/*      */       case FENCE_IRON_GATE_HIGH:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE:
/*      */       case FENCE_SLATE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE:
/*      */       case FENCE_ROUNDED_STONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE:
/*      */       case FENCE_SANDSTONE_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE:
/*      */       case FENCE_RENDERED_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE:
/*      */       case FENCE_POTTERY_HIGH_IRON_FENCE_GATE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE:
/*      */       case FENCE_MARBLE_HIGH_IRON_FENCE_GATE:
/* 4280 */         return 3.0F;
/*      */       case FENCE_STONEWALL_HIGH:
/*      */       case FENCE_SLATE_TALL_STONE_WALL:
/*      */       case FENCE_ROUNDED_STONE_TALL_STONE_WALL:
/*      */       case FENCE_SANDSTONE_TALL_STONE_WALL:
/*      */       case FENCE_RENDERED_TALL_STONE_WALL:
/*      */       case FENCE_POTTERY_TALL_STONE_WALL:
/*      */       case FENCE_MARBLE_TALL_STONE_WALL:
/* 4288 */         return 3.5F;
/*      */       case FENCE_SIEGEWALL:
/* 4290 */         return 3.0F;
/*      */       
/*      */       case FENCE_STONEWALL:
/* 4293 */         return 1.0F;
/*      */       case FENCE_STONE:
/*      */       case FENCE_SLATE:
/*      */       case FENCE_ROUNDED_STONE:
/*      */       case FENCE_POTTERY:
/*      */       case FENCE_SANDSTONE:
/*      */       case FENCE_RENDERED:
/*      */       case FENCE_MARBLE:
/* 4301 */         return 1.6F;
/*      */       case FENCE_WOODEN_CRUDE:
/*      */       case FENCE_WOODEN_CRUDE_GATE:
/* 4304 */         return 1.1F;
/*      */       case FENCE_WOODEN:
/*      */       case FENCE_WOODEN_GATE:
/* 4307 */         return 1.1F;
/*      */       case FENCE_IRON:
/*      */       case FENCE_SLATE_IRON:
/*      */       case FENCE_ROUNDED_STONE_IRON:
/*      */       case FENCE_POTTERY_IRON:
/*      */       case FENCE_SANDSTONE_IRON:
/*      */       case FENCE_RENDERED_IRON:
/*      */       case FENCE_MARBLE_IRON:
/*      */       case FENCE_IRON_GATE:
/*      */       case FENCE_SLATE_IRON_GATE:
/*      */       case FENCE_ROUNDED_STONE_IRON_GATE:
/*      */       case FENCE_POTTERY_IRON_GATE:
/*      */       case FENCE_SANDSTONE_IRON_GATE:
/*      */       case FENCE_RENDERED_IRON_GATE:
/*      */       case FENCE_MARBLE_IRON_GATE:
/* 4322 */         return 1.5F;
/*      */       case FENCE_WOVEN:
/*      */       case FENCE_ROPE_LOW:
/*      */       case FENCE_CURB:
/*      */       case FENCE_MEDIUM_CHAIN:
/*      */       case FENCE_SLATE_CHAIN_FENCE:
/*      */       case FENCE_ROUNDED_STONE_CHAIN_FENCE:
/*      */       case FENCE_SANDSTONE_CHAIN_FENCE:
/*      */       case FENCE_RENDERED_CHAIN_FENCE:
/*      */       case FENCE_POTTERY_CHAIN_FENCE:
/*      */       case FENCE_MARBLE_CHAIN_FENCE:
/* 4333 */         return 2.0F;
/*      */       
/*      */       case FENCE_ROPE_HIGH:
/* 4336 */         return 1.3F;
/*      */       case FENCE_GARDESGARD_LOW:
/* 4338 */         return 1.0F;
/*      */       case FENCE_GARDESGARD_GATE:
/* 4340 */         return 1.2F;
/*      */       case FENCE_GARDESGARD_HIGH:
/* 4342 */         return 1.5F;
/*      */       case HEDGE_FLOWER1_LOW:
/*      */       case HEDGE_FLOWER1_MEDIUM:
/*      */       case HEDGE_FLOWER1_HIGH:
/*      */       case HEDGE_FLOWER2_LOW:
/*      */       case HEDGE_FLOWER2_MEDIUM:
/*      */       case HEDGE_FLOWER2_HIGH:
/*      */       case HEDGE_FLOWER3_LOW:
/*      */       case HEDGE_FLOWER3_MEDIUM:
/*      */       case HEDGE_FLOWER3_HIGH:
/*      */       case HEDGE_FLOWER4_LOW:
/*      */       case HEDGE_FLOWER4_MEDIUM:
/*      */       case HEDGE_FLOWER4_HIGH:
/*      */       case HEDGE_FLOWER5_LOW:
/*      */       case HEDGE_FLOWER5_MEDIUM:
/*      */       case HEDGE_FLOWER5_HIGH:
/*      */       case HEDGE_FLOWER6_LOW:
/*      */       case HEDGE_FLOWER6_MEDIUM:
/*      */       case HEDGE_FLOWER6_HIGH:
/*      */       case HEDGE_FLOWER7_LOW:
/*      */       case HEDGE_FLOWER7_MEDIUM:
/*      */       case HEDGE_FLOWER7_HIGH:
/*      */       case FENCE_MAGIC_STONE:
/*      */       case FENCE_MAGIC_ICE:
/* 4366 */         return 2.0F;
/*      */       case FENCE_WOODEN_PARAPET:
/* 4368 */         return 2.0F;
/*      */       case FENCE_STONE_PARAPET:
/*      */       case FENCE_STONE_IRON_PARAPET:
/*      */       case FENCE_SLATE_STONE_PARAPET:
/*      */       case FENCE_ROUNDED_STONE_STONE_PARAPET:
/*      */       case FENCE_SANDSTONE_STONE_PARAPET:
/*      */       case FENCE_RENDERED_STONE_PARAPET:
/*      */       case FENCE_POTTERY_STONE_PARAPET:
/*      */       case FENCE_MARBLE_STONE_PARAPET:
/* 4377 */         return 2.0F;
/*      */       case WALL_SOLID_WOODEN:
/*      */       case WALL_CANOPY_WOODEN:
/*      */       case WALL_WINDOW_WOODEN:
/*      */       case WALL_WINDOW_WIDE_WOODEN:
/*      */       case WALL_DOOR_WOODEN:
/*      */       case WALL_DOUBLE_DOOR_WOODEN:
/*      */       case WALL_PORTCULLIS_WOOD:
/* 4385 */         return 3.0F;
/*      */       case WALL_SOLID_STONE_DECORATED:
/*      */       case WALL_ORIEL_STONE_DECORATED:
/*      */       case WALL_ORIEL_STONE_PLAIN:
/*      */       case WALL_SOLID_STONE:
/*      */       case WALL_BARRED_STONE:
/*      */       case WALL_WINDOW_STONE:
/*      */       case WALL_PLAIN_NARROW_WINDOW:
/*      */       case WALL_DOOR_STONE:
/*      */       case WALL_WINDOW_STONE_DECORATED:
/*      */       case WALL_DOOR_STONE_DECORATED:
/*      */       case WALL_DOUBLE_DOOR_STONE_DECORATED:
/*      */       case WALL_PORTCULLIS_STONE:
/*      */       case WALL_PORTCULLIS_STONE_DECORATED:
/*      */       case WALL_DOUBLE_DOOR_STONE:
/*      */       case WALL_SOLID_TIMBER_FRAMED:
/*      */       case WALL_WINDOW_TIMBER_FRAMED:
/*      */       case WALL_DOOR_TIMBER_FRAMED:
/*      */       case WALL_DOUBLE_DOOR_TIMBER_FRAMED:
/*      */       case WALL_BALCONY_TIMBER_FRAMED:
/*      */       case WALL_JETTY_TIMBER_FRAMED:
/*      */       case WALL_SOLID_SLATE:
/*      */       case WALL_WINDOW_SLATE:
/*      */       case WALL_NARROW_WINDOW_SLATE:
/*      */       case WALL_DOOR_SLATE:
/*      */       case WALL_DOUBLE_DOOR_SLATE:
/*      */       case WALL_PORTCULLIS_SLATE:
/*      */       case WALL_BARRED_SLATE:
/*      */       case WALL_ORIEL_SLATE:
/*      */       case WALL_SOLID_ROUNDED_STONE:
/*      */       case WALL_WINDOW_ROUNDED_STONE:
/*      */       case WALL_NARROW_WINDOW_ROUNDED_STONE:
/*      */       case WALL_DOOR_ROUNDED_STONE:
/*      */       case WALL_DOUBLE_DOOR_ROUNDED_STONE:
/*      */       case WALL_PORTCULLIS_ROUNDED_STONE:
/*      */       case WALL_BARRED_ROUNDED_STONE:
/*      */       case WALL_ORIEL_ROUNDED_STONE:
/*      */       case WALL_SOLID_POTTERY:
/*      */       case WALL_WINDOW_POTTERY:
/*      */       case WALL_NARROW_WINDOW_POTTERY:
/*      */       case WALL_DOOR_POTTERY:
/*      */       case WALL_DOUBLE_DOOR_POTTERY:
/*      */       case WALL_PORTCULLIS_POTTERY:
/*      */       case WALL_BARRED_POTTERY:
/*      */       case WALL_ORIEL_POTTERY:
/*      */       case WALL_SOLID_SANDSTONE:
/*      */       case WALL_WINDOW_SANDSTONE:
/*      */       case WALL_NARROW_WINDOW_SANDSTONE:
/*      */       case WALL_DOOR_SANDSTONE:
/*      */       case WALL_DOUBLE_DOOR_SANDSTONE:
/*      */       case WALL_PORTCULLIS_SANDSTONE:
/*      */       case WALL_BARRED_SANDSTONE:
/*      */       case WALL_ORIEL_SANDSTONE:
/*      */       case WALL_SOLID_RENDERED:
/*      */       case WALL_WINDOW_RENDERED:
/*      */       case WALL_NARROW_WINDOW_RENDERED:
/*      */       case WALL_DOOR_RENDERED:
/*      */       case WALL_DOUBLE_DOOR_RENDERED:
/*      */       case WALL_PORTCULLIS_RENDERED:
/*      */       case WALL_BARRED_RENDERED:
/*      */       case WALL_ORIEL_RENDERED:
/*      */       case WALL_SOLID_MARBLE:
/*      */       case WALL_WINDOW_MARBLE:
/*      */       case WALL_NARROW_WINDOW_MARBLE:
/*      */       case WALL_DOOR_MARBLE:
/*      */       case WALL_DOUBLE_DOOR_MARBLE:
/*      */       case WALL_PORTCULLIS_MARBLE:
/*      */       case WALL_BARRED_MARBLE:
/*      */       case WALL_ORIEL_MARBLE:
/*      */       case FENCE_PORTCULLIS:
/*      */       case FENCE_SLATE_PORTCULLIS:
/*      */       case FENCE_ROUNDED_STONE_PORTCULLIS:
/*      */       case FENCE_SANDSTONE_PORTCULLIS:
/*      */       case FENCE_RENDERED_PORTCULLIS:
/*      */       case FENCE_POTTERY_PORTCULLIS:
/*      */       case FENCE_MARBLE_PORTCULLIS:
/* 4461 */         return 3.0F;
/*      */     } 
/*      */     
/* 4464 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean isBlocking(StructureConstantsEnum type) {
/* 4472 */     switch (type) {
/*      */ 
/*      */       
/*      */       case FENCE_PLAN_WOODEN:
/*      */       case FENCE_PLAN_WOODEN_CRUDE:
/*      */       case FENCE_PLAN_WOODEN_GATE_CRUDE:
/*      */       case FENCE_PLAN_PALISADE:
/*      */       case FENCE_PLAN_STONEWALL:
/*      */       case FENCE_PLAN_PALISADE_GATE:
/*      */       case FENCE_PLAN_WOODEN_GATE:
/*      */       case FENCE_PLAN_STONEWALL_HIGH:
/*      */       case FENCE_PLAN_IRON:
/*      */       case FENCE_PLAN_IRON_HIGH:
/*      */       case FENCE_PLAN_IRON_GATE:
/*      */       case FENCE_PLAN_IRON_GATE_HIGH:
/*      */       case FENCE_PLAN_WOVEN:
/*      */       case FENCE_PLAN_STONE_PARAPET:
/*      */       case FENCE_PLAN_WOODEN_PARAPET:
/*      */       case FENCE_ROPE_LOW:
/*      */       case FENCE_CURB:
/*      */       case FENCE_PLAN_STONE:
/*      */       case FENCE_PLAN_ROPE_LOW:
/*      */       case FENCE_PLAN_GARDESGARD_LOW:
/*      */       case FENCE_PLAN_GARDESGARD_HIGH:
/*      */       case FENCE_PLAN_GARDESGARD_GATE:
/*      */       case FENCE_PLAN_CURB:
/*      */       case FENCE_PLAN_ROPE_HIGH:
/*      */       case HEDGE_FLOWER1_LOW:
/*      */       case HEDGE_FLOWER1_MEDIUM:
/*      */       case HEDGE_FLOWER1_HIGH:
/*      */       case HEDGE_FLOWER2_LOW:
/*      */       case HEDGE_FLOWER3_LOW:
/*      */       case HEDGE_FLOWER4_LOW:
/*      */       case HEDGE_FLOWER5_LOW:
/*      */       case HEDGE_FLOWER6_LOW:
/*      */       case HEDGE_FLOWER7_LOW:
/*      */       case FENCE_MAGIC_FIRE:
/*      */       case FENCE_RUBBLE:
/*      */       case FLOWERBED_BLUE:
/*      */       case FLOWERBED_GREENISH_YELLOW:
/*      */       case FLOWERBED_ORANGE_RED:
/*      */       case FLOWERBED_PURPLE:
/*      */       case FLOWERBED_WHITE:
/*      */       case FLOWERBED_WHITE_DOTTED:
/*      */       case FLOWERBED_YELLOW:
/*      */       case WALL_JETTY_TIMBER_FRAMED_PLAN:
/*      */       case FENCE_PLAN_MEDIUM_CHAIN:
/* 4519 */         return false;
/*      */       case WALL_RUBBLE:
/*      */       case WALL_DOUBLE_DOOR_WOODEN_PLAN:
/*      */       case WALL_DOUBLE_DOOR_STONE_PLAN:
/*      */       case WALL_SOLID_WOODEN_PLAN:
/*      */       case WALL_WINDOW_WOODEN_PLAN:
/*      */       case WALL_DOOR_WOODEN_PLAN:
/*      */       case WALL_CANOPY_WOODEN_PLAN:
/*      */       case WALL_SOLID_STONE_PLAN:
/*      */       case WALL_ORIEL_STONE_DECORATED_PLAN:
/*      */       case WALL_WINDOW_STONE_PLAN:
/*      */       case WALL_DOOR_STONE_PLAN:
/*      */       case WALL_SOLID_TIMBER_FRAMED_PLAN:
/*      */       case WALL_BALCONY_TIMBER_FRAMED_PLAN:
/*      */       case WALL_WINDOW_TIMBER_FRAMED_PLAN:
/*      */       case WALL_DOOR_TIMBER_FRAMED_PLAN:
/*      */       case WALL_DOUBLE_DOOR_TIMBER_FRAMED_PLAN:
/*      */       case WALL_DOOR_ARCHED_TIMBER_FRAMED_PLAN:
/* 4537 */         return false;
/*      */       case WALL_DOOR_ARCHED_WOODEN:
/*      */       case WALL_LEFT_ARCH_WOODEN:
/*      */       case WALL_RIGHT_ARCH_WOODEN:
/*      */       case WALL_T_ARCH_WOODEN:
/*      */       case WALL_DOOR_ARCHED_WOODEN_PLAN:
/*      */       case WALL_DOOR_ARCHED_PLAN:
/*      */       case WALL_DOOR_ARCHED_STONE_DECORATED:
/*      */       case WALL_LEFT_ARCH_STONE_DECORATED:
/*      */       case WALL_RIGHT_ARCH_STONE_DECORATED:
/*      */       case WALL_T_ARCH_STONE_DECORATED:
/*      */       case WALL_DOOR_ARCHED_STONE:
/*      */       case WALL_LEFT_ARCH_STONE:
/*      */       case WALL_RIGHT_ARCH_STONE:
/*      */       case WALL_T_ARCH_STONE:
/*      */       case WALL_DOOR_ARCHED_TIMBER_FRAMED:
/*      */       case WALL_LEFT_ARCH_TIMBER_FRAMED:
/*      */       case WALL_RIGHT_ARCH_TIMBER_FRAMED:
/*      */       case WALL_T_ARCH_TIMBER_FRAMED:
/*      */       case WALL_ARCHED_SLATE:
/*      */       case WALL_LEFT_ARCH_SLATE:
/*      */       case WALL_RIGHT_ARCH_SLATE:
/*      */       case WALL_T_ARCH_SLATE:
/*      */       case WALL_ARCHED_ROUNDED_STONE:
/*      */       case WALL_LEFT_ARCH_ROUNDED_STONE:
/*      */       case WALL_RIGHT_ARCH_ROUNDED_STONE:
/*      */       case WALL_T_ARCH_ROUNDED_STONE:
/*      */       case WALL_ARCHED_POTTERY:
/*      */       case WALL_LEFT_ARCH_POTTERY:
/*      */       case WALL_RIGHT_ARCH_POTTERY:
/*      */       case WALL_T_ARCH_POTTERY:
/*      */       case WALL_ARCHED_SANDSTONE:
/*      */       case WALL_LEFT_ARCH_SANDSTONE:
/*      */       case WALL_RIGHT_ARCH_SANDSTONE:
/*      */       case WALL_T_ARCH_SANDSTONE:
/*      */       case WALL_ARCHED_RENDERED:
/*      */       case WALL_LEFT_ARCH_RENDERED:
/*      */       case WALL_RIGHT_ARCH_RENDERED:
/*      */       case WALL_T_ARCH_RENDERED:
/*      */       case WALL_ARCHED_MARBLE:
/*      */       case WALL_LEFT_ARCH_MARBLE:
/*      */       case WALL_RIGHT_ARCH_MARBLE:
/*      */       case WALL_T_ARCH_MARBLE:
/* 4580 */         return false;
/*      */       case FENCE_WOVEN:
/* 4582 */         return false;
/*      */       case NO_WALL:
/*      */       case FENCE_PLAN_PORTCULLIS:
/* 4585 */         return false;
/*      */     } 
/* 4587 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\WallConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
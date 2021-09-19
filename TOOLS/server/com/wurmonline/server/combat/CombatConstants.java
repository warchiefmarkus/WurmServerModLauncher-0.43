/*    */ package com.wurmonline.server.combat;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface CombatConstants
/*    */ {
/*    */   public static final int ROUND_TIME = 10;
/*    */   public static final byte FIGHTNORMAL = 0;
/*    */   public static final byte FIGHTAGG = 1;
/*    */   public static final byte FIGHTDEFEND = 2;
/*    */   public static final byte STANCE_STANDARD = 0;
/*    */   public static final byte STANCE_UPPER_RIGHT = 1;
/*    */   public static final byte STANCE_MID_RIGHT = 2;
/*    */   public static final byte STANCE_LOWER_RIGHT = 3;
/*    */   public static final byte STANCE_LOWER_LEFT = 4;
/*    */   public static final byte STANCE_MID_LEFT = 5;
/*    */   public static final byte STANCE_UPPER_LEFT = 6;
/*    */   public static final byte STANCE_HIGH = 7;
/*    */   public static final byte STANCE_PRONE = 8;
/*    */   public static final byte STANCE_OPEN = 9;
/*    */   public static final byte STANCE_LOW = 10;
/*    */   public static final byte STANCE_DEFEND_LOW = 11;
/*    */   public static final byte STANCE_DEFEND_HIGH = 12;
/*    */   public static final byte STANCE_DEFEND_RIGHT = 13;
/*    */   public static final byte STANCE_DEFEND_LEFT = 14;
/*    */   public static final byte STANCE_IDLE = 15;
/* 53 */   public static final byte[] standardSoftSpots = new byte[] { 6, 1 };
/*    */ 
/*    */   
/* 56 */   public static final byte[] lowCenterSoftSpots = new byte[] { 5, 2 };
/*    */ 
/*    */   
/* 59 */   public static final byte[] midLeftSoftSpots = new byte[] { 1, 4 };
/*    */ 
/*    */   
/* 62 */   public static final byte[] midRightSoftSpots = new byte[] { 6, 3 };
/*    */ 
/*    */   
/* 65 */   public static final byte[] upperCenterSoftSpots = new byte[] { 4, 5 };
/*    */ 
/*    */   
/* 68 */   public static final byte[] upperLeftSoftSpots = new byte[] { 3, 2 };
/*    */ 
/*    */   
/* 71 */   public static final byte[] upperRightSoftSpots = new byte[] { 4 };
/*    */ 
/*    */   
/* 74 */   public static final byte[] lowerRightSoftSpots = new byte[] { 7 };
/*    */ 
/*    */   
/* 77 */   public static final byte[] lowerLeftSoftSpots = new byte[] { 10 };
/*    */ 
/*    */   
/* 80 */   public static final byte[] emptyByteArray = new byte[0];
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\CombatConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
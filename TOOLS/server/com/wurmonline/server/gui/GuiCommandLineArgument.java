/*    */ package com.wurmonline.server.gui;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum GuiCommandLineArgument
/*    */ {
/*  8 */   START("start"),
/*  9 */   QUERY_PORT("queryport"),
/* 10 */   INTERNAL_PORT("internalport"),
/* 11 */   EXTERNAL_PORT("externalport"),
/* 12 */   IP_ADDR("ip"),
/* 13 */   RMI_REG("rmiregport"),
/* 14 */   RMI_PORT("rmiport"),
/* 15 */   SERVER_PASS("serverpassword"),
/* 16 */   PLAYER_NUM("maxplayers"),
/* 17 */   LOGIN_SERVER("loginserver"),
/* 18 */   PVP("pvp"),
/* 19 */   HOME_SERVER("homeserver"),
/* 20 */   HOME_KINGDOM("homekingdom"),
/* 21 */   EPIC_SETTINGS("epicsettings"),
/* 22 */   SERVER_NAME("servername"),
/* 23 */   ADMIN_PWD("adminpwd");
/*    */   
/*    */   private final String argString;
/*    */ 
/*    */   
/*    */   GuiCommandLineArgument(String arg) {
/* 29 */     this.argString = arg;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getArgumentString() {
/* 38 */     return this.argString;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\GuiCommandLineArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
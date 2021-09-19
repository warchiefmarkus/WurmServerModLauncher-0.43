/*     */ package javax.activation;
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
/*     */ public abstract class CommandMap
/*     */ {
/*  53 */   private static CommandMap defaultCommandMap = null;
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
/*     */   public static CommandMap getDefaultCommandMap() {
/*  73 */     if (defaultCommandMap == null) {
/*  74 */       defaultCommandMap = new MailcapCommandMap();
/*     */     }
/*  76 */     return defaultCommandMap;
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
/*     */   public static void setDefaultCommandMap(CommandMap commandMap) {
/*  88 */     SecurityManager security = System.getSecurityManager();
/*  89 */     if (security != null)
/*     */       
/*     */       try {
/*  92 */         security.checkSetFactory();
/*  93 */       } catch (SecurityException ex) {
/*     */ 
/*     */ 
/*     */         
/*  97 */         if (CommandMap.class.getClassLoader() != commandMap.getClass().getClassLoader())
/*     */         {
/*  99 */           throw ex;
/*     */         }
/*     */       }  
/* 102 */     defaultCommandMap = commandMap;
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
/*     */   public CommandInfo[] getPreferredCommands(String mimeType, DataSource ds) {
/* 130 */     return getPreferredCommands(mimeType);
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
/*     */   public CommandInfo[] getAllCommands(String mimeType, DataSource ds) {
/* 158 */     return getAllCommands(mimeType);
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
/*     */   public CommandInfo getCommand(String mimeType, String cmdName, DataSource ds) {
/* 187 */     return getCommand(mimeType, cmdName);
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
/*     */   public DataContentHandler createDataContentHandler(String mimeType, DataSource ds) {
/* 219 */     return createDataContentHandler(mimeType);
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
/*     */   public String[] getMimeTypes() {
/* 231 */     return null;
/*     */   }
/*     */   
/*     */   public abstract CommandInfo[] getPreferredCommands(String paramString);
/*     */   
/*     */   public abstract CommandInfo[] getAllCommands(String paramString);
/*     */   
/*     */   public abstract CommandInfo getCommand(String paramString1, String paramString2);
/*     */   
/*     */   public abstract DataContentHandler createDataContentHandler(String paramString);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\CommandMap.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */
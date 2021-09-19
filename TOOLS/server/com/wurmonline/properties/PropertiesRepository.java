/*    */ package com.wurmonline.properties;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ import java.util.HashMap;
/*    */ import java.util.Properties;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum PropertiesRepository
/*    */ {
/* 17 */   INSTANCE; private static final HashMap<URL, Properties> propertiesHashMap;
/*    */   static {
/* 19 */     logger = Logger.getLogger(PropertiesRepository.class.getName());
/* 20 */     propertiesHashMap = new HashMap<>();
/*    */   }
/*    */   private static final Logger logger;
/*    */   public static PropertiesRepository getInstance() {
/* 24 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   Properties getProperties(URL file) {
/* 29 */     if (propertiesHashMap.containsKey(file))
/* 30 */       return propertiesHashMap.get(file); 
/* 31 */     Properties properties = new Properties();
/* 32 */     propertiesHashMap.put(file, properties);
/* 33 */     try (InputStream is = file.openStream()) {
/*    */       
/* 35 */       properties.load(is);
/*    */     }
/* 37 */     catch (IOException e) {
/*    */       
/* 39 */       logger.warning("Unable to open properties file " + file.toString());
/*    */     } 
/* 41 */     return properties;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValueFor(URL file, String key) {
/* 46 */     return getProperties(file).getProperty(key);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\properties\PropertiesRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
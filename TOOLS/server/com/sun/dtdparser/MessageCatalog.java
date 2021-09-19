/*     */ package com.sun.dtdparser;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
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
/*     */ public abstract class MessageCatalog
/*     */ {
/*     */   private String bundleName;
/*     */   private Hashtable cache;
/*     */   
/*     */   protected MessageCatalog(Class packageMember) {
/* 131 */     this(packageMember, "Messages");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MessageCatalog(Class packageMember, String bundle) {
/* 412 */     this.cache = new Hashtable(5);
/*     */     this.bundleName = packageMember.getName();
/*     */     int index = this.bundleName.lastIndexOf('.');
/*     */     if (index == -1) {
/*     */       this.bundleName = "";
/*     */     } else {
/*     */       this.bundleName = this.bundleName.substring(0, index) + ".";
/*     */     } 
/*     */     this.bundleName += "resources." + bundle;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(Locale locale, String messageId) {
/*     */     ResourceBundle resourceBundle;
/*     */     if (locale == null) {
/*     */       locale = Locale.getDefault();
/*     */     }
/*     */     try {
/*     */       resourceBundle = ResourceBundle.getBundle(this.bundleName, locale);
/*     */     } catch (MissingResourceException e) {
/*     */       resourceBundle = ResourceBundle.getBundle(this.bundleName, Locale.ENGLISH);
/*     */     } 
/*     */     return resourceBundle.getString(messageId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocaleSupported(String localeName) {
/* 441 */     Boolean value = (Boolean)this.cache.get(localeName);
/*     */     
/* 443 */     if (value != null) {
/* 444 */       return value.booleanValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 451 */     ClassLoader loader = null;
/*     */     
/*     */     while (true) {
/* 454 */       String name = this.bundleName + "_" + localeName;
/*     */ 
/*     */       
/*     */       try {
/* 458 */         Class.forName(name);
/* 459 */         this.cache.put(localeName, Boolean.TRUE);
/* 460 */         return true;
/* 461 */       } catch (Exception e) {
/*     */         InputStream in;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 467 */         if (loader == null) {
/* 468 */           loader = getClass().getClassLoader();
/*     */         }
/* 470 */         name = name.replace('.', '/');
/* 471 */         name = name + ".properties";
/* 472 */         if (loader == null) {
/* 473 */           in = ClassLoader.getSystemResourceAsStream(name);
/*     */         } else {
/* 475 */           in = loader.getResourceAsStream(name);
/* 476 */         }  if (in != null) {
/* 477 */           this.cache.put(localeName, Boolean.TRUE);
/* 478 */           return true;
/*     */         } 
/*     */         
/* 481 */         int index = localeName.indexOf('_');
/*     */         
/* 483 */         if (index > 0) {
/* 484 */           localeName = localeName.substring(0, index);
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 492 */     this.cache.put(localeName, Boolean.FALSE);
/* 493 */     return false;
/*     */   }
/*     */   
/*     */   public String getMessage(Locale locale, String messageId, Object[] parameters) {
/*     */     ResourceBundle resourceBundle;
/*     */     if (parameters == null)
/*     */       return getMessage(locale, messageId); 
/*     */     for (int i = 0; i < parameters.length; i++) {
/*     */       if (!(parameters[i] instanceof String) && !(parameters[i] instanceof Number) && !(parameters[i] instanceof java.util.Date))
/*     */         if (parameters[i] == null) {
/*     */           parameters[i] = "(null)";
/*     */         } else {
/*     */           parameters[i] = parameters[i].toString();
/*     */         }  
/*     */     } 
/*     */     if (locale == null)
/*     */       locale = Locale.getDefault(); 
/*     */     try {
/*     */       resourceBundle = ResourceBundle.getBundle(this.bundleName, locale);
/*     */     } catch (MissingResourceException e) {
/*     */       resourceBundle = ResourceBundle.getBundle(this.bundleName, Locale.ENGLISH);
/*     */     } 
/*     */     MessageFormat format = new MessageFormat(resourceBundle.getString(messageId));
/*     */     format.setLocale(locale);
/*     */     StringBuffer result = new StringBuffer();
/*     */     result = format.format(parameters, result, new FieldPosition(0));
/*     */     return result.toString();
/*     */   }
/*     */   
/*     */   public Locale chooseLocale(String[] languages) {
/*     */     if ((languages = canonicalize(languages)) != null)
/*     */       for (int i = 0; i < languages.length; i++) {
/*     */         if (isLocaleSupported(languages[i]))
/*     */           return getLocale(languages[i]); 
/*     */       }  
/*     */     return null;
/*     */   }
/*     */   
/*     */   private String[] canonicalize(String[] languages) {
/*     */     boolean didClone = false;
/*     */     int trimCount = 0;
/*     */     if (languages == null)
/*     */       return languages; 
/*     */     for (int i = 0; i < languages.length; i++) {
/*     */       String lang = languages[i];
/*     */       int len = lang.length();
/*     */       if (len != 2 && len != 5) {
/*     */         if (!didClone) {
/*     */           languages = (String[])languages.clone();
/*     */           didClone = true;
/*     */         } 
/*     */         languages[i] = null;
/*     */         trimCount++;
/*     */       } else if (len == 2) {
/*     */         lang = lang.toLowerCase();
/*     */         if (lang != languages[i]) {
/*     */           if (!didClone) {
/*     */             languages = (String[])languages.clone();
/*     */             didClone = true;
/*     */           } 
/*     */           languages[i] = lang;
/*     */         } 
/*     */       } else {
/*     */         char[] buf = new char[5];
/*     */         buf[0] = Character.toLowerCase(lang.charAt(0));
/*     */         buf[1] = Character.toLowerCase(lang.charAt(1));
/*     */         buf[2] = '_';
/*     */         buf[3] = Character.toUpperCase(lang.charAt(3));
/*     */         buf[4] = Character.toUpperCase(lang.charAt(4));
/*     */         if (!didClone) {
/*     */           languages = (String[])languages.clone();
/*     */           didClone = true;
/*     */         } 
/*     */         languages[i] = new String(buf);
/*     */       } 
/*     */     } 
/*     */     if (trimCount != 0) {
/*     */       String[] temp = new String[languages.length - trimCount];
/*     */       for (int j = 0; j < temp.length; j++) {
/*     */         while (languages[j + trimCount] == null)
/*     */           trimCount++; 
/*     */         temp[j] = languages[j + trimCount];
/*     */       } 
/*     */       languages = temp;
/*     */     } 
/*     */     return languages;
/*     */   }
/*     */   
/*     */   private Locale getLocale(String localeName) {
/*     */     String language, country;
/*     */     int index = localeName.indexOf('_');
/*     */     if (index == -1) {
/*     */       if (localeName.equals("de"))
/*     */         return Locale.GERMAN; 
/*     */       if (localeName.equals("en"))
/*     */         return Locale.ENGLISH; 
/*     */       if (localeName.equals("fr"))
/*     */         return Locale.FRENCH; 
/*     */       if (localeName.equals("it"))
/*     */         return Locale.ITALIAN; 
/*     */       if (localeName.equals("ja"))
/*     */         return Locale.JAPANESE; 
/*     */       if (localeName.equals("ko"))
/*     */         return Locale.KOREAN; 
/*     */       if (localeName.equals("zh"))
/*     */         return Locale.CHINESE; 
/*     */       language = localeName;
/*     */       country = "";
/*     */     } else {
/*     */       if (localeName.equals("zh_CN"))
/*     */         return Locale.SIMPLIFIED_CHINESE; 
/*     */       if (localeName.equals("zh_TW"))
/*     */         return Locale.TRADITIONAL_CHINESE; 
/*     */       language = localeName.substring(0, index);
/*     */       country = localeName.substring(index + 1);
/*     */     } 
/*     */     return new Locale(language, country);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\dtdparser\MessageCatalog.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */
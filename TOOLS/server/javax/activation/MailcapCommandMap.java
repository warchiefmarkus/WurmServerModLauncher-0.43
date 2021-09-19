/*     */ package javax.activation;
/*     */ 
/*     */ import com.sun.activation.registries.LogSupport;
/*     */ import com.sun.activation.registries.MailcapFile;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MailcapCommandMap
/*     */   extends CommandMap
/*     */ {
/* 140 */   private static MailcapFile defDB = null;
/*     */ 
/*     */   
/*     */   private MailcapFile[] DB;
/*     */   
/*     */   private static final int PROG = 0;
/*     */ 
/*     */   
/*     */   public MailcapCommandMap() {
/* 149 */     List dbv = new ArrayList(5);
/* 150 */     MailcapFile mf = null;
/* 151 */     dbv.add(null);
/*     */     
/* 153 */     LogSupport.log("MailcapCommandMap: load HOME");
/*     */     try {
/* 155 */       String user_home = System.getProperty("user.home");
/*     */       
/* 157 */       if (user_home != null) {
/* 158 */         String path = user_home + File.separator + ".mailcap";
/* 159 */         mf = loadFile(path);
/* 160 */         if (mf != null)
/* 161 */           dbv.add(mf); 
/*     */       } 
/* 163 */     } catch (SecurityException ex) {}
/*     */     
/* 165 */     LogSupport.log("MailcapCommandMap: load SYS");
/*     */     
/*     */     try {
/* 168 */       String system_mailcap = System.getProperty("java.home") + File.separator + "lib" + File.separator + "mailcap";
/*     */       
/* 170 */       mf = loadFile(system_mailcap);
/* 171 */       if (mf != null)
/* 172 */         dbv.add(mf); 
/* 173 */     } catch (SecurityException ex) {}
/*     */     
/* 175 */     LogSupport.log("MailcapCommandMap: load JAR");
/*     */     
/* 177 */     loadAllResources(dbv, "META-INF/mailcap");
/*     */     
/* 179 */     LogSupport.log("MailcapCommandMap: load DEF");
/* 180 */     synchronized (MailcapCommandMap.class) {
/*     */       
/* 182 */       if (defDB == null) {
/* 183 */         defDB = loadResource("/META-INF/mailcap.default");
/*     */       }
/*     */     } 
/* 186 */     if (defDB != null) {
/* 187 */       dbv.add(defDB);
/*     */     }
/* 189 */     this.DB = new MailcapFile[dbv.size()];
/* 190 */     this.DB = dbv.<MailcapFile>toArray(this.DB);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MailcapFile loadResource(String name) {
/* 197 */     InputStream clis = null;
/*     */     try {
/* 199 */       clis = SecuritySupport.getResourceAsStream(getClass(), name);
/* 200 */       if (clis != null) {
/* 201 */         MailcapFile mf = new MailcapFile(clis);
/* 202 */         if (LogSupport.isLoggable()) {
/* 203 */           LogSupport.log("MailcapCommandMap: successfully loaded mailcap file: " + name);
/*     */         }
/* 205 */         return mf;
/*     */       } 
/* 207 */       if (LogSupport.isLoggable()) {
/* 208 */         LogSupport.log("MailcapCommandMap: not loading mailcap file: " + name);
/*     */       }
/*     */     }
/* 211 */     catch (IOException e) {
/* 212 */       if (LogSupport.isLoggable())
/* 213 */         LogSupport.log("MailcapCommandMap: can't load " + name, e); 
/* 214 */     } catch (SecurityException sex) {
/* 215 */       if (LogSupport.isLoggable())
/* 216 */         LogSupport.log("MailcapCommandMap: can't load " + name, sex); 
/*     */     } finally {
/*     */       try {
/* 219 */         if (clis != null)
/* 220 */           clis.close(); 
/* 221 */       } catch (IOException ex) {}
/*     */     } 
/* 223 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadAllResources(List v, String name) {
/* 230 */     boolean anyLoaded = false;
/*     */     try {
/*     */       URL[] urls;
/* 233 */       ClassLoader cld = null;
/*     */       
/* 235 */       cld = SecuritySupport.getContextClassLoader();
/* 236 */       if (cld == null)
/* 237 */         cld = getClass().getClassLoader(); 
/* 238 */       if (cld != null) {
/* 239 */         urls = SecuritySupport.getResources(cld, name);
/*     */       } else {
/* 241 */         urls = SecuritySupport.getSystemResources(name);
/* 242 */       }  if (urls != null) {
/* 243 */         if (LogSupport.isLoggable())
/* 244 */           LogSupport.log("MailcapCommandMap: getResources"); 
/* 245 */         for (int i = 0; i < urls.length; i++) {
/* 246 */           URL url = urls[i];
/* 247 */           InputStream clis = null;
/* 248 */           if (LogSupport.isLoggable());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 282 */     catch (Exception ex) {
/* 283 */       if (LogSupport.isLoggable()) {
/* 284 */         LogSupport.log("MailcapCommandMap: can't load " + name, ex);
/*     */       }
/*     */     } 
/*     */     
/* 288 */     if (!anyLoaded) {
/* 289 */       if (LogSupport.isLoggable())
/* 290 */         LogSupport.log("MailcapCommandMap: !anyLoaded"); 
/* 291 */       MailcapFile mf = loadResource("/" + name);
/* 292 */       if (mf != null) {
/* 293 */         v.add(mf);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MailcapFile loadFile(String name) {
/* 301 */     MailcapFile mtf = null;
/*     */     
/*     */     try {
/* 304 */       mtf = new MailcapFile(name);
/* 305 */     } catch (IOException e) {}
/*     */ 
/*     */     
/* 308 */     return mtf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MailcapCommandMap(String fileName) throws IOException {
/* 319 */     this();
/*     */     
/* 321 */     if (LogSupport.isLoggable())
/* 322 */       LogSupport.log("MailcapCommandMap: load PROG from " + fileName); 
/* 323 */     if (this.DB[0] == null) {
/* 324 */       this.DB[0] = new MailcapFile(fileName);
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
/*     */   public MailcapCommandMap(InputStream is) {
/* 336 */     this();
/*     */     
/* 338 */     LogSupport.log("MailcapCommandMap: load PROG");
/* 339 */     if (this.DB[0] == null) {
/*     */       try {
/* 341 */         this.DB[0] = new MailcapFile(is);
/* 342 */       } catch (IOException ex) {}
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
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized CommandInfo[] getPreferredCommands(String mimeType) {
/* 362 */     List cmdList = new ArrayList();
/* 363 */     if (mimeType != null)
/* 364 */       mimeType = mimeType.toLowerCase(Locale.ENGLISH); 
/*     */     int i;
/* 366 */     for (i = 0; i < this.DB.length; i++) {
/* 367 */       if (this.DB[i] != null) {
/*     */         
/* 369 */         Map cmdMap = this.DB[i].getMailcapList(mimeType);
/* 370 */         if (cmdMap != null) {
/* 371 */           appendPrefCmdsToList(cmdMap, cmdList);
/*     */         }
/*     */       } 
/*     */     } 
/* 375 */     for (i = 0; i < this.DB.length; i++) {
/* 376 */       if (this.DB[i] != null) {
/*     */         
/* 378 */         Map cmdMap = this.DB[i].getMailcapFallbackList(mimeType);
/* 379 */         if (cmdMap != null)
/* 380 */           appendPrefCmdsToList(cmdMap, cmdList); 
/*     */       } 
/*     */     } 
/* 383 */     CommandInfo[] cmdInfos = new CommandInfo[cmdList.size()];
/* 384 */     cmdInfos = (CommandInfo[])cmdList.toArray((Object[])cmdInfos);
/*     */     
/* 386 */     return cmdInfos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendPrefCmdsToList(Map cmdHash, List cmdList) {
/* 393 */     Iterator verb_enum = cmdHash.keySet().iterator();
/*     */     
/* 395 */     while (verb_enum.hasNext()) {
/* 396 */       String verb = verb_enum.next();
/* 397 */       if (!checkForVerb(cmdList, verb)) {
/* 398 */         List cmdList2 = (List)cmdHash.get(verb);
/* 399 */         String className = cmdList2.get(0);
/* 400 */         cmdList.add(new CommandInfo(verb, className));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkForVerb(List cmdList, String verb) {
/* 410 */     Iterator ee = cmdList.iterator();
/* 411 */     while (ee.hasNext()) {
/* 412 */       String enum_verb = ((CommandInfo)ee.next()).getCommandName();
/*     */       
/* 414 */       if (enum_verb.equals(verb))
/* 415 */         return true; 
/*     */     } 
/* 417 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized CommandInfo[] getAllCommands(String mimeType) {
/* 428 */     List cmdList = new ArrayList();
/* 429 */     if (mimeType != null)
/* 430 */       mimeType = mimeType.toLowerCase(Locale.ENGLISH); 
/*     */     int i;
/* 432 */     for (i = 0; i < this.DB.length; i++) {
/* 433 */       if (this.DB[i] != null) {
/*     */         
/* 435 */         Map cmdMap = this.DB[i].getMailcapList(mimeType);
/* 436 */         if (cmdMap != null) {
/* 437 */           appendCmdsToList(cmdMap, cmdList);
/*     */         }
/*     */       } 
/*     */     } 
/* 441 */     for (i = 0; i < this.DB.length; i++) {
/* 442 */       if (this.DB[i] != null) {
/*     */         
/* 444 */         Map cmdMap = this.DB[i].getMailcapFallbackList(mimeType);
/* 445 */         if (cmdMap != null)
/* 446 */           appendCmdsToList(cmdMap, cmdList); 
/*     */       } 
/*     */     } 
/* 449 */     CommandInfo[] cmdInfos = new CommandInfo[cmdList.size()];
/* 450 */     cmdInfos = (CommandInfo[])cmdList.toArray((Object[])cmdInfos);
/*     */     
/* 452 */     return cmdInfos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendCmdsToList(Map typeHash, List cmdList) {
/* 459 */     Iterator verb_enum = typeHash.keySet().iterator();
/*     */     
/* 461 */     while (verb_enum.hasNext()) {
/* 462 */       String verb = verb_enum.next();
/* 463 */       List cmdList2 = (List)typeHash.get(verb);
/* 464 */       Iterator cmd_enum = cmdList2.iterator();
/*     */       
/* 466 */       while (cmd_enum.hasNext()) {
/* 467 */         String cmd = cmd_enum.next();
/* 468 */         cmdList.add(new CommandInfo(verb, cmd));
/*     */       } 
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
/*     */   public synchronized CommandInfo getCommand(String mimeType, String cmdName) {
/* 483 */     if (mimeType != null)
/* 484 */       mimeType = mimeType.toLowerCase(Locale.ENGLISH); 
/*     */     int i;
/* 486 */     for (i = 0; i < this.DB.length; i++) {
/* 487 */       if (this.DB[i] != null) {
/*     */         
/* 489 */         Map cmdMap = this.DB[i].getMailcapList(mimeType);
/* 490 */         if (cmdMap != null) {
/*     */           
/* 492 */           List v = (List)cmdMap.get(cmdName);
/* 493 */           if (v != null) {
/* 494 */             String cmdClassName = v.get(0);
/*     */             
/* 496 */             if (cmdClassName != null) {
/* 497 */               return new CommandInfo(cmdName, cmdClassName);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 503 */     for (i = 0; i < this.DB.length; i++) {
/* 504 */       if (this.DB[i] != null) {
/*     */         
/* 506 */         Map cmdMap = this.DB[i].getMailcapFallbackList(mimeType);
/* 507 */         if (cmdMap != null) {
/*     */           
/* 509 */           List v = (List)cmdMap.get(cmdName);
/* 510 */           if (v != null) {
/* 511 */             String cmdClassName = v.get(0);
/*     */             
/* 513 */             if (cmdClassName != null)
/* 514 */               return new CommandInfo(cmdName, cmdClassName); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 518 */     }  return null;
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
/*     */   public synchronized void addMailcap(String mail_cap) {
/* 532 */     LogSupport.log("MailcapCommandMap: add to PROG");
/* 533 */     if (this.DB[0] == null) {
/* 534 */       this.DB[0] = new MailcapFile();
/*     */     }
/* 536 */     this.DB[0].appendToMailcap(mail_cap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized DataContentHandler createDataContentHandler(String mimeType) {
/* 547 */     if (LogSupport.isLoggable()) {
/* 548 */       LogSupport.log("MailcapCommandMap: createDataContentHandler for " + mimeType);
/*     */     }
/* 550 */     if (mimeType != null)
/* 551 */       mimeType = mimeType.toLowerCase(Locale.ENGLISH); 
/*     */     int i;
/* 553 */     for (i = 0; i < this.DB.length; i++) {
/* 554 */       if (this.DB[i] != null) {
/*     */         
/* 556 */         if (LogSupport.isLoggable())
/* 557 */           LogSupport.log("  search DB #" + i); 
/* 558 */         Map cmdMap = this.DB[i].getMailcapList(mimeType);
/* 559 */         if (cmdMap != null) {
/* 560 */           List v = (List)cmdMap.get("content-handler");
/* 561 */           if (v != null) {
/* 562 */             String name = v.get(0);
/* 563 */             DataContentHandler dch = getDataContentHandler(name);
/* 564 */             if (dch != null) {
/* 565 */               return dch;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 571 */     for (i = 0; i < this.DB.length; i++) {
/* 572 */       if (this.DB[i] != null) {
/*     */         
/* 574 */         if (LogSupport.isLoggable())
/* 575 */           LogSupport.log("  search fallback DB #" + i); 
/* 576 */         Map cmdMap = this.DB[i].getMailcapFallbackList(mimeType);
/* 577 */         if (cmdMap != null) {
/* 578 */           List v = (List)cmdMap.get("content-handler");
/* 579 */           if (v != null) {
/* 580 */             String name = v.get(0);
/* 581 */             DataContentHandler dch = getDataContentHandler(name);
/* 582 */             if (dch != null)
/* 583 */               return dch; 
/*     */           } 
/*     */         } 
/*     */       } 
/* 587 */     }  return null;
/*     */   }
/*     */   
/*     */   private DataContentHandler getDataContentHandler(String name) {
/* 591 */     if (LogSupport.isLoggable())
/* 592 */       LogSupport.log("    got content-handler"); 
/* 593 */     if (LogSupport.isLoggable())
/* 594 */       LogSupport.log("      class " + name); 
/*     */     try {
/* 596 */       ClassLoader cld = null;
/*     */       
/* 598 */       cld = SecuritySupport.getContextClassLoader();
/* 599 */       if (cld == null)
/* 600 */         cld = getClass().getClassLoader(); 
/* 601 */       Class cl = null;
/*     */       try {
/* 603 */         cl = cld.loadClass(name);
/* 604 */       } catch (Exception ex) {
/*     */         
/* 606 */         cl = Class.forName(name);
/*     */       } 
/* 608 */       if (cl != null)
/* 609 */         return (DataContentHandler)cl.newInstance(); 
/* 610 */     } catch (IllegalAccessException e) {
/* 611 */       if (LogSupport.isLoggable())
/* 612 */         LogSupport.log("Can't load DCH " + name, e); 
/* 613 */     } catch (ClassNotFoundException e) {
/* 614 */       if (LogSupport.isLoggable())
/* 615 */         LogSupport.log("Can't load DCH " + name, e); 
/* 616 */     } catch (InstantiationException e) {
/* 617 */       if (LogSupport.isLoggable())
/* 618 */         LogSupport.log("Can't load DCH " + name, e); 
/*     */     } 
/* 620 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String[] getMimeTypes() {
/* 630 */     List mtList = new ArrayList();
/*     */     
/* 632 */     for (int i = 0; i < this.DB.length; i++) {
/* 633 */       if (this.DB[i] != null) {
/*     */         
/* 635 */         String[] ts = this.DB[i].getMimeTypes();
/* 636 */         if (ts != null)
/* 637 */           for (int j = 0; j < ts.length; j++) {
/*     */             
/* 639 */             if (!mtList.contains(ts[j])) {
/* 640 */               mtList.add(ts[j]);
/*     */             }
/*     */           }  
/*     */       } 
/*     */     } 
/* 645 */     String[] mts = new String[mtList.size()];
/* 646 */     mts = mtList.<String>toArray(mts);
/*     */     
/* 648 */     return mts;
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
/*     */   public synchronized String[] getNativeCommands(String mimeType) {
/* 666 */     List cmdList = new ArrayList();
/* 667 */     if (mimeType != null) {
/* 668 */       mimeType = mimeType.toLowerCase(Locale.ENGLISH);
/*     */     }
/* 670 */     for (int i = 0; i < this.DB.length; i++) {
/* 671 */       if (this.DB[i] != null) {
/*     */         
/* 673 */         String[] arrayOfString = this.DB[i].getNativeCommands(mimeType);
/* 674 */         if (arrayOfString != null)
/* 675 */           for (int j = 0; j < arrayOfString.length; j++) {
/*     */             
/* 677 */             if (!cmdList.contains(arrayOfString[j])) {
/* 678 */               cmdList.add(arrayOfString[j]);
/*     */             }
/*     */           }  
/*     */       } 
/*     */     } 
/* 683 */     String[] cmds = new String[cmdList.size()];
/* 684 */     cmds = cmdList.<String>toArray(cmds);
/*     */     
/* 686 */     return cmds;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\MailcapCommandMap.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */
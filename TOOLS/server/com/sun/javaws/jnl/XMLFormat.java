/*     */ package com.sun.javaws.jnl;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.deploy.util.URLUtil;
/*     */ import com.sun.deploy.xml.BadTokenException;
/*     */ import com.sun.deploy.xml.XMLEncoding;
/*     */ import com.sun.deploy.xml.XMLNode;
/*     */ import com.sun.deploy.xml.XMLParser;
/*     */ import com.sun.javaws.Globals;
/*     */ import com.sun.javaws.exceptions.BadFieldException;
/*     */ import com.sun.javaws.exceptions.JNLParseException;
/*     */ import com.sun.javaws.exceptions.MissingFieldException;
/*     */ import com.sun.javaws.util.GeneralUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLFormat
/*     */ {
/*     */   public static LaunchDesc parse(byte[] paramArrayOfbyte) throws IOException, BadFieldException, MissingFieldException, JNLParseException {
/*     */     String str1;
/*     */     XMLNode xMLNode;
/*     */     byte b2;
/*     */     try {
/*  42 */       str1 = XMLEncoding.decodeXML(paramArrayOfbyte);
/*  43 */     } catch (Exception exception) {
/*  44 */       throw new JNLParseException(null, exception, "exception determining encoding of jnlp file", 0);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/*  49 */       xMLNode = (new XMLParser(str1)).parse();
/*  50 */     } catch (BadTokenException badTokenException) {
/*  51 */       throw new JNLParseException(str1, badTokenException, "wrong kind of token found", badTokenException.getLine());
/*     */     }
/*  53 */     catch (Exception exception) {
/*  54 */       throw new JNLParseException(str1, exception, "exception parsing jnlp file", 0);
/*     */     } 
/*     */ 
/*     */     
/*  58 */     InformationDesc informationDesc = null;
/*  59 */     ResourcesDesc resourcesDesc = null;
/*  60 */     ApplicationDesc applicationDesc = null;
/*  61 */     AppletDesc appletDesc = null;
/*  62 */     LibraryDesc libraryDesc = null;
/*  63 */     InstallerDesc installerDesc = null;
/*  64 */     String str2 = null;
/*     */ 
/*     */     
/*  67 */     if (xMLNode.getName().equals("player") || xMLNode.getName().equals("viewer")) {
/*     */       
/*  69 */       String str = XMLUtils.getAttribute(xMLNode, null, "tab");
/*  70 */       return LaunchDescFactory.buildInternalLaunchDesc(xMLNode.getName(), str1, str);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  75 */     if (!xMLNode.getName().equals("jnlp")) {
/*  76 */       throw new MissingFieldException(str1, "<jnlp>");
/*     */     }
/*     */ 
/*     */     
/*  80 */     String str3 = XMLUtils.getAttribute(xMLNode, "", "spec", "1.0+");
/*  81 */     String str4 = XMLUtils.getAttribute(xMLNode, "", "version");
/*     */     
/*  83 */     URL uRL1 = URLUtil.asPathURL(XMLUtils.getAttributeURL(str1, xMLNode, "", "codebase"));
/*     */     
/*  85 */     URL uRL2 = XMLUtils.getAttributeURL(str1, uRL1, xMLNode, "", "href");
/*     */ 
/*     */     
/*  88 */     byte b1 = 0;
/*  89 */     if (XMLUtils.isElementPath(xMLNode, "<security><all-permissions>")) {
/*  90 */       b1 = 1;
/*  91 */     } else if (XMLUtils.isElementPath(xMLNode, "<security><j2ee-application-client-permissions>")) {
/*  92 */       b1 = 2;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  97 */     if (XMLUtils.isElementPath(xMLNode, "<application-desc>")) {
/*  98 */       b2 = 1;
/*  99 */       applicationDesc = buildApplicationDesc(str1, xMLNode);
/* 100 */     } else if (XMLUtils.isElementPath(xMLNode, "<component-desc>")) {
/* 101 */       b2 = 3;
/* 102 */       libraryDesc = buildLibraryDesc(str1, xMLNode);
/* 103 */     } else if (XMLUtils.isElementPath(xMLNode, "<installer-desc>")) {
/* 104 */       b2 = 4;
/* 105 */       installerDesc = buildInstallerDesc(str1, uRL1, xMLNode);
/* 106 */     } else if (XMLUtils.isElementPath(xMLNode, "<applet-desc>")) {
/* 107 */       b2 = 2;
/* 108 */       appletDesc = buildAppletDesc(str1, uRL1, xMLNode);
/*     */     } else {
/* 110 */       throw new MissingFieldException(str1, "<jnlp>(<application-desc>|<applet-desc>|<installer-desc>|<component-desc>)");
/*     */     } 
/*     */ 
/*     */     
/* 114 */     informationDesc = buildInformationDesc(str1, uRL1, xMLNode);
/* 115 */     resourcesDesc = buildResourcesDesc(str1, uRL1, xMLNode, false);
/*     */     
/* 117 */     LaunchDesc launchDesc = new LaunchDesc(str3, uRL1, uRL2, str4, informationDesc, b1, resourcesDesc, b2, applicationDesc, appletDesc, libraryDesc, installerDesc, str2, str1, paramArrayOfbyte);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     Trace.println("returning LaunchDesc from XMLFormat.parse():\n" + launchDesc, TraceLevel.TEMP);
/*     */ 
/*     */     
/* 137 */     return launchDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static InformationDesc combineInformationDesc(InformationDesc paramInformationDesc1, InformationDesc paramInformationDesc2) {
/* 144 */     if (paramInformationDesc1 == null) return paramInformationDesc2; 
/* 145 */     if (paramInformationDesc2 == null) return paramInformationDesc1;
/*     */     
/* 147 */     String str1 = (paramInformationDesc1.getTitle() != null) ? paramInformationDesc1.getTitle() : paramInformationDesc2.getTitle();
/* 148 */     String str2 = (paramInformationDesc1.getVendor() != null) ? paramInformationDesc1.getVendor() : paramInformationDesc2.getVendor();
/* 149 */     URL uRL = (paramInformationDesc1.getHome() != null) ? paramInformationDesc1.getHome() : paramInformationDesc2.getHome();
/*     */ 
/*     */     
/* 152 */     String[] arrayOfString = new String[4];
/* 153 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 154 */       arrayOfString[b] = (paramInformationDesc1.getDescription(b) != null) ? paramInformationDesc1.getDescription(b) : paramInformationDesc2.getDescription(b);
/*     */     }
/*     */ 
/*     */     
/* 158 */     ArrayList arrayList = new ArrayList();
/* 159 */     if (paramInformationDesc2.getIcons() != null) arrayList.addAll(Arrays.asList(paramInformationDesc2.getIcons())); 
/* 160 */     if (paramInformationDesc1.getIcons() != null) arrayList.addAll(Arrays.asList(paramInformationDesc1.getIcons())); 
/* 161 */     IconDesc[] arrayOfIconDesc = new IconDesc[arrayList.size()];
/* 162 */     arrayOfIconDesc = (IconDesc[])arrayList.toArray((Object[])arrayOfIconDesc);
/*     */ 
/*     */     
/* 165 */     boolean bool = (paramInformationDesc1.supportsOfflineOperation() || paramInformationDesc1.supportsOfflineOperation()) ? true : false;
/*     */     
/* 167 */     ShortcutDesc shortcutDesc = (paramInformationDesc1.getShortcut() != null) ? paramInformationDesc1.getShortcut() : paramInformationDesc2.getShortcut();
/*     */     
/* 169 */     AssociationDesc[] arrayOfAssociationDesc = (AssociationDesc[])addArrays((Object[])paramInformationDesc1.getAssociations(), (Object[])paramInformationDesc2.getAssociations());
/*     */ 
/*     */     
/* 172 */     RContentDesc[] arrayOfRContentDesc = (RContentDesc[])addArrays((Object[])paramInformationDesc1.getRelatedContent(), (Object[])paramInformationDesc2.getRelatedContent());
/*     */ 
/*     */     
/* 175 */     return new InformationDesc(str1, str2, uRL, arrayOfString, arrayOfIconDesc, shortcutDesc, arrayOfRContentDesc, arrayOfAssociationDesc, bool);
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
/*     */   private static InformationDesc buildInformationDesc(String paramString, URL paramURL, XMLNode paramXMLNode) throws MissingFieldException, BadFieldException {
/* 189 */     ArrayList arrayList = new ArrayList();
/*     */ 
/*     */     
/* 192 */     XMLUtils.visitElements(paramXMLNode, "<information>", new XMLUtils.ElementVisitor(paramString, paramURL, arrayList) {
/*     */           private final String val$source;
/*     */           private final URL val$codebase;
/*     */           private final ArrayList val$list;
/*     */           
/*     */           public void visitElement(XMLNode param1XMLNode) throws BadFieldException, MissingFieldException {
/* 198 */             String[] arrayOfString1 = GeneralUtil.getStringList(XMLUtils.getAttribute(param1XMLNode, "", "locale"));
/*     */ 
/*     */ 
/*     */             
/* 202 */             String[] arrayOfString2 = GeneralUtil.getStringList(XMLUtils.getAttribute(param1XMLNode, "", "os", null));
/*     */             
/* 204 */             String[] arrayOfString3 = GeneralUtil.getStringList(XMLUtils.getAttribute(param1XMLNode, "", "arch", null));
/*     */             
/* 206 */             String[] arrayOfString4 = GeneralUtil.getStringList(XMLUtils.getAttribute(param1XMLNode, "", "locale", null));
/*     */             
/* 208 */             String[] arrayOfString5 = GeneralUtil.getStringList(XMLUtils.getAttribute(param1XMLNode, "", "platform", null));
/*     */             
/* 210 */             if (GeneralUtil.prefixMatchStringList(arrayOfString2, Config.getOSName()) && GeneralUtil.prefixMatchStringList(arrayOfString3, Config.getOSArch()) && GeneralUtil.prefixMatchStringList(arrayOfString5, Config.getOSPlatform()) && XMLFormat.matchDefaultLocale(arrayOfString4)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 219 */               String str1 = XMLUtils.getElementContents(param1XMLNode, "<title>");
/* 220 */               String str2 = XMLUtils.getElementContents(param1XMLNode, "<vendor>");
/* 221 */               URL uRL = XMLUtils.getAttributeURL(this.val$source, this.val$codebase, param1XMLNode, "<homepage>", "href");
/*     */ 
/*     */ 
/*     */               
/* 225 */               String[] arrayOfString = new String[4];
/*     */               
/* 227 */               arrayOfString[0] = XMLUtils.getElementContentsWithAttribute(param1XMLNode, "<description>", "kind", "", null);
/*     */ 
/*     */               
/* 230 */               arrayOfString[2] = XMLUtils.getElementContentsWithAttribute(param1XMLNode, "<description>", "kind", "one-line", null);
/*     */ 
/*     */               
/* 233 */               arrayOfString[1] = XMLUtils.getElementContentsWithAttribute(param1XMLNode, "<description>", "kind", "short", null);
/*     */ 
/*     */               
/* 236 */               arrayOfString[3] = XMLUtils.getElementContentsWithAttribute(param1XMLNode, "<description>", "kind", "tooltip", null);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 241 */               IconDesc[] arrayOfIconDesc = XMLFormat.getIconDescs(this.val$source, this.val$codebase, param1XMLNode);
/*     */ 
/*     */               
/* 244 */               ShortcutDesc shortcutDesc = XMLFormat.getShortcutDesc(param1XMLNode);
/*     */ 
/*     */               
/* 247 */               RContentDesc[] arrayOfRContentDesc = XMLFormat.getRContentDescs(this.val$source, this.val$codebase, param1XMLNode);
/*     */ 
/*     */ 
/*     */               
/* 251 */               AssociationDesc[] arrayOfAssociationDesc = XMLFormat.getAssociationDesc(this.val$source, param1XMLNode);
/*     */               
/* 253 */               this.val$list.add(new InformationDesc(str1, str2, uRL, arrayOfString, arrayOfIconDesc, shortcutDesc, arrayOfRContentDesc, arrayOfAssociationDesc, XMLUtils.isElementPath(param1XMLNode, "<offline-allowed>")));
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 269 */     InformationDesc informationDesc = new InformationDesc(null, null, null, null, null, null, null, null, false);
/*     */     
/* 271 */     for (byte b = 0; b < arrayList.size(); b++) {
/* 272 */       InformationDesc informationDesc1 = arrayList.get(b);
/* 273 */       informationDesc = combineInformationDesc(informationDesc1, informationDesc);
/*     */     } 
/*     */ 
/*     */     
/* 277 */     if (informationDesc.getTitle() == null) {
/* 278 */       throw new MissingFieldException(paramString, "<jnlp><information><title>");
/*     */     }
/*     */     
/* 281 */     if (informationDesc.getVendor() == null) {
/* 282 */       throw new MissingFieldException(paramString, "<jnlp><information><vendor>");
/*     */     }
/*     */     
/* 285 */     return informationDesc;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object[] addArrays(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2) {
/* 290 */     if (paramArrayOfObject1 == null) return paramArrayOfObject2; 
/* 291 */     if (paramArrayOfObject2 == null) return paramArrayOfObject1; 
/* 292 */     ArrayList arrayList = new ArrayList();
/*     */     byte b;
/* 294 */     for (b = 0; b < paramArrayOfObject1.length; arrayList.add(paramArrayOfObject1[b++]));
/* 295 */     for (b = 0; b < paramArrayOfObject2.length; arrayList.add(paramArrayOfObject2[b++]));
/* 296 */     return arrayList.<Object>toArray(paramArrayOfObject1);
/*     */   }
/*     */   
/*     */   public static boolean matchDefaultLocale(String[] paramArrayOfString) {
/* 300 */     return GeneralUtil.matchLocale(paramArrayOfString, Globals.getDefaultLocale());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final ResourcesDesc buildResourcesDesc(String paramString, URL paramURL, XMLNode paramXMLNode, boolean paramBoolean) throws MissingFieldException, BadFieldException {
/* 307 */     ResourcesDesc resourcesDesc = new ResourcesDesc();
/*     */ 
/*     */     
/* 310 */     XMLUtils.visitElements(paramXMLNode, "<resources>", new XMLUtils.ElementVisitor(paramString, paramURL, resourcesDesc, paramBoolean) { private final String val$source; private final URL val$codebase;
/*     */           
/*     */           public void visitElement(XMLNode param1XMLNode) throws MissingFieldException, BadFieldException {
/* 313 */             String[] arrayOfString1 = GeneralUtil.getStringList(XMLUtils.getAttribute(param1XMLNode, "", "os", null));
/* 314 */             String[] arrayOfString2 = GeneralUtil.getStringList(XMLUtils.getAttribute(param1XMLNode, "", "arch", null));
/* 315 */             String[] arrayOfString3 = GeneralUtil.getStringList(XMLUtils.getAttribute(param1XMLNode, "", "locale", null));
/* 316 */             if (GeneralUtil.prefixMatchStringList(arrayOfString1, Config.getOSName()) && GeneralUtil.prefixMatchStringList(arrayOfString2, Config.getOSArch()) && XMLFormat.matchDefaultLocale(arrayOfString3))
/*     */             {
/*     */ 
/*     */               
/* 320 */               XMLUtils.visitChildrenElements(param1XMLNode, new XMLUtils.ElementVisitor(this) { private final XMLFormat.null this$0;
/*     */                     public void visitElement(XMLNode param2XMLNode) throws MissingFieldException, BadFieldException {
/* 322 */                       XMLFormat.handleResourceElement(this.this$0.val$source, this.this$0.val$codebase, param2XMLNode, this.this$0.val$rdesc, this.this$0.val$ignoreJres);
/*     */                     } }
/*     */                 ); } 
/*     */           } private final ResourcesDesc val$rdesc;
/*     */           private final boolean val$ignoreJres; }
/*     */       );
/* 328 */     return resourcesDesc.isEmpty() ? null : resourcesDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static IconDesc[] getIconDescs(String paramString, URL paramURL, XMLNode paramXMLNode) throws MissingFieldException, BadFieldException {
/* 334 */     ArrayList arrayList = new ArrayList();
/* 335 */     XMLUtils.visitElements(paramXMLNode, "<icon>", new XMLUtils.ElementVisitor(paramString, paramURL, arrayList) { private final String val$source;
/*     */           
/*     */           public void visitElement(XMLNode param1XMLNode) throws MissingFieldException, BadFieldException {
/* 338 */             String str1 = XMLUtils.getAttribute(param1XMLNode, "", "kind", "");
/* 339 */             URL uRL = XMLUtils.getRequiredURL(this.val$source, this.val$codebase, param1XMLNode, "", "href");
/*     */             
/* 341 */             String str2 = XMLUtils.getAttribute(param1XMLNode, "", "version", null);
/*     */             
/* 343 */             int i = XMLUtils.getIntAttribute(this.val$source, param1XMLNode, "", "height", 0);
/*     */             
/* 345 */             int j = XMLUtils.getIntAttribute(this.val$source, param1XMLNode, "", "width", 0);
/*     */             
/* 347 */             int k = XMLUtils.getIntAttribute(this.val$source, param1XMLNode, "", "depth", 0);
/*     */ 
/*     */ 
/*     */             
/* 351 */             byte b = 0;
/* 352 */             if (str1.equals("selected")) {
/* 353 */               b = 1;
/* 354 */             } else if (str1.equals("disabled")) {
/* 355 */               b = 2;
/* 356 */             } else if (str1.equals("rollover")) {
/* 357 */               b = 3;
/* 358 */             } else if (str1.equals("splash")) {
/* 359 */               b = 4;
/*     */             } 
/* 361 */             this.val$answer.add(new IconDesc(uRL, str2, i, j, k, b));
/*     */           }
/*     */           private final URL val$codebase; private final ArrayList val$answer; }
/*     */       );
/* 365 */     return (IconDesc[])arrayList.toArray((Object[])new IconDesc[arrayList.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ShortcutDesc getShortcutDesc(XMLNode paramXMLNode) throws MissingFieldException, BadFieldException {
/* 370 */     ArrayList arrayList = new ArrayList();
/*     */     
/* 372 */     XMLUtils.visitElements(paramXMLNode, "<shortcut>", new XMLUtils.ElementVisitor(arrayList) { private final ArrayList val$shortcuts;
/*     */           
/*     */           public void visitElement(XMLNode param1XMLNode) throws MissingFieldException, BadFieldException {
/* 375 */             String str1 = XMLUtils.getAttribute(param1XMLNode, "", "online", "true");
/*     */             
/* 377 */             boolean bool1 = str1.equalsIgnoreCase("true");
/* 378 */             boolean bool2 = XMLUtils.isElementPath(param1XMLNode, "<desktop>");
/*     */             
/* 380 */             boolean bool3 = XMLUtils.isElementPath(param1XMLNode, "<menu>");
/*     */             
/* 382 */             String str2 = XMLUtils.getAttribute(param1XMLNode, "<menu>", "submenu");
/*     */             
/* 384 */             this.val$shortcuts.add(new ShortcutDesc(bool1, bool2, bool3, str2));
/*     */           } }
/*     */       );
/*     */ 
/*     */     
/* 389 */     if (arrayList.size() > 0) {
/* 390 */       return arrayList.get(0);
/*     */     }
/* 392 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static AssociationDesc[] getAssociationDesc(String paramString, XMLNode paramXMLNode) throws MissingFieldException, BadFieldException {
/* 397 */     ArrayList arrayList = new ArrayList();
/* 398 */     XMLUtils.visitElements(paramXMLNode, "<association>", new XMLUtils.ElementVisitor(paramString, arrayList) { private final String val$source;
/*     */           private final ArrayList val$answer;
/*     */           
/*     */           public void visitElement(XMLNode param1XMLNode) throws MissingFieldException, BadFieldException {
/* 402 */             String str1 = XMLUtils.getRequiredAttribute(this.val$source, param1XMLNode, "", "extensions");
/*     */             
/* 404 */             String str2 = XMLUtils.getRequiredAttribute(this.val$source, param1XMLNode, "", "mime-type");
/*     */             
/* 406 */             this.val$answer.add(new AssociationDesc(str1, str2));
/*     */           } }
/*     */       );
/* 409 */     return (AssociationDesc[])arrayList.toArray((Object[])new AssociationDesc[arrayList.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static RContentDesc[] getRContentDescs(String paramString, URL paramURL, XMLNode paramXMLNode) throws MissingFieldException, BadFieldException {
/* 416 */     ArrayList arrayList = new ArrayList();
/* 417 */     XMLUtils.visitElements(paramXMLNode, "<related-content>", new XMLUtils.ElementVisitor(paramString, paramURL, arrayList) { private final String val$source; private final URL val$codebase;
/*     */           private final ArrayList val$answer;
/*     */           
/*     */           public void visitElement(XMLNode param1XMLNode) throws MissingFieldException, BadFieldException {
/* 421 */             URL uRL1 = XMLUtils.getRequiredURL(this.val$source, this.val$codebase, param1XMLNode, "", "href");
/*     */             
/* 423 */             String str1 = XMLUtils.getElementContents(param1XMLNode, "<title>");
/* 424 */             String str2 = XMLUtils.getElementContents(param1XMLNode, "<description>");
/*     */             
/* 426 */             URL uRL2 = XMLUtils.getAttributeURL(this.val$source, this.val$codebase, param1XMLNode, "<icon>", "href");
/*     */             
/* 428 */             this.val$answer.add(new RContentDesc(uRL1, str1, str2, uRL2));
/*     */           } }
/*     */       );
/* 431 */     return (RContentDesc[])arrayList.toArray((Object[])new RContentDesc[arrayList.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void handleResourceElement(String paramString, URL paramURL, XMLNode paramXMLNode, ResourcesDesc paramResourcesDesc, boolean paramBoolean) throws MissingFieldException, BadFieldException {
/* 440 */     String str = paramXMLNode.getName();
/*     */     
/* 442 */     if (str.equals("jar") || str.equals("nativelib")) {
/*     */ 
/*     */ 
/*     */       
/* 446 */       URL uRL = XMLUtils.getRequiredURL(paramString, paramURL, paramXMLNode, "", "href");
/* 447 */       String str1 = XMLUtils.getAttribute(paramXMLNode, "", "version", null);
/* 448 */       String str2 = XMLUtils.getAttribute(paramXMLNode, "", "download");
/* 449 */       String str3 = XMLUtils.getAttribute(paramXMLNode, "", "main");
/* 450 */       String str4 = XMLUtils.getAttribute(paramXMLNode, "", "part");
/* 451 */       int i = XMLUtils.getIntAttribute(paramString, paramXMLNode, "", "size", 0);
/* 452 */       boolean bool = str.equals("nativelib");
/* 453 */       boolean bool1 = false;
/* 454 */       boolean bool2 = false;
/* 455 */       if ("lazy".equalsIgnoreCase(str2)) bool1 = true; 
/* 456 */       if ("true".equalsIgnoreCase(str3)) bool2 = true; 
/* 457 */       JARDesc jARDesc = new JARDesc(uRL, str1, bool1, bool2, bool, str4, i, paramResourcesDesc);
/*     */       
/* 459 */       paramResourcesDesc.addResource(jARDesc);
/* 460 */     } else if (str.equals("property")) {
/*     */ 
/*     */ 
/*     */       
/* 464 */       String str1 = XMLUtils.getRequiredAttribute(paramString, paramXMLNode, "", "name");
/* 465 */       String str2 = XMLUtils.getRequiredAttributeEmptyOK(paramString, paramXMLNode, "", "value");
/*     */       
/* 467 */       paramResourcesDesc.addResource(new PropertyDesc(str1, str2));
/* 468 */     } else if (str.equals("package")) {
/*     */ 
/*     */ 
/*     */       
/* 472 */       String str1 = XMLUtils.getRequiredAttribute(paramString, paramXMLNode, "", "name");
/* 473 */       String str2 = XMLUtils.getRequiredAttribute(paramString, paramXMLNode, "", "part");
/* 474 */       String str3 = XMLUtils.getAttribute(paramXMLNode, "", "recursive", "false");
/* 475 */       boolean bool = "true".equals(str3);
/* 476 */       paramResourcesDesc.addResource(new PackageDesc(str1, str2, bool));
/* 477 */     } else if (str.equals("extension")) {
/* 478 */       String str1 = XMLUtils.getAttribute(paramXMLNode, "", "name");
/* 479 */       URL uRL = XMLUtils.getRequiredURL(paramString, paramURL, paramXMLNode, "", "href");
/* 480 */       String str2 = XMLUtils.getAttribute(paramXMLNode, "", "version", null);
/*     */       
/* 482 */       ExtDownloadDesc[] arrayOfExtDownloadDesc = getExtDownloadDescs(paramString, paramXMLNode);
/* 483 */       paramResourcesDesc.addResource(new ExtensionDesc(str1, uRL, str2, arrayOfExtDownloadDesc));
/* 484 */     } else if (str.equals("j2se") && !paramBoolean) {
/*     */ 
/*     */ 
/*     */       
/* 488 */       String str1 = XMLUtils.getRequiredAttribute(paramString, paramXMLNode, "", "version");
/*     */ 
/*     */ 
/*     */       
/* 492 */       URL uRL = XMLUtils.getAttributeURL(paramString, paramURL, paramXMLNode, "", "href");
/*     */ 
/*     */       
/* 495 */       String str2 = XMLUtils.getAttribute(paramXMLNode, "", "initial-heap-size");
/*     */       
/* 497 */       String str3 = XMLUtils.getAttribute(paramXMLNode, "", "max-heap-size");
/*     */ 
/*     */       
/* 500 */       String str4 = XMLUtils.getAttribute(paramXMLNode, "", "java-vm-args");
/*     */       
/* 502 */       long l1 = -1L;
/* 503 */       long l2 = -1L;
/* 504 */       l1 = GeneralUtil.heapValToLong(str2);
/* 505 */       l2 = GeneralUtil.heapValToLong(str3);
/*     */       
/* 507 */       ResourcesDesc resourcesDesc = buildResourcesDesc(paramString, paramURL, paramXMLNode, true);
/*     */ 
/*     */       
/* 510 */       JREDesc jREDesc = new JREDesc(str1, l1, l2, str4, uRL, resourcesDesc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 518 */       paramResourcesDesc.addResource(jREDesc);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ExtDownloadDesc[] getExtDownloadDescs(String paramString, XMLNode paramXMLNode) throws BadFieldException, MissingFieldException {
/* 525 */     ArrayList arrayList = new ArrayList();
/*     */     
/* 527 */     XMLUtils.visitElements(paramXMLNode, "<ext-download>", new XMLUtils.ElementVisitor(paramString, arrayList) { private final String val$source; private final ArrayList val$al;
/*     */           public void visitElement(XMLNode param1XMLNode) throws MissingFieldException {
/* 529 */             String str1 = XMLUtils.getRequiredAttribute(this.val$source, param1XMLNode, "", "ext-part");
/* 530 */             String str2 = XMLUtils.getAttribute(param1XMLNode, "", "part");
/* 531 */             String str3 = XMLUtils.getAttribute(param1XMLNode, "", "download", "eager");
/* 532 */             boolean bool = "lazy".equals(str3);
/* 533 */             this.val$al.add(new ExtDownloadDesc(str1, str2, bool));
/*     */           } }
/*     */       );
/* 536 */     ExtDownloadDesc[] arrayOfExtDownloadDesc = new ExtDownloadDesc[arrayList.size()];
/* 537 */     return (ExtDownloadDesc[])arrayList.toArray((Object[])arrayOfExtDownloadDesc);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ApplicationDesc buildApplicationDesc(String paramString, XMLNode paramXMLNode) throws MissingFieldException, BadFieldException {
/* 542 */     String str = XMLUtils.getAttribute(paramXMLNode, "<application-desc>", "main-class");
/*     */     
/* 544 */     ArrayList arrayList = new ArrayList();
/* 545 */     XMLUtils.visitElements(paramXMLNode, "<application-desc><argument>", new XMLUtils.ElementVisitor(paramString, arrayList) { private final String val$source; private final ArrayList val$al1;
/*     */           public void visitElement(XMLNode param1XMLNode) throws MissingFieldException, BadFieldException {
/* 547 */             String str = XMLUtils.getElementContents(param1XMLNode, "", null);
/* 548 */             if (str == null) {
/* 549 */               throw new BadFieldException(this.val$source, XMLUtils.getPathString(param1XMLNode), "");
/*     */             }
/*     */             
/* 552 */             this.val$al1.add(str);
/*     */           } }
/*     */       );
/*     */     
/* 556 */     String[] arrayOfString = new String[arrayList.size()];
/* 557 */     arrayOfString = (String[])arrayList.toArray((Object[])arrayOfString);
/*     */     
/* 559 */     return new ApplicationDesc(str, arrayOfString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static LibraryDesc buildLibraryDesc(String paramString, XMLNode paramXMLNode) throws MissingFieldException, BadFieldException {
/* 566 */     String str = XMLUtils.getAttribute(paramXMLNode, "<component-desc>", "unique-id");
/*     */ 
/*     */     
/* 569 */     return new LibraryDesc(str);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static InstallerDesc buildInstallerDesc(String paramString, URL paramURL, XMLNode paramXMLNode) throws MissingFieldException, BadFieldException {
/* 575 */     String str = XMLUtils.getAttribute(paramXMLNode, "<installer-desc>", "main-class");
/* 576 */     return new InstallerDesc(str);
/*     */   }
/*     */ 
/*     */   
/*     */   private static AppletDesc buildAppletDesc(String paramString, URL paramURL, XMLNode paramXMLNode) throws MissingFieldException, BadFieldException {
/* 581 */     String str1 = XMLUtils.getRequiredAttribute(paramString, paramXMLNode, "<applet-desc>", "main-class");
/* 582 */     String str2 = XMLUtils.getRequiredAttribute(paramString, paramXMLNode, "<applet-desc>", "name");
/* 583 */     URL uRL = XMLUtils.getAttributeURL(paramString, paramURL, paramXMLNode, "<applet-desc>", "documentbase");
/* 584 */     int i = XMLUtils.getRequiredIntAttribute(paramString, paramXMLNode, "<applet-desc>", "width");
/* 585 */     int j = XMLUtils.getRequiredIntAttribute(paramString, paramXMLNode, "<applet-desc>", "height");
/*     */     
/* 587 */     if (i <= 0) throw new BadFieldException(paramString, XMLUtils.getPathString(paramXMLNode) + "<applet-desc>width", (new Integer(i)).toString()); 
/* 588 */     if (j <= 0) throw new BadFieldException(paramString, XMLUtils.getPathString(paramXMLNode) + "<applet-desc>height", (new Integer(j)).toString());
/*     */     
/* 590 */     Properties properties = new Properties();
/*     */     
/* 592 */     XMLUtils.visitElements(paramXMLNode, "<applet-desc><param>", new XMLUtils.ElementVisitor(paramString, properties) { private final String val$source; private final Properties val$params;
/*     */           public void visitElement(XMLNode param1XMLNode) throws MissingFieldException, BadFieldException {
/* 594 */             String str1 = XMLUtils.getRequiredAttribute(this.val$source, param1XMLNode, "", "name");
/*     */             
/* 596 */             String str2 = XMLUtils.getRequiredAttributeEmptyOK(this.val$source, param1XMLNode, "", "value");
/*     */             
/* 598 */             this.val$params.setProperty(str1, str2);
/*     */           } }
/*     */       );
/*     */     
/* 602 */     return new AppletDesc(str2, str1, uRL, i, j, properties);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\XMLFormat.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
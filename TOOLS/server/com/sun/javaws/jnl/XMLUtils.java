/*     */ package com.sun.javaws.jnl;
/*     */ 
/*     */ import com.sun.deploy.xml.XMLNode;
/*     */ import com.sun.javaws.exceptions.BadFieldException;
/*     */ import com.sun.javaws.exceptions.MissingFieldException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLUtils
/*     */ {
/*     */   public static int getIntAttribute(String paramString1, XMLNode paramXMLNode, String paramString2, String paramString3, int paramInt) throws BadFieldException {
/*  25 */     String str = getAttribute(paramXMLNode, paramString2, paramString3);
/*  26 */     if (str == null) return paramInt; 
/*     */     try {
/*  28 */       return Integer.parseInt(str);
/*  29 */     } catch (NumberFormatException numberFormatException) {
/*  30 */       throw new BadFieldException(paramString1, getPathString(paramXMLNode) + paramString2 + paramString3, str);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getRequiredIntAttribute(String paramString1, XMLNode paramXMLNode, String paramString2, String paramString3) throws BadFieldException, MissingFieldException {
/*  37 */     String str = getAttribute(paramXMLNode, paramString2, paramString3);
/*  38 */     if (str == null) throw new MissingFieldException(paramString1, getPathString(paramXMLNode) + paramString2 + paramString3); 
/*     */     try {
/*  40 */       return Integer.parseInt(str);
/*  41 */     } catch (NumberFormatException numberFormatException) {
/*  42 */       throw new BadFieldException(paramString1, getPathString(paramXMLNode) + paramString2 + paramString3, str);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getAttribute(XMLNode paramXMLNode, String paramString1, String paramString2) {
/*  48 */     return getAttribute(paramXMLNode, paramString1, paramString2, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getRequiredAttributeEmptyOK(String paramString1, XMLNode paramXMLNode, String paramString2, String paramString3) throws MissingFieldException {
/*  54 */     String str = null;
/*  55 */     XMLNode xMLNode = findElementPath(paramXMLNode, paramString2);
/*  56 */     if (xMLNode != null) {
/*  57 */       str = xMLNode.getAttribute(paramString3);
/*     */     }
/*  59 */     if (str == null) {
/*  60 */       throw new MissingFieldException(paramString1, getPathString(paramXMLNode) + paramString2 + paramString3);
/*     */     }
/*     */     
/*  63 */     return str;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getRequiredAttribute(String paramString1, XMLNode paramXMLNode, String paramString2, String paramString3) throws MissingFieldException {
/*  68 */     String str = getAttribute(paramXMLNode, paramString2, paramString3, null);
/*  69 */     if (str == null) throw new MissingFieldException(paramString1, getPathString(paramXMLNode) + paramString2 + paramString3); 
/*  70 */     str = str.trim();
/*  71 */     return (str.length() == 0) ? null : str;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getAttribute(XMLNode paramXMLNode, String paramString1, String paramString2, String paramString3) {
/*  76 */     XMLNode xMLNode = findElementPath(paramXMLNode, paramString1);
/*  77 */     if (xMLNode == null) return paramString3; 
/*  78 */     String str = xMLNode.getAttribute(paramString2);
/*  79 */     return (str == null || str.length() == 0) ? paramString3 : str;
/*     */   }
/*     */ 
/*     */   
/*     */   public static URL getAttributeURL(String paramString1, URL paramURL, XMLNode paramXMLNode, String paramString2, String paramString3) throws BadFieldException {
/*  84 */     String str = getAttribute(paramXMLNode, paramString2, paramString3);
/*  85 */     if (str == null) return null; 
/*     */     try {
/*  87 */       if (str.startsWith("jar:")) {
/*  88 */         int i = str.indexOf("!/");
/*  89 */         if (i > 0) {
/*  90 */           String str1 = str.substring(i);
/*  91 */           String str2 = str.substring(4, i);
/*  92 */           URL uRL = (paramURL == null) ? new URL(str2) : new URL(paramURL, str2);
/*     */           
/*  94 */           return new URL("jar:" + uRL.toString() + str1);
/*     */         } 
/*     */       } 
/*  97 */       return (paramURL == null) ? new URL(str) : new URL(paramURL, str);
/*  98 */     } catch (MalformedURLException malformedURLException) {
/*  99 */       if (malformedURLException.getMessage().indexOf("https") != -1) {
/* 100 */         throw new BadFieldException(paramString1, "<jnlp>", "https");
/*     */       }
/* 102 */       throw new BadFieldException(paramString1, getPathString(paramXMLNode) + paramString2 + paramString3, str);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static URL getAttributeURL(String paramString1, XMLNode paramXMLNode, String paramString2, String paramString3) throws BadFieldException {
/* 108 */     return getAttributeURL(paramString1, null, paramXMLNode, paramString2, paramString3);
/*     */   }
/*     */   
/*     */   public static URL getRequiredURL(String paramString1, URL paramURL, XMLNode paramXMLNode, String paramString2, String paramString3) throws BadFieldException, MissingFieldException {
/* 112 */     URL uRL = getAttributeURL(paramString1, paramURL, paramXMLNode, paramString2, paramString3);
/* 113 */     if (uRL == null) throw new MissingFieldException(paramString1, getPathString(paramXMLNode) + paramString2 + paramString3); 
/* 114 */     return uRL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL getRequiredURL(String paramString1, XMLNode paramXMLNode, String paramString2, String paramString3) throws BadFieldException, MissingFieldException {
/* 121 */     return getRequiredURL(paramString1, null, paramXMLNode, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isElementPath(XMLNode paramXMLNode, String paramString) {
/* 126 */     return (findElementPath(paramXMLNode, paramString) != null);
/*     */   }
/*     */   
/*     */   public static URL getElementURL(String paramString1, XMLNode paramXMLNode, String paramString2) throws BadFieldException {
/* 130 */     String str = getElementContents(paramXMLNode, paramString2);
/*     */     try {
/* 132 */       return new URL(str);
/* 133 */     } catch (MalformedURLException malformedURLException) {
/* 134 */       throw new BadFieldException(paramString1, getPathString(paramXMLNode) + paramString2, str);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getPathString(XMLNode paramXMLNode) {
/* 140 */     return (paramXMLNode == null || !paramXMLNode.isElement()) ? "" : (getPathString(paramXMLNode.getParent()) + "<" + paramXMLNode.getName() + ">");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getElementContentsWithAttribute(XMLNode paramXMLNode, String paramString1, String paramString2, String paramString3, String paramString4) throws BadFieldException, MissingFieldException {
/* 148 */     XMLNode xMLNode = getElementWithAttribute(paramXMLNode, paramString1, paramString2, paramString3);
/* 149 */     if (xMLNode == null) return paramString4; 
/* 150 */     return getElementContents(xMLNode, "", paramString4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL getAttributeURLWithAttribute(String paramString1, XMLNode paramXMLNode, String paramString2, String paramString3, String paramString4, String paramString5, URL paramURL) throws BadFieldException, MissingFieldException {
/* 156 */     XMLNode xMLNode = getElementWithAttribute(paramXMLNode, paramString2, paramString3, paramString4);
/* 157 */     if (xMLNode == null) return paramURL; 
/* 158 */     URL uRL = getAttributeURL(paramString1, xMLNode, "", paramString5);
/* 159 */     if (uRL == null) return paramURL; 
/* 160 */     return uRL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLNode getElementWithAttribute(XMLNode paramXMLNode, String paramString1, String paramString2, String paramString3) throws BadFieldException, MissingFieldException {
/* 169 */     XMLNode[] arrayOfXMLNode = { null };
/* 170 */     visitElements(paramXMLNode, paramString1, new ElementVisitor(arrayOfXMLNode, paramString2, paramString3) { private final XMLNode[] val$result;
/*     */           public void visitElement(XMLNode param1XMLNode) throws BadFieldException, MissingFieldException {
/* 172 */             if (this.val$result[0] == null && param1XMLNode.getAttribute(this.val$attr).equals(this.val$val))
/* 173 */               this.val$result[0] = param1XMLNode; 
/*     */           }
/*     */           private final String val$attr; private final String val$val; }
/*     */       );
/* 177 */     return arrayOfXMLNode[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getElementContents(XMLNode paramXMLNode, String paramString) {
/* 182 */     return getElementContents(paramXMLNode, paramString, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getElementContents(XMLNode paramXMLNode, String paramString1, String paramString2) {
/* 189 */     XMLNode xMLNode1 = findElementPath(paramXMLNode, paramString1);
/* 190 */     if (xMLNode1 == null) return paramString2; 
/* 191 */     XMLNode xMLNode2 = xMLNode1.getNested();
/* 192 */     if (xMLNode2 != null && !xMLNode2.isElement()) return xMLNode2.getName(); 
/* 193 */     return paramString2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLNode findElementPath(XMLNode paramXMLNode, String paramString) {
/* 202 */     if (paramXMLNode == null) return null;
/*     */     
/* 204 */     if (paramString == null || paramString.length() == 0) return paramXMLNode;
/*     */ 
/*     */     
/* 207 */     int i = paramString.indexOf('>');
/* 208 */     if (paramString.charAt(0) != '<') {
/* 209 */       throw new IllegalArgumentException("bad path. Missing begin tag");
/*     */     }
/* 211 */     if (i == -1) {
/* 212 */       throw new IllegalArgumentException("bad path. Missing end tag");
/*     */     }
/* 214 */     String str1 = paramString.substring(1, i);
/* 215 */     String str2 = paramString.substring(i + 1);
/* 216 */     return findElementPath(findChildElement(paramXMLNode, str1), str2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static XMLNode findChildElement(XMLNode paramXMLNode, String paramString) {
/* 221 */     XMLNode xMLNode = paramXMLNode.getNested();
/* 222 */     while (xMLNode != null) {
/* 223 */       if (xMLNode.isElement() && xMLNode.getName().equals(paramString)) return xMLNode; 
/* 224 */       xMLNode = xMLNode.getNext();
/*     */     } 
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class ElementVisitor
/*     */   {
/*     */     public abstract void visitElement(XMLNode param1XMLNode) throws BadFieldException, MissingFieldException;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void visitElements(XMLNode paramXMLNode, String paramString, ElementVisitor paramElementVisitor) throws BadFieldException, MissingFieldException {
/* 240 */     int i = paramString.lastIndexOf('<');
/* 241 */     if (i == -1) {
/* 242 */       throw new IllegalArgumentException("bad path. Must contain atleast one tag");
/*     */     }
/*     */     
/* 245 */     if (paramString.length() == 0 || paramString.charAt(paramString.length() - 1) != '>') {
/* 246 */       throw new IllegalArgumentException("bad path. Must end with a >");
/*     */     }
/* 248 */     String str1 = paramString.substring(0, i);
/* 249 */     String str2 = paramString.substring(i + 1, paramString.length() - 1);
/*     */     
/* 251 */     XMLNode xMLNode1 = findElementPath(paramXMLNode, str1);
/* 252 */     if (xMLNode1 == null) {
/*     */       return;
/*     */     }
/* 255 */     XMLNode xMLNode2 = xMLNode1.getNested();
/* 256 */     while (xMLNode2 != null) {
/* 257 */       if (xMLNode2.isElement() && xMLNode2.getName().equals(str2)) {
/* 258 */         paramElementVisitor.visitElement(xMLNode2);
/*     */       }
/* 260 */       xMLNode2 = xMLNode2.getNext();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void visitChildrenElements(XMLNode paramXMLNode, ElementVisitor paramElementVisitor) throws BadFieldException, MissingFieldException {
/* 267 */     XMLNode xMLNode = paramXMLNode.getNested();
/* 268 */     while (xMLNode != null) {
/* 269 */       if (xMLNode.isElement()) paramElementVisitor.visitElement(xMLNode); 
/* 270 */       xMLNode = xMLNode.getNext();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\XMLUtils.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.parser.Messages;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProhibitedFeaturesFilter
/*     */   extends XMLFilterImpl
/*     */ {
/*  27 */   private Locator locator = null;
/*     */ 
/*     */ 
/*     */   
/*  31 */   private ErrorHandler errorHandler = null;
/*     */ 
/*     */   
/*     */   private boolean strict = true;
/*     */ 
/*     */   
/*     */   private static final int REPORT_DISABLED_IN_STRICT_MODE = 1;
/*     */ 
/*     */   
/*     */   private static final int REPORT_RESTRICTED = 2;
/*     */ 
/*     */   
/*     */   private static final int REPORT_WARN = 3;
/*     */   
/*     */   private static final int REPORT_UNSUPPORTED_ERROR = 4;
/*     */ 
/*     */   
/*     */   public ProhibitedFeaturesFilter(ErrorHandler eh, boolean strict) {
/*  49 */     this.errorHandler = eh;
/*  50 */     this.strict = strict;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/*  61 */     if (this.strict && localName.equals("any") && "skip".equals(atts.getValue("processContents"))) {
/*     */ 
/*     */ 
/*     */       
/*  65 */       report(3, "ProhibitedFeaturesFilter.ProcessContentsAttrOfAny", this.locator);
/*     */     }
/*  67 */     else if (localName.equals("anyAttribute")) {
/*     */       
/*  69 */       report(2, (this.strict == true) ? "ProhibitedFeaturesFilter.AnyAttr" : "ProhibitedFeaturesFilter.AnyAttrWarning", this.locator);
/*     */     
/*     */     }
/*  72 */     else if (localName.equals("complexType")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  87 */       if (atts.getValue("block") != null && !parseComplexTypeBlockAttr(atts.getValue("block")))
/*     */       {
/*  89 */         report(3, "ProhibitedFeaturesFilter.BlockAttrOfComplexType", this.locator);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  95 */       if (atts.getValue("final") != null) {
/*  96 */         report(3, "ProhibitedFeaturesFilter.FinalAttrOfComplexType", this.locator);
/*     */       
/*     */       }
/*     */     }
/* 100 */     else if (localName.equals("element")) {
/*     */ 
/*     */       
/* 103 */       if (atts.getValue("abstract") != null && parsedBooleanTrue(atts.getValue("abstract")))
/*     */       {
/*     */         
/* 106 */         report(1, "ProhibitedFeaturesFilter.AbstractAttrOfElement", this.locator);
/*     */       }
/*     */       
/* 109 */       if (atts.getValue("substitutionGroup") != null && !atts.getValue("substitutionGroup").trim().equals(""))
/*     */       {
/*     */         
/* 112 */         report(1, "ProhibitedFeaturesFilter.SubstitutionGroupAttrOfElement", this.locator);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       if (atts.getValue("final") != null) {
/* 119 */         report(3, "ProhibitedFeaturesFilter.FinalAttrOfElement", this.locator);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 125 */       if (atts.getValue("block") != null && !parseElementBlockAttr(atts.getValue("block")))
/*     */       {
/* 127 */         report(3, "ProhibitedFeaturesFilter.BlockAttrOfElement", this.locator);
/*     */       
/*     */       }
/*     */     }
/* 131 */     else if (localName.equals("key")) {
/*     */       
/* 133 */       report(2, (this.strict == true) ? "ProhibitedFeaturesFilter.Key" : "ProhibitedFeaturesFilter.KeyWarning", this.locator);
/*     */     
/*     */     }
/* 136 */     else if (localName.equals("keyref")) {
/*     */       
/* 138 */       report(2, (this.strict == true) ? "ProhibitedFeaturesFilter.Keyref" : "ProhibitedFeaturesFilter.KeyrefWarning", this.locator);
/*     */     
/*     */     }
/* 141 */     else if (localName.equals("notation")) {
/*     */       
/* 143 */       report(2, (this.strict == true) ? "ProhibitedFeaturesFilter.Notation" : "ProhibitedFeaturesFilter.NotationWarning", this.locator);
/*     */     
/*     */     }
/* 146 */     else if (localName.equals("unique")) {
/*     */       
/* 148 */       report(2, (this.strict == true) ? "ProhibitedFeaturesFilter.Unique" : "ProhibitedFeaturesFilter.UniqueWarning", this.locator);
/*     */     
/*     */     }
/* 151 */     else if (localName.equals("redefine")) {
/*     */ 
/*     */       
/* 154 */       report(4, "ProhibitedFeaturesFilter.Redefine", this.locator);
/*     */     
/*     */     }
/* 157 */     else if (localName.equals("schema")) {
/*     */       
/* 159 */       if (atts.getValue("blockDefault") != null && !atts.getValue("blockDefault").equals("#all"))
/*     */       {
/* 161 */         report(3, "ProhibitedFeaturesFilter.BlockDefaultAttrOfSchema", this.locator);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       if (atts.getValue("finalDefault") != null) {
/* 168 */         report(3, "ProhibitedFeaturesFilter.FinalDefaultAttrOfSchema", this.locator);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 173 */       if (atts.getValue("http://java.sun.com/xml/ns/jaxb", "extensionBindingPrefixes") != null) {
/* 174 */         report(1, "ProhibitedFeaturesFilter.ExtensionBindingPrefixesOfSchema", this.locator);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     super.startElement(uri, localName, qName, atts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 193 */     super.setDocumentLocator(locator);
/* 194 */     this.locator = locator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void report(int type, String msg, Locator loc) throws SAXException {
/* 205 */     SAXParseException spe = null;
/*     */     
/* 207 */     if (type == 2 && !this.strict)
/* 208 */       type = 3; 
/* 209 */     if (type == 1 && !this.strict) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     switch (type) {
/*     */       case 1:
/*     */       case 2:
/* 221 */         spe = new SAXParseException(Messages.format("ProhibitedFeaturesFilter.StrictModePrefix") + "\n\t" + Messages.format(msg), loc);
/*     */ 
/*     */         
/* 224 */         this.errorHandler.error(spe);
/* 225 */         throw spe;
/*     */       case 3:
/* 227 */         spe = new SAXParseException(Messages.format("ProhibitedFeaturesFilter.WarningPrefix") + " " + Messages.format(msg), loc);
/*     */ 
/*     */         
/* 230 */         this.errorHandler.warning(spe);
/*     */         return;
/*     */       case 4:
/* 233 */         spe = new SAXParseException(Messages.format("ProhibitedFeaturesFilter.UnsupportedPrefix") + " " + Messages.format(msg), loc);
/*     */ 
/*     */         
/* 236 */         this.errorHandler.error(spe);
/* 237 */         throw spe;
/*     */     } 
/* 239 */     throw new JAXBAssertionError();
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
/*     */   private static boolean parsedBooleanTrue(String lexicalBoolean) throws SAXParseException {
/* 253 */     if (lexicalBoolean.equals("true") || lexicalBoolean.equals("1")) {
/* 254 */       return true;
/*     */     }
/* 256 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean parseElementBlockAttr(String lexicalBlock) {
/* 267 */     if (lexicalBlock.equals("#all") || (lexicalBlock.indexOf("restriction") != -1 && lexicalBlock.indexOf("extension") != -1 && lexicalBlock.indexOf("substitution") != -1))
/*     */     {
/*     */ 
/*     */       
/* 271 */       return true;
/*     */     }
/* 273 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean parseComplexTypeBlockAttr(String lexicalBlock) {
/* 284 */     if (lexicalBlock.equals("#all") || (lexicalBlock.indexOf("restriction") != -1 && lexicalBlock.indexOf("extension") != -1))
/*     */     {
/*     */       
/* 287 */       return true;
/*     */     }
/* 289 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\parser\ProhibitedFeaturesFilter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
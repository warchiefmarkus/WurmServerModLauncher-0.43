/*     */ package org.fourthline.cling.binding.xml;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.seamless.util.Exceptions;
/*     */ import org.seamless.xml.XmlPullParserUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RecoveringUDA10DeviceDescriptorBinderImpl
/*     */   extends UDA10DeviceDescriptorBinderImpl
/*     */ {
/*  35 */   private static Logger log = Logger.getLogger(RecoveringUDA10DeviceDescriptorBinderImpl.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   public <D extends org.fourthline.cling.model.meta.Device> D describe(D undescribedDevice, String descriptorXml) throws DescriptorBindingException, ValidationException {
/*  40 */     D device = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  45 */       if (descriptorXml != null)
/*  46 */         descriptorXml = descriptorXml.trim(); 
/*  47 */       device = super.describe(undescribedDevice, descriptorXml);
/*  48 */       return device;
/*  49 */     } catch (DescriptorBindingException ex) {
/*  50 */       log.warning("Regular parsing failed: " + Exceptions.unwrap(ex).getMessage());
/*  51 */       DescriptorBindingException originalException = ex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  57 */       String fixedXml = fixGarbageLeadingChars(descriptorXml);
/*  58 */       if (fixedXml != null) {
/*     */         try {
/*  60 */           device = super.describe(undescribedDevice, fixedXml);
/*  61 */           return device;
/*  62 */         } catch (DescriptorBindingException descriptorBindingException) {
/*  63 */           log.warning("Removing leading garbage didn't work: " + Exceptions.unwrap(descriptorBindingException).getMessage());
/*     */         } 
/*     */       }
/*     */       
/*  67 */       fixedXml = fixGarbageTrailingChars(descriptorXml, originalException);
/*  68 */       if (fixedXml != null) {
/*     */         try {
/*  70 */           device = super.describe(undescribedDevice, fixedXml);
/*  71 */           return device;
/*  72 */         } catch (DescriptorBindingException descriptorBindingException) {
/*  73 */           log.warning("Removing trailing garbage didn't work: " + Exceptions.unwrap(descriptorBindingException).getMessage());
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*  78 */       DescriptorBindingException lastException = originalException;
/*  79 */       fixedXml = descriptorXml;
/*  80 */       for (int retryCount = 0; retryCount < 5; ) {
/*  81 */         fixedXml = fixMissingNamespaces(fixedXml, lastException);
/*  82 */         if (fixedXml != null) {
/*     */           try {
/*  84 */             device = super.describe(undescribedDevice, fixedXml);
/*  85 */             return device;
/*  86 */           } catch (DescriptorBindingException descriptorBindingException) {
/*  87 */             log.warning("Fixing namespace prefix didn't work: " + Exceptions.unwrap(descriptorBindingException).getMessage());
/*  88 */             lastException = descriptorBindingException;
/*     */           } 
/*     */           
/*     */           retryCount++;
/*     */         } 
/*     */       } 
/*     */       
/*  95 */       fixedXml = XmlPullParserUtils.fixXMLEntities(descriptorXml);
/*  96 */       if (!fixedXml.equals(descriptorXml)) {
/*     */         try {
/*  98 */           device = super.describe(undescribedDevice, fixedXml);
/*  99 */           return device;
/* 100 */         } catch (DescriptorBindingException descriptorBindingException) {
/* 101 */           log.warning("Fixing XML entities didn't work: " + Exceptions.unwrap(descriptorBindingException).getMessage());
/*     */         } 
/*     */       }
/*     */       
/* 105 */       handleInvalidDescriptor(descriptorXml, originalException);
/*     */     }
/* 107 */     catch (ValidationException ex) {
/* 108 */       device = handleInvalidDevice(descriptorXml, device, ex);
/* 109 */       if (device != null)
/* 110 */         return device; 
/*     */     } 
/* 112 */     throw new IllegalStateException("No device produced, did you swallow exceptions in your subclass?");
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
/*     */   private String fixGarbageLeadingChars(String descriptorXml) {
/* 133 */     int index = descriptorXml.indexOf("<?xml");
/* 134 */     if (index == -1) return descriptorXml; 
/* 135 */     return descriptorXml.substring(index);
/*     */   }
/*     */   
/*     */   protected String fixGarbageTrailingChars(String descriptorXml, DescriptorBindingException ex) {
/* 139 */     int index = descriptorXml.indexOf("</root>");
/* 140 */     if (index == -1) {
/* 141 */       log.warning("No closing </root> element in descriptor");
/* 142 */       return null;
/*     */     } 
/* 144 */     if (descriptorXml.length() != index + "</root>".length()) {
/* 145 */       log.warning("Detected garbage characters after <root> node, removing");
/* 146 */       return descriptorXml.substring(0, index) + "</root>";
/*     */     } 
/* 148 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String fixMissingNamespaces(String descriptorXml, DescriptorBindingException ex) {
/* 156 */     Throwable cause = ex.getCause();
/* 157 */     if (!(cause instanceof org.xml.sax.SAXParseException) && !(cause instanceof org.seamless.xml.ParserException))
/* 158 */       return null; 
/* 159 */     String message = cause.getMessage();
/* 160 */     if (message == null) {
/* 161 */       return null;
/*     */     }
/* 163 */     Pattern pattern = Pattern.compile("The prefix \"(.*)\" for element");
/* 164 */     Matcher matcher = pattern.matcher(message);
/* 165 */     if (!matcher.find() || matcher.groupCount() != 1) {
/* 166 */       pattern = Pattern.compile("undefined prefix: ([^ ]*)");
/* 167 */       matcher = pattern.matcher(message);
/* 168 */       if (!matcher.find() || matcher.groupCount() != 1) {
/* 169 */         return null;
/*     */       }
/*     */     } 
/* 172 */     String missingNS = matcher.group(1);
/* 173 */     log.warning("Fixing missing namespace declaration for: " + missingNS);
/*     */ 
/*     */     
/* 176 */     pattern = Pattern.compile("<root([^>]*)");
/* 177 */     matcher = pattern.matcher(descriptorXml);
/* 178 */     if (!matcher.find() || matcher.groupCount() != 1) {
/* 179 */       log.fine("Could not find <root> element attributes");
/* 180 */       return null;
/*     */     } 
/*     */     
/* 183 */     String rootAttributes = matcher.group(1);
/* 184 */     log.fine("Preserving existing <root> element attributes/namespace declarations: " + matcher.group(0));
/*     */ 
/*     */     
/* 187 */     pattern = Pattern.compile("<root[^>]*>(.*)</root>", 32);
/* 188 */     matcher = pattern.matcher(descriptorXml);
/* 189 */     if (!matcher.find() || matcher.groupCount() != 1) {
/* 190 */       log.fine("Could not extract body of <root> element");
/* 191 */       return null;
/*     */     } 
/*     */     
/* 194 */     String rootBody = matcher.group(1);
/*     */ 
/*     */     
/* 197 */     return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root " + 
/*     */       
/* 199 */       String.format(Locale.ROOT, "xmlns:%s=\"urn:schemas-dlna-org:device-1-0\"", new Object[] { missingNS }) + rootAttributes + ">" + rootBody + "</root>";
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
/*     */   protected void handleInvalidDescriptor(String xml, DescriptorBindingException exception) throws DescriptorBindingException {
/* 222 */     throw exception;
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
/*     */   protected <D extends org.fourthline.cling.model.meta.Device> D handleInvalidDevice(String xml, D device, ValidationException exception) throws ValidationException {
/* 247 */     throw exception;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\xml\RecoveringUDA10DeviceDescriptorBinderImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
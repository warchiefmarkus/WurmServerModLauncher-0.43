/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import javax.enterprise.inject.Alternative;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.model.action.ActionArgumentValue;
/*     */ import org.fourthline.cling.model.action.ActionException;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.message.control.ActionMessage;
/*     */ import org.fourthline.cling.model.message.control.ActionRequestMessage;
/*     */ import org.fourthline.cling.model.message.control.ActionResponseMessage;
/*     */ import org.fourthline.cling.model.meta.ActionArgument;
/*     */ import org.fourthline.cling.model.types.ErrorCode;
/*     */ import org.fourthline.cling.transport.spi.SOAPActionProcessor;
/*     */ import org.seamless.xml.XmlPullParserUtils;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Alternative
/*     */ public class PullSOAPActionProcessorImpl
/*     */   extends SOAPActionProcessorImpl
/*     */ {
/*  50 */   protected static Logger log = Logger.getLogger(SOAPActionProcessor.class.getName());
/*     */   
/*     */   public void readBody(ActionRequestMessage requestMessage, ActionInvocation actionInvocation) throws UnsupportedDataException {
/*  53 */     String body = getMessageBody((ActionMessage)requestMessage);
/*     */     try {
/*  55 */       XmlPullParser xpp = XmlPullParserUtils.createParser(body);
/*  56 */       readBodyRequest(xpp, requestMessage, actionInvocation);
/*  57 */     } catch (Exception ex) {
/*  58 */       throw new UnsupportedDataException("Can't transform message payload: " + ex, ex, body);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void readBody(ActionResponseMessage responseMsg, ActionInvocation actionInvocation) throws UnsupportedDataException {
/*  63 */     String body = getMessageBody((ActionMessage)responseMsg);
/*     */     try {
/*  65 */       XmlPullParser xpp = XmlPullParserUtils.createParser(body);
/*  66 */       readBodyElement(xpp);
/*  67 */       readBodyResponse(xpp, actionInvocation);
/*  68 */     } catch (Exception ex) {
/*  69 */       throw new UnsupportedDataException("Can't transform message payload: " + ex, ex, body);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void readBodyElement(XmlPullParser xpp) throws Exception {
/*  74 */     XmlPullParserUtils.searchTag(xpp, "Body");
/*     */   }
/*     */   
/*     */   protected void readBodyRequest(XmlPullParser xpp, ActionRequestMessage requestMessage, ActionInvocation actionInvocation) throws Exception {
/*  78 */     XmlPullParserUtils.searchTag(xpp, actionInvocation.getAction().getName());
/*  79 */     readActionInputArguments(xpp, actionInvocation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readBodyResponse(XmlPullParser xpp, ActionInvocation actionInvocation) throws Exception {
/*     */     int event;
/*     */     do {
/*  86 */       event = xpp.next();
/*  87 */       if (event != 2)
/*  88 */         continue;  if (xpp.getName().equals("Fault")) {
/*  89 */         ActionException e = readFaultElement(xpp);
/*  90 */         actionInvocation.setFailure(e); return;
/*     */       } 
/*  92 */       if (xpp.getName().equals(actionInvocation.getAction().getName() + "Response")) {
/*  93 */         readActionOutputArguments(xpp, actionInvocation);
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*  99 */     } while (event != 1 && (event != 3 || !xpp.getName().equals("Body")));
/*     */     
/* 101 */     throw new ActionException(ErrorCode.ACTION_FAILED, 
/*     */         
/* 103 */         String.format("Action SOAP response do not contain %s element", new Object[] {
/* 104 */             actionInvocation.getAction().getName() + "Response"
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readActionInputArguments(XmlPullParser xpp, ActionInvocation actionInvocation) throws Exception {
/* 110 */     actionInvocation.setInput(readArgumentValues(xpp, actionInvocation.getAction().getInputArguments()));
/*     */   }
/*     */   
/*     */   protected void readActionOutputArguments(XmlPullParser xpp, ActionInvocation actionInvocation) throws Exception {
/* 114 */     actionInvocation.setOutput(readArgumentValues(xpp, actionInvocation.getAction().getOutputArguments()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<String, String> getMatchingNodes(XmlPullParser xpp, ActionArgument[] args) throws Exception {
/*     */     int event;
/* 120 */     List<String> names = new ArrayList<>();
/* 121 */     for (ActionArgument argument : args) {
/* 122 */       names.add(argument.getName().toUpperCase(Locale.ROOT));
/* 123 */       for (String alias : Arrays.<String>asList(argument.getAliases())) {
/* 124 */         names.add(alias.toUpperCase(Locale.ROOT));
/*     */       }
/*     */     } 
/*     */     
/* 128 */     Map<String, String> matches = new HashMap<>();
/*     */     
/* 130 */     String enclosingTag = xpp.getName();
/*     */ 
/*     */     
/*     */     do {
/* 134 */       event = xpp.next();
/* 135 */       if (event != 2 || !names.contains(xpp.getName().toUpperCase(Locale.ROOT)))
/* 136 */         continue;  matches.put(xpp.getName(), xpp.nextText());
/*     */ 
/*     */     
/*     */     }
/* 140 */     while (event != 1 && (event != 3 || !xpp.getName().equals(enclosingTag)));
/*     */     
/* 142 */     if (matches.size() < args.length) {
/* 143 */       throw new ActionException(ErrorCode.ARGUMENT_VALUE_INVALID, "Invalid number of input or output arguments in XML message, expected " + args.length + " but found " + matches
/*     */ 
/*     */           
/* 146 */           .size());
/*     */     }
/*     */     
/* 149 */     return matches;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ActionArgumentValue[] readArgumentValues(XmlPullParser xpp, ActionArgument[] args) throws Exception {
/* 154 */     Map<String, String> matches = getMatchingNodes(xpp, args);
/*     */     
/* 156 */     ActionArgumentValue[] values = new ActionArgumentValue[args.length];
/*     */     
/* 158 */     for (int i = 0; i < args.length; i++) {
/*     */       
/* 160 */       ActionArgument arg = args[i];
/* 161 */       String value = findActionArgumentValue(matches, arg);
/* 162 */       if (value == null) {
/* 163 */         throw new ActionException(ErrorCode.ARGUMENT_VALUE_INVALID, "Could not find argument '" + arg
/*     */             
/* 165 */             .getName() + "' node");
/*     */       }
/*     */       
/* 168 */       log.fine("Reading action argument: " + arg.getName());
/* 169 */       values[i] = createValue(arg, value);
/*     */     } 
/* 171 */     return values;
/*     */   }
/*     */   
/*     */   protected String findActionArgumentValue(Map<String, String> entries, ActionArgument arg) {
/* 175 */     for (Map.Entry<String, String> entry : entries.entrySet()) {
/* 176 */       if (arg.isNameOrAlias(entry.getKey())) return entry.getValue(); 
/*     */     } 
/* 178 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ActionException readFaultElement(XmlPullParser xpp) throws Exception {
/*     */     int event;
/* 184 */     String errorCode = null;
/* 185 */     String errorDescription = null;
/*     */     
/* 187 */     XmlPullParserUtils.searchTag(xpp, "UPnPError");
/*     */ 
/*     */     
/*     */     do {
/* 191 */       event = xpp.next();
/* 192 */       if (event != 2)
/* 193 */         continue;  String tag = xpp.getName();
/* 194 */       if (tag.equals("errorCode")) {
/* 195 */         errorCode = xpp.nextText();
/* 196 */       } else if (tag.equals("errorDescription")) {
/* 197 */         errorDescription = xpp.nextText();
/*     */       }
/*     */     
/*     */     }
/* 201 */     while (event != 1 && (event != 3 || !xpp.getName().equals("UPnPError")));
/*     */     
/* 203 */     if (errorCode != null) {
/*     */       try {
/* 205 */         int numericCode = Integer.valueOf(errorCode).intValue();
/* 206 */         ErrorCode standardErrorCode = ErrorCode.getByCode(numericCode);
/* 207 */         if (standardErrorCode != null) {
/* 208 */           log.fine("Reading fault element: " + standardErrorCode.getCode() + " - " + errorDescription);
/* 209 */           return new ActionException(standardErrorCode, errorDescription, false);
/*     */         } 
/* 211 */         log.fine("Reading fault element: " + numericCode + " - " + errorDescription);
/* 212 */         return new ActionException(numericCode, errorDescription);
/*     */       }
/* 214 */       catch (NumberFormatException ex) {
/* 215 */         throw new RuntimeException("Error code was not a number");
/*     */       } 
/*     */     }
/*     */     
/* 219 */     throw new RuntimeException("Received fault element but no error code");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\PullSOAPActionProcessorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
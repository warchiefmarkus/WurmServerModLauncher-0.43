package org.intellij.lang.annotations;

@Pattern("(?:[^%]|%%|(?:%(?:\\d+\\$)?(?:[-#+ 0,(<]*)?(?:\\d+)?(?:\\.\\d+)?(?:[tT])?(?:[a-zA-Z%])))*")
public @interface PrintFormat {}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\intellij\lang\annotations\PrintFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Regex
/*     */ {
/*     */   private static final String UNICODE_SPACES = "[\\u0009-\\u000d\\u0020\\u0085\\u00a0\\u1680\\u180E\\u2000-\\u200a\\u2028\\u2029\\u202F\\u205F\\u3000]";
/*  26 */   private static String LATIN_ACCENTS_CHARS = "\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff\\u0100-\\u024f\\u0253\\u0254\\u0256\\u0257\\u0259\\u025b\\u0263\\u0268\\u026f\\u0272\\u0289\\u028b\\u02bb\\u0300-\\u036f\\u1e00-\\u1eff";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  32 */   private static final String HASHTAG_ALPHA_CHARS = "a-z" + LATIN_ACCENTS_CHARS + 
/*  33 */     "\\u0400-\\u04ff\\u0500-\\u0527" + 
/*  34 */     "\\u2de0-\\u2dff\\ua640-\\ua69f" + 
/*  35 */     "\\u0591-\\u05bf\\u05c1-\\u05c2\\u05c4-\\u05c5\\u05c7" + 
/*  36 */     "\\u05d0-\\u05ea\\u05f0-\\u05f4" + 
/*  37 */     "\\ufb1d-\\ufb28\\ufb2a-\\ufb36\\ufb38-\\ufb3c\\ufb3e\\ufb40-\\ufb41" + 
/*  38 */     "\\ufb43-\\ufb44\\ufb46-\\ufb4f" + 
/*  39 */     "\\u0610-\\u061a\\u0620-\\u065f\\u066e-\\u06d3\\u06d5-\\u06dc" + 
/*  40 */     "\\u06de-\\u06e8\\u06ea-\\u06ef\\u06fa-\\u06fc\\u06ff" + 
/*  41 */     "\\u0750-\\u077f\\u08a0\\u08a2-\\u08ac\\u08e4-\\u08fe" + 
/*  42 */     "\\ufb50-\\ufbb1\\ufbd3-\\ufd3d\\ufd50-\\ufd8f\\ufd92-\\ufdc7\\ufdf0-\\ufdfb" + 
/*  43 */     "\\ufe70-\\ufe74\\ufe76-\\ufefc" + 
/*  44 */     "\\u200c" + 
/*  45 */     "\\u0e01-\\u0e3a\\u0e40-\\u0e4e" + 
/*  46 */     "\\u1100-\\u11ff\\u3130-\\u3185\\uA960-\\uA97F\\uAC00-\\uD7AF\\uD7B0-\\uD7FF" + 
/*  47 */     "\\p{InHiragana}\\p{InKatakana}" + 
/*  48 */     "\\p{InCJKUnifiedIdeographs}" + 
/*  49 */     "\\u3003\\u3005\\u303b" + 
/*  50 */     "\\uff21-\\uff3a\\uff41-\\uff5a" + 
/*  51 */     "\\uff66-\\uff9f" + 
/*  52 */     "\\uffa1-\\uffdc";
/*  53 */   private static final String HASHTAG_ALPHA_NUMERIC_CHARS = "0-9\\uff10-\\uff19_" + HASHTAG_ALPHA_CHARS;
/*  54 */   private static final String HASHTAG_ALPHA = "[" + HASHTAG_ALPHA_CHARS + "]";
/*  55 */   private static final String HASHTAG_ALPHA_NUMERIC = "[" + HASHTAG_ALPHA_NUMERIC_CHARS + "]";
/*     */ 
/*     */   
/*     */   private static final String URL_VALID_PRECEEDING_CHARS = "(?:[^A-Z0-9@＠$#＃‪-‮]|^)";
/*     */   
/*  60 */   private static final String URL_VALID_CHARS = "[\\p{Alnum}" + LATIN_ACCENTS_CHARS + "]";
/*  61 */   private static final String URL_VALID_SUBDOMAIN = "(?:(?:" + URL_VALID_CHARS + "[" + URL_VALID_CHARS + "\\-_]*)?" + URL_VALID_CHARS + "\\.)";
/*  62 */   private static final String URL_VALID_DOMAIN_NAME = "(?:(?:" + URL_VALID_CHARS + "[" + URL_VALID_CHARS + "\\-]*)?" + URL_VALID_CHARS + "\\.)";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String URL_VALID_UNICODE_CHARS = "[.[^\\p{Punct}\\s\\p{Z}\\p{InGeneralPunctuation}]]";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String URL_VALID_GTLD = "(?:(?:aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|xxx)(?=\\P{Alnum}|$))";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String URL_VALID_CCTLD = "(?:(?:ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|za|zm|zw)(?=\\P{Alnum}|$))";
/*     */ 
/*     */   
/*     */   private static final String URL_PUNYCODE = "(?:xn--[0-9a-z]+)";
/*     */ 
/*     */   
/*  80 */   private static final String URL_VALID_DOMAIN = "(?:" + 
/*  81 */     URL_VALID_SUBDOMAIN + "+" + URL_VALID_DOMAIN_NAME + 
/*  82 */     "(?:" + "(?:(?:aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|xxx)(?=\\P{Alnum}|$))" + "|" + "(?:(?:ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|za|zm|zw)(?=\\P{Alnum}|$))" + "|" + "(?:xn--[0-9a-z]+)" + ")" + 
/*  83 */     ")" + 
/*  84 */     "|(?:" + 
/*  85 */     URL_VALID_DOMAIN_NAME + 
/*  86 */     "(?:" + "(?:(?:aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|xxx)(?=\\P{Alnum}|$))" + "|" + "(?:xn--[0-9a-z]+)" + ")" + 
/*  87 */     ")" + 
/*  88 */     "|(?:" + "(?<=https?://)" + 
/*  89 */     "(?:" + 
/*  90 */     "(?:" + URL_VALID_DOMAIN_NAME + "(?:(?:ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|za|zm|zw)(?=\\P{Alnum}|$))" + ")" + 
/*  91 */     "|(?:" + 
/*  92 */     "[.[^\\p{Punct}\\s\\p{Z}\\p{InGeneralPunctuation}]]" + "+\\." + 
/*  93 */     "(?:" + "(?:(?:aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|xxx)(?=\\P{Alnum}|$))" + "|" + "(?:(?:ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|za|zm|zw)(?=\\P{Alnum}|$))" + ")" + 
/*  94 */     ")" + 
/*  95 */     ")" + 
/*  96 */     ")" + 
/*  97 */     "|(?:" + 
/*  98 */     URL_VALID_DOMAIN_NAME + "(?:(?:ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|za|zm|zw)(?=\\P{Alnum}|$))" + "(?=/)" + 
/*  99 */     ")";
/*     */   
/*     */   private static final String URL_VALID_PORT_NUMBER = "[0-9]++";
/*     */   
/* 103 */   private static final String URL_VALID_GENERAL_PATH_CHARS = "[a-z0-9!\\*';:=\\+,.\\$/%#\\[\\]\\-_~\\|&@" + LATIN_ACCENTS_CHARS + "]";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   private static final String URL_BALANCED_PARENS = "\\(" + URL_VALID_GENERAL_PATH_CHARS + "+\\)";
/*     */ 
/*     */ 
/*     */   
/* 112 */   private static final String URL_VALID_PATH_ENDING_CHARS = "[a-z0-9=_#/\\-\\+" + LATIN_ACCENTS_CHARS + "]|(?:" + URL_BALANCED_PARENS + ")";
/*     */   
/* 114 */   private static final String URL_VALID_PATH = "(?:(?:" + 
/*     */     
/* 116 */     URL_VALID_GENERAL_PATH_CHARS + "*" + 
/* 117 */     "(?:" + URL_BALANCED_PARENS + URL_VALID_GENERAL_PATH_CHARS + "*)*" + 
/* 118 */     URL_VALID_PATH_ENDING_CHARS + 
/* 119 */     ")|(?:@" + URL_VALID_GENERAL_PATH_CHARS + "+/)" + 
/* 120 */     ")";
/*     */   
/*     */   private static final String URL_VALID_URL_QUERY_CHARS = "[a-z0-9!?\\*'\\(\\);:&=\\+\\$/%#\\[\\]\\-_\\.,~\\|@]";
/*     */   
/*     */   private static final String URL_VALID_URL_QUERY_ENDING_CHARS = "[a-z0-9_&=#/]";
/* 125 */   private static final String VALID_URL_PATTERN_STRING = "(((?:[^A-Z0-9@＠$#＃‪-‮]|^))((https?://)?(" + 
/*     */ 
/*     */ 
/*     */     
/* 129 */     URL_VALID_DOMAIN + ")" + 
/* 130 */     "(?::(" + "[0-9]++" + "))?" + 
/* 131 */     "(/" + 
/* 132 */     URL_VALID_PATH + "*+" + 
/* 133 */     ")?" + 
/* 134 */     "(\\?" + "[a-z0-9!?\\*'\\(\\);:&=\\+\\$/%#\\[\\]\\-_\\.,~\\|@]" + "*" + 
/* 135 */     "[a-z0-9_&=#/]" + ")?" + 
/* 136 */     ")" + 
/* 137 */     ")";
/*     */   
/* 139 */   private static String AT_SIGNS_CHARS = "@＠";
/*     */ 
/*     */   
/*     */   private static final String DOLLAR_SIGN_CHAR = "\\$";
/*     */   
/*     */   private static final String CASHTAG = "[a-z]{1,6}(?:[._][a-z]{1,2})?";
/*     */   
/* 146 */   public static final Pattern VALID_HASHTAG = Pattern.compile("(^|[^&" + HASHTAG_ALPHA_NUMERIC_CHARS + "])(#|＃)(" + HASHTAG_ALPHA_NUMERIC + "*" + HASHTAG_ALPHA + HASHTAG_ALPHA_NUMERIC + "*)", 2);
/*     */   public static final int VALID_HASHTAG_GROUP_BEFORE = 1;
/*     */   public static final int VALID_HASHTAG_GROUP_HASH = 2;
/*     */   public static final int VALID_HASHTAG_GROUP_TAG = 3;
/* 150 */   public static final Pattern INVALID_HASHTAG_MATCH_END = Pattern.compile("^(?:[#＃]|://)");
/* 151 */   public static final Pattern RTL_CHARACTERS = Pattern.compile("[؀-ۿݐ-ݿ֐-׿ﹰ-﻿]");
/*     */   
/* 153 */   public static final Pattern AT_SIGNS = Pattern.compile("[" + AT_SIGNS_CHARS + "]");
/* 154 */   public static final Pattern VALID_MENTION_OR_LIST = Pattern.compile("([^a-z0-9_!#$%&*" + AT_SIGNS_CHARS + "]|^|RT:?)(" + AT_SIGNS + "+)([a-z0-9_]{1,20})(/[a-z][a-z0-9_\\-]{0,24})?", 2);
/*     */   
/*     */   public static final int VALID_MENTION_OR_LIST_GROUP_BEFORE = 1;
/*     */   public static final int VALID_MENTION_OR_LIST_GROUP_AT = 2;
/*     */   public static final int VALID_MENTION_OR_LIST_GROUP_USERNAME = 3;
/*     */   public static final int VALID_MENTION_OR_LIST_GROUP_LIST = 4;
/* 160 */   public static final Pattern VALID_REPLY = Pattern.compile("^(?:[\\u0009-\\u000d\\u0020\\u0085\\u00a0\\u1680\\u180E\\u2000-\\u200a\\u2028\\u2029\\u202F\\u205F\\u3000])*" + AT_SIGNS + "([a-z0-9_]{1,20})", 2);
/*     */   
/*     */   public static final int VALID_REPLY_GROUP_USERNAME = 1;
/* 163 */   public static final Pattern INVALID_MENTION_MATCH_END = Pattern.compile("^(?:[" + AT_SIGNS_CHARS + LATIN_ACCENTS_CHARS + "]|://)");
/*     */   
/* 165 */   public static final Pattern VALID_URL = Pattern.compile(VALID_URL_PATTERN_STRING, 2);
/*     */   
/*     */   public static final int VALID_URL_GROUP_ALL = 1;
/*     */   public static final int VALID_URL_GROUP_BEFORE = 2;
/*     */   public static final int VALID_URL_GROUP_URL = 3;
/*     */   public static final int VALID_URL_GROUP_PROTOCOL = 4;
/*     */   public static final int VALID_URL_GROUP_DOMAIN = 5;
/*     */   public static final int VALID_URL_GROUP_PORT = 6;
/*     */   public static final int VALID_URL_GROUP_PATH = 7;
/*     */   public static final int VALID_URL_GROUP_QUERY_STRING = 8;
/* 175 */   public static final Pattern VALID_TCO_URL = Pattern.compile("^https?:\\/\\/t\\.co\\/[a-z0-9]+", 2);
/* 176 */   public static final Pattern INVALID_URL_WITHOUT_PROTOCOL_MATCH_BEGIN = Pattern.compile("[-_./]$");
/*     */   
/* 178 */   public static final Pattern VALID_CASHTAG = Pattern.compile("(^|[\\u0009-\\u000d\\u0020\\u0085\\u00a0\\u1680\\u180E\\u2000-\\u200a\\u2028\\u2029\\u202F\\u205F\\u3000])(\\$)([a-z]{1,6}(?:[._][a-z]{1,2})?)(?=$|\\s|\\p{Punct})", 2);
/*     */   public static final int VALID_CASHTAG_GROUP_BEFORE = 1;
/*     */   public static final int VALID_CASHTAG_GROUP_DOLLAR = 2;
/*     */   public static final int VALID_CASHTAG_GROUP_CASHTAG = 3;
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\Regex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
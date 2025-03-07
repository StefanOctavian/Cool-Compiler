// Generated from d:/Stefan/Facultate/Anul IV/CPL/teme/tema3/src/cool/lexer/CoolLexer.g4 by ANTLR 4.13.1

package cool.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;	

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class CoolLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ERROR=1, WS=2, INTEGER=3, CASE=4, CLASS=5, ELSE=6, ESAC=7, FALSE=8, FI=9, 
		IF=10, IN=11, INHERITS=12, ISVOID=13, LET=14, LOOP=15, NEW=16, NOT=17, 
		OF=18, POOL=19, THEN=20, TRUE=21, WHILE=22, ID=23, TYPE=24, LPAREN=25, 
		RPAREN=26, DOT=27, EQ=28, LT=29, LEQ=30, NEG=31, DIV=32, MUL=33, SUB=34, 
		PLUS=35, DARROW=36, ASSIGN=37, COLON=38, SEMICOLON=39, COMMA=40, AT=41, 
		LBRACE=42, RBRACE=43, STRING=44, LINE_COMM=45, BLOCK_COMM=46, UNMATCHED_COMM_END=47, 
		INVALID_CHAR=48;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "A", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", 
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "DIGIT", 
			"INTEGER", "CASE", "CLASS", "ELSE", "ESAC", "FALSE", "FI", "IF", "IN", 
			"INHERITS", "ISVOID", "LET", "LOOP", "NEW", "NOT", "OF", "POOL", "THEN", 
			"TRUE", "WHILE", "UPPERCASE", "LOWERCASE", "LETTER", "ID", "TYPE", "LPAREN", 
			"RPAREN", "DOT", "EQ", "LT", "LEQ", "NEG", "DIV", "MUL", "SUB", "PLUS", 
			"DARROW", "ASSIGN", "COLON", "SEMICOLON", "COMMA", "AT", "LBRACE", "RBRACE", 
			"NEWLINE", "NULCHAR", "STRING", "LINE_COMM", "BLOCK_COMM", "UNMATCHED_COMM_END", 
			"INVALID_CHAR"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, "'('", "')'", "'.'", "'='", "'<'", "'<='", "'~'", "'/'", "'*'", 
			"'-'", "'+'", "'=>'", "'<-'", "':'", "';'", "','", "'@'", "'{'", "'}'", 
			null, null, null, "'*)'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ERROR", "WS", "INTEGER", "CASE", "CLASS", "ELSE", "ESAC", "FALSE", 
			"FI", "IF", "IN", "INHERITS", "ISVOID", "LET", "LOOP", "NEW", "NOT", 
			"OF", "POOL", "THEN", "TRUE", "WHILE", "ID", "TYPE", "LPAREN", "RPAREN", 
			"DOT", "EQ", "LT", "LEQ", "NEG", "DIV", "MUL", "SUB", "PLUS", "DARROW", 
			"ASSIGN", "COLON", "SEMICOLON", "COMMA", "AT", "LBRACE", "RBRACE", "STRING", 
			"LINE_COMM", "BLOCK_COMM", "UNMATCHED_COMM_END", "INVALID_CHAR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	    
	    private void raiseError(String msg) {
	        setText(msg);
	        setType(ERROR);
	    }

	    private String replaceEscapes(String str) {
	        return str.replaceAll("\\\\\"", "\"").replaceAll("\\\\n", "\n")
	                  .replaceAll("\\\\b", "\b").replaceAll("\\\\t", "\t")
	                  .replaceAll("\\\\f", "\f").replaceAll("\\\\(\\r?)\\n", "$1\n")
	                  .replaceAll("\\\\(.)", "$1");
	    }

	    private void rewriteString() {
	        if (getType() == ERROR) return;
	        String str = getText();
	        str = str.substring(1, str.length() - 1);
	        StringBuilder builder = new StringBuilder();
	        Pattern pattern = Pattern.compile("\\\\\\\\");
	        Matcher matcher = pattern.matcher(str);
	        while (matcher.find()) {
	            String substr = replaceEscapes(str.substring(0, matcher.start()));
	            str = str.substring(matcher.end());
	            builder.append(substr + "\\");
	            matcher = pattern.matcher(str);
	        }
	        builder.append(replaceEscapes(str));
	        if (builder.toString().length() > 1024) raiseError("String constant too long");
	        else setText(builder.toString());
	    }


	public CoolLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CoolLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 73:
			STRING_action((RuleContext)_localctx, actionIndex);
			break;
		case 75:
			BLOCK_COMM_action((RuleContext)_localctx, actionIndex);
			break;
		case 76:
			UNMATCHED_COMM_END_action((RuleContext)_localctx, actionIndex);
			break;
		case 77:
			INVALID_CHAR_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void STRING_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 raiseError("String contains null character"); 
			break;
		case 1:
			 raiseError("Unterminated string constant"); 
			break;
		case 2:
			 raiseError("EOF in string constant"); 
			break;
		case 3:
			 rewriteString(); 
			break;
		}
	}
	private void BLOCK_COMM_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 4:
			 skip(); 
			break;
		case 5:
			 raiseError("EOF in comment"); 
			break;
		}
	}
	private void UNMATCHED_COMM_END_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 6:
			 raiseError("Unmatched *)"); 
			break;
		}
	}
	private void INVALID_CHAR_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 7:
			 raiseError("Invalid character: " + getText()); 
			break;
		}
	}

	public static final String _serializedATN =
		"\u0004\u00000\u01c3\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"+
		"\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002"+
		"\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002"+
		"\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002"+
		"\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002"+
		"\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007"+
		"!\u0002\"\u0007\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007"+
		"&\u0002\'\u0007\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007"+
		"+\u0002,\u0007,\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u0007"+
		"0\u00021\u00071\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u0007"+
		"5\u00026\u00076\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007"+
		":\u0002;\u0007;\u0002<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007"+
		"?\u0002@\u0007@\u0002A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007"+
		"D\u0002E\u0007E\u0002F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007"+
		"I\u0002J\u0007J\u0002K\u0007K\u0002L\u0007L\u0002M\u0007M\u0001\u0000"+
		"\u0004\u0000\u009f\b\u0000\u000b\u0000\f\u0000\u00a0\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n"+
		"\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001"+
		"\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001"+
		"\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001"+
		"\u001a\u0001\u001a\u0001\u001b\u0004\u001b\u00da\b\u001b\u000b\u001b\f"+
		"\u001b\u00db\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001 \u0001 \u0001 "+
		"\u0001 \u0001 \u0001 \u0001!\u0001!\u0001!\u0001\"\u0001\"\u0001\"\u0001"+
		"#\u0001#\u0001#\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001"+
		"$\u0001$\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001&\u0001"+
		"&\u0001&\u0001&\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001(\u0001("+
		"\u0001(\u0001(\u0001)\u0001)\u0001)\u0001)\u0001*\u0001*\u0001*\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0001,\u0001,\u0001,\u0001,\u0001,\u0001"+
		"-\u0001-\u0001-\u0001-\u0001-\u0001.\u0001.\u0001.\u0001.\u0001.\u0001"+
		".\u0001/\u0001/\u00010\u00010\u00011\u00011\u00031\u0141\b1\u00012\u0001"+
		"2\u00012\u00012\u00052\u0147\b2\n2\f2\u014a\t2\u00013\u00013\u00013\u0001"+
		"3\u00053\u0150\b3\n3\f3\u0153\t3\u00014\u00014\u00015\u00015\u00016\u0001"+
		"6\u00017\u00017\u00018\u00018\u00019\u00019\u00019\u0001:\u0001:\u0001"+
		";\u0001;\u0001<\u0001<\u0001=\u0001=\u0001>\u0001>\u0001?\u0001?\u0001"+
		"?\u0001@\u0001@\u0001@\u0001A\u0001A\u0001B\u0001B\u0001C\u0001C\u0001"+
		"D\u0001D\u0001E\u0001E\u0001F\u0001F\u0001G\u0001G\u0001G\u0003G\u0181"+
		"\bG\u0001H\u0001H\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001"+
		"I\u0001I\u0005I\u018e\bI\nI\fI\u0191\tI\u0001I\u0001I\u0001I\u0001I\u0001"+
		"I\u0001I\u0003I\u0199\bI\u0001I\u0001I\u0001J\u0001J\u0001J\u0001J\u0005"+
		"J\u01a1\bJ\nJ\fJ\u01a4\tJ\u0001J\u0001J\u0001J\u0001J\u0001K\u0001K\u0001"+
		"K\u0001K\u0001K\u0005K\u01af\bK\nK\fK\u01b2\tK\u0001K\u0001K\u0001K\u0001"+
		"K\u0001K\u0001K\u0003K\u01ba\bK\u0001L\u0001L\u0001L\u0001L\u0001L\u0001"+
		"M\u0001M\u0001M\u0003\u018f\u01a2\u01b0\u0000N\u0001\u0002\u0003\u0000"+
		"\u0005\u0000\u0007\u0000\t\u0000\u000b\u0000\r\u0000\u000f\u0000\u0011"+
		"\u0000\u0013\u0000\u0015\u0000\u0017\u0000\u0019\u0000\u001b\u0000\u001d"+
		"\u0000\u001f\u0000!\u0000#\u0000%\u0000\'\u0000)\u0000+\u0000-\u0000/"+
		"\u00001\u00003\u00005\u00007\u00039\u0004;\u0005=\u0006?\u0007A\bC\tE"+
		"\nG\u000bI\fK\rM\u000eO\u000fQ\u0010S\u0011U\u0012W\u0013Y\u0014[\u0015"+
		"]\u0016_\u0000a\u0000c\u0000e\u0017g\u0018i\u0019k\u001am\u001bo\u001c"+
		"q\u001ds\u001eu\u001fw y!{\"}#\u007f$\u0081%\u0083&\u0085\'\u0087(\u0089"+
		")\u008b*\u008d+\u008f\u0000\u0091\u0000\u0093,\u0095-\u0097.\u0099/\u009b"+
		"0\u0001\u0000\u001d\u0002\u0000\t\r  \u0002\u0000AAaa\u0002\u0000CCcc"+
		"\u0002\u0000DDdd\u0002\u0000EEee\u0002\u0000FFff\u0002\u0000GGgg\u0002"+
		"\u0000HHhh\u0002\u0000IIii\u0002\u0000JJjj\u0002\u0000KKkk\u0002\u0000"+
		"LLll\u0002\u0000MMmm\u0002\u0000NNnn\u0002\u0000OOoo\u0002\u0000PPpp\u0002"+
		"\u0000QQqq\u0002\u0000RRrr\u0002\u0000SSss\u0002\u0000TTtt\u0002\u0000"+
		"UUuu\u0002\u0000VVvv\u0002\u0000WWww\u0002\u0000XXxx\u0002\u0000YYyy\u0002"+
		"\u0000ZZzz\u0001\u000009\u0001\u0000AZ\u0001\u0000az\u01b7\u0000\u0001"+
		"\u0001\u0000\u0000\u0000\u00007\u0001\u0000\u0000\u0000\u00009\u0001\u0000"+
		"\u0000\u0000\u0000;\u0001\u0000\u0000\u0000\u0000=\u0001\u0000\u0000\u0000"+
		"\u0000?\u0001\u0000\u0000\u0000\u0000A\u0001\u0000\u0000\u0000\u0000C"+
		"\u0001\u0000\u0000\u0000\u0000E\u0001\u0000\u0000\u0000\u0000G\u0001\u0000"+
		"\u0000\u0000\u0000I\u0001\u0000\u0000\u0000\u0000K\u0001\u0000\u0000\u0000"+
		"\u0000M\u0001\u0000\u0000\u0000\u0000O\u0001\u0000\u0000\u0000\u0000Q"+
		"\u0001\u0000\u0000\u0000\u0000S\u0001\u0000\u0000\u0000\u0000U\u0001\u0000"+
		"\u0000\u0000\u0000W\u0001\u0000\u0000\u0000\u0000Y\u0001\u0000\u0000\u0000"+
		"\u0000[\u0001\u0000\u0000\u0000\u0000]\u0001\u0000\u0000\u0000\u0000e"+
		"\u0001\u0000\u0000\u0000\u0000g\u0001\u0000\u0000\u0000\u0000i\u0001\u0000"+
		"\u0000\u0000\u0000k\u0001\u0000\u0000\u0000\u0000m\u0001\u0000\u0000\u0000"+
		"\u0000o\u0001\u0000\u0000\u0000\u0000q\u0001\u0000\u0000\u0000\u0000s"+
		"\u0001\u0000\u0000\u0000\u0000u\u0001\u0000\u0000\u0000\u0000w\u0001\u0000"+
		"\u0000\u0000\u0000y\u0001\u0000\u0000\u0000\u0000{\u0001\u0000\u0000\u0000"+
		"\u0000}\u0001\u0000\u0000\u0000\u0000\u007f\u0001\u0000\u0000\u0000\u0000"+
		"\u0081\u0001\u0000\u0000\u0000\u0000\u0083\u0001\u0000\u0000\u0000\u0000"+
		"\u0085\u0001\u0000\u0000\u0000\u0000\u0087\u0001\u0000\u0000\u0000\u0000"+
		"\u0089\u0001\u0000\u0000\u0000\u0000\u008b\u0001\u0000\u0000\u0000\u0000"+
		"\u008d\u0001\u0000\u0000\u0000\u0000\u0093\u0001\u0000\u0000\u0000\u0000"+
		"\u0095\u0001\u0000\u0000\u0000\u0000\u0097\u0001\u0000\u0000\u0000\u0000"+
		"\u0099\u0001\u0000\u0000\u0000\u0000\u009b\u0001\u0000\u0000\u0000\u0001"+
		"\u009e\u0001\u0000\u0000\u0000\u0003\u00a4\u0001\u0000\u0000\u0000\u0005"+
		"\u00a6\u0001\u0000\u0000\u0000\u0007\u00a8\u0001\u0000\u0000\u0000\t\u00aa"+
		"\u0001\u0000\u0000\u0000\u000b\u00ac\u0001\u0000\u0000\u0000\r\u00ae\u0001"+
		"\u0000\u0000\u0000\u000f\u00b0\u0001\u0000\u0000\u0000\u0011\u00b2\u0001"+
		"\u0000\u0000\u0000\u0013\u00b4\u0001\u0000\u0000\u0000\u0015\u00b6\u0001"+
		"\u0000\u0000\u0000\u0017\u00b8\u0001\u0000\u0000\u0000\u0019\u00ba\u0001"+
		"\u0000\u0000\u0000\u001b\u00bc\u0001\u0000\u0000\u0000\u001d\u00be\u0001"+
		"\u0000\u0000\u0000\u001f\u00c0\u0001\u0000\u0000\u0000!\u00c2\u0001\u0000"+
		"\u0000\u0000#\u00c4\u0001\u0000\u0000\u0000%\u00c6\u0001\u0000\u0000\u0000"+
		"\'\u00c8\u0001\u0000\u0000\u0000)\u00ca\u0001\u0000\u0000\u0000+\u00cc"+
		"\u0001\u0000\u0000\u0000-\u00ce\u0001\u0000\u0000\u0000/\u00d0\u0001\u0000"+
		"\u0000\u00001\u00d2\u0001\u0000\u0000\u00003\u00d4\u0001\u0000\u0000\u0000"+
		"5\u00d6\u0001\u0000\u0000\u00007\u00d9\u0001\u0000\u0000\u00009\u00dd"+
		"\u0001\u0000\u0000\u0000;\u00e2\u0001\u0000\u0000\u0000=\u00e8\u0001\u0000"+
		"\u0000\u0000?\u00ed\u0001\u0000\u0000\u0000A\u00f2\u0001\u0000\u0000\u0000"+
		"C\u00f8\u0001\u0000\u0000\u0000E\u00fb\u0001\u0000\u0000\u0000G\u00fe"+
		"\u0001\u0000\u0000\u0000I\u0101\u0001\u0000\u0000\u0000K\u010a\u0001\u0000"+
		"\u0000\u0000M\u0111\u0001\u0000\u0000\u0000O\u0115\u0001\u0000\u0000\u0000"+
		"Q\u011a\u0001\u0000\u0000\u0000S\u011e\u0001\u0000\u0000\u0000U\u0122"+
		"\u0001\u0000\u0000\u0000W\u0125\u0001\u0000\u0000\u0000Y\u012a\u0001\u0000"+
		"\u0000\u0000[\u012f\u0001\u0000\u0000\u0000]\u0134\u0001\u0000\u0000\u0000"+
		"_\u013a\u0001\u0000\u0000\u0000a\u013c\u0001\u0000\u0000\u0000c\u0140"+
		"\u0001\u0000\u0000\u0000e\u0142\u0001\u0000\u0000\u0000g\u014b\u0001\u0000"+
		"\u0000\u0000i\u0154\u0001\u0000\u0000\u0000k\u0156\u0001\u0000\u0000\u0000"+
		"m\u0158\u0001\u0000\u0000\u0000o\u015a\u0001\u0000\u0000\u0000q\u015c"+
		"\u0001\u0000\u0000\u0000s\u015e\u0001\u0000\u0000\u0000u\u0161\u0001\u0000"+
		"\u0000\u0000w\u0163\u0001\u0000\u0000\u0000y\u0165\u0001\u0000\u0000\u0000"+
		"{\u0167\u0001\u0000\u0000\u0000}\u0169\u0001\u0000\u0000\u0000\u007f\u016b"+
		"\u0001\u0000\u0000\u0000\u0081\u016e\u0001\u0000\u0000\u0000\u0083\u0171"+
		"\u0001\u0000\u0000\u0000\u0085\u0173\u0001\u0000\u0000\u0000\u0087\u0175"+
		"\u0001\u0000\u0000\u0000\u0089\u0177\u0001\u0000\u0000\u0000\u008b\u0179"+
		"\u0001\u0000\u0000\u0000\u008d\u017b\u0001\u0000\u0000\u0000\u008f\u0180"+
		"\u0001\u0000\u0000\u0000\u0091\u0182\u0001\u0000\u0000\u0000\u0093\u0184"+
		"\u0001\u0000\u0000\u0000\u0095\u019c\u0001\u0000\u0000\u0000\u0097\u01a9"+
		"\u0001\u0000\u0000\u0000\u0099\u01bb\u0001\u0000\u0000\u0000\u009b\u01c0"+
		"\u0001\u0000\u0000\u0000\u009d\u009f\u0007\u0000\u0000\u0000\u009e\u009d"+
		"\u0001\u0000\u0000\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0\u009e"+
		"\u0001\u0000\u0000\u0000\u00a0\u00a1\u0001\u0000\u0000\u0000\u00a1\u00a2"+
		"\u0001\u0000\u0000\u0000\u00a2\u00a3\u0006\u0000\u0000\u0000\u00a3\u0002"+
		"\u0001\u0000\u0000\u0000\u00a4\u00a5\u0007\u0001\u0000\u0000\u00a5\u0004"+
		"\u0001\u0000\u0000\u0000\u00a6\u00a7\u0007\u0002\u0000\u0000\u00a7\u0006"+
		"\u0001\u0000\u0000\u0000\u00a8\u00a9\u0007\u0003\u0000\u0000\u00a9\b\u0001"+
		"\u0000\u0000\u0000\u00aa\u00ab\u0007\u0004\u0000\u0000\u00ab\n\u0001\u0000"+
		"\u0000\u0000\u00ac\u00ad\u0007\u0005\u0000\u0000\u00ad\f\u0001\u0000\u0000"+
		"\u0000\u00ae\u00af\u0007\u0006\u0000\u0000\u00af\u000e\u0001\u0000\u0000"+
		"\u0000\u00b0\u00b1\u0007\u0007\u0000\u0000\u00b1\u0010\u0001\u0000\u0000"+
		"\u0000\u00b2\u00b3\u0007\b\u0000\u0000\u00b3\u0012\u0001\u0000\u0000\u0000"+
		"\u00b4\u00b5\u0007\t\u0000\u0000\u00b5\u0014\u0001\u0000\u0000\u0000\u00b6"+
		"\u00b7\u0007\n\u0000\u0000\u00b7\u0016\u0001\u0000\u0000\u0000\u00b8\u00b9"+
		"\u0007\u000b\u0000\u0000\u00b9\u0018\u0001\u0000\u0000\u0000\u00ba\u00bb"+
		"\u0007\f\u0000\u0000\u00bb\u001a\u0001\u0000\u0000\u0000\u00bc\u00bd\u0007"+
		"\r\u0000\u0000\u00bd\u001c\u0001\u0000\u0000\u0000\u00be\u00bf\u0007\u000e"+
		"\u0000\u0000\u00bf\u001e\u0001\u0000\u0000\u0000\u00c0\u00c1\u0007\u000f"+
		"\u0000\u0000\u00c1 \u0001\u0000\u0000\u0000\u00c2\u00c3\u0007\u0010\u0000"+
		"\u0000\u00c3\"\u0001\u0000\u0000\u0000\u00c4\u00c5\u0007\u0011\u0000\u0000"+
		"\u00c5$\u0001\u0000\u0000\u0000\u00c6\u00c7\u0007\u0012\u0000\u0000\u00c7"+
		"&\u0001\u0000\u0000\u0000\u00c8\u00c9\u0007\u0013\u0000\u0000\u00c9(\u0001"+
		"\u0000\u0000\u0000\u00ca\u00cb\u0007\u0014\u0000\u0000\u00cb*\u0001\u0000"+
		"\u0000\u0000\u00cc\u00cd\u0007\u0015\u0000\u0000\u00cd,\u0001\u0000\u0000"+
		"\u0000\u00ce\u00cf\u0007\u0016\u0000\u0000\u00cf.\u0001\u0000\u0000\u0000"+
		"\u00d0\u00d1\u0007\u0017\u0000\u0000\u00d10\u0001\u0000\u0000\u0000\u00d2"+
		"\u00d3\u0007\u0018\u0000\u0000\u00d32\u0001\u0000\u0000\u0000\u00d4\u00d5"+
		"\u0007\u0019\u0000\u0000\u00d54\u0001\u0000\u0000\u0000\u00d6\u00d7\u0007"+
		"\u001a\u0000\u0000\u00d76\u0001\u0000\u0000\u0000\u00d8\u00da\u00035\u001a"+
		"\u0000\u00d9\u00d8\u0001\u0000\u0000\u0000\u00da\u00db\u0001\u0000\u0000"+
		"\u0000\u00db\u00d9\u0001\u0000\u0000\u0000\u00db\u00dc\u0001\u0000\u0000"+
		"\u0000\u00dc8\u0001\u0000\u0000\u0000\u00dd\u00de\u0003\u0005\u0002\u0000"+
		"\u00de\u00df\u0003\u0003\u0001\u0000\u00df\u00e0\u0003%\u0012\u0000\u00e0"+
		"\u00e1\u0003\t\u0004\u0000\u00e1:\u0001\u0000\u0000\u0000\u00e2\u00e3"+
		"\u0003\u0005\u0002\u0000\u00e3\u00e4\u0003\u0017\u000b\u0000\u00e4\u00e5"+
		"\u0003\u0003\u0001\u0000\u00e5\u00e6\u0003%\u0012\u0000\u00e6\u00e7\u0003"+
		"%\u0012\u0000\u00e7<\u0001\u0000\u0000\u0000\u00e8\u00e9\u0003\t\u0004"+
		"\u0000\u00e9\u00ea\u0003\u0017\u000b\u0000\u00ea\u00eb\u0003%\u0012\u0000"+
		"\u00eb\u00ec\u0003\t\u0004\u0000\u00ec>\u0001\u0000\u0000\u0000\u00ed"+
		"\u00ee\u0003\t\u0004\u0000\u00ee\u00ef\u0003%\u0012\u0000\u00ef\u00f0"+
		"\u0003\u0003\u0001\u0000\u00f0\u00f1\u0003\u0005\u0002\u0000\u00f1@\u0001"+
		"\u0000\u0000\u0000\u00f2\u00f3\u0005f\u0000\u0000\u00f3\u00f4\u0003\u0003"+
		"\u0001\u0000\u00f4\u00f5\u0003\u0017\u000b\u0000\u00f5\u00f6\u0003%\u0012"+
		"\u0000\u00f6\u00f7\u0003\t\u0004\u0000\u00f7B\u0001\u0000\u0000\u0000"+
		"\u00f8\u00f9\u0003\u000b\u0005\u0000\u00f9\u00fa\u0003\u0011\b\u0000\u00fa"+
		"D\u0001\u0000\u0000\u0000\u00fb\u00fc\u0003\u0011\b\u0000\u00fc\u00fd"+
		"\u0003\u000b\u0005\u0000\u00fdF\u0001\u0000\u0000\u0000\u00fe\u00ff\u0003"+
		"\u0011\b\u0000\u00ff\u0100\u0003\u001b\r\u0000\u0100H\u0001\u0000\u0000"+
		"\u0000\u0101\u0102\u0003\u0011\b\u0000\u0102\u0103\u0003\u001b\r\u0000"+
		"\u0103\u0104\u0003\u000f\u0007\u0000\u0104\u0105\u0003\t\u0004\u0000\u0105"+
		"\u0106\u0003#\u0011\u0000\u0106\u0107\u0003\u0011\b\u0000\u0107\u0108"+
		"\u0003\'\u0013\u0000\u0108\u0109\u0003%\u0012\u0000\u0109J\u0001\u0000"+
		"\u0000\u0000\u010a\u010b\u0003\u0011\b\u0000\u010b\u010c\u0003%\u0012"+
		"\u0000\u010c\u010d\u0003+\u0015\u0000\u010d\u010e\u0003\u001d\u000e\u0000"+
		"\u010e\u010f\u0003\u0011\b\u0000\u010f\u0110\u0003\u0007\u0003\u0000\u0110"+
		"L\u0001\u0000\u0000\u0000\u0111\u0112\u0003\u0017\u000b\u0000\u0112\u0113"+
		"\u0003\t\u0004\u0000\u0113\u0114\u0003\'\u0013\u0000\u0114N\u0001\u0000"+
		"\u0000\u0000\u0115\u0116\u0003\u0017\u000b\u0000\u0116\u0117\u0003\u001d"+
		"\u000e\u0000\u0117\u0118\u0003\u001d\u000e\u0000\u0118\u0119\u0003\u001f"+
		"\u000f\u0000\u0119P\u0001\u0000\u0000\u0000\u011a\u011b\u0003\u001b\r"+
		"\u0000\u011b\u011c\u0003\t\u0004\u0000\u011c\u011d\u0003-\u0016\u0000"+
		"\u011dR\u0001\u0000\u0000\u0000\u011e\u011f\u0003\u001b\r\u0000\u011f"+
		"\u0120\u0003\u001d\u000e\u0000\u0120\u0121\u0003\'\u0013\u0000\u0121T"+
		"\u0001\u0000\u0000\u0000\u0122\u0123\u0003\u001d\u000e\u0000\u0123\u0124"+
		"\u0003\u000b\u0005\u0000\u0124V\u0001\u0000\u0000\u0000\u0125\u0126\u0003"+
		"\u001f\u000f\u0000\u0126\u0127\u0003\u001d\u000e\u0000\u0127\u0128\u0003"+
		"\u001d\u000e\u0000\u0128\u0129\u0003\u0017\u000b\u0000\u0129X\u0001\u0000"+
		"\u0000\u0000\u012a\u012b\u0003\'\u0013\u0000\u012b\u012c\u0003\u000f\u0007"+
		"\u0000\u012c\u012d\u0003\t\u0004\u0000\u012d\u012e\u0003\u001b\r\u0000"+
		"\u012eZ\u0001\u0000\u0000\u0000\u012f\u0130\u0005t\u0000\u0000\u0130\u0131"+
		"\u0003#\u0011\u0000\u0131\u0132\u0003)\u0014\u0000\u0132\u0133\u0003\t"+
		"\u0004\u0000\u0133\\\u0001\u0000\u0000\u0000\u0134\u0135\u0003-\u0016"+
		"\u0000\u0135\u0136\u0003\u000f\u0007\u0000\u0136\u0137\u0003\u0011\b\u0000"+
		"\u0137\u0138\u0003\u0017\u000b\u0000\u0138\u0139\u0003\t\u0004\u0000\u0139"+
		"^\u0001\u0000\u0000\u0000\u013a\u013b\u0007\u001b\u0000\u0000\u013b`\u0001"+
		"\u0000\u0000\u0000\u013c\u013d\u0007\u001c\u0000\u0000\u013db\u0001\u0000"+
		"\u0000\u0000\u013e\u0141\u0003_/\u0000\u013f\u0141\u0003a0\u0000\u0140"+
		"\u013e\u0001\u0000\u0000\u0000\u0140\u013f\u0001\u0000\u0000\u0000\u0141"+
		"d\u0001\u0000\u0000\u0000\u0142\u0148\u0003a0\u0000\u0143\u0147\u0003"+
		"c1\u0000\u0144\u0147\u00035\u001a\u0000\u0145\u0147\u0005_\u0000\u0000"+
		"\u0146\u0143\u0001\u0000\u0000\u0000\u0146\u0144\u0001\u0000\u0000\u0000"+
		"\u0146\u0145\u0001\u0000\u0000\u0000\u0147\u014a\u0001\u0000\u0000\u0000"+
		"\u0148\u0146\u0001\u0000\u0000\u0000\u0148\u0149\u0001\u0000\u0000\u0000"+
		"\u0149f\u0001\u0000\u0000\u0000\u014a\u0148\u0001\u0000\u0000\u0000\u014b"+
		"\u0151\u0003_/\u0000\u014c\u0150\u0003c1\u0000\u014d\u0150\u00035\u001a"+
		"\u0000\u014e\u0150\u0005_\u0000\u0000\u014f\u014c\u0001\u0000\u0000\u0000"+
		"\u014f\u014d\u0001\u0000\u0000\u0000\u014f\u014e\u0001\u0000\u0000\u0000"+
		"\u0150\u0153\u0001\u0000\u0000\u0000\u0151\u014f\u0001\u0000\u0000\u0000"+
		"\u0151\u0152\u0001\u0000\u0000\u0000\u0152h\u0001\u0000\u0000\u0000\u0153"+
		"\u0151\u0001\u0000\u0000\u0000\u0154\u0155\u0005(\u0000\u0000\u0155j\u0001"+
		"\u0000\u0000\u0000\u0156\u0157\u0005)\u0000\u0000\u0157l\u0001\u0000\u0000"+
		"\u0000\u0158\u0159\u0005.\u0000\u0000\u0159n\u0001\u0000\u0000\u0000\u015a"+
		"\u015b\u0005=\u0000\u0000\u015bp\u0001\u0000\u0000\u0000\u015c\u015d\u0005"+
		"<\u0000\u0000\u015dr\u0001\u0000\u0000\u0000\u015e\u015f\u0005<\u0000"+
		"\u0000\u015f\u0160\u0005=\u0000\u0000\u0160t\u0001\u0000\u0000\u0000\u0161"+
		"\u0162\u0005~\u0000\u0000\u0162v\u0001\u0000\u0000\u0000\u0163\u0164\u0005"+
		"/\u0000\u0000\u0164x\u0001\u0000\u0000\u0000\u0165\u0166\u0005*\u0000"+
		"\u0000\u0166z\u0001\u0000\u0000\u0000\u0167\u0168\u0005-\u0000\u0000\u0168"+
		"|\u0001\u0000\u0000\u0000\u0169\u016a\u0005+\u0000\u0000\u016a~\u0001"+
		"\u0000\u0000\u0000\u016b\u016c\u0005=\u0000\u0000\u016c\u016d\u0005>\u0000"+
		"\u0000\u016d\u0080\u0001\u0000\u0000\u0000\u016e\u016f\u0005<\u0000\u0000"+
		"\u016f\u0170\u0005-\u0000\u0000\u0170\u0082\u0001\u0000\u0000\u0000\u0171"+
		"\u0172\u0005:\u0000\u0000\u0172\u0084\u0001\u0000\u0000\u0000\u0173\u0174"+
		"\u0005;\u0000\u0000\u0174\u0086\u0001\u0000\u0000\u0000\u0175\u0176\u0005"+
		",\u0000\u0000\u0176\u0088\u0001\u0000\u0000\u0000\u0177\u0178\u0005@\u0000"+
		"\u0000\u0178\u008a\u0001\u0000\u0000\u0000\u0179\u017a\u0005{\u0000\u0000"+
		"\u017a\u008c\u0001\u0000\u0000\u0000\u017b\u017c\u0005}\u0000\u0000\u017c"+
		"\u008e\u0001\u0000\u0000\u0000\u017d\u0181\u0005\n\u0000\u0000\u017e\u017f"+
		"\u0005\r\u0000\u0000\u017f\u0181\u0005\n\u0000\u0000\u0180\u017d\u0001"+
		"\u0000\u0000\u0000\u0180\u017e\u0001\u0000\u0000\u0000\u0181\u0090\u0001"+
		"\u0000\u0000\u0000\u0182\u0183\u0005\u0000\u0000\u0000\u0183\u0092\u0001"+
		"\u0000\u0000\u0000\u0184\u018f\u0005\"\u0000\u0000\u0185\u0186\u0005\\"+
		"\u0000\u0000\u0186\u018e\u0003\u008fG\u0000\u0187\u0188\u0005\\\u0000"+
		"\u0000\u0188\u018e\u0005\"\u0000\u0000\u0189\u018a\u0003\u0091H\u0000"+
		"\u018a\u018b\u0006I\u0001\u0000\u018b\u018e\u0001\u0000\u0000\u0000\u018c"+
		"\u018e\t\u0000\u0000\u0000\u018d\u0185\u0001\u0000\u0000\u0000\u018d\u0187"+
		"\u0001\u0000\u0000\u0000\u018d\u0189\u0001\u0000\u0000\u0000\u018d\u018c"+
		"\u0001\u0000\u0000\u0000\u018e\u0191\u0001\u0000\u0000\u0000\u018f\u0190"+
		"\u0001\u0000\u0000\u0000\u018f\u018d\u0001\u0000\u0000\u0000\u0190\u0198"+
		"\u0001\u0000\u0000\u0000\u0191\u018f\u0001\u0000\u0000\u0000\u0192\u0199"+
		"\u0005\"\u0000\u0000\u0193\u0194\u0003\u008fG\u0000\u0194\u0195\u0006"+
		"I\u0002\u0000\u0195\u0199\u0001\u0000\u0000\u0000\u0196\u0197\u0005\u0000"+
		"\u0000\u0001\u0197\u0199\u0006I\u0003\u0000\u0198\u0192\u0001\u0000\u0000"+
		"\u0000\u0198\u0193\u0001\u0000\u0000\u0000\u0198\u0196\u0001\u0000\u0000"+
		"\u0000\u0199\u019a\u0001\u0000\u0000\u0000\u019a\u019b\u0006I\u0004\u0000"+
		"\u019b\u0094\u0001\u0000\u0000\u0000\u019c\u019d\u0005-\u0000\u0000\u019d"+
		"\u019e\u0005-\u0000\u0000\u019e\u01a2\u0001\u0000\u0000\u0000\u019f\u01a1"+
		"\t\u0000\u0000\u0000\u01a0\u019f\u0001\u0000\u0000\u0000\u01a1\u01a4\u0001"+
		"\u0000\u0000\u0000\u01a2\u01a3\u0001\u0000\u0000\u0000\u01a2\u01a0\u0001"+
		"\u0000\u0000\u0000\u01a3\u01a5\u0001\u0000\u0000\u0000\u01a4\u01a2\u0001"+
		"\u0000\u0000\u0000\u01a5\u01a6\u0003\u008fG\u0000\u01a6\u01a7\u0001\u0000"+
		"\u0000\u0000\u01a7\u01a8\u0006J\u0000\u0000\u01a8\u0096\u0001\u0000\u0000"+
		"\u0000\u01a9\u01aa\u0005(\u0000\u0000\u01aa\u01ab\u0005*\u0000\u0000\u01ab"+
		"\u01b0\u0001\u0000\u0000\u0000\u01ac\u01af\u0003\u0097K\u0000\u01ad\u01af"+
		"\t\u0000\u0000\u0000\u01ae\u01ac\u0001\u0000\u0000\u0000\u01ae\u01ad\u0001"+
		"\u0000\u0000\u0000\u01af\u01b2\u0001\u0000\u0000\u0000\u01b0\u01b1\u0001"+
		"\u0000\u0000\u0000\u01b0\u01ae\u0001\u0000\u0000\u0000\u01b1\u01b9\u0001"+
		"\u0000\u0000\u0000\u01b2\u01b0\u0001\u0000\u0000\u0000\u01b3\u01b4\u0005"+
		"*\u0000\u0000\u01b4\u01b5\u0005)\u0000\u0000\u01b5\u01b6\u0001\u0000\u0000"+
		"\u0000\u01b6\u01ba\u0006K\u0005\u0000\u01b7\u01b8\u0005\u0000\u0000\u0001"+
		"\u01b8\u01ba\u0006K\u0006\u0000\u01b9\u01b3\u0001\u0000\u0000\u0000\u01b9"+
		"\u01b7\u0001\u0000\u0000\u0000\u01ba\u0098\u0001\u0000\u0000\u0000\u01bb"+
		"\u01bc\u0005*\u0000\u0000\u01bc\u01bd\u0005)\u0000\u0000\u01bd\u01be\u0001"+
		"\u0000\u0000\u0000\u01be\u01bf\u0006L\u0007\u0000\u01bf\u009a\u0001\u0000"+
		"\u0000\u0000\u01c0\u01c1\t\u0000\u0000\u0000\u01c1\u01c2\u0006M\b\u0000"+
		"\u01c2\u009c\u0001\u0000\u0000\u0000\u0010\u0000\u00a0\u00db\u0140\u0146"+
		"\u0148\u014f\u0151\u0180\u018d\u018f\u0198\u01a2\u01ae\u01b0\u01b9\t\u0006"+
		"\u0000\u0000\u0001I\u0000\u0001I\u0001\u0001I\u0002\u0001I\u0003\u0001"+
		"K\u0004\u0001K\u0005\u0001L\u0006\u0001M\u0007";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
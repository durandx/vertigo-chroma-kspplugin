package io.vertigo.chroma.kspplugin.ui.editors.ksp.scanners;

import io.vertigo.chroma.kspplugin.lexicon.LexiconManager;
import io.vertigo.chroma.kspplugin.lexicon.Lexicons;
import io.vertigo.chroma.kspplugin.ui.theme.ColorName;
import io.vertigo.chroma.kspplugin.ui.theme.ColorTheme;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Scanner de la partition des string (cha�ne entre guillemets doubles).
 * <p>
 * Le scanner vise les string de la propri�t� "request" des t�ches SQL.
 * </p>
 * <p>
 * Il s'applique � tous les string de KSP avec des risques de faux positifs dans la coloration syntaxique.
 * </p>
 */
public class KspStringScanner extends RuleBasedScanner {

	private static final char NO_ESCAPE_CHAR = (char) -1;
	private static final String[] SQL_PUNCTUATION = { ".", "=", ",", "*", ">", "<", "!", "|", "(", ")", "%", "'", "'" };
	private static final String[] SQL_KEY_WORDS = LexiconManager.getInstance().getWords(Lexicons.SQL_KEY_WORDS);
	private RGB commentColor;
	private RGB verbColor;
	private RGB javaTagColor;
	private RGB parameterColor;
	private RGB doubleQuoteColor;
	private RGB defaultColor;

	/**
	 * Cr�� une nouvelle instance de KspStringScanner.
	 */
	public KspStringScanner() {
		super();
		setColors();
		setRules(extractRules());
	}

	private void setColors() {
		ColorTheme theme = ColorTheme.getCurrent();
		commentColor = theme.getColor(ColorName.COMMENT);
		verbColor = theme.getColor(ColorName.STRING_VERB);
		javaTagColor = theme.getColor(ColorName.STRING_INLINE_JAVA);
		parameterColor = theme.getColor(ColorName.STRING_PARAMETER);
		doubleQuoteColor = theme.getColor(ColorName.STRING_DOUBLE_QUOTE);
		defaultColor = theme.getColor(ColorName.STRING_DEFAULT);
	}

	private IRule[] extractRules() {
		IToken verb = new Token(new TextAttribute(new Color(Display.getCurrent(), verbColor), null, SWT.BOLD));
		IToken comment = new Token(new TextAttribute(new Color(Display.getCurrent(), commentColor), null, SWT.NORMAL));
		IToken javaTag = new Token(new TextAttribute(new Color(Display.getCurrent(), javaTagColor), null, SWT.BOLD));
		IToken parameter = new Token(new TextAttribute(new Color(Display.getCurrent(), parameterColor), null, SWT.NORMAL));
		IToken doubleQuote = new Token(new TextAttribute(new Color(Display.getCurrent(), doubleQuoteColor), null, SWT.NORMAL));
		IToken defaut = new Token(new TextAttribute(new Color(Display.getCurrent(), defaultColor), null, SWT.BOLD));

		/* R�gle de d�tection de mots avec un style par d�faut. */
		WordRule wordRule = new WordRule(new IWordDetector() {
			@Override
			public boolean isWordPart(char c) {
				return Character.isJavaIdentifierPart(c);
			}

			@Override
			public boolean isWordStart(char c) {
				return Character.isJavaIdentifierStart(c);
			}
		}, defaut, true /* ignoreCase */);

		/* Note les mots-cl�s SQL. */
		for (String k : SQL_KEY_WORDS) {
			wordRule.addWord(k, verb);
		}

		/* R�gle de d�tection de signes de ponctuation avec un style par d�faut. */
		List<String> puncutationList = Arrays.asList(SQL_PUNCTUATION);
		WordRule punctuationRule = new WordRule(new IWordDetector() {
			@Override
			public boolean isWordPart(char c) {
				return puncutationList.contains(String.valueOf(c));
			}

			@Override
			public boolean isWordStart(char c) {
				return puncutationList.contains(String.valueOf(c));
			}
		}, comment, true /* ignoreCase */);

		/*
		 * R�gle pour les guillemets doubles ouvrantes et fermantes de la cha�ne.
		 */
		WordRule doubleQuoteRule = new WordRule(new IWordDetector() {

			/*
			 * @see IWordDetector#isWordStart
			 */
			public boolean isWordStart(char c) {
				return c == '"';
			}

			/*
			 * @see IWordDetector#isWordPart
			 */
			public boolean isWordPart(char c) {
				return c == '"';
			}
		}, defaut, false /* ignoreCase */);

		doubleQuoteRule.addWord("\"", doubleQuote);

		return new IRule[] {
		/* R�gle sur les guillemets doubles */
		doubleQuoteRule,
		/* R�gle pour les commentaires SQL multi-lignes. */
		new PatternRule("/*", "*/", comment, NO_ESCAPE_CHAR, false),
		/* R�gle pour les commentaires SQL sur une ligne. */
		new EndOfLineRule("--", comment, NO_ESCAPE_CHAR, false),
		/* R�gle pour les tags Java dans le SQL (Kasper >=4 ). */
		new PatternRule("<%", "%>", javaTag, NO_ESCAPE_CHAR, false),
		/* R�gle pour les tags Java dans le SQL (Kasper 3 ). */
		new PatternRule("<if", ">", javaTag, NO_ESCAPE_CHAR, false),
		/* R�gle pour les tags Java dans le SQL (Kasper 3 ). */
		new PatternRule("</if", ">", javaTag, NO_ESCAPE_CHAR, false),
		/* R�gle pour les param�tres SQL */
		new PatternRule("#", "#", parameter, NO_ESCAPE_CHAR, false),
		/* R�gle sur les mots-cl�s */
		wordRule,
		/* R�gle pour la ponctuation */
		punctuationRule };
	}
}

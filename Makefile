full:
	make clean
	make program
	make jar
	make clean
program:
	javac -cp ./src/main -Xlint:unchecked ./*.java
test:
	make clean
	find src/test -name "*.java" > ./sources.txt
	javac -cp src/main:src/test:junit-4.12.jar:hamcrest-core-1.3.jar -Xlint:unchecked @sources.txt
	rm sources.txt
	java -cp src/main:src/test:junit-4.12.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore larp.automaton.FiniteAutomataTest larp.automaton.DFATest larp.grammartokenizer.regularlanguage.RegularExpressionIntermediateNestingLevelValidAssertionTest larp.grammartokenizer.regularlanguage.RegularExpressionFinalNestingLevelValidAssertionTest larp.grammartokenizer.regularlanguage.RegularExpressionFinalEscapingStatusValidAssertionTest larp.grammartokenizer.regularlanguage.RegularExpressionGrammarTokenizerTest RegularLanguageEpsilonTokenTest OrTokenTest CloseParenthesisTokenTest OpenParenthesisTokenTest KleeneClosureTokenTest CharacterTokenTest larp.grammarparser.regularlanguage.RegularExpressionGrammarParserTest CharacterNodeTest KleeneClosureNodeTest OrNodeTest RegularLanguageConcatenationNodeTest larp.automaton.NFATest larp.automaton.EpsilonNFATest larp.automaton.StateTest larp.automaton.StateTransitionTest larp.automaton.DFAStateTest larp.parsercompiler.regularlanguage.RegularExpressionParserCompilerTest larp.parsercompiler.regularlanguage.EpsilonNFAToNFAConverterTest larp.parsercompiler.regularlanguage.NFAToDFAConverterTest SeparatorTokenTest NonTerminalTokenTest TerminalTokenTest ContextFreeLanguageEpsilonTokenTest larp.grammartokenizer.contextfreelanguage.ContextFreeGrammarTokenizerTest larp.grammartokenizer.contextfreelanguage.ContextFreeGrammarFinalQuoteNestingCorrectAssertionTest larp.grammartokenizer.contextfreelanguage.ContextFreeGrammarStartingTokensValidAssertionTest larp.grammartokenizer.contextfreelanguage.ContextFreeGrammarCorrectNumberOfSeparatorsAssertionTest larp.grammartokenizer.contextfreelanguage.ContextFreeGrammarEscapeCharacterPositionCorrectAssertionTest ProductionNodeTest EpsilonNodeTest NonTerminalNodeTest TerminalNodeTest ContextFreeLanguageConcatenationNodeTest EndOfStringNodeTest DotNodeTest larp.grammarparser.contextfreelanguage.ContextFreeGrammarParserTest larp.grammar.contextfreelanguage.ContextFreeGrammarTest larp.parser.contextfreelanguage.LL1ParseTableCellAvailableAssertionTest larp.parser.contextfreelanguage.LL1ParseTableTest PairToValueMapTest larp.parser.contextfreelanguage.LR0ShiftActionTest larp.parser.contextfreelanguage.LR0ReduceActionTest larp.parser.contextfreelanguage.LR0GotoActionTest larp.parser.contextfreelanguage.LR0AcceptActionTest larp.parser.contextfreelanguage.LR0ParseTableCellAvailableAssertionTest larp.parser.contextfreelanguage.LR0ParseTableTest larp.parser.contextfreelanguage.LL1ParserTest larp.parser.contextfreelanguage.LR0ParseStackTest larp.parser.contextfreelanguage.LR0ParserTest ValueToSetMapTest larp.parsercompiler.contextfreelanguage.FirstSetCalculatorTest larp.parsercompiler.contextfreelanguage.FollowSetCalculatorTest larp.parsercompiler.contextfreelanguage.ContextFreeGrammarLL1ParserCompilerTest larp.parser.contextfreelanguage.LR0ProductionSetDFATest larp.parser.contextfreelanguage.LR0ProductionSetDFAStateTest larp.parsercompiler.contextfreelanguage.ContextFreeGrammarLR0ParserCompilerTest larp.parsercompiler.contextfreelanguage.ContextFreeGrammarAugmentorTest larp.parsercompiler.contextfreelanguage.ProductionNodeDotRepositoryTest larp.parsercompiler.contextfreelanguage.ContextFreeGrammarClosureCalculatorTest larp.parsercompiler.contextfreelanguage.ContextFreeGrammarLR0ProductionSetDFACompilerTest larp.parserfactory.regularlanguage.RegularLanguageParserFactoryTest ContextFreeLanguageParserFactoryTest
	make clean
clean:
	find . -name "*.class" -exec rm {} \;
	find . -name "*.java~" -exec rm {} \;
jar:
	rm -f ./LARP.jar
	jar cvfm LARP.jar Manifest.txt LARP.java LARP.class -C ./src/main larp
run:
	java -jar LARP.jar

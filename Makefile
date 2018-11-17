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
	java -cp src/main:src/test:junit-4.12.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore FiniteAutomataTest larp.automaton.DFATest RegularExpressionIntermediateNestingLevelValidAssertionTest RegularExpressionFinalNestingLevelValidAssertionTest RegularExpressionFinalEscapingStatusValidAssertionTest RegularExpressionGrammarTokenizerTest RegularLanguageEpsilonTokenTest OrTokenTest CloseParenthesisTokenTest OpenParenthesisTokenTest KleeneClosureTokenTest CharacterTokenTest RegularExpressionGrammarParserTest CharacterNodeTest KleeneClosureNodeTest OrNodeTest RegularLanguageConcatenationNodeTest NFATest EpsilonNFATest StateTest StateTransitionTest DFAStateTest RegularExpressionParserCompilerTest EpsilonNFAToNFAConverterTest NFAToDFAConverterTest SeparatorTokenTest NonTerminalTokenTest TerminalTokenTest ContextFreeLanguageEpsilonTokenTest ContextFreeGrammarTokenizerTest ContextFreeGrammarFinalQuoteNestingCorrectAssertionTest ContextFreeGrammarStartingTokensValidAssertionTest ContextFreeGrammarCorrectNumberOfSeparatorsAssertionTest ContextFreeGrammarEscapeCharacterPositionCorrectAssertionTest ProductionNodeTest EpsilonNodeTest NonTerminalNodeTest TerminalNodeTest ContextFreeLanguageConcatenationNodeTest EndOfStringNodeTest DotNodeTest ContextFreeGrammarParserTest ContextFreeGrammarTest LL1ParseTableCellAvailableAssertionTest LL1ParseTableTest PairToValueMapTest LR0ShiftActionTest LR0ReduceActionTest LR0GotoActionTest LR0AcceptActionTest LR0ParseTableCellAvailableAssertionTest LR0ParseTableTest LL1ParserTest LR0ParseStackTest LR0ParserTest ValueToSetMapTest FirstSetCalculatorTest FollowSetCalculatorTest ContextFreeGrammarLL1ParserCompilerTest LR0ProductionSetDFATest LR0ProductionSetDFAStateTest ContextFreeGrammarLR0ParserCompilerTest ContextFreeGrammarAugmentorTest ProductionNodeDotRepositoryTest ContextFreeGrammarClosureCalculatorTest ContextFreeGrammarLR0ProductionSetDFACompilerTest RegularLanguageParserFactoryTest ContextFreeLanguageParserFactoryTest
	make clean
clean:
	find . -name "*.class" -exec rm {} \;
	find . -name "*.java~" -exec rm {} \;
jar:
	rm -f ./LARP.jar
	jar cvfm LARP.jar Manifest.txt LARP.java LARP.class -C ./src/main larp
run:
	java -jar LARP.jar

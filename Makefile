# actual source files
GRMRS := JavaLexer.g4 JavaParser.g4
SRCS := Main.java IntDivideVisitor.java StringEqualsVisitor.java \
		SwitchCheckVisitor.java VoidConstructorVisitor.java Warnings.java \
		ShadowCheckVisitor.java

# generated code files from antlr
GEN := JavaLexer.java JavaParser.java JavaParserVisitor.java JavaParserBaseVisitor.java

# all the source files
ALLSRC := $(GEN) $(SRCS)

# the corresponding class files
ALLCLASS := $(ALLSRC:.java=.class)

all: $(ALLCLASS) $(GRMRS)
	@echo "All done!"

JavaParser.java: JavaParser.g4
	java org.antlr.v4.Tool  -no-listener -visitor JavaParser.g4

JavaLexer.java: JavaLexer.g4
	java org.antlr.v4.Tool JavaLexer.g4

# why does the meta rule not find this one???
JavaParserBaseVisitor.class: JavaParserBaseVisitor.java
	javac JavaParserBaseVisitor.java

# each class depends on its java
%.class: %.java
	javac $<

clean:
	rm -f *.class *.interp *.tokens $(GEN)


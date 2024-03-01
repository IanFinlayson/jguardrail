package net.ianfinlayson.jguardrail;

// if the input java code cannot be parsed, we do not want to do the analysis
// that's because it'll likely be wrong and also because the javac compiler will
// give a better syntax error than we could here
//
// so this error listener simply notes that an error has been encountered
// after the parse we check in with it and bail if an error is present

import org.antlr.v4.runtime.*;

public class ErrorListener extends BaseErrorListener {
    private boolean seenErrors = false;

    @Override
    public void syntaxError(Recognizer recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        seenErrors = true;
    }

    public boolean errorsExist() {
        return seenErrors;
    }
}


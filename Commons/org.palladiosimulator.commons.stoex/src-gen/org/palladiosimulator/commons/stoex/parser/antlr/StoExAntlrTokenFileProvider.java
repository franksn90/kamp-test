/*
 * generated by Xtext
 */
package org.palladiosimulator.commons.stoex.parser.antlr;

import java.io.InputStream;

import org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider;

public class StoExAntlrTokenFileProvider implements IAntlrTokenFileProvider {
    public InputStream getAntlrTokenFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader
                .getResourceAsStream("org/palladiosimulator/commons/stoex/parser/antlr/internal/InternalStoEx.tokens");
    }
}
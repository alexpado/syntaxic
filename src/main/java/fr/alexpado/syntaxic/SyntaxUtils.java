package fr.alexpado.syntaxic;


import fr.alexpado.syntaxic.interfaces.ISyntax;
import fr.alexpado.syntaxic.interfaces.ISyntaxContainer;
import fr.alexpado.syntaxic.rules.*;

import java.util.*;
import java.util.stream.Collectors;

public final class SyntaxUtils {

    /**
     * Convert the provided {@link String} into the corresponding {@link ISyntax}.
     * <p>
     * Will always default to {@link WordSyntax} if nothing matched.
     *
     * @param options The {@link Map} containing the options possible for each argument.
     * @param value   The {@link String} to convert into {@link ISyntax}.
     *
     * @return The {@link ISyntax} matching the provided {@link String}.
     */
    public static ISyntax toSyntax(Map<String, List<String>> options, String value) {

        if (SyntaxService.isEncapsulated(value, "{", "}")) {
            String name = SyntaxService.getName(value, "{", "}");
            return new SelectiveSyntax(value, options.getOrDefault(name, Collections.emptyList()));
        } else if (SyntaxService.isEncapsulated(value, "/", "/")) {
            return new RegexSyntax(value);
        } else if (SyntaxService.isEncapsulated(value, "[", "]")) {
            return new EagerSyntax(value);
        } else if (value.endsWith("...")) {
            return new FillingSyntax(value);
        } else {
            return new WordSyntax(value);
        }
    }

    /**
     * Convert the provided {@link String}s into the corresponding {@link ISyntax}es.
     * <p>
     * Will always default to {@link WordSyntax} if nothing matched.
     *
     * @param options The {@link Map} containing the options possible for each argument.
     * @param values  The {@link String}s to convert into {@link ISyntax}es.
     *
     * @return The {@link ISyntax}es matching the provided {@link String}s.
     */
    public static List<ISyntax> toSyntax(Map<String, List<String>> options, Collection<String> values) {

        return values.stream().map(value -> toSyntax(options, value)).collect(Collectors.toList());
    }

    /**
     * Convert the provided string representing {@link ISyntax}es into the corresponding {@link ISyntaxContainer}.
     *
     * @param options        The {@link Map} containing the list of parameters available for a {@link SelectiveSyntax}.
     * @param representation The representation to convert.
     * @param order          The order (priority) of the {@link ISyntaxContainer}.
     *
     * @return An {@link ISyntaxContainer} implementation.
     */
    public static ISyntaxContainer toContainer(Map<String, List<String>> options, String representation, int order) {

        Collection<String> rawSyntaxList = Arrays.asList(representation.trim().split(" "));
        return new SyntaxContainer(toSyntax(options, rawSyntaxList), order);
    }
}

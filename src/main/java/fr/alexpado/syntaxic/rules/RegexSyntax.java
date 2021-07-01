package fr.alexpado.syntaxic.rules;

import fr.alexpado.syntaxic.SyntaxService;
import fr.alexpado.syntaxic.interfaces.ISyntax;
import fr.alexpado.syntaxic.interfaces.ISyntaxContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The RegexSyntax represents an {@link ISyntax} that will match a provided regex.
 * <p>
 * By design, {@link #complete(String)} will always return an  empty list.
 * <p>
 * A RegexSyntax is represented by a name, a colon and a regex, everything being between slashes (ex:
 * <code>/syntax:[0-9]+/...</code>).
 * <p>
 * You can also use regex group to capture only a part of the matched string (it will always take the last matched
 * group). Ex: /syntax:([0-9])[ab]?/ will match any number followed by an optional 'a' or 'b', but will only 'memorize'
 * the number.
 */
public class RegexSyntax implements ISyntax {

    private final     String  name;
    private final     Pattern pattern;
    private @Nullable String  lastMatch;

    /**
     * Create a new instance of this {@link ISyntax} implementation.
     *
     * @param name The name of this {@link ISyntax}.
     */
    public RegexSyntax(String name) {


        String[] parts = SyntaxService.getName(name, "/", "/").split(":");

        if (parts.length == 2) {
            this.name    = parts[0];
            this.pattern = Pattern.compile(parts[1]);
        } else {
            throw new IllegalArgumentException("Wrong regex declaration: Should be /name:regex/");
        }

        this.lastMatch = null;
    }

    /**
     * Retrieve this {@link ISyntax}'s name.
     * <p>
     * The returned value may then be used to retrieve the value from {@link ISyntaxContainer#getMatches()}.
     *
     * @return This {@link ISyntax}'s name.
     */
    @Override
    public @NotNull String getName() {

        return this.name;
    }

    /**
     * Check if the provided string perfectly matches this {@link ISyntax} rules.
     * <p>
     * Every implementation of this method should only return <code>true</code> if, and only if, the provided string
     * matches exactly the expected data format.
     * <p>
     * You may not use this method to check if the provided data matches partially the rules, for which {@link
     * #isCompletable(String)} is more appropriate.
     *
     * @param data The data to check against this {@link ISyntax} rules.
     *
     * @return True if the provided data exactly matches this {@link ISyntax} rules, false otherwise.
     */
    @Override
    public boolean isMatching(@NotNull String data) {

        Matcher matcher = this.pattern.matcher(data);

        if (matcher.matches()) {
            this.lastMatch = matcher.group(matcher.groupCount());
            return true;
        }

        this.lastMatch = null;
        return false;
    }

    /**
     * Retrieve the last value that matched with {@link #isMatching(String)}. This may return an empty optional if the
     * last return value of {@link #isMatching(String)} was <code>false</code>.
     *
     * @return The last matched value.
     */
    @Override
    public Optional<String> getLastMatch() {

        return Optional.ofNullable(this.lastMatch);
    }

    /**
     * Check if the provided string partially matches this {@link ISyntax} rules and can be auto-completed.
     * <p>
     * Every implementation of this method may behave similarly to {@link String#startsWith(String)} for better user
     * experience with the auto-completion.
     * <p>
     * If this {@link ISyntax} isn't completable, this method should always return <code>false</code>.
     *
     * @param data The data to check against this {@link ISyntax} rules.
     *
     * @return True if the provided data can be auto-completed, false otherwise.
     */
    @Override
    public boolean isCompletable(@NotNull String data) {

        return true;
    }

    /**
     * Retrieve a {@link List} of strings that matched the auto-complete of this {@link ISyntax}. All strings returned
     * should be complete options that contains the original user's input, if applicable.
     *
     * @param data The data to complete using this {@link ISyntax} rules.
     *
     * @return A {@link List} of strings that matched the user's input.
     *
     * @see #isCompletable(String)
     */
    @Override
    public List<String> complete(@NotNull String data) {

        return Collections.emptyList();
    }
}

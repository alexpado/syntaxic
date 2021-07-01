package fr.alexpado.syntaxic.interfaces;

import fr.alexpado.syntaxic.SyntaxService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Interface representing a matching and completion service. You can implement this interface yourself, but this will
 * defeat the purpose of this library itself.
 * <p>
 * If you want to override some features of Syntaxic, you may want to override methods of {@link SyntaxService}.
 *
 * @param <T> Type of the identifier. An identifier can be anything, but should be easily distinguishable from other
 *            identifiers.
 */
public interface ISyntaxService<T> {

    /**
     * Prepare the given user's input for matching or completion handling.
     *
     * @param data The user's input to prepare
     *
     * @return A {@link List} of strings containing the sanitized user's input
     */
    @NotNull List<String> prepareUserData(@NotNull String data);

    /**
     * Retrieve a {@link List} of strings completing the user's input.
     *
     * @param data The user's input to complete.
     *
     * @return A {@link List} of strings.
     */
    @NotNull List<String> complete(@NotNull String data);

    /**
     * Retrieve an optional {@link IMatchingResult} for the given user's input. The returned optional value won't be
     * empty if one, and only one identifier matches the user's input.
     *
     * @param data The user's input to match.
     *
     * @return An optional {@link IMatchingResult} for the given user's input.
     *
     * @see ISyntaxContainer#isMatching(List)
     */
    Optional<IMatchingResult<T>> getMatchingResult(@NotNull String data);

}

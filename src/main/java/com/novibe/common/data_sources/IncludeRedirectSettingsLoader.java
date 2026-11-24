package com.novibe.common.data_sources;

import com.novibe.common.config.EnvironmentVariables;
import com.novibe.common.exception.UserInputException;
import com.novibe.common.util.EnvParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Loads and compiles domain inclusion regular expressions from the `INCLUDE_REDIRECT` environment variable.
 */
@Service
public class IncludeRedirectSettingsLoader {

    /**
     * Parses and compiles regex patterns from `INCLUDE_REDIRECT`.
     *
     * @return list of compiled case-insensitive {@link Pattern} objects
     */
    public List<Pattern> loadIncludePatterns() {
        return Optional.ofNullable(EnvParser.parse(EnvironmentVariables.INCLUDE_REDIRECT))
                .stream()
                .flatMap(List::stream)
                .map(String::trim)
                .filter(pattern -> !pattern.isEmpty())
                .distinct()
                .map(this::compilePattern)
                .toList();
    }

    private Pattern compilePattern(String regex) {
        try {
            return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            throw UserInputException.noStackTrace("Invalid regex pattern in INCLUDE_REDIRECT: '%s'. %s"
                    .formatted(regex, e.getMessage()));
        }
    }
}

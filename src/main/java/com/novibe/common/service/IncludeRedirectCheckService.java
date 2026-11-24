package com.novibe.common.service;

import com.novibe.common.data_sources.IncludeRedirectSettingsLoader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Evaluates whether a domain matches the configured include regex patterns for DNS rewrites.
 */
@Service
public class IncludeRedirectCheckService {

    private final List<Pattern> includePatterns;

    public IncludeRedirectCheckService(IncludeRedirectSettingsLoader includeRedirectSettingsLoader) {
        includePatterns = includeRedirectSettingsLoader.loadIncludePatterns();
    }

    /**
     * Checks if the given domain should be included in DNS rewrites.
     *
     * - Returns `true` if no patterns are configured (default behavior: include everything).
     * - Returns `false` if domain is `null` or blank.
     * - Returns `true` if the domain matches at least one configured regex pattern.
     *
     * @param domain domain name to check
     * @return `true` if domain should be included, `false` otherwise
     */
    public boolean shouldInclude(String domain) {
        if (includePatterns.isEmpty()) {
            return true;
        }
        if (domain == null || domain.isBlank()) {
            return false;
        }
        String normalizedDomain = domain.strip();
        for (Pattern pattern : includePatterns) {
            if (pattern.matcher(normalizedDomain).matches()) {
                return true;
            }
        }
        return false;
    }
}

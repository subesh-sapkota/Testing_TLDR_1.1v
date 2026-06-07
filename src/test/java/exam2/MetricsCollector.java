package exam2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MetricsCollector {

    // Week and Provider tracking
    public static Set<String> weeksTested = new HashSet<>();
    public static Set<String> providersValidated = new HashSet<>();

    // External page mismatch tracking
    public static int externalPageNameMismatchCount = 0;
    public static List<String> externalPageNameMismatchDetails = new ArrayList<>();

    // Movie and validation tracking
    public static Set<String> moviesValidatedWithWatchOn = new HashSet<>();
    public static Set<String> moviesValidatedWithTrailer = new HashSet<>();
    public static List<String> providerPagesOpened = new ArrayList<>();

    // Count metrics
    public static int totalMonthValidated = 1;
    public static int totalTitlesVerified = 0;
    public static int totalTitlesVerifiedTC04 = 0;
    public static int totalWeekValidated = 0;
    public static int totalProvidersValidated = 0;
    public static int totalWeekCheckforProviderContent = 0;
    public static int totalMoviesProvider = 0;

    // Validation counters
    public static int watchOnButtonsValidated = 0;
    public static int trailerButtonsValidated = 0;
    public static int issuesFound = 0;
    public static int MissingDiscription = 0;

    public static StringBuilder issues = new StringBuilder();

    // Reset method - call this at the beginning of test suite
    public static void reset() {
        weeksTested.clear();
        providersValidated.clear();
        moviesValidatedWithWatchOn.clear();
        moviesValidatedWithTrailer.clear();
        providerPagesOpened.clear();

        totalMonthValidated = 1;
        totalTitlesVerified = 0;
        totalTitlesVerifiedTC04 = 0;
        totalWeekValidated = 0;
        totalProvidersValidated = 0;
        totalWeekCheckforProviderContent = 0;
        totalMoviesProvider = 0;

        watchOnButtonsValidated = 0;
        trailerButtonsValidated = 0;
        issuesFound = 0;
        MissingDiscription = 0;

        externalPageNameMismatchCount = 0;
        externalPageNameMismatchDetails.clear();

        issues = new StringBuilder();
    }

    public static void addIssue(String issue) {
        issuesFound++;
        issues.append(issue).append("\n");
    }

    public static void addExternalPageNameMismatch(
            String movieTitle,
            String providerTitle,
            String providerUrl
    ) {
        externalPageNameMismatchCount++;

        externalPageNameMismatchDetails.add(
                "Movie: " + movieTitle +
                " | Provider Title: " + providerTitle +
                " | URL: " + providerUrl
        );

        addIssue(
                "External page opened but name not strictly matched" +
                " | Movie: " + movieTitle +
                " | Provider Title: " + providerTitle +
                " | URL: " + providerUrl
        );
    }
}

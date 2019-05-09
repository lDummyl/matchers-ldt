package src;

import net.minidev.json.JSONArray;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.time.LocalDateTime;

public class TimeIntervalMatcher extends BaseMatcher<LocalDateTime> {

    private LocalDateTime from;
    private LocalDateTime to;
    private int missMatchAtIndex;

    public TimeIntervalMatcher(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean matches(Object item) {
        JSONArray rawData = (JSONArray) item;
        if (rawData.isEmpty()) {
            throw new RuntimeException("No data to parse!");
        }
        for (Object raw : rawData) {
            LocalDateTime parsed = LocalDateTime.parse(raw.toString());
            if (!parsed.isBefore(to) || !parsed.isAfter(from)) {
                missMatchAtIndex = rawData.indexOf(raw);
                return false;
            }
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("All DateTime fields from %s to %s, mismatch at index %d",
                from, to, missMatchAtIndex));
    }
}

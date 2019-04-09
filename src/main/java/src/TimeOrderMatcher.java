package src;

import net.minidev.json.JSONArray;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.time.LocalDateTime;

public class TimeOrderMatcher extends BaseMatcher<LocalDateTime> {

    private Direction direction;
    private int missMatchAtIndex;

    public enum Direction {
        DESC,
        ASC
    }

    public TimeOrderMatcher(Direction direction) {
        this.direction = direction;
    }

    @Override
    public boolean matches(Object item) {
        JSONArray rawData = (JSONArray) item;
        if (rawData.size() == 0) {
            throw new RuntimeException("No data to parse!");
        }
        LocalDateTime before = null;
        for (Object raw : rawData) {
            LocalDateTime parsed = LocalDateTime.parse(raw.toString());
            if (before == null) {
                before = parsed;
                continue;
            }
            if (!before.isEqual(parsed) && (!before.isBefore(parsed) && direction == Direction.ASC)) {
                missMatchAtIndex = rawData.indexOf(raw);
                return false;
            }
            if (!before.isEqual(parsed) && ((before.isBefore(parsed) || before.isEqual(parsed)) && direction == Direction.DESC)) {
                missMatchAtIndex = rawData.indexOf(raw);
                return false;
            }
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("Order is not %s at index %d",
                direction.name(), missMatchAtIndex));
    }
}

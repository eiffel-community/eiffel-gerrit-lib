package com.axis.eiffel.gerrit.lib.formatter;

import com.ericsson.eiffel.remrem.semantics.EiffelEventType;
import com.ericsson.eiffel.remrem.semantics.validator.EiffelValidationException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.axis.eiffel.gerrit.lib.formatter.TestResourceType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Christian Bilevits, christian.bilevits@axis.com
 * @since 2019-07-10
 */
@DisplayName("Testing EiffelEventService")
public class EiffelEventServiceTest {

    private final EiffelEventService service = new EiffelEventService();

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Testing getEiffelType for a Gerrit type")
    void getEiffelTypeGerrit() {
        assertEquals(service.getEiffelType(GerritEventType.PATCHSET_CREATED.getEventType()),
                EiffelEventType.SOURCECHANGE_CREATED.getEventName());
        assertEquals(service.getEiffelType(GerritEventType.CHANGE_MERGED.getEventType()),
                EiffelEventType.SOURCECHANGE_SUBMITTED.getEventName());
    }

    @Test
    @DisplayName("Testing getEiffelType for a eiffel event")
    void getEiffelTypeEiffel() {
        assertEquals(service.getEiffelType(EIFFEL_CREATED_EVENT.load()),
                EiffelEventType.SOURCECHANGE_CREATED.getEventName());
        assertEquals(service.getEiffelType(EIFFEL_SUBMITTED_EVENT.load()),
                EiffelEventType.SOURCECHANGE_SUBMITTED.getEventName());
    }

    @Test
    @DisplayName("Testing getEiffelEventId")
    void getEiffelEventId() {
        assertEquals(service.getEiffelEventId(EIFFEL_CREATED_EVENT.load()), "14418a3d-5cf2-4d27-8b54-8d307b790164");
        assertEquals(service.getEiffelEventId(EIFFEL_SUBMITTED_EVENT.load()), "60a928ee-5f1d-47a2-8985-815fb731e634");
    }

    @Test
    @DisplayName("Testing both convertToEiffel()")
    void convertToEiffel() {
        JsonObject eiffelCreate = null;
        JsonObject eiffelSubmit = null;

        try {
            eiffelCreate = service.convertToEiffel(GERRIT_CREATED_EVENT.load());
            eiffelSubmit = service.convertToEiffel(GERRIT_MERGED_EVENT.load(), service.getEiffelEventId(eiffelCreate));
        } catch (EventException | EiffelValidationException e) {
            e.printStackTrace();
        }

        assertNotNull(eiffelCreate);
        assertNotNull(eiffelSubmit);

        assertEquals(eiffelCreate.getAsJsonObject("meta").get("id").getAsString(),
                eiffelSubmit.getAsJsonArray("links").get(0).getAsJsonObject().get("target").getAsString());
    }

    @Test
    @DisplayName("Testing convertToEiffel with wrong type and empty input")
    void convertToEiffelWrong() {
        JsonObject wrongType = new JsonParser().parse("{\"type\":\"ref-replication-scheduled\"}").getAsJsonObject();
        JsonObject empty = new JsonParser().parse("{}").getAsJsonObject();
        JsonObject eiffelWrongType = null;
        JsonObject eiffelEmpty = null;
        JsonObject eiffelNonExistCreate = null;

        try {
            eiffelWrongType = service.convertToEiffel(wrongType);
        } catch (EventException | EiffelValidationException ignored) {
        }

        assertNull(eiffelWrongType);

        try {
            eiffelEmpty = service.convertToEiffel(empty);
        } catch (EventException | EiffelValidationException ignored) {
        }

        assertNull(eiffelEmpty);

        try {
            eiffelNonExistCreate = service.convertToEiffel(GERRIT_MERGED_EVENT_FALSE.load());
        } catch (EventException | EiffelValidationException ignored) {
        }

        assertNull(eiffelNonExistCreate);
    }

    @AfterEach
    void tearDown() {
    }
}
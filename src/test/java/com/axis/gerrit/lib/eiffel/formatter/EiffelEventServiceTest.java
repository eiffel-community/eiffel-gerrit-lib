package com.axis.gerrit.lib.eiffel.formatter;

import com.ericsson.eiffel.remrem.semantics.EiffelEventType;
import com.ericsson.eiffel.remrem.semantics.validator.EiffelValidationException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Christian Bilevits, christian.bilevits@axis.com
 * @since 2019-07-10
 */
@DisplayName("Testing EiffelEventService")
public class EiffelEventServiceTest {

    private EiffelEventService service = new EiffelEventService();

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
        assertEquals(service.getEiffelType(getEiffelCreatedEvent()),
                EiffelEventType.SOURCECHANGE_CREATED.getEventName());
        assertEquals(service.getEiffelType(getEiffelSubmittedEvent()),
                EiffelEventType.SOURCECHANGE_SUBMITTED.getEventName());
    }

    @Test
    @DisplayName("Testing getEiffelEventId")
    void getEiffelEventId() {
        assertEquals(service.getEiffelEventId(getEiffelCreatedEvent()), "14418a3d-5cf2-4d27-8b54-8d307b790164");
        assertEquals(service.getEiffelEventId(getEiffelSubmittedEvent()), "60a928ee-5f1d-47a2-8985-815fb731e634");
    }

    @Test
    @DisplayName("Testing both convertToEiffel()")
    void convertToEiffel() {
        JsonObject eiffelCreate = null;
        JsonObject eiffelSubmit = null;

        try {
            eiffelCreate = service.convertToEiffel(getGerritCreatedEvent());
            eiffelSubmit = service.convertToEiffel(getGerritMergedEvent(), service.getEiffelEventId(eiffelCreate));
        } catch (EventException | EiffelValidationException e) {
            e.printStackTrace();
        }

        assertNotNull(eiffelCreate);
        assertNotNull(eiffelSubmit);

        assertEquals(eiffelCreate.getAsJsonObject("meta").get("id").toString(),
                eiffelSubmit.getAsJsonArray("links").get(0).getAsJsonObject().get("target").toString());
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
            eiffelNonExistCreate = service.convertToEiffel(getGerritMergedEventFalse());
        } catch (EventException | EiffelValidationException ignored) {
        }

        assertNull(eiffelNonExistCreate);
    }

    @AfterEach
    void tearDown() {
    }

    private JsonObject getEiffelCreatedEvent() {
        return new JsonParser()
                .parse("{\"meta\":{\"id\":\"14418a3d-5cf2-4d27-8b54-8d307b790164\",\"type\":\"EiffelSourceChangeCreatedEvent\",\"version\":\"4.0.0\",\"time\":1564659341925,\"tags\":[],\"source\":{\"domainId\":\"\",\"host\":\"\\\"https://link.se\\\"\",\"name\":\"\\\"Test Testsson\\\"\",\"uri\":\"\"}},\"data\":{\"gitIdentifier\":{\"commitId\":\"\\\"I4a75a2005033860f084463c15e7ce40312e53c40\\\"\",\"branch\":\"\\\"master\\\"\",\"repoName\":\"\\\"test\\\"\",\"repoUri\":\"\\\"https://link.se\\\"\"},\"customData\":[]},\"links\":[]}")
                .getAsJsonObject();
    }

    private JsonObject getEiffelSubmittedEvent() {
        return new JsonParser()
                .parse("{\"meta\":{\"id\":\"60a928ee-5f1d-47a2-8985-815fb731e634\",\"type\":\"EiffelSourceChangeSubmittedEvent\",\"version\":\"3.0.0\",\"time\":1564659395978,\"tags\":[],\"source\":{\"domainId\":\"\",\"host\":\"\\\"https://links.se\\\"\",\"name\":\"\\\"Test Testsson\\\"\",\"uri\":\"\"}},\"data\":{\"gitIdentifier\":{\"commitId\":\"\\\"I0b165d8ad7bfdca846e91ae16913df5bbe7e153c\\\"\",\"branch\":\"\\\"master\\\"\",\"repoName\":\"\\\"test\\\"\",\"repoUri\":\"\\\"https://link.se\\\"\"},\"customData\":[]},\"links\":[{\"type\":\"CHANGE\",\"target\":\"14418a3d-5cf2-4d27-8b54-8d307b790164\"}]}")
                .getAsJsonObject();
    }

    private JsonObject getGerritCreatedEvent() {
        return new JsonParser()
                .parse("{\"uploader\":{\"name\":\"Test Testsson\",\"email\":\"test@mail.com\",\"username\":\"test\"},\"patchSet\":{\"number\":1,\"revision\":\"833156812815a0649b1171a44fdef8856d637a56\",\"parents\":[\"cb438f3e6c0d2c0cf77c8b013d5b019206e09e66\"],\"ref\":\"refs/changes/95/394895/1\",\"uploader\":{\"name\":\"Test Testsson\",\"email\":\"test@mail.com\",\"username\":\"test\"},\"createdOn\":1562576289,\"author\":{\"name\":\"Test Testsson\",\"email\":\"test@mail.com\",\"username\":\"test\"},\"kind\":\"REWORK\",\"sizeInsertions\":5,\"sizeDeletions\":0},\"change\":{\"project\":\"apps/utils/test\",\"branch\":\"master\",\"id\":\"Icf5666b32dc733440666910faa612b998e44ced6\",\"number\":394895,\"subject\":\"I did something good\",\"owner\":{\"name\":\"Test Testsson\",\"email\":\"test@test.com\",\"username\":\"test\"},\"url\":\"https://this.is.a.url\",\"commitMessage\":\"Very good. Much Work\",\"createdOn\":1562576289,\"status\":\"NEW\",\"wip\":true},\"project\":\"apps/utils/test\",\"refName\":\"refs/heads/master\",\"changeKey\":{\"id\":\"Icf5666b32dc733440666910faa612b998e44ced6\"},\"type\":\"patchset-created\",\"eventCreatedOn\":1562576293}")
                .getAsJsonObject();
    }

    private JsonObject getGerritMergedEvent() {
        return new JsonParser()
                .parse("{\"submitter\":{\"name\":\"Test Testsson\",\"email\":\"test@mail.com\",\"username\":\"test\"},\"newRev\":\"67c0283490033c8b06dbd9b18ade062c18b3825a\",\"patchSet\":{\"number\":4,\"revision\":\"67c0283490033c8b06dbd9b18ade062c18b3825a\",\"parents\":[\"48bedd5f006b69a22245cec5acb8d9feab001005\"],\"ref\":\"refs/changes/30/394730/4\",\"uploader\":{\"name\":\"Test Testsson\",\"email\":\"test@mail.com\",\"username\":\"test\"},\"createdOn\":1562654460,\"author\":{\"name\":\"Test Testsson\",\"email\":\"test@mail.com\",\"username\":\"test\"},\"kind\":\"TRIVIAL_REBASE\",\"sizeInsertions\":0,\"sizeDeletions\":0},\"change\":{\"project\":\"layers/meta-test\",\"branch\":\"master\",\"id\":\"Ic9752f5819304951194dcbc48baead626b409211\",\"number\":394730,\"subject\":\"Doing things\",\"owner\":{\"name\":\"test\",\"email\":\"test@mail.com\",\"username\":\"test\"},\"url\":\"https://this.is.a.url\",\"commitMessage\":\"Did much work, stayed up all night\",\"hashtags\":[\"atom/1b7ae7d9618c0630c1747076943f083600622983\"],\"createdOn\":1562328923,\"status\":\"MERGED\"},\"project\":\"layers/meta-test\",\"refName\":\"refs/heads/master\",\"changeKey\":{\"id\":\"Icf5666b32dc733440666910faa612b998e44ced6\"},\"type\":\"change-merged\",\"eventCreatedOn\":1562654464}")
                .getAsJsonObject();
    }

    private JsonObject getGerritMergedEventFalse() {
        return new JsonParser()
                .parse("{\"submitter\":{\"name\":\"Test Testsson\",\"email\":\"test@mail.com\",\"username\":\"test\"},\"newRev\":\"67c0283490033c8b06dbd9b18ade062c18b3825a\",\"patchSet\":{\"number\":4,\"revision\":\"67c0283490033c8b06dbd9b18ade062c18b3825a\",\"parents\":[\"48bedd5f006b69a22245cec5acb8d9feab001005\"],\"ref\":\"refs/changes/30/394730/4\",\"uploader\":{\"name\":\"Test Testsson\",\"email\":\"test@mail.com\",\"username\":\"test\"},\"createdOn\":1562654460,\"author\":{\"name\":\"Test Testsson\",\"email\":\"test@mail.com\",\"username\":\"test\"},\"kind\":\"TRIVIAL_REBASE\",\"sizeInsertions\":0,\"sizeDeletions\":0},\"change\":{\"project\":\"layers/meta-test\",\"branch\":\"master\",\"id\":\"Ic9752f5819304951194dcbc48baead626b409211\",\"number\":394730,\"subject\":\"Doing things\",\"owner\":{\"name\":\"test\",\"email\":\"test@mail.com\",\"username\":\"test\"},\"url\":\"https://this.is.a.url\",\"commitMessage\":\"Did much work, stayed up all night\",\"hashtags\":[\"atom/1b7ae7d9618c0630c1747076943f083600622983\"],\"createdOn\":1562328923,\"status\":\"MERGED\"},\"project\":\"layers/meta-test\",\"refName\":\"refs/heads/master\",\"changeKey\":{\"id\":\"Icf5666b32dc733440666910faa612b99leet1337\"},\"type\":\"change-merged\",\"eventCreatedOn\":1562654464}")
                .getAsJsonObject();
    }
}
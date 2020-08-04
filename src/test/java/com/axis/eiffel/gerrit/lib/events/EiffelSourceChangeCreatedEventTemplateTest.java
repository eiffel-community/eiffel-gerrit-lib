/*
 * Copyright 2020 Axis Communications AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.axis.eiffel.gerrit.lib.events;

import com.ericsson.eiffel.remrem.semantics.SemanticsService;
import com.ericsson.eiffel.semantics.events.GitIdentifier;
import com.ericsson.eiffel.semantics.events.Source;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.axis.eiffel.gerrit.lib.formatter.TestResourceType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Magnus Bäck, magnus.back@axis.com
 */
@DisplayName("Testing EiffelSourceChangeCreatedEventTemplate")
public class EiffelSourceChangeCreatedEventTemplateTest {

    private final SemanticsService semanticsService = new SemanticsService();

    @Test
    @DisplayName("Testing the contents of the resulting event data")
    void TestData() {
        EiffelSourceChangeCreatedEventTemplate template = new EiffelSourceChangeCreatedEventTemplate(semanticsService);
        template.generateTemplate(GERRIT_CREATED_EVENT.load());

        GitIdentifier gitIdentifier = template.getEvent().getData().getGitIdentifier();
        assertEquals("master", gitIdentifier.getBranch());
        assertEquals("Icf5666b32dc733440666910faa612b998e44ced6", gitIdentifier.getCommitId());
        assertEquals("apps/utils/test", gitIdentifier.getRepoName());
        assertEquals("https://this.is.a.url", gitIdentifier.getRepoUri());
    }

    @Test
    @DisplayName("Testing the contents of the resulting event metadata")
    void TestMeta() {
        EiffelSourceChangeCreatedEventTemplate template = new EiffelSourceChangeCreatedEventTemplate(semanticsService);
        template.generateTemplate(GERRIT_CREATED_EVENT.load());

        Source source = template.getEvent().getMeta().getSource();
        assertEquals("", source.getDomainId());
        assertEquals("https://this.is.a.url", source.getHost());
        assertEquals("Test Testsson", source.getName());
        assertEquals("", source.getUri());
    }
}

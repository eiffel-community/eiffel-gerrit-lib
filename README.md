<!---
   Copyright 2018 Ericsson AB.
   For a full list of individual contributors, please see the commit history.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
--->

<img src="./images/logo.png" alt="Gerrit to Eiffel event converter" width="350"/>

[![Sandbox badge](https://img.shields.io/badge/Stage-Sandbox-yellow)](https://github.com/eiffel-community/community/blob/master/PROJECT_LIFECYCLE.md#stage-sandbox)
[![](https://jitpack.io/v/eiffel-community/eiffel-gerrit-lib.svg)](https://jitpack.io/#eiffel-community/eiffel-gerrit-lib)

# Gerrit to Eiffel event converter

This library converts Gerrit events into Eiffel events. For now, only two types of events is supported. The library can be extended with more events. All Eiffel events are generated and validated with the help of [RemRem Semantics](https://github.com/eiffel-community/eiffel-remrem-semantics). Supported events can be found in the table below. 

# About this repository

The contents of this repository are licensed under the [Apache License 2.0](./LICENSE).

To get involved, please see [Code of Conduct](https://github.com/eiffel-community/.github/blob/master/CODE_OF_CONDUCT.md) and [contribution guidelines](https://github.com/eiffel-community/.github/blob/master/CONTRIBUTING.md).

# About Eiffel

This repository forms part of the Eiffel Community. Eiffel is a protocol for technology agnostic machine-to-machine communication in continuous integration and delivery pipelines, aimed at securing scalability, flexibility and traceability. Eiffel is based on the concept of decentralized real time messaging, both to drive the continuous integration and delivery system and to document it.

Visit [Eiffel Community](https://eiffel-community.github.io) to get started and get involved.

# Events

Supported events:

| Gerrit                           | Eiffel                           |
| -------------------------------- | -------------------------------- |
| [Patchset Created](https://gerrit-review.googlesource.com/Documentation/cmd-stream-events.html#_patchset_created)                 | [EiffelSourceChangeCreatedEvent](https://github.com/eiffel-community/eiffel/blob/master/eiffel-vocabulary/EiffelSourceChangeCreatedEvent.md)   |
| [Change Merged](https://gerrit-review.googlesource.com/Documentation/cmd-stream-events.html#_change_merged)                    | [EiffelSourceChangeSubmittedEvent](https://github.com/eiffel-community/eiffel/blob/master/eiffel-vocabulary/EiffelSourceChangeSubmittedEvent.md) |

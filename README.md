# Gerrit to Eiffel Event

This library converts Gerrit events into Eiffel events. For now, only two types of events is supported. The library can be extended with more events. All Eiffel events are generated and validated with the help of [RemRem Semantics](https://github.com/eiffel-community/eiffel-remrem-semantics). Supported events can be found in the table below. 

This library is part of the [Eiffel Community](https://eiffel-community.github.io/).

Read more about the Eiffel protocol on https://github.com/eiffel-community/eiffel.

## Events

### Gerrit events represented in Eiffel

| Gerrit                           | Eiffel                           |
| -------------------------------- | -------------------------------- |
| [Patchset Created](https://gerrit-review.googlesource.com/Documentation/cmd-stream-events.html#_patchset_created)                 | [EiffelSourceChangeCreatedEvent](https://github.com/eiffel-community/eiffel/blob/master/eiffel-vocabulary/EiffelSourceChangeCreatedEvent.md)   |
| [Change Merged](https://gerrit-review.googlesource.com/Documentation/cmd-stream-events.html#_change_merged)                    | [EiffelSourceChangeSubmittedEvent](https://github.com/eiffel-community/eiffel/blob/master/eiffel-vocabulary/EiffelSourceChangeSubmittedEvent.md) |

## Usage

TODO

## Maintainers
 * Christian Bilevits
    - <christian.bilevits@axis.com>

## License
[Apache-2.0](LICENSE)

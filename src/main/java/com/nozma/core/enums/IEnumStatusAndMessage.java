package com.nozma.core.enums;

public sealed interface IEnumStatusAndMessage
        permits StatusAndMessage{
    String getMessage();
}

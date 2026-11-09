package com.demo.payment_service.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo (use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Card.class, name = "CARD"),
    @JsonSubTypes.Type(value = Upi.class, name = "UPI"),
    @JsonSubTypes.Type(value = NetBanking.class, name = "NET_BANKING")
})

public sealed interface PaymentMethod permits Card, Upi, NetBanking {}

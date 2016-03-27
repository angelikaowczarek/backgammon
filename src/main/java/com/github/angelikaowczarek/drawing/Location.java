package com.github.angelikaowczarek.drawing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Lombok plugin + annotation processor (ctrl + alt + s lub File->Settings)
@Getter
@Setter
@AllArgsConstructor
public class Location {
    private int x;
    private int y;
}

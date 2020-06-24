package com.java.relay42.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
}

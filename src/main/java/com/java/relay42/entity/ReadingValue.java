package com.java.relay42.entity;

import java.math.BigInteger;

/**
 * Interface used to fetch reading value instead of ArrayBackedRow.
 */
public interface ReadingValue {
    BigInteger getReading();
}

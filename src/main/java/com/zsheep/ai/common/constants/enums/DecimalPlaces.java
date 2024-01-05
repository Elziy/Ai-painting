package com.zsheep.ai.common.constants.enums;

/**
 * 小数点位数
 *
 * @author Elziy
 */
public enum DecimalPlaces {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10);
    
    private final int value;
    
    DecimalPlaces(int value) {
        this.value = value;
    }
    
    public int value() {
        return value;
    }
}


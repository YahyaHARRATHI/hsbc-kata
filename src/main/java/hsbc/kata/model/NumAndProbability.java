package hsbc.kata.model;

import lombok.Getter;

@Getter
public class NumAndProbability {

    private final int number;
    private final float probabilityOfSample;

    public NumAndProbability(int number, float probabilityOfSample) {
        this.number = number;
        this.probabilityOfSample = probabilityOfSample;
    }
}
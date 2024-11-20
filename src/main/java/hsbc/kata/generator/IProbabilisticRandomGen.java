package hsbc.kata.generator;

import hsbc.kata.exception.ProbabilityException;

public interface IProbabilisticRandomGen {
     int nextFromSample() throws ProbabilityException;
}

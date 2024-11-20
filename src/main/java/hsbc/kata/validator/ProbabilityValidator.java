package hsbc.kata.validator;

import hsbc.kata.model.NumAndProbability;

import java.util.List;

public interface ProbabilityValidator {
    void validate(List<NumAndProbability> numProbList, float epsilon);
}

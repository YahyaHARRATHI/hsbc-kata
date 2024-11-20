package hsbc.kata.validator;

import hsbc.kata.exception.ProbabilityValidationException;
import hsbc.kata.model.NumAndProbability;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProbabilityValidatorImplTest {
    @Test
    void validate_shouldThrowExceptionWhenProbabilityIsLessThanZero() {
        ProbabilityValidator validator = new ProbabilityValidatorImpl();
        List<NumAndProbability> numProbList = new ArrayList<>();
        numProbList.add(new NumAndProbability(1, -0.1f));
        float epsilon = 0.01f;

        assertThrows(ProbabilityValidationException.class, () -> validator.validate(numProbList, epsilon));
    }

    @Test
    void validate_shouldNotThrowExceptionWhenAllProbabilitiesAreWithinEpsilonOfOne() {
        ProbabilityValidator validator = new ProbabilityValidatorImpl();
        List<NumAndProbability> numProbList = new ArrayList<>();
        numProbList.add(new NumAndProbability(1, 0.999f));
        numProbList.add(new NumAndProbability(2, 0.001f));
        float epsilon = 0.01f;

        assertDoesNotThrow(() -> validator.validate(numProbList, epsilon));
    }

    @Test
    void validate_shouldThrowExceptionWhenTotalProbabilityIsNotEqualToOneWithinEpsilon() {
        ProbabilityValidator validator = new ProbabilityValidatorImpl();
        List<NumAndProbability> numProbList = new ArrayList<>();
        numProbList.add(new NumAndProbability(1, 0.5f));
        numProbList.add(new NumAndProbability(2, 0.3f));
        float epsilon = 0.01f;

        assertThrows(ProbabilityValidationException.class, () -> validator.validate(numProbList, epsilon));
    }

    @Test
    void validate_shouldThrowExceptionWhenProbabilityIsGreaterThanOne() {
        ProbabilityValidator validator = new ProbabilityValidatorImpl();
        List<NumAndProbability> numProbList = new ArrayList<>();
        numProbList.add(new NumAndProbability(1, 1.1f));
        float epsilon = 0.01f;

        assertThrows(ProbabilityValidationException.class, () -> validator.validate(numProbList, epsilon));
    }
}
import hsbc.kata.exception.ProbabilityException;
import hsbc.kata.generator.ProbabilisticRandomGenImpl;
import hsbc.kata.model.NumAndProbability;
import hsbc.kata.random.gen.RandomFloatGenerator;
import hsbc.kata.validator.ProbabilityValidator;
import hsbc.kata.validator.ProbabilityValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.fail;


public class ProbTest {
    @Test
    public void nextFromSample_shouldReturn1() {
        List<NumAndProbability> numProbList = List.of(
                new NumAndProbability(1, 0.9f),
                new NumAndProbability(2, 0.1f)
        );
        Random randomMock = Mockito.mock(Random.class);
        Mockito.when(randomMock.nextFloat()).thenReturn(0.7f).thenReturn(0.899999f);
        ProbabilityValidator probabilityValidator = new ProbabilityValidatorImpl();
        ProbabilisticRandomGenImpl probabilisticRandomGen = new ProbabilisticRandomGenImpl(numProbList, probabilityValidator, new RandomFloatGenerator(randomMock), 0.0001f);
        try {
            Assertions.assertEquals(1, probabilisticRandomGen.nextFromSample());
            Assertions.assertEquals(1, probabilisticRandomGen.nextFromSample());
        } catch (ProbabilityException e) {
            fail("Unexpected exception", e);
        }
    }


    @Test
    public void nextFromSample_shouldReturn2() {

        List<NumAndProbability> numProbList = List.of(
                new NumAndProbability(1, 0.9f),
                new NumAndProbability(2, 0.1f)
        );
        Random randomMock = Mockito.mock(Random.class);
        Mockito.when(randomMock.nextFloat()).thenReturn(0.95f).thenReturn(0.01f);
        ProbabilityValidator probabilityValidator = new ProbabilityValidatorImpl();
        ProbabilisticRandomGenImpl probabilisticRandomGen = new ProbabilisticRandomGenImpl(numProbList, probabilityValidator, new RandomFloatGenerator(randomMock), 0.0001f);

        try {
            Assertions.assertEquals(2, probabilisticRandomGen.nextFromSample());
            Assertions.assertEquals(1, probabilisticRandomGen.nextFromSample());
        } catch (ProbabilityException e) {
            fail("Unexpected exception", e);
        }
    }
}
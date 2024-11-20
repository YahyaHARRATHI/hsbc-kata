package hsbc.kata.generator;

import hsbc.kata.exception.ProbabilityException;
import hsbc.kata.model.NumAndProbability;
import hsbc.kata.random.gen.IRandomGen;
import hsbc.kata.validator.ProbabilityValidator;

import java.util.List;

public class ProbabilisticRandomGenImpl implements IProbabilisticRandomGen {

    private final List<NumAndProbability> numProbList;
    private final ProbabilityValidator probabilityValidator;
    private final IRandomGen randomGen;


    public ProbabilisticRandomGenImpl(List<NumAndProbability> numProbList, ProbabilityValidator probabilityValidator, IRandomGen randomGen, float epsilon) {
        this.probabilityValidator = probabilityValidator;
        this.randomGen = randomGen;


        validateProbabilities(numProbList, epsilon);
        this.numProbList = numProbList;
    }

    private void validateProbabilities(List<NumAndProbability> numProbList, float epsilon) {
        probabilityValidator.validate(numProbList, epsilon);
    }

    @Override
    public int nextFromSample() throws ProbabilityException {
        float rand = randomGen.getRandomFloat();
        for (NumAndProbability numProb : numProbList) {
            if (rand <= numProb.getProbabilityOfSample()) {
                return numProb.getNumber();
            }
            rand -= numProb.getProbabilityOfSample();
        }
        throw new ProbabilityException("Impossible de sélectionner un élément - probabilités incorrectes : " + rand);
    }
}

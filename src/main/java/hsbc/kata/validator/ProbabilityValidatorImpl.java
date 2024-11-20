package hsbc.kata.validator;

import hsbc.kata.exception.ProbabilityValidationException;
import hsbc.kata.model.NumAndProbability;

import java.util.List;

public class ProbabilityValidatorImpl implements ProbabilityValidator {


    @Override
    public void validate(List<NumAndProbability> numProbList, float epsilon) {
        if (numProbList == null || numProbList.isEmpty()) {
            throw new ProbabilityValidationException("La liste ne doit pas être vide.");
        }
        float totalProbability = 0;
        for (NumAndProbability numProb : numProbList) {
            if (numProb.getProbabilityOfSample() < 0 || numProb.getProbabilityOfSample() > 1) {
                throw new ProbabilityValidationException("Les probabilités doivent être entre 0 et 1.");
            }
            totalProbability += numProb.getProbabilityOfSample();
        }

        if (Math.abs(1.0f - totalProbability) > epsilon) {
            throw new ProbabilityValidationException("La somme des probabilités doit être égale à 1.");
        }
    }
}

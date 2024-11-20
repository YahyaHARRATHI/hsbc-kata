package hsbc.kata.random.gen;

import java.util.Random;

public class RandomFloatGenerator implements IRandomGen {

    private final Random random;

    public RandomFloatGenerator(Random random) {
        this.random = random;
    }

    @Override
    public float getRandomFloat() {
        return random.nextFloat();
    }


}

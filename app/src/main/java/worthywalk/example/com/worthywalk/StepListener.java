package worthywalk.example.com.worthywalk;

/**
 * Created by Mujtaba on 8/6/2018.
 */

    public interface StepListener {

        public void step(long timeNs);

    void registerListener(StepCounterService stepCounterService);
}

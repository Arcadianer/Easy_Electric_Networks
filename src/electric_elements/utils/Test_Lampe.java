package electric_elements.utils;

import electric_elements.basic_elements.Device;

public class Test_Lampe extends Device {

    public Test_Lampe(double standart_drain, double extra_drain) {
        super(standart_drain, extra_drain);
    }

    @Override
    protected void when_active() {
        System.out.println("LAMPE EIN");
    }

    @Override
    protected void when_inactive() {
        System.out.println("LAMPE AUS");
    }

    @Override
    public void when_power_update_layer_1() {

    }

    @Override
    protected void final_shutdown_layer2() {

    }


}

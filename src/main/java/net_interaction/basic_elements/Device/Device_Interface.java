package net_interaction.basic_elements.Device;

import electric_elements.basic_elements.Device;

public class Device_Interface extends Device {
    public Device_Interface(String Name, double standart_drain, double extra_drain) {
        super(Name, standart_drain, extra_drain);
    }

    @Override
    protected void when_active() {

    }

    @Override
    protected void when_inactive() {

    }

    @Override
    protected void when_power_update() {

    }
}

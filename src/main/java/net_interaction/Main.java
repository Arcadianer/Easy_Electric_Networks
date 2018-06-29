package net_interaction;


import electric_elements.basic_elements.Power_Grid;
import electric_elements.power_provider.Battery;
import electric_elements.power_provider.Battery_Charger;
import electric_elements.utils.Network_Status_Enum;
import electric_elements.utils.Power_Grid_Monitor;
import electric_elements.utils.Test_Lampe;
import net_interaction.basic_elements.Device.Device_Controller;
import sim_systems.ObjectDB;

import java.io.IOException;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        ObjectDB.createLogger();
        ObjectDB.log.setLevel(Level.ALL);
        ObjectDB.log.info("Hellu");

        Device_Controller device_controller = new Device_Controller("TEST_Device", 0, 0) {
            @Override
            protected void when_active() {

            }

            @Override
            protected void when_inactive() {

            }

            @Override
            protected void when_power_update() {

            }
        };


        //ObjectDB.shutdown();

    }


}

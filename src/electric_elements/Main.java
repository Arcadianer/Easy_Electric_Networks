package electric_elements;


import electric_elements.basic_elements.Network;
import electric_elements.power_provider.Battery;
import electric_elements.utils.Battery_Charger;
import electric_elements.utils.Test_Lampe;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        Network main_power_net=new Network("Main-Power");

        Battery main_batt_1=new Battery(1000,10000);
        main_batt_1.setName("Main-Battery");

        Battery secondary_batt=new Battery(100,1000);
        secondary_batt.remove_capacity(900);
        secondary_batt.setName("Backup-Battery");


        Battery_Charger secondary_batt_charger=new Battery_Charger(1,100,10000);
        secondary_batt_charger.setName("Backup-Battery-Charger");

        Test_Lampe test_lampe=new Test_Lampe(-10,-20);
        test_lampe.setName("Lampe");

        main_power_net.attach(main_batt_1);

        secondary_batt.attach(main_power_net);

        secondary_batt_charger.attach_battery(secondary_batt);

        secondary_batt_charger.attach(main_power_net);

        test_lampe.attach(main_power_net);


        System.out.println(main_power_net.get_current_status_log());
        main_batt_1.turn_on();
        main_batt_1.set_percent_extra((float) 0.5);

        secondary_batt.turn_on();
        secondary_batt.set_chaging_to(40);

        test_lampe.turn_on();
        Thread.sleep(1000);
        System.out.println(main_power_net.get_current_status_log());



        //ObjectDB.shutdown();

    }

}

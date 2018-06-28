package electric_elements;


import electric_elements.basic_elements.Power_Grid;
import electric_elements.power_provider.Battery;
import electric_elements.utils.Battery_Charger;
import electric_elements.utils.Network_Status_Enum;
import electric_elements.utils.Power_Grid_Monitor;
import electric_elements.utils.Test_Lampe;
import sim_systems.ObjectDB;

import java.io.IOException;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        ObjectDB.createLogger();
        ObjectDB.log.setLevel(Level.ALL);
        ObjectDB.log.info("Hellu");
        Power_Grid main_power_net=new Power_Grid("Main-Power");

        Battery main_batt_1=new Battery(10000,10000);
        //main_batt_1.setDont_draw(true);
        main_batt_1.setName("Main-Battery");

        Battery secondary_batt=new Battery(100,1000);
        secondary_batt.remove_capacity(900);
        secondary_batt.setName("Backup-Battery");


        Battery_Charger secondary_batt_charger=new Battery_Charger(1,9000,10000);
        secondary_batt_charger.setName("Backup-Battery-Charger");

        Test_Lampe test_lampe=new Test_Lampe(-10,-20);
        test_lampe.setName("Lampe");

        Power_Grid_Monitor monitor=new Power_Grid_Monitor();
        monitor.setName("Monitor");
        monitor.attach(main_power_net);
        monitor.turn_on();


        main_power_net.attach(main_batt_1);

        secondary_batt.attach(main_power_net);

        secondary_batt_charger.attach_battery(secondary_batt);

        secondary_batt_charger.attach(main_power_net);

        test_lampe.attach(main_power_net);

        main_batt_1.turn_on();
        main_batt_1.set_watt_output(10000);



       secondary_batt.turn_on();
        secondary_batt.set_chaging_to(-9000);

        test_lampe.turn_on();

        System.out.println(main_power_net.get_current_status_log());

        Thread destroy_something=new Thread(()->{
            try {
                Thread.sleep(10000);

                test_lampe.set_to_draw(-30);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        destroy_something.start();
        Thread.sleep(1000);
        while(true){
            Thread.sleep(1000);
            if(main_power_net.getStatus()!=Network_Status_Enum.OK){

                double test=main_batt_1.get_current_drain()-main_power_net.get_available_energy();
                main_batt_1.set_watt_output(
                    test
                );
            }
        }


        //ObjectDB.shutdown();

    }

}

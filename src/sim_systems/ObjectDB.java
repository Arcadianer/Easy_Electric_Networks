package sim_systems;

import electric_elements.basic_elements.Device;
import electric_elements.basic_elements.Power_Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ObjectDB {

    public static Logger log;
   static final List<Device> deviceList=new ArrayList<Device>();
   static final List<Power_Grid> POWER_GRID_LIST =new ArrayList<Power_Grid>();

    public static void shutdown(){
        System.out.println("[SIMSYSTEM] init final shutdown");
        for (Device D:deviceList
             ) {
            D.final_shutdown_layer_1();

        }
        for (Power_Grid n: POWER_GRID_LIST
             ) {
            n.final_shutdown();
        }
        System.out.println("[SIMSYSTEM] final shutdown done");


    }
    public static void add(Device toadd){
        if(!deviceList.contains(toadd))
            deviceList.add(toadd);
    }
    public static void add(Power_Grid toadd){
        if(!POWER_GRID_LIST.contains(toadd))
            POWER_GRID_LIST.add(toadd);
    }
    public static void createLogger(){
        log=Logger.getLogger(ObjectDB.class.getName());
    }
}

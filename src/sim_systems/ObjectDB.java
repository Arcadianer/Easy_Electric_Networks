package sim_systems;

import electric_elements.basic_elements.Device;
import electric_elements.basic_elements.Network;

import java.util.ArrayList;
import java.util.List;

public class ObjectDB {
   static final List<Device> deviceList=new ArrayList<Device>();
   static final List<Network> networkList=new ArrayList<Network>();

    public static void shutdown(){
        System.out.println("[SIMSYSTEM] init final shutdown");
        for (Device D:deviceList
             ) {
            D.final_shutdown_layer_1();

        }
        for (Network n:networkList
             ) {
            n.final_shutdown();
        }
        System.out.println("[SIMSYSTEM] final shutdown done");


    }
    public static void add(Device toadd){
        if(!deviceList.contains(toadd))
            deviceList.add(toadd);
    }
    public static void add(Network toadd){
        if(!networkList.contains(toadd))
            networkList.add(toadd);
    }
}

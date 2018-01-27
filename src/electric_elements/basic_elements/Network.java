package electric_elements.basic_elements;

import sim_systems.ObjectDB;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Network
{
   //Attribs
   private final String Name;
    private final ArrayList<Device> attached;
    private double available_energy;
    private final ExecutorService thread_pool = Executors.newCachedThreadPool();
    private static int class_counter=0;

    //Runnables
    private final Runnable update_runnable=new Runnable() {
        @Override
        public void run() {
            available_energy = 0;
            for (Device D : attached) {

                available_energy = available_energy + D.get_current_drain();

            }
            System.out.println("Netzwerk power "+available_energy);
            for (Device D:attached){
                D.check_power();
            }
        }
    };

    public Network() {
        ObjectDB.add(this);
      attached=new ArrayList<Device>();
      available_energy=0;
      class_counter++;
      Name="Power Bus "+class_counter;
    }
    public Network(String Name) {
        ObjectDB.add(this);
        attached=new ArrayList<Device>();
        available_energy=0;
        this.Name=Name;
        class_counter++;
    }

    //Functions
    public boolean is_attached_to(Device D){
        return attached.contains(D);
    }
    public double get_available_energy(){
        return available_energy;
    }
    public void attach(Device D){
        if(!is_attached_to(D)){

                attached.add(D);
                D.attach(this);

        }
    }
    public void detach(Device D)
    {
        if(is_attached_to(D)){
            attached.remove(D);
        }
    }
    public void update(){
        thread_pool.execute(update_runnable);
    }


    public String get_current_status_log()
    {

        String status="";
        if(get_available_energy()>0){
            status="OVERPOWERED";
        }else if(get_available_energy()==0)
        {
            status="OK";
        }else if(get_available_energy()<0)
        {
            status="UNDERPOWERED (ACTION REQUIRED)";
        }

        StringBuilder log=new StringBuilder();
        log.append("================= Powergrid Status =================\n");

        log.append("    Name            : "+Name+"\n");
        log.append("    Attached devices: "+attached.size()+"\n");
        log.append("    Available energy: "+available_energy+" Watt"+"\n");
        log.append("    Status          : "+status+"\n");
        log.append("----------------- Device List ----------------------"+"\n\n");



        int index=0;
        int max_length=0;
        for (Device D:attached)
        {
           log.append(D.toString()+"\n");
            index++;

        }
        log.append("\n\n===================================================="+"\n");



    return log.toString();
    }
    public void final_shutdown(){
        thread_pool.shutdownNow();
    }
}
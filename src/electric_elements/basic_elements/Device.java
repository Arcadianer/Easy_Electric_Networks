package electric_elements.basic_elements;

import sim_systems.ObjectDB;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Device
{
    private String Name;
    private boolean on_off;
    private boolean active;
    private float percent_extra_drain;
    private final double standart_drain;
    private final double extra_drain;
    private double current_drain;
    private Network attached_network;
    private boolean is_running=false;
    private static int class_counter=0;
    private ExecutorService device_thread_pool;
    private final Runnable device_standart_runnable=new Runnable() {
        @Override
        public void run() {
            if(active){
                if(!is_running){
                when_active();
                is_running=true;
                }
            }else{
                if(is_running){
                when_inactive();
                is_running=false;
                device_thread_pool.shutdownNow();
                }
            }
        }
    };
    private final Runnable device_power_update= () -> when_power_update_layer_1();

    protected Device(double standart_drain, double extra_drain) {
        ObjectDB.add(this);
        device_thread_pool=Executors.newCachedThreadPool();
        on_off=false;
        active=false;
        current_drain=0;
        this.standart_drain = standart_drain;
        this.extra_drain = extra_drain;
        class_counter++;
        this.Name="electric_elements.basic_elements.Device "+class_counter;
    }
    public Device(String Name,double standart_drain, double extra_drain) {

        ObjectDB.add(this);
        device_thread_pool = Executors.newCachedThreadPool();
        on_off=false;
        active=false;
        current_drain=0;
        this.standart_drain = standart_drain;
        this.extra_drain = extra_drain;
        class_counter++;
        this.Name=Name;
    }

    protected abstract void when_active();
    protected abstract void when_inactive();

    public void turn_on(){
        if(!on_off){
    on_off=true;

    update_drain();
        }
    }
    public void turn_off(){
        if(on_off){
        on_off=true;
    update_drain();
        }
    }
    public void set_percent_extra(float percent_extra){
        if((percent_extra>=0)&&(percent_extra_drain<=1)){
        percent_extra_drain=percent_extra;
        update_drain();
        }else{
            throw new IllegalArgumentException("argument needs to be between 1 and 0");
        }
    }

    public float get_percent_extra(){
        return percent_extra_drain;
    }

    private void set_active(){
        if(!active){
        active=true;
        device_thread_pool=Executors.newCachedThreadPool();
        device_thread_pool.execute(device_standart_runnable);
        }
    }
    private void set_inactive(){
       if(active){
        active=false;
        device_thread_pool.execute(device_standart_runnable);



       }
    }

   public double get_current_drain(){
        return current_drain;
    }

    protected void update_drain(){
        current_drain=0;
        if(on_off){
            current_drain=standart_drain;
            double toadd=extra_drain*percent_extra_drain;
            current_drain=current_drain+toadd;
        }
            if(is_attached()){
            attached_network.update();
            }

    }
    public void attach(Network net){
        if(!is_attached()){
            attached_network=net;
            net.attach(this);

        }


    }
    public void retach(Network net){
        if((!net.is_attached_to(this)&&(is_attached()))){
            detach();
            attached_network=net;
            net.attach(this);



        }

    }
    public void detach(){
        if(is_attached()) {
            Network temp_net=attached_network;
            attached_network = null;
            temp_net.detach(this);
        }
    }
    public boolean is_attached(){
        return !(attached_network == null);
    }
    public void check_power(){

        device_thread_pool.execute(device_power_update);
        if(isOn_off()){
        if(attached_network.get_available_energy()<0){
            set_inactive();
        }else{
            set_active();
        }
        }
    }

    public boolean isOn_off() {
        return on_off;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    protected abstract void when_power_update_layer_1();

    public double get_max_power_draw(){
        return standart_drain+extra_drain;
    }
    public boolean set_to_draw(double watt){
        watt=-(watt);
        if(standart_drain>=watt&&watt>=(standart_drain+extra_drain)){
            if(watt==standart_drain){
                set_percent_extra(0);
            }else{
                double toset=watt/(standart_drain+extra_drain);
                set_percent_extra((float) toset);

            }
            return true;
        }
        return false;
    }
    private void runn_task(Runnable command){
        if(is_running)
        device_thread_pool.execute(command);
    }


    public double getStandart_drain() {
        return standart_drain;
    }

    public double getExtra_drain() {
        return extra_drain;
    }

    @Override
    public String toString() {
        return Name+"{" +
                "on_off=" + on_off +
                ", active=" + active +
                ", percent_extra_drain=" + percent_extra_drain +
                ", current_drain=" + current_drain +
                '}';
    }
    public void final_shutdown_layer_1(){
        if(!(device_thread_pool==null))
        device_thread_pool.shutdownNow();
        final_shutdown_layer2();
    }
    protected abstract void final_shutdown_layer2();


}

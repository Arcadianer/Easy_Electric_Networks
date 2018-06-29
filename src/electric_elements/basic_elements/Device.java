package electric_elements.basic_elements;

import sim_systems.ObjectDB;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;


public abstract class Device
{
    protected Logger log;
    private String Name;
    private boolean on_off;
    private boolean active;
    private float percent_extra_drain;
    private final double standart_drain;
    private final double extra_drain;
    private double current_drain;
    private Power_Grid attached_powerGrid;
    private boolean is_running=false;
    private static int class_counter=0;
    private ExecutorService device_thread_pool;
    protected Thread_Pool_Factory factory;


    private final Runnable device_power_update= () -> when_power_update();

    private final Runnable device_standart_runnable = new Runnable() {
        @Override
        public void run() {
            if (active) {
                if (!is_running) {
                    when_active();
                    is_running = true;
                }
            } else {
                if (is_running) {
                    when_inactive();
                    is_running = false;

                }
            }
        }
    };




    protected Device(double standart_drain, double extra_drain) {
        ObjectDB.add(this);
        log=ObjectDB.log;
        this.Name="Device "+class_counter;
        factory=new Thread_Pool_Factory(Name);
        device_thread_pool=Executors.newCachedThreadPool(factory);
        on_off=false;
        active=false;
        current_drain=0;
        this.standart_drain = standart_drain;
        this.extra_drain = extra_drain;
        class_counter++;
        this.Name="Device "+class_counter;


    }
    public Device(String Name,double standart_drain, double extra_drain) {

        ObjectDB.add(this);
        log=ObjectDB.log;
        factory=new Thread_Pool_Factory(Name);
        device_thread_pool = Executors.newCachedThreadPool(factory);

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
            log.fine("turned on");
    update_drain();
        }
    }
    public void turn_off(){
        if(on_off){
        on_off=false;
            log.fine("turned off");
    update_drain();
        }
    }
    protected void set_percent_extra(float percent_extra){
        if((percent_extra>=0)&&(percent_extra_drain<=1)){
        percent_extra_drain=percent_extra;
        update_drain();
        }else{
            throw new IllegalArgumentException("argument needs to be between 1 and 0");
        }
    }

    protected float get_percent_extra(){
        return percent_extra_drain;
    }

    private void set_active(){
        if(!active){
        active=true;
        device_thread_pool=Executors.newCachedThreadPool(factory);
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


            current_drain=current_drain+Math.round(toadd*100)/100;
        }
            if(is_attached()){
            attached_powerGrid.update();
            }

    }
    public void attach(Power_Grid net){
        if(!is_attached()){
            attached_powerGrid =net;
            net.attach(this);

        }


    }
    public void retach(Power_Grid net){
        if((!net.is_attached_to(this)&&(is_attached()))){
            detach();
            attached_powerGrid =net;
            net.attach(this);



        }

    }
    public void detach(){
        if(is_attached()) {
            Power_Grid temp_net= attached_powerGrid;
            attached_powerGrid = null;
            temp_net.detach(this);
        }
    }
    public boolean is_attached(){
        return !(attached_powerGrid == null);
    }
    public void check_power(){

        if(isOn_off()){
        if(attached_powerGrid.get_available_energy()<0){
            set_inactive();

        }else{
            set_active();
        }
        }
        try {
            device_thread_pool.execute(device_power_update);
        } catch (RejectedExecutionException e) {
            log.warning(Name + " Thread cant be executed");
            e.printStackTrace();
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

    protected abstract void when_power_update();

    public double get_max_power_draw(){
        return standart_drain+extra_drain;
    }
    public boolean set_to_draw(double watt){

        if((standart_drain>=watt)&&(watt>=(standart_drain+extra_drain))){
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


    protected double getStandart_drain() {
        return standart_drain;
    }

    protected double getExtra_drain() {
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

    public void finalize_device() throws Throwable {
        super.finalize();
        turn_off();
        if(!(device_thread_pool==null))
            device_thread_pool.shutdownNow();
    }



    protected Power_Grid getAttached_powerGrid(){
        return attached_powerGrid;
    }



    protected class Thread_Pool_Factory implements ThreadFactory{
        ThreadGroup group;
        private AtomicInteger th_id;

        public Thread_Pool_Factory(String name) {
            group=new ThreadGroup(name);
        }

        @Override

        public Thread newThread(Runnable r) {

            Thread to_create=new Thread(group,r);

            return to_create;
        }
    }

    public boolean isActive() {
        return active;
    }
}

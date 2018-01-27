package electric_elements.utils;

import com.sun.istack.internal.NotNull;
import electric_elements.basic_elements.Device;
import electric_elements.power_provider.Battery;

import java.util.Timer;
import java.util.TimerTask;

public class Battery_Charger extends Device {
    public Battery_Charger(double standart_drain, double extra_drain,long charge_interval) {
        super(-standart_drain, -extra_drain);
        this.charge_interval=charge_interval;
    }



    private Battery attached_battery;
    private Timer charge_timer=new Timer();
    private final TimerTask charge_task=new TimerTask() {
        @Override
        public void run() {
            double factor_1=charge_interval/(60.0*60.0*1000.0);
            double to_charge=(get_current_drain()-getStandart_drain())*factor_1;

        attached_battery.add_capacity(to_charge);
        }
    };
    private final long charge_interval;

    @NotNull
    public void attach_battery(Battery battery){
        if(!is_attached_to_battery()){
            attached_battery=battery;
            battery.attach_charger(this);
            if(battery.isCharge()){
                turn_on();
            }else{
                turn_off();
            }
        }



    }
    public void detach_battery(){
        if(is_attached_to_battery()){
            Battery temp_batt=attached_battery;
            attached_battery=null;
            temp_batt.detach_charger();

        }



    }


    protected void when_active() {
        if(attached_battery==null)
            turn_off();
        charge_timer.scheduleAtFixedRate(charge_task,charge_interval,charge_interval);


    }

    protected void when_inactive() {
        charge_timer.cancel();
        charge_timer=new Timer();
        set_to_draw(0);
    }


    public void when_power_update_layer_1() {

    }

    @Override
    protected void final_shutdown_layer2() {
        charge_timer.cancel();
        charge_timer.purge();
    }

    public boolean is_attached_to_battery(){
        return null != attached_battery;
    }




}

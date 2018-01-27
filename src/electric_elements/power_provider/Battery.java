package electric_elements.power_provider;

import com.sun.istack.internal.NotNull;
import electric_elements.utils.Battery_Charger;

public class Battery extends Generator {
    public Battery(double max_power_output, double max_power_capacity) {

        super(max_power_output, max_power_capacity);
    }

    private boolean charge = false;
    private Battery_Charger charger;


    @Override
    protected void when_active() {

    }

    @Override
    protected void when_inactive() {

    }

    @Override
    public void when_power_update_layer_2() {

    }

    @Override
    protected void final_shutdown_layer3() {

    }

    public boolean isCharge() {
        return charge;
    }

    @NotNull
    public void attach_charger(Battery_Charger battery_charger) {
        if(!has_charger()){
        this.charger = battery_charger;
            battery_charger.attach_battery(this);
        }

    }
    public void detach_charger(){
        if(has_charger()){
            Battery_Charger temp_charger=charger;
            charger=null;
            temp_charger.detach_battery();
        }
    }

    public boolean set_chaging_to(double watt) {
        if (!(charger == null)) {

            if (watt <= 0) {
                charge = false;

                charger.turn_off();
            } else {
                charge = true;
                charger.turn_on();
                return charger.set_to_draw(watt);
            }

        }
        return false;

    }
    public boolean has_charger(){
        return !(charger == null);
    }
}

package electric_elements.utils;

import com.sun.istack.internal.NotNull;
import electric_elements.basic_elements.Device;
import electric_elements.power_provider.Battery;

import java.util.Timer;

public class Battery_Charger extends Device {
    public Battery_Charger(double standart_drain, double extra_drain, long charge_interval) {
        super(-standart_drain, -extra_drain);
        this.charge_interval = charge_interval;
    }


    private long charge_interval = 0;
    private Battery attached_battery;
    private Thread sleep_thread;
    private final Runnable charge_task = () -> {
        while (isOn_off()) {
            try {
                Thread.sleep(charge_interval);
            } catch (InterruptedException e) {

            }
            double factor_1 = charge_interval / (60.0 * 60.0 * 1000.0);
            double to_charge = (get_current_drain() - getStandart_drain()) * factor_1;

            attached_battery.add_capacity(to_charge);
        }

    };


    @NotNull
    public void attach_battery(Battery battery) {
        if (!is_attached_to_battery()) {
            attached_battery = battery;
            battery.attach_charger(this);
            if (battery.isCharge()) {
                turn_on();
            } else {
                turn_off();
            }
        }


    }

    public void detach_battery() {
        if (is_attached_to_battery()) {
            Battery temp_batt = attached_battery;
            attached_battery = null;
            temp_batt.detach_charger();

        }


    }


    protected void when_active() {
        if (attached_battery == null)
            turn_off();
        if (sleep_thread != null)
            sleep_thread.interrupt();
        sleep_thread = null;
        sleep_thread = factory.newThread(charge_task);
        sleep_thread.setName("charge_timer");
        sleep_thread.start();
    }

    protected void when_inactive() {
      sleep_thread.interrupt();
        sleep_thread=null;
    }

    @Override
    protected void when_power_update() {

    }


    @Override
    public void finalize_device() throws Throwable {

        sleep_thread.interrupt();
        sleep_thread=null;
        super.finalize_device();
    }

    public boolean is_attached_to_battery() {
        return null != attached_battery;
    }


}

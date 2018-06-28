package electric_elements.utils;

import electric_elements.basic_elements.Device;
import electric_elements.basic_elements.Power_Grid;

public class Power_Grid_Monitor extends Device {
    Thread monitor = new Thread(new Runnable() {
        @Override
        public void run() {
            try {

                if (is_attached()) {
                    Power_Grid temp = getAttached_powerGrid();
                    Network_Status_Enum prev_status = temp.getStatus();
                    while (isOn_off()) {
                        synchronized (this) {
                            wait();


                            Network_Status_Enum status_enum = temp.getStatus();
                            switch (status_enum) {
                                case OK:
                                    if (!(prev_status == Network_Status_Enum.OK)) {
                                        log.info("Power Grid " + temp.getName() + "is nominal (" + temp.get_available_energy() + " Watt)");
                                        log.fine("\n" + temp.get_current_status_log());
                                    }
                                    break;
                                case OVERPOWERED:
                                    if (!(prev_status == Network_Status_Enum.OVERPOWERED)) {
                                        log.info("Power Grid " + temp.getName() + "has to much Power (" + temp.get_available_energy() + " Watt)");
                                        log.fine("\n" + temp.get_current_status_log());
                                    }
                                    break;
                                case UNDERPOWERED:
                                    if (!(prev_status == Network_Status_Enum.UNDERPOWERED)) {
                                        log.warning("Power Grid " + temp.getName() + "needs more Power (" + temp.get_available_energy() + " Watt)");
                                        log.fine("\n" + temp.get_current_status_log());
                                    }
                                    break;
                            }
                            prev_status = status_enum;
                        }

                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    });

    public Power_Grid_Monitor() {
        super("Power_Monitor", 0, 0);
    }

    @Override
    protected void when_active() {
        monitor.run();
    }

    @Override
    protected void when_inactive() {
    }

    @Override
    protected void when_power_update_layer_1() {
        if(monitor.isAlive())
        notify();
    }

    @Override
    protected void final_shutdown_layer2() {
        monitor.stop();

    }
}

package electric_elements.utils;

import electric_elements.basic_elements.Device;
import electric_elements.basic_elements.Power_Grid;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Power_Grid_Monitor extends Device {
    boolean update = false;
    long updateintervall = 60000;
    Runnable monitor_run = ()->{
                   try {
                if (is_attached()) {
                    Power_Grid temp = getAttached_powerGrid();
                    Network_Status_Enum prev_status = temp.getStatus();
                    Instant prev_instant = Instant.now();
                    while (isOn_off()) {


                        if (update || prev_instant.plus(updateintervall, ChronoUnit.MILLIS).isBefore(Instant.now())) {
                                update=false;
                                Network_Status_Enum status_enum = temp.getStatus();
                                switch (status_enum) {
                                    case OK:
                                        if (!(prev_status == Network_Status_Enum.OK)) {
                                            log.info("Power Grid " + temp.getName() + " is nominal (" + temp.get_available_energy() + "Watt)");
                                            log.fine("\n" + temp.get_current_status_log());
                                        }
                                        break;
                                    case OVERPOWERED:

                                        log.info("Power Grid " + temp.getName() + " has to much Power (" + temp.get_available_energy() + " Watt)");
                                            log.fine("\n" + temp.get_current_status_log());

                                        break;
                                    case UNDERPOWERED:

                                        log.warning("Power Grid " + temp.getName() + " needs more Power (" + temp.get_available_energy() + " Watt)");
                                            log.fine("\n" + temp.get_current_status_log());

                                        break;
                                }
                                prev_status = status_enum;
                            prev_instant = Instant.now();
                            }
                        Thread.sleep(100);
                        }

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


    };
    Thread monitor;

    public Power_Grid_Monitor() {
        super("Power_Monitor", 0, 0);
        monitor=factory.newThread(monitor_run);
        monitor.setName("Monitor_Thread");
    }

    public Power_Grid_Monitor(long updatetime) {
        super("Power_Monitor", 0, 0);
        monitor = factory.newThread(monitor_run);
        monitor.setName("Monitor_Thread");
        this.updateintervall = updatetime;
    }

    @Override
    protected void when_active() {
        if(!monitor.isAlive())
        monitor.start();
    }

    @Override
    protected void when_inactive() {
    }

    @Override
    protected void when_power_update() {

        update = true;

    }

    @Override
    public void finalize_device() throws Throwable {

        monitor.stop();
        super.finalize_device();
    }
}

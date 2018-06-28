package electric_elements.power_provider;

import electric_elements.basic_elements.Device;

public abstract class Generator extends Device {

    private final double max_power_capacity;
    private double current_capacity;
    private long time;
    private double current_output = 0;
    private boolean dont_draw = false;
    private long timer_time=0;
    private final Runnable turn_off_runnable= () -> {
        try {
            Thread.sleep(timer_time);
            turn_off();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    };
    private Thread turn_off_timer=new Thread(turn_off_runnable);

    public Generator(double max_power_output, double max_power_capacity) {
        super(0, max_power_output);
        this.max_power_capacity = max_power_capacity;
        this.current_capacity = max_power_capacity;
        time = System.currentTimeMillis();
    }

    public Generator(double max_power_output, boolean no_capacity_draw) {
        super(0, max_power_output);
        this.max_power_capacity = 0;
        this.current_capacity = max_power_capacity;
        time = System.currentTimeMillis();
        dont_draw = true;

    }


    protected abstract void when_active();


    protected abstract void when_inactive();

    @Override
    public void when_power_update_layer_1() {
        if (!dont_draw) {
            if(turn_off_timer==null){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
           turn_off_timer.stop();
           turn_off_timer=null;
           turn_off_timer=new Thread(turn_off_runnable);
            long stop_time = System.currentTimeMillis();
            long diff_time = stop_time - time;
            time = stop_time;
            double timepass = ((diff_time / 1000.0) / 60.0) / 60.0;
            double todraw = current_output * timepass;
            current_capacity = current_capacity - todraw;
            current_output = get_current_drain();
            if (current_capacity < 0) {
                turn_off();
            } else {
                timer_time = (long) ((current_capacity / current_output) * 60 * 60);
                if (timer_time < 999999999) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    turn_off_timer.run();
                }

            }

            when_power_update_layer_2();

        }


    }

    protected abstract void when_power_update_layer_2();

    public boolean add_capacity(double to_add) {
        if ((to_add > 0) && (!dont_draw)) {
            if (current_capacity + to_add <= max_power_capacity) {
                current_capacity = current_capacity + to_add;
            } else {
                current_capacity = max_power_capacity;
            }

            update_drain();
            return true;
        }
        return false;
    }

    public boolean remove_capacity(double to_remove) {

        if ((to_remove > 0) && (!dont_draw)) {
            if (current_capacity - to_remove >= 0) {
                current_capacity = current_capacity - to_remove;
            } else {
                current_capacity = 0;
            }

            update_drain();
            return true;
        }
        return false;
    }

    public void set_percent_output(float percent) {
        set_percent_extra(percent);

    }

    public void set_watt_output(double watt) {
        watt=-watt;
        set_to_draw(watt);
    }

    @Override
    protected void final_shutdown_layer2() {
       turn_off_timer.stop();
       turn_off_timer=null;
    }

    protected abstract void final_shutdown_layer3();
}

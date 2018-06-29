package net_interaction.basic_elements.Device;

import electric_elements.basic_elements.Device;
import electric_elements.basic_elements.Power_Grid;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@SpringBootApplication

public abstract class Device_Controller extends Device {

    protected Device_Controller(double standart_drain, double extra_drain) {
        super(standart_drain, extra_drain);
        Properties properties = new Properties();
        properties.setProperty("spring.application.name", getName());
        properties.setProperty("server.port", "0");
        new SpringApplicationBuilder().sources(Device_Controller.class)
                .properties(properties)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run();
    }

    public Device_Controller(String Name, double standart_drain, double extra_drain) {
        super(Name, standart_drain, extra_drain);
        Properties properties = new Properties();
        properties.setProperty("spring.application.name", getName());
        properties.setProperty("server.port", "0");
        new SpringApplicationBuilder().sources(Device_Controller.class)
                .properties(properties)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run();
    }

    @Override
    public void turn_on() {
        super.turn_on();
    }

    @Override
    public void turn_off() {
        super.turn_off();
    }

    @Override
    public double get_current_drain() {
        return super.get_current_drain();
    }

    @Override
    public void attach(Power_Grid net) {
        super.attach(net);
    }

    @Override
    public void retach(Power_Grid net) {
        super.retach(net);
    }

    @Override
    public void detach() {
        super.detach();
    }

    @Override
    public boolean is_attached() {
        return super.is_attached();
    }

    @Override
    public void check_power() {
        super.check_power();
    }

    @Override
    public boolean isOn_off() {
        return super.isOn_off();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public double get_max_power_draw() {
        return super.get_max_power_draw();
    }

    @Override
    public boolean set_to_draw(double watt) {
        return super.set_to_draw(watt);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void finalize_device() throws Throwable {
        super.finalize_device();
    }

    @Override
    public boolean isActive() {
        return super.isActive();
    }
}

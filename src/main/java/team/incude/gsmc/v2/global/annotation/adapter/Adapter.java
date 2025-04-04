package team.incude.gsmc.v2.global.annotation.adapter;

import org.springframework.stereotype.Component;
import team.incude.gsmc.v2.global.annotation.PortDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface Adapter {
    PortDirection direction();
}
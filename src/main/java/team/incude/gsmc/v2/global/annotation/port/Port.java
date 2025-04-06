package team.incude.gsmc.v2.global.annotation.port;

import team.incude.gsmc.v2.global.annotation.PortDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface Port {
    PortDirection direction();
}
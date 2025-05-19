package team.incude.gsmc.v2.global.security.jwt.persistence;

import team.incude.gsmc.v2.global.annotation.PortDirection;
import team.incude.gsmc.v2.global.annotation.adapter.Adapter;
import team.incude.gsmc.v2.global.security.jwt.application.port.JwtApplicationPort;

@Adapter(direction = PortDirection.INBOUND)
public class JwtApplicationAdapter implements JwtApplicationPort {
    
}

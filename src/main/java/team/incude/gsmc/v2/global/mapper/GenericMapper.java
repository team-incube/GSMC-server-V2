package team.incude.gsmc.v2.global.mapper;

public interface GenericMapper<ENTITY, DOMAIN> {
    ENTITY toEntity(DOMAIN domain);

    DOMAIN toDomain(ENTITY entity);
}
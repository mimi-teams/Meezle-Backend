package com.mimi.w2m.backend.domain.generator;

import com.fasterxml.uuid.Generators;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * 시간 순서대로 생성되는 UUID
 *
 * @since 2022-12-25
 * @author yeh35
 */
public class SequentialUUIDGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        final var uuid = Generators.timeBasedGenerator().generate();
        final var uuidArr = uuid.toString().split("-");

        final var sb = new StringBuilder(uuidArr.length + 4);
        sb.append(uuidArr[2]);
        sb.append(uuidArr[1]);
        sb.append(uuidArr[0]);
        sb.append(uuidArr[3]);
        sb.append(uuidArr[4]);
        sb.insert(8, "-");
        sb.insert(13, "-");
        sb.insert(18, "-");
        sb.insert(23, "-");

        return UUID.fromString(sb.toString());
    }

}

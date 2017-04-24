package org.crygier.graphql;

import graphql.language.*;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.lang.reflect.Method;

public class MutationJPADataFetcher extends JpaDataFetcher {

    private static final Logger logger = LoggerFactory.getLogger(MutationJPADataFetcher.class);

    public MutationJPADataFetcher(EntityManager entityManager, EntityType<?> entityType) {
        super(entityManager, entityType);
    }

    @Override
    public Object get(DataFetchingEnvironment environment) {
        return mutate(environment, environment.getFields().iterator().next());
    }

    protected Object mutate(DataFetchingEnvironment environment, Field field) {
        try {
            Class<?> entityClassType = entityType.getJavaType();

            Object entity = entityClassType.newInstance();

            field.getArguments().forEach(arg -> {
                try {
                    java.lang.reflect.Field f = entity.getClass().getDeclaredField(arg.getName());
                    Method method = entity.getClass().getDeclaredMethod("set" + capatalizeFieldName(arg.getName()), f.getType());
                    method.invoke(entity, convertToType(environment, arg, arg.getValue(), f.getType()));
                } catch (Throwable e) {
                    logger.error("Failed to mutate object", e);
                }
            });

            return entityManager.merge(entity);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object convertToType(DataFetchingEnvironment environment, Argument argument, Value value, Class<?> required) {
        if (value instanceof IntValue) {
            if (required.isAssignableFrom(Integer.class)) {
                return (((IntValue) value).getValue()).intValue();
            } else if (required.isAssignableFrom(Double.class)) {
                return (((IntValue) value).getValue()).doubleValue();
            }
        } else if (value instanceof FloatValue) {
            if (required.isAssignableFrom(Double.class)) {
                return (((FloatValue) value).getValue()).doubleValue();
            }
        }


        Object result = convertValue(environment, argument, value);
        if (required.isAssignableFrom(result.getClass())) {
            return result;
        }

        throw new IllegalArgumentException("No conversion strategy is present for GraphQL type: " + value.getClass() + " and required type: " + required.getName());
    }
    /**
     * Capitalizes the field name unless one of the first two characters are uppercase. This is in accordance with java
     * bean naming conventions in JavaBeans API spec section 8.8.
     *
     * @param fieldName
     * @return the capitalised field name
     */
    public static String capatalizeFieldName(String fieldName) {
        final String result;
        if (fieldName != null && !fieldName.isEmpty()
                && Character.isLowerCase(fieldName.charAt(0))
                && (fieldName.length() == 1 || Character.isLowerCase(fieldName.charAt(1)))) {
            result = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        } else {
            result = fieldName;
        }
        return result;
    }
}

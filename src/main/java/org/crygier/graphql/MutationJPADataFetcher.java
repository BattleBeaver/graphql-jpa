package org.crygier.graphql;

import graphql.language.Argument;
import graphql.language.Field;
import graphql.schema.DataFetchingEnvironment;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.stream.Collectors;

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
            Class<?> entityClassType = (Class) entityType.getJavaType();

            Session session = entityManager.unwrap(Session.class);
            Object entity = entityClassType.newInstance();

            String schema = field.getArguments().stream().map(Argument::getName).collect(Collectors.joining(", ", "(", ")"));
            String values = field.getArguments().stream().map(arg -> (convertValue(environment, arg, arg.getValue())).toString()).collect(Collectors.joining(", ", "(", ")"));
            String hql = "INSERT INTO " + entityType.getName() + schema + " VALUES " + values;

            session.createQuery(hql).executeUpdate();
            return entity;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
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

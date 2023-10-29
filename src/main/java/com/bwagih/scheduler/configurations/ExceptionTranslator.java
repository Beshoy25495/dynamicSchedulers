package com.bwagih.scheduler.configurations;

import com.bwagih.scheduler.utils.Defines;
import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultExecuteListener;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

@Profile(Defines.SPRING_PROFILES_JOOQ)
public class ExceptionTranslator extends DefaultExecuteListener {

    @Override
    public void exception(ExecuteContext context) {
        SQLDialect dialect = SQLDialect.ORACLE12C;
        SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dialect.getName());
        context.exception(translator.translate("Access database using jOOQ", context.sql(),
                context.sqlException()));
    }

}

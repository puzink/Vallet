package org.wallet.liquibase;

import liquibase.command.CommandScope;
import liquibase.command.core.helpers.DatabaseChangelogCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep;
import liquibase.exception.CommandExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommandFactory {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${schema.liquibase.change-log}")
    private String changeLogPath;

    public CommandScope build(String commandName) throws CommandExecutionException {
        CommandScope command = new CommandScope(commandName);

        command.addArgumentValue(DbUrlConnectionArgumentsCommandStep.URL_ARG, dbUrl)
                .addArgumentValue(DbUrlConnectionArgumentsCommandStep.USERNAME_ARG, username)
                .addArgumentValue(DbUrlConnectionArgumentsCommandStep.PASSWORD_ARG, password)
                .addArgumentValue(DbUrlConnectionArgumentsCommandStep.DRIVER_ARG, driverClassName)
                .addArgumentValue(DatabaseChangelogCommandStep.CHANGELOG_FILE_ARG, changeLogPath);

        return command;
    }


}

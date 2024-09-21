package org.wallet.liquibase;

import liquibase.command.CommandResults;
import liquibase.command.core.DropAllCommandStep;
import liquibase.command.core.RollbackCountCommandStep;
import liquibase.command.core.UpdateCountCommandStep;
import liquibase.exception.CommandExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LiquibaseService {

    private final CommandFactory commandBuilder;

    public CommandResults updateCount(int count) throws CommandExecutionException {
        val updateCount = commandBuilder.build(UpdateCountCommandStep.COMMAND_NAME[0]);
        updateCount.addArgumentValue(UpdateCountCommandStep.COUNT_ARG, count);
        return updateCount.execute();
    }

    public CommandResults rollbackCount(int count) throws CommandExecutionException {
        val rollbackCount = commandBuilder.build(RollbackCountCommandStep.COMMAND_NAME[0]);
        rollbackCount.addArgumentValue(UpdateCountCommandStep.COUNT_ARG, count);
        return rollbackCount.execute();
    }

    public CommandResults dropAll() throws CommandExecutionException {
        val dropAll = commandBuilder.build(DropAllCommandStep.COMMAND_NAME[0]);
        dropAll.addArgumentValue(DropAllCommandStep.FORCE_ARG, true)
                .addArgumentValue(DropAllCommandStep.REQUIRE_FORCE_ARG, true);
        return dropAll.execute();
    }

}

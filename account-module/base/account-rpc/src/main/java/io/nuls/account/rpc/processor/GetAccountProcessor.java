package io.nuls.account.rpc.processor;

import io.nuls.account.model.Address;
import io.nuls.core.tools.cmd.CommandBuilder;
import io.nuls.core.tools.cmd.CommandHelper;
import io.nuls.kernel.lite.annotation.Cmd;
import io.nuls.kernel.lite.annotation.Component;
import io.nuls.kernel.model.Result;
import io.nuls.kernel.model.RpcClientResult;
import io.nuls.kernel.processor.CommandProcessor;

/**
 * @author: Charlie
 * @date: 2018/5/25
 */
@Cmd
@Component
public class GetAccountProcessor implements CommandProcessor {
    @Override
    public String getCommand() {
        return "getaccount";
    }

    @Override
    public String getHelp() {
        CommandBuilder builder = new CommandBuilder();
        builder.newLine(getCommandDescription())
                .newLine("\t<address> the account address - Required");
        return builder.toString();
    }

    @Override
    public String getCommandDescription() {
        return "getaccount <address> --get account information";
    }

    @Override
    public boolean argsValidate(String[] args) {
        if (args.length != 2) {
            return false;
        }
        if (!CommandHelper.checkArgsIsNull(args)) {
            return false;
        }
        if (!Address.validAddress(args[1])) {
            return false;
        }
        return true;
    }

    @Override
    public RpcClientResult execute(String[] args) {
        return null;
    }
}

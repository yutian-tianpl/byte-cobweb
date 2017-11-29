package com.tianpl.opcode.example.javaagent;

import com.tianpl.opcode.OpcdeException;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * AgentArgs
 *
 * @Author yu.tian@tianpl.com
 * @Date 17/11/15 18:22
 */
public class AgentArgs {
    private static final String ARG_SPLIT_BY = ";";
    private static final String ARGS_RULES;

    public enum NameMatcherRule {
        NAME_ALL, NAME_START_WITH, NAME_END_WITH, NAME_CONTAINS,NAMED;
        public static Optional<NameMatcherRule> of(String name) {
            if (name == null || name.length() == 0) return Optional.empty();
            return Arrays.stream(values()).filter(value -> value.name().equals(name)).findFirst();
        }
    }

    static {
        ARGS_RULES = "需要使用JVM参数javaagent挂载代理。\n形如:-javaagent:install.jar=agentArgs\n" +
                "agentArgs必须明确指定代理参数，参数形式如下：\n" +
                "1.使用分隔符进行参数分割，分隔符为半角字符分号[;]\n" +
                "2.合法的参数数量为两个或者三个\n" +
                "3.参数说明:\n" +
                "3.2.参数一:必选，目标Class匹配规则，可选值：" + Arrays.stream(NameMatcherRule.values()).map(Enum::name).collect(Collectors.joining(",")) + "\n" +
                "3.3.参数二:特殊必选，参数二为" + NameMatcherRule.NAME_ALL.name() + "时无此参数，此参数的值跟参数二关系密切";
    }
    private NameMatcherRule nameMatcherRule;
    private String matchRule;

    public NameMatcherRule getNameMatcherRule() {
        return nameMatcherRule;
    }

    public void setNameMatcherRule(NameMatcherRule nameMatcherRule) {
        this.nameMatcherRule = nameMatcherRule;
    }

    public String getMatchRule() {
        return matchRule;
    }

    private void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }

    private AgentArgs(NameMatcherRule nameMatcherRule) {
        this.nameMatcherRule = nameMatcherRule;
    }

    static AgentArgs of(String agentArgsStr) {
        if (agentArgsStr == null || agentArgsStr.length() == 0) {
            throw new OpcdeException("JavaAgent必须设置参数.");
        }
        String[] args = agentArgsStr.split(ARG_SPLIT_BY);
        if (args.length != 1 && args.length != 2) {
            throw new OpcdeException("JavaAgent参数数量不正确.");
        }
        Optional<NameMatcherRule> optionalMatchRuleAs = NameMatcherRule.of(args[0]);
        if (!optionalMatchRuleAs.isPresent()) {
            throw new OpcdeException("JavaAgent必须设置正确的代理目标类匹配方式方式.");
        }
        AgentArgs agentArgs = new AgentArgs(optionalMatchRuleAs.get());
        switch (optionalMatchRuleAs.get()) {
            case NAME_ALL:
                break;
            case NAME_START_WITH:
            case NAME_END_WITH:
            case NAMED:
                if (args.length != 2) {
                    System.out.println(ARGS_RULES);
                    throw new OpcdeException("JavaAgent必须设置正确的代理目标类匹配方式方式.");
                }
                agentArgs.setMatchRule(args[1]);
                break;
            default:
                throw new OpcdeException("未支持目标类型匹配规则.");
        }
        return agentArgs;
    }

    public static boolean valida(AgentArgs agentArgs) {
        if (agentArgs == null) return false;
        if (agentArgs.getNameMatcherRule() == null) return false;
        if (agentArgs.getNameMatcherRule() != NameMatcherRule.NAME_ALL && (agentArgs.getMatchRule() == null || agentArgs.getMatchRule().trim().length() == 0)) return false;
        return true;
    }
}

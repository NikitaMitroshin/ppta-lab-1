import model.Grammar;
import model.GrammarType;
import model.Rule;
import org.apache.commons.io.FileUtils;
import utils.RuleAnalyzer;
import utils.RuleParser;
import utils.StringUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final String FILE_EXTENSION = ".txt";
    private static final String EXIT = "exit";

    public static void main(String[] args) {
        try (final Scanner scanner = new Scanner(System.in)) {
            readFromConsole(scanner);
        }
    }

    private static void readFromConsole(Scanner scanner) {
            System.out.println("Enter file name (without extension) or 'exit' to exit: ");
            final String input = scanner.nextLine();
            if (input == null || input.isEmpty()) {
                System.out.println("invalid file name");
                readFromConsole(scanner);
            }
            if (EXIT.equals(input)) {
                return;
            }
            try {
                final Grammar grammar = fromFile(new File(input + FILE_EXTENSION));
                System.out.println(getType(grammar).getType());
            } catch (IllegalArgumentException e) {
                System.out.println("invalid file name");
                readFromConsole(scanner);
            }
            readFromConsole(scanner);

    }

    private static Grammar fromFile(File file) throws IllegalArgumentException {
        try {
            final List<String> rulesStrings = FileUtils.readLines(file, Charset.defaultCharset());
            final List<Rule> rules = RuleParser.parseAll(rulesStrings);
            final Set<Character> terminals = RuleAnalyzer.getTerminals(rules);
            final Set<Character> nonTerminals = RuleAnalyzer.getNonterminals(rules);
            return new Grammar(rules, terminals, nonTerminals);
        } catch (java.io.IOException e) {
            throw new IllegalArgumentException("Wrong file name");
        }
    }

    private static GrammarType getType(Grammar grammar) {
        GrammarType result = GrammarType.TYPE_0;
        if (isType1(grammar)) {
            result = GrammarType.TYPE_1;
        }
        if (isType2(grammar)) {
            result = GrammarType.TYPE_2;
        }
        if (isType3(grammar)) {
            result = GrammarType.TYPE_3;
        }
        return result;
    }

    // проверяем что нет ни одной строки где слева было бы больше символов чем справа
    private static boolean isType1(Grammar grammar) {
        return grammar.getRules()
                .stream()
                .noneMatch(rule -> rule.getLeft()
                        .length() > rule.getRight()
                        .length());
    }

    // у КС левая часть должна состоять только из одного нетерминала
    private static boolean isType2(Grammar grammar) {
        return grammar.getRules()
                .stream()
                .allMatch(rule -> {
                    final List<Character> leftChars = StringUtil.splitToChars(rule.getLeft());
                    return leftChars.size() == 1 && !grammar.getT().contains(leftChars.get(0));
                });
    }


    private static boolean isType3(Grammar grammar) {
        for (Rule rule : grammar.getRules()) {
            final List<Character> rightChars = StringUtil.splitToChars(rule.getRight());
            final List<Integer> nonTerminalsPositions = new ArrayList<>();
            for (int i = 0; i < rightChars.size(); i++) {
                if (grammar.getN().contains(rightChars.get(i))) {
                    nonTerminalsPositions.add(i);
                }
            }
            // если справа больше одного нетерминала то не рг -> AA
            if (nonTerminalsPositions.size() > 1) return false;
            // если справа только терминальный символ -> A
            if (nonTerminalsPositions.size() == 1 && rightChars.size() == 1) return false;
            // если справа aAa (регулярная порождается при добавлении цепочки справа ИЛИ слева)
            if (nonTerminalsPositions.size() != 0 && nonTerminalsPositions.get(0) != 0 && nonTerminalsPositions.get(0) != rightChars.size() - 1)
                return false;
        }
        return true;
    }

}

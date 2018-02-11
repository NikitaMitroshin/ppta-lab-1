package utils;

import model.Rule;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RuleAnalyzer {

	public static Set<Character> getTerminals(List<Rule> rules) {
		return rules.stream()
				.map(RuleAnalyzer::getTerminals)
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());
	}

	public static Set<Character> getNonterminals(List<Rule> rules) {
		return rules.stream()
				.map(RuleAnalyzer::getNonterminals)
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());
	}

	private static Set<Character> getTerminals(Rule rule) {
		return getCharsFromRule(rule).stream()
				.filter(Character::isLowerCase)
				.collect(Collectors.toSet());
	}

	private static Set<Character> getNonterminals(Rule rule) {
		return getCharsFromRule(rule).stream()
				.filter(Character::isUpperCase)
				.collect(Collectors.toSet());
	}

	private static Set<Character> getCharsFromRule(Rule rule) {
		return Stream.concat(StringUtil.splitToChars(rule.getLeft())
				.stream(), StringUtil.splitToChars(rule.getRight())
				.stream())
				.collect(Collectors.toSet());
	}
}

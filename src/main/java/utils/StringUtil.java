package utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	public static List<Character> splitToChars(final String string) {
		final List<Character> charsList = new ArrayList<>();
		final char[] chars = string.toCharArray();
		for (char c : chars) {
			charsList.add(c);
		}
		return charsList;
	}
}

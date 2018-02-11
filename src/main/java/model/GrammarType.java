package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GrammarType {

	TYPE_0(0, "Тип 0"),
	TYPE_1(1, "Тип 1 Контекстно-зависимая"),
	TYPE_2(2, "Тип 2 Контекстно-свободная"),
	TYPE_3(3, "Тип 3 Регулярная");

	private final int typeNumber;
	private final String type;

}

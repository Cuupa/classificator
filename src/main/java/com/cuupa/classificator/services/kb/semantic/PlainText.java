package com.cuupa.classificator.services.kb.semantic;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class PlainText {

	private final String plainText;

	private int distance;

	public PlainText(String plainText) {
		this.plainText = plainText;
	}

	public boolean contains(String text, int tolerance) {
		try {
			text = normalizeText(text);
			String tempPlaintext = normalizeText(plainText);

			if (text.length() == 0 || tempPlaintext.length() == 0) {
				return false;
			}

			String[] wordsToSearch = text.split(" ");

			return search(tempPlaintext, wordsToSearch, tolerance);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean contains(String text) {
		return contains(text, 1);
	}

	private boolean search(String tempPlaintext, String[] wordsToSearch, int tolerance) {
		String[] splitPlain = tempPlaintext.split(" ");

		int positionOfFirstMatch = -1;
		for (int i = 0; i < splitPlain.length; i++) {
			if (findFirst(splitPlain[i], wordsToSearch, tolerance)) {
				positionOfFirstMatch = i;
				break;
			}
		}

		if (positionOfFirstMatch == -1) {
			return false;
		}

		int currentPositionPlainText = positionOfFirstMatch;
		int currentPositionSearchString = 0;

		int matchingWords = 0;

		boolean search = true;
		while (search) {
			Pair<String, Integer> pairPlain = findNonEmptyEntry(splitPlain, currentPositionPlainText);
			Pair<String, Integer> pairSearch = findNonEmptyEntry(wordsToSearch, currentPositionSearchString);
			String currentWordFromPlain = pairPlain.getLeft();
			String currentWordToSearch = pairSearch.getLeft();

			currentPositionPlainText = pairPlain.getRight();
			currentPositionSearchString = pairSearch.getRight();

			if (currentWordFromPlain.length() > (currentWordToSearch.length() + 5 + tolerance)) {

				Integer distance = LevenshteinDistance.getDefaultInstance().apply(currentWordFromPlain,
						currentWordToSearch);

				if (distance <= tolerance) {
					matchingWords++;
					currentPositionPlainText++;
					currentPositionSearchString++;
					this.distance += distance;
				} else {
					matchingWords = 0;
					currentPositionSearchString = 0;
					Pair<String, Integer> pair = findNonEmptyEntry(wordsToSearch, currentPositionSearchString);
					currentWordToSearch = pair.getLeft();
					currentPositionSearchString = pair.getRight();
					distance = LevenshteinDistance.getDefaultInstance().apply(currentWordFromPlain,
							currentWordToSearch);

					if (distance <= tolerance) {
						matchingWords++;
						currentPositionSearchString++;
					}
					currentPositionPlainText++;
				}
			} else {
				currentPositionPlainText++;
			}

			if (currentPositionPlainText + 1 > splitPlain.length
					|| currentPositionSearchString + 1 > wordsToSearch.length) {
				search = false;
			}
		}

		return matchingWords == wordsToSearch.length;
	}

	private Pair<String, Integer> findNonEmptyEntry(String[] strings, int index) {
		Pair<String, Integer> value = Pair.of("", index);
		while (strings.length >= index) {
			String temp = strings[index].trim();
			if (temp.length() > 0) {
				return Pair.of(temp, index);
			} else {
				index++;
			}
		}
		return value;
	}

	private boolean findFirst(String string, String[] wordsToSearch, int tolerance) {
		Integer distance = LevenshteinDistance.getDefaultInstance().apply(string, wordsToSearch[0]);
		if (distance <= tolerance) {
			return true;
		}
		return false;
	}

	private String normalizeText(String text) {
		text = text.replace("\n\r", " ");
		text = text.replace("\r\n", " ");
		text = text.replace("\r", " ");
		text = text.replace("\n", " ");
		text = text.replace("\t", " ");
		text = text.replace("-", " ");
		text = text.replace(",", " ");
		text = text.replace(": ", " ");
		text = text.replace("€", " €");
		return text.trim().toLowerCase();
	}

	public int getDistance() {
		return distance;
	}
}

package com.cuupa.classificator.services.kb.semantic;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlainText {

	private final String plainText;

	private int distance;

	public PlainText(String plainText) {
		this.plainText = plainText;
	}

	public boolean contains(@Nullable String text, int tolerance) {
		try {
			if (text == null || plainText == null) {
				return false;
			}
			text = normalizeText(text);
			String tempPlaintext = normalizeText(plainText);

			if (text.length() == 0 || tempPlaintext.length() == 0) {
				return false;
			}

			String[] wordsToSearch = text.split(" ");

            return search(tempPlaintext, wordsToSearch, tolerance) && distance <= tolerance;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean contains(String text) {
		return contains(text, 1);
	}

	private boolean search(@NotNull String tempPlaintext, @NotNull String[] wordsToSearch, int tolerance) {
		String[] splitPlain = tempPlaintext.split(" ");

		int currentPositionPlainText = 0;
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
				this.distance = 0;
				Pair<String, Integer> pair = findNonEmptyEntry(wordsToSearch, currentPositionSearchString);
				currentWordToSearch = pair.getLeft();
				currentPositionSearchString = pair.getRight();
				distance = LevenshteinDistance.getDefaultInstance().apply(currentWordFromPlain, currentWordToSearch);

				if (distance <= tolerance) {
					matchingWords++;
					currentPositionSearchString++;
				}
				currentPositionPlainText++;
			}

			if (currentPositionPlainText + 1 > splitPlain.length
					|| currentPositionSearchString + 1 > wordsToSearch.length) {
				search = false;
			}
		}
		return matchingWords == wordsToSearch.length;
	}

	@NotNull
	private Pair<String, Integer> findNonEmptyEntry(@NotNull String[] strings, int index) {
		while (strings.length >= index) {
			String temp = strings[index].trim();
			if (temp.length() > 0) {
				return Pair.of(temp, index);
			} else {
				index++;
			}
		}
		return Pair.of(Strings.EMPTY, index);
	}

	@NotNull
	private String normalizeText(String text) {
		text = text.toLowerCase();
		text = text.replace("\t", " ");
		text = text.replace("\n\r", " ");
		text = text.replace("\r\n", " ");
		text = text.replace("\r", " ");
		text = text.replace("\n", " ");
		text = text.replace("\t", " ");
//		text = text.replace("-", " ");
		text = text.replace(",", " ");
		text = text.replace(": ", " ");
		text = text.replace("€", " €");
		text = text.replace("Ãœ", "ae");
		text = text.replace("ä", "ae");
		text = text.replace("ã¼", "ue");
		text = text.replace("ü", "ue");
		text = text.replace("/", " ");
		text = text.replace("_", " ");
        text = text.replaceAll(" {2}", " ");
		return text.trim();
	}

	public int getDistance() {
		return distance;
	}
}

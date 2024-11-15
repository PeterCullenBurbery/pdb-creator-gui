/**
 * @since 2024-W42-7 13.46.52.270 -0400
 * @author peter
 */
package com.peter_burbery.pdb_creator;

/**
 * 
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Name {
	/* Private field for the spaceDelimited string (editable) */
	private String spaceDelimited;

	/* Private fields for computed properties (not editable, but recalculated) */
	private String underscoreUppercase;
	private String dashLowercase;
	private List<String> words; // Store the words extracted from the input

	/**
	 * Constructor that initializes the spaceDelimited and computes the fields.
	 * 
	 * @param spaceDelimited the spaceDelimited string
	 */
	public Name(String spaceDelimited) {
		setSpaceDelimited(spaceDelimited); // This sets spaceDelimited and recalculates computed fields
	}

	/**
	 * Overloaded constructor that can handle dash-separated input. If the input is
	 * dash-separated, it converts it to a space-delimited string.
	 * 
	 * @param input           the input string (either space or dash separated)
	 * @param isDashSeparated true if the input is dash-separated
	 */
	public Name(String input, boolean isDashSeparated) {
		this.spaceDelimited = isDashSeparated ? input.replace("-", " ") : input;
		this.words = Arrays.asList(this.spaceDelimited.split(" "));
		this.underscoreUppercase = computeUnderscoreUppercase(this.words);
		this.dashLowercase = computeDashLowercase(this.words);
	}

	/**
	 * Sets the spaceDelimited field and updates the computed fields.
	 * 
	 * @param spaceDelimited the new spaceDelimited string
	 */
	public void setSpaceDelimited(String spaceDelimited) {
		this.spaceDelimited = spaceDelimited;
		this.words = Arrays.asList(spaceDelimited.split(" "));
		this.underscoreUppercase = computeUnderscoreUppercase(this.words);
		this.dashLowercase = computeDashLowercase(this.words);
	}

	/**
	 * Gets the spaceDelimited field.
	 * 
	 * @return the spaceDelimited string
	 */
	public String getOriginal() {
		return spaceDelimited;
	}

	/**
	 * Gets the underscoreUppercase computed field.
	 * 
	 * @return the string with underscores and uppercase
	 */
	public String getUnderscoreUppercase() {
		return underscoreUppercase;
	}

	/**
	 * Gets the dashLowercase computed field.
	 * 
	 * @return the string with dashes and lowercase
	 */
	public String getDashLowercase() {
		return dashLowercase;
	}

	/**
	 * Computes the string with underscores and converts it to uppercase.
	 * 
	 * @param words the list of words
	 * @return the string with underscores and uppercase
	 */
	private String computeUnderscoreUppercase(List<String> words) {
		return String.join("_", words).toUpperCase();
	}

	/**
	 * Computes the string with dashes and converts it to lowercase.
	 * 
	 * @param words the list of words
	 * @return the string with dashes and lowercase
	 */
	private String computeDashLowercase(List<String> words) {
		return String.join("-", words).toLowerCase();
	}

	/**
	 * Returns the list of words extracted from the space-delimited or
	 * dash-delimited input.
	 * 
	 * The words are extracted during initialization based on the input format. If
	 * the input was space-separated, the list contains each word split by spaces.
	 * If the input was dash-separated, it was converted to space-delimited first,
	 * and the list contains words split by spaces.
	 * 
	 * @return a list of words as {@code List<String>}.
	 */
	public List<String> getWords() {
		return new ArrayList<>(words); // Return a copy of the list to prevent external modification
	}

	/**
	 * Returns the original input string in space-delimited format.
	 * 
	 * 
	 * @return the space-delimited string.
	 */
	public String getSpaceDelimited() {
		return spaceDelimited;
	}

}

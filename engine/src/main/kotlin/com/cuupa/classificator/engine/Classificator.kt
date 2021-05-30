package com.cuupa.classificator.engine

import com.cuupa.classificator.domain.SemanticResult

/**
 * @author Simon Thiel (https://github.com/cuupa) (29.5.2021)
 */
interface Classificator {

    /**
     * This method accepts a simple String and calls the underlying methods to classify the input
     * This method assumes that the content-type is "text/plain"
     * @param content: The content to classify
     * @return A list of {@code SemanticResult}
     */
    fun classify(content: String?): List<SemanticResult>

    /**
     * Method classifies text and uses the content-type to determine
     */
    fun classify(contentType: String?, content: String?): Pair<String, List<SemanticResult>>

}

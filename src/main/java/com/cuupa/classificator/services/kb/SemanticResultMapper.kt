package com.cuupa.classificator.services.kb

import com.cuupa.classificator.services.kb.semantic.Topic

class SemanticResultMapper {

     fun toSemanticResult(text: String): (Topic) -> SemanticResult = {
        SemanticResult(it.name, it.getMetaData(text))
    }
}
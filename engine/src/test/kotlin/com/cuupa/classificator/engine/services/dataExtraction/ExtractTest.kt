package com.cuupa.classificator.engine.services.dataExtraction

import com.cuupa.classificator.engine.services.kb.KnowledgeFileExtractor
import java.io.File

open class ExtractTest {
    val knowledgeFile = KnowledgeFileExtractor.extractKnowledgebase(File("../build/knowledgebase/kb-DEV.db"))
}

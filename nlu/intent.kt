package furhatos.app.corhat.nlu

import furhatos.nlu.Intent
import furhatos.util.Language


class GiveSymptomsIntent : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "I am feeling unwell",
                "I am feeling sick",
                "I have symptoms"
        )
    }
}


class GetGeneralInfoIntent : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "I want general information about COVID-19",
                "I want general information about Corona",
                "How can I avoid COVID-19?",
                "What is COVID-19?",
                "How can I be safe from COVID-19?"
        )
    }
}

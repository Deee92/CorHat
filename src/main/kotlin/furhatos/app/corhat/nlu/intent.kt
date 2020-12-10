package furhatos.app.corhat.nlu

import furhatos.nlu.Intent
import furhatos.nlu.TextGenerator
import furhatos.records.GenericRecord
import furhatos.util.Language

class DescribeContactHistory(var covid: Covid? = null, var person: Person? = null) : Intent(), TextGenerator {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I've had contact with @person who has @covid",
                "I've been in touch with @person who tested positive for @covid",
                "my @person has @covid",
                "my @person tested positive",
                "my @person tested positive for @covid",
                "my @person has tested positive for @covid",
                "I was in contact with my @person who had @covid",
                "I was in contact with my @person who tested positive for @covid"
        )
    }

    override fun toText(lang: Language): String {
        return generate(lang, "[$person]")
    }

    override fun toString(): String {
        return toText()
    }

}

open class DescribeHealthIntent : Intent(), TextGenerator {
    var symptoms: ListOfSymptom? = null
    var duration: Duration? = null

    override fun getExamples(lang: Language): List<String> {
        return listOf("I feel @symptoms for @duration",
                "I'm experiencing @symptoms since @duration",
                "I have @symptoms for @duration",
                "I feel @symptoms",
                "I have @symptoms",
                "I'm having headache and feeling unwell",
                "I've been feeling sick for 2 days",
                "I've had fever for @duration"
        )
    }

    override fun toText(lang: Language): String {
        return generate(lang, "[$symptoms][for $duration]")
    }

    override fun toString(): String {
        return toText()
    }

    override fun adjoin(record: GenericRecord<Any>?) {
        super.adjoin(record)
        if (symptoms != null) {
            symptoms?.list = symptoms?.list?.distinctBy { it.value }!!.toMutableList()
        }
    }
}

class DescribeSymptomIntent : Intent() {
    var symptoms: ListOfSymptom? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("I feel @symptoms",
                "I'm having @symptoms",
                "@symptoms",
                "since yesterday",
                "since last week"
        )
    }
}

class DescribeDurationIntent : Intent() {
    var duration: Duration? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("It has been @duration",
                "I've felt sick for @duration",
                "@duration", "about 2 weeks"
        )
    }
}

class RequestServiceOptions : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("what services do you have?",
                "what can I do here?",
                "what can you do for me?",
                "can you help me with",
                "how can you help"
        )
    }
}

/* General information */

class AskCovid19 : Intent() {
    var covid: Covid? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("what is @covid?",
                "can you tell me about @covid?"
        )
    }
}

class AskSymptoms : Intent() {
    var covid: Covid? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("what are the symptoms of @covid?",
                "what common symptoms do we have for @covid?",
                "what symptoms are common for @covid?"
        )
    }
}

class AskSafetyMeasure : Intent() {
    var covid: Covid? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("How can I protect myself from @covid?",
                "safety measures",
                "safety measures for @covid",
                "How can I be safe from @covid?",
                "How to protect myself from @covid?",
                "How do I protect myself from @covid?",
                "What can I do to avoid getting infected?"
        )
    }
}

class AskTestTypeIntent : Intent() {
    var test: Test? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("What is a @test test",
                "Tell me about the @test test",
                "What is @test",
                "@test test",
                "@test"
        )
    }
    override fun getNegativeExamples(lang: Language): List<String> {
        return listOf("I want", "book")
    }

}

class BookTest : Intent() {
    var covid: Covid? = null
    var test: Test? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("I want to book a test",
                "I want a test",
                "I want to get tested for @covid"
        )
    }
    override fun getNegativeExamples(lang: Language): List<String> {
        return listOf("tell me", "what is")
    }
}

class GetInfo : Intent() {
    var covid: Covid? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("I'd like to learn more about @covid",
                "general information",
                "I need some general information",
                "I need some general information about @covid",
                "I want to have some general information about @covid",
                "What is @covid?",
                "Tell me everything"
        )
    }
}

class GovHelpline : Intent() {
    var covid: Covid? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("Government helpline",
                "helpline",
                "government helpline for @covid",
                "Where can I call for official guidelines",
                "I need the government helpline"
        )
    }
}
package furhatos.app.corhat.nlu

import furhatos.util.Language
import furhatos.nlu.*
import furhatos.nlu.common.Number

// City entity
class City : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Stockholm",
                "Linkoping: Linköping, linkoping",
                "Gothenburg: Gothenburg, Goteborg, Göteborg",
                "Uppsala: Uppsala, Uppsaala",
                "Lund: Lund, Loond, Malmö, Malmoe, Malmo")
    }
}

// Day entity
class Day : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    }
}

// Centers entity
class Centers : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        // Center 1-4 Gothenburg
        return listOf("Center 1", "Center 2", "Center 3", "Center 4",
                // Center 5-8 Stockholm
                "Center 5", "Center 6", "Center 7", "Center 8",
                // Center 9-12 Linkoping
                "Center 9", "Center 10", "Center 11", "Center 12",
                // Center 13-16 Uppsala
                "Center 13", "Center 14", "Center 15", "Center 16"
        )
    }
}

// Directions entity
class Center_Direction(val center: String? = null) : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        print(center)
        if (center == "Center 1") {
            return listOf("Address 1")
        }
        if (center == "Center 2") {
            return listOf("Address 2")
        }
        if (center == "Center 3") {
            return listOf("Address 3")
        }
        if (center == "Center 4") {
            return listOf("Address 4")
        }
        if (center == "Center 5") {
            return listOf("Address 5")
        }
        if (center == "Center 6") {
            return listOf("Address 6")
        }
        if (center == "Center 7") {
            return listOf("Address 7")
        }
        if (center == "Center 8") {
            return listOf("Address 8")
        }
        if (center == "Center 9") {
            return listOf("Address 9")
        }
        if (center == "Center 10") {
            return listOf("Address 10")
        }
        if (center == "Center 11") {
            return listOf("Address 11")
        }
        if (center == "Center 12") {
            return listOf("Address 12")
        }
        if (center == "Center 13") {
            return listOf("Address 13")
        }
        if (center == "Center 14") {
            return listOf("Adress 14")
        }
        if (center == "Center 15") {
            return listOf("Adress 15")
        } else {
            return listOf("Adress 16")
        }
    }

}

// Available Centers entity.
class Available_Centers(val day: String? = null, val city: String? = null) : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        print(day)
        if (city == "Gothenburg") {
            if (day == "Monday") {
                return listOf("Center 1", "Center 2")
            }
            if (day == "Tuesday") {
                return listOf("Center 3", "Center 4")
            }
            if (day == "Wednesday") {
                return listOf("Center 1", "Center 3")
            }
            if (day == "Thursday") {
                return listOf("Center 2", "Center 3")
            }
            if (day == "Friday") {
                return listOf("Center 1", "Center 4")
            }
            if (day == "Saturday") {
                return listOf("center 3", "Center 4")
            } else {
                return listOf("Center 2", "Center 4")
            }
        }
        if (city == "Stockholm") {
            if (day == "Monday") {
                return listOf("Center 5", "Center 6")
            }
            if (day == "Tuesday") {
                return listOf("Center 7", "Center 8")
            }
            if (day == "Wednesday") {
                return listOf("Center 5", "Center 7")
            }
            if (day == "Thursday") {
                return listOf("Center 6", "Center 7")
            }
            if (day == "Friday") {
                return listOf("Center 5", "Center 8")
            }
            if (day == "Saturday") {
                return listOf("center 7", "Center 8")
            } else {
                return listOf("Center 6", "Center 8")
            }
        }

        if (city == "Linkoping") {
            if (day == "Monday") {
                return listOf("Center 9", "Center 10")
            }
            if (day == "Tuesday") {
                return listOf("Center 11", "Center 12")
            }
            if (day == "Wednesday") {
                return listOf("Center 9", "Center 11")
            }
            if (day == "Thursday") {
                return listOf("Center 10", "Center 11")
            }
            if (day == "Friday") {
                return listOf("Center 9", "Center 12")
            }
            if (day == "Saturday") {
                return listOf("center 11", "Center 12")
            } else {
                return listOf("Center 10", "Center 12")
            }
        } else {
            if (day == "Monday") {
                return listOf("Center 13", "Center 14")
            }
            if (day == "Tuesday") {
                return listOf("Center 15", "Center 16")
            }
            if (day == "Wednesday") {
                return listOf("Center 13", "Center 15")
            }
            if (day == "Thursday") {
                return listOf("Center 14", "Center 15")
            }
            if (day == "Friday") {
                return listOf("Center 13", "Center 16")
            }
            if (day == "Saturday") {
                return listOf("center 15", "Center 16")
            } else {
                return listOf("Center 14", "Center 16")
            }
        }
    }
}

// Location intent
class Location(val city: City? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@city",
                "I'm in @city",
                "I would like to get tested in @city",
                "I can take the test in @city",
                "I live in @city",
                "I'm from @city"
        )
    }
}

// Availability intent
class Availability(val day: Day? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@day",
                "I can do it on @day",
                "I would like to test on @day",
                "I can test on @day",
                "I can do it on @day this week",
                "I would like test on @day this week",
                "I can test on @day this week",
                "I can do it on @day next week",
                "I would like test on @day next week",
                "I can test on @day next week"
        )
    }
}

// Direction intent
class Show_direction(val center: Centers? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@center",
                "I'd rather go to @center",
                "I would like to get tested in @center",
                "I can test in @center",
                "I can do it in @center",
                "I would like to take the test in @center"
        )
    }
}

class Test : EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("PCR: PCR, infection",
                "antibody: antibody, immunity"
        )
    }
}

class Person(var person: String? = null) : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("contact: father, mom, dad, mother, sister, brother, parents, family, neighbor, colleague, partner, husband, wife, friend", "girlfriend", "boyfriend", "family",
                "@person"
        )
    }
}

class ListOfSymptom : ListEntity<Symptom>()

class HealthCondition : EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("feel well: healthy, no symptoms, fine, well, asymptomatic")
    }
}

class Symptom : EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("head ache: head ache, headache, pain in head",
                "sore throat: sore throat, dry throat, throat pain",
                "fever: fever, sick, high temperature, unwell, illness, ill",
                "cough: cough, dry cough",
                "difficulty breathing: difficulty breathing, hard to breathe, can't breathe",
                "fatigue: fatigue, tired, bad, feel bad, nausea, pain",
                "no symptoms: no symptoms, feel well, healthy, fine, well, asymptomatic"
        )
    }
}

class Timeunit : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("a day: a day, yesterday, last night, today",
                "a week: a week, last week, last friday, this monday", "month", "day", "week"
        )
    }
}

class Duration(var count: Number? = null,
        var timeunit: Timeunit? = null) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("@count @timeunit",
                "@timeunit"
        )
    }

    override fun toText(): String {
        if (count == null) {
            if (timeunit?.value == "a day") {
                count = Number(1)
                timeunit?.value = "day"
            }
            if (timeunit?.value == "a week") {
                count = Number(1)
                timeunit?.value = "week"
            }
        }
        return generate("$count " + if (count?.value == 1) timeunit?.value else "${timeunit?.value}" + "s")
    }
}

class Covid : EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("COVID-19: covid, covid-19, covid 19, corona, corona virus, coronavirus, virus, SARS COV 2")
    }
}

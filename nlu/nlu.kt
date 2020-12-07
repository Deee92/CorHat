package furhatos.app.corhat.nlu
import cc.mallet.types.NullLabel
import furhatos.nlu.TextGenerator
import furhatos.util.Language
import furhatos.nlu.*
import furhatos.nlu.common.*
import furhatos.nlu.common.Number
import furhatos.records.GenericRecord

//start - testing facilities and directions////////////////////////////////////////////////////////////////////////////////////////////

// Our City entity
class City : EnumEntity(stemming = true, speechRecPhrases = true){
    override fun getEnum(lang: Language): List<String> {
        return listOf("Stockholm", "Linköping", "Gothenburg", "Uppsala")
    }
}
// Our Day entity.

class Day : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Monday", "Tuesday", "Wednesday", "Thursday",	"Friday", "Saturday", "Sunday")
    }
}

//Our Centers entity
class Centers : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        // Center 1-4 Gothenburg
        return listOf("Center 1","Center 2","Center 3","Center 4",
        // Center 5-8 Stockholm
                "Center 5", "Center 6", "Center 7", "Center 8",
        // Center 9-12 Linköping
                "Center 9", "Center 10", "Center 11", "Center 12",
        // Center 13-16 Uppsala
                "Center 13", "Center 14", "Center 15", "Center 16"
        )
    }
}

//Our Directions entity
class Center_Direction(val center : String ? = null) : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        print(center)
        if (center== "Center 1"){
            return listOf("Adress 1")
        }
        if (center== "Center 2"){
            return listOf("Address 2")
        }
        if (center== "Center 3"){
            return listOf("Address 3")
        }
        if (center== "Center 4"){
            return listOf("Address 4")
        }
        if (center== "Center 5"){
            return listOf("Address 5")
        }
        if (center== "Center 6"){
            return listOf("Address 6")
        }
        if (center== "Center 7"){
            return listOf("Address 7")
        }
        if (center== "Center 8"){
            return listOf("Address 8")
        }
        if (center== "Center 9"){
            return listOf("Address 9")
        }
        if (center== "Center 10"){
            return listOf("Address 10")
        }
        if (center== "Center 11"){
            return listOf("Address 11")
        }
        if (center== "Center 12"){
            return listOf("Address 12")
        }
        if (center== "Center 13"){
            return listOf("Address 13")
        }
        if (center== "Center 14"){
            return listOf("Adress 14")
        }
        if (center== "Center 15"){
            return listOf("Adress 15")
        }
        else{
            return listOf("Adress 16")
        }
    }

}

// Our Available Centers entity.
class Available_centers(val day : String ? = null, val city : String ? = null)  : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        print(day)
        if (city == "Gothenburg"){
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
        if (city == "Stockholm"){
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

        if (city == "Linkoping"){
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
        }
        else {
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

// Our Locaction intent
class Location(val city : City ? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@city",
                "Im in @city",
                "I live in @city",
                "Im from @city")
    }
}

// Our Availability intent
class Availability(val day : Day ? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@day",
                "I can do it on @day",
                "I would like test on @day",
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

//Our Direction intent
class Show_direction(val center : Centers ? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@center",
                "I'd rather to go to @center",
                "I would like test in @center",
                "I can test in @center",
                "I can do it in @center",
                "I would like test in @center"
        )
    }
}
//end - testing facilities and directions////////////////////////////////////////////////////////////////////////////////////////////


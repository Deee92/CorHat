package furhatos.app.corhat.nlu
import furhatos.nlu.TextGenerator
import furhatos.util.Language
import furhatos.nlu.*
import furhatos.nlu.common.*
import furhatos.nlu.common.Number
import furhatos.records.GenericRecord

//Javad - start - testing facilities and directions////////////////////////////////////////////////////////////////////////////////////////////
// Our Day entity.
class Day : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Monday", "Tuesday", "Wednesday", "Thursday",	"Friday", "Saturday", "Sunday")
    }
}

//Our Centers entity
class Centers : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Center 1","Center 2","Center 3","Center 4")
    }
}

//Our Directions entity
class Center_Direction(val center : String ? = null) : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        print(center)
        if (center== "Center 1"){
            return listOf("Address 1")
        }
        if (center== "Center 2"){
            return listOf("Address 2")
        }
        if (center== "Center 3"){
            return listOf("Address 3")
        }
        else {
            return listOf("Address 4")
        }
    }

}

// Our Available Centers entity.
class Available_centers(val day : String ? = null) : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        print(day)
        if (day== "Monday"){
            return listOf("Center 1", "center 2")
        }
        if (day== "Tuesday"){
            return listOf("Center 3", "center 4")
        }
        if (day== "Wednesday"){
            return listOf("Center 1", "center 3")
        }
        if (day== "Thursday"){
            return listOf("Center 2","Center 3")
        }
        if (day== "Friday"){
            return listOf("Center 1", "Center 4")
        }
        if (day== "Saturday"){
            return listOf("center 3", "Center 4")
        }
        else {
            return listOf("Center 2","Center 4")
        }

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
//Javad - end - testing facilities and directions////////////////////////////////////////////////////////////////////////////////////////////


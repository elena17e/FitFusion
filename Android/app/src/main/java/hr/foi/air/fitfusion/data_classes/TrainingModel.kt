package hr.foi.air.fitfusion.data_classes

@Suppress("PropertyName")
data class TrainingModel(
    val id: String?=null,
    var date: String?=null,
    var participants: String?=null,
    var state: String?=null,
    var time: String?=null,
    var time_end: String?=null,
    var type: String?=null,
    var trainerId: String?=null,
    var type_trainerId: String?=null,
    var date_time: String?=null
)
package hr.foi.air.fitfusion.data_classes

data class TrainerModel (
    var firstName: String?=null,
    var lastName: String?=null,
    var email: String?=null,
    var password: String?=null,
    var hashedPassword: String?=null,
    var salt: String?=null,
    var type: String?=null,
    var description: String?=null,
    var usId: String?=null
)
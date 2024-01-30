package hr.foi.air.fitfusion.data_classes

data class UserModel(
    var email: String?=null,
    var password: String?=null,
    var hashedPassword: String?=null,
    var salt: String?=null,
    var firstName: String?=null,
    var lastName: String?=null,
    var type: String?=null,
    var usId: String?=null
)
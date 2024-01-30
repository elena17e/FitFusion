package hr.foi.air.google_login.data_classes_google

data class UserModelGoogle(
    var email: String?=null,
    var password: String?=null,
    var hashedPassword: String?=null,
    var salt: String?=null,
    var firstName: String?=null,
    var lastName: String?=null,
    var type: String?=null,
    var usId: String?=null
)
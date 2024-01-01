package hr.foi.air.fitfusion.data_classes
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import hr.foi.air.fitfusion.adapters.ClassAdapter
import hr.foi.air.fitfusion.adapters.ClassAdapterCardio
import hr.foi.air.fitfusion.adapters.ClassAdapterYoga
import hr.foi.air.fitfusion.entities.ClassesCardio
import hr.foi.air.fitfusion.entities.ClassesStrength
import hr.foi.air.fitfusion.entities.ClassesYoga
import com.google.firebase.auth.FirebaseAuth
import hr.foi.air.fitfusion.adapters.ReplyAdapter
import hr.foi.air.fitfusion.entities.Reply
import java.security.MessageDigest
import java.security.SecureRandom

class FirebaseManager {

    private var firebaseDatabase: FirebaseDatabase
    private var databaseReference: DatabaseReference
    private val databaseRf: DatabaseReference

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user")
        databaseRf = FirebaseDatabase.getInstance().getReference("Training")
    }

    fun saveTrainingSession(
        time: String,
        date: String,
        participants: String,
        type: String,
        trainerId: String?,
        callback: (Boolean) -> Unit
    ) {

        if (type.isNotEmpty() && participants.isNotEmpty() && time.isNotEmpty() && date.isNotEmpty()) {
            val sessionId: String = databaseRf.push().key ?: ""

            val trainingSession = TrainingModel(
                id = sessionId,
                date = date,
                participants = participants,
                state = "active",
                time = time,
                userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                type = type,
                trainerId = trainerId,
                type_trainerId = type + "_" + trainerId
            )

            databaseRf.child(sessionId).setValue(trainingSession)
                .addOnSuccessListener {
                    callback(true)
                }
                .addOnFailureListener {
                    callback(false)
                }
        } else {
            callback(false)
        }
    }

    fun addTrainer(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        description: String,
        callback: (Boolean, String?) -> Unit
    ) {
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        callback(false, "User with this email already exists")
                    } else {
                        val usId = databaseReference.push().key!!

                        val salt = generateSalt()
                        val hashedPassword = hashPassword(password, salt)

                        val trainer = TrainerModel()
                        trainer.firstName = firstName
                        trainer.lastName = lastName
                        trainer.email = email
                        trainer.password = password
                        trainer.hashedPassword = hashedPassword
                        trainer.salt = salt
                        trainer.type = "trainer"
                        trainer.description = description
                        trainer.usId = usId

                        databaseReference.child(usId).setValue(trainer)

                        callback(true, "Trainer added successfully")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(false, "Error checking existing user")
                }
            })
    }

    private fun generateSalt(): String {
        val random = SecureRandom()
        val saltBytes = ByteArray(16)
        random.nextBytes(saltBytes)
        return saltBytes.joinToString("") { "%02x".format(it) }
    }

    private fun hashPassword(password: String, salt: String): String {
        val bytes = (password + salt).toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun loginUser(email: String, password: String, callback: (UserModel?, String?) -> Unit) {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user")

        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.P)
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userData = userSnapshot.getValue(UserModel::class.java)
                            if (userData != null && verifyPassword(
                                    password,
                                    userData.hashedPassword,
                                    userData.salt
                                )
                            ) {
                                callback(userData, null)
                            } else {
                                callback(null, "Invalid email or password")
                            }
                            return
                        }
                    } else {
                        callback(null, "Invalid email or password")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(null, databaseError.message)
                }
            })
    }

    private fun verifyPassword(
        enteredPassword: String,
        hashedPassword: String?,
        salt: String?
    ): Boolean {
        if (hashedPassword == null || salt == null) {
            return false
        }
        val enteredPasswordHash = hashPassword(enteredPassword, salt)
        return enteredPasswordHash == hashedPassword
    }

    fun getPostId(postTitle: String, callback: (String) -> Unit) {
        var id = ""
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Posts")

        val query = databaseReference.orderByChild("title").equalTo(postTitle)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (classSnapshot in snapshot.children) {
                        id = classSnapshot.child("id").getValue(String::class.java) ?: ""
                    }
                }

                callback(id)
            }

            override fun onCancelled(error: DatabaseError) {
                error.message
                callback("")
            }
        })
    }

    fun saveReplyToFirebase(
        content: String,
        timestamp: Long,
        authorFirstName: String?,
        authorLastName: String?,
        idPost: String
    ) {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Replies")
        val replyId = databaseReference.push().key
        val newPost =
            mapOf(
                "id" to replyId,
                "content" to content,
                "timestamp" to timestamp,
                "authorFirstName" to authorFirstName,
                "authorLastName" to authorLastName,
                "postId" to idPost
            )

        if (replyId != null) {
            databaseReference.child(replyId).setValue(newPost)
                .addOnSuccessListener {}
                .addOnFailureListener {}
        }
    }

    fun fetchReplyFromFirebase(postTitle: String, recyclerView: RecyclerView) {
        getPostId(postTitle) { postId ->
            firebaseDatabase = FirebaseDatabase.getInstance()
            databaseReference = firebaseDatabase.reference.child("Replies")
            val query = databaseReference.orderByChild("postId").equalTo(postId)

            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val replies: MutableList<Reply> = mutableListOf()

                    for (postSnapshot in dataSnapshot.children) {
                        val id = postSnapshot.child("id").getValue(String::class.java) ?: ""
                        val postIds =
                            postSnapshot.child("postId").getValue(String::class.java) ?: ""
                        val content =
                            postSnapshot.child("content").getValue(String::class.java) ?: ""
                        val timestamp =
                            postSnapshot.child("timestamp").getValue(Long::class.java) ?: 0
                        val authorFirstName =
                            postSnapshot.child("authorFirstName").getValue(String::class.java) ?: ""
                        val authorLastName =
                            postSnapshot.child("authorLastName").getValue(String::class.java) ?: ""


                        val reply =
                            Reply(id, postIds, content, timestamp, authorFirstName, authorLastName)
                        replies.add(reply)
                    }
                    val replyAdapter = ReplyAdapter(replies)
                    recyclerView.adapter = replyAdapter

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("ReplyActivity", "onCancelled: ${databaseError.message}")
                }
            })
        }
    }

        fun showTrainingsList(
            classRecyclerviewStrength: RecyclerView,
            classRecyclerviewCardio: RecyclerView,
            classRecyclerviewYoga: RecyclerView,
            classArrayListStrength: ArrayList<ClassesStrength>,
            classArrayListCardio: ArrayList<ClassesCardio>,
            classArrayListYoga: ArrayList<ClassesYoga>,
            context: Context
        ) {
            firebaseDatabase = FirebaseDatabase.getInstance()
            val loggedInUser = LoggedInUser(context)
            val trainerId = loggedInUser.getUserId()
            databaseReference = firebaseDatabase.reference.child("Training")
            if (trainerId != null) {
                val query =
                    databaseReference.orderByChild("type_trainerId").equalTo("Strength_$trainerId")

                query.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {

                            for (classSnapshot in snapshot.children) {

                                val classesStrength =
                                    classSnapshot.getValue(ClassesStrength::class.java)
                                classArrayListStrength.add(classesStrength!!)
                            }

                            classRecyclerviewStrength.adapter = ClassAdapter(classArrayListStrength)
                        }

                    }


                    override fun onCancelled(error: DatabaseError) {
                        error.message
                    }


                })


                val query2 =
                    databaseReference.orderByChild("type_trainerId").equalTo("Cardio_$trainerId")
                val query3 =
                    databaseReference.orderByChild("type_trainerId").equalTo("Yoga_$trainerId")
                query2.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {

                            for (classSnapshot in snapshot.children) {
                                val classesCardio =
                                    classSnapshot.getValue(ClassesCardio::class.java)
                                classArrayListCardio.add(classesCardio!!)
                            }

                            classRecyclerviewCardio.adapter =
                                ClassAdapterCardio(classArrayListCardio)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        error.message
                    }


                })

                query3.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {

                            for (classSnapshot in snapshot.children) {


                                val classesYoga = classSnapshot.getValue(ClassesYoga::class.java)
                                classArrayListYoga.add(classesYoga!!)

                            }

                            classRecyclerviewYoga.adapter = ClassAdapterYoga(classArrayListYoga)
                        }

                    }


                    override fun onCancelled(error: DatabaseError) {
                        error.message
                    }


                })

            }
        }
    }

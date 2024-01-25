package hr.foi.air.fitfusion.data_classes
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import hr.foi.air.fitfusion.WelcomeActivity
import hr.foi.air.fitfusion.adapters.ReplyAdapter
import hr.foi.air.fitfusion.adapters.TrainingHomepageAdapter
import hr.foi.air.fitfusion.entities.ClassesCardio
import hr.foi.air.fitfusion.entities.ClassesStrength
import hr.foi.air.fitfusion.entities.ClassesYoga
import hr.foi.air.fitfusion.entities.Event
import hr.foi.air.fitfusion.entities.Post
import hr.foi.air.fitfusion.entities.Reply
import hr.foi.air.fitfusion.entities.Training
import hr.foi.air.fitfusion.fragments.CardioDataListener
import hr.foi.air.fitfusion.fragments.StrengthDataListener
import hr.foi.air.fitfusion.fragments.YogaDataListener
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter



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
        timeEnd: String,
        date: String,
        participants: String,
        type: String,
        trainerId: String?,
        callback: (Boolean) -> Unit
    ) {

        if (type.isNotEmpty() && participants.isNotEmpty() && time.isNotEmpty() && date.isNotEmpty()) {
            val query = databaseRf.orderByChild("date_time")
                .startAt(date + "_" + time)
                .endAt(date + "_" + timeEnd)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        callback(false)
                    } else {
                        val sessionId: String = databaseRf.push().key ?: ""

                        val trainingSession = TrainingModel(
                            id = sessionId,
                            date = date,
                            participants = participants,
                            state = "active",
                            time = time,
                            type = type,
                            trainerId = trainerId,
                            type_trainerId = type + "_" + trainerId,
                            date_time = date + "_" + time + "_" + timeEnd,
                            time_end = timeEnd,
                            participantsId = emptyMap()
                        )

                        databaseRf.child(sessionId).setValue(trainingSession)
                            .addOnSuccessListener {
                                callback(true)
                            }
                            .addOnFailureListener {
                                callback(false)
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(false)
                }
            })
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
                @SuppressLint("ObsoleteSdkInt")
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

    fun showTrainingsList(
        classArrayListStrength: ArrayList<ClassesStrength>,
        classArrayListCardio: ArrayList<ClassesCardio>,
        classArrayListYoga: ArrayList<ClassesYoga>,
        context: Context,
        strengthDataListener: StrengthDataListener,
        cardioDataListener: CardioDataListener,
        yogaDataListener: YogaDataListener
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
                            if (classesStrength != null) {
                                classesStrength.sessionId = classSnapshot.key
                            }
                            classArrayListStrength.add(classesStrength!!)
                        }
                        strengthDataListener.onStrengthDataReceived(classArrayListStrength)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    error.message
                }
            })


            val query2 =
                databaseReference.orderByChild("type_trainerId").equalTo("Cardio_$trainerId")
            query2.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (classSnapshot in snapshot.children) {
                            val classesCardio = classSnapshot.getValue(ClassesCardio::class.java)
                            if (classesCardio != null) {
                                classesCardio.sessionId = classSnapshot.key
                            }
                            classArrayListCardio.add(classesCardio!!)
                        }
                        cardioDataListener.onCardioDataReceived(classArrayListCardio)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    error.message
                }
            })

            val query3 = databaseReference.orderByChild("type_trainerId").equalTo("Yoga_$trainerId")
            query3.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (classSnapshot in snapshot.children) {
                            val classesYoga = classSnapshot.getValue(ClassesYoga::class.java)

                            if (classesYoga != null) {
                                classesYoga.sessionId = classSnapshot.key
                            }
                            classArrayListYoga.add(classesYoga!!)
                        }
                        yogaDataListener.onYogaDataReceived(classArrayListYoga)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    error.message
                }
            })
        }
    }

    fun saveChangedPassword(
        email: String?,
        password: String,
        firstName: String?,
        lastName: String?,
        type: String?,
        userId: String?,
        context: Context
    ) {
        if (userId != null) {
            val referenca = firebaseDatabase.getReference("user").child(userId)

            val salt = generateSalt()
            val hashedPassword = hashPassword(password, salt)

            val user =
                UserModel(email, password, hashedPassword, salt, firstName, lastName, type, userId)

            referenca.setValue(user)
                .addOnCompleteListener {
                    val loggedInUser = LoggedInUser(context)
                    loggedInUser.saveUserData(userId, firstName, lastName, password, email, type)
                    Toast.makeText(
                        context,
                        "Password change complete",
                        Toast.LENGTH_LONG
                    ).show()
                }.addOnFailureListener { err ->
                    Toast.makeText(
                        context,
                        "Error ${err.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    private val database = FirebaseDatabase.getInstance()
    private val postRef = database.getReference("Posts")
    fun savePost(
        title: String,
        content: String,
        timestamp: Long,
        authorFirstName: String,
        authorLastName: String
    ) {
        val postId = postRef.push().key
        val newPost =
            mapOf(
                "id" to postId,
                "title" to title,
                "content" to content,
                "timestamp" to timestamp,
                "authorFirstName" to authorFirstName,
                "authorLastName" to authorLastName
            )

        if (postId != null) {
            postRef.child(postId).setValue(newPost)
                .addOnSuccessListener {}
                .addOnFailureListener {}
        }
    }

    fun fetchPosts(completion: (List<Post>) -> Unit) {
        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val posts: MutableList<Post> = mutableListOf()

                for (postSnapshot in dataSnapshot.children) {
                    val id = postSnapshot.child("id").getValue(String::class.java) ?: ""
                    val title = postSnapshot.child("title").getValue(String::class.java) ?: ""
                    val content = postSnapshot.child("content").getValue(String::class.java) ?: ""
                    val timestamp = postSnapshot.child("timestamp").getValue(Long::class.java) ?: 0
                    val authorFirstName =
                        postSnapshot.child("authorFirstName").getValue(String::class.java) ?: ""
                    val authorLastName =
                        postSnapshot.child("authorLastName").getValue(String::class.java) ?: ""


                    val post = Post(id, title, content, timestamp, authorFirstName, authorLastName)
                    posts.add(post)
                }

                completion(posts)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("ForumFragment", "onCancelled: ${databaseError.message}")
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

    fun updateTrainingSession(
        date: String,
        participants: String,
        time: String,
        timeEnd: String,
        type: String,
        state: String,
        sessionId: String,
        trainerId: String
    ) {
        val updatedData = TrainingModel(
            sessionId,
            date,
            participants,
            state,
            time,
            timeEnd,
            type,
            trainerId,
            type + "_" + trainerId
        )
        databaseRf.child(sessionId).setValue(updatedData)
    }

    fun fetchTrainingFromFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Training")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Event.eventsList.clear()
                for (trainingSnapshot in dataSnapshot.children) {
                    val trainingId =
                        trainingSnapshot.child("id").getValue(String::class.java) ?: ""
                    val trainingParticipants =
                        trainingSnapshot.child("participants").getValue(String::class.java) ?: ""
                    val trainingType =
                        trainingSnapshot.child("type").getValue(String::class.java) ?: ""
                    val trainingDate =
                        trainingSnapshot.child("date").getValue(String::class.java) ?: ""
                    val trainingTime =
                        trainingSnapshot.child("time").getValue(String::class.java) ?: ""
                    val trainingTimeEnd =
                        trainingSnapshot.child("time_end").getValue(String::class.java) ?: ""
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    val mydate = LocalDate.parse(trainingDate, formatter)
                    val mytime = LocalTime.parse(trainingTime)
                    val mytimeEnd = LocalTime.parse(trainingTimeEnd)
                    val newEvent = Event(
                        trainingType,
                        mydate,
                        mytime,
                        mytimeEnd,
                        trainingId,
                        trainingParticipants
                    )
                    Event.eventsList.add(newEvent)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("EventActivity", "onCancelled: ${databaseError.message}")
            }
        })
    }

    fun checkIfUserIsAlreadyApplied(
        context: Context,
        trainingId: String?,
        callback: (Boolean) -> Unit
    ) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("Training")

        val query = databaseReference.orderByChild("id").equalTo(trainingId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (trainingSnapshot in dataSnapshot.children) {
                    val loggedInUser = LoggedInUser(context)
                    val usId = loggedInUser.getUserId()
                    if (trainingSnapshot.child("participantsId").hasChild(usId!!)) {
                        callback.invoke(true)
                        return
                    }
                }
                callback.invoke(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("checkApplyForTraining", "Error querying Training: ${databaseError.message}")
                callback.invoke(false)
            }
        })
    }

    fun applyForTraining(context: Context, trainingId: String?) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("Training")

        val query = databaseReference.orderByChild("id").equalTo(trainingId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (trainingSnapshot in dataSnapshot.children) {
                    val participantsIdReference = trainingSnapshot.child("participantsId").ref
                    val loggedInUser = LoggedInUser(context)
                    val usId = loggedInUser.getUserId()
                    participantsIdReference.child(usId!!).setValue(usId)

                    var currentParticipantsCount =
                        trainingSnapshot.child("participants").getValue(String::class.java)?.toInt()
                            ?: 0
                    currentParticipantsCount--
                    currentParticipantsCount = maxOf(0, currentParticipantsCount)
                    trainingSnapshot.child("participants").ref.setValue(currentParticipantsCount.toString())

                    Toast.makeText(
                        context,
                        "Successfully applied for training!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("applyForTraining", "Error querying Training: ${databaseError.message}")
            }
        })
    }

    fun getTrainings(context: Context, trainingsRecycleView: RecyclerView) {
        val trainingsList = ArrayList<Training>()
        val loggedInUser = LoggedInUser(context)
        val userId = loggedInUser.getUserId()

        val dataQuery = database.getReference("Training")
        dataQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trainingsList.clear()
                for (trainingSnapshot in snapshot.children) {
                    val training = trainingSnapshot.getValue(Training::class.java)
                    if (training != null && userId in training.participantsId.orEmpty()) {
                        trainingsList.add(training)
                    }
                }
                trainingsRecycleView.adapter = TrainingHomepageAdapter(trainingsList) {
                    navigateToCalendarTab()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun navigateToCalendarTab() {
        val activity = WelcomeActivity()
        (activity as? WelcomeActivity)?.navigateToCalendarTab()
    }

    fun deleteTrainer(usId: String, callback: (Boolean) -> Unit){
        val dataQuery = database.getReference("user").child(usId)
        dataQuery.removeValue().addOnSuccessListener { callback(true) }.addOnFailureListener { callback(false) }
    }

}
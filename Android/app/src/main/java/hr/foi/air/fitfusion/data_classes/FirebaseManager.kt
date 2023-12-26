package hr.foi.air.fitfusion.data_classes
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
import hr.foi.air.fitfusion.entities.Post

class FirebaseManager {

    private var firebaseDatabase: FirebaseDatabase
    private var databaseReference: DatabaseReference

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user")
    }

    fun addTrainer(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        description: String,
        callback: (Boolean, String?) -> Unit
    ) {
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    callback(false, "User with this email already exists")
                } else {
                    val trainerId = databaseReference.push().key!!

                    val trainer = TrainerModel()
                    trainer.firstName = firstName
                    trainer.lastName = lastName
                    trainer.email = email
                    trainer.password = password
                    trainer.type = "trainer"
                    trainer.description = description
                    trainer.trainerId = trainerId

                    databaseReference.child(trainerId).setValue(trainer)

                    callback(true, "Trainer added successfully")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false, "Error checking existing user")
            }
        })
    }

    fun loginUser(email: String, password: String, callback: (UserModel?, String?) -> Unit) {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user")

        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
           @RequiresApi(Build.VERSION_CODES.P)
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for (userSnapshot in snapshot.children){
                        val userData = userSnapshot.getValue(UserModel::class.java)
                        callback(userData, null)
                        return
                    }
                } else {
                    callback(null, null)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null, databaseError.message)
            }
        })
    }

    fun showTrainingsList (classRecyclerviewStrength : RecyclerView, classRecyclerviewCardio : RecyclerView, classRecyclerviewYoga : RecyclerView, classArrayListStrength : ArrayList<ClassesStrength>, classArrayListCardio : ArrayList<ClassesCardio>, classArrayListYoga : ArrayList<ClassesYoga>){
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Training")
        val query = databaseReference.orderByChild("type").equalTo("Strength")
        val query2 = databaseReference.orderByChild("type").equalTo("Cardio")
        val query3 = databaseReference.orderByChild("type").equalTo("Yoga")

        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (classSnapshot in snapshot.children){

                        val classesStrength = classSnapshot.getValue(ClassesStrength::class.java)
                        classArrayListStrength.add(classesStrength!!)
                    }

                    classRecyclerviewStrength.adapter = ClassAdapter(classArrayListStrength)
                }

            }


            override fun onCancelled(error: DatabaseError) {
               error.message
            }


        })
        query2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (classSnapshot in snapshot.children){
                        val classesCardio = classSnapshot.getValue(ClassesCardio::class.java)
                        classArrayListCardio.add(classesCardio!!)
                    }

                    classRecyclerviewCardio.adapter = ClassAdapterCardio(classArrayListCardio)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                error.message
            }


        })

        query3.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (classSnapshot in snapshot.children){


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

    private val database = FirebaseDatabase.getInstance()
    private val postRef = database.getReference("Posts")
    fun savePost(title: String, content: String, timestamp: Long){
        val postId = postRef.push().key
        val newPost = postId?.let {
            mapOf(
                "title" to title,
                "content" to content,
                //"author" to author, //autor nije dovršen
                "timestamp" to timestamp
            )
        }
        if (postId != null && newPost != null){
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
                    val title = postSnapshot.child("title").getValue(String::class.java) ?: ""
                    val content = postSnapshot.child("content").getValue(String::class.java) ?: ""
                    val timestamp = postSnapshot.child("timestamp").getValue(Long::class.java) ?: 0

                    val post = Post(title, content, timestamp)
                    posts.add(post)
                }
                completion(posts)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("FirebaseManager", "onCancelled: ${databaseError.message}")
            }
        })
    }
}
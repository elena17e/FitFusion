package hr.foi.air.fitfusion.data_classes
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import hr.foi.air.fitfusion.adapters.ClassAdapter
import hr.foi.air.fitfusion.adapters.ClassAdapterCardio
import hr.foi.air.fitfusion.adapters.ClassAdapterYoga
import hr.foi.air.fitfusion.entities.ClassesCardio
import hr.foi.air.fitfusion.entities.ClassesStrength
import hr.foi.air.fitfusion.entities.ClassesYoga

class FirebaseManager {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

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
}
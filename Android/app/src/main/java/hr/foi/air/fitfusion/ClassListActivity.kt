package hr.foi.air.fitfusion


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.foi.air.fitfusion.adapters.ClassAdapter
import hr.foi.air.fitfusion.entities.Classes


class ClassListActivity : ComponentActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var classRecyclerview : RecyclerView
    private lateinit var classArrayList : ArrayList<Classes>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_trainer_home)

        classRecyclerview = findViewById(R.id.rvClassList)
        classRecyclerview.layoutManager = LinearLayoutManager(this)
        classRecyclerview.setHasFixedSize(true)

        classArrayList = arrayListOf<Classes>()
        getClassData()

    }

    private fun getClassData() {

        dbref = FirebaseDatabase.getInstance().getReference("Training")

        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (classSnapshot in snapshot.children){


                        val classes = classSnapshot.getValue(Classes::class.java)
                        classArrayList.add(classes!!)

                    }

                    classRecyclerview.adapter = ClassAdapter(classArrayList)


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
}
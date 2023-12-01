package hr.foi.air.fitfusion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.foi.air.fitfusion.adapters.ClassAdapter
import hr.foi.air.fitfusion.entities.ClassesStrength
import hr.foi.air.fitfusion.adapters.ClassAdapterCardio
import hr.foi.air.fitfusion.entities.ClassesCardio
import hr.foi.air.fitfusion.adapters.ClassAdapterYoga
import hr.foi.air.fitfusion.entities.ClassesYoga

class ClassListActivity : ComponentActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var classRecyclerviewStrength : RecyclerView
    private lateinit var classRecyclerviewCardio : RecyclerView
    private lateinit var classRecyclerviewYoga : RecyclerView
    private lateinit var classArrayListStrength : ArrayList<ClassesStrength>
    private lateinit var classArrayListCardio : ArrayList<ClassesCardio>
    private lateinit var classArrayListYoga : ArrayList<ClassesYoga>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_trainer_home)

        classRecyclerviewStrength = findViewById(R.id.rvClassListStrength)
        classRecyclerviewStrength.layoutManager = LinearLayoutManager(this)
        classRecyclerviewStrength.setHasFixedSize(true)

        classRecyclerviewCardio = findViewById(R.id.rvClassListCardio)
        classRecyclerviewCardio.layoutManager = LinearLayoutManager(this)
        classRecyclerviewCardio.setHasFixedSize(true)

        classRecyclerviewYoga = findViewById(R.id.rvClassListYoga)
        classRecyclerviewYoga.layoutManager = LinearLayoutManager(this)
        classRecyclerviewYoga.setHasFixedSize(true)

        classArrayListStrength = arrayListOf()
        classArrayListCardio = arrayListOf()
        classArrayListYoga = arrayListOf()
        getClassData()

    }

     private fun getClassData() {

        dbref = FirebaseDatabase.getInstance().getReference("Training")

        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (classSnapshot in snapshot.children){


                        val classesStrength = classSnapshot.getValue(ClassesStrength::class.java)
                        classArrayListStrength.add(classesStrength!!)

                        val classesCardio= classSnapshot.getValue(ClassesCardio::class.java)
                        classArrayListCardio.add(classesCardio!!)

                        val classesYoga = classSnapshot.getValue(ClassesYoga::class.java)
                        classArrayListYoga.add(classesYoga!!)

                    }

                    classRecyclerviewStrength.adapter = ClassAdapter(classArrayListStrength)
                    classRecyclerviewCardio.adapter = ClassAdapterCardio(classArrayListCardio)
                    classRecyclerviewYoga.adapter = ClassAdapterYoga(classArrayListYoga)


                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ClassListActivity,"Unable to read the data!", Toast.LENGTH_SHORT).show()
            }


        })

    }
}
package hr.foi.air.fitfusion.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.adapters.ClassAdapter
import hr.foi.air.fitfusion.adapters.ClassAdapterCardio
import hr.foi.air.fitfusion.adapters.ClassAdapterYoga
import hr.foi.air.fitfusion.entities.ClassesCardio
import hr.foi.air.fitfusion.entities.ClassesStrength
import hr.foi.air.fitfusion.entities.ClassesYoga


class HomeTrainerFragment : Fragment() {
    private lateinit var dbref : DatabaseReference
    private lateinit var classRecyclerviewStrength : RecyclerView
    private lateinit var classRecyclerviewCardio : RecyclerView
    private lateinit var classRecyclerviewYoga : RecyclerView
    private lateinit var classArrayListStrength : ArrayList<ClassesStrength>
    private lateinit var classArrayListCardio : ArrayList<ClassesCardio>
    private lateinit var classArrayListYoga : ArrayList<ClassesYoga>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trainer_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        classRecyclerviewStrength = view.findViewById(R.id.rvClassListStrength)
        classRecyclerviewStrength.layoutManager = LinearLayoutManager(view.context)
        classRecyclerviewStrength.setHasFixedSize(true)

        classRecyclerviewCardio = view.findViewById(R.id.rvClassListCardio)
        classRecyclerviewCardio.layoutManager = LinearLayoutManager(view.context)
        classRecyclerviewCardio.setHasFixedSize(true)

        classRecyclerviewYoga = view.findViewById(R.id.rvClassListYoga)
        classRecyclerviewYoga.layoutManager = LinearLayoutManager(view.context)
        classRecyclerviewYoga.setHasFixedSize(true)

        classArrayListStrength = arrayListOf()
        classArrayListCardio = arrayListOf()
        classArrayListYoga= arrayListOf()

        getClassData()

    }
    private fun getClassData() {

        dbref = FirebaseDatabase.getInstance().getReference("Training")
        val query = dbref.orderByChild("type").equalTo("Strength")
        val query2 = dbref.orderByChild("type").equalTo("Cardio")
        val query3 = dbref.orderByChild("type").equalTo("Yoga")

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
                TODO("Not yet implemented")
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
                TODO("Not yet implemented")
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
                TODO("Not yet implemented")
            }


        })

    }
}

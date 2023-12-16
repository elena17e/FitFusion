package hr.foi.air.fitfusion.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.adapters.TrainerAdapter
import hr.foi.air.fitfusion.entities.Trainer
import hr.foi.air.fitfusion.TrainerActivity

class AdminFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var trainersArrayList: ArrayList<Trainer>
    private lateinit var btnNewTrainer: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btnNewTrainer = view.findViewById(R.id.NewTrainer)
        btnNewTrainer.setOnClickListener {
            val intent = Intent(activity, TrainerActivity::class.java)
            startActivity(intent)
        }

        recyclerView = view.findViewById(R.id.trainers)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        database = FirebaseDatabase.getInstance().getReference("Trainers")

        trainersArrayList = arrayListOf<Trainer>()
        var dataQuery = database.orderByChild("email")

        dataQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val userData = userSnapshot.getValue(Trainer::class.java)

                        if (userData != null) {
                            trainersArrayList.add(userData)
                        }
                    }
                    recyclerView.adapter = TrainerAdapter(trainersArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
package hr.foi.air.fitfusion.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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
import hr.foi.air.fitfusion.TrainerActivity
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.UserModel

class AdminFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var trainersArrayList: ArrayList<UserModel>
    private lateinit var btnNewTrainer: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    @Suppress("CanBeVal", "RemoveExplicitTypeArguments")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val firebaseManager = FirebaseManager()
        btnNewTrainer = view.findViewById(R.id.NewTrainer)
        btnNewTrainer.setOnClickListener {
            val intent = Intent(activity, TrainerActivity::class.java)
            startActivity(intent)
        }

        recyclerView = view.findViewById(R.id.trainers)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        trainersArrayList = arrayListOf<UserModel>()
        database = FirebaseDatabase.getInstance().getReference("user")

        var dataQuery = database.orderByChild("type").equalTo("trainer")

        dataQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trainersArrayList.clear()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val userData = userSnapshot.getValue(UserModel::class.java)

                        if (userData != null) {
                            trainersArrayList.add(userData)
                        }
                    }
                    recyclerView.adapter = TrainerAdapter(trainersArrayList){trainer ->
                        trainer.usId?.let { trainerId ->
                            firebaseManager.deleteTrainer(trainerId){success ->
                                if(success){
                                    val index = trainersArrayList.indexOf(trainer)
                                    if (index != -1){
                                        trainersArrayList.removeAt(index)
                                        recyclerView.adapter?.notifyItemRemoved(index)
                                    Toast.makeText(context, "Trainer successfully removed!", Toast.LENGTH_SHORT).show()
                                        }
                                } else{
                                    Toast.makeText(context, "Failed to delete trainer!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}
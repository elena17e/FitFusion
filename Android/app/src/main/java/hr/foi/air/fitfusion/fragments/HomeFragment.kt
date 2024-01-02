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
import hr.foi.air.fitfusion.adapters.TrainerHomepageAdapter
import hr.foi.air.fitfusion.entities.Trainer
import android.content.Intent
import hr.foi.air.fitfusion.TrainerDetailsActivity


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var trainersArrayList: ArrayList<Trainer>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.trainers_homepage)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        trainersArrayList = arrayListOf<Trainer>()
        database = FirebaseDatabase.getInstance().getReference("user")

        var dataQuery = database.orderByChild("type").equalTo("trainer")

        dataQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val userData = userSnapshot.getValue(Trainer::class.java)

                        if (userData != null) {
                            trainersArrayList.add(userData)
                        }
                    }
                    recyclerView.adapter = TrainerHomepageAdapter(trainersArrayList) { clickedTrainer ->
                        val intent =
                            Intent(requireContext(), TrainerDetailsActivity::class.java).apply {
                                putExtra("firstName", clickedTrainer.FirstName)
                                putExtra("lastName", clickedTrainer.LastName)
                                putExtra("email", clickedTrainer.Email)
                                putExtra("description", clickedTrainer.Description)
                            }
                        startActivity(intent)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
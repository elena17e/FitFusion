package hr.foi.air.fitfusion.fragments

import android.content.Context
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
import android.widget.ImageButton
import android.widget.Toast
import hr.foi.air.fitfusion.TrainerDetailsActivity
import hr.foi.air.fitfusion.WelcomeActivity
import hr.foi.air.fitfusion.adapters.TrainerAdapter
import hr.foi.air.fitfusion.adapters.TrainingHomepageAdapter
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.LoggedInUser
import hr.foi.air.fitfusion.data_classes.TrainingModel
import hr.foi.air.fitfusion.data_classes.UserModel

@Suppress("RemoveExplicitTypeArguments")
class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var trainersArrayList: ArrayList<Trainer>
    private lateinit var trainingsArrayListModel: ArrayList<TrainingModel>

    private lateinit var trainingsRecycleView: RecyclerView
    private val firebaseManager = FirebaseManager()
    private lateinit var loggedInUser: LoggedInUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @Suppress("CanBeVal")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loggedInUser = LoggedInUser(requireContext())
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
                                putExtra("firstName", clickedTrainer.firstName)
                                putExtra("lastName", clickedTrainer.lastName)
                                putExtra("email", clickedTrainer.email)
                                putExtra("description", clickedTrainer.description)
                            }
                        startActivity(intent)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



        trainingsRecycleView = view.findViewById(R.id.trainings_homepage)
        trainingsRecycleView.layoutManager = LinearLayoutManager(context)

        trainingsArrayListModel = arrayListOf()
        firebaseManager.getTrainings(requireContext(), trainingsRecycleView)
        trainingsRecycleView.adapter = TrainingHomepageAdapter(trainingsArrayListModel, {

        }) { trainingModel ->
            firebaseManager.removeParticipant(trainingModel, requireContext())
        }






        val addButton = view.findViewById<ImageButton>(R.id.addTraining)
        addButton.setOnClickListener{
            navigateToCalendarTab()
        }
    }

    private fun navigateToCalendarTab(){
        (activity as? WelcomeActivity)?.navigateToCalendarTab()
    }
}
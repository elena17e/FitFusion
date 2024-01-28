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
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.TrainerActivity
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.UserModel
import hr.foi.air.fitfusion.adapters.TrainerAdapter

class AdminFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnNewTrainer: Button
    private lateinit var adapter: TrainerAdapter
    private lateinit var trainersList: ArrayList<UserModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firebaseManager = FirebaseManager()
        btnNewTrainer = view.findViewById(R.id.NewTrainer)
        btnNewTrainer.setOnClickListener {
            val intent = Intent(activity, TrainerActivity::class.java)
            startActivity(intent)
        }

        recyclerView = view.findViewById(R.id.trainers)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        trainersList = ArrayList()
        adapter = TrainerAdapter(trainersList) { trainer ->
            firebaseManager.deleteTrainer(trainer.usId!!) { success ->
                if (success) {
                    trainersList.remove(trainer)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(context, "Trainer successfully removed!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to delete trainer!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        recyclerView.adapter = adapter

        firebaseManager.fetchTrainers(requireContext()) { trainers ->
            trainersList.clear()
            trainersList.addAll(trainers)
            adapter.notifyDataSetChanged()
        }
    }
}

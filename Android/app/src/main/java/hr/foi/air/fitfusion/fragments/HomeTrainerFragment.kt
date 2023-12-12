package hr.foi.air.fitfusion.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.entities.ClassesCardio
import hr.foi.air.fitfusion.entities.ClassesStrength
import hr.foi.air.fitfusion.entities.ClassesYoga
import android.content.Intent
import android.widget.Button
import hr.foi.air.fitfusion.TrainingSessionActivity



class HomeTrainerFragment : Fragment() {
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var classRecyclerviewStrength : RecyclerView
    private lateinit var classRecyclerviewCardio : RecyclerView
    private lateinit var classRecyclerviewYoga : RecyclerView
    private lateinit var classArrayListStrength : ArrayList<ClassesStrength>
    private lateinit var classArrayListCardio : ArrayList<ClassesCardio>
    private lateinit var classArrayListYoga : ArrayList<ClassesYoga>
    private lateinit var btnNewClass : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trainer_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firebaseManager = FirebaseManager()
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

        btnNewClass = view.findViewById(R.id.btnNewClass)
        btnNewClass.setOnClickListener {
            val intent = Intent(activity, TrainingSessionActivity::class.java)
            startActivity(intent)
        }

        showTrainingsList()
    }

    private fun showTrainingsList() {
        firebaseManager.showTrainingsList(
            classRecyclerviewStrength,
            classRecyclerviewCardio,
            classRecyclerviewYoga,
            classArrayListStrength,
            classArrayListCardio,
            classArrayListYoga
        )
    }

}

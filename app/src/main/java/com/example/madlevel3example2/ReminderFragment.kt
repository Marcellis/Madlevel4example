package com.example.madlevel3example2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel3example2.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */



class ReminderFragment : Fragment() {

    private lateinit var reminderRepository: ReminderRepository


    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val reminders = arrayListOf<Reminder>()
    private val reminderAdapter = RemindersAdapter(reminders)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initViews()
        observeAddReminderResult()

        reminderRepository = ReminderRepository(requireContext())
        getRemindersFromDatabase()
        //binding.buttonFirst.setOnClickListener {
        //    findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        // }
    }


    private fun observeAddReminderResult() {
        setFragmentResultListener(REQ_REMINDER_KEY) { _, bundle ->
            bundle.getString(BUNDLE_REMINDER_KEY)?.let {
                val reminder = Reminder(it)

                reminderRepository.insertReminder(reminder)
                getRemindersFromDatabase()

            } ?: Log.e("ReminderFragment", "Request triggered, but empty reminder text!")
        }
    }


    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        binding.rvReminders.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvReminders.adapter = reminderAdapter
    }

    private fun getRemindersFromDatabase() {
        val reminders = reminderRepository.getAllReminders()
        this@ReminderFragment.reminders.clear()
        this@ReminderFragment.reminders.addAll(reminders)
        reminderAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
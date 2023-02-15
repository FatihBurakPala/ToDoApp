package com.android.todoapp.ui.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.android.todoapp.R
import com.android.todoapp.databinding.FragmentAddBinding
import com.android.todoapp.model.ToDoModel
import com.android.todoapp.viewmodel.SharedViewModel
import com.android.todoapp.viewmodel.ToDoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val todoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentAddBinding.inflate(inflater, container, false)

        binding.prioritiesSpinner.onItemSelectedListener = sharedViewModel.listener

        return binding.root
    }

    /* --------------------------------------- MENU PART --------------------------------------- */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_add) {
                    insertDataToDatabase()
                }
                else if (menuItem.itemId == android.R.id.home) {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /* -------------------------------------- INSERT DATA -------------------------------------- */
    private fun insertDataToDatabase() {
        binding.apply {

            val title = titleEt.text.toString()
            val priority = prioritiesSpinner.selectedItem.toString()
            val description = descriptionEt.text.toString()

            if (title.isNotEmpty() || description.isNotEmpty()) {

                val newData = ToDoModel(
                    0,
                    title,
                    sharedViewModel.parsePriority(priority),
                    description
                )
                todoViewModel.insertData(newData)

                Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addFragment_to_listFragment)
            }
            else {
                Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

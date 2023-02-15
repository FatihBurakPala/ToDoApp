package com.android.todoapp.ui.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.todoapp.R
import com.android.todoapp.databinding.FragmentUpdateBinding
import com.android.todoapp.model.ToDoModel
import com.android.todoapp.viewmodel.SharedViewModel
import com.android.todoapp.viewmodel.ToDoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateFragmentArgs by navArgs()
    private val todoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        binding.apply {

            currentTitleEt.setText(args.currentItem.title)
            currentDescriptionEt.setText(args.currentItem.description)
            currentPrioritiesSpinner.setSelection(sharedViewModel.parsePriorityToInt(args.currentItem.priority))

            currentPrioritiesSpinner.onItemSelectedListener = sharedViewModel.listener
        }
        return binding.root
    }

    /* --------------------------------------- MENU PART --------------------------------------- */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_save -> updateItem()
                    R.id.menu_delete -> confirmItemDelete()
                    android.R.id.home -> requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /* -------------------------------------- UPDATE DATA -------------------------------------- */
    private fun updateItem() {
        binding.apply {

            val title = currentTitleEt.text.toString()
            val description = currentDescriptionEt.text.toString()
            val priority = currentPrioritiesSpinner.selectedItem.toString()

            if (title.isNotEmpty() || description.isNotEmpty()) {

                val updatedItem = ToDoModel(
                    args.currentItem.id,
                    title,
                    sharedViewModel.parsePriority(priority),
                    description
                )
                todoViewModel.updateData(updatedItem)

                Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            else {
                Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /* ---------------------------------- CONFIRM DELETE ITEM ---------------------------------- */
    private fun confirmItemDelete() {
        val builder = AlertDialog.Builder(requireContext())

        builder.apply {
            setPositiveButton("Yes") { _, _ ->

                todoViewModel.deleteItem(args.currentItem)

                Toast.makeText(
                    requireContext(),
                    "Successfully Removed: ${args.currentItem.title}",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            setNegativeButton("No") { _, _ -> }
            setTitle("Delete '${args.currentItem.title}'?")
            setMessage("Are you sure you want to remove '${args.currentItem.title}'?")
            create().show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

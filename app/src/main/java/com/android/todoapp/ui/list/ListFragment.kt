package com.android.todoapp.ui.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.todoapp.R
import com.android.todoapp.databinding.FragmentListBinding
import com.android.todoapp.model.ToDoModel
import com.android.todoapp.ui.list.adapter.ListAdapter
import com.android.todoapp.utils.hideKeyboard
import com.android.todoapp.viewmodel.SharedViewModel
import com.android.todoapp.viewmodel.ToDoViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val todoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private val listAdapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = this@ListFragment
            xmlSharedViewModel = sharedViewModel
        }
        setupRecyclerView()
        hideKeyboard(requireActivity())

        /* -------------- Observe Data and Send to Adapter -------------- */
        lifecycleScope.launchWhenStarted {
            todoViewModel.getAllData.collect { data ->
                sharedViewModel.checkIfDatabaseEmpty(data)
                listAdapter.setData(data)
                binding.recyclerView.scheduleLayoutAnimation()
            }
        }
//        todoViewModel.getAllData.observe(viewLifecycleOwner) { data ->
//            sharedViewModel.checkIfDatabaseEmpty(data)
//            listAdapter.setData(data)
//            binding.recyclerView.scheduleLayoutAnimation()
//        }

        return binding.root
    }

    /* --------------------------------------- MENU PART --------------------------------------- */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)

                val searchView = menu.findItem(R.id.menu_search).actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@ListFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {

                    R.id.menu_delete_all -> confirmDeleteAll()

                    R.id.menu_priority_high -> lifecycleScope.launchWhenCreated {
                        todoViewModel.sortByHighPriority.collect { listAdapter.setData(it) }
                    }

                    R.id.menu_priority_low -> lifecycleScope.launchWhenCreated {
                        todoViewModel.sortByLowPriority.collect { listAdapter.setData(it) }
                    }

                    android.R.id.home -> requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /* ----------------------------------- RECYCLERVIEW SETUP ----------------------------------- */
    private fun setupRecyclerView() {

        binding.recyclerView.apply {
            adapter = listAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            swipeToDelete(this)
        }
    }

    /* ----------------------------------- SWIPE TO DELETE ----------------------------------- */
    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = listAdapter.dataList[viewHolder.adapterPosition]

                todoViewModel.deleteItem(deletedItem)

                listAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                restoreDeletedData(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoModel) {
        val snackBar = Snackbar.make(
            view,
            "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            todoViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }

    /* -------------------------------------- SEARCH PART -------------------------------------- */
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }
    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }
    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        lifecycleScope.launchWhenStarted {
            todoViewModel.searchDatabase(searchQuery).collect { list ->
                list.let { listAdapter.setData(it) }
            }
        }
    }

    /* ----------------------------------- CONFIRM DELETE ALL ----------------------------------- */
    private fun confirmDeleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setPositiveButton("Yes") { _, _ ->

                todoViewModel.deleteAll()

                Toast.makeText(
                    requireContext(),
                    "Successfully Removed Everything!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            setNegativeButton("No") { _, _ -> }
            setTitle("Delete everything?")
            setMessage("Are you sure you want to remove everything?")
            create().show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

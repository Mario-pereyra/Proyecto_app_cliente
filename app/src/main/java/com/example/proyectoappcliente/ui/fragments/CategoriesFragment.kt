package com.example.proyectoappcliente.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoappcliente.R
import com.example.proyectoappcliente.data.model.Categoria
import com.example.proyectoappcliente.databinding.FragmentCategoriesBinding
import com.example.proyectoappcliente.ui.activities.MainActivity
import com.example.proyectoappcliente.ui.adapters.CategoriaAdapter
import com.example.proyectoappcliente.ui.viewmodels.CategoriesViewModel
import com.example.proyectoappcliente.ui.viewmodels.factories.ViewModelFactory

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoriesViewModel by viewModels {
        ViewModelFactory(MainActivity.repository)
    }

    private lateinit var categoriaAdapter: CategoriaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true) // Habilita el menú en este fragmento
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        setupObservers()
        setupEventListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        categoriaAdapter = CategoriaAdapter(emptyList()) { categoria ->
            val action = CategoriesFragmentDirections.actionCategoriesFragmentToWorkersFragment(categoria.id)
            findNavController().navigate(action)
        }
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = categoriaAdapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterCategories(newText.orEmpty())
                return true
            }
        })
    }

    private fun setupObservers() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoriaAdapter.updateData(categories)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }

        viewModel.logoutStatus.observe(viewLifecycleOwner) { isLoggedOut ->
            if (isLoggedOut) {
                // Navegar de vuelta al login
                // La acción se modificará más adelante para limpiar el backstack
                findNavController().navigate(R.id.loginFragment)
            }
        }
    }

    private fun setupEventListeners() {
        binding.fabMisCitas.setOnClickListener {
            // Navegar a la pantalla de "Mis Citas"
            findNavController().navigate(R.id.action_categoriesFragment_to_appointmentsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
